package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.machine.FluidIconItem;
import com.hbm.main.NuclearTechMod;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ChemicalPlantRecipeHandler implements IRecipeCategory<GenericRecipe> {

    public static final RecipeType<GenericRecipe> RECIPE_TYPE = RecipeType.create(
            NuclearTechMod.MODID,
            "chemical_plant",
            GenericRecipe.class
    );

    private final IDrawable background;
    private final IDrawable icon;

    public ChemicalPlantRecipeHandler(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(
                NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_chemplant.png"),
                0, 0, 176, 141
        ).setTextureSize(256, 256).build();
        this.icon = guiHelper.createDrawableItemLike(NtmBlocks.MACHINE_CHEMICAL_PLANT.asItem());
    }

    @Override public RecipeType<GenericRecipe> getRecipeType() { return RECIPE_TYPE; }
    @Override public Component getTitle() { return Component.translatable("container.machineChemicalPlant"); }
    @Override public IDrawable getBackground() { return this.background; }
    @Override public IDrawable getIcon() { return this.icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GenericRecipe recipe, IFocusGroup focuses) {
        if(recipe.inputItem != null) {
            for(int i = 0; i < Math.min(recipe.inputItem.length, 3); i++) {
                builder.addInputSlot(8 + i * 18, 99).setStandardSlotBackground().addItemStacks(recipe.inputItem[i].extractForJEI());
            }
        }
        if(recipe.outputItem != null) {
            for(int i = 0; i < Math.min(recipe.outputItem.length, 3); i++) {
                builder.addOutputSlot(80 + i * 18, 99).setOutputSlotBackground().addItemStacks(Arrays.asList(recipe.outputItem[i].getAllPossibilities()));
            }
        }
        if(recipe.inputFluid != null) {
            for(int i = 0; i < Math.min(recipe.inputFluid.length, 3); i++) {
                addFluidSlot(builder, 8 + i * 18, 54, recipe.inputFluid[i], true);
            }
        }
        if(recipe.outputFluid != null) {
            for(int i = 0; i < Math.min(recipe.outputFluid.length, 3); i++) {
                addFluidSlot(builder, 80 + i * 18, 54, recipe.outputFluid[i], false);
            }
        }
    }

    @Override
    public void draw(GenericRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
    }

    private static void addFluidSlot(IRecipeLayoutBuilder builder, int x, int y, FluidStack stack, boolean input) {
        var slot = input ? builder.addInputSlot(x, y) : builder.addOutputSlot(x, y);
        slot.setStandardSlotBackground().addItemStack(FluidIconItem.make(stack));
    }
}
