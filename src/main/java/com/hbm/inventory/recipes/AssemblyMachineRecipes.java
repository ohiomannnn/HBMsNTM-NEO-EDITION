package com.hbm.inventory.recipes;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.NtmItems;
import com.hbm.main.NuclearTechMod;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

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
        this.register(new GenericRecipe("ass.boytarget").setup(200, 100).outputItems(new ItemStack(NtmItems.LITTLE_BOY_TARGET.get(), 1))
                        .inputItems(new ComparableStack(NtmItems.INGOT_URANIUM.get(), 18)));

        this.register(new GenericRecipe("ass.grass_block").setup(200, 100).outputItems(new ItemStack(Blocks.GRASS_BLOCK, 1))
                .inputItems(
                        new ComparableStack(NtmItems.SINGULARITY.get(), 1),
                        new ComparableStack(NtmItems.SINGULARITY_COUNTER_RESONANT.get(), 1),
                        new ComparableStack(NtmItems.MISSILE_DOOMSDAY_RUSTED.get(), 1)
                )
        );

        this.register(new GenericRecipe("ass.man").setup(200, 100).outputItems(new ItemStack(NtmBlocks.NUKE_FAT_MAN.get(), 1))
                .inputItems(new ComparableStack(NtmItems.PELLET_ANTIMATTER.get(), 1)));
    }
}
