package com.hbm.entity.mob;

import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.explosion.ExplosionNukeGeneric;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockMutatorFire;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.items.special.PolaroidItem;
import com.hbm.lib.ModDamageTypes;
import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
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
    public boolean hurt(DamageSource source, float amount) {
        // for some reason the nuclear explosion would damage the already dead entity, reviving it and forcing it to play the death animation
        if (this.dead) return false;

        if (source.is(ModDamageTypes.RADIATION)) {
            if (this.isAlive()) {
                this.heal(amount);
            }
            return false;
        }

        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5));

            for (LivingEntity entity : list) {
                ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, 0.25F);
            }

            if (this.isAlive() && this.getHealth() < this.getMaxHealth() && this.tickCount % 10 == 0) {
                this.heal(1.0F);
            }
        }
    }

    @Override
    public void ignite() {
        super.ignite();

        this.dead = true;
        this.nuclearExplode();
        this.triggerOnDeathMobEffects(RemovalReason.KILLED);
        this.discard();
    }

    private void nuclearExplode() {

        Level level = this.level();
        if (!level.isClientSide) {
            boolean mobGriefing = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);

            // this has to be the single worst solution ever
            level.playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 25.0F, 1F);
            CompoundTag tag = new CompoundTag();
            tag.putString("type", "muke");
            tag.putBoolean("balefire", PolaroidItem.polaroidID == 11 || level.random.nextInt(100) == 0);
            if (level instanceof ServerLevel serverLevel) {
                PacketDistributor.sendToPlayersNear(serverLevel, null, this.getX(), this.getY(), this.getZ(), 250, new AuxParticle(tag, this.getX(), this.getY() + 0.5, this.getZ()));
            }

            if (this.isPowered()) {
                if (mobGriefing) {
                    NukeExplosionMK5.statFac(this.level(), 50, this.getX(), this.getY(), this.getZ());
                } else {
                    ExplosionNukeGeneric.dealDamage(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), 100);
                }
            } else {
                if (mobGriefing) {
                    ExplosionVNT vnt = new ExplosionVNT(level, this.getX(), this.getY() + 0.5, this.getZ(), 20)
                            .setBlockAllocator(new BlockAllocatorStandard())
                            .setBlockProcessor(new BlockProcessorStandard().withBlockEffect(new BlockMutatorFire()).setNoDrop());
                    vnt.explode();
                    ExplosionNukeGeneric.dealDamage(level, this.getX(), this.getY() + 0.5, this.getZ(), 55);
                    ExplosionNukeGeneric.incrementRad(level, this.getX(), this.getY() + 0.5, this.getZ(), 3 / 3F);
                } else {
                    ExplosionNukeGeneric.dealDamage(level, this.getX(), this.getY() + 0.5, this.getZ(), 45);
                    ExplosionNukeGeneric.incrementRad(level, this.getX(), this.getY() + 0.5, this.getZ(), 2 / 3F);
                }
            }
        }
    }
}
