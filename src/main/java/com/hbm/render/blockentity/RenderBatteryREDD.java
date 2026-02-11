package com.hbm.render.blockentity;

import com.hbm.HBMsNTMClient;
import com.hbm.blockentity.machine.storage.BatteryREDDBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.CustomRenderTypes;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.loader.WavefrontObjVBO;
import com.hbm.render.util.BeamPronter;
import com.hbm.render.util.BeamPronter.BeamType;
import com.hbm.render.util.BeamPronter.WaveType;
import com.hbm.render.util.OffsetVertexConsumer;
import com.hbm.util.BobMathUtil;
import com.hbm.util.Clock;
import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

import java.util.Random;

public class RenderBatteryREDD extends BlockEntityRendererNT<BatteryREDDBlockEntity> implements IBEWLRProvider {

    public RenderBatteryREDD(Context context) { }

    @Override
    public BlockEntityRenderer<BatteryREDDBlockEntity> create(Context context) {
        return new RenderBatteryREDD(context);
    }

    @Override
    public void render(BatteryREDDBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 180F;
            case SOUTH -> 90F;
            case EAST -> 0F;
            case NORTH -> 270F;
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
        ResourceManager.battery_redd.renderPart("Lights",  poseStack, consumer, LightTexture.FULL_BRIGHT, packedOverlay);

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
        ((WavefrontObjVBO) ResourceManager.battery_redd).renderPart("Plasma", poseStack, offsetConsumer, LightTexture.FULL_BRIGHT, packedOverlay, r, g, b, alpha * alphaMult);

        // cost-cutting measure, don't render extra layers from more than 100m away
        if (HBMsNTMClient.me().distanceToSqr(be.getBlockPos().getX() + 0.5, be.getBlockPos().getY() + 2.5, be.getBlockPos().getZ()) < 100 * 100) {
            VertexConsumer sparkleConsumer = buffer.getBuffer(CustomRenderTypes.entityAdditive(ResourceManager.FUSION_PLASMA_SPARKLE_TEX));
            VertexConsumer offsetSparkleConsumer = new OffsetVertexConsumer(sparkleConsumer, (float) sparkleSpin, (float) sparkleOsc);
            ((WavefrontObjVBO) ResourceManager.battery_redd).renderPart("Plasma", poseStack, offsetSparkleConsumer, LightTexture.FULL_BRIGHT, packedOverlay, r * 2, g * 2, b * 2, 0.75F * alphaMult);
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
    public AABB getRenderBoundingBox(BatteryREDDBlockEntity be) {

        if (bb == null) {
            int x = be.getBlockPos().getX();
            int y = be.getBlockPos().getX();
            int z = be.getBlockPos().getX();

            bb = new AABB(
                    x - 4,
                    y - 0,
                    z - 4,
                    x - 5,
                    y - 10,
                    z - 5
            );
        }

        return bb;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.MACHINE_BATTERY_REDD.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(0, -3, 0);
                poseStack.scale(2.5F, 2.5F, 2.5F);
            }

            @Override
            public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.mulPose(Axis.YN.rotationDegrees(-90F));
                poseStack.scale(0.5F, 0.5F, 0.5F);
                VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.BATTERY_REDD_TEX));
                ResourceManager.battery_redd.renderPart("Base", poseStack, consumer, packedLight, packedOverlay);
                ResourceManager.battery_redd.renderPart("Wheel", poseStack, consumer, packedLight, packedOverlay);
                ResourceManager.battery_redd.renderPart("Lights", poseStack, consumer, 240, packedOverlay);
            }
        };
    }
}
