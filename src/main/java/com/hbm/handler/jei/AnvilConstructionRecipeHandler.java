package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.machine.NTMAnvilBlock;
import com.hbm.inventory.recipes.anvil.AnvilRecipes.AnvilConstructionRecipe;
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

import java.util.List;
import java.util.Locale;

public class AnvilConstructionRecipeHandler implements IRecipeCategory<AnvilConstructionRecipe> {

    public static final RecipeType<AnvilConstructionRecipe> RECIPE_TYPE = RecipeType.create(
            NuclearTechMod.MODID,
            "anvil_construction",
            AnvilConstructionRecipe.class
    );

    private static final String TEXTURE = "textures/gui/jei/gui_nei_anvil.png";

    private final IDrawable background;
    private final IDrawable icon;

    public AnvilConstructionRecipeHandler(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(
                NuclearTechMod.withDefaultNamespace(TEXTURE),
                5, 11, 166, 65
        ).setTextureSize(256, 256).build();
        this.icon = guiHelper.createDrawableItemLike(NtmBlocks.ANVIL.asItem());
    }

    @Override
    public RecipeType<AnvilConstructionRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, AnvilConstructionRecipe recipe, IFocusGroup focuses) {
        List<List<ItemStack>> inputs = recipe.input.stream()
                .map(stack -> stack.extractForJEI())
                .toList();
        Layout layout = Layout.of(recipe.getOverlay());

        for(int i = 0; i < inputs.size(); i++) {
            int x = layout.inputX + 18 * (i % layout.inputLine);
            int y = layout.inputY + 18 * (i / layout.inputLine);
            addInputSlot(builder, x, y, inputs.get(i));
        }

        for(int i = 0; i < recipe.output.size(); i++) {
            int x = layout.outputX + 18 * (i % layout.outputLine);
            int y = layout.outputY + 18 * (i / layout.outputLine);
            var slot = builder.addOutputSlot(x, y).setStandardSlotBackground();
            slot.addItemStack(recipe.output.get(i).stack.copy());
            int outputIndex = i;
            slot.addRichTooltipCallback((slotView, tooltip) -> {
                float chance = recipe.output.get(outputIndex).chance;
                if(chance < 1.0F) {
                    tooltip.add(Component.literal(String.format(Locale.US, "%.1f%%", chance * 100.0F)));
                }
            });
        }
    }

    @Override
    public void draw(AnvilConstructionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int overlayU = switch(recipe.getOverlay()) {
            case NONE -> 131;
            case SMITHING -> 149;
            case CONSTRUCTION -> 167;
            case RECYCLING -> 185;
        };

        switch(recipe.getOverlay()) {
            case NONE -> {
                blit(guiGraphics, 2, 5, 5, 87, 72, 54);
                blit(guiGraphics, 92, 5, 5, 87, 72, 54);
                blit(guiGraphics, 74, 14, overlayU, 96, 18, 36);
            }
            case SMITHING -> {
                blit(guiGraphics, 47, 23, 113, 105, 18, 18);
                blit(guiGraphics, 101, 23, 113, 105, 18, 18);
                blit(guiGraphics, 74, 14, overlayU, 96, 18, 36);
            }
            case CONSTRUCTION -> {
                blit(guiGraphics, 11, 5, 5, 87, 108, 54);
                blit(guiGraphics, 137, 23, 113, 105, 18, 18);
                blit(guiGraphics, 119, 14, overlayU, 96, 18, 36);
            }
            case RECYCLING -> {
                blit(guiGraphics, 11, 23, 113, 105, 18, 18);
                blit(guiGraphics, 47, 5, 5, 87, 108, 54);
                blit(guiGraphics, 29, 14, overlayU, 96, 18, 36);
            }
        }

        List<ItemStack> anvils = NTMAnvilBlock.getAnvilsFromTier(recipe.tierLower);
        if(!anvils.isEmpty()) {
            int anvilX = switch(recipe.getOverlay()) {
                case RECYCLING -> 30;
                case CONSTRUCTION -> 120;
                case SMITHING, NONE -> 75;
            };
            guiGraphics.renderItem(anvils.getFirst(), anvilX, 31);
        }
    }

    private static void addInputSlot(IRecipeLayoutBuilder builder, int x, int y, List<ItemStack> stacks) {
        var slot = builder.addInputSlot(x, y).setStandardSlotBackground();
        if(!stacks.isEmpty()) {
            slot.addItemStacks(stacks);
        }
    }

    private static void blit(GuiGraphics guiGraphics, int x, int y, int u, int v, int w, int h) {
        guiGraphics.blit(NuclearTechMod.withDefaultNamespace(TEXTURE), x, y, u, v, w, h, 256, 256);
    }

    private record Layout(int inputLine, int outputLine, int inputX, int inputY, int outputX, int outputY) {
        private static Layout of(com.hbm.inventory.recipes.anvil.AnvilRecipes.OverlayType overlay) {
            return switch(overlay) {
                case SMITHING -> new Layout(1, 1, 48, 24, 102, 24);
                case RECYCLING -> new Layout(1, 6, 12, 24, 48, 6);
                case CONSTRUCTION -> new Layout(6, 1, 12, 6, 138, 24);
                case NONE -> new Layout(4, 4, 3, 6, 93, 6);
            };
        }
    }
}
