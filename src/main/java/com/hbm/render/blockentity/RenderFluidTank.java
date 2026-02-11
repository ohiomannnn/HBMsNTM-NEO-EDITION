package com.hbm.render.blockentity;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.machine.storage.MachineFluidTankBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Corrosive;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.DiamondPronter;
import com.hbm.util.TagsUtilDegradation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public class RenderFluidTank extends BlockEntityRendererNT<MachineFluidTankBlockEntity> implements IBEWLRProvider {

    public RenderFluidTank(Context context) { }

    @Override
    public BlockEntityRenderer<MachineFluidTankBlockEntity> create(Context context) {
        return new RenderFluidTank(context);
    }

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

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.MACHINE_FLUID_TANK.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(0, -2, 0);
                poseStack.scale(3.5F, 3.5F, 3.5F);
            }

            @Override
            public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));
                poseStack.scale(0.75F, 0.75F, 0.75F);

                FluidTank tank = new FluidTank(Fluids.NONE, 0);
                boolean exploded = false;
                if (TagsUtilDegradation.getTag(stack).contains(IPersistentNBT.NBT_PERSISTENT_KEY)) {
                    CompoundTag persistentTag = TagsUtilDegradation.getTag(stack).getCompound(IPersistentNBT.NBT_PERSISTENT_KEY);
                    tank.readFromNBT(persistentTag, "Tank");
                    exploded = persistentTag.getBoolean("HasExploded");
                }

                FluidType type = tank.getTankType();

                VertexConsumer frameConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.TANK_TEX));
                if (!exploded) {
                    ResourceManager.fluid_tank.renderPart("Frame", poseStack, frameConsumer, packedLight, packedOverlay);
                    VertexConsumer tankConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(HBMsNTM.withDefaultNamespaceNT(RenderFluidTank.getTextureFromType(type))));
                    ResourceManager.fluid_tank.renderPart("Tank", poseStack, tankConsumer, packedLight, packedOverlay);
                } else {
                    ResourceManager.fluid_tank_exploded.renderPart("Frame", poseStack, frameConsumer, packedLight, packedOverlay);
                    VertexConsumer innerConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.TANK_INNER_TEX));
                    ResourceManager.fluid_tank_exploded.renderPart("TankInner", poseStack, innerConsumer, packedLight, packedOverlay);
                    VertexConsumer tankConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(HBMsNTM.withDefaultNamespaceNT(RenderFluidTank.getTextureFromType(type))));
                    ResourceManager.fluid_tank_exploded.renderPart("Tank", poseStack, tankConsumer, packedLight, packedOverlay);
                }
            }
        };
    }
}
