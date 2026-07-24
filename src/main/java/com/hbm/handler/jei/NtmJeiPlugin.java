package com.hbm.handler.jei;

import com.hbm.blocks.NtmBlocks;
import com.hbm.handler.jei.AssemblyMachineRecipeHandler;
import com.hbm.handler.jei.AssemblyMachineTransferInfo;
import com.hbm.handler.jei.BoilerRecipeHandler;
import com.hbm.handler.jei.ShredderRecipeHandler;
import com.hbm.handler.jei.subtypes.BatterySubtypeInterpreter;
import com.hbm.handler.jei.subtypes.MetaSubtypeInterpreter;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.AssemblyMachineRecipes;
import com.hbm.inventory.recipes.ArcWelderRecipes;
import com.hbm.inventory.recipes.BlastFurnaceRecipes;
import com.hbm.inventory.recipes.ChemicalPlantRecipes;
import com.hbm.inventory.recipes.CentrifugeRecipes;
import com.hbm.inventory.recipes.CombinationRecipes;
import com.hbm.inventory.recipes.anvil.AnvilRecipes;
import com.hbm.inventory.recipes.SolderingRecipes;
import com.hbm.inventory.recipes.ShredderRecipes;
import com.hbm.inventory.screens.AnvilMenuScreen;
import com.hbm.inventory.screens.MachineFurnaceCombinationScreen;
import com.hbm.inventory.screens.MachineAssemblyMachineScreen;
import com.hbm.inventory.screens.MachineArcWelderScreen;
import com.hbm.inventory.screens.MachineSolderingStationScreen;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.FluidIconItem;
import com.hbm.main.NuclearTechMod;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IExtraIngredientRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
@SuppressWarnings("unused")
public class NtmJeiPlugin implements IModPlugin {

