package com.hbm.entity.projectile;

import com.hbm.particle.NtmParticles;
import com.hbm.particle.helper.IParticleCreator;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.SoundUtils;
import com.hbm.util.particle.ParticleUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Tom extends Entity {

    public Tom(EntityType<? extends Tom> entityType, Level level) {
        super(entityType, level);

        this.noCulling = true;
    }

    @Override
    public void tick() {

        this.setPos(this.position().add(0.0, -0.5, 0.0));

        if(this.tickCount % 100 == 0) SoundUtils.playAtEntity(this, NtmSoundEvents.CHIME.get(), SoundSource.AMBIENT, 10000F, 1F);

        if(!this.level.getBlockState(this.blockPosition()).isAir() || this.position().y < 10) {
            ParticleUtil.addParticle(this.level, NtmParticles.TOM_BLAST.get(), this.position.x, this.position.y, this.position.z);
            this.discard();
        }
    }

    @Override public boolean shouldRenderAtSqrDistance(double distance) { return true; }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) { }

    @Override protected void readAdditionalSaveData(CompoundTag tag) { }
    @Override protected void addAdditionalSaveData(CompoundTag tag) { }
}
