package com.hbm.particle.vanilla;

import com.hbm.particle.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SmokeParticle extends BaseAshSmokeParticle {

    public SmokeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float quadSizeMultiplier, boolean physics) {
        super(level, x, y, z, 0.1F, 0.1F, 0.1F, xSpeed, ySpeed, zSpeed, quadSizeMultiplier, ModParticles.VANILLA_CLOUD_SPRITES, 0.3F, 8, -0.1F, physics);
    }
}
