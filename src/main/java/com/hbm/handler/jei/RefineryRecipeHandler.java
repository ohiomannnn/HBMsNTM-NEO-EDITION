package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.recipes.RefineryRecipes;
import com.hbm.items.machine.FluidIconItem;
import com.hbm.main.NuclearTechMod;
import com.hbm.util.Tuple;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RefineryRecipeHandler implements IRecipeCategory<RefineryRecipeHandler.RefineryRecipe> {

    public static final RecipeType<RefineryRecipe> RECIPE_TYPE = RecipeType.create(
            NuclearTechMod.MODID,
            "refinery",
            RefineryRecipe.class
    );

    private static final String TEXTURE = "textures/gui/jei/gui_nei_refinery.png";

    private final IDrawable background;
    private final IDrawable icon;

    public RefineryRecipeHandler(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(
                NuclearTechMod.withDefaultNamespace(TEXTURE),
                0, 0, 174, 84
        ).setTextureSize(256, 256).build();
        this.icon = guiHelper.createDrawableItemLike(NtmBlocks.MACHINE_REFINERY.asItem());
    }

    public static List<RefineryRecipe> getRecipes() {
        List<RefineryRecipe> list = new ArrayList<>();

        for(Map.Entry<FluidType, Tuple.Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack>> entry : RefineryRecipes.getRecipes().entrySet()) {
            list.add(new RefineryRecipe(entry.getKey(), entry.getValue()));
        }

        return list;
    }

    @Override
    public RecipeType<RefineryRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.machine_refinery");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RefineryRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(52, 34)
                .addItemStack(FluidIconItem.make(recipe.input, 1000));

        builder.addOutputSlot(115, 16)
                .addItemStack(FluidIconItem.make(recipe.output3Scaled()));
        builder.addOutputSlot(133, 25)
                .addItemStack(FluidIconItem.make(recipe.output4Scaled()));
        builder.addOutputSlot(115, 34)
                .addItemStack(FluidIconItem.make(recipe.output1Scaled()));
        builder.addOutputSlot(133, 43)
                .addItemStack(FluidIconItem.make(recipe.output2Scaled()));
        builder.addOutputSlot(115, 52)
                .addItemStack(recipe.byproduct.copy());
    }

    public static class RefineryRecipe {
        public final FluidType input;
        public final FluidStack output1;
        public final FluidStack output2;
        public final FluidStack output3;
        public final FluidStack output4;
        public final ItemStack byproduct;

        public RefineryRecipe(FluidType input, Tuple.Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack> outputs) {
            this.input = input;
            this.output1 = outputs.getV();
            this.output2 = outputs.getW();
            this.output3 = outputs.getX();
            this.output4 = outputs.getY();
            this.byproduct = outputs.getZ();
        }

        public FluidStack output1Scaled() {
            return scaled(this.output1);
        }

        public FluidStack output2Scaled() {
            return scaled(this.output2);
        }

        public FluidStack output3Scaled() {
            return scaled(this.output3);
        }

        public FluidStack output4Scaled() {
            return scaled(this.output4);
        }

        private static FluidStack scaled(FluidStack stack) {
            return new FluidStack(stack.type, stack.fill * 10, stack.pressure);
        }
    }
}
