package com.hbm.render.blockentity;

import com.hbm.HBMsNTM;
import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.PlushieBlock;
import com.hbm.blocks.generic.PlushieBlock.PlushieBlockEntity;
import com.hbm.blocks.generic.PlushieBlock.PlushieType;
import com.hbm.items.ModItems;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.HorsePronter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class RenderPlushie extends BlockEntityRendererNT<PlushieBlockEntity> implements IBEWLRProvider {

    public RenderPlushie(Context context) { }

    @Override
    public BlockEntityRenderer<PlushieBlockEntity> create(Context context) {
        return new RenderPlushie(context);
    }

    public static final ResourceLocation yomiTex = HBMsNTM.withDefaultNamespaceNT("textures/models/trinkets/yomi.png");
    public static final ResourceLocation numbernineTex = HBMsNTM.withDefaultNamespaceNT("textures/models/horse/numbernine.png");
    public static final ResourceLocation hundunTex = HBMsNTM.withDefaultNamespaceNT("textures/models/trinkets/hundun.png");
    public static final ResourceLocation dergTex = HBMsNTM.withDefaultNamespaceNT("textures/models/trinkets/derg.png");

    @Override
    public void render(PlushieBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0F, 0.5F);
        poseStack.mulPose(Axis.YN.rotationDegrees((float) (22.5D * be.getBlockState().getValue(PlushieBlock.DIRECTION) + 90)));

        if (be.squishTimer > 0) {
            double squish = be.squishTimer - partialTicks;
            poseStack.scale(1F,  (float) (1F + (-(Math.sin(squish)) * squish) * 0.025F), 1F);
        }

        switch (be.type) {
            case YOMI -> poseStack.scale(0.5F, 0.5F, 0.5F);
            case NUMBERNINE -> poseStack.scale(0.75F, 0.75F, 0.75F);
            case HUNDUN -> poseStack.scale(1F, 1F, 1F);
        }

        renderPlushie(poseStack, buffer, packedLight, packedOverlay, be.type, be.squishTimer > 0);

        poseStack.popPose();
    }

    public static void renderPlushie(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, PlushieType type, boolean squish) {

        switch (type) {
            case YOMI -> {
                VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(yomiTex));
                ResourceManager.yomiModel.renderAll(poseStack, consumer, packedLight, packedOverlay);
            }
            case NUMBERNINE -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));
                poseStack.mulPose(Axis.XN.rotationDegrees(15));
                poseStack.translate(0F, -0.25F, 0.75F);
                HorsePronter.reset();
                double r = 45;
                HorsePronter.pose(HorsePronter.id_body, 0, -r, 0);
                HorsePronter.pose(HorsePronter.id_tail, 0, 60, 90);
                HorsePronter.pose(HorsePronter.id_lbl, 0, -75 + r, 35);
                HorsePronter.pose(HorsePronter.id_rbl, 0, -75 + r, -35);
                HorsePronter.pose(HorsePronter.id_lfl, 0, r - 25, 5);
                HorsePronter.pose(HorsePronter.id_rfl, 0, r - 25, -5);
                HorsePronter.pose(HorsePronter.id_head, 0, r + 15, 0);
                HorsePronter.pront(buffer, poseStack, packedLight, packedOverlay, numbernineTex);
                poseStack.mulPose(Axis.XP.rotationDegrees(15F));

                poseStack.pushPose();
                poseStack.translate(0F, 1F, -0.6875F);
                float s = 1.125F;
                poseStack.scale(0.0625F * s, 0.0625F * s, 0.0625F * s);
                poseStack.mulPose(Axis.XP.rotationDegrees(180F));
                VertexConsumer consumerHelmet = buffer.getBuffer(RenderType.entitySmoothCutout(ResourceManager.NO9));
                ResourceManager.armor_no9.renderPart("Helmet", poseStack, consumerHelmet, packedLight, packedOverlay);
                VertexConsumer consumerInsignia = buffer.getBuffer(RenderType.entitySmoothCutout(ResourceManager.NO9_INSIGNIA));
                ResourceManager.armor_no9.renderPart("Insignia", poseStack, consumerInsignia, packedLight, packedOverlay);
                poseStack.popPose();

                ItemStack stack = new ItemStack(ModItems.CIGARETTE.get());
                float scale = 0.25F;
                poseStack.translate(-0.06F, 1.13F, -0.28F);
                poseStack.scale(scale, scale, scale);
                poseStack.mulPose(Axis.YN.rotationDegrees(90F));
                poseStack.mulPose(Axis.ZN.rotationDegrees(60F));
                Minecraft mc = Minecraft.getInstance();
                ItemRenderer itemRenderer = mc.getItemRenderer();
                BakedModel model = itemRenderer.getModel(stack, null, null, 0);
                itemRenderer.render(stack, ItemDisplayContext.FIXED, false, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, model);
            }
            case HUNDUN -> {
                VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(hundunTex));
                ResourceManager.hundunModel.renderPart("goober_posed", poseStack, consumer, packedLight, packedOverlay);
            }
            case DERG -> {
                VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(dergTex));
                ResourceManager.dergModel.renderPart("Derg", poseStack, consumer, packedLight, packedOverlay);
                ResourceManager.dergModel.renderPart(squish ? "Blep" : "ColonThree", poseStack, consumer, packedLight, packedOverlay);
            }
        }
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public Item getItemForRenderer() {
        return null;
    }

    @Override
    public Item[] getItemsForRenderer() {
        return new Item[] {
                ModBlocks.PLUSHIE_YOMI.asItem(),
                ModBlocks.PLUSHIE_NUMBERNINE.asItem(),
                ModBlocks.PLUSHIE_HUNDUN.asItem(),
                ModBlocks.PLUSHIE_DERG.asItem()
        };
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(0F, -6F, 0F);
                poseStack.scale(6F, 6F, 6F);
            }

            @Override
            public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(0F, 0.25F, 0F);

                PlushieType type = getType(stack);

                if (type == null) return;

                switch (type) {
                    case YOMI -> poseStack.scale(1.25F, 1.25F, 1.25F);
                    case NUMBERNINE -> {
                        poseStack.translate(0F, 0.25F, 0.25F);
                        poseStack.scale(1.25F, 1.25F, 1.25F);
                    }
                    case HUNDUN -> {
                        poseStack.translate(0.5F, 0.5F, 0);
                        poseStack.scale(1.25F, 1.25F, 1.25F);
                    }
                    case DERG -> poseStack.scale(1.5F, 1.5F, 1.5F);
                }

                renderPlushie(poseStack, buffer, packedLight, packedOverlay, type, false);
            }
        };
    }

    @Nullable
    private static PlushieType getType(ItemStack stack) {
        if (stack.is(ModBlocks.PLUSHIE_YOMI.asItem())) return PlushieType.YOMI;
        if (stack.is(ModBlocks.PLUSHIE_NUMBERNINE.asItem())) return PlushieType.NUMBERNINE;
        if (stack.is(ModBlocks.PLUSHIE_HUNDUN.asItem())) return PlushieType.HUNDUN;
        if (stack.is(ModBlocks.PLUSHIE_DERG.asItem())) return PlushieType.DERG;
        return null;
    }
}
