package com.hbm.render.item;

import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.util.FullBright;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
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

        RenderContext.setup(poseStack, packedLight, packedOverlay);

        if (displayContext != ItemDisplayContext.GUI) RenderContext.translate(0.5F, 0F, 0.5F);
        switch (displayContext) {
            case FIRST_PERSON_RIGHT_HAND -> {
                RenderContext.translate(-0.2F, 0.63F, 0.1F);
                RenderContext.scale(0.15F, 0.15F, 0.15F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            }
            case FIRST_PERSON_LEFT_HAND -> {
                RenderContext.translate(0.2F, 0.63F, 0.1F);
                RenderContext.scale(-0.15F, 0.15F, 0.15F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            }
            case THIRD_PERSON_RIGHT_HAND, HEAD -> {
                RenderContext.translate(0F, 0.48F, 0.05F);
                RenderContext.scale(0.15F, 0.15F, 0.15F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            }
            case THIRD_PERSON_LEFT_HAND -> {
                RenderContext.translate(0F, 0.48F, 0.05F);
                RenderContext.scale(-0.15F, 0.15F, 0.15F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            }
            case GROUND -> {
                RenderContext.translate(0F, 0.3F, 0F);
                RenderContext.scale(0.15F, 0.15F, 0.15F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            }
            case FIXED, GUI -> {
                float s = 0.22F;
                RenderContext.scale(s, -s, -s);
                RenderContext.translate(1.4F, -1.7F, 0F);
                RenderContext.mulPose(Axis.XP.rotationDegrees(180F));
                RenderContext.mulPose(Axis.YP.rotationDegrees(-90F));
                RenderContext.mulPose(Axis.XP.rotationDegrees(-50F));
            }
        }

        RenderContext.pushPose();

        RenderContext.enableCull(false);

        RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.DETONATOR_LASER_TEX));
        ResourceManager.detonator_laser.renderPart("Main");

        RenderContext.setColor(1F, 0F, 0F, 1F);
        FullBright.enable();
        RenderContext.setRenderType(NtmRenderTypes.FVBO_NL_NT);
        ResourceManager.detonator_laser.renderPart("Lights");
        FullBright.disable();
        RenderContext.setColor(1F, 1F, 1F, 1F);

        RenderContext.enableCull(true);

        RenderContext.popPose();

        RenderContext.pushPose();

        float px = 0.0625F;
        RenderContext.translate(0.5626F, px * 18, -px * 14);

        VertexConsumer sinConsumer = buffer.getBuffer(NtmRenderTypes.GLOW);

        int sub = 32;
        double width = px * 8;
        double len = width / sub;
        double time = System.currentTimeMillis() / -100D;
        double amplitude = 0.075;

        for (int i = 0; i < sub; i++) {
            double h0 = Math.sin(i * 0.5 + time) * amplitude;
            double h1 = Math.sin((i + 1) * 0.5 + time) * amplitude;
            Matrix4f matrix = RenderContext.poseStack().last().pose();
            sinConsumer.addVertex(matrix, 0F, (float) (-px * 0.25 + h1), (float) (len * (i + 1))).setColor(0xFFFFFF00);
            sinConsumer.addVertex(matrix, 0F, (float) (px * 0.25 + h1), (float) (len * (i + 1))).setColor(0xFFFFFF00);
            sinConsumer.addVertex(matrix, 0F, (float) (px * 0.25 + h0), (float) (len * i)).setColor(0xFFFFFF00);
            sinConsumer.addVertex(matrix, 0F, (float) (-px * 0.25 + h0), (float) (len * i)).setColor(0xFFFFFF00);
        }

        RenderContext.popPose();

        RenderContext.pushPose();

        String s;
        Random rand = new Random(System.currentTimeMillis() / 500);
        Font font = Minecraft.getInstance().font;
        float f3 = 0.01F;
        RenderContext.translate(0.5625F, 1.3125F, 0.875F);
        if (displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
            RenderContext.scale(f3, -f3, -f3);
            RenderContext.translate(0F, 0F, 40F);
        } else {
            RenderContext.scale(f3, -f3, f3);
        }
        RenderContext.mulPose(Axis.YP.rotationDegrees(90));

        RenderContext.translate(3F, -2F, 0.2F);

        Matrix4f matrix = RenderContext.poseStack().last().pose();
        for (int i = 0; i < 3; i++) {
            s = (rand.nextInt(900000) + 100000) + "";
            font.drawInBatch(s, 0, 0, 0xff0000, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 240);
            RenderContext.translate(0F, 12.5F, 0F);
        }
        RenderContext.popPose();

        RenderContext.end();
    }
}
