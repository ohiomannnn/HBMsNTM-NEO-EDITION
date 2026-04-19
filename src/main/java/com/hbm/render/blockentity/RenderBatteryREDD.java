package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.storage.BatteryREDDBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.BeamPronter;
import com.hbm.render.util.BeamPronter.BeamType;
import com.hbm.render.util.BeamPronter.WaveType;
import com.hbm.render.util.FullBright;
import com.hbm.render.util.RenderContext;
import com.hbm.util.BobMathUtil;
import com.hbm.util.Clock;
import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

import java.util.Random;

public class RenderBatteryREDD extends BlockEntityRendererNT<BatteryREDDBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<BatteryREDDBlockEntity> create(Context context) { return new RenderBatteryREDD(); }

    @Override
    public void render(BatteryREDDBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case NORTH -> 270F;
            case EAST -> 0F;
            case SOUTH -> 90F;
            case WEST -> 180F;
        };

        int tPackedLight = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(9));

        RenderContext.setup(NtmRenderTypes.FVBO.apply(ResourceManager.BATTERY_REDD_TEX), poseStack, tPackedLight, packedOverlay);
        RenderContext.translate(0.5, 0, 0.5);
        RenderContext.mulPose(Axis.YP.rotationDegrees(rot));

        ResourceManager.battery_redd.renderPart("Base");

        RenderContext.pushPose();

        RenderContext.translate(0F, 5.5F, 0F);
        float speed = be.getSpeed();
        float wheelRot = be.prevRotation + (be.rotation - be.prevRotation) * partialTicks;
        RenderContext.mulPose(Axis.XP.rotationDegrees(wheelRot));
        RenderContext.translate(0F, -5.5F, 0F);

        ResourceManager.battery_redd.renderPart("Wheel");

        FullBright.enable();
        ResourceManager.battery_redd.renderPart("Lights");
        FullBright.disable();

        RenderContext.pushPose();
        RenderContext.translate(0F, 5.5F, 0F);

        Vec3NT vec = new Vec3NT(0, 0, 4);
        Matrix4f matrix = RenderContext.poseStack().last().pose();

        double len = 4.25D;
        double width = 0.125D;
        double span = speed * 0.75;

        if (span > 0) {
            VertexConsumer glowConsumer = buffer.getBuffer(NtmRenderTypes.GLOW);

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

        RenderContext.popPose();

        this.renderSparkle(be);

        RenderContext.popPose();

        if (speed > 0) this.renderZaps(be);

        RenderContext.end();
    }

    protected void renderSparkle(BatteryREDDBlockEntity be) {
        long time = Clock.get_ms();
        float alpha = 0.45F + (float) (Math.sin(time / 1000D) * 0.15F);
        float alphaMult = be.getSpeed() / 15F;
        float r = 1.0F;
        float g = 0.25F;
        float b = 0.75F;

        double mainOsc = BobMathUtil.sps(time / 1000D) % 1D;
        double sparkleSpin = time / 250D * -1 % 1D;
        double sparkleOsc = Math.sin(time / 1000D) * 0.5D % 1D;

        FullBright.enable();

        RenderContext.setRenderType(NtmRenderTypes.FVBO_ADDITIVE_NL.apply(ResourceManager.FUSION_PLASMA_TEX));
        RenderSystem.setTextureMatrix(new Matrix4f().translate(0f, (float) mainOsc, 0f));
        RenderContext.setColor(r, g, b, alpha * alphaMult);
        ResourceManager.battery_redd.renderPart("Plasma");
        RenderSystem.resetTextureMatrix();

        // cost-cutting measure, don't render extra layers from more than 100m away
        if (Minecraft.getInstance().player.distanceToSqr(be.getBlockPos().getX() + 0.5, be.getBlockPos().getY() + 2.5, be.getBlockPos().getZ()) < 100 * 100) {
            RenderContext.setRenderType(NtmRenderTypes.FVBO_ADDITIVE_NL.apply(ResourceManager.FUSION_PLASMA_SPARKLE_TEX));
            RenderSystem.setTextureMatrix(new Matrix4f().translate((float) sparkleSpin, (float) sparkleOsc, 0f));
            RenderContext.setColor(r * 2, g * 2, b * 2, 0.75F * alphaMult);
            ResourceManager.battery_redd.renderPart("Plasma");
            RenderSystem.resetTextureMatrix();
        }

        FullBright.disable();
    }

    protected void renderZaps(BatteryREDDBlockEntity be) {

        Random rand = new Random(be.getLevel().getGameTime() / 5);
        rand.nextBoolean();

        if (rand.nextBoolean()) {
            RenderContext.pushPose();
            RenderContext.translate(3.125, 5.5, 0);
            BeamPronter.prontBeam(new Vec3NT(-1.375, -2.625, 3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 15, 0.25F, 3, 0.0625F);
            BeamPronter.prontBeam(new Vec3NT(-1.375, -2.625, 3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 1, 0, 3, 0.0625F);
            RenderContext.popPose();
        }

        if (rand.nextBoolean()) {
            RenderContext.pushPose();
            RenderContext.translate(-3.125, 5.5, 0);
            BeamPronter.prontBeam(new Vec3NT(1.375, -2.625, 3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 15, 0.25F, 3, 0.0625F);
            BeamPronter.prontBeam(new Vec3NT(1.375, -2.625, 3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 1, 0, 3, 0.0625F);
            RenderContext.popPose();
        }

        if (rand.nextBoolean()) {
            RenderContext.pushPose();
            RenderContext.translate(3.125, 5.5, 0);
            BeamPronter.prontBeam(new Vec3NT(-1.375, -2.625, -3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 15, 0.25F, 3, 0.0625F);
            BeamPronter.prontBeam(new Vec3NT(-1.375, -2.625, -3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 1, 0, 3, 0.0625F);
            RenderContext.popPose();
        }

        if (rand.nextBoolean()) {
            RenderContext.pushPose();
            RenderContext.translate(-3.125, 5.5, 0);
            BeamPronter.prontBeam(new Vec3NT(1.375, -2.625, -3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 15, 0.25F, 3, 0.0625F);
            BeamPronter.prontBeam(new Vec3NT(1.375, -2.625, -3.75), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int)(System.currentTimeMillis() % 1000) / 50, 1, 0, 3, 0.0625F);
            RenderContext.popPose();
        }
    }

    private AABB bb = null;

    @Override
    public AABB getRenderBoundingBox(BatteryREDDBlockEntity be) {

        if (bb == null) {
            int x = be.getBlockPos().getX();
            int y = be.getBlockPos().getY();
            int z = be.getBlockPos().getZ();

            bb = new AABB(x - 2, y, z - 2, x + 2, y + 10, z + 2);
        }

        return bb;
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.MACHINE_BATTERY_REDD.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0, -3, 0);
                RenderContext.scale(2.5F, 2.5F, 2.5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YN.rotationDegrees(-90F));
                RenderContext.scale(0.5F, 0.5F, 0.5F);

                RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.BATTERY_REDD_TEX));
                ResourceManager.battery_redd.renderPart("Base");
                ResourceManager.battery_redd.renderPart("Wheel");
                RenderContext.setRenderType(NtmRenderTypes.FVBO_NL.apply(ResourceManager.BATTERY_REDD_TEX));
                ResourceManager.battery_redd.renderPart("Lights");
            }
        };
    }
}
