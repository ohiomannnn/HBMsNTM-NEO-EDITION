package com.hbm.render.entity.mob;

import com.hbm.HBMsNTM;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Creeper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CreeperNuclearRenderer extends CreeperRenderer {

    private static final ResourceLocation CREEPER_LOCATION = HBMsNTM.withDefaultNamespaceNT("textures/entity/creeper_nuclear.png");
    private float swellMod = 1.0F;

    public CreeperNuclearRenderer(EntityRendererProvider.Context context) { super(context); }

    protected void scale(Creeper livingEntity, PoseStack poseStack, float partialTickTime) {
        float swell = livingEntity.getSwelling(partialTickTime);
        float flash = 1.0F + Mth.sin(swell * 100.0F) * swell * 0.01F;
        swell = Mth.clamp(swell, 0.0F, 1.0F);
        swell *= swell;
        swell *= swell;
        swell *= swellMod;
        float scaleHorizontal = (1.0F + swell * 0.4F) * flash;
        float scaleVertical = (1.0F + swell * 0.1F) / flash;
        poseStack.scale(scaleHorizontal, scaleVertical, scaleHorizontal);
    }

    @Override
    public ResourceLocation getTextureLocation(Creeper creeper) {
        return CREEPER_LOCATION;
    }
}