package com.hbm.render.item;

import com.hbm.main.ResourceManager;
import com.hbm.render.model.SimpleWavefrontBakedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;

public class RenderSpikesItem extends ItemRenderBaseStandard {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(0F, -0.25F, 0F);
    }

    @Override
    public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        VertexConsumer consumer = buffer.getBuffer(CUTOUT);
        this.renderModel(poseStack, consumer, models[0], packedLight, packedOverlay);
    }

    @Override
    public void registerModelsAndSprites() {
        this.sprites = new TextureAtlasSprite[] {
                this.getSprite("block/spikes")
        };
        this.models = new SimpleWavefrontBakedModel[] {
                new SimpleWavefrontBakedModel(ResourceManager.spikes, sprites[0]),
        };
    }
}
