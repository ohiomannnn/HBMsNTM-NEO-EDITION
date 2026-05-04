package com.hbm.render.item;

import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.model.BarrelBakedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.data.ModelData;

public class RenderBarrelItem extends ItemRenderBaseStandard {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(0F, -0.3F, 0F);
    }

    @Override
    public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(-0.5, 0, -0.5);

        VertexConsumer consumer = buffer.getBuffer(SOLID);
        this.renderModel(poseStack, consumer, models[this.getBarrelType(stack)], packedLight, packedOverlay);
    }

    private int getBarrelType(ItemStack stack) {
        if(stack.is(ModBlocks.BARREL_RED.asItem()))   return 0;
        if(stack.is(ModBlocks.BARREL_PINK.asItem()))  return 1;
        if(stack.is(ModBlocks.BARREL_LOX.asItem()))   return 2;
        if(stack.is(ModBlocks.BARREL_TAINT.asItem())) return 3;
        return 0;
    }

    @Override
    public void registerModelsAndSprites() {
        this.sprites = new TextureAtlasSprite[] {
                this.getSprite("block/barrel_red"),
                this.getSprite("block/barrel_pink"),
                this.getSprite("block/barrel_lox"),
                this.getSprite("block/barrel_taint")
        };
        this.models = new BarrelBakedModel[] {
                new BarrelBakedModel(ResourceManager.barrel, sprites[0]),
                new BarrelBakedModel(ResourceManager.barrel, sprites[1]),
                new BarrelBakedModel(ResourceManager.barrel, sprites[2]),
                new BarrelBakedModel(ResourceManager.barrel, sprites[3]),
        };
    }
}
