package com.hbm.particle;

import com.hbm.HBMsNTM;
import com.hbm.render.CustomRenderTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class ExplosionSmallParticle extends RotatingParticleNT {

    private final float hue;

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/particle/base_particle.png");

    public ExplosionSmallParticle(ClientLevel level, double x, double y, double z, float scale, float speedMultiplier) {
        super(level, x, y, z);
        this.lifetime = 25 + this.random.nextInt(10);
        this.quadSize = scale * 0.9F + this.random.nextFloat() * 0.2F;

        this.xd = this.random.nextGaussian() * speedMultiplier;
        this.zd = this.random.nextGaussian() * speedMultiplier;

        this.gravity = this.random.nextFloat() * -0.01F;

        this.hue = 20F + this.random.nextFloat() * 20F;
        Color base = Color.getHSBColor(hue / 255F, 1F, 1F);
        this.rCol = base.getRed() / 255F;
        this.gCol = base.getGreen() / 255F;
        this.bCol = base.getBlue() / 255F;

        this.noClip = true;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.age++;

        if (this.age >= this.lifetime) {
            this.remove();
        }

        this.yd -= gravity;
        this.oRoll = this.roll;

        float ageScaled = (float) this.age / (float) this.lifetime;
        this.roll += (float) ((1 - ageScaled) * 5 * ((this.hashCode() % 2) - 0.5));

        this.xd *= 0.65D;
        this.zd *= 0.65D;

        this.move(this.xd, this.yd, this.zd);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {

        float ageScaled = (this.age + partialTicks) / this.lifetime;

        Color color = Color.getHSBColor(hue / 255F, Math.max(1F - ageScaled * 2F, 0), Mth.clamp(1.25F - ageScaled * 2F, hue * 0.01F - 0.1F, 1F));
        this.rCol = color.getRed() / 255F;
        this.gCol = color.getGreen() / 255F;
        this.bCol = color.getBlue() / 255F;

        this.alpha = (float) Math.pow(1 - Math.min(ageScaled, 1), 0.25);

        float scale = (float) ((0.25 + 1 - Math.pow(1 - ageScaled, 4) + (this.age + partialTicks) * 0.02) * this.quadSize);

        this.renderParticleRotated(consumer, camera, this.rCol, this.gCol, this.bCol, this.alpha * 0.5F, scale, partialTicks, 240);
    }

    @Override
    public RenderType getRenderType() {
        return CustomRenderTypes.entitySmothNoDepth(TEXTURE);
    }
}