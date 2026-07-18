package com.hbm.inventory.recipes;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.CastPlateItem;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.FluidIconItem;
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

        this.register(new GenericRecipe("ass.machine_refinery").setup(200, 100).outputItems(new ItemStack(NtmBlocks.MACHINE_REFINERY, 1))
                .inputItems(
                        new ComparableStack(NtmItems.PLATE_COPPER.get(), 8),
                        new ComparableStack(NtmItems.SHELL_STEEL.get(), 4),
                        new ComparableStack(NtmItems.PIPE_STEEL.get(), 12),
                        new ComparableStack(NtmItems.INSULATOR.get(), 8),
                        new ComparableStack(NtmItems.CIRCUIT_ANALOG_BOARD.get(), 3),
                        NtmItems.castPlateWeldedIngredient(CastPlateItem.Type.STEEL, 3)

                        ));
        this.register(new GenericRecipe("ass.drill_titanium").setup(200, 100).outputItems(new ItemStack(NtmItems.DRILL_TITANIUM.get(), 1))
                .inputItems(
                        NtmItems.castPlateIngredient(CastPlateItem.Type.DURA_STEEL, 1),
                        new ComparableStack(NtmItems.PLATE_TITANIUM.get(), 8)
                )
        );
        this.register(new GenericRecipe("ass.machine_oil_derrick").setup(200, 100).outputItems(new ItemStack(NtmBlocks.MACHINE_OIL_DERRICK, 1))
                .inputItems(
                        NtmItems.castPlateIngredient(CastPlateItem.Type.COPPER, 1),
                        new ComparableStack(NtmItems.PLATE_STEEL.get(), 8),
                        new ComparableStack(NtmItems.PIPE_STEEL.get(), 4),
                        new ComparableStack(NtmItems.DRILL_TITANIUM.get(), 1),
                        new ComparableStack(NtmItems.MOTOR.get(), 1)

                )
        );


        this.register(new GenericRecipe("ass.man").setup(200, 100).outputItems(new ItemStack(NtmBlocks.NUKE_FAT_MAN.get(), 1))
                .inputItems(new ComparableStack(NtmItems.PELLET_ANTIMATTER.get(), 1)));

        FluidType[] order = Fluids.getInNiceOrder();
        for(int i = 1; i < order.length; ++i) {
            FluidType type = order[i];
            if(type.hasNoContainer()) continue;
            this.register(new GenericRecipe("ass.package" + type.getUnlocalizedName()).setup(40, 100).outputItems(MetaHelper.newStack(NtmItems.FLUID_PACK_FULL, 1, type.getID()))
                    .inputItems(new ComparableStack(NtmItems.FLUID_PACK_EMPTY.get())).inputFluids(new FluidStack(type, 32_000)));
            this.register(new GenericRecipe("ass.unpackage" + type.getUnlocalizedName()).setup(40, 100).setIcon(FluidIconItem.make(type, 32_000)).outputItems(new ItemStack(NtmItems.FLUID_PACK_EMPTY.get()))
                    .inputItems(new ComparableStack(MetaHelper.newStack(NtmItems.FLUID_PACK_FULL, 1, type.getID()))).outputFluids(new FluidStack(type, 32_000)));
        }
    }
}
