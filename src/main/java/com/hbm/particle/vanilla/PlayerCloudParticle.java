package com.hbm.particle.vanilla;

import com.hbm.particle.ModParticles;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlayerCloudParticle extends TextureSheetParticle {

    public float scaleFactor;

    public PlayerCloudParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z);
        this.friction = 0.96F;
        this.xd *= 0.1F;
        this.yd *= 0.1F;
        this.zd *= 0.1F;
        this.xd += xSpeed;
        this.yd += ySpeed;
        this.zd += zSpeed;
        float f1 = 1.0F - (float) (Math.random() * (double) 0.3F);
        this.rCol = f1;
        this.gCol = f1;
        this.bCol = f1;
        this.quadSize *= 1.875F;
        this.scaleFactor = quadSize;
        int i = (int) ((double) 8.0F / (Math.random() * 0.8 + 0.3));
        this.lifetime = (int) Math.max((float) i * 2.5F, 1.0F);
        this.hasPhysics = false;
        this.setSpriteFromAge(ModParticles.VANILLA_CLOUD_SPRITES);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public float getQuadSize(float scaleFactor) {
        return this.quadSize * Mth.clamp(((float) this.age + this.scaleFactor) / (float) this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    public void tick() {
        super.tick();

        if (!this.removed) {
            this.setSpriteFromAge(ModParticles.VANILLA_CLOUD_SPRITES);
            Player player = this.level.getNearestPlayer(this.x, this.y, this.z, 2.0, false);
            if (player != null) {
                double d0 = player.getY();
                if (this.y > d0) {
                    this.y = this.y + (d0 - this.y) * 0.2;
                    this.yd = this.yd + (player.getDeltaMovement().y - this.yd) * 0.2;
                    this.setPos(this.x, this.y, this.z);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Provider(SpriteSet sprites) { ModParticles.VANILLA_CLOUD_SPRITES = sprites; }
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return null;
        }
    }
}