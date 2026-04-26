package com.hbm.render.item;

import com.hbm.main.ResourceManager;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.model.CableBakedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;

public class RenderCableItem extends ItemRenderBaseStandard {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(0F, 0.05F, 0F);
    }

    @Override
    public void renderNonInv(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean righthand) {
        poseStack.translate(0F, 0.4F, 0F);
    }

    @Override
    public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        if(this.sprites == null || this.models == null) {
            this.sprites = new TextureAtlasSprite[] {
                    this.getSprite("block/red_cable")
            };
            this.models = new CableBakedModel[] {
                new CableBakedModel(ResourceManager.cable_neo, sprites[0], false)
            };
        }

        poseStack.scale(1.25F, 1.25F, 1.25F);
        poseStack.translate(0F, 0.1F, 0F);

        this.renderModel(poseStack, buffer, models[0], packedLight, packedOverlay);
    }
}
