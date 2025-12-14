package com.hbm.entity.logic;

import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.AuxParticle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class DeathBlast extends Entity {

    public static final int MAX_AGE = 60;

    public DeathBlast(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) { }

    @Override protected void readAdditionalSaveData(CompoundTag compoundTag) { }
    @Override protected void addAdditionalSaveData(CompoundTag compoundTag) { }

    @Override
    public void tick() {
        if (this.tickCount >= MAX_AGE && !this.level().isClientSide) {
            this.discard();

            NukeExplosionMK5.statFac(level(), 40, getX(), getY(), getZ()).setNoRad();

            this.level().playSound(null, this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5, ModSounds.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 25.0F, 0.9F);
            CompoundTag tag = new CompoundTag();
            tag.putString("type", "muke");
            if (this.level() instanceof ServerLevel serverLevel) {
                PacketDistributor.sendToPlayersNear(serverLevel, null, this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5, 250, new AuxParticle(tag, this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5));
            }
        }
    }
}
