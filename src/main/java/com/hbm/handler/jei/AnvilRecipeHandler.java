package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.recipes.anvil.AnvilSmithingRecipe;
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

public class AnvilRecipeHandler implements IRecipeCategory<AnvilSmithingRecipe> {

    public static final RecipeType<AnvilSmithingRecipe> RECIPE_TYPE = RecipeType.create(NuclearTechMod.MODID, "anvil_smithing", AnvilSmithingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public AnvilRecipeHandler(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(
                NuclearTechMod.withDefaultNamespace("textures/gui/jei/gui_nei_smithing.png"),
                5, 11, 166, 65
        ).setTextureSize(256, 256).build();
        this.icon = guiHelper.createDrawableItemLike(NtmBlocks.ANVIL.asItem());
    }

    @Override
    public RecipeType<AnvilSmithingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.anvil");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AnvilSmithingRecipe recipe, IFocusGroup focuses) {
        var left = builder.addInputSlot(38, 23)
                .addItemStacks(recipe.getLeft())
                .setStandardSlotBackground();

        var right = builder.addInputSlot(74, 23)
                .addItemStacks(recipe.getRight())
                .setStandardSlotBackground();

        if(recipe.isShapeless()) {
            builder.setShapeless(114, 6);
        }

        builder.addOutputSlot(110, 23)
                .addItemStack(recipe.getSimpleOutput())
                .setOutputSlotBackground();

        builder.createFocusLink(left, right);
    }

    @Override
    public void draw(AnvilSmithingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.drawString(Minecraft.getInstance().font, "Tier " + recipe.getTier(), 52, 43, 0x404040, false);
    }
}
