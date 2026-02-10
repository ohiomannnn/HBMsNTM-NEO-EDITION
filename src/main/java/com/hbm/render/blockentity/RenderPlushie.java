package com.hbm.render.blockentity;

import com.hbm.HBMsNTM;
import com.hbm.blocks.generic.PlushieBlock;
import com.hbm.blocks.generic.PlushieBlock.PlushieBlockEntity;
import com.hbm.blocks.generic.PlushieBlock.PlushieType;
import com.hbm.items.ModItems;
import com.hbm.main.ResourceManager;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.IModelCustom;
import com.hbm.render.util.HorsePronter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderPlushie implements BlockEntityRenderer<PlushieBlockEntity> {

    public RenderPlushie(BlockEntityRendererProvider.Context context) { }

    public static final IModelCustom yomiModel = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/trinkets/yomi.obj"), false).asVBO();
    public static final IModelCustom hundunModel = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/trinkets/hundun.obj"), false).asVBO();
    public static final IModelCustom dergModel = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/trinkets/derg.obj"), false).asVBO();
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
                yomiModel.renderAll(poseStack, consumer, packedLight, packedOverlay);
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
                hundunModel.renderPart("goober_posed", poseStack, consumer, packedLight, packedOverlay);
            }
            case DERG -> {
                VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(dergTex));
                dergModel.renderPart("Derg", poseStack, consumer, packedLight, packedOverlay);
                dergModel.renderPart(squish ? "Blep" : "ColonThree", poseStack, consumer, packedLight, packedOverlay);
            }
        }
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
