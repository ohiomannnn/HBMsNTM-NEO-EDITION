package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.recipes.ShredderRecipes;
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

public class ShredderRecipeHandler implements IRecipeCategory<ShredderRecipes.ShredderRecipe> {

    public static final RecipeType<ShredderRecipes.ShredderRecipe> RECIPE_TYPE = RecipeType.create(
            NuclearTechMod.MODID,
            "shredder",
            ShredderRecipes.ShredderRecipe.class
    );

    private static final String TEXTURE = "textures/gui/jei/gui_nei_shredder.png";

    private final IDrawable background;
    private final IDrawable icon;

    public ShredderRecipeHandler(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(
                NuclearTechMod.withDefaultNamespace(TEXTURE),
                5, 11, 166, 65
        ).setTextureSize(256, 256).build();
        this.icon = guiHelper.createDrawableItemLike(NtmBlocks.MACHINE_SHREDDER.asItem());
    }

    @Override
    public RecipeType<ShredderRecipes.ShredderRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.machine_shredder");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ShredderRecipes.ShredderRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(38, 23)
                .setStandardSlotBackground()
                .addItemStack(recipe.input.copy());

        builder.addOutputSlot(128, 23)
                .setStandardSlotBackground()
                .addItemStack(recipe.output.copy());

        var topBlade = builder.addInputSlot(83, 5).setStandardSlotBackground();
        var bottomBlade = builder.addInputSlot(83, 41).setStandardSlotBackground();

        for(ItemStack blade : ShredderRecipes.getBlades()) {
            topBlade.addItemStack(blade.copy());
            bottomBlade.addItemStack(blade.copy());
        }
    }

    @Override
    public void draw(ShredderRecipes.ShredderRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(NuclearTechMod.withDefaultNamespace(TEXTURE), 3, 6, 36, 86, 16, 52, 256, 256);
        guiGraphics.blit(NuclearTechMod.withDefaultNamespace(TEXTURE), 80, 23, 100, 118, 24, 16, 256, 256);
    }
}
