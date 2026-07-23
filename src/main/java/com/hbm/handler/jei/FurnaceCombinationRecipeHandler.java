package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.recipes.CombinationRecipes;
import com.hbm.items.machine.FluidIconItem;
import com.hbm.main.NuclearTechMod;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FurnaceCombinationRecipeHandler implements IRecipeCategory<CombinationRecipes.JeiRecipe> {

    public static final RecipeType<CombinationRecipes.JeiRecipe> RECIPE_TYPE = RecipeType.create(NuclearTechMod.MODID, "furnace_combination", CombinationRecipes.JeiRecipe.class);
    private static final String TEXTURE = "textures/gui/jei/gui_nei.png";
    private static final ItemStack MACHINE_STACK = new ItemStack(NtmBlocks.FURNACE_COMBINATION.asItem());
    private static final int ARROW_X = 74;
    private static final int ARROW_Y = 19;
    private static final int ARROW_U = 17;
    private static final int ARROW_V = 84;
    private static final int ARROW_WIDTH = 17;
    private static final int ARROW_HEIGHT = 10;

    private final IDrawable background;
    private final IDrawable icon;

    public FurnaceCombinationRecipeHandler(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(NuclearTechMod.withDefaultNamespace(TEXTURE), 5, 11, 166, 65).setTextureSize(256, 256).build();
        this.icon = guiHelper.createDrawableItemLike(NtmBlocks.FURNACE_COMBINATION.asItem());
    }

    @Override public RecipeType<CombinationRecipes.JeiRecipe> getRecipeType() { return RECIPE_TYPE; }
    @Override public Component getTitle() { return Component.translatable("container.furnace_combination"); }
    @Override public IDrawable getBackground() { return this.background; }
    @Override public IDrawable getIcon() { return this.icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CombinationRecipes.JeiRecipe recipe, IFocusGroup focuses) {
        var input = builder.addInputSlot(48, 24).setStandardSlotBackground();
        for(ItemStack stack : recipe.inputStacks()) {
            input.addItemStack(stack);
        }

        builder.addSlot(RecipeIngredientRole.CATALYST, 74, 31).setStandardSlotBackground().addItemStack(MACHINE_STACK.copy());

        int outputIndex = 0;
        if(!recipe.outputItem().isEmpty()) {
            builder.addOutputSlot(102, 24).setStandardSlotBackground().addItemStack(recipe.outputItem().copy());
            outputIndex++;
        }
        if(recipe.outputFluid() != null) {
            int x = outputIndex == 0 ? 102 : 120;
            builder.addOutputSlot(x, 24).setStandardSlotBackground().addItemStack(FluidIconItem.make(recipe.outputFluid()));
        }
    }

    @Override
    public void draw(CombinationRecipes.JeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(NuclearTechMod.withDefaultNamespace(TEXTURE), ARROW_X, ARROW_Y, ARROW_U, ARROW_V, ARROW_WIDTH, ARROW_HEIGHT, 256, 256);
    }
}
