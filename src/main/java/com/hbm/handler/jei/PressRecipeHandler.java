package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.recipes.PressRecipes;
import com.hbm.items.machine.StampItem;
import com.hbm.items.machine.StampItem.StampType;
import com.hbm.main.NuclearTechMod;
import com.hbm.util.Tuple.Pair;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PressRecipeHandler implements IRecipeCategory<PressRecipeHandler.PressRecipe> {

    public static final RecipeType<PressRecipe> RECIPE_TYPE = RecipeType.create(
            NuclearTechMod.MODID,
            "press",
            PressRecipe.class
    );

    private static final String TEXTURE = "textures/gui/jei/gui_nei_press.png";

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable progressAnimated;

    public PressRecipeHandler(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(
                NuclearTechMod.withDefaultNamespace(TEXTURE),
                5, 11, 166, 65
        ).setTextureSize(256, 256).build();
        this.icon = guiHelper.createDrawableItemLike(NtmBlocks.MACHINE_PRESS.asItem());

        IDrawableStatic progress = guiHelper.drawableBuilder(
                NuclearTechMod.withDefaultNamespace(TEXTURE),
                0, 86, 18, 18
        ).setTextureSize(256, 256).build();
        this.progressAnimated = guiHelper.createAnimatedDrawable(progress, 20, IDrawableAnimated.StartDirection.TOP, false);
    }

    public static List<PressRecipe> getRecipes() {
        List<PressRecipe> list = new ArrayList<>();

        for (Map.Entry<Pair<AStack, StampType>, ItemStack> entry : PressRecipes.recipes.entrySet()) {
            list.add(new PressRecipe(entry.getKey().getKey(), entry.getKey().getValue(), entry.getValue()));
        }

        return list;
    }

    @Override
    public RecipeType<PressRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("desc.machine_press");
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
    public void setRecipe(IRecipeLayoutBuilder builder, PressRecipe recipe, IFocusGroup focuses) {
        var ingredient = builder.addInputSlot(47, 41).setStandardSlotBackground();
        ingredient.addItemStacks(recipe.input.extractForJEI());

        var stamp = builder.addInputSlot(47, 5).setStandardSlotBackground();
        List<ItemStack> stampStacks = StampItem.stamps.get(recipe.stamp);
        if (stampStacks != null && !stampStacks.isEmpty()) {
            stamp.addItemStacks(stampStacks);
        }

        for (int i = 0; i < recipe.outputs.size(); i++) {
            builder.addOutputSlot(110 + (i % 3) * 18, 23 + (i / 3) * 18)
                    .setStandardSlotBackground()
                    .addItemStack(recipe.outputs.get(i).copy());
        }
    }

    @Override
    public void draw(PressRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.progressAnimated.draw(guiGraphics, 47, 24);
    }

    public record PressRecipe(AStack input, StampType stamp, List<ItemStack> outputs) {
        public PressRecipe(AStack input, StampType stamp, ItemStack output) {
            this(input, stamp, List.of(output.copy()));
        }
    }
}
