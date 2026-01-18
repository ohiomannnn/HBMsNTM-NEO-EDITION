package com.hbm.render.blockentity;

import com.hbm.HBMsNTMClient;
import com.hbm.blockentity.machine.storage.BatteryREDDBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.main.ResourceManager;
import com.hbm.render.CustomRenderTypes;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.util.BeamPronter;
import com.hbm.render.util.BeamPronter.BeamType;
import com.hbm.render.util.BeamPronter.WaveType;
import com.hbm.render.util.OffsetVertexConsumer;
import com.hbm.util.BobMathUtil;
import com.hbm.util.Clock;
import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

import java.util.Random;

public class RenderBatteryREDD implements BlockEntityRenderer<BatteryREDDBlockEntity> {

    public RenderBatteryREDD(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(BatteryREDDBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        float rot = switch (facing) {
            case NORTH -> 270f;
            case SOUTH -> 90f;
            case WEST -> 0f;
            default -> 180f;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.BATTERY_REDD_TEX));
        ResourceManager.battery_redd.renderPart("Base", poseStack, consumer, packedLight, packedOverlay);

        poseStack.pushPose();

        poseStack.translate(0F, 5.5F, 0F);
        float speed = be.getSpeed();
        float wheelRot = be.prevRotation + (be.rotation - be.prevRotation) * partialTicks;
        poseStack.mulPose(Axis.XP.rotationDegrees(wheelRot));
        poseStack.translate(0F, -5.5F, 0F);

        ResourceManager.battery_redd.renderPart("Wheel",  poseStack, consumer, packedLight, packedOverlay);
        ResourceManager.battery_redd.renderPart("Lights",  poseStack, consumer, 240, packedOverlay);

        poseStack.pushPose();
        poseStack.translate(0F, 5.5F, 0F);

        Vec3NT vec = new Vec3NT(0, 0, 4);
        Matrix4f matrix = poseStack.last().pose();

        double len = 4.25D;
        double width = 0.125D;
        double span = speed * 0.75;

        if (span > 0) {
            VertexConsumer glowConsumer = buffer.getBuffer(CustomRenderTypes.GLOW);

            for (int j = -1; j <= 1; j += 2) {
                for (int i = 0; i < 8; i++) {
                    double xOffset = 0.8125 * j;
                    vec.setComponents(0, 1, 0);
                    vec.rotateAroundXDeg(i * 45D);

                    glowConsumer.addVertex(matrix, (float) xOffset, (float) (vec.yCoord * len - vec.yCoord * width), (float) (vec.zCoord * len - vec.zCoord * width)).setColor(1F, 1F, 0F, 0.75F);
                    glowConsumer.addVertex(matrix, (float) xOffset, (float) (vec.yCoord * len + vec.yCoord * width), (float) (vec.zCoord * len + vec.zCoord * width)).setColor(1F, 1F, 0F, 0.75F);
                    vec.rotateAroundXDeg(span);
                    glowConsumer.addVertex(matrix, (float) xOffset, (float) (vec.yCoord * len + vec.yCoord * width), (float) (vec.zCoord * len + vec.zCoord * width)).setColor(1F, 1F, 0F, 0.5F);
                    glowConsumer.addVertex(matrix, (float) xOffset, (float) (vec.yCoord * len - vec.yCoord * width), (float) (vec.zCoord * len - vec.zCoord * width)).setColor(1F, 1F, 0F, 0.5F);

                    glowConsumer.addVertex(matrix, (float) xOffset, (float) (vec.yCoord * len - vec.yCoord * width), (float) (vec.zCoord * len - vec.zCoord * width)).setColor(1F, 1F, 0F, 0.5F);
                    glowConsumer.addVertex(matrix, (float) xOffset, (float) (vec.yCoord * len + vec.yCoord * width), (float) (vec.zCoord * len + vec.zCoord * width)).setColor(1F, 1F, 0F, 0.5F);
                    vec.rotateAroundXDeg(span);
                    glowConsumer.addVertex(matrix, (float) xOffset, (float) (vec.yCoord * len + vec.yCoord * width), (float) (vec.zCoord * len + vec.zCoord * width)).setColor(1F, 1F, 0F, 0.25F);
                    glowConsumer.addVertex(matrix, (float) xOffset, (float) (vec.yCoord * len - vec.yCoord * width), (float) (vec.zCoord * len - vec.zCoord * width)).setColor(1F, 1F, 0F, 0.25F);

                    glowConsumer.addVertex(matrix, (float) xOffset, (float) (vec.yCoord * len - vec.yCoord * width), (float) (vec.zCoord * len - vec.zCoord * width)).setColor(1F, 1F, 0F, 0.25F);
                    glowConsumer.addVertex(matrix, (float) xOffset, (float) (vec.yCoord * len + vec.yCoord * width), (float) (vec.zCoord * len + vec.zCoord * width)).setColor(1F, 1F, 0F, 0.25F);
                    vec.rotateAroundXDeg(span);
                    glowConsumer.addVertex(matrix, (float) xOffset, (float) (vec.yCoord * len + vec.yCoord * width), (float) (vec.zCoord * len + vec.zCoord * width)).setColor(1F, 1F, 0F, 0F);
                    glowConsumer.addVertex(matrix, (float) xOffset, (float) (vec.yCoord * len - vec.yCoord * width), (float) (vec.zCoord * len - vec.zCoord * width)).setColor(1F, 1F, 0F, 0F);
                }
            }
        }

        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.depthMask(true);

        poseStack.popPose();

        this.renderSparkle(be, poseStack, buffer, packedOverlay);

        poseStack.popPose();

        if (speed > 0) renderZaps(be, buffer, poseStack);

        poseStack.popPose();
    }

