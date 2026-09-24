package com.hbm.blocks;

import com.hbm.blockentity.IPersistentNBT;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.interfaces.ICopiable;
import com.hbm.render.NtmRenderTypes;
import com.hbm.util.Clock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class DummyableBlock extends BaseEntityBlock implements ICustomBlockHighlight, ICopiable {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());
    public static final EnumProperty<DummyBlockType> TYPE = EnumProperty.create("type", DummyBlockType.class);

    public static boolean safeRem = false;

    public DummyableBlock(Properties properties) {
        super(properties.isSuffocating(NtmBlocks::never).isViewBlocking(NtmBlocks::never));

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(TYPE, DummyBlockType.CORE)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    /**
     * Creates a dummy block pressed pointing towards the given direction
     */
    public BlockState createDummyState(Direction pointingTo) {
        return this.defaultBlockState().setValue(FACING, pointingTo).setValue(TYPE, DummyBlockType.DUMMY);
    }

    /**
     * Creates an extra (flagged dummy) block pressed
     */
    public BlockState createExtraState(Direction pointingTo) {
        return this.defaultBlockState().setValue(FACING, pointingTo).setValue(TYPE, DummyBlockType.EXTRA);
    }

    /**
     * Creates a core block pressed with the given facing direction
     */
    public BlockState createCoreState(Direction facing) {
        return this.defaultBlockState().setValue(FACING, facing).setValue(TYPE, DummyBlockType.CORE);
    }

    /**
     * Checks if this block pressed represents the core of the multiblock
     */
    public static boolean isCore(BlockState state) {
        return state.getValue(TYPE) == DummyBlockType.CORE;
    }

    /**
     * Checks if this block pressed has the extra flag
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
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return null;
        if(level.getBlockEntity(corePos) instanceof MenuProvider prov) {
            return prov;
        }
        return null;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        if(safeRem) return;
        this.destroyIfOrphan(level, pos, state);
    }

    private void destroyIfOrphan(Level level, BlockPos pos, BlockState state) {
        if(level.isClientSide) return;
        // todo figure out????
        if(isCore(state)) return;

        Direction dir = state.getValue(FACING).getOpposite();
        Block b = level.getBlockState(pos.relative(dir)).getBlock();

        // An extra precaution against multiblocks on chunk borders being erroneously deleted.
        // Technically, this might be used to persist ghost dummy blocks by manipulating
        // loaded chunks and block destruction, but this gives no benefit to the player,
        // cannot be done accidentally, and is definitely preferable to multiblocks
        // just vanishing when their chunks are unloaded in an unlucky way.
        if(b != this && level.hasChunksAt(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            level.removeBlock(pos, false);
        }
    }

    private final List<BlockPos> positions = new ArrayList<>();

    @Nullable
    public BlockPos findCore(BlockGetter level, BlockPos pos) {
        positions.clear();
        return findCoreRecursive(level, pos);
    }

    @Nullable
    private BlockPos findCoreRecursive(BlockGetter level, BlockPos pos) {

        // todo kill sable devs for making BlockGetter.getBlockState return null!!!
        BlockState state = level.getBlockState(pos);
        if(state == null || state.getBlock() != this) return null;

        if(isCore(state)) return pos;

        if(positions.contains(pos)) return null;

        positions.add(pos);

        Direction pointingDir = getPointingDirection(state).getOpposite();
        BlockPos nextPos = pos.relative(pointingDir);

        BlockState nextState = level.getBlockState(nextPos);
        if(nextState == null || nextState.getBlock() != this) return null;

        return findCoreRecursive(level, nextPos);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if(!(placer instanceof Player player)) return;

        safeRem = true;
        level.removeBlock(pos, false);
        safeRem = false;

        int offset = -getOffset();
        BlockPos adjustedPos = pos.above(this.getHeightOffset());

        Direction dir = state.getValue(FACING);
        dir = getDirModified(dir);

        if(!checkRequirement(level, adjustedPos, dir, offset)) {
            if(!player.hasInfiniteMaterials()) {
                ItemStack mainHandStack = player.getMainHandItem();
                ItemStack offHandStack = player.getOffhandItem();
                Item item = this.asItem();

                boolean added = false;

                if(mainHandStack.is(item) && mainHandStack.getCount() < mainHandStack.getMaxStackSize()) {
                    mainHandStack.grow(1);
                    added = true;
                } else if(offHandStack.is(item) && offHandStack.getCount() < offHandStack.getMaxStackSize()) {
                    offHandStack.grow(1);
                    added = true;
                } else if(mainHandStack.isEmpty()) {
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(this));
                    added = true;
                } else if(offHandStack.isEmpty()) {
                    player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(this));
                    added = true;
                }

                if(!added) {
                    player.getInventory().add(new ItemStack(this));
                }
            }
            return;
        }

        if(!level.isClientSide) {
            BlockPos corePos = adjustedPos.relative(dir, offset);
            BlockState coreState = this.getStateForCore(level, corePos, player, dir);

            level.setBlock(corePos, coreState, 3);

            IPersistentNBT.restoreData(level, corePos, stack);

            this.fillSpace(level, adjustedPos, dir, offset);
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
        return MultiblockHandlerXR.checkSpace(level, pos.relative(dir, offset), this.getDimensions(), pos, dir);
    }

    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        MultiblockHandlerXR.fillSpace(level, pos.relative(dir, offset), this.getDimensions(), this, dir);
    }

    /**
     * "upgrades" regular dummy blocks to ones with the extra flag
     */
    public void makeExtra(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        if(state.getBlock() != this) return;
        if(state.getValue(TYPE) != DummyBlockType.DUMMY) return;

        safeRem = true;
        level.setBlock(pos, state.setValue(TYPE, DummyBlockType.EXTRA), 3);
        safeRem = false;
    }

    public void removeExtra(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        if(state.getBlock() != this) return;
        if(state.getValue(TYPE) != DummyBlockType.EXTRA) return;

        safeRem = true;
        level.setBlock(pos, state.setValue(TYPE, DummyBlockType.DUMMY), 3);
        safeRem = false;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {

        Containers.dropContentsOnDestroy(state, newState, level, pos);

        if(!state.is(newState.getBlock())) {
            if(!safeRem) {
                Direction dir = state.getValue(FACING);
                BlockPos neighborPos = pos.relative(dir.getOpposite());

                if(level.getBlockState(neighborPos).getBlock() == this) {
                    level.removeBlock(neighborPos, false);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity be, ItemStack tool) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if(!player.hasInfiniteMaterials()) {
            BlockPos corePos = this.findCore(level, pos);
            if(corePos == null) return super.playerWillDestroy(level, pos, state, player);
            dropResources(state, level, corePos, level.getBlockEntity(corePos), player, player.getMainHandItem());
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) { return 1.0F; }

    @Override
    protected RenderShape getRenderShape(BlockState state) { return RenderShape.ENTITYBLOCK_ANIMATED; }

    public List<AABB> bounding = new ArrayList<>();

    public boolean useDetailedHitbox() { return !bounding.isEmpty(); }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if(!this.useDetailedHitbox()) return super.getShape(state, getter, pos, context);

        BlockPos corePos = findCore(getter, pos);
        if(corePos == null) return super.getShape(state, getter, pos, context);

        Direction dir = getter.getBlockState(corePos).getValue(FACING).getClockWise(Axis.Y);

        VoxelShape combinedShape = Shapes.empty();
        Vec3 offset = Vec3.atLowerCornerOf(corePos.subtract(pos));

        for(AABB aabb : bounding) {
            AABB rotatedBox = getAABBRotationOffset(aabb, offset.x + 0.5, offset.y, offset.z + 0.5, dir);
            combinedShape = Shapes.or(combinedShape, Shapes.create(rotatedBox));
        }

        return combinedShape;
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
        if(corePos == null) return;

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
        Direction rot = facing.getClockWise(Axis.Y);

        PoseStack poseStack = event.getPoseStack();
        VertexConsumer vertexConsumer = event.getMultiBufferSource().getBuffer(RenderType.lines());

        for(AABB aabb : this.bounding) {
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
        if(corePos == null) return null;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(blockEntity instanceof ICopiable copiable) {
            return copiable.getSettings(level, corePos);
        }
        return null;
    }

    @Override
    public void pasteSettings(CompoundTag nbt, int index, Level level, Player player, BlockPos pos) {
        BlockPos corePos = findCore(level, pos);
        if(corePos == null) return;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(blockEntity instanceof ICopiable copiable) {
            copiable.pasteSettings(nbt, index, level, player, corePos);
        }
    }

    @Override
    @Nullable
    public String[] infoForDisplay(Level level, BlockPos pos) {
        BlockPos corePos = findCore(level, pos);
        if(corePos == null) return null;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(blockEntity instanceof ICopiable copiable) {
            return copiable.infoForDisplay(level, corePos);
        }
        return null;
    }

    public abstract int[] getDimensions();
    public abstract int getOffset();
    public int getHeightOffset() { return 0; }

    protected InteractionResult standardOpenBehavior(Level level, BlockPos pos, Player player) {
        if(level.isClientSide) return InteractionResult.SUCCESS;

        if(!player.isShiftKeyDown()) {
            BlockPos corePos = this.findCore(level, pos);
            if(corePos == null) return InteractionResult.FAIL;

            BlockEntity be = level.getBlockEntity(corePos);
            if(be instanceof MenuProvider menu) player.openMenu(new SimpleMenuProvider(menu, menu.getDisplayName()), pos);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * Allows six int arrays with standard format (up, down, forward, backward, left, right)
     * or nine int arrays with extra offset info (backward, up, left)
     */
    public int[][] getAllDimensions() { return new int[][] { this.getDimensions() }; }
    public double[][] getAABBExtras() { return new double[0][0]; }

    @OnlyIn(Dist.CLIENT)
    public void drawPlacementHighlight(MultiBufferSource buffer, Vec3 camPos, PoseStack poseStack, Player player) {
        HitResult hr = Minecraft.getInstance().hitResult;
        if(hr == null) return;

        if(hr.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) hr;

            BlockPos bhrBlockPos = bhr.getBlockPos();
            int offset = -this.getOffset();
            int pY = bhrBlockPos.getY() + this.getHeightOffset();

            // Orientation
            Direction facing = player.getDirection().getOpposite();
            Direction sideHit = bhr.getDirection();
            facing = getDirModified(facing);

            double originX = bhrBlockPos.getX() + facing.getStepX() * offset + sideHit.getStepX();
            double originY = pY + sideHit.getStepY();
            double originZ = bhrBlockPos.getZ() + facing.getStepZ() * offset + sideHit.getStepZ();

            BlockPos checkOrigin = new BlockPos(bhrBlockPos.getX(), pY, bhrBlockPos.getZ());
            boolean canPlace = this.checkRequirement(player.level, checkOrigin.relative(sideHit), facing, offset);

            VertexConsumer consumer = buffer.getBuffer(NtmRenderTypes.DEBUG_LINE_STRIP.apply(2.0));
            Matrix4f matrix = poseStack.last().pose();

            double timer = (Clock.get_ms() % (1000D * Math.PI)) / 250D;
            double sine = Math.sin(timer);
            int sineColor = (int) (255 * (sine * 0.25 + 0.75));

            int color = canPlace ? FastColor.ARGB32.color(255, 0, sineColor, 0) : FastColor.ARGB32.color(255, sineColor, 0, 0);

            // Gets the list of different dimensions that each XLmultiblock has,
            // and generates the list of blocks that needs to be highlighted.
            // Each XL multiblock has the getAllDimensions overridden in its own
            // class. Each NEEDS it or it will show the incorect shape.
            List<BlockPos> blocks = new ArrayList<>();
            Set<BlockPos> set = new HashSet<>();

            for(int[] dims : this.getAllDimensions()) {

                // Some of the multiblocks have offsets for the placements, so
                // this allows for the ones that dont need it to have a bunch of
                // 0s at the end.
                int offBkwd = dims.length > 6 ? dims[6] : 0;
                int offUp = dims.length > 7 ? dims[7] : 0;
                int offLeft = dims.length > 8 ? dims[8] : 0;
                int worldOffX;
                int worldOffY;
                int worldOffZ;

                worldOffY = offUp;
                worldOffX = facing.getStepX() * offBkwd + facing.getClockWise(Axis.Y).getStepX() * offLeft;
                worldOffZ = facing.getStepZ() * offBkwd + facing.getClockWise(Axis.Y).getStepZ() * offLeft;

                int[] rot = MultiblockHandlerXR.rotate(dims, facing);
                for(int bx = -rot[4] + worldOffX; bx <= rot[5] + worldOffX; bx++) {
                    for(int by = -rot[1] + worldOffY; by <= rot[0] + worldOffY; by++) {
                        for(int bz = -rot[2] + worldOffZ; bz <= rot[3] + worldOffZ; bz++) {
                            BlockPos bp = new BlockPos(Mth.floor(originX) + bx, Mth.floor(originY) + by, Mth.floor(originZ) + bz);
                            blocks.add(bp);
                            set.add(bp);
                        }
                    }
                }
            }

            // This looks for the blocks nearby and draws lines between the
            // vertexes to make different shaped boxes.
            // Most of this was taken from Mellow (Thanks mellow) -Wolf
            for(BlockPos pos : blocks) {
                boolean px = set.contains(pos.offset(1, 0, 0));
                boolean nx = set.contains(pos.offset(-1, 0, 0));
                boolean ppy = set.contains(pos.offset(0, 1, 0));
                boolean ny = set.contains(pos.offset(0, -1, 0));
                boolean ppz = set.contains(pos.offset(0, 0, 1));
                boolean nz = set.contains(pos.offset(0, 0, -1));

                float minX = (float)(pos.getX() - camPos.x);
                float maxX = (float)(pos.getX() + 1 - camPos.x);
                float minY = (float)(pos.getY() - camPos.y);
                float maxY = (float)(pos.getY() + 1 - camPos.y);
                float minZ = (float)(pos.getZ() - camPos.z);
                float maxZ = (float)(pos.getZ() + 1 - camPos.z);

                if(!ppy) {
                    if(!nx) {
                        consumer.addVertex(matrix, minX, maxY, minZ).setColor(color);
                        consumer.addVertex(matrix, minX, maxY, maxZ).setColor(color);
                    }
                    if(!ppz) {
                        consumer.addVertex(matrix, minX, maxY, maxZ).setColor(color);
                        consumer.addVertex(matrix, maxX, maxY, maxZ).setColor(color);
                    }
                    if(!px) {
                        consumer.addVertex(matrix, maxX, maxY, maxZ).setColor(color);
                        consumer.addVertex(matrix, maxX, maxY, minZ).setColor(color);
                    }
                    if(!nz) {
                        consumer.addVertex(matrix, maxX, maxY, minZ).setColor(color);
                        consumer.addVertex(matrix, minX, maxY, minZ).setColor(color);
                    }
                }
                if(!ny) {
                    if(!nx) {
                        consumer.addVertex(matrix, minX, minY, minZ).setColor(color);
                        consumer.addVertex(matrix, minX, minY, maxZ).setColor(color);
                    }
                    if(!ppz) {
                        consumer.addVertex(matrix, minX, minY, maxZ).setColor(color);
                        consumer.addVertex(matrix, maxX, minY, maxZ).setColor(color);
                    }
                    if(!px) {
                        consumer.addVertex(matrix, maxX, minY, maxZ).setColor(color);
                        consumer.addVertex(matrix, maxX, minY, minZ).setColor(color);
                    }
                    if(!nz) {
                        consumer.addVertex(matrix, maxX, minY, minZ).setColor(color);
                        consumer.addVertex(matrix, minX, minY, minZ).setColor(color);
                    }
                }
                if(!nz) {
                    if(!nx) {
                        consumer.addVertex(matrix, minX, minY, minZ).setColor(color);
                        consumer.addVertex(matrix, minX, maxY, minZ).setColor(color);
                    }
                    if(!ppy) {
                        consumer.addVertex(matrix, minX, maxY, minZ).setColor(color);
                        consumer.addVertex(matrix, maxX, maxY, minZ).setColor(color);
                    }
                    if(!px) {
                        consumer.addVertex(matrix, maxX, maxY, minZ).setColor(color);
                        consumer.addVertex(matrix, maxX, minY, minZ).setColor(color);
                    }
                    if(!ny) {
                        consumer.addVertex(matrix, maxX, minY, minZ).setColor(color);
                        consumer.addVertex(matrix, minX, minY, minZ).setColor(color);
                    }
                }
                if(!ppz) {
                    if(!nx) {
                        consumer.addVertex(matrix, minX, minY, maxZ).setColor(color);
                        consumer.addVertex(matrix, minX, maxY, maxZ).setColor(color);
                    }
                    if(!ppy) {
                        consumer.addVertex(matrix, minX, maxY, maxZ).setColor(color);
                        consumer.addVertex(matrix, maxX, maxY, maxZ).setColor(color);
                    }
                    if(!px) {
                        consumer.addVertex(matrix, maxX, maxY, maxZ).setColor(color);
                        consumer.addVertex(matrix, maxX, minY, maxZ).setColor(color);
                    }
                    if(!ny) {
                        consumer.addVertex(matrix, maxX, minY, maxZ).setColor(color);
                        consumer.addVertex(matrix, minX, minY, maxZ).setColor(color);
                    }
                }
                if(!nx) {
                    if(!nz) {
                        consumer.addVertex(matrix, minX, minY, minZ).setColor(color);
                        consumer.addVertex(matrix, minX, maxY, minZ).setColor(color);
                    }
                    if(!ppy) {
                        consumer.addVertex(matrix, minX, maxY, minZ).setColor(color);
                        consumer.addVertex(matrix, minX, maxY, maxZ).setColor(color);
                    }
                    if(!ppz) {
                        consumer.addVertex(matrix, minX, maxY, maxZ).setColor(color);
                        consumer.addVertex(matrix, minX, minY, maxZ).setColor(color);
                    }
                    if(!ny) {
                        consumer.addVertex(matrix, minX, minY, maxZ).setColor(color);
                        consumer.addVertex(matrix, minX, minY, minZ).setColor(color);
                    }
                }
                if(!px) {
                    if(!nz) {
                        consumer.addVertex(matrix, maxX, minY, minZ).setColor(color);
                        consumer.addVertex(matrix, maxX, maxY, minZ).setColor(color);
                    }
                    if(!ppy) {
                        consumer.addVertex(matrix, maxX, maxY, minZ).setColor(color);
                        consumer.addVertex(matrix, maxX, maxY, maxZ).setColor(color);
                    }
                    if(!ppz) {
                        consumer.addVertex(matrix, maxX, maxY, maxZ).setColor(color);
                        consumer.addVertex(matrix, maxX, minY, maxZ).setColor(color);
                    }
                    if(!ny) {
                        consumer.addVertex(matrix, maxX, minY, maxZ).setColor(color);
                        consumer.addVertex(matrix, maxX, minY, minZ).setColor(color);
                    }
                }
            }

            color = FastColor.ARGB32.color(255, 0, 0, sineColor);

            // boo-yeah
            for(double[] extra : this.getAABBExtras()) {

                Direction rot = facing.getClockWise(Axis.Y);
                float cX = (float)(Mth.floor(originX) - camPos.x) + 0.5F;
                float cY = (float)(Mth.floor(originY) - camPos.y);
                float cZ = (float)(Mth.floor(originZ) - camPos.z) + 0.5F;

                float upr = (float) extra[0];
                float lwr = (float) extra[1];
                float fwd = (float) extra[2];
                float bwd = (float) extra[3];
                float lft = (float) extra[4];
                float rgt = (float) extra[5];

                float x0 = cX + fwd * facing.getStepX() + lft * rot.getStepX();
                float x1 = cX + bwd * facing.getStepX() + rgt * rot.getStepX();
                float y0 = cY + lwr;
                float y1 = cY + upr;
                float z0 = cZ + fwd * facing.getStepZ() + lft * rot.getStepZ();
                float z1 = cZ + bwd * facing.getStepZ() + rgt * rot.getStepZ();

                consumer.addVertex(matrix, x0, y0, z0).setColor(color); consumer.addVertex(matrix, x0, y0, z1).setColor(color);
                consumer.addVertex(matrix, x1, y0, z0).setColor(color); consumer.addVertex(matrix, x1, y0, z1).setColor(color);
                consumer.addVertex(matrix, x0, y0, z0).setColor(color); consumer.addVertex(matrix, x1, y0, z0).setColor(color);
                consumer.addVertex(matrix, x0, y0, z1).setColor(color); consumer.addVertex(matrix, x1, y0, z1).setColor(color);

                consumer.addVertex(matrix, x0, y1, z0).setColor(color); consumer.addVertex(matrix, x0, y1, z1).setColor(color);
                consumer.addVertex(matrix, x1, y1, z0).setColor(color); consumer.addVertex(matrix, x1, y1, z1).setColor(color);
                consumer.addVertex(matrix, x0, y1, z0).setColor(color); consumer.addVertex(matrix, x1, y1, z0).setColor(color);
                consumer.addVertex(matrix, x0, y1, z1).setColor(color); consumer.addVertex(matrix, x1, y1, z1).setColor(color);

                consumer.addVertex(matrix, x0, y0, z0).setColor(color); consumer.addVertex(matrix, x0, y1, z0).setColor(color);
                consumer.addVertex(matrix, x1, y0, z0).setColor(color); consumer.addVertex(matrix, x1, y1, z0).setColor(color);
                consumer.addVertex(matrix, x0, y0, z1).setColor(color); consumer.addVertex(matrix, x0, y1, z1).setColor(color);
                consumer.addVertex(matrix, x1, y0, z1).setColor(color); consumer.addVertex(matrix, x1, y1, z1).setColor(color);
            }
        }
    }
}