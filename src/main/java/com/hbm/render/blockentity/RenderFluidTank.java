package com.hbm.render.blockentity;

import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.machine.storage.MachineFluidTankBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Corrosive;
import com.hbm.main.NuclearTechMod;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.DiamondPronter;
import com.hbm.render.util.RenderContext;
import com.hbm.util.TagsUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RenderFluidTank extends BlockEntityRendererNT<MachineFluidTankBlockEntity> implements IBEWLRProvider {

    public static final Map<String, ResourceLocation> textureByName = new HashMap<>();

    public static ResourceLocation getTextureFromType(FluidType type) {

        if(type.renderWithTint) {
            int color = type.getTint();
            float r = ((color & 0xff0000) >> 16) / 255F;
            float g = ((color & 0x00ff00) >> 8) / 255F;
            float b = ((color & 0x0000ff) >> 0) / 255F;
            RenderContext.setColor(r, g, b, 1F);
            return textureByName.computeIfAbsent("textures/models/tank/tank_none.png", NuclearTechMod::withDefaultNamespace);
        }

        String s = type.getInternalName().toLowerCase(Locale.US);
        if(type.isAntimatter() || (type.hasTrait(FT_Corrosive.class) && type.getTrait(FT_Corrosive.class).isHighlyCorrosive())) s = "danger";

        return textureByName.computeIfAbsent("textures/models/tank/tank_" + s + ".png", NuclearTechMod::withDefaultNamespace);
    }

    @Override public BlockEntityRenderer<MachineFluidTankBlockEntity> create(Context context) { return new RenderFluidTank(); }

    @Override
    public void render(MachineFluidTankBlockEntity be, MultiBufferSource buffer, float partialTicks) {

        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderSystem.disableCull();

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case WEST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            case EAST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
        }

        FluidType type = be.tank.getTankType();

        bindTexture(ResourceManager.TANK_TEX);
        if(!be.hasExploded) {
            ResourceManager.fluid_tank.renderPart("Frame");
            bindTexture(getTextureFromType(type));
            ResourceManager.fluid_tank.renderPart("Tank");
        } else {
            ResourceManager.fluid_tank_exploded.renderPart("Frame");
            bindTexture(ResourceManager.TANK_INNER_TEX);
            ResourceManager.fluid_tank_exploded.renderPart("TankInner");
            bindTexture(getTextureFromType(type));
            ResourceManager.fluid_tank_exploded.renderPart("Tank");
        }

        if(type != Fluids.NONE) {
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

        RenderSystem.enableCull();
    }

    @Override
    public int getPacketLight(int packedLight, MachineFluidTankBlockEntity be) {
        if(be.getLevel() != null && be.getBlockState().getBlock() instanceof DummyableBlock dummy) {
            return LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(dummy.getDimensions()[0]));
        }
        return packedLight;
    }

    private AABB bb = null;

    @Override
    public AABB getRenderBoundingBox(MachineFluidTankBlockEntity be) {

        if(bb == null) {
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
        return NtmBlocks.MACHINE_FLUID_TANK.asItem();
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
                if(TagsUtil.getCustomData(stack).contains(IPersistentNBT.NBT_PERSISTENT_KEY)) {
                    CompoundTag persistentTag = TagsUtil.getCustomData(stack).getCompound(IPersistentNBT.NBT_PERSISTENT_KEY);
                    tank.readFromNBT(persistentTag, "Tank");
                    exploded = persistentTag.getBoolean("HasExploded");
                }

                FluidType type = tank.getTankType();

                bindTexture(ResourceManager.TANK_TEX);
                if(!exploded) {
                    ResourceManager.fluid_tank.renderPart("Frame");
                    bindTexture(getTextureFromType(type));
                    ResourceManager.fluid_tank.renderPart("Tank");
                } else {
                    ResourceManager.fluid_tank_exploded.renderPart("Frame");
                    bindTexture(ResourceManager.TANK_INNER_TEX);
                    ResourceManager.fluid_tank_exploded.renderPart("TankInner");
                    bindTexture(getTextureFromType(type));
                    ResourceManager.fluid_tank_exploded.renderPart("Tank");
                }
            }
        };
    }
}
