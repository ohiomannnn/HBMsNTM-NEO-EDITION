package com.hbm.blocks;

import com.hbm.blockentity.IPersistentNBT;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.interfaces.ICopiable;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class DummyableBlock extends BaseEntityBlock implements ICustomBlockHighlight, ICopiable, INBTBlockTransformable {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());
    public static final EnumProperty<DummyBlockType> TYPE = EnumProperty.create("type", DummyBlockType.class);

    public static boolean safeRem = false;

    public DummyableBlock(Properties properties) {
        super(properties.randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TYPE, DummyBlockType.CORE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE);
    }

    /**
     * Creates a dummy block state pointing towards the given direction
     */
    public BlockState createDummyState(Direction pointingTo) {
        return this.defaultBlockState().setValue(FACING, pointingTo).setValue(TYPE, DummyBlockType.DUMMY);
    }

    /**
     * Creates an extra (flagged dummy) block state
     */
    public BlockState createExtraState(Direction pointingTo) {
        return this.defaultBlockState().setValue(FACING, pointingTo).setValue(TYPE, DummyBlockType.EXTRA);
    }

    /**
     * Creates a core block state with the given facing direction
     */
    public BlockState createCoreState(Direction facing) {
        return this.defaultBlockState().setValue(FACING, facing).setValue(TYPE, DummyBlockType.CORE);
    }

    /**
     * Checks if this block state represents the core of the multiblock
     */
    public static boolean isCore(BlockState state) {
        return state.getValue(TYPE) == DummyBlockType.CORE;
    }

    /**
     * Checks if this block state has the extra flag
     */
    public static boolean hasExtra(BlockState state) {
        return state.getValue(TYPE) == DummyBlockType.EXTRA;
    }

    /**
     * Gets the direction this dummy block points to (towards core)
     */
    public static Direction getPointingDirection(BlockState state) {
        return state.getValue(FACING);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        if (safeRem) return;

        destroyIfOrphan(level, pos, state);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);

        destroyIfOrphan(level, pos, state);
    }

    private void destroyIfOrphan(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        DummyBlockType type = state.getValue(TYPE);

        // core blocks are never orphans
        if (type == DummyBlockType.CORE) return;

        Direction pointingDir = state.getValue(FACING).getOpposite();
        BlockPos neighborPos = pos.relative(pointingDir);
        Block neighborBlock = level.getBlockState(neighborPos).getBlock();

        // An extra precaution against multiblocks on chunk borders being erroneously deleted.
        // Technically, this might be used to persist ghost dummy blocks by manipulating
        // loaded chunks and block destruction, but this gives no benefit to the player,
        // cannot be done accidentally, and is definitely preferable to multiblocks
        // just vanishing when their chunks are unloaded in an unlucky way.
        if (neighborBlock != this && level.hasChunksAt(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            if (isLegacyMonoblock(level, pos, state)) {
                fixLegacyMonoblock(level, pos, state);
            } else {
                level.removeBlock(pos, false);
            }
        }
    }

    /**
     * Override this when turning a single block into a pseudo-multiblock.
     * If this returns true, instead of being deleted as an orphan, the block
     * will be promoted to a core of a dummyable, however without any dummies.
     * This is only called if the block is presumed an orphan, so you don't
     * need to check that here.
     */
    protected boolean isLegacyMonoblock(Level level, BlockPos pos, BlockState state) {
        return false;
    }

    protected void fixLegacyMonoblock(Level level, BlockPos pos, BlockState state) {
        // Promote to a lone core block with the same effective rotation as before the change
        level.setBlock(pos, createCoreState(state.getValue(FACING)), 3);
    }

    List<BlockPos> positions = new ArrayList<>();

    @Nullable
    public BlockPos findCore(Level level, BlockPos pos) {
        positions.clear();
        return findCoreRecursive(level, pos);
    }

    @Nullable
    private BlockPos findCoreRecursive(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() != this) {
            return null;
        }

        if (isCore(state)) {
            return pos;
        }

        if (positions.contains(pos)) {
            return null;
        }

        positions.add(pos);

        Direction pointingDir = getPointingDirection(state).getOpposite();
        BlockPos nextPos = pos.relative(pointingDir);

        if (level.getBlockState(nextPos).getBlock() != this) {
            return null;
        }

        return findCoreRecursive(level, nextPos);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!(placer instanceof Player player)) return;

        safeRem = true;
        level.removeBlock(pos, false);
        safeRem = false;

        int rotation = Mth.floor((placer.getYRot() * 4.0F / 360.0F) + 0.5D) & 3;
        int offset = -getOffset();
        BlockPos adjustedPos = pos.above(getHeightOffset());

        Direction dir = switch (rotation) {
            case 1 -> Direction.EAST;
            case 2 -> Direction.SOUTH;
            case 3 -> Direction.WEST;
            default -> Direction.NORTH;
        };

        dir = getDirModified(dir);

        if (!checkRequirement(level, adjustedPos, dir, offset)) {
            if (!player.hasInfiniteMaterials()) {
                ItemStack mainHandStack = player.getMainHandItem();
                ItemStack offHandStack = player.getOffhandItem();
                Item item = this.asItem();

                boolean added = false;

                if (mainHandStack.is(item) && mainHandStack.getCount() < mainHandStack.getMaxStackSize()) {
                    mainHandStack.grow(1);
                    added = true;
                } else if (offHandStack.is(item) && offHandStack.getCount() < offHandStack.getMaxStackSize()) {
                    offHandStack.grow(1);
                    added = true;
                } else if (mainHandStack.isEmpty()) {
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(this));
                    added = true;
                } else if (offHandStack.isEmpty()) {
                    player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(this));
                    added = true;
                }

                if (!added) {
                    player.getInventory().add(new ItemStack(this));
                }
            }
            return;
        }

        if (!level.isClientSide) {
            BlockPos corePos = adjustedPos.relative(dir, offset);
            BlockState coreState = getStateForCore(level, corePos, player, dir);

            level.setBlock(corePos, coreState, 3);

            IPersistentNBT.restoreData(level, corePos, stack);

            fillSpace(level, adjustedPos, dir, offset);
        }

        level.scheduleTick(pos, this, 1);
        level.scheduleTick(pos, this, 2);

        super.setPlacedBy(level, pos, state, placer, stack);
    }

    /**
     * A bit more advanced than the dir modifier, but it is important that the resulting direction meta is in the core range.
     * Using the "extra" metas is technically possible but requires a bit of tinkering, e.g. preventing a recursive loop
     * in the core finder and making sure the TE uses the right metas.
     */
    protected BlockState getStateForCore(Level level, BlockPos pos, Player player, Direction dir) {
        return createCoreState(dir);
    }

    /**
     * Allows to modify the general placement direction as if the player had another rotation.
     * Quite basic due to only having 1 param but it's more meant to fix/limit the amount of directions
     */
    protected Direction getDirModified(Direction dir) {
        return dir;
    }

    protected boolean checkRequirement(Level level, BlockPos pos, Direction dir, int offset) {
        return MultiblockHandlerXR.checkSpace(level, pos.relative(dir, offset), getDimensions(), pos, dir);
    }

    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        MultiblockHandlerXR.fillSpace(level, pos.relative(dir, offset), getDimensions(), this, dir);
    }

    /**
     * "upgrades" regular dummy blocks to ones with the extra flag
     */
    public void makeExtra(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() != this) return;
        if (state.getValue(TYPE) != DummyBlockType.DUMMY) return;

        safeRem = true;
        level.setBlock(pos, state.setValue(TYPE, DummyBlockType.EXTRA), 3);
        safeRem = false;
    }

    public void removeExtra(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() != this) return;
        if (state.getValue(TYPE) != DummyBlockType.EXTRA) return;

        safeRem = true;
        level.setBlock(pos, state.setValue(TYPE, DummyBlockType.DUMMY), 3);
        safeRem = false;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {

            if (!safeRem) {
                Direction dir = state.getValue(FACING);
                BlockPos neighborPos = pos.relative(dir.getOpposite());

                if (level.getBlockState(neighborPos).getBlock() == this) {
                    level.removeBlock(neighborPos, false);
                }
            }

            // Drop inventory contents
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof Container container) {
                Containers.dropContents(level, pos, container);
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos corePos = this.findCore(level, pos);
        if (!player.isCreative()) {
            dropResources(state, level, corePos, level.getBlockEntity(corePos), player, player.getMainHandItem());
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    public List<AABB> bounding = new ArrayList<>();

    public boolean useDetailedHitbox() {
        return !bounding.isEmpty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (!this.useDetailedHitbox()) {
            return Shapes.block();
        }

        if (!(getter instanceof Level level)) {
            return Shapes.block();
        }

        BlockPos corePos = findCore(level, pos);
        if (corePos == null) {
            return Shapes.block();
        }

        BlockState coreState = getter.getBlockState(corePos);
        Direction facing = coreState.getValue(FACING);
        Direction rot = facing.getClockWise();

        VoxelShape combinedShape = Shapes.empty();
        Vec3 offset = Vec3.atLowerCornerOf(corePos.subtract(pos));

        for (AABB aabb : bounding) {
            AABB rotatedBox = getAABBRotationOffset(aabb, offset.x + 0.5, offset.y, offset.z + 0.5, rot);
            combinedShape = Shapes.or(combinedShape, Shapes.create(rotatedBox));
        }

        return combinedShape;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    public static AABB getAABBRotationOffset(AABB aabb, double x, double y, double z, Direction dir) {
        AABB newBox = switch (dir) {
            case NORTH -> new AABB(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
            case EAST -> new AABB(-aabb.maxZ, aabb.minY, aabb.minX, -aabb.minZ, aabb.maxY, aabb.maxX);
            case SOUTH -> new AABB(-aabb.maxX, aabb.minY, -aabb.maxZ, -aabb.minX, aabb.maxY, -aabb.minZ);
            case WEST -> new AABB(aabb.minZ, aabb.minY, -aabb.maxX, aabb.maxZ, aabb.maxY, -aabb.minX);
            default -> aabb;
        };

        return newBox.move(x, y, z);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldDrawHighlight(Level level, BlockPos pos) {
        return !bounding.isEmpty();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawHighlight(RenderHighlightEvent.Block event, Level level, BlockPos pos) {

        BlockPos corePos = this.findCore(level, pos);
        if (corePos == null) return;

        int x = corePos.getX();
        int y = corePos.getY();
        int z = corePos.getZ();

        Vec3 camera = event.getCamera().getPosition();
        double dX = camera.x;
        double dY = camera.y;
        double dZ = camera.z;
        float exp = 0.002F;

        BlockState state = level.getBlockState(corePos);
        Direction facing = state.getValue(FACING);
        Direction rot = facing.getClockWise();

        PoseStack poseStack = event.getPoseStack();
        VertexConsumer vertexConsumer = event.getMultiBufferSource().getBuffer(RenderType.lines());

        for (AABB aabb : this.bounding) {
            AABB transformedAABB = getAABBRotationOffset(aabb.inflate(exp), 0, 0, 0, rot).move(x - dX + 0.5, y - dY, z - dZ + 0.5);
            LevelRenderer.renderLineBox(
                    poseStack,
                    vertexConsumer,
                    transformedAABB,
                    0.0F, 0.0F, 0.0F, 0.4F
            );
        }
    }

    @Override
    @Nullable
    public CompoundTag getSettings(Level level, BlockPos pos) {
        BlockPos corePos = findCore(level, pos);
        if (corePos == null) return null;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if (blockEntity instanceof ICopiable copiable) {
            return copiable.getSettings(level, corePos);
        }
        return null;
    }

    @Override
    public void pasteSettings(CompoundTag nbt, int index, Level level, Player player, BlockPos pos) {
        BlockPos corePos = findCore(level, pos);
        if (corePos == null) return;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if (blockEntity instanceof ICopiable copiable) {
            copiable.pasteSettings(nbt, index, level, player, corePos);
        }
    }

    @Override
    @Nullable
    public String[] infoForDisplay(Level level, BlockPos pos) {
        BlockPos corePos = findCore(level, pos);
        if (corePos == null) return null;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if (blockEntity instanceof ICopiable copiable) {
            return copiable.infoForDisplay(level, corePos);
        }
        return null;
    }

    @Override
    public BlockState transformState(BlockState state, Rotation rotation) {
        if (rotation == Rotation.NONE) {
            return state;
        }

        Direction facing = state.getValue(FACING);

        if (facing.getAxis() != Direction.Axis.Y) {
            Direction rotated = rotation.rotate(facing);
            return state.setValue(FACING, rotated);
        }

        return state;
    }

    public abstract int[] getDimensions();
    public abstract int getOffset();
    public int getHeightOffset() {
        return 0;
    }

    protected InteractionResult standardOpenBehavior(Level level, BlockPos pos, Player player, int guiId) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!player.isShiftKeyDown()) {
            BlockPos corePos = findCore(level, pos);
            if (corePos == null) {
                return InteractionResult.FAIL;
            }

            BlockEntity blockentity = level.getBlockEntity(corePos);
            if (blockentity instanceof MenuProvider be) {
                player.openMenu(new SimpleMenuProvider(be, be.getDisplayName()), pos);
            }
            return InteractionResult.CONSUME;
        }

        return InteractionResult.SUCCESS;
    }
}