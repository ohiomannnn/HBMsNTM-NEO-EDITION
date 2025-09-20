package com.hbm.entity.item;

import api.hbm.block.IFuckingExplode;
import com.hbm.entity.ModEntities;
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
import net.minecraft.core.particles.ParticleTypes;

public class EntityTNTPrimedBase extends Entity {

    private static final EntityDataAccessor<Integer> BLOCK_ID =
            SynchedEntityData.defineId(EntityTNTPrimedBase.class, EntityDataSerializers.INT);

    public boolean detonateOnCollision;
    public int fuse;
    private LivingEntity tntPlacedBy;

    public EntityTNTPrimedBase(EntityType<? extends EntityTNTPrimedBase> type, Level level) {
        super(type, level);
        this.fuse = 80;
        this.detonateOnCollision = false;
    }

    public EntityTNTPrimedBase(Level level, double x, double y, double z, LivingEntity placer, Block bomb) {
        this(ModEntities.TNT_PRIMED_BASE.get(), level);
        this.setPos(x, y, z);
        double angle = this.random.nextDouble() * Math.PI * 2.0D;
        this.setDeltaMovement(-Math.sin(angle) * 0.02D, 0.2D, -Math.cos(angle) * 0.02D);
        this.tntPlacedBy = placer;
        this.entityData.set(BLOCK_ID, Block.getId(bomb.defaultBlockState()));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BLOCK_ID, 0);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        var motion = this.getDeltaMovement().add(0, -0.04D, 0);
        this.setDeltaMovement(motion);
        this.move(MoverType.SELF, motion);

        motion = this.getDeltaMovement().scale(0.98D);
        if (this.onGround()) {
            motion = new net.minecraft.world.phys.Vec3(
                    motion.x * 0.7D,
                    -motion.y * 0.5D,
                    motion.z * 0.7D
            );
        }
        this.setDeltaMovement(motion);

        if (this.fuse-- <= 0 || (this.detonateOnCollision && this.horizontalCollision)) {
            this.discard();
            if (!this.level().isClientSide) {
                this.explode();
            }
        } else {
            this.level().addParticle(ParticleTypes.SMOKE,
                    this.getX(), this.getY() + 0.5D, this.getZ(),
                    0.0D, 0.0D, 0.0D);
        }
    }

    private void explode() {
        this.getBomb().explodeEntity(level(), this.getX(), this.getY(), this.getZ(), this);
    }

    public IFuckingExplode getBomb() {
        return (IFuckingExplode) getBlock();
    }

    public Block getBlock() {
        return Block.stateById(this.entityData.get(BLOCK_ID)).getBlock();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Fuse", this.fuse);
        tag.putInt("Block", this.entityData.get(BLOCK_ID));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.fuse = tag.getInt("Fuse");
        this.entityData.set(BLOCK_ID, tag.getInt("Block"));
    }

    public LivingEntity getTntPlacedBy() {
        return this.tntPlacedBy;
    }
}