    @Override public ResourceLocation getPluginUid() { return NuclearTechMod.withDefaultNamespace("jei_plugin"); }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new AnvilConstructionRecipeHandler(guiHelper),
                new AnvilRecipeHandler(guiHelper),
                new SolderingStationRecipeHandler(guiHelper),
                new ArcWelderRecipeHandler(guiHelper),
                new AssemblyMachineRecipeHandler(guiHelper),
                new BlastFurnaceRecipeHandler(guiHelper),
                new CentrifugeRecipeHandler(guiHelper),
                new ChemicalPlantRecipeHandler(guiHelper),
                new FurnaceCombinationRecipeHandler(guiHelper),
                new PressRecipeHandler(guiHelper),
                new ShredderRecipeHandler(guiHelper),
                new RefineryRecipeHandler(guiHelper),
                new BoilerRecipeHandler(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(AnvilConstructionRecipeHandler.RECIPE_TYPE, AnvilRecipes.getConstruction());
        registration.addRecipes(AnvilRecipeHandler.RECIPE_TYPE, AnvilRecipes.getSmithing());
        registration.addRecipes(SolderingStationRecipeHandler.RECIPE_TYPE, SolderingRecipes.recipes);
        registration.addRecipes(ArcWelderRecipeHandler.RECIPE_TYPE, ArcWelderRecipes.recipes);
        registration.addRecipes(AssemblyMachineRecipeHandler.RECIPE_TYPE, AssemblyMachineRecipes.INSTANCE.recipeOrderedList);
        registration.addRecipes(BlastFurnaceRecipeHandler.RECIPE_TYPE, BlastFurnaceRecipes.INSTANCE.recipeOrderedList);
        registration.addRecipes(CentrifugeRecipeHandler.RECIPE_TYPE, CentrifugeRecipes.getJeiRecipes());
        registration.addRecipes(ChemicalPlantRecipeHandler.RECIPE_TYPE, ChemicalPlantRecipes.INSTANCE.recipeOrderedList);
        registration.addRecipes(FurnaceCombinationRecipeHandler.RECIPE_TYPE, CombinationRecipes.getJeiRecipes());
        registration.addRecipes(PressRecipeHandler.RECIPE_TYPE, PressRecipeHandler.getRecipes());
        registration.addRecipes(ShredderRecipeHandler.RECIPE_TYPE, ShredderRecipes.getJeiRecipes());
        registration.addRecipes(RefineryRecipeHandler.RECIPE_TYPE, RefineryRecipeHandler.getRecipes());
        registration.addRecipes(BoilerRecipeHandler.RECIPE_TYPE, BoilerRecipeHandler.getRecipes());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for(com.hbm.blocks.machine.NTMAnvilBlock.Variant variant : com.hbm.blocks.machine.NTMAnvilBlock.Variant.values()) {
            registration.addRecipeCatalyst(
                    MetaHelper.newStack(NtmBlocks.ANVIL.asItem(), variant.ordinal()),
                    AnvilConstructionRecipeHandler.RECIPE_TYPE
            );
            registration.addRecipeCatalyst(
                    MetaHelper.newStack(NtmBlocks.ANVIL.asItem(), variant.ordinal()),
                    AnvilRecipeHandler.RECIPE_TYPE
            );
        }

        registration.addRecipeCatalyst(
                NtmBlocks.MACHINE_SOLDERING_STATION.asItem(),
                SolderingStationRecipeHandler.RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                NtmBlocks.MACHINE_ARC_WELDER.asItem(),
                ArcWelderRecipeHandler.RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                NtmBlocks.MACHINE_ASSEMBLY_MACHINE.asItem(),
                AssemblyMachineRecipeHandler.RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                NtmBlocks.MACHINE_BLAST_FURNACE.asItem(),
                BlastFurnaceRecipeHandler.RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                NtmBlocks.MACHINE_SHREDDER.asItem(),
                ShredderRecipeHandler.RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                NtmBlocks.MACHINE_CENTRIFUGE.asItem(),
                CentrifugeRecipeHandler.RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                NtmBlocks.MACHINE_CHEMICAL_PLANT.asItem(),
                ChemicalPlantRecipeHandler.RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                NtmBlocks.FURNACE_COMBINATION.asItem(),
                FurnaceCombinationRecipeHandler.RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                NtmBlocks.MACHINE_PRESS.asItem(),
                PressRecipeHandler.RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                NtmBlocks.MACHINE_REFINERY.asItem(),
                RefineryRecipeHandler.RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                NtmBlocks.HEAT_BOILER.asItem(),
                BoilerRecipeHandler.RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                NtmBlocks.MACHINE_INDUSTRIAL_BOILER.asItem(),
                BoilerRecipeHandler.RECIPE_TYPE
        );
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new SolderingStationTransferInfo());
        registration.addRecipeTransferHandler(new AssemblyMachineTransferInfo());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AnvilMenuScreen.class, 12, 50, 36, 16, AnvilConstructionRecipeHandler.RECIPE_TYPE);
        registration.addRecipeClickArea(MachineSolderingStationScreen.class, 72, 29, 32, 13, SolderingStationRecipeHandler.RECIPE_TYPE);
        registration.addRecipeClickArea(MachineArcWelderScreen.class, 72, 36, 32, 13, ArcWelderRecipeHandler.RECIPE_TYPE);
        registration.addRecipeClickArea(MachineFurnaceCombinationScreen.class, 54, 61, 18, 18, FurnaceCombinationRecipeHandler.RECIPE_TYPE);
        registration.addRecipeClickArea(com.hbm.inventory.screens.MachinePressScreen.class, 79, 35, 18, 18, PressRecipeHandler.RECIPE_TYPE);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration regs) {

        List<Item> ignoreMeta = List.of(

                NtmItems.ROD.get(),
                NtmItems.ROD_DUAL.get(),
                NtmItems.ROD_QUAD.get(),

                NtmItems.STARTER_KIT.get(),

                NtmItems.MISSILE_SOYUZ.get(),

                NtmItems.BATTERY_SC.get(),
                NtmItems.WIRE_DENSE.get(),
                NtmItems.BOLT.get(),
                NtmItems.PART_GENERIC.get(),
                NtmItems.CAST_PLATE.get(),
                NtmItems.CAST_PLATE_WELDED.get(),

                NtmItems.FLUID_TANK_FULL.get(),
                NtmItems.FLUID_TANK_LEAD_FULL.get(),
                NtmItems.FLUID_BARREL_FULL.get(),
                NtmItems.FLUID_PACK_FULL.get(),

                NtmItems.FLUID_ICON.get(),
                NtmItems.FLUID_IDENTIFIER_MULTI.get(),
                NtmItems.INGOT_RAW.get(),

                NtmItems.DRINK.get(),
                NtmItems.CANNED_CONSERVE.get(),
                NtmItems.CAP.get(),

                NtmBlocks.BOBBLEHEAD.asItem(),
                NtmBlocks.PLUSHIE.asItem(),

                NtmBlocks.BARBED_WIRE.asItem(),

                NtmBlocks.FLUID_DUCT_NEO.asItem(),

                NtmBlocks.CRASHED_BOMB.asItem()
        );

        for(Item item : ignoreMeta) {
            regs.registerSubtypeInterpreter(item, MetaSubtypeInterpreter.INSTANCE);
        }

        regs.registerSubtypeInterpreter(NtmItems.BATTERY_PACK.get(), BatterySubtypeInterpreter.INSTANCE);
    }

    @Override
    public void registerExtraIngredients(IExtraIngredientRegistration regs) {
        List<ItemStack> extra = new ArrayList<>();

        FluidType[] types = Fluids.getInNiceOrder();
        for(int i = 1; i < types.length; ++i) {
            FluidType type = types[i];

            extra.add(FluidIconItem.make(type, 1000));
        }

        regs.addExtraItemStacks(extra);
    }
}
