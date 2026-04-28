package com.hbm.render.item;

import com.hbm.main.ResourceManager;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.model.DetCordBakedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;

public class RenderDetCordItem extends ItemRenderBaseStandard {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(0F, 0.05F, 0F);
    }

    @Override
    public void renderNonInv(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean righthand) {
        poseStack.translate(0F, 0.4F, 0F);
    }

    @Override
    public void renderFirstPerson(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean righthand) {
        poseStack.mulPose(Axis.YP.rotationDegrees(90F));
    }

    @Override
    public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        if(this.sprites == null || this.models == null) {
            this.sprites = new TextureAtlasSprite[] {
                    this.getSprite("block/det_cord")
            };
            this.models = new DetCordBakedModel[] {
                new DetCordBakedModel(ResourceManager.cable_neo, sprites[0], false)
            };
        }

        poseStack.scale(1.25F, 1.25F, 1.25F);
        poseStack.translate(0F, 0.1F, 0F);

        VertexConsumer consumer = buffer.getBuffer(CUTOUT);
        this.renderModel(poseStack, consumer, models[0], packedLight, packedOverlay);
    }
}
