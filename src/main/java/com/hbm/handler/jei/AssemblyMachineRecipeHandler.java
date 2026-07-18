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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AssemblyMachineRecipeHandler implements IRecipeCategory<GenericRecipe> {

    public static final RecipeType<GenericRecipe> RECIPE_TYPE = RecipeType.create(
            NuclearTechMod.MODID,
            "assembly_machine",
            GenericRecipe.class
    );

    private static final String TEXTURE = "textures/gui/jei/gui_nei.png";

    private static final int[][] INPUT_COORDS = new int[][] {
            {12, 6}, {30, 6}, {48, 6}, {66, 6},
            {12, 24}, {30, 24}, {48, 24}, {66, 24},
            {12, 42}, {30, 42}, {48, 42}, {66, 42}
    };

    private final IDrawable background;
    private final IDrawable icon;

    public AssemblyMachineRecipeHandler(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(
                NuclearTechMod.withDefaultNamespace(TEXTURE),
                5, 11, 166, 65
        ).setTextureSize(256, 256).build();
        this.icon = guiHelper.createDrawableItemLike(NtmBlocks.MACHINE_ASSEMBLY_MACHINE.asItem());
    }

    @Override
    public RecipeType<GenericRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.machine_assembly_machine");
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
    public void setRecipe(IRecipeLayoutBuilder builder, GenericRecipe recipe, IFocusGroup focuses) {
        if(recipe.inputItem != null) {
            int limit = Math.min(recipe.inputItem.length, INPUT_COORDS.length);
            for(int i = 0; i < limit; i++) {
                addInputSlot(builder, INPUT_COORDS[i][0], INPUT_COORDS[i][1], recipe.inputItem[i].extractForJEI());
            }
        }

        if(recipe.inputFluid != null && recipe.inputFluid.length > 0) {
            addFluidSlot(builder, 134, 6, recipe.inputFluid[0], true);
        }

        if(recipe.outputItem != null && recipe.outputItem.length > 0) {
            builder.addOutputSlot(134, 24)
                    .setStandardSlotBackground()
                    .addItemStacks(Arrays.asList(recipe.outputItem[0].getAllPossibilities()));
        }

        if(recipe.outputFluid != null && recipe.outputFluid.length > 0) {
            addFluidSlot(builder, 134, 42, recipe.outputFluid[0], false);
        }
    }

    @Override
    public void draw(GenericRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
    }

    private static void addInputSlot(IRecipeLayoutBuilder builder, int x, int y, List<ItemStack> stacks) {
        var slot = builder.addInputSlot(x, y).setStandardSlotBackground();
        if(!stacks.isEmpty()) {
            slot.addItemStacks(stacks);
        }
    }

    private static void addFluidSlot(IRecipeLayoutBuilder builder, int x, int y, FluidStack stack, boolean input) {
        var slot = input ? builder.addInputSlot(x, y) : builder.addOutputSlot(x, y);
        slot.setStandardSlotBackground().addItemStack(FluidIconItem.make(stack));
    }
}
