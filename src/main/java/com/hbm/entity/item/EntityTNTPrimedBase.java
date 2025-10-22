package com.hbm.entity.item;

import api.hbm.block.IFuckingExplode;
import com.hbm.entity.ModEntities;
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
import net.minecraft.world.phys.Vec3;

public class EntityTNTPrimedBase extends Entity {

    private static final EntityDataAccessor<Integer> BLOCK_ID =
            SynchedEntityData.defineId(EntityTNTPrimedBase.class, EntityDataSerializers.INT);

    public boolean detonateOnCollision;
    public int fuse;
    private LivingEntity tntPlacedBy;

    public EntityTNTPrimedBase(EntityType<? extends EntityTNTPrimedBase> type, Level level) {
        super(type, level);
        this.blocksBuilding = true;
        this.fuse = 80;
        this.detonateOnCollision = false;
    }

    public EntityTNTPrimedBase(Level level, double x, double y, double z, LivingEntity placer, Block bomb) {
        this(ModEntities.TNT_PRIMED_BASE.get(), level);
        this.setPos(x, y, z);
        float f = (float) (Math.random() * Math.PI * 2.0D);
        Vec3 vec = new Vec3(-Math.sin(f) * 0.02, 0.2, -Math.cos(f) * 0.02);
        this.setDeltaMovement(vec);
        this.tntPlacedBy = placer;
        this.entityData.set(BLOCK_ID, Block.getId(bomb.defaultBlockState()));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BLOCK_ID, 0);
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 motion = this.getDeltaMovement();
        if (!this.isNoGravity()) {
            motion = motion.add(0.0, -0.04, 0.0);
        }

        this.move(MoverType.SELF, motion);
        motion = motion.scale(0.98);

        if (this.onGround()) {
            motion = new Vec3(motion.x * 0.7, -motion.y * 0.5, motion.z * 0.7);
        }

        this.setDeltaMovement(motion);

        if (this.fuse-- <= 0 || (this.detonateOnCollision && this.horizontalCollision)) {
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
        tag.putInt("Fuse", this.fuse);
        tag.putInt("BlockId", this.entityData.get(BLOCK_ID));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.fuse = tag.getInt("Fuse");
        this.entityData.set(BLOCK_ID, tag.getInt("BlockId"));
    }

    public LivingEntity getTntPlacedBy() {
        return this.tntPlacedBy;
    }
}
