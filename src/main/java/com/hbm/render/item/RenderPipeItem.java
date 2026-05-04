package com.hbm.render.item;

import com.hbm.inventory.MetaHelper;
import com.hbm.main.ResourceManager;
import com.hbm.render.model.PipeNeoBakedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;

public class RenderPipeItem extends ItemRenderBaseStandard {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(0F, 0.15F, 0F);
    }

    @Override
    public void renderNonInv(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean righthand) {
        poseStack.translate(0F, 0.5F, 0F);
    }

    @Override
    public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.scale(1.25F, 1.25F, 1.25F);

        int meta = MetaHelper.getMeta(stack) - 1;

        VertexConsumer consumer = buffer.getBuffer(CUTOUT);
        this.renderModel(stack, poseStack, consumer, models[meta], packedLight, packedOverlay);
    }

    @Override
    public void registerModelsAndSprites() {
        this.sprites = new TextureAtlasSprite[] {
                this.getSprite("block/pipe_neo"),
                this.getSprite("block/pipe_neo_overlay"),
                this.getSprite("block/pipe_colored"),
                this.getSprite("block/pipe_colored_overlay"),
                this.getSprite("block/pipe_silver"),
                this.getSprite("block/pipe_silver_overlay")
        };
        this.models = new PipeNeoBakedModel[] {
                new PipeNeoBakedModel(ResourceManager.pipe_neo, sprites[0], sprites[1], false),
                new PipeNeoBakedModel(ResourceManager.pipe_neo, sprites[2], sprites[3], false),
                new PipeNeoBakedModel(ResourceManager.pipe_neo, sprites[4], sprites[5], false)
        };
    }
}
