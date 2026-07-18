package com.hbm.render.item;

import com.hbm.blocks.machine.NTMAnvilBlock;
import com.hbm.inventory.MetaHelper;
import com.hbm.main.ResourceManager;
import com.hbm.render.model.AnvilBakedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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

        poseStack.mulPose(Axis.YP.rotationDegrees(180F));
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
        this.models = new AnvilBakedModel[] {
                new AnvilBakedModel(ResourceManager.anvil, sprites[0]),
                new AnvilBakedModel(ResourceManager.anvil, sprites[1]),
                new AnvilBakedModel(ResourceManager.anvil, sprites[2]),
                new AnvilBakedModel(ResourceManager.anvil, sprites[3]),
                new AnvilBakedModel(ResourceManager.anvil, sprites[4]),
                new AnvilBakedModel(ResourceManager.anvil, sprites[5]),
                new AnvilBakedModel(ResourceManager.anvil, sprites[6]),
                new AnvilBakedModel(ResourceManager.anvil, sprites[7]),
                new AnvilBakedModel(ResourceManager.anvil, sprites[8]),
                new AnvilBakedModel(ResourceManager.anvil, sprites[9]),
                new AnvilBakedModel(ResourceManager.anvil, sprites[10])
        };
    }
}
