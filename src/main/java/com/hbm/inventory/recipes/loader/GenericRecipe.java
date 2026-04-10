package com.hbm.inventory.recipes.loader;

import com.hbm.config.MainConfig;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.loader.GenericRecipes.ChanceOutput;
import com.hbm.inventory.recipes.loader.GenericRecipes.ChanceOutputMulti;
import com.hbm.inventory.recipes.loader.GenericRecipes.IOutput;
import com.hbm.items.machine.FluidIconItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public class GenericRecipe {

    protected final String name;
    public String nameWrapper;
    public AStack[] inputItem;
    public FluidStack[] inputFluid;
    public IOutput[] outputItem;
    public FluidStack[] outputFluid;
    public int duration;
    public long power;
    protected ItemStack icon;
    public boolean writeIcon = false;
    public boolean customLocalization = false;
    protected String[] blueprintPools = null;
    public String autoSwitchGroup = null;

    public GenericRecipe(String name) {
        this.name = name;
    }

    public boolean isPooled() { return blueprintPools != null; }
    public String[] getPools() { return this.blueprintPools; }

    public GenericRecipe setDuration(int duration) { this.duration = duration; return this; }
    public GenericRecipe setPower(long power) { this.power = power; return this; }
    public GenericRecipe setup(int duration, long power) { return this.setDuration(duration).setPower(power); }
    public GenericRecipe setupNamed(int duration, long power) { return this.setDuration(duration).setPower(power).setNamed(); }
    public GenericRecipe setNameWrapper(String wrapper) { this.nameWrapper = wrapper; return this; }
    public GenericRecipe setIcon(ItemStack icon) { this.icon = icon; this.writeIcon = true; return this; }
    public GenericRecipe setIcon(ItemLike item, int meta) { return this.setIcon(MetaHelper.newStack(item, 1, meta)); }
    public GenericRecipe setIcon(ItemLike item) { return this.setIcon(new ItemStack(item)); }
    public GenericRecipe setNamed() { this.customLocalization = true; return this; }

    public GenericRecipe setPools(String... pools) {
        this.blueprintPools = pools;
        for(String pool : pools) {
            if(!MainConfig.COMMON.ENABLE_528.get() && pool.startsWith(GenericRecipes.POOL_PREFIX_528)) throw new IllegalArgumentException("Tried initializing a recipe's default blueprint pool with a 528 blueprint - this is not allowed.");
            GenericRecipes.addToPool(pool, this);
        }
        return this;
    }
    /** Only for recipe configs - same as regular except the anti 528 check doesn't exist */
    public GenericRecipe setPoolsAllow528(String... pools) { this.blueprintPools = pools; for(String pool : pools) GenericRecipes.addToPool(pool, this); return this; }
    public GenericRecipe setPools528(String... pools) { if(MainConfig.COMMON.ENABLE_528.get()) { this.blueprintPools = pools; for(String pool : pools) GenericRecipes.addToPool(pool, this); } return this; }
    public GenericRecipe setGroup(String autoSwitch, GenericRecipes set) { this.autoSwitchGroup = autoSwitch; set.addToGroup(autoSwitch, this); return this; }

    public GenericRecipe inputItems(AStack... input) { this.inputItem = input; for(AStack stack : this.inputItem) checkStackLimit(stack); return this; }
    public GenericRecipe inputItemsEx(AStack... input) { if(!MainConfig.COMMON.ENABLE_EXPENSIVE_MODE.get()) return this; this.inputItem = input; for(AStack stack : this.inputItem) checkStackLimit(stack); return this; }
    public GenericRecipe inputFluids(FluidStack... input) { this.inputFluid = input; return this; }
    public GenericRecipe inputFluidsEx(FluidStack... input) { if(!MainConfig.COMMON.ENABLE_EXPENSIVE_MODE.get()) return this; this.inputFluid = input; return this; }
    public GenericRecipe outputItems(IOutput... output) { this.outputItem = output; return this; }
    public GenericRecipe outputFluids(FluidStack... output) { this.outputFluid = output; return this; }

    private void checkStackLimit(AStack stack) {
        int max = 64;
        if(stack instanceof ComparableStack) {
            ComparableStack comp = (ComparableStack) stack;
            max = comp.item.getMaxStackSize(comp.toStack());
        }
        if(stack.stacksize > max) throw new IllegalArgumentException("AStack " + stack + " in " + this.name + " exceeds stack limit of " + max + "!");
    }

    public GenericRecipe outputItems(ItemStack... output) {
        this.outputItem = new IOutput[output.length];
        for(int i = 0; i < outputItem.length; i++) this.outputItem[i] = new ChanceOutput(output[i]);
        return this;
    }

    public GenericRecipe setIconToFirstIngredient() {
        if(this.inputItem != null) {
            List<ItemStack> stacks = this.inputItem[0].extractForJEI();
            if(!stacks.isEmpty()) this.icon = stacks.get(0);
        }
        return this;
    }

    public ItemStack getIcon() {

        if(icon == null) {
            if(outputItem != null) {
                if(outputItem[0] instanceof ChanceOutput) icon = ((ChanceOutput) outputItem[0]).stack.copy();
                if(outputItem[0] instanceof ChanceOutputMulti) icon = ((ChanceOutputMulti) outputItem[0]).pool.get(0).stack.copy();
                return icon;
            }
            if(outputFluid != null) {
                icon = FluidIconItem.make(outputFluid[0]);
            }
        }

        if(icon == null) icon = ItemStack.EMPTY;
        return icon;
    }

    public String getInternalName() {
        return this.name;
    }

    public Component getName() {
        Component name = Component.empty();
        if(customLocalization) name = Component.translatable(this.name);
        if(name.equals(Component.empty())) name = this.getIcon().getDisplayName();
        if(this.nameWrapper != null) name = Component.translatable(this.nameWrapper, name);
        return name;
    }
}
