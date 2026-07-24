package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.recipes.ArcWelderRecipes.ArcWelderRecipe;
import com.hbm.items.machine.FluidIconItem;
import com.hbm.main.NuclearTechMod;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArcWelderRecipeHandler implements IRecipeCategory<ArcWelderRecipe> {

    public static final RecipeType<ArcWelderRecipe> RECIPE_TYPE = RecipeType.create(NuclearTechMod.MODID, "arc_welder", ArcWelderRecipe.class);
    private static final ItemStack MACHINE_STACK = new ItemStack(NtmBlocks.MACHINE_ARC_WELDER.asItem());

    private final IDrawable background;
    private final IDrawable icon;

    public ArcWelderRecipeHandler(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(NuclearTechMod.withDefaultNamespace("textures/gui/jei/gui_nei.png"), 5, 11, 166, 65).setTextureSize(256, 256).build();
        this.icon = guiHelper.createDrawableItemLike(NtmBlocks.MACHINE_ARC_WELDER.asItem());
    }

    @Override public RecipeType<ArcWelderRecipe> getRecipeType() { return RECIPE_TYPE; }
    @Override public Component getTitle() { return Component.translatable("container.machine_arc_welder"); }
    @Override public IDrawable getBackground() { return this.background; }
    @Override public IDrawable getIcon() { return this.icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ArcWelderRecipe recipe, IFocusGroup focuses) {
        List<List<ItemStack>> inputLists = new ArrayList<>();
        for(var ingredient : recipe.ingredients) inputLists.add(ingredient.extractForJEI());
        if(recipe.fluid != null) inputLists.add(List.of(FluidIconItem.make(recipe.fluid)));

        int[][] coords = recipe.fluid == null
                ? new int[][] { { 12, 24 }, { 30, 24 }, { 48, 24 } }
                : new int[][] { { 12, 15 }, { 30, 15 }, { 48, 15 }, { 30, 33 } };
        for(int i = 0; i < inputLists.size(); i++) {
            var slot = builder.addInputSlot(coords[i][0], coords[i][1]).setStandardSlotBackground();
            if(!inputLists.get(i).isEmpty()) slot.addItemStacks(inputLists.get(i));
        }

        builder.addInputSlot(74, 31).addItemStack(MACHINE_STACK.copy()).setStandardSlotBackground();
        builder.addOutputSlot(102, 24).addItemStack(recipe.output).setStandardSlotBackground();
    }

    @Override
    public void draw(ArcWelderRecipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;
        String duration = String.format(Locale.US, "%,d", recipe.duration) + " ticks";
        String consumption = String.format(Locale.US, "%,d", recipe.consumption) + " HE/t";
        guiGraphics.drawString(font, duration, 160 - font.width(duration), 43, 0x404040, false);
        guiGraphics.drawString(font, consumption, 160 - font.width(consumption), 55, 0x404040, false);
    }
}
