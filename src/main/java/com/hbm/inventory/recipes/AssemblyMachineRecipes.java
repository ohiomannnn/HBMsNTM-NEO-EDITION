package com.hbm.inventory.recipes;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.CastPlateItem;
import com.hbm.items.NtmItems;
import com.hbm.items.PartGenericItem;
import com.hbm.items.machine.BatteryPackItem.BatteryPackType;
import com.hbm.items.machine.FluidIconItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
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

        this.register(new GenericRecipe("ass.missileassembly").setup(200, 100).outputItems(new ItemStack(NtmItems.MISSILE_ASSEMBLY.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.SHELL_ALUMINIUM.get(), 2),
                        new ComparableStack(NtmItems.SHELL_TITANIUM.get(), 4),
                        new RecipesCommon.TagStack(ItemTags.create(ResourceLocation.fromNamespaceAndPath("ntm", "bars_hard_plastic")), 8),
                        new ComparableStack(NtmItems.ROCKET_FUEL.get(), 8),
                        new ComparableStack(NtmItems.CIRCUIT_PRINTED_BOARD.get(), 1)
                )
        );

        this.register(new GenericRecipe("ass.warheadhe1").setup(100, 100).outputItems(new ItemStack(NtmItems.WARHEAD_GENERIC_SMALL.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.PLATE_TITANIUM.get(), 4),
                        new ComparableStack(NtmItems.BALL_DYNAMITE.get(), 2),
                        new ComparableStack(NtmItems.CIRCUIT_MICROCHIP.get(), 1)
                )
        );

        this.register(new GenericRecipe("ass.warheadhe2").setup(200, 100).outputItems(new ItemStack(NtmItems.WARHEAD_GENERIC_MEDIUM.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.PLATE_TITANIUM.get(), 8),
                        new RecipesCommon.TagStack(ItemTags.create(ResourceLocation.fromNamespaceAndPath("ntm", "balls_he")), 4),
                        new ComparableStack(NtmItems.CIRCUIT_PRINTED_BOARD.get(), 1)
                )
        );

        this.register(new GenericRecipe("ass.warheadhe3").setup(400, 100).outputItems(new ItemStack(NtmItems.WARHEAD_GENERIC_LARGE.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.PLATE_TITANIUM.get(), 16),
                        new RecipesCommon.TagStack(ItemTags.create(ResourceLocation.fromNamespaceAndPath("ntm", "balls_he")), 8),
                        new ComparableStack(NtmItems.CIRCUIT_INTEGRATED_BOARD.get(), 1)
                )
        );

        this.register(new GenericRecipe("ass.warheadinc1").setup(100, 100).outputItems(new ItemStack(NtmItems.WARHEAD_INCENDIARY_SMALL.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.WARHEAD_GENERIC_SMALL.get(), 1),
                        new ComparableStack(NtmItems.POWDER_FIRE.get(), 2)
                )
        );

        this.register(new GenericRecipe("ass.warheadinc2").setup(200, 100).outputItems(new ItemStack(NtmItems.WARHEAD_INCENDIARY_MEDIUM.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.WARHEAD_GENERIC_MEDIUM.get(), 1),
                        new ComparableStack(NtmItems.POWDER_FIRE.get(), 4)
                )
        );

        this.register(new GenericRecipe("ass.warheadinc3").setup(400, 100).outputItems(new ItemStack(NtmItems.WARHEAD_INCENDIARY_LARGE.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.WARHEAD_GENERIC_LARGE.get(), 1),
                        new ComparableStack(NtmItems.POWDER_FIRE.get(), 8)
                )
        );

        this.register(new GenericRecipe("ass.warheadcl1").setup(100, 100).outputItems(new ItemStack(NtmItems.WARHEAD_CLUSTER_SMALL.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.WARHEAD_GENERIC_SMALL.get(), 1),
                        new ComparableStack(NtmItems.PELLET_CLUSTER.get(), 2)
                )
        );

        this.register(new GenericRecipe("ass.warheadcl2").setup(200, 100).outputItems(new ItemStack(NtmItems.WARHEAD_CLUSTER_MEDIUM.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.WARHEAD_GENERIC_MEDIUM.get(), 1),
                        new ComparableStack(NtmItems.PELLET_CLUSTER.get(), 4)
                )
        );

        this.register(new GenericRecipe("ass.warheadcl3").setup(400, 100).outputItems(new ItemStack(NtmItems.WARHEAD_CLUSTER_LARGE.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.WARHEAD_GENERIC_LARGE.get(), 1),
                        new ComparableStack(NtmItems.PELLET_CLUSTER.get(), 8)
                )
        );

        this.register(new GenericRecipe("ass.warheadbb1").setup(100, 100).outputItems(new ItemStack(NtmItems.WARHEAD_BUSTER_SMALL.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.WARHEAD_GENERIC_SMALL.get(), 1),
                        new RecipesCommon.TagStack(ItemTags.create(ResourceLocation.fromNamespaceAndPath("ntm", "balls_he")), 2)
                )
        );

        this.register(new GenericRecipe("ass.warheadbb2").setup(200, 100).outputItems(new ItemStack(NtmItems.WARHEAD_BUSTER_MEDIUM.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.WARHEAD_GENERIC_MEDIUM.get(), 1),
                        new RecipesCommon.TagStack(ItemTags.create(ResourceLocation.fromNamespaceAndPath("ntm", "balls_he")), 4)
                )
        );

        this.register(new GenericRecipe("ass.warheadbb3").setup(400, 100).outputItems(new ItemStack(NtmItems.WARHEAD_BUSTER_LARGE.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.WARHEAD_GENERIC_LARGE.get(), 1),
                        new RecipesCommon.TagStack(ItemTags.create(ResourceLocation.fromNamespaceAndPath("ntm", "balls_he")), 8)
                )
        );

        this.register(new GenericRecipe("ass.warheadnuke").setup(400, 100).outputItems(new ItemStack(NtmItems.WARHEAD_NUCLEAR.get(), 1))
                .inputItems(
                        NtmItems.castPlateIngredient(CastPlateItem.Type.TITANIUM, 12),
                        NtmItems.castPlateIngredient(CastPlateItem.Type.LEAD, 6),
                        new ComparableStack(NtmItems.BILLET_U235.get(), 6),
                        new ComparableStack(NtmItems.CORDITE.get(), 12),
                        new ComparableStack(NtmItems.CIRCUIT_CONTROL_UNIT.get(), 1)
                )
        );

        this.register(new GenericRecipe("ass.warheadthermonuke").setup(600, 100).outputItems(new ItemStack(NtmItems.WARHEAD_MIRV.get(), 1))
                .inputItems(
                        NtmItems.castPlateIngredient(CastPlateItem.Type.TITANIUM, 12),
                        NtmItems.castPlateIngredient(CastPlateItem.Type.LEAD, 6),
                        new ComparableStack(NtmItems.BILLET_PU239.get(), 8),
                        new ComparableStack(NtmItems.BALL_TATB.get(), 12),
                        new ComparableStack(NtmItems.CIRCUIT_ADVANCED_CONTROL_UNIT.get(), 2)
                ).inputFluids(new FluidStack(Fluids.DEUTERIUM, 4_000))
        );

        this.register(new GenericRecipe("ass.warheadvolcano").setup(600, 100).outputItems(new ItemStack(NtmItems.WARHEAD_VOLCANO.get(), 1))
                .inputItems(
                        NtmItems.castPlateIngredient(CastPlateItem.Type.TITANIUM, 12),
                        NtmItems.castPlateIngredient(CastPlateItem.Type.STEEL, 6),
                        new ComparableStack(NtmBlocks.DET_NUKE.get(), 3),
                        new ComparableStack(NtmBlocks.BLOCK_U238.get(), 24),
                        new ComparableStack(NtmItems.CIRCUIT_CAPACITOR_BOARD.get(), 5)
                )
        );

        this.register(new GenericRecipe("ass.satellitebase").setup(600, 100).outputItems(new ItemStack(NtmItems.SAT_BASE.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.INGOT_RUBBER.get(), 12),
                        new ComparableStack(NtmItems.SHELL_TITANIUM.get(), 3),
                        new ComparableStack(NtmItems.THRUSTER_MEDIUM.get(), 1),
                        new ComparableStack(MetaHelper.newStack(NtmItems.PART_GENERIC.get(), 8, PartGenericItem.Type.LDE.ordinal())),
                        new ComparableStack(NtmItems.PLATE_DESH.get(), 4),
                        new ComparableStack(MetaHelper.newStack(NtmItems.FLUID_BARREL_FULL.get(), 1, Fluids.KEROSENE.getID())),
                        new ComparableStack(NtmItems.PHOTO_PANEL.get(), 24),
                        new ComparableStack(NtmItems.CIRCUIT_PRINTED_BOARD.get(), 12),
                        new ComparableStack(MetaHelper.newStack(NtmItems.BATTERY_PACK.get(), 1, BatteryPackType.BATTERY_LITHIUM.ordinal()))
                )
        );

        this.register(new GenericRecipe("ass.satellitemapper").setup(600, 100).outputItems(new ItemStack(NtmItems.SAT_HEAD_MAPPER.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.SHELL_STEEL.get(), 3),
                        new ComparableStack(NtmItems.PLATE_DESH.get(), 4),
                        new ComparableStack(NtmItems.CIRCUIT_MILITARY_GRADE_BOARD.get(), 4),
                        new ComparableStack(NtmBlocks.GLASS_QUARTZ.get(), 8)
                )
        );

        this.register(new GenericRecipe("ass.satellitescanner").setup(600, 100).outputItems(new ItemStack(NtmItems.SAT_HEAD_SCANNER.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.SHELL_STEEL.get(), 3),
                        NtmItems.castPlateIngredient(CastPlateItem.Type.TITANIUM, 8),
                        new ComparableStack(NtmItems.PLATE_DESH.get(), 4),
                        new ComparableStack(NtmItems.MAGNETRON.get(), 8),
                        new ComparableStack(NtmItems.CIRCUIT_MILITARY_GRADE_BOARD.get(), 8)
                )
        );

        this.register(new GenericRecipe("ass.satelliteradar").setup(600, 100).outputItems(new ItemStack(NtmItems.SAT_HEAD_RADAR.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.SHELL_STEEL.get(), 3),
                        NtmItems.castPlateIngredient(CastPlateItem.Type.TITANIUM, 12),
                        new ComparableStack(NtmItems.MAGNETRON.get(), 12),
                        new ComparableStack(NtmItems.COIL_GOLD.get(), 16),
                        new ComparableStack(NtmItems.CIRCUIT_MILITARY_GRADE_BOARD.get(), 4)
                )
        );

        this.register(new GenericRecipe("ass.satellitelaser").setup(600, 100).outputItems(new ItemStack(NtmItems.SAT_HEAD_LASER.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.SHELL_STEEL.get(), 6),
                        NtmItems.castPlateIngredient(CastPlateItem.Type.COPPER, 24),
                        new RecipesCommon.TagStack(ItemTags.create(ResourceLocation.fromNamespaceAndPath("ntm", "bars_hard_plastic")), 16),
                        new ComparableStack(NtmItems.CIRCUIT_ADVANCED_CONTROL_UNIT.get(), 8),
                        new ComparableStack(NtmItems.CIRCUIT_CAPACITOR_BOARD.get(), 16),
                        new ComparableStack(NtmItems.CRYSTAL_DIAMOND.get(), 8),
                        new ComparableStack(NtmItems.MAGNETRON.get(), 16)
                )
        );

        this.register(new GenericRecipe("ass.satelliteresonator").setup(600, 100).outputItems(new ItemStack(NtmItems.SAT_HEAD_RESONATOR.get(), 1))
                .inputItems(
                        NtmItems.castPlateIngredient(CastPlateItem.Type.STEEL, 6),
                        new ComparableStack(NtmItems.INGOT_STARMETAL.get(), 12),
                        new RecipesCommon.TagStack(ItemTags.create(ResourceLocation.fromNamespaceAndPath("ntm", "bars_hard_plastic")), 48),
                        new ComparableStack(NtmItems.CRYSTAL_XEN.get(), 1),
                        new ComparableStack(NtmItems.CIRCUIT_MILITARY_GRADE_BOARD.get(), 16)
                )
        );

        this.register(new GenericRecipe("ass.plate_desh").setup(200, 100).outputItems(new ItemStack(NtmItems.PLATE_DESH.get(), 4))
                .inputItems(
                        new ComparableStack(NtmItems.INGOT_DESH.get(), 4),
                        new ComparableStack(NtmItems.INGOT_DURA_STEEL.get(), 1),
                        new RecipesCommon.TagStack(ItemTags.create(ResourceLocation.fromNamespaceAndPath("ntm", "powders_plastic")), 2)

                )
        );

        this.register(new GenericRecipe("ass.plate_bismuth").setup(200, 100).outputItems(new ItemStack(NtmItems.PLATE_BISMUTH.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.NUGGET_BISMUTH.get(), 2),
                        new ComparableStack(NtmItems.BILLET_U238.get(), 2),
                        new ComparableStack(NtmItems.POWDER_NIOBIUM.get(), 1)

                )
        );

        this.register(new GenericRecipe("ass.magnetron").setup(40, 100).outputItems(new ItemStack(NtmItems.MAGNETRON.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.WIRE_TUNGSTEN.get(), 4),
                        new ComparableStack(NtmItems.POWDER_NIOBIUM.get(), 3)

                )
        );

        this.register(new GenericRecipe("ass.pellet_cluster").setup(40, 100).outputItems(new ItemStack(NtmItems.PELLET_CLUSTER.get(), 1))
                .inputItems(
                        new ComparableStack(NtmItems.PLATE_STEEL.get(), 4),
                        new RecipesCommon.TagStack(ItemTags.create(ResourceLocation.fromNamespaceAndPath("ntm", "balls_he")), 1)

                )
        );

        this.register(new GenericRecipe("ass.plate_dalek").setup(40, 100).outputItems(new ItemStack(NtmItems.PLATE_DALEKANIUM.get(), 1))
                .inputItems(
                        new ComparableStack(NtmBlocks.BLOCK_METEOR.get(), 1)

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
