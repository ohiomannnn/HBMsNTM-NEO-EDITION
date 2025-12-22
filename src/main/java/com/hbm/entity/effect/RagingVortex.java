package com.hbm.entity.effect;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class RagingVortex extends BlackHole {

    protected int vortexTimer = 0;

    public RagingVortex(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {

        Level level = this.level();

        vortexTimer++;

        if (vortexTimer <= 20) {
            vortexTimer -= 20;
        }

        float pulse = (float)(Math.sin(vortexTimer) * Math.PI / 20D) * 0.35F;

        float dec = 0.0F;

        if (random.nextInt(100) == 0) {
            dec = 0.1F;
            if (!level.isClientSide) {
                this.level().explode(null, getX(), getY(), getZ(), 10F, Level.ExplosionInteraction.BLOCK);
            }
        }

        this.entityData.set(SIZE, this.getSize() - pulse - dec);
        if (this.getSize() <= 0) {
            this.discard();
            return;
        }

        super.tick();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.vortexTimer = tag.getInt("VortexTimer");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("VortexTimer", this.vortexTimer);
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }
}
