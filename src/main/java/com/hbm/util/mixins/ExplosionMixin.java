package com.hbm.util.mixins;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {

    @Final @Shadow private Level level;
    @Final @Shadow private double x;
    @Final @Shadow private double y;
    @Final @Shadow private double z;

    @Final @Shadow private float radius;

    @Final @Shadow private ObjectArrayList<BlockPos> toBlow;

    @Inject(method = "finalizeExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", shift = At.Shift.AFTER))
    private void redirectExplosionParticle(boolean spawnParticles, CallbackInfo ci) {

        for (BlockPos pos : this.toBlow) {

            int pX = pos.getX();
            int pY = pos.getY();
            int pZ = pos.getZ();

            double oX = ((float) pX + level.random.nextFloat());
            double oY = ((float) pY + level.random.nextFloat());
            double oZ = ((float) pZ + level.random.nextFloat());
            double dX = oX - x;
            double dY = oY - y;
            double dZ = oZ - z;
            double delta = Math.sqrt(dX * dX + dY * dY + dZ * dZ) / 1D /*hehe*/;
            dX /= delta;
            dY /= delta;
            dZ /= delta;
            double mod = 0.5D / (delta / (double) this.radius + 0.1D);
            mod *= (level.random.nextFloat() * level.random.nextFloat() + 0.3F);
            dX *= mod;
            dY *= mod;
            dZ *= mod;
            level.addParticle(ParticleTypes.CLOUD, (oX + x * 1.0D) / 2.0D, (oY + y * 1.0D) / 2.0D, (oZ + z * 1.0D) / 2.0D, dX, dY, dZ);
            level.addParticle(ParticleTypes.SMOKE, oX, oY, oZ, dX, dY, dZ);
        }
    }
}
