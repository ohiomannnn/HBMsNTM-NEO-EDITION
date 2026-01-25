package com.hbm.render.blockentity;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.machine.storage.MachineFluidTankBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.trait.FT_Corrosive;
import com.hbm.main.ResourceManager;
import com.hbm.render.util.DiamondPronter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

import java.util.Locale;

public class RenderFluidTank implements BlockEntityRenderer<MachineFluidTankBlockEntity> {

    public RenderFluidTank(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(MachineFluidTankBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case NORTH -> 180F;
            case EAST -> 90F;
            case SOUTH -> 0F;
            case WEST -> 270F;
        };

        poseStack.pushPose();
        poseStack.translate(0.5F, 0F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        FluidType type = be.tank.getTankType();

        VertexConsumer frameConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.TANK_TEX));
        VertexConsumer tankConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(HBMsNTM.withDefaultNamespaceNT(getTextureFromType(type))));

        if (!be.hasExploded) {
            ResourceManager.fluid_tank.renderPart("Frame", poseStack, frameConsumer, packedLight, packedOverlay);
            ResourceManager.fluid_tank.renderPart("Tank", poseStack, tankConsumer, packedLight, packedOverlay);
        } else {
            ResourceManager.fluid_tank_exploded.renderPart("Frame", poseStack, frameConsumer, packedLight, packedOverlay);
            VertexConsumer innerConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.TANK_INNER_TEX));
            ResourceManager.fluid_tank_exploded.renderPart("TankInner", poseStack, innerConsumer, packedLight, packedOverlay);
            ResourceManager.fluid_tank_exploded.renderPart("Tank", poseStack, tankConsumer, packedLight, packedOverlay);
        }

        if (type != Fluids.NONE) {
            poseStack.pushPose();
            poseStack.translate(-0.25F, 0.5F, -1.501F);
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            poseStack.scale(1.0F, 0.375F, 0.375F);
            DiamondPronter.pront(buffer, poseStack, packedLight, type.poison, type.flammability, type.reactivity, type.symbol);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.25F, 0.5F, 1.501F);
            poseStack.mulPose(Axis.YN.rotationDegrees(90));
            poseStack.scale(1.0F, 0.375F, 0.375F);
            DiamondPronter.pront(buffer, poseStack, packedLight, type.poison, type.flammability, type.reactivity, type.symbol);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    public static String getTextureFromType(FluidType type) {
        String s = type.getInternalName().toLowerCase(Locale.US);

        if (type.isAntimatter() || (type.hasTrait(FT_Corrosive.class) && type.getTrait(FT_Corrosive.class).isHighlyCorrosive())) {
            s = "danger";
        }

        return "textures/models/tank/tank_" + s + ".png";
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
