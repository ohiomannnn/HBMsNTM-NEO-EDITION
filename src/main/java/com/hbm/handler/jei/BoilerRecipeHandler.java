package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.trait.FT_Heatable;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BoilerRecipeHandler implements IRecipeCategory<BoilerRecipeHandler.BoilerRecipe> {

    public static final RecipeType<BoilerRecipe> RECIPE_TYPE = RecipeType.create(
            NuclearTechMod.MODID,
            "boiler",
            BoilerRecipe.class
    );

    public static List<BoilerRecipe> getRecipes() {
        List<BoilerRecipe> list = new ArrayList<>();

        FluidType[] types = Fluids.getInNiceOrder();
        for(int i = 1; i < types.length; i++) {
            FluidType type = types[i];
            if(type == null || !type.hasTrait(FT_Heatable.class)) continue;

            FT_Heatable trait = type.getTrait(FT_Heatable.class);
            if(!trait.hasSteps() || trait.getEfficiency(FT_Heatable.HeatingType.BOILER) <= 0) continue;

            list.add(new BoilerRecipe(type, trait.getFirstStep(), trait.getEfficiency(FT_Heatable.HeatingType.BOILER)));
        }

        return list;
    }

    private final IDrawable background;
    private final IDrawable icon;

    public BoilerRecipeHandler(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(
                NuclearTechMod.withDefaultNamespace("textures/gui/jei/gui_nei.png"),
                5, 11, 166, 65
        ).setTextureSize(256, 256).build();
        this.icon = guiHelper.createDrawableItemLike(NtmBlocks.HEAT_BOILER.asItem());
    }

    @Override
    public RecipeType<BoilerRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.heat_boiler");
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
    public void setRecipe(IRecipeLayoutBuilder builder, BoilerRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(38, 23)
                .setStandardSlotBackground()
                .addItemStack(FluidIconItem.make(recipe.inputType, recipe.step.amountReq));

        builder.addOutputSlot(110, 23)
                .setStandardSlotBackground()
                .addItemStack(FluidIconItem.make(recipe.step.typeProduced, recipe.step.amountProduced));
    }

    @Override
    public void draw(BoilerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;

        String heat = String.format(Locale.US, "%,d TU", recipe.getHeatRequirement());
        String eff = String.format(Locale.US, "%,d%%", (int)(recipe.efficiency * 100D));
        guiGraphics.drawString(font, heat, 74 - font.width(heat) / 2, 43, 0x404040, false);
        guiGraphics.drawString(font, eff, 74 - font.width(eff) / 2, 55, 0x404040, false);
    }

    public static class BoilerRecipe {
        public final FluidType inputType;
        public final FT_Heatable.HeatingStep step;
        public final double efficiency;

        public BoilerRecipe(FluidType inputType, FT_Heatable.HeatingStep step, double efficiency) {
            this.inputType = inputType;
            this.step = step;
            this.efficiency = efficiency;
        }

        public int getHeatRequirement() {
            return (int)Math.max(this.step.heatReq / this.efficiency, 1);
        }
    }
}