    protected void renderSparkle(BatteryREDDBlockEntity be, PoseStack poseStack, MultiBufferSource buffer, int packedOverlay) {
        long time = Clock.get_ms();
        float alpha = 0.45F + (float) (Math.sin(time / 1000D) * 0.15F);
        float alphaMult = be.getSpeed() / 15F;
        float r = 1.0F;
        float g = 0.25F;
        float b = 0.75F;

        double mainOsc = BobMathUtil.sps(time / 1000D) % 1D;
        double sparkleSpin = time / 250D * -1 % 1D;
        double sparkleOsc = Math.sin(time / 1000D) * 0.5D % 1D;

        // why did i start using shaders...
        VertexConsumer plasmaConsumer = buffer.getBuffer(CustomRenderTypes.entityAdditive(ResourceManager.FUSION_PLASMA_TEX));
        VertexConsumer offsetConsumer = new OffsetVertexConsumer(plasmaConsumer, 0, (float) mainOsc);
        ((HFRWavefrontObject) ResourceManager.battery_redd)
                .renderPart("Plasma", poseStack, offsetConsumer, 240, packedOverlay, r, g, b, alpha * alphaMult);

        // cost-cutting measure, don't render extra layers from more than 100m away
        if (HBMsNTMClient.me().distanceToSqr(be.getBlockPos().getX() + 0.5, be.getBlockPos().getY() + 2.5, be.getBlockPos().getZ()) < 100 * 100) {
            VertexConsumer sparkleConsumer = buffer.getBuffer(CustomRenderTypes.entityAdditive(ResourceManager.FUSION_PLASMA_SPARKLE_TEX));
            VertexConsumer offsetSparkleConsumer = new OffsetVertexConsumer(sparkleConsumer, (float) sparkleSpin, (float) sparkleOsc);
            ((HFRWavefrontObject) ResourceManager.battery_redd)
                    .renderPart("Plasma", poseStack, offsetSparkleConsumer, 240, packedOverlay, r * 2, g * 2, b * 2, 0.75F * alphaMult);
        }
    }

    protected void renderZaps(BatteryREDDBlockEntity be, MultiBufferSource buffer, PoseStack poseStack) {

        Random rand = new Random(be.getLevel().getGameTime() / 5);
        rand.nextBoolean();

        if (rand.nextBoolean()) {
            poseStack.pushPose();
            poseStack.translate(3.125, 5.5, 0);
            BeamPronter.prontBeam(poseStack, buffer, new Vec3NT(-1.375, -2.625, 3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 15, 0.25F, 3, 0.0625F);
            BeamPronter.prontBeam(poseStack, buffer, new Vec3NT(-1.375, -2.625, 3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 1, 0, 3, 0.0625F);
            poseStack.popPose();
        }

        if (rand.nextBoolean()) {
            poseStack.pushPose();
            poseStack.translate(-3.125, 5.5, 0);
            BeamPronter.prontBeam(poseStack, buffer, new Vec3NT(1.375, -2.625, 3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 15, 0.25F, 3, 0.0625F);
            BeamPronter.prontBeam(poseStack, buffer, new Vec3NT(1.375, -2.625, 3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 1, 0, 3, 0.0625F);
            poseStack.popPose();
        }

        if (rand.nextBoolean()) {
            poseStack.pushPose();
            poseStack.translate(3.125, 5.5, 0);
            BeamPronter.prontBeam(poseStack, buffer, new Vec3NT(-1.375, -2.625, -3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 15, 0.25F, 3, 0.0625F);
            BeamPronter.prontBeam(poseStack, buffer, new Vec3NT(-1.375, -2.625, -3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 1, 0, 3, 0.0625F);
            poseStack.popPose();
        }

        if (rand.nextBoolean()) {
            poseStack.pushPose();
            poseStack.translate(-3.125, 5.5, 0);
            BeamPronter.prontBeam(poseStack, buffer, new Vec3NT(1.375, -2.625, -3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 15, 0.25F, 3, 0.0625F);
            BeamPronter.prontBeam(poseStack, buffer, new Vec3NT(1.375, -2.625, -3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 1, 0, 3, 0.0625F);
            poseStack.popPose();
        }
    }

    private AABB bb = null;

    @Override
    public AABB getRenderBoundingBox(BatteryREDDBlockEntity blockEntity) {

        if (bb == null) {
            bb = new AABB(
                    blockEntity.getBlockPos().getX() - 4,
                    blockEntity.getBlockPos().getY(),
                    blockEntity.getBlockPos().getZ() - 4,
                    blockEntity.getBlockPos().getX() - 5,
                    blockEntity.getBlockPos().getY() - 10,
                    blockEntity.getBlockPos().getZ() - 5
            );
        }

        return bb;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
