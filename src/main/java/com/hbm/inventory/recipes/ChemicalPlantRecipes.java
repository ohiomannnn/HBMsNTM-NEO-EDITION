package com.hbm.inventory.recipes;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.RecipesCommon.TagStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.FuelAdditiveItem;
import com.hbm.items.NtmItems;
import com.hbm.items.PartGenericItem;
import com.hbm.items.RawIngotItem;
import com.hbm.items.BoltItem;
import com.hbm.items.CastPlateItem;
import com.hbm.items.WireDenseItem;
import com.hbm.items.machine.BatteryPackItem.BatteryPackType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class ChemicalPlantRecipes extends GenericRecipes<GenericRecipe> {

    public static final ChemicalPlantRecipes INSTANCE = new ChemicalPlantRecipes();

    @Override public int inputItemLimit() { return 3; }
    @Override public int inputFluidLimit() { return 3; }
    @Override public int outputItemLimit() { return 3; }
    @Override public int outputFluidLimit() { return 3; }
    @Override public String getFileName() { return "hbmChemicalPlant.json"; }
    @Override public GenericRecipe instantiateRecipe(String name) { return new GenericRecipe(name); }

    @Override
    public void registerDefaults() {
        if(!this.recipeOrderedList.isEmpty()) return;

        this.register(new GenericRecipe("chem.hydrogen").setupNamed(20, 400L)
                .setIcon(NtmItems.FLUID_ICON.get(), Fluids.HYDROGEN.getID())
                .inputItems(new ComparableStack(Items.COAL))
                .inputFluids(new FluidStack(Fluids.WATER, 8000))
                .outputFluids(new FluidStack(Fluids.HYDROGEN, 500)));
        this.register(new GenericRecipe("chem.hydrogencoke").setupNamed(20, 400L)
                .setIcon(NtmItems.FLUID_ICON.get(), Fluids.HYDROGEN.getID())
                .inputItems(new ComparableStack(NtmItems.COKE_COAL.get()))
                .inputFluids(new FluidStack(Fluids.WATER, 8000))
                .outputFluids(new FluidStack(Fluids.HYDROGEN, 500)));
        this.register(new GenericRecipe("chem.oxygen").setupNamed(20, 400L)
                .setIcon(NtmItems.FLUID_ICON.get(), Fluids.OXYGEN.getID())
                .inputFluids(new FluidStack(Fluids.AIR, 8000))
                .outputFluids(new FluidStack(Fluids.OXYGEN, 500)));
        this.register(new GenericRecipe("chem.xenon").setupNamed(300, 1000L)
                .setIcon(NtmItems.FLUID_ICON.get(), Fluids.XENON.getID())
                .inputFluids(new FluidStack(Fluids.AIR, 16000))
                .outputFluids(new FluidStack(Fluids.XENON, 50)));
        this.register(new GenericRecipe("chem.xenonoxy").setupNamed(20, 1000L)
                .setIcon(NtmItems.FLUID_ICON.get(), Fluids.XENON.getID())
                .inputFluids(new FluidStack(Fluids.AIR, 8000), new FluidStack(Fluids.OXYGEN, 250))
                .outputFluids(new FluidStack(Fluids.XENON, 50))
                .setPools("alt..xenonoxy"));
        this.register(new GenericRecipe("chem.helium3").setupNamed(200, 2000L)
                .setIcon(NtmItems.FLUID_ICON.get(), Fluids.HELIUM_3.getID())
                .inputItems(new ComparableStack(NtmBlocks.MOON_TURF.get(), 8))
                .outputFluids(new FluidStack(Fluids.HELIUM_3, 1000)));
        this.register(new GenericRecipe("chem.co2").setupNamed(60, 100L)
                .inputFluids(new FluidStack(Fluids.GAS, 1000))
                .outputFluids(new FluidStack(Fluids.CARBONDIOXIDE, 1000)));
        this.register(new GenericRecipe("chem.perfluoromethyl").setupNamed(20, 100L)
                .inputItems(new ComparableStack(NtmItems.FLUORITE.get()))
                .inputFluids(new FluidStack(Fluids.PETROLEUM, 1000), new FluidStack(Fluids.UNSATURATEDS, 500))
                .outputFluids(new FluidStack(Fluids.PERFLUOROMETHYL, 1000)));
        this.register(new GenericRecipe("chem.cccentrifuge").setupNamed(200, 100L)
                .inputFluids(new FluidStack(Fluids.CHLOROCALCITE_CLEANED, 500), new FluidStack(Fluids.SULFURIC_ACID, 8000))
                .outputFluids(new FluidStack(Fluids.POTASSIUM_CHLORIDE, 250), new FluidStack(Fluids.CALCIUM_CHLORIDE, 250)));
        this.register(new GenericRecipe("chem.ethanol").setupNamed(50, 100L)
                .setIcon(NtmItems.FLUID_ICON.get(), Fluids.ETHANOL.getID())
                .inputItems(new ComparableStack(Items.SUGAR, 10))
                .outputFluids(new FluidStack(Fluids.ETHANOL, 1000)));
        this.register(new GenericRecipe("chem.biogas").setupNamed(60, 100L)
                .setIcon(NtmItems.FLUID_ICON.get(), Fluids.BIOGAS.getID())
                .inputItems(new ComparableStack(NtmItems.BIOMASS.get(), 16))
                .inputFluids(new FluidStack(Fluids.AIR, 4000))
                .outputFluids(new FluidStack(Fluids.BIOGAS, 2000)));
        this.register(new GenericRecipe("chem.biofuel").setupNamed(60, 100L)
                .setIcon(NtmItems.FLUID_ICON.get(), Fluids.BIOFUEL.getID())
                .inputFluids(new FluidStack(Fluids.BIOGAS, 1500), new FluidStack(Fluids.ETHANOL, 250))
                .outputFluids(new FluidStack(Fluids.BIOFUEL, 1000)));
        this.register(new GenericRecipe("chem.reoil").setupNamed(40, 100L)
                .setIcon(NtmItems.FLUID_ICON.get(), Fluids.OIL_INDUSTRIAL_RECLAIMED.getID())
                .inputFluids(new FluidStack(Fluids.OIL_INDUSTRIAL, 1000))
                .outputFluids(new FluidStack(Fluids.OIL_INDUSTRIAL_RECLAIMED, 800)));
        this.register(new GenericRecipe("chem.gasoline").setupNamed(40, 100L)
                .setIcon(NtmItems.FLUID_ICON.get(), Fluids.GASOLINE.getID())
                .inputFluids(new FluidStack(Fluids.NAPHTHA, 1000))
                .outputFluids(new FluidStack(Fluids.GASOLINE, 800)));
        this.register(new GenericRecipe("chem.tarsand").setupNamed(200, 100L)
                .setIcon(NtmBlocks.ORE_OIL_SAND.get())
                .inputItems(new ComparableStack(NtmBlocks.ORE_OIL_SAND.get(), 16), new RecipesCommon.TagStack(ItemTags.create(ResourceLocation.fromNamespaceAndPath("ntm", "any_tars"))))
                .outputItems(new ItemStack(Blocks.SAND, 16))
                .outputFluids(new FluidStack(Fluids.BITUMEN, 1000)));
        this.register(new GenericRecipe("chem.tel").setupNamed(40, 100L)
                .inputItems(new RecipesCommon.TagStack(ItemTags.create(ResourceLocation.fromNamespaceAndPath("ntm", "any_tars"))), new ComparableStack(NtmItems.POWDER_LEAD.get()))
                .inputFluids(new FluidStack(Fluids.PETROLEUM, 100), new FluidStack(Fluids.STEAM, 1000))
                .outputItems(MetaHelper.newStack(NtmItems.FUEL_ADDITIVE.get(), 1, FuelAdditiveItem.Type.ANTIKNOCK.ordinal())));
        this.register(new GenericRecipe("chem.deicer").setupNamed(40, 100L)
                .inputFluids(new FluidStack(Fluids.GAS, 100), new FluidStack(Fluids.HYDROGEN, 50))
                .outputItems(MetaHelper.newStack(NtmItems.FUEL_ADDITIVE.get(), 1, FuelAdditiveItem.Type.DEICER.ordinal())));
        this.register(new GenericRecipe("chem.cobble").setupNamed(20, 100L)
                .inputFluids(new FluidStack(Fluids.WATER, 1000), new FluidStack(Fluids.LAVA, 25))
                .outputItems(new ItemStack(Blocks.COBBLESTONE)));
        this.register(new GenericRecipe("chem.stone").setupNamed(60, 500L)
                .setPools("discover..stone")
                .inputFluids(new FluidStack(Fluids.WATER, 1000), new FluidStack(Fluids.LAVA, 25), new FluidStack(Fluids.AIR, 4000))
                .outputItems(new ItemStack(Blocks.STONE)));
        this.register(new GenericRecipe("chem.obsidian").setupNamed(60, 500L)
                .setPools("discover..stone")
                .inputFluids(new FluidStack(Fluids.WATER, 1000), new FluidStack(Fluids.LAVA, 500), new FluidStack(Fluids.AIR, 4000))
                .outputItems(new ItemStack(Blocks.OBSIDIAN)));
        this.register(new GenericRecipe("chem.aggregate").setupNamed(320, 500L)
                .setPools("discover..stone")
                .inputItems(new ComparableStack(Blocks.COBBLESTONE, 16))
                .outputItems(new ItemStack(Blocks.GRAVEL, 8), new ItemStack(Blocks.SAND, 8)));
        this.register(new GenericRecipe("chem.concrete").setupNamed(100, 100L)
                .inputItems(new ComparableStack(NtmItems.POWDER_CEMENT.get()), new ComparableStack(Blocks.GRAVEL, 8), new ComparableStack(Blocks.SAND, 8))
                .inputFluids(new FluidStack(Fluids.WATER, 2000))
                .outputItems(new ItemStack(NtmBlocks.CONCRETE_SMOOTH.get(), 16)));
        this.register(new GenericRecipe("chem.concreteasbestos").setupNamed(100, 100L)
                .inputItems(new ComparableStack(NtmItems.POWDER_CEMENT.get(), 4), new ComparableStack(NtmItems.INGOT_ASBESTOS.get(), 4), new ComparableStack(Blocks.SAND, 8))
                .inputFluids(new FluidStack(Fluids.WATER, 2000))
                .outputItems(new ItemStack(NtmBlocks.CONCRETE_ASBESTOS.get(), 16)));
        this.register(new GenericRecipe("chem.ducrete").setupNamed(150, 100L)
                .inputItems(new ComparableStack(NtmItems.POWDER_CEMENT.get(), 4), new ComparableStack(NtmItems.INGOT_FERROURANIUM.get()), new ComparableStack(Blocks.SAND, 8))
                .inputFluids(new FluidStack(Fluids.WATER, 2000))
                .outputItems(new ItemStack(NtmBlocks.DUCRETE_SMOOTH.get(), 8)));
        this.register(new GenericRecipe("chem.liquidconk").setupNamed(100, 100L)
                .inputItems(new ComparableStack(NtmItems.POWDER_CEMENT.get()), new ComparableStack(Blocks.GRAVEL, 8), new ComparableStack(Blocks.SAND, 8))
                .inputFluids(new FluidStack(Fluids.WATER, 2000))
                .outputFluids(new FluidStack(Fluids.CONCRETE, 16000)));
        this.register(new GenericRecipe("chem.asphalt").setupNamed(100, 100L)
                .inputItems(new ComparableStack(Blocks.GRAVEL, 2), new ComparableStack(Blocks.SAND, 6))
                .inputFluids(new FluidStack(Fluids.BITUMEN, 1000))
                .outputItems(new ItemStack(NtmBlocks.ASPHALT.get(), 16)));
        this.register(new GenericRecipe("chem.batterylead").setupNamed(100, 100L)
                .inputItems(new ComparableStack(NtmItems.PLATE_STEEL.get(), 4), new ComparableStack(NtmItems.INGOT_LEAD.get(), 4))
                .inputFluids(new FluidStack(Fluids.SULFURIC_ACID, 8000))
                .outputItems(MetaHelper.newStack(NtmItems.BATTERY_PACK.get(), 1, BatteryPackType.BATTERY_LEAD.ordinal())));
        this.register(new GenericRecipe("chem.batterylithium").setupNamed(100, 1000L)
                .inputItems(new ComparableStack(NtmItems.POWDER_LITHIUM.get(), 12), new ComparableStack(NtmItems.POWDER_COBALT.get(), 8), new ComparableStack(NtmItems.INGOT_POLYMER.get(), 4))
                .inputFluids(new FluidStack(Fluids.OXYGEN, 2000))
                .outputItems(MetaHelper.newStack(NtmItems.BATTERY_PACK.get(), 1, BatteryPackType.BATTERY_LITHIUM.ordinal())));
        this.register(new GenericRecipe("chem.batterysodium").setupNamed(100, 10000L)
                .inputItems(new ComparableStack(NtmItems.POWDER_SODIUM.get(), 24), new ComparableStack(NtmItems.POWDER_IRON.get(), 24), new ComparableStack(NtmItems.INGOT_PC.get(), 12))
                .outputItems(MetaHelper.newStack(NtmItems.BATTERY_PACK.get(), 1, BatteryPackType.BATTERY_SODIUM.ordinal())));
        this.register(new GenericRecipe("chem.batteryschrabidium").setupNamed(100, 25000L)
                .inputItems(new ComparableStack(NtmItems.POWDER_SCHRABIDIUM.get(), 24), new ComparableStack(NtmItems.CAST_PLATE.get(), 8, CastPlateItem.Type.IRON.ordinal()))
                .inputFluids(new FluidStack(Fluids.HELIUM_4, 8000))
                .outputItems(MetaHelper.newStack(NtmItems.BATTERY_PACK.get(), 1, BatteryPackType.BATTERY_SCHRABIDIUM.ordinal())));
        this.register(new GenericRecipe("chem.batteryquantum").setupNamed(100, 100000L)
                .inputItems(new ComparableStack(NtmItems.WIRE_DENSE.get(), 24, WireDenseItem.Type.GOLD.meta), new ComparableStack(NtmItems.PELLET_CHARGED.get(), 32), new ComparableStack(NtmItems.INGOT_CTF.get(), 16))
                .inputFluids(new FluidStack(Fluids.COLD_PERFLUOROMETHYL, 8000))
                .outputItems(MetaHelper.newStack(NtmItems.BATTERY_PACK.get(), 1, BatteryPackType.BATTERY_QUANTUM.ordinal()))
                .outputFluids(new FluidStack(Fluids.PERFLUOROMETHYL, 8000)));
        this.register(new GenericRecipe("chem.desh").setupNamed(100, 100L)
                .inputItems(new ComparableStack(NtmItems.POWDER_DESH_MIX.get()))
                .inputFluids(new FluidStack(Fluids.OIL_LIGHT, 200), new FluidStack(Fluids.MERCURY, 200))
                .outputItems(new ItemStack(NtmItems.INGOT_DESH.get())));
        this.register(new GenericRecipe("chem.deshcracked").setupNamed(100, 100L)
                .inputItems(new ComparableStack(NtmItems.POWDER_DESH_MIX.get()))
                .inputFluids(new FluidStack(Fluids.OIL_LIGHT_CRACKED, 500, 1), new FluidStack(Fluids.MERCURY, 100))
                .outputItems(new ItemStack(NtmItems.INGOT_DESH.get())));
        this.register(new GenericRecipe("chem.polymer").setupNamed(100, 100L)
                .inputItems(new ComparableStack(NtmItems.POWDER_COAL.get(), 2), new ComparableStack(NtmItems.FLUORITE.get()))
                .inputFluids(new FluidStack(Fluids.PETROLEUM, 500))
                .outputItems(new ItemStack(NtmItems.INGOT_POLYMER.get(), 1)));
        this.register(new GenericRecipe("chem.bakelite").setupNamed(100, 100L)
                .inputFluids(new FluidStack(Fluids.AROMATICS, 500), new FluidStack(Fluids.PETROLEUM, 500))
                .outputItems(new ItemStack(NtmItems.INGOT_BAKELITE.get())));
        this.register(new GenericRecipe("chem.rubber").setupNamed(100, 200L)
                .inputItems(new ComparableStack(NtmItems.SULFUR.get()))
                .inputFluids(new FluidStack(Fluids.UNSATURATEDS, 500))
                .outputItems(new ItemStack(NtmItems.INGOT_RUBBER.get(), 2)));
        this.register(new GenericRecipe("chem.hardplastic").setupNamed(100, 1000L)
                .inputFluids(new FluidStack(Fluids.XYLENE, 500), new FluidStack(Fluids.PHOSGENE, 500))
                .outputItems(new ItemStack(NtmItems.INGOT_PC.get())));
        this.register(new GenericRecipe("chem.pvc").setupNamed(100, 1000L)
                .inputItems(new ComparableStack(NtmItems.POWDER_CADMIUM.get()))
                .inputFluids(new FluidStack(Fluids.UNSATURATEDS, 250), new FluidStack(Fluids.CHLORINE, 250))
                .outputItems(new ItemStack(NtmItems.INGOT_PVC.get(), 2)));
        this.register(new GenericRecipe("chem.kevlar").setupNamed(60, 300L)
                .inputFluids(new FluidStack(Fluids.AROMATICS, 200), new FluidStack(Fluids.NITRIC_ACID, 100), new FluidStack(Fluids.PHOSGENE, 100))
                .outputItems(new ItemStack(NtmItems.PLATE_KEVLAR.get(), 4)));
        this.register(new GenericRecipe("chem.meth").setupNamed(60, 300L)
                .inputItems(new ComparableStack(Items.WHEAT), new ComparableStack(Items.COCOA_BEANS, 2))
                .inputFluids(new FluidStack(Fluids.LUBRICANT, 400), new FluidStack(Fluids.PEROXIDE, 500))
                .outputItems(new ItemStack(NtmItems.CHOCOLATE.get(), 4)));
        this.register(new GenericRecipe("chem.epearl").setupNamed(100, 300L)
                .inputItems(new ComparableStack(NtmItems.POWDER_DIAMOND.get()))
                .inputFluids(new FluidStack(Fluids.XPJUICE, 500))
                .outputFluids(new FluidStack(Fluids.ENDERJUICE, 100)));
        this.register(new GenericRecipe("chem.meatprocessing").setupNamed(200, 200L)
                .setIcon(NtmItems.GLYPHID_MEAT.get())
                .inputItems(new ComparableStack(NtmItems.GLYPHID_MEAT.get(), 3))
                .inputFluids(new FluidStack(Fluids.WATER, 1000))
                .outputItems(new ItemStack(NtmItems.SULFUR.get(), 4), new ItemStack(NtmItems.NITER.get(), 3))
                .outputFluids(new FluidStack(Fluids.SALIENT, 250)));
        this.register(new GenericRecipe("chem.rustysteel").setupNamed(40, 100L)
                .inputItems(new ComparableStack(NtmBlocks.DECO_STEEL.get(), 8))
                .inputFluids(new FluidStack(Fluids.WATER, 1000))
                .outputItems(new ItemStack(NtmBlocks.DECO_RUSTY_STEEL.get(), 8)));
        this.register(new GenericRecipe("chem.peroxide").setupNamed(50, 100L)
                .inputFluids(new FluidStack(Fluids.WATER, 1000))
                .outputFluids(new FluidStack(Fluids.PEROXIDE, 1000)));
        this.register(new GenericRecipe("chem.sulfuricacid").setupNamed(50, 100L)
                .inputItems(new ComparableStack(NtmItems.SULFUR.get()))
                .inputFluids(new FluidStack(Fluids.PEROXIDE, 1000), new FluidStack(Fluids.WATER, 1000))
                .outputFluids(new FluidStack(Fluids.SULFURIC_ACID, 2000)));
        this.register(new GenericRecipe("chem.nitricacid").setupNamed(50, 100L)
                .inputItems(new ComparableStack(NtmItems.NITER.get()))
                .inputFluids(new FluidStack(Fluids.SULFURIC_ACID, 500))
                .outputFluids(new FluidStack(Fluids.NITRIC_ACID, 1000)));
        this.register(new GenericRecipe("chem.birkeland").setupNamed(200, 5000L)
                .inputFluids(new FluidStack(Fluids.AIR, 8000), new FluidStack(Fluids.WATER, 2000))
                .outputFluids(new FluidStack(Fluids.NITRIC_ACID, 1000))
                .setPools("alt..birkeland"));
        this.register(new GenericRecipe("chem.schrabidic").setupNamed(100, 5000L)
                .inputItems(new ComparableStack(NtmItems.PELLET_CHARGED.get()))
                .inputFluids(new FluidStack(Fluids.SAS3, 8000), new FluidStack(Fluids.PEROXIDE, 6000))
                .outputFluids(new FluidStack(Fluids.SCHRABIDIC, 16000)));
        this.register(new GenericRecipe("chem.schrabidate").setupNamed(150, 5000L)
                .inputItems(new ComparableStack(NtmItems.POWDER_IRON.get()))
                .inputFluids(new FluidStack(Fluids.SCHRABIDIC, 250))
                .outputItems(new ItemStack(NtmItems.POWDER_SCHRABIDATE.get())));
        this.register(new GenericRecipe("chem.coltancleaning").setupNamed(60, 100L)
                .inputItems(new ComparableStack(NtmItems.POWDER_COLTAN.get(), 2), new ComparableStack(NtmItems.POWDER_COAL.get()))
                .inputFluids(new FluidStack(Fluids.PEROXIDE, 250), new FluidStack(Fluids.HYDROGEN, 500))
                .outputItems(new ItemStack(NtmItems.POWDER_COLTAN.get()), new ItemStack(NtmItems.POWDER_NIOBIUM.get()), new ItemStack(NtmItems.DUST.get()))
                .outputFluids(new FluidStack(Fluids.WATER, 500)));
        this.register(new GenericRecipe("chem.coltanpain").setupNamed(120, 100L)
                .inputItems(new ComparableStack(NtmItems.POWDER_COLTAN.get()), new ComparableStack(NtmItems.FLUORITE.get()))
                .inputFluids(new FluidStack(Fluids.GAS, 1000), new FluidStack(Fluids.OXYGEN, 500))
                .outputFluids(new FluidStack(Fluids.PAIN, 1000)));
        this.register(new GenericRecipe("chem.coltancrystal").setupNamed(80, 100L)
                .inputFluids(new FluidStack(Fluids.PAIN, 1000), new FluidStack(Fluids.PEROXIDE, 500))
                .outputItems(new ItemStack(NtmItems.GEM_TANTALIUM.get()), new ItemStack(NtmItems.DUST.get(), 3))
                .outputFluids(new FluidStack(Fluids.WATER, 250)));
        this.register(new GenericRecipe("chem.cordite").setupNamed(40, 100L)
                .inputItems(new ComparableStack(NtmItems.NITER.get(), 2), new ComparableStack(NtmItems.POWDER_SAWDUST.get(), 2))
                .inputFluids(new FluidStack(Fluids.GAS, 200))
                .outputItems(new ItemStack(NtmItems.CORDITE.get(), 4)));
        this.register(new GenericRecipe("chem.rocketfuel").setupNamed(200, 100L)
                .inputItems(new ComparableStack(NtmItems.SOLID_FUEL.get(), 2))
                .inputFluids(new FluidStack(Fluids.PETROLEUM, 200), new FluidStack(Fluids.NITRIC_ACID, 100))
                .outputItems(new ItemStack(NtmItems.ROCKET_FUEL.get(), 4)));
        this.register(new GenericRecipe("chem.dynamite").setupNamed(50, 100L)
                .inputItems(new ComparableStack(Items.SUGAR), new ComparableStack(NtmItems.NITER.get()), new TagStack(ItemTags.SAND))
                .outputItems(new ItemStack(NtmItems.BALL_DYNAMITE.get(), 2)));
        this.register(new GenericRecipe("chem.tnt").setupNamed(100, 1000L)
                .inputItems(new ComparableStack(NtmItems.NITER.get()))
                .inputFluids(new FluidStack(Fluids.AROMATICS, 500))
                .outputItems(new ItemStack(NtmItems.BALL_TNT.get(), 4)));
        this.register(new GenericRecipe("chem.tatb").setupNamed(50, 5000L)
                .inputItems(new ComparableStack(NtmItems.BALL_TNT.get()))
                .inputFluids(new FluidStack(Fluids.SOURGAS, 200, 1), new FluidStack(Fluids.NITRIC_ACID, 10))
                .outputItems(new ItemStack(NtmItems.BALL_TATB.get())));
        this.register(new GenericRecipe("chem.c4").setupNamed(100, 1000L)
                .inputItems(new ComparableStack(NtmItems.NITER.get()))
                .inputFluids(new FluidStack(Fluids.UNSATURATEDS, 500))
                .outputItems(new ItemStack(NtmItems.INGOT_C4.get(), 4)));
        this.register(new GenericRecipe("chem.napalm").setupNamed(40, 100L)
                .inputItems(new ComparableStack(NtmItems.CANISTER_EMPTY.get()))
                .inputFluids(new FluidStack(Fluids.GASOLINE, 100), new FluidStack(Fluids.AROMATICS, 50))
                .outputItems(new ItemStack(NtmItems.CANISTER_NAPALM.get())));
        this.register(new GenericRecipe("chem.laminate").setupNamed(20, 100L)
                .inputFluids(new FluidStack(Fluids.XYLENE, 50), new FluidStack(Fluids.PHOSGENE, 50))
                .inputItems(new TagStack(ItemTags.create(net.minecraft.resources.ResourceLocation.withDefaultNamespace("glass_blocks"))), new ComparableStack(NtmItems.BOLT.get(), 4, BoltItem.Type.STEEL.meta))
                .outputItems(new ItemStack(NtmBlocks.REINFORCED_LAMINATE.get())));
        this.register(new GenericRecipe("chem.polarized").setupNamed(100, 500L)
                .inputFluids(new FluidStack(Fluids.PETROLEUM, 1000))
                .inputItems(new ComparableStack(Blocks.GLASS_PANE))
                .outputItems(MetaHelper.newStack(NtmItems.PART_GENERIC.get(), 16, PartGenericItem.Type.GLASS_POLARIZED.ordinal())));
        this.register(new GenericRecipe("chem.yellowcake").setupNamed(250, 500L)
                .inputItems(new ComparableStack(NtmItems.POWDER_URANIUM.get(), 2), new ComparableStack(NtmItems.SULFUR.get(), 2))
                .inputFluids(new FluidStack(Fluids.PEROXIDE, 500))
                .outputItems(new ItemStack(NtmItems.POWDER_YELLOWCAKE.get())));
        this.register(new GenericRecipe("chem.uf6").setupNamed(100, 500L)
                .setIcon(NtmItems.FLUID_ICON.get(), Fluids.UF6.getID())
                .inputItems(new ComparableStack(NtmItems.POWDER_YELLOWCAKE.get()), new ComparableStack(NtmItems.FLUORITE.get(), 4))
                .inputFluids(new FluidStack(Fluids.WATER, 1000))
                .outputItems(new ItemStack(NtmItems.SULFUR.get(), 2))
                .outputFluids(new FluidStack(Fluids.UF6, 1200)));
        this.register(new GenericRecipe("chem.puf6").setupNamed(200, 500L)
                .inputItems(new ComparableStack(NtmItems.POWDER_PLUTONIUM.get()), new ComparableStack(NtmItems.FLUORITE.get(), 3))
                .inputFluids(new FluidStack(Fluids.WATER, 1000))
                .outputFluids(new FluidStack(Fluids.PUF6, 900)));
        this.register(new GenericRecipe("chem.sas3").setupNamed(200, 5000L)
                .inputItems(new ComparableStack(NtmItems.POWDER_SCHRABIDIUM.get()), new ComparableStack(NtmItems.SULFUR.get(), 2))
                .inputFluids(new FluidStack(Fluids.PEROXIDE, 2000))
                .outputFluids(new FluidStack(Fluids.SAS3, 1000)));
        this.register(new GenericRecipe("chem.balefire").setupNamed(100, 10000L)
                .setIcon(NtmItems.FLUID_ICON.get(), Fluids.BALEFIRE.getID())
                .inputItems(new ComparableStack(NtmItems.EGG_BALEFIRE_SHARD.get()))
                .inputFluids(new FluidStack(Fluids.KEROSENE, 6000))
                .outputItems(new ItemStack(NtmItems.POWDER_BALEFIRE.get()))
                .outputFluids(new FluidStack(Fluids.BALEFIRE, 8000)));
        this.register(new GenericRecipe("chem.dhc").setupNamed(400, 500L)
                .setIcon(NtmItems.FLUID_ICON.get(), Fluids.DHC.getID())
                .inputFluids(new FluidStack(Fluids.DEUTERIUM, 500), new FluidStack(Fluids.REFORMGAS, 250), new FluidStack(Fluids.SYNGAS, 250))
                .outputFluids(new FluidStack(Fluids.DHC, 500)));
        this.register(new GenericRecipe("chem.osmiridiumdeath").setupNamed(240, 1000L)
                .inputItems(new ComparableStack(NtmItems.POWDER_PALEOGENITE.get()), new ComparableStack(NtmItems.FLUORITE.get(), 8), new ComparableStack(NtmItems.NUGGET_BISMUTH.get(), 4))
                .inputFluids(new FluidStack(Fluids.PEROXIDE, 1000, 5))
                .outputFluids(new FluidStack(Fluids.DEATH, 1000)));
    }
}
