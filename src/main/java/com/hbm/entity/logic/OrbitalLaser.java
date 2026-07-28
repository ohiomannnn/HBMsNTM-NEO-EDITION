package com.hbm.entity.logic;

import com.hbm.entity.NtmEntityTypes;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.explosion.vanillant.standard.EntityProcessorCrossSmooth;
import com.hbm.explosion.vanillant.standard.ExplosionEffectWeapon;
import com.hbm.util.DamageResistanceHandler.DamageClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class OrbitalLaser extends Entity {

    public static final int MAX_AGE = 5;

    public OrbitalLaser(EntityType<? extends OrbitalLaser> entityType, Level level) {
        super(entityType, level);

        this.noCulling = true;
    }

    public OrbitalLaser(Level level) { this(NtmEntityTypes.ORBITAL_LASER.get(), level); }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) { }

    @Override protected void readAdditionalSaveData(CompoundTag compoundTag) { }
    @Override protected void addAdditionalSaveData(CompoundTag compoundTag) { }

    @Override
    public void tick() {
        if(this.tickCount >= MAX_AGE && !this.level.isClientSide) this.discard();
    }

    public void explode() {

        ExplosionVNT vnt = new ExplosionVNT(this.level, this.getX(), this.getY(), this.getZ(), 5F)
                .setBlockAllocator(new BlockAllocatorStandard())
                .setBlockProcessor(new BlockProcessorStandard())
                .setEntityProcessor(new EntityProcessorCrossSmooth(1, 1_000F).setupPiercing(50F, 0.5F).setDamageClass(DamageClass.LASER))
                .setSFX(new ExplosionEffectWeapon(15, 3.5F, 1.25F));
        vnt.explode();
    }
}
