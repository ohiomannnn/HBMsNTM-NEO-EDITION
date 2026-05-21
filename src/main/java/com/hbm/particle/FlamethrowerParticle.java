package com.hbm.particle;

import com.hbm.main.NuclearTechMod;
import com.hbm.particle.helper.FlameCreator;
import com.hbm.render.NtmRenderTypes;
import com.hbm.util.old.TessColorUtil;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class FlamethrowerParticle extends RotatingParticleNT {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/particle/base_particle.png");

    public int type;

    public FlamethrowerParticle(ClientLevel level, double x, double y, double z, int type) {
        super(level, x, y, z);

        this.lifetime = 20 + random.nextInt(10);
        this.quadSize = 0.5F;
        this.type = type;

        this.xd = level.random.nextGaussian() * 0.02;
        this.zd = level.random.nextGaussian() * 0.02;

        float initialColor = 15F + random.nextFloat() * 25F;

        if(type == FlameCreator.META_BALEFIRE) initialColor = 65F + random.nextFloat() * 35F;
        if(type == FlameCreator.META_DIGAMMA) initialColor = 0F - random.nextFloat() * 15F;

        Color color = Color.getHSBColor(initialColor / 255F, 1F, 1F);
        this.rCol = color.getRed() / 255F;
        this.gCol = color.getGreen() / 255F;
        this.bCol = color.getBlue() / 255F;

        if(type == FlameCreator.META_OXY) this.rCol = this.gCol = this.bCol = 1F;
        if(type == FlameCreator.META_BLACK) this.rCol = this.gCol = this.bCol = 1F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.age++;

        if(this.age >= this.lifetime) this.remove();

        this.xd *= 0.91D;
        this.yd *= 0.91D;
        this.zd *= 0.91D;

        this.yd += 0.01D;
        this.oRoll = this.roll;
        this.roll += 30 * ((this.hashCode() % 2) - 0.5);

        this.move(this.xd, this.yd, this.zd);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {

        float ageScaled = (float) this.age / this.lifetime;

        int color;

        if(type == FlameCreator.META_OXY) {
            this.alpha = 1 - ageScaled;
            float add = ageScaled * 1.25F - 0.25F;
            color = TessColorUtil.getColorRGBA_F(this.rCol - add, this.gCol - add * 0.75F, this.bCol, this.alpha);
        } else if(type == FlameCreator.META_BLACK) {
            this.alpha = 1 - ageScaled;
            float add = ageScaled * 2F - 0.25F;
            color = TessColorUtil.getColorRGBA_F(this.rCol - add * 0.75F, this.gCol - add, this.bCol - add * 0.5F, this.alpha);
        } else {
            this.alpha = (float) Math.pow(1 - Math.min(ageScaled, 1), 0.5);
            float add = 0.75F - ageScaled;
            color = TessColorUtil.getColorRGBA_F(this.rCol + add, this.gCol + add, this.bCol + add, this.alpha * 0.5F);
        }

        float scale = (ageScaled * 1.25F + 0.25F) * quadSize;
        this.renderParticleRotated(consumer, camera, color, scale, partialTicks, 240);
    }

    @Override
    public RenderType getRenderType() {
        return NtmRenderTypes.SMOTH_NO_DEPTH.apply(TEXTURE);
    }
}
