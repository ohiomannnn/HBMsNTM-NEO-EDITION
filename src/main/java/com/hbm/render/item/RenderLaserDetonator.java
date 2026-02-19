package com.hbm.render.item;

import com.hbm.main.ResourceManager;
import com.hbm.render.CustomRenderTypes;
import com.hbm.render.loader.WavefrontObjVBO;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

import java.util.Random;

public class RenderLaserDetonator extends BlockEntityWithoutLevelRenderer {

    public RenderLaserDetonator() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        poseStack.pushPose();

        if (displayContext != ItemDisplayContext.GUI) poseStack.translate(0.5F, 0F, 0.5F);
        switch (displayContext) {
            case FIRST_PERSON_RIGHT_HAND -> {
                poseStack.translate(-0.2F, 0.63F, 0.1F);
                poseStack.scale(0.15F, 0.15F, 0.15F);
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
            }
            case FIRST_PERSON_LEFT_HAND -> {
                poseStack.translate(0.2F, 0.63F, 0.1F);
                poseStack.scale(-0.15F, 0.15F, 0.15F);
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
            }
            case THIRD_PERSON_RIGHT_HAND, HEAD -> {
                poseStack.translate(0F, 0.48F, 0.05F);
                poseStack.scale(0.15F, 0.15F, 0.15F);
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
            }
            case THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(0F, 0.48F, 0.05F);
                poseStack.scale(-0.15F, 0.15F, 0.15F);
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
            }
            case GROUND -> {
                poseStack.translate(0F, 0.3F, 0F);
                poseStack.scale(0.15F, 0.15F, 0.15F);
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));
            }
            case FIXED, GUI -> {
                float s = 0.22F;
                poseStack.scale(s, -s, -s);
                poseStack.translate(1.4F, -1.7F, 0F);
                poseStack.mulPose(Axis.XP.rotationDegrees(180F));
                poseStack.mulPose(Axis.YP.rotationDegrees(-90F));
                poseStack.mulPose(Axis.XP.rotationDegrees(-50F));
            }
        }

        VertexConsumer mainConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.DETONATOR_LASER_TEX));
        ResourceManager.detonator_laser.renderPart("Main", poseStack, mainConsumer, packedLight, packedOverlay);

        VertexConsumer lightsConsumer = buffer.getBuffer(CustomRenderTypes.entitySmothNoLight(ResourceManager.DETONATOR_LASER_TEX));
        ((WavefrontObjVBO) ResourceManager.detonator_laser).renderPart("Lights", poseStack, lightsConsumer, 240, packedOverlay, 1F, 0F, 0F, 1F);

        poseStack.pushPose();

        float px = 0.0625F;
        poseStack.translate(0.5626F, px * 18, -px * 14);

        VertexConsumer sinConsumer = buffer.getBuffer(CustomRenderTypes.GLOW);

        int sub = 32;
        double width = px * 8;
        double len = width / sub;
        double time = System.currentTimeMillis() / -100D;
        double amplitude = 0.075;

        for (int i = 0; i < sub; i++) {
            double h0 = Math.sin(i * 0.5 + time) * amplitude;
            double h1 = Math.sin((i + 1) * 0.5 + time) * amplitude;
            Matrix4f matrix = poseStack.last().pose();
            sinConsumer.addVertex(matrix, 0F, (float) (-px * 0.25 + h1), (float) (len * (i + 1))).setColor(0xFFFFFF00);
            sinConsumer.addVertex(matrix, 0F, (float) (px * 0.25 + h1), (float) (len * (i + 1))).setColor(0xFFFFFF00);
            sinConsumer.addVertex(matrix, 0F, (float) (px * 0.25 + h0), (float) (len * i)).setColor(0xFFFFFF00);
            sinConsumer.addVertex(matrix, 0F, (float) (-px * 0.25 + h0), (float) (len * i)).setColor(0xFFFFFF00);
        }

        poseStack.popPose();

        poseStack.pushPose();

        String s;
        Random rand = new Random(System.currentTimeMillis() / 500);
        Font font = Minecraft.getInstance().font;
        float f3 = 0.01F;
        poseStack.translate(0.5625F, 1.3125F, 0.875F);
        if (displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
            poseStack.scale(f3, -f3, -f3);
            poseStack.translate(0F, 0F, 40F);
        } else {
            poseStack.scale(f3, -f3, f3);
        }
        poseStack.mulPose(Axis.YP.rotationDegrees(90));

        poseStack.translate(3F, -2F, 0.2F);

        Matrix4f matrix = poseStack.last().pose();
        for (int i = 0; i < 3; i++) {
            s = (rand.nextInt(900000) + 100000) + "";
            font.drawInBatch(s, 0, 0, 0xff0000, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 240);
            poseStack.translate(0F, 12.5F, 0F);
        }
        poseStack.popPose();

        poseStack.popPose();
    }
}
