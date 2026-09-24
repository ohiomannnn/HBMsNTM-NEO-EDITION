package com.hbm.particle;

import com.hbm.particle.vanilla.NbtParticleOptions;
import com.hbm.particle.vanilla.ParticleProviderBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;

import java.awt.*;

public class GasFlameParticle extends TextureSheetParticle {

    private final float colorMod;

    public GasFlameParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, float scale) {
        super(level, x, y, z, xd, yd * 1.5, zd);

        this.updateColor();
        this.setSpriteFromAge(NtmParticleTypes.VANILLA_CLOUD_SPRITES);
        this.quadSize = scale;
        this.colorMod = 0.8F + this.random.nextFloat() * 0.2F;
        this.hasPhysics = false;
        this.lifetime = 30 + this.random.nextInt(13);
    }

    @Override
    public void tick() {
        double prevY = this.yd;

        super.tick();

        this.updateColor();

        this.yd = prevY;

        this.xd *= 0.75;
        this.yd += 0.005;
        this.zd *= 0.75;

        this.setSpriteFromAge(NtmParticleTypes.VANILLA_CLOUD_SPRITES);
    }

    private void updateColor() {
        float time = (float) this.age / (float) this.lifetime;

        Color color = Color.getHSBColor(Math.max((60 - time * 100) / 360F, 0.0F), 1 - time * 0.25F, 1 - time * 0.5F);

        this.rCol = color.getRed() / 255F;
        this.gCol = color.getGreen() / 255F;
        this.bCol = color.getBlue() / 255F;

        this.rCol *= colorMod;
        this.gCol *= colorMod;
        this.bCol *= colorMod;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTick) {
        return 240;
    }

    public static class Provider extends ParticleProviderBase<NbtParticleOptions> {

        @Override
        public void createParticle(NbtParticleOptions options, double x, double y, double z, double xd, double yd, double zd, ClientLevel level, RandomSource random, ParticleStatus particleStatus) {
            CompoundTag tag = options.tag;

            float scale = tag.getFloat("scale");

            GasFlameParticle particle = new GasFlameParticle(level, x, y, z, xd, yd, zd, scale > 0 ? scale : 0.5F);
            Minecraft.getInstance().particleEngine.add(particle);
        }
    }
}
