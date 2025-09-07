package com.hbm.entity.grenade;

import com.hbm.explosion.vanillalike.ExplosionVNT;
import com.hbm.items.ModItems;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityGrenadeGeneric extends EntityGrenadeBouncyBase implements ItemSupplier {

    // For register
    public EntityGrenadeGeneric(EntityType<? extends EntityGrenadeGeneric> type, Level level) {
        super(type, level);
    }

    // Constructor to spawn grenade from player
    public EntityGrenadeGeneric(EntityType<? extends EntityGrenadeGeneric> type, Level level, LivingEntity thrower) {
        super(type, level, thrower);
    }

    // Constructor to spawn grenade in cords
    public EntityGrenadeGeneric(EntityType<? extends EntityGrenadeGeneric> type, Level level, double x, double y, double z) {
        super(type, level, x, y, z);
    }

    @Override
    public void explode() {
        if (!level().isClientSide) {
            this.discard();
            ExplosionVNT explosion = new ExplosionVNT(this.level(), this.getX(), this.getY(), this.getZ(), 15.0F);
            explosion.makeAmat();
            explosion.explode();
        }
    }

    @Override
    protected int getMaxTimer() {
        return 50;
    }

    @Override
    protected double getBounceMod() {
        return 0.20D;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public ItemStack getItem() {
        return new ItemStack(ModItems.GRENADE.get());
    }
}
