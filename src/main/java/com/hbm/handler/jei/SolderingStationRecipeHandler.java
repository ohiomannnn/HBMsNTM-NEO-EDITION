package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.recipes.SolderingRecipes.SolderingRecipe;
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

public class SolderingStationRecipeHandler implements IRecipeCategory<SolderingRecipe> {

    public static final RecipeType<SolderingRecipe> RECIPE_TYPE = RecipeType.create(
            NuclearTechMod.MODID,
            "soldering_station",
            SolderingRecipe.class
    );

    private static final ItemStack MACHINE_STACK = new ItemStack(NtmBlocks.MACHINE_SOLDERING_STATION.asItem());

    private final IDrawable background;
    private final IDrawable icon;

    public SolderingStationRecipeHandler(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(
                NuclearTechMod.withDefaultNamespace("textures/gui/jei/gui_nei.png"),
                5, 11, 166, 65
        ).setTextureSize(256, 256).build();
        this.icon = guiHelper.createDrawableItemLike(NtmBlocks.MACHINE_SOLDERING_STATION.asItem());
    }

    @Override
    public RecipeType<SolderingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.machine_soldering_station");
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
    public void setRecipe(IRecipeLayoutBuilder builder, SolderingRecipe recipe, IFocusGroup focuses) {
        List<List<ItemStack>> inputLists = new ArrayList<>(recipe.fluid == null ? 6 : 7);
        inputLists.add(recipe.toppings.length > 0 ? recipe.toppings[0].extractForJEI() : List.of());
        inputLists.add(recipe.toppings.length > 1 ? recipe.toppings[1].extractForJEI() : List.of());
        inputLists.add(recipe.toppings.length > 2 ? recipe.toppings[2].extractForJEI() : List.of());
        inputLists.add(recipe.pcb.length > 0 ? recipe.pcb[0].extractForJEI() : List.of());
        inputLists.add(recipe.pcb.length > 1 ? recipe.pcb[1].extractForJEI() : List.of());
        inputLists.add(recipe.solder.length > 0 ? recipe.solder[0].extractForJEI() : List.of());
        if (recipe.fluid != null) {
            inputLists.add(List.of(FluidIconItem.make(recipe.fluid)));
        }

        builder.moveRecipeTransferButton(150, 26);

        int[][] inputCoords = getInputCoords(inputLists.size());
        for (int i = 0; i < inputLists.size(); i++) {
            addIngredientSlot(builder, inputCoords[i][0], inputCoords[i][1], inputLists.get(i));
        }

        builder.addInputSlot(74, 31)
                .addItemStack(MACHINE_STACK.copy())
                .setStandardSlotBackground();

        builder.addOutputSlot(102, 24)
                .addItemStack(recipe.output)
                .setOutputSlotBackground();
    }

    @Override
    public void draw(SolderingRecipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;

        String duration = String.format(Locale.US, "%,d", recipe.duration) + " ticks";
        String consumption = String.format(Locale.US, "%,d", recipe.consumption) + " HE/t";

        int side = 160;
        guiGraphics.drawString(font, duration, side - font.width(duration), 43, 0x404040, false);
        guiGraphics.drawString(font, consumption, side - font.width(consumption), 55, 0x404040, false);
    }

    private static void addIngredientSlot(IRecipeLayoutBuilder builder, int x, int y, List<ItemStack> stacks) {
        var slot = builder.addInputSlot(x, y).setStandardSlotBackground();
        if (!stacks.isEmpty()) {
            slot.addItemStacks(stacks);
        }
    }

    private static int[][] getInputCoords(int count) {
        return switch (count) {
            case 1 -> new int[][]{{48, 24}};
            case 2 -> new int[][]{{48, 24}, {30, 24}};
            case 3 -> new int[][]{{48, 24}, {30, 24}, {12, 24}};
            case 4 -> new int[][]{{48, 15}, {30, 15}, {48, 33}, {30, 33}};
            case 5 -> new int[][]{{48, 15}, {30, 15}, {12, 24}, {48, 33}, {30, 33}};
            case 6 -> new int[][]{{48, 15}, {30, 15}, {12, 15}, {48, 33}, {30, 33}, {12, 33}};
            case 7 -> new int[][]{{48, 6}, {30, 15}, {12, 15}, {48, 24}, {30, 33}, {12, 33}, {48, 42}};
            case 8 -> new int[][]{{48, 6}, {30, 6}, {12, 15}, {48, 24}, {30, 24}, {12, 33}, {48, 42}, {30, 42}};
            case 9 -> new int[][]{{48, 6}, {30, 6}, {12, 6}, {48, 24}, {30, 24}, {12, 24}, {48, 42}, {30, 42}, {12, 42}};
            default -> new int[count][2];
        };
    }
}
