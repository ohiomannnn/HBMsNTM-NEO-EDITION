package com.hbm.particle;

import com.hbm.particle.vanilla.NbtParticleOptions;
import com.hbm.particle.vanilla.ParticleProviderBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;

import java.awt.*;

public class ParticleGasFlame extends TextureSheetParticle {

    private final float colorMod;

    public ParticleGasFlame(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float scale) {
        super(level, x, y, z, vx, vy * 1.5, vz);
        this.setSpriteFromAge(NtmParticles.GAS_FLAME_PARTICLES);
        this.colorMod = 0.8F + RandomSource.create().nextFloat() * 0.2F;
        this.hasPhysics = false;
        this.lifetime = 30 + RandomSource.create().nextInt(13);
        this.quadSize = scale;
        updateColor();
    }

    @Override
    public void tick() {
        double prevY = this.yd;
        super.tick();
        updateColor();
        this.yd = prevY;

        this.xd *= 0.75;
        this.yd += 0.005;
        this.zd *= 0.75;
        this.setSpriteFromAge(NtmParticles.GAS_FLAME_PARTICLES);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    private void updateColor() {
        float time = (float) this.age / (float) this.lifetime;
        Color color = Color.getHSBColor(Math.max((60 - time * 100) / 360F, 0.0F), 1 - time * 0.25F, 1 - time * 0.5F);

        this.rCol = (color.getRed() / 255F) * colorMod;
        this.gCol = (color.getGreen() / 255F) * colorMod;
        this.bCol = (color.getBlue() / 255F) * colorMod;
    }

    @Override
    public int getLightColor(float partialTick) {
        return 240;
    }

    public static class Provider extends ParticleProviderBase<NbtParticleOptions> {

        public Provider(SpriteSet sprites) {
            NtmParticles.GAS_FLAME_PARTICLES = sprites;
        }

        @Override
        public void createParticle(NbtParticleOptions options, double x, double y, double z, double xd, double yd, double zd, ClientLevel level, LocalPlayer player, int particleSetting) {
            CompoundTag tag = options.tag;

            float scale = tag.getFloat("scale");

            ParticleGasFlame particle = new ParticleGasFlame(level, x, y, z, xd, yd, zd, scale > 0 ? scale : 0.5F);
            Minecraft.getInstance().particleEngine.add(particle);
        }
    }
}
