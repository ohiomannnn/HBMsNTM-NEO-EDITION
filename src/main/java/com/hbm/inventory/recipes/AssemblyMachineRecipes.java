package com.hbm.inventory.recipes;

import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;

public class AssemblyMachineRecipes extends GenericRecipes<GenericRecipe> {

    public static final AssemblyMachineRecipes INSTANCE = new AssemblyMachineRecipes();

    @Override public int inputItemLimit() { return 12; }
    @Override public int inputFluidLimit() { return 1; }
    @Override public int outputItemLimit() { return 1; }
    @Override public int outputFluidLimit() { return 1; }

    @Override public String getFileName() { return "hbmAssemblyMachine.json"; }
    @Override public GenericRecipe instantiateRecipe(String name) { return new GenericRecipe(name); }

    @Override
    public void registerDefaults() {

    }
}
