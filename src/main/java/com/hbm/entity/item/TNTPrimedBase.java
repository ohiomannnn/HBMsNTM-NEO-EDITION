package com.hbm.entity.item;

import api.hbm.block.IFuckingExplode;
import com.hbm.entity.ModEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class TNTPrimedBase extends Entity {

    private static final EntityDataAccessor<Integer> BLOCK_ID = SynchedEntityData.defineId(TNTPrimedBase.class, EntityDataSerializers.INT);

    public boolean detonateOnCollision;
    public int fuse;
    private LivingEntity tntPlacedBy;

    public TNTPrimedBase(EntityType<? extends TNTPrimedBase> type, Level level) {
        super(type, level);
        this.blocksBuilding = true;
        this.fuse = 80;
        this.detonateOnCollision = false;
    }

    public TNTPrimedBase(Level level, double x, double y, double z, LivingEntity placer, Block bomb) {
        this(ModEntityTypes.TNT_PRIMED_BASE.get(), level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * (double)((float)Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.2F, -Math.cos(d0) * 0.02);
        this.tntPlacedBy = placer;
        this.entityData.set(BLOCK_ID, Block.getId(bomb.defaultBlockState()));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BLOCK_ID, 0);
    }

    @Override
    public void tick() {

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        this.setDeltaMovement(this.getDeltaMovement().subtract(0, 0.04D, 0));
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.98D, 0.98D, 0.98D));

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }

        if (this.fuse-- <= 0 || (this.detonateOnCollision && (this.horizontalCollision || this.verticalCollision))) {
            this.discard();

            if (!this.level().isClientSide) {
                this.explode();
            }
        } else {
            this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode() {
        this.getBomb().explodeEntity(this.level(), this.getX(), this.getY(), this.getZ(), this);
    }

    public IFuckingExplode getBomb() {
        return (IFuckingExplode) getBlock();
    }

    public Block getBlock() {
        return Block.stateById(this.entityData.get(BLOCK_ID)).getBlock();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putByte("Fuse", (byte) this.fuse);
        tag.putInt("BlockId", this.entityData.get(BLOCK_ID));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.fuse = tag.getByte("Fuse");
        this.entityData.set(BLOCK_ID, tag.getInt("BlockId"));
    }

    public LivingEntity getTntPlacedBy() {
        return this.tntPlacedBy;
    }
}
