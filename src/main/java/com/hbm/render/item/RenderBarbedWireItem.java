package com.hbm.render.item;

import com.hbm.inventory.MetaHelper;
import com.hbm.main.ResourceManager;
import com.hbm.render.model.BarbedWireBakedModel;
import com.hbm.render.model.SimpleWavefrontBakedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;

public class RenderBarbedWireItem extends ItemRenderBaseStandard {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(0F, -0.25F, 0F);
    }

    @Override
    public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        VertexConsumer consumer = buffer.getBuffer(CUTOUT);
        this.renderModel(poseStack, consumer, models[MetaHelper.getMeta(stack)], packedLight, packedOverlay);
    }

    @Override
    public void registerModelsAndSprites() {
        this.sprites = new TextureAtlasSprite[] {
                this.getSprite("block/barbed_wire"),
                this.getSprite("block/barbed_wire_fire"),
                this.getSprite("block/barbed_wire_poison"),
                this.getSprite("block/barbed_wire_acid"),
                this.getSprite("block/barbed_wire_wither"),
                this.getSprite("block/barbed_wire_ultradeath")
        };
        this.models = new BarbedWireBakedModel[] {
                new BarbedWireBakedModel(ResourceManager.barbed_wire, sprites[0]),
                new BarbedWireBakedModel(ResourceManager.barbed_wire, sprites[1]),
                new BarbedWireBakedModel(ResourceManager.barbed_wire, sprites[2]),
                new BarbedWireBakedModel(ResourceManager.barbed_wire, sprites[3]),
                new BarbedWireBakedModel(ResourceManager.barbed_wire, sprites[4]),
                new BarbedWireBakedModel(ResourceManager.barbed_wire, sprites[5]),
        };
    }
}
