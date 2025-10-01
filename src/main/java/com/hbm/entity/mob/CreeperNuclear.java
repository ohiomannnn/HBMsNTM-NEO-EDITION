package com.hbm.entity.mob;

import com.hbm.entity.logic.NukeExplosionMK5Entity;
import com.hbm.explosion.ExplosionNukeGeneric;
import com.hbm.explosion.ExplosionNukeSmall;
import com.hbm.lib.ModSounds;
import com.hbm.packets.toclient.AuxParticlePacket;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class CreeperNuclear extends Creeper {

    public CreeperNuclear(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);

        this.spawnAtLocation(Items.TNT);
    }


    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            AABB box = this.getBoundingBox().inflate(5);
            List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, box);

            for (LivingEntity e : list) {
                ContaminationUtil.contaminate(e, HazardType.RADIATION, ContaminationType.CREATIVE, 0.25F);
            }

            if (this.isAlive() && this.getHealth() < this.getMaxHealth() && this.tickCount % 10 == 0) {
                this.heal(1.0F);
            }
        }
    }

    @Override
    public void ignite() {
        super.ignite();
        nuclearExplode();
    }

    private void nuclearExplode() {
        if (!this.level().isClientSide) {
            this.discard();

            boolean mobGriefing = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);

            if (this.isPowered()) {
                CompoundTag data = new CompoundTag();
                data.putString("type", "muke");
                PacketDistributor.sendToPlayersNear(
                        (ServerLevel) level(),
                        null,
                        this.getX(), this.getY(), this.getZ(),
                        1000,
                        new AuxParticlePacket(data, this.getX(), this.getY(), this.getZ())
                );

                this.level().playSound(null, this.blockPosition(), ModSounds.MUKE_EXPLOSION.get(), SoundSource.HOSTILE, 15.0F, 1.0F);

                if (mobGriefing) {
                    this.level().addFreshEntity(NukeExplosionMK5Entity.statFac(this.level(), 50, this.getX(), this.getY(), this.getZ()));
                } else {
                    ExplosionNukeGeneric.dealDamage(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), 100);
                }
            } else {
                if (mobGriefing) {
                    ExplosionNukeSmall.explode(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), ExplosionNukeSmall.PARAMS_MEDIUM);
                } else {
                    ExplosionNukeSmall.explode(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), ExplosionNukeSmall.PARAMS_SAFE);
                }
            }
        }
    }
}
