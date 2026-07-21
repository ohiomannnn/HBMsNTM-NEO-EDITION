package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.recipes.BlastFurnaceRecipe;
import com.hbm.main.NuclearTechMod;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BlastFurnaceRecipeHandler implements IRecipeCategory<BlastFurnaceRecipe> {

    public static final RecipeType<BlastFurnaceRecipe> RECIPE_TYPE = RecipeType.create(
            NuclearTechMod.MODID,
            "blast_furnace",
            BlastFurnaceRecipe.class
    );

    private final IDrawable background;
    private final IDrawable icon;

    public BlastFurnaceRecipeHandler(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(
                NuclearTechMod.withDefaultNamespace("textures/gui/jei/gui_nei.png"),
                5, 11, 166, 65
        ).setTextureSize(256, 256).build();
        this.icon = guiHelper.createDrawableItemLike(NtmBlocks.MACHINE_BLAST_FURNACE.asItem());
    }

    @Override public RecipeType<BlastFurnaceRecipe> getRecipeType() { return RECIPE_TYPE; }
    @Override public Component getTitle() { return Component.translatable("container.machine_blast_furnace"); }
    @Override public IDrawable getBackground() { return this.background; }
    @Override public IDrawable getIcon() { return this.icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BlastFurnaceRecipe recipe, IFocusGroup focuses) {
        if(recipe.inputItem != null && recipe.inputItem.length > 0) {
            addInputSlot(builder, 44, 15, recipe.inputItem[0].extractForJEI());
        }
        if(recipe.inputItem != null && recipe.inputItem.length > 1) {
            addInputSlot(builder, 44, 33, recipe.inputItem[1].extractForJEI());
        }
        if(recipe.outputItem != null && recipe.outputItem.length > 0) {
            builder.addOutputSlot(100, 15).setStandardSlotBackground().addItemStacks(Arrays.asList(recipe.outputItem[0].getAllPossibilities()));
        }
        if(recipe.outputItem != null && recipe.outputItem.length > 1) {
            builder.addOutputSlot(100, 33).setStandardSlotBackground().addItemStacks(Arrays.asList(recipe.outputItem[1].getAllPossibilities()));
        }
    }

    @Override
    public void draw(BlastFurnaceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if(recipe.duration > 0) {
            String text = (recipe.duration / 20.0D) + " s";
            guiGraphics.drawString(Minecraft.getInstance().font, text, 130 - Minecraft.getInstance().font.width(text) / 2, 51, 0x808080, false);
        }
    }

    private static void addInputSlot(IRecipeLayoutBuilder builder, int x, int y, List<ItemStack> stacks) {
        var slot = builder.addInputSlot(x, y).setStandardSlotBackground();
        if(!stacks.isEmpty()) {
            slot.addItemStacks(stacks);
        }
    }
}
