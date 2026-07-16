package com.hbm.render.item;

import com.hbm.inventory.MetaHelper;
import com.hbm.blocks.machine.NTMAnvilBlock;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.model.SimpleWavefrontBakedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;

public class RenderAnvilItem extends ItemRenderBaseStandard {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(0F, -0.25F, 0F);
    }

    @Override
    public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        int meta = Math.floorMod(MetaHelper.getMeta(stack), NTMAnvilBlock.Variant.values().length);

        VertexConsumer consumer = buffer.getBuffer(SOLID);
        this.renderModel(poseStack, consumer, models[meta], packedLight, packedOverlay);
    }

    @Override
    public void registerModelsAndSprites() {
        this.sprites = new TextureAtlasSprite[] {
                this.getSprite("block/anvil_iron"),
                this.getSprite("block/anvil_lead"),
                this.getSprite("block/anvil_steel"),
                this.getSprite("block/anvil_desh"),
                this.getSprite("block/anvil_ferrouranium"),
                this.getSprite("block/anvil_saturnite"),
                this.getSprite("block/anvil_bismuth_bronze"),
                this.getSprite("block/anvil_arsenic_bronze"),
                this.getSprite("block/anvil_ferric_schrabidate"),
                this.getSprite("block/anvil_dineutronium"),
                this.getSprite("block/anvil_osmiridium")
        };
        this.models = new SimpleWavefrontBakedModel[] {
                new SimpleWavefrontBakedModel(new HFRWavefrontObject("models/obj/machines/anvil_iron.obj"), sprites[0]),
                new SimpleWavefrontBakedModel(new HFRWavefrontObject("models/obj/machines/anvil_lead.obj"), sprites[1]),
                new SimpleWavefrontBakedModel(new HFRWavefrontObject("models/obj/machines/anvil_steel.obj"), sprites[2]),
                new SimpleWavefrontBakedModel(new HFRWavefrontObject("models/obj/machines/anvil_desh.obj"), sprites[3]),
                new SimpleWavefrontBakedModel(new HFRWavefrontObject("models/obj/machines/anvil_ferrouranium.obj"), sprites[4]),
                new SimpleWavefrontBakedModel(new HFRWavefrontObject("models/obj/machines/anvil_saturnite.obj"), sprites[5]),
                new SimpleWavefrontBakedModel(new HFRWavefrontObject("models/obj/machines/anvil_bismuth_bronze.obj"), sprites[6]),
                new SimpleWavefrontBakedModel(new HFRWavefrontObject("models/obj/machines/anvil_arsenic_bronze.obj"), sprites[7]),
                new SimpleWavefrontBakedModel(new HFRWavefrontObject("models/obj/machines/anvil_ferric_schrabidate.obj"), sprites[8]),
                new SimpleWavefrontBakedModel(new HFRWavefrontObject("models/obj/machines/anvil_dineutronium.obj"), sprites[9]),
                new SimpleWavefrontBakedModel(new HFRWavefrontObject("models/obj/machines/anvil_osmiridium.obj"), sprites[10])
        };
    }
}
