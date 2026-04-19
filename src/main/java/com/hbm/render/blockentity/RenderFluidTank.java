package com.hbm.render.blockentity;

import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.machine.storage.MachineFluidTankBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Corrosive;
import com.hbm.main.NuclearTechMod;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.DiamondPronter;
import com.hbm.render.util.RenderContext;
import com.hbm.util.TagsUtilDegradation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.Locale;

public class RenderFluidTank extends BlockEntityRendererNT<MachineFluidTankBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<MachineFluidTankBlockEntity> create(Context context) { return new RenderFluidTank(); }

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

        RenderContext.setup(poseStack, packedLight, packedOverlay);
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(rot));

        FluidType type = be.tank.getTankType();

        if (!be.hasExploded) {
            RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(ResourceManager.TANK_TEX));
            ResourceManager.fluid_tank.renderPart("Frame");
            RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(NuclearTechMod.withDefaultNamespace(getTextureFromType(type))));
            ResourceManager.fluid_tank.renderPart("Tank");
        } else {
            RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(ResourceManager.TANK_TEX));
            ResourceManager.fluid_tank_exploded.renderPart("Frame");
            RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(ResourceManager.TANK_INNER_TEX));
            ResourceManager.fluid_tank_exploded.renderPart("TankInner");
            RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(NuclearTechMod.withDefaultNamespace(getTextureFromType(type))));
            ResourceManager.fluid_tank_exploded.renderPart("Tank");
        }

        if (type != Fluids.NONE) {
            RenderContext.pushPose();
            RenderContext.translate(-0.25F, 0.5F, -1.501F);
            RenderContext.mulPose(Axis.YP.rotationDegrees(90));
            RenderContext.scale(1.0F, 0.375F, 0.375F);
            DiamondPronter.pront(buffer, type.poison, type.flammability, type.reactivity, type.symbol);
            RenderContext.popPose();

            RenderContext.pushPose();
            RenderContext.translate(0.25F, 0.5F, 1.501F);
            RenderContext.mulPose(Axis.YN.rotationDegrees(90));
            RenderContext.scale(1.0F, 0.375F, 0.375F);
            DiamondPronter.pront(buffer, type.poison, type.flammability, type.reactivity, type.symbol);
            RenderContext.popPose();
        }

        RenderContext.end();
    }

    public static String getTextureFromType(FluidType type) {
        String s = type.getInternalName().toLowerCase(Locale.US);

        if (type.isAntimatter() || (type.hasTrait(FT_Corrosive.class) && type.getTrait(FT_Corrosive.class).isHighlyCorrosive())) {
            s = "danger";
        }

        return "textures/models/tank/tank_" + s + ".png";
    }

    private AABB bb = null;

    @Override
    public AABB getRenderBoundingBox(MachineFluidTankBlockEntity be) {

        if (bb == null) {
            int x = be.getBlockPos().getX();
            int y = be.getBlockPos().getY();
            int z = be.getBlockPos().getZ();

            bb = new AABB(
                    x - 2,
                    y - 0,
                    z - 2,
                    x + 3,
                    y + 3,
                    z + 3
            );
        }

        return bb;
    }


    @Override
    public Item getItemForRenderer() {
        return ModBlocks.MACHINE_FLUID_TANK.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0, -2, 0);
                RenderContext.scale(3.5F, 3.5F, 3.5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.scale(0.75F, 0.75F, 0.75F);

                FluidTank tank = new FluidTank(Fluids.NONE, 0);
                boolean exploded = false;
                if (TagsUtilDegradation.getTag(stack).contains(IPersistentNBT.NBT_PERSISTENT_KEY)) {
                    CompoundTag persistentTag = TagsUtilDegradation.getTag(stack).getCompound(IPersistentNBT.NBT_PERSISTENT_KEY);
                    tank.readFromNBT(persistentTag, "Tank");
                    exploded = persistentTag.getBoolean("HasExploded");
                }

                FluidType type = tank.getTankType();

                if (!exploded) {
                    RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(ResourceManager.TANK_TEX));
                    ResourceManager.fluid_tank.renderPart("Frame");
                    RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(NuclearTechMod.withDefaultNamespace(getTextureFromType(type))));
                    ResourceManager.fluid_tank.renderPart("Tank");
                } else {
                    RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(ResourceManager.TANK_TEX));
                    ResourceManager.fluid_tank_exploded.renderPart("Frame");
                    RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(ResourceManager.TANK_INNER_TEX));
                    ResourceManager.fluid_tank_exploded.renderPart("TankInner");
                    RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(NuclearTechMod.withDefaultNamespace(getTextureFromType(type))));
                    ResourceManager.fluid_tank_exploded.renderPart("Tank");
                }
            }
        };
    }
}
