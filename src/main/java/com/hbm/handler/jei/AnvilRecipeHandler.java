package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.recipes.anvil.AnvilSmithingRecipe;
import com.hbm.main.NuclearTechMod;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.network.chat.Component;

public class AnvilRecipeHandler implements IRecipeCategory<AnvilSmithingRecipe> {

    public static final RecipeType<AnvilSmithingRecipe> RECIPE_TYPE = RecipeType.create(NuclearTechMod.MODID, "anvil_smithing", AnvilSmithingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public AnvilRecipeHandler(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(
                NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_anvil.png"),
                5, 11, 166, 65
        ).setTextureSize(176, 222).build();
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
                .setOutputSlotBackground()
                .addRichTooltipCallback((slotView, tooltip) -> tooltip.add(Component.literal("Tier " + recipe.getTier())));

        builder.createFocusLink(left, right);
    }
}
