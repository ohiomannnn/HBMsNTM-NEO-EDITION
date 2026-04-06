package com.hbm.entity.projectile;

import com.hbm.explosion.ExplosionLarge;
import com.hbm.items.NtmItems;
import com.hbm.util.RayTraceResult;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

// original (1.7.10 one) class is pure madness
// and its probably for testing idk
public class Rocket extends ThrowableNT implements ItemSupplier {

    public double gravity = 0.03D;

    public Rocket(EntityType<? extends Rocket> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SMOKE, this.position.x, this.position.y, this.position.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {

        if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
            if (this.level instanceof ServerLevel serverLevel) {
                ExplosionLarge.explode(serverLevel, this.position.x, this.position.y, this.position.z, 5, true, false, true);
            }
            this.discard();
        }
    }

    @Override
    public boolean doesImpactEntities() {
        return false;
    }

    @Override
    public double getGravityVelocity() {
        return gravity;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(NtmItems.FAT_MAN_CORE.get());
    }
}
