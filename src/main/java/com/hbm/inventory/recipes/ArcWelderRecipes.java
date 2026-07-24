package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.blocks.NtmBlocks;
import com.hbm.items.CastPlateItem;
import com.hbm.items.NtmItems;
import com.hbm.items.PartGenericItem;
import com.hbm.items.WireDenseItem;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ArcWelderRecipes extends SerializableRecipe {

    public static final List<ArcWelderRecipe> recipes = new ArrayList<>();
    public static final HashSet<AStack> ingredients = new HashSet<>();

    @Override
    public String getFileName() {
        return "hbmArcWelder.json";
    }

    @Override
    public Object getRecipeObject() {
        return recipes;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        JsonObject json = recipe.getAsJsonObject();
        AStack[] inputs = readAStackArray(json.get("inputs").getAsJsonArray());
        FluidStack fluid = json.has("fluid") ? readFluidStack(json.get("fluid").getAsJsonArray()) : null;
        ItemStack output = readItemStack(json.get("output").getAsJsonArray());
        int duration = json.get("duration").getAsInt();
        long consumption = json.get("consumption").getAsLong();
        recipes.add(new ArcWelderRecipe(output, duration, consumption, fluid, inputs));
    }

    @Override
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        ArcWelderRecipe arcRecipe = (ArcWelderRecipe) recipe;
        writer.name("inputs").beginArray();
        for(AStack input : arcRecipe.ingredients) {
            writeAStack(input, writer);
        }
        writer.endArray();
        if(arcRecipe.fluid != null) {
            writer.name("fluid");
            writeFluidStack(arcRecipe.fluid, writer);
        }
        writer.name("output");
        writeItemStack(arcRecipe.output, writer);
        writer.name("duration").value(arcRecipe.duration);
        writer.name("consumption").value(arcRecipe.consumption);
    }

    @Override
    public void registerDefaults() {
        addRecipe(new ItemStack(NtmItems.MOTOR.get(), 2), 100, 400, null,
                new ComparableStack(NtmItems.PLATE_STEEL.get(), 2),
                new ComparableStack(MetaHelper.newStack(NtmItems.WIRE_DENSE.get(), 2, WireDenseItem.Type.RED_COPPER.meta)));

        addRecipe(MetaHelper.newStack(NtmItems.PART_GENERIC.get(), 1, PartGenericItem.Type.LDE), 200, 5_000, null,
                new ComparableStack(NtmItems.PLATE_ALUMINIUM.get(), 4),
                new ComparableStack(NtmItems.INGOT_FIBERGLASS.get(), 4),
                new ComparableStack(NtmItems.INGOT_PC.get(), 1));
        addRecipe(MetaHelper.newStack(NtmItems.PART_GENERIC.get(), 1, PartGenericItem.Type.LDE), 200, 10_000, null,
                new ComparableStack(NtmItems.PLATE_TITANIUM.get(), 2),
                new ComparableStack(NtmItems.INGOT_FIBERGLASS.get(), 4),
                new ComparableStack(NtmItems.INGOT_PC.get(), 1));
        addRecipe(MetaHelper.newStack(NtmItems.PART_GENERIC.get(), 1, PartGenericItem.Type.HDE), 600, 25_000_000, new FluidStack(Fluids.STELLAR_FLUX, 4_000),
                new ComparableStack(NtmItems.CAST_PLATE.get(), 2, CastPlateItem.Type.BISMUTH_BRONZE.ordinal()),
                new ComparableStack(NtmItems.CAST_PLATE_WELDED.get(), 1, CastPlateItem.Type.COMBINE_STEEL.ordinal()),
                new ComparableStack(NtmItems.INGOT_CTF.get(), 1));

        addRecipe(MetaHelper.newStack(NtmItems.WIRE_DENSE.get(), 1, WireDenseItem.Type.COPPER.meta), 100, 10_000, null,
                new ComparableStack(NtmItems.WIRE_COPPER.get(), 8));
        addRecipe(MetaHelper.newStack(NtmItems.WIRE_DENSE.get(), 1, WireDenseItem.Type.RED_COPPER.meta), 100, 10_000, null,
                new ComparableStack(NtmItems.WIRE_RED_COPPER.get(), 8));
        addRecipe(MetaHelper.newStack(NtmItems.WIRE_DENSE.get(), 1, WireDenseItem.Type.GOLD.meta), 100, 10_000, null,
                new ComparableStack(NtmItems.WIRE_GOLD.get(), 8));

        addRecipe(MetaHelper.newStack(NtmItems.CAST_PLATE_WELDED.get(), 1, CastPlateItem.Type.IRON), 100, 100, null, NtmItems.castPlateIngredient(CastPlateItem.Type.IRON, 2));
        addRecipe(MetaHelper.newStack(NtmItems.CAST_PLATE_WELDED.get(), 1, CastPlateItem.Type.STEEL), 100, 500, null, NtmItems.castPlateIngredient(CastPlateItem.Type.STEEL, 2));
        addRecipe(MetaHelper.newStack(NtmItems.CAST_PLATE_WELDED.get(), 1, CastPlateItem.Type.COPPER), 200, 1_000, null, NtmItems.castPlateIngredient(CastPlateItem.Type.COPPER, 2));
        addRecipe(MetaHelper.newStack(NtmItems.CAST_PLATE_WELDED.get(), 1, CastPlateItem.Type.TITANIUM), 600, 50_000, null, NtmItems.castPlateIngredient(CastPlateItem.Type.TITANIUM, 2));
        addRecipe(MetaHelper.newStack(NtmItems.CAST_PLATE_WELDED.get(), 1, CastPlateItem.Type.ZIRCONIUM), 600, 10_000, null, NtmItems.castPlateIngredient(CastPlateItem.Type.ZIRCONIUM, 2));
        addRecipe(MetaHelper.newStack(NtmItems.CAST_PLATE_WELDED.get(), 1, CastPlateItem.Type.ALUMINIUM), 300, 10_000, null, NtmItems.castPlateIngredient(CastPlateItem.Type.ALUMINIUM, 2));
        addRecipe(MetaHelper.newStack(NtmItems.CAST_PLATE_WELDED.get(), 1, CastPlateItem.Type.TCALLOY), 1_200, 1_000_000, new FluidStack(Fluids.OXYGEN, 1_000), NtmItems.castPlateIngredient(CastPlateItem.Type.TCALLOY, 2));
        addRecipe(MetaHelper.newStack(NtmItems.CAST_PLATE_WELDED.get(), 1, CastPlateItem.Type.CDALLOY), 1_200, 1_000_000, new FluidStack(Fluids.OXYGEN, 1_000), NtmItems.castPlateIngredient(CastPlateItem.Type.CDALLOY, 2));
        addRecipe(MetaHelper.newStack(NtmItems.CAST_PLATE_WELDED.get(), 1, CastPlateItem.Type.TUNGSTEN), 1_200, 250_000, new FluidStack(Fluids.OXYGEN, 1_000), NtmItems.castPlateIngredient(CastPlateItem.Type.TUNGSTEN, 2));
        addRecipe(MetaHelper.newStack(NtmItems.CAST_PLATE_WELDED.get(), 1, CastPlateItem.Type.COMBINE_STEEL), 1_200, 10_000_000, new FluidStack(Fluids.REFORMGAS, 1_000), NtmItems.castPlateIngredient(CastPlateItem.Type.COMBINE_STEEL, 2));
        addRecipe(MetaHelper.newStack(NtmItems.CAST_PLATE_WELDED.get(), 1, CastPlateItem.Type.OSMIRIDIUM), 6_000, 20_000_000, new FluidStack(Fluids.REFORMGAS, 16_000), NtmItems.castPlateIngredient(CastPlateItem.Type.OSMIRIDIUM, 2));

        addRecipe(new ItemStack(NtmItems.THRUSTER_SMALL.get()), 60, 1_000, null,
                new ComparableStack(NtmItems.PLATE_STEEL.get(), 4),
                new ComparableStack(NtmItems.WIRE_ALUMINIUM.get(), 4),
                new ComparableStack(NtmItems.PLATE_COPPER.get(), 4));
        addRecipe(new ItemStack(NtmItems.THRUSTER_MEDIUM.get()), 100, 2_000, null,
                new ComparableStack(NtmItems.PLATE_STEEL.get(), 8),
                new ComparableStack(NtmItems.MOTOR.get(), 1),
                new ComparableStack(NtmItems.INGOT_GRAPHITE.get(), 8));
        addRecipe(new ItemStack(NtmItems.THRUSTER_LARGE.get()), 200, 5_000, null,
                new ComparableStack(NtmItems.INGOT_DURA_STEEL.get(), 10),
                new ComparableStack(NtmItems.MOTOR.get(), 1),
                new ComparableStack(NtmItems.NEUTRON_REFLECTOR.get(), 12));

        addRecipe(new ItemStack(NtmItems.FUEL_TANK_SMALL.get()), 60, 1_000, null,
                new ComparableStack(NtmItems.PLATE_ALUMINIUM.get(), 6),
                new ComparableStack(NtmItems.PLATE_COPPER.get(), 4),
                new ComparableStack(NtmBlocks.STEEL_SCAFFOLD.get(), 4));
        addRecipe(new ItemStack(NtmItems.FUEL_TANK_MEDIUM.get()), 100, 2_000, null,
                NtmItems.castPlateIngredient(CastPlateItem.Type.ALUMINIUM, 4),
                new ComparableStack(NtmItems.PLATE_TITANIUM.get(), 8),
                new ComparableStack(NtmBlocks.STEEL_SCAFFOLD.get(), 12));
        addRecipe(new ItemStack(NtmItems.FUEL_TANK_LARGE.get()), 200, 5_000, null,
                NtmItems.castPlateWeldedIngredient(CastPlateItem.Type.ALUMINIUM, 8),
                new ComparableStack(NtmItems.INGOT_STARMETAL.get(), 12),
                new ComparableStack(NtmBlocks.STEEL_SCAFFOLD.get(), 16));

        addRecipe(new ItemStack(NtmItems.MISSILE_ANTI_BALLISTIC.get()), 100, 5_000, null,
                new ComparableStack(NtmItems.SOLID_FUEL.get(), 3),
                new ComparableStack(NtmItems.MISSILE_ASSEMBLY.get(), 1),
                new ComparableStack(NtmItems.THRUSTER_SMALL.get(), 4));

        addRecipe(new ItemStack(NtmItems.MISSILE_GENERIC.get()), 100, 5_000, null,
                new ComparableStack(NtmItems.WARHEAD_GENERIC_SMALL.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_SMALL.get(), 1),
                new ComparableStack(NtmItems.THRUSTER_SMALL.get(), 1));
        addRecipe(new ItemStack(NtmItems.MISSILE_INCENDIARY.get()), 100, 5_000, null,
                new ComparableStack(NtmItems.WARHEAD_INCENDIARY_SMALL.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_SMALL.get(), 1),
                new ComparableStack(NtmItems.THRUSTER_SMALL.get(), 1));
        addRecipe(new ItemStack(NtmItems.MISSILE_CLUSTER.get()), 100, 5_000, null,
                new ComparableStack(NtmItems.WARHEAD_CLUSTER_SMALL.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_SMALL.get(), 1),
                new ComparableStack(NtmItems.THRUSTER_SMALL.get(), 1));
        addRecipe(new ItemStack(NtmItems.MISSILE_BUSTER.get()), 100, 5_000, null,
                new ComparableStack(NtmItems.WARHEAD_BUSTER_SMALL.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_SMALL.get(), 1),
                new ComparableStack(NtmItems.THRUSTER_SMALL.get(), 1));
        addRecipe(new ItemStack(NtmItems.MISSILE_DECOY.get()), 60, 2_500, null,
                new ComparableStack(NtmItems.INGOT_STEEL.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_SMALL.get(), 1),
                new ComparableStack(NtmItems.THRUSTER_SMALL.get(), 1));

        addRecipe(new ItemStack(NtmItems.MISSILE_STRONG.get()), 200, 10_000, null,
                new ComparableStack(NtmItems.WARHEAD_GENERIC_MEDIUM.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_MEDIUM.get(), 1),
                new ComparableStack(NtmItems.THRUSTER_MEDIUM.get(), 1));
        addRecipe(new ItemStack(NtmItems.MISSILE_INCENDIARY_STRONG.get()), 200, 10_000, null,
                new ComparableStack(NtmItems.WARHEAD_INCENDIARY_MEDIUM.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_MEDIUM.get(), 1),
                new ComparableStack(NtmItems.THRUSTER_MEDIUM.get(), 1));
        addRecipe(new ItemStack(NtmItems.MISSILE_CLUSTER_STRONG.get()), 200, 10_000, null,
                new ComparableStack(NtmItems.WARHEAD_CLUSTER_MEDIUM.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_MEDIUM.get(), 1),
                new ComparableStack(NtmItems.THRUSTER_MEDIUM.get(), 1));
        addRecipe(new ItemStack(NtmItems.MISSILE_BUSTER_STRONG.get()), 200, 10_000, null,
                new ComparableStack(NtmItems.WARHEAD_BUSTER_MEDIUM.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_MEDIUM.get(), 1),
                new ComparableStack(NtmItems.THRUSTER_MEDIUM.get(), 1));
        addRecipe(new ItemStack(NtmItems.MISSILE_EMP_STRONG.get()), 200, 10_000, null,
                new ComparableStack(NtmBlocks.EMP_BOMB.get(), 3),
                new ComparableStack(NtmItems.FUEL_TANK_MEDIUM.get(), 1),
                new ComparableStack(NtmItems.THRUSTER_MEDIUM.get(), 1));

        addRecipe(new ItemStack(NtmItems.MISSILE_BURST.get()), 300, 25_000, null,
                new ComparableStack(NtmItems.WARHEAD_GENERIC_LARGE.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_MEDIUM.get(), 2),
                new ComparableStack(NtmItems.THRUSTER_MEDIUM.get(), 4));
        addRecipe(new ItemStack(NtmItems.MISSILE_INFERNO.get()), 300, 25_000, null,
                new ComparableStack(NtmItems.WARHEAD_INCENDIARY_LARGE.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_MEDIUM.get(), 2),
                new ComparableStack(NtmItems.THRUSTER_MEDIUM.get(), 4));
        addRecipe(new ItemStack(NtmItems.MISSILE_RAIN.get()), 300, 25_000, null,
                new ComparableStack(NtmItems.WARHEAD_CLUSTER_LARGE.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_MEDIUM.get(), 2),
                new ComparableStack(NtmItems.THRUSTER_MEDIUM.get(), 4));
        addRecipe(new ItemStack(NtmItems.MISSILE_DRILL.get()), 300, 25_000, null,
                new ComparableStack(NtmItems.WARHEAD_BUSTER_LARGE.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_MEDIUM.get(), 2),
                new ComparableStack(NtmItems.THRUSTER_MEDIUM.get(), 4));

        addRecipe(new ItemStack(NtmItems.MISSILE_NUCLEAR.get()), 600, 50_000, null,
                new ComparableStack(NtmItems.WARHEAD_NUCLEAR.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_LARGE.get(), 1),
                new ComparableStack(NtmItems.THRUSTER_LARGE.get(), 3));
        addRecipe(new ItemStack(NtmItems.MISSILE_NUCLEAR_CLUSTER.get()), 600, 50_000, null,
                new ComparableStack(NtmItems.WARHEAD_MIRV.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_LARGE.get(), 1),
                new ComparableStack(NtmItems.THRUSTER_LARGE.get(), 3));
        addRecipe(new ItemStack(NtmItems.MISSILE_VOLCANO.get()), 600, 50_000, null,
                new ComparableStack(NtmItems.WARHEAD_VOLCANO.get(), 1),
                new ComparableStack(NtmItems.FUEL_TANK_LARGE.get(), 1),
                new ComparableStack(NtmItems.THRUSTER_LARGE.get(), 3));

        addRecipe(new ItemStack(NtmItems.SAT_MAPPER.get()), 600, 10_000, null,
                new ComparableStack(NtmItems.SAT_BASE.get(), 1),
                new ComparableStack(NtmItems.SAT_HEAD_MAPPER.get(), 1));
        addRecipe(new ItemStack(NtmItems.SAT_SCANNER.get()), 600, 10_000, null,
                new ComparableStack(NtmItems.SAT_BASE.get(), 1),
                new ComparableStack(NtmItems.SAT_HEAD_SCANNER.get(), 1));
        addRecipe(new ItemStack(NtmItems.SAT_RADAR.get()), 600, 10_000, null,
                new ComparableStack(NtmItems.SAT_BASE.get(), 1),
                new ComparableStack(NtmItems.SAT_HEAD_RADAR.get(), 1));
        addRecipe(new ItemStack(NtmItems.SAT_LASER.get()), 600, 50_000, null,
                new ComparableStack(NtmItems.SAT_BASE.get(), 1),
                new ComparableStack(NtmItems.SAT_HEAD_LASER.get(), 1));
        addRecipe(new ItemStack(NtmItems.SAT_RESONATOR.get()), 600, 50_000, null,
                new ComparableStack(NtmItems.SAT_BASE.get(), 1),
                new ComparableStack(NtmItems.SAT_HEAD_RESONATOR.get(), 1));
    }

    @Override
    public void deleteRecipes() {
        recipes.clear();
        ingredients.clear();
    }

    public static void addRecipe(ItemStack output, int duration, long consumption, FluidStack fluid, AStack... ingredientsIn) {
        ArcWelderRecipe recipe = new ArcWelderRecipe(output, duration, consumption, fluid, ingredientsIn);
        recipes.add(recipe);
        Collections.addAll(ingredients, ingredientsIn);
    }

    public static ArcWelderRecipe getRecipe(ItemStack... stacks) {
        for(ArcWelderRecipe recipe : recipes) {
            if(matchesIngredients(stacks, recipe.ingredients)) return recipe;
        }
        return null;
    }

    public static boolean matchesIngredients(ItemStack[] slots, AStack[] ingredientsIn) {
        boolean[] used = new boolean[slots.length];
        for(AStack ingredient : ingredientsIn) {
            boolean matched = false;
            for(int i = 0; i < slots.length; i++) {
                if(used[i]) continue;
                if(ingredient.matchesRecipe(slots[i], true) && slots[i].getCount() >= ingredient.stacksize) {
                    used[i] = true;
                    matched = true;
                    break;
                }
            }
            if(!matched) return false;
        }
        return true;
    }

    public static class ArcWelderRecipe {
        public final ItemStack output;
        public final int duration;
        public final long consumption;
        public final FluidStack fluid;
        public final AStack[] ingredients;

        public ArcWelderRecipe(ItemStack output, int duration, long consumption, FluidStack fluid, AStack[] ingredients) {
            this.output = output;
            this.duration = duration;
            this.consumption = consumption;
            this.fluid = fluid;
            this.ingredients = ingredients;
        }
    }
}
