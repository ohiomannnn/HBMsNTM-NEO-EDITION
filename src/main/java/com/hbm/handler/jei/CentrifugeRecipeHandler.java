package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.recipes.CentrifugeRecipes;
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

public class CentrifugeRecipeHandler implements IRecipeCategory<CentrifugeRecipes.CentrifugeRecipe> {

    public static final RecipeType<CentrifugeRecipes.CentrifugeRecipe> RECIPE_TYPE = RecipeType.create(
            NuclearTechMod.MODID,
            "centrifuge",
            CentrifugeRecipes.CentrifugeRecipe.class
    );

    private static final String TEXTURE = "textures/gui/gui_centrifuge.png";

    private final IDrawable background;
    private final IDrawable icon;

    public CentrifugeRecipeHandler(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(
                NuclearTechMod.withDefaultNamespace(TEXTURE),
                0, 0, 176, 86
        ).setTextureSize(256, 256).build();
        this.icon = guiHelper.createDrawableItemLike(NtmBlocks.MACHINE_CENTRIFUGE.asItem());
    }

    @Override
    public RecipeType<CentrifugeRecipes.CentrifugeRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.machine_centrifuge");
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
    public void setRecipe(IRecipeLayoutBuilder builder, CentrifugeRecipes.CentrifugeRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(36, 50)
                .setStandardSlotBackground()
                .addItemStack(recipe.input.copy());

        for(int i = 0; i < recipe.outputs.length && i < 4; i++) {
            ItemStack output = recipe.outputs[i];
            if(output.isEmpty()) continue;

            builder.addOutputSlot(63 + i * 20, 50)
                    .setStandardSlotBackground()
                    .addItemStack(output.copy());
        }
    }

    @Override
    public void draw(CentrifugeRecipes.CentrifugeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(NuclearTechMod.withDefaultNamespace(TEXTURE), 65, 14, 176, 35, 72, 36, 256, 256);
    }
}
