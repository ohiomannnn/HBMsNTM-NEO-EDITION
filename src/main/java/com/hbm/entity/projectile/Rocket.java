package com.hbm.entity.projectile;

import com.hbm.explosion.ExplosionLarge;
import com.hbm.items.NtmItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class Rocket extends ThrowableProjectile implements ItemSupplier {

    public double gravity = 0.03D;

    public Rocket(EntityType<? extends Rocket> type, Level level) {
        super(type, level);
    }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) { }

    @Override
    public void tick() {
        super.tick();

        if(this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SMOKE, this.position.x, this.position.y, this.position.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        if(this.level instanceof ServerLevel serverLevel) {
            ExplosionLarge.explode(serverLevel, this.position.x, this.position.y, this.position.z, 5, true, false, true);
        }
        this.discard();
    }

    @Override
    protected double getDefaultGravity() {
        return gravity;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(NtmItems.FAT_MAN_CORE.get());
    }
}
