package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineChemicalPlantBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.recipes.ChemicalPlantRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.hbm.util.BobMathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.awt.Color;

public class RenderChemicalPlant extends BlockEntityRendererNT<MachineChemicalPlantBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<MachineChemicalPlantBlockEntity> create(Context context) {
        return new RenderChemicalPlant();
    }

    @Override
    public void render(MachineChemicalPlantBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(90F));

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            case WEST -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
        }

        float anim = BobMathUtil.interp(be.prevAnim, be.anim, partialTicks);
        GenericRecipe recipe = ChemicalPlantRecipes.INSTANCE.recipeNameMap.get(be.chemicalModule.recipe);

        bindTexture(ResourceManager.CHEMICAL_PLANT_TEX);
        ResourceManager.chemical_plant.renderPart("Base");
        if(be.frame) ResourceManager.chemical_plant.renderPart("Frame");

        RenderContext.pushPose();
        RenderContext.translate((float) (BobMathUtil.sps(anim * 0.125D) * 0.375D), 0F, 0F);
        ResourceManager.chemical_plant.renderPart("Slider");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees((anim * 15F) % 360F));
        RenderContext.translate(-0.5F, 0F, -0.5F);
        ResourceManager.chemical_plant.renderPart("Spinner");
        RenderContext.popPose();

        if(be.didProcess && recipe != null) {
            int colors = 0;
            int r = 0;
            int g = 0;
            int b = 0;

            if(recipe.outputFluid != null) {
                for(FluidStack stack : recipe.outputFluid) {
                    Color color = new Color(stack.type.getTint());
                    r += color.getRed();
                    g += color.getGreen();
                    b += color.getBlue();
                    colors++;
                }
            }

            if(colors == 0 && recipe.inputFluid != null) {
                for(FluidStack stack : recipe.inputFluid) {
                    Color color = new Color(stack.type.getTint());
                    r += color.getRed();
                    g += color.getGreen();
                    b += color.getBlue();
                    colors++;
                }
            }

            if(colors > 0) {
                bindTexture(ResourceManager.CHEMICAL_PLANT_FLUID_TEX);
                RenderContext.pushPose();
                RenderSystem.disableCull();
                RenderSystem.enableBlend();
                RenderSystem.depthMask(false);
                RenderContext.setColor((float) r / 255F / colors, (float) g / 255F / colors, (float) b / 255F / colors, 0.5F);
                RenderContext.translate(0F, (float) (BobMathUtil.sps(anim * 0.1D) * 0.05D), 0F);
                ResourceManager.chemical_plant.renderPart("Fluid");
                RenderContext.setColor(1F, 1F, 1F, 1F);
                RenderSystem.depthMask(true);
                RenderSystem.disableBlend();
                RenderSystem.enableCull();
                RenderContext.popPose();
            }
        }
    }

    @Override
    public int getPacketLight(int packedLight, MachineChemicalPlantBlockEntity be) {
        if(be.getLevel() != null && be.getBlockState().getBlock() instanceof DummyableBlock dummy) {
            return LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(dummy.getDimensions()[0]));
        }
        return packedLight;
    }

    @Override
    public AABB getRenderBoundingBox(MachineChemicalPlantBlockEntity be) {
        return be.getRenderBoundingBox();
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_CHEMICAL_PLANT.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -2.75F, 0F);
                RenderContext.scale(4.5F, 4.5F, 4.5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.scale(0.75F, 0.75F, 0.75F);
                bindTexture(ResourceManager.CHEMICAL_PLANT_TEX);
                ResourceManager.chemical_plant.renderAll();
            }
        };
    }
}
