package com.hbm.entity.item;

import com.hbm.entity.ModEntityTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.CrashReportCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class FallingBlockEntityNT extends Entity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private BlockState blockState;
    public int time;
    public boolean dropItem;
    private boolean cancelDrop;
    private boolean hurtEntities;
    private int fallDamageMax;
    private float fallDamagePerDistance;
    @Nullable
    public CompoundTag blockData;
    public boolean forceTickAfterTeleportToDuplicate;
    protected static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId(FallingBlockEntityNT.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Integer> BLOCK_ID = SynchedEntityData.defineId(FallingBlockEntityNT.class, EntityDataSerializers.INT);

    public FallingBlockEntityNT(EntityType<? extends FallingBlockEntityNT> entityType, Level level) {
        super(entityType, level);
        this.blockState = Blocks.AIR.defaultBlockState();
        this.dropItem = true;
        this.fallDamageMax = 40;
    }

    private FallingBlockEntityNT(Level level, double x, double y, double z, BlockState state) {
        this(ModEntityTypes.FALLING_BLOCK.get(), level);
        this.blockState = state;
        this.entityData.set(BLOCK_ID, Block.getId(state));
        this.blocksBuilding = true;
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }

    public static FallingBlockEntityNT fall(Level level, BlockPos pos, BlockState blockState) {
        FallingBlockEntityNT fallingblockentity = new FallingBlockEntityNT(level, (double)pos.getX() + (double)0.5F, pos.getY(), (double)pos.getZ() + (double)0.5F, blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? (BlockState)blockState.setValue(BlockStateProperties.WATERLOGGED, false) : blockState);
        level.setBlock(pos, blockState.getFluidState().createLegacyBlock(), 3);
        level.addFreshEntity(fallingblockentity);
        return fallingblockentity;
    }

    public boolean isAttackable() {
        return false;
    }

    public void setStartPos(BlockPos startPos) {
        this.entityData.set(DATA_START_POS, startPos);
    }

    public BlockPos getStartPos() {
        return (BlockPos)this.entityData.get(DATA_START_POS);
    }

    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_START_POS, BlockPos.ZERO);
        builder.define(BLOCK_ID, 0);
    }

    public boolean isPickable() {
        return !this.isRemoved();
    }

    protected double getDefaultGravity() {
        return 0.04;
    }

    public void tick() {
        if (this.blockState.isAir()) {
            this.discard();
        } else {
            Block block = this.blockState.getBlock();
            ++this.time;
            this.applyGravity();
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.handlePortal();
            if (!this.level().isClientSide && (this.isAlive() || this.forceTickAfterTeleportToDuplicate)) {
                BlockPos blockpos = this.blockPosition();
                boolean flag = this.blockState.getBlock() instanceof ConcretePowderBlock;
                boolean flag1 = flag && this.blockState.canBeHydrated(this.level(), blockpos, this.level().getFluidState(blockpos), blockpos);
                double d0 = this.getDeltaMovement().lengthSqr();
                if (flag && d0 > (double)1.0F) {
                    BlockHitResult blockhitresult = this.level().clip(new ClipContext(new Vec3(this.xo, this.yo, this.zo), this.position(), net.minecraft.world.level.ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this));
                    if (blockhitresult.getType() != HitResult.Type.MISS && this.blockState.canBeHydrated(this.level(), blockpos, this.level().getFluidState(blockhitresult.getBlockPos()), blockhitresult.getBlockPos())) {
                        blockpos = blockhitresult.getBlockPos();
                        flag1 = true;
                    }
                }

                if (!this.onGround() && !flag1) {
                    if (!this.level().isClientSide && (this.time > 100 && (blockpos.getY() <= this.level().getMinBuildHeight() || blockpos.getY() > this.level().getMaxBuildHeight()) || this.time > 600)) {
                        if (this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            this.spawnAtLocation(block);
                        }

                        this.discard();
                    }
                } else {
                    BlockState blockstate = this.level().getBlockState(blockpos);
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, (double)-0.5F, 0.7));
                    if (!blockstate.is(Blocks.MOVING_PISTON)) {
                        if (this.cancelDrop) {
                            this.discard();
                            this.callOnBrokenAfterFall(block, blockpos);
                        } else {
                            boolean flag2 = blockstate.canBeReplaced(new DirectionalPlaceContext(this.level(), blockpos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                            boolean flag3 = FallingBlock.isFree(this.level().getBlockState(blockpos.below())) && (!flag || !flag1);
                            boolean flag4 = this.blockState.canSurvive(this.level(), blockpos) && !flag3;
                            if (flag2 && flag4) {
                                if (this.blockState.hasProperty(BlockStateProperties.WATERLOGGED) && this.level().getFluidState(blockpos).getType() == Fluids.WATER) {
                                    this.blockState = this.blockState.setValue(BlockStateProperties.WATERLOGGED, true);
                                }

                                if (!this.level().setBlock(blockpos, this.blockState, 3)) {
                                    if (this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                        this.discard();
                                        this.callOnBrokenAfterFall(block, blockpos);
                                        this.spawnAtLocation(block);
                                    }
                                } else {
                                    ((ServerLevel)this.level()).getChunkSource().chunkMap.broadcast(this, new ClientboundBlockUpdatePacket(blockpos, this.level().getBlockState(blockpos)));
                                    this.discard();
                                    if (block instanceof Fallable) {
                                        ((Fallable)block).onLand(this.level(), blockpos, this.blockState, blockstate, new FallingBlockEntity(EntityType.FALLING_BLOCK, level()));
                                    }

                                    if (this.blockData != null && this.blockState.hasBlockEntity()) {
                                        BlockEntity blockentity = this.level().getBlockEntity(blockpos);
                                        if (blockentity != null) {
                                            CompoundTag compoundtag = blockentity.saveWithoutMetadata(this.level().registryAccess());

                                            for(String s : this.blockData.getAllKeys()) {
                                                compoundtag.put(s, this.blockData.get(s).copy());
                                            }

                                            try {
                                                blockentity.loadWithComponents(compoundtag, this.level().registryAccess());
                                            } catch (Exception exception) {
                                                LOGGER.error("Failed to load block entity from falling block", exception);
                                            }

                                            blockentity.setChanged();
                                        }
                                    }
                                }
                            } else {
                                this.discard();
                                if (this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                    this.callOnBrokenAfterFall(block, blockpos);
                                    this.spawnAtLocation(block);
                                }
                            }
                        }
                    }
                }
            }

            this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        }

    }

    public void callOnBrokenAfterFall(Block block, BlockPos pos) {
        if (block instanceof Fallable) {
            ((Fallable)block).onBrokenAfterFall(this.level(), pos, new FallingBlockEntity(EntityType.FALLING_BLOCK, level()));
        }

    }

    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        if (!this.hurtEntities) {
            return false;
        } else {
            int i = Mth.ceil(fallDistance - 1.0F);
            if (i < 0) {
                return false;
            } else {
                Predicate<Entity> predicate = EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE);
                Block var8 = this.blockState.getBlock();
                DamageSource var10000;
                if (var8 instanceof Fallable) {
                    Fallable fallable = (Fallable)var8;
                    var10000 = fallable.getFallDamageSource(this);
                } else {
                    var10000 = this.damageSources().fallingBlock(this);
                }

                DamageSource damagesource = var10000;
                float f = (float)Math.min(Mth.floor((float)i * this.fallDamagePerDistance), this.fallDamageMax);
                this.level().getEntities(this, this.getBoundingBox(), predicate).forEach((p_149649_) -> p_149649_.hurt(damagesource, f));
                boolean flag = this.blockState.is(BlockTags.ANVIL);
                if (flag && f > 0.0F && this.random.nextFloat() < 0.05F + (float)i * 0.05F) {
                    BlockState blockstate = AnvilBlock.damage(this.blockState);
                    if (blockstate == null) {
                        this.cancelDrop = true;
                    } else {
                        this.blockState = blockstate;
                    }
                }

                return false;
            }
        }
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.put("BlockState", NbtUtils.writeBlockState(this.blockState));
        compound.putInt("Time", this.time);
        compound.putBoolean("DropItem", this.dropItem);
        compound.putBoolean("HurtEntities", this.hurtEntities);
        compound.putFloat("FallHurtAmount", this.fallDamagePerDistance);
        compound.putInt("FallHurtMax", this.fallDamageMax);
        if (this.blockData != null) {
            compound.put("TileEntityData", this.blockData);
        }

        compound.putBoolean("CancelDrop", this.cancelDrop);
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        this.blockState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compound.getCompound("BlockState"));
        this.time = compound.getInt("Time");
        if (compound.contains("HurtEntities", 99)) {
            this.hurtEntities = compound.getBoolean("HurtEntities");
            this.fallDamagePerDistance = compound.getFloat("FallHurtAmount");
            this.fallDamageMax = compound.getInt("FallHurtMax");
        } else if (this.blockState.is(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }

        if (compound.contains("DropItem", 99)) {
            this.dropItem = compound.getBoolean("DropItem");
        }

        if (compound.contains("TileEntityData", 10)) {
            this.blockData = compound.getCompound("TileEntityData").copy();
        }

        this.cancelDrop = compound.getBoolean("CancelDrop");
    }

    public void setHurtsEntities(float fallDamagePerDistance, int fallDamageMax) {
        this.hurtEntities = true;
        this.fallDamagePerDistance = fallDamagePerDistance;
        this.fallDamageMax = fallDamageMax;
    }

    public void disableDrop() {
        this.cancelDrop = true;
    }

    public boolean displayFireAnimation() {
        return false;
    }

    public void fillCrashReportCategory(CrashReportCategory category) {
        super.fillCrashReportCategory(category);
        category.setDetail("Immitating BlockState", this.blockState.toString());
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    protected Component getTypeName() {
        return Component.translatable("entity.minecraft.falling_block_type", new Object[]{this.blockState.getBlock().getName()});
    }

    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, entity, Block.getId(this.getBlockState()));
    }

    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.blockState = Block.stateById(packet.getData());
        this.blocksBuilding = true;
        double d0 = packet.getX();
        double d1 = packet.getY();
        double d2 = packet.getZ();
        this.setPos(d0, d1, d2);
        this.setStartPos(this.blockPosition());
    }

    @Nullable
    public Entity changeDimension(DimensionTransition transition) {
        ResourceKey<Level> resourcekey = transition.newLevel().dimension();
        ResourceKey<Level> resourcekey1 = this.level().dimension();
        boolean flag = (resourcekey1 == Level.END || resourcekey == Level.END) && resourcekey1 != resourcekey;
        Entity entity = super.changeDimension(transition);
        this.forceTickAfterTeleportToDuplicate = entity != null && flag;
        return entity;
    }
}