package com.hbm.particle;

import com.hbm.main.NuclearTechMod;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.awt.*;

public class BlackPowderSmokeParticle extends RotatingParticleNT {

    private final float hue;

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/particle/base_particle.png");
    private static final RenderType BLACK_POWDER = RenderType.create(
            "black_powder_render_type", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 43241,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(TEXTURE, false, false))
                    .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(RenderType.LIGHTMAP)
                    .setOverlayState(RenderType.OVERLAY)
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                    .setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
                    .createCompositeState(false)
    );

    public BlackPowderSmokeParticle(ClientLevel level, double x, double y, double z, float scale) {
        super(level, x, y, z);
        this.lifetime = 30 + this.random.nextInt(15);
        this.quadSize = scale * 0.9F + this.random.nextFloat() * 0.2F;

        this.gravity = 0F;

        this.hue = 20F + this.random.nextFloat() * 20F;
        Color color = Color.getHSBColor(hue / 255F, 1F, 1F);
        this.rCol = color.getRed() / 255F;
        this.gCol = color.getGreen() / 255F;
        this.bCol = color.getBlue() / 255F;

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
        this.roll += (float) ((1 - ageScaled) * 2 * ((this.hashCode() % 2) - 0.5));

        this.xd *= 0.65D;
        this.yd *= 0.65D;
        this.zd *= 0.65D;

        this.move(this.xd, this.yd, this.zd);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        float ageScaled = (this.age + partialTicks) / this.lifetime;

        Color color = Color.getHSBColor(hue / 255F, Math.max(1F - ageScaled * 4F, 0), Mth.clamp(1.25F - ageScaled * 2F, 0.7F, 1F));
        this.rCol = color.getRed() / 255F;
        this.gCol = color.getGreen() / 255F;
        this.bCol = color.getBlue() / 255F;

        this.alpha = (float) Math.pow(1 - Math.min(ageScaled, 1), 0.25);

        float scale = (float) ((0.25 + ageScaled + (this.lifetime + partialTicks) * 0.025) * this.quadSize);

        this.renderParticleRotated(consumer, camera, this.rCol, this.gCol, this.bCol, this.alpha * 0.25F, scale, partialTicks, LightTexture.FULL_BRIGHT);
    }

    @Override public RenderType getRenderType() { return BLACK_POWDER; }
}
