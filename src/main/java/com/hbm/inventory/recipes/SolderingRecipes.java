package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.NtmItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class SolderingRecipes extends SerializableRecipe {

    public static final List<SolderingRecipe> recipes = new ArrayList<>();
    public static final HashSet<AStack> toppings = new HashSet<>();
    public static final HashSet<AStack> pcb = new HashSet<>();
    public static final HashSet<AStack> solder = new HashSet<>();

    @Override
    public void registerDefaults() {
        addRecipe(
                out(NtmItems.CIRCUIT_ANALOG_BOARD),
                100,
                100,
                null,
                ingredients(
                        stack(NtmItems.CIRCUIT_VACUUM_TUBE, 3),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 2)
                ),
                ingredients(
                        stack(NtmItems.CIRCUIT_PRINTED_BOARD, 4)
                ),
                ingredients(
                        stack(NtmItems.WIRE_LEAD, 4)
                )
        );

        addRecipe(
                out(NtmItems.CIRCUIT_INTEGRATED_BOARD),
                200,
                250,
                null,
                ingredients(
                        stack(NtmItems.CIRCUIT_MICROCHIP, 4)
                ),
                ingredients(
                        stack(NtmItems.CIRCUIT_PRINTED_BOARD, 4)
                ),
                ingredients(
                        stack(NtmItems.WIRE_LEAD, 4)
                )
        );

        addRecipe(
                out(NtmItems.CIRCUIT_MILITARY_GRADE_BOARD),
                300,
                1_000,
                new FluidStack(Fluids.SULFURIC_ACID, 1_000),
                ingredients(
                        stack(NtmItems.CIRCUIT_MICROCHIP, 16),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 4)
                ),
                ingredients(
                        stack(NtmItems.CIRCUIT_PRINTED_BOARD, 8),
                        stack(NtmItems.INGOT_PC, 2)
                ),
                ingredients(
                        stack(NtmItems.WIRE_LEAD, 8)
                )
        );

        addRecipe(
                out(NtmItems.CIRCUIT_MILITARY_GRADE_BOARD),
                300,
                1_000,
                new FluidStack(Fluids.SULFURIC_ACID, 1_000),
                ingredients(
                        stack(NtmItems.CIRCUIT_MICROCHIP, 16),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 4)
                ),
                ingredients(
                        stack(NtmItems.CIRCUIT_PRINTED_BOARD, 8),
                        stack(NtmItems.INGOT_PVC, 2)
                ),
                ingredients(
                        stack(NtmItems.WIRE_LEAD, 8)
                )
        );

        addRecipe(
                out(NtmItems.CIRCUIT_CAPACITOR_BOARD),
                200,
                300,
                new FluidStack(Fluids.PEROXIDE, 250),
                ingredients(
                        stack(NtmItems.CIRCUIT_TANTALIUM_CAPACITOR, 3)
                ),
                ingredients(
                        stack(NtmItems.CIRCUIT_PRINTED_BOARD, 1)
                ),
                ingredients(
                        stack(NtmItems.WIRE_LEAD, 3)
                )
        );

        addRecipe(
                out(NtmItems.CIRCUIT_VERSATILE_BOARD),
                400,
                10_000,
                new FluidStack(Fluids.SOLVENT, 1_000),
                ingredients(
                        stack(NtmItems.CIRCUIT_VERSATILE_INTEGRATED, 4),
                        stack(NtmItems.CIRCUIT_MICROCHIP, 16),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 24)
                ),
                ingredients(
                        stack(NtmItems.CIRCUIT_PRINTED_BOARD, 12),
                        stack(NtmItems.INGOT_POLYMER, 2)
                ),
                ingredients(
                        stack(NtmItems.WIRE_LEAD, 12)
                )
        );

        addRecipe(
                out(NtmItems.CIRCUIT_VERSATILE_BOARD),
                400,
                10_000,
                new FluidStack(Fluids.SOLVENT, 1_000),
                ingredients(
                        stack(NtmItems.CIRCUIT_VERSATILE_INTEGRATED, 4),
                        stack(NtmItems.CIRCUIT_MICROCHIP, 16),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 24)
                ),
                ingredients(
                        stack(NtmItems.CIRCUIT_PRINTED_BOARD, 12),
                        stack(NtmItems.INGOT_BAKELITE, 2)
                ),
                ingredients(
                        stack(NtmItems.WIRE_LEAD, 12)
                )
        );

        addRecipe(
                out(NtmItems.CIRCUIT_QUANTUM_PROCESSING_UNIT),
                400,
                100_000,
                new FluidStack(Fluids.HELIUM_4, 1_000),
                ingredients(
                        stack(NtmItems.CIRCUIT_SOLID_STATE_QUANTUM_PROCESSOR, 4),
                        stack(NtmItems.CIRCUIT_VERSATILE_INTEGRATED, 16),
                        stack(NtmItems.CIRCUIT_ATOMIC_CLOCK, 4)
                ),
                ingredients(
                        stack(NtmItems.CIRCUIT_PRINTED_BOARD, 16),
                        stack(NtmItems.INGOT_PC, 4)
                ),
                ingredients(
                        stack(NtmItems.WIRE_LEAD, 16)
                )
        );

        addRecipe(
                out(NtmItems.CIRCUIT_QUANTUM_PROCESSING_UNIT),
                400,
                100_000,
                new FluidStack(Fluids.HELIUM_4, 1_000),
                ingredients(
                        stack(NtmItems.CIRCUIT_SOLID_STATE_QUANTUM_PROCESSOR, 4),
                        stack(NtmItems.CIRCUIT_VERSATILE_INTEGRATED, 16),
                        stack(NtmItems.CIRCUIT_ATOMIC_CLOCK, 4)
                ),
                ingredients(
                        stack(NtmItems.CIRCUIT_PRINTED_BOARD, 16),
                        stack(NtmItems.INGOT_PVC, 4)
                ),
                ingredients(
                        stack(NtmItems.WIRE_LEAD, 16)
                )
        );

        addRecipe(
                out(NtmItems.CIRCUIT_CONTROL_UNIT),
                400,
                15_000,
                new FluidStack(Fluids.PERFLUOROMETHYL, 1_000),
                ingredients(
                        stack(NtmItems.CIRCUIT_MICROCHIP, 32),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 32),
                        stack(NtmItems.CIRCUIT_TANTALIUM_CAPACITOR, 16)
                ),
                ingredients(
                        stack(NtmItems.CIRCUIT_CONTROL_UNIT_CASING, 1),
                        stack(NtmItems.UPGRADE_SPEED_1, 1)
                ),
                ingredients(
                        stack(NtmItems.WIRE_LEAD, 16)
                )
        );

        addRecipe(
                out(NtmItems.CIRCUIT_ADVANCED_CONTROL_UNIT),
                600,
                25_000,
                new FluidStack(Fluids.PERFLUOROMETHYL, 4_000),
                ingredients(
                        stack(NtmItems.CIRCUIT_VERSATILE_INTEGRATED, 16),
                        stack(NtmItems.CIRCUIT_TANTALIUM_CAPACITOR, 48),
                        stack(NtmItems.CIRCUIT_ATOMIC_CLOCK, 1)
                ),
                ingredients(
                        stack(NtmItems.CIRCUIT_CONTROL_UNIT_CASING, 1),
                        stack(NtmItems.UPGRADE_SPEED_3, 1)
                ),
                ingredients(
                        stack(NtmItems.WIRE_LEAD, 24)
                )
        );

        addRecipe(
                out(NtmItems.CIRCUIT_QUANTUM_COMPUTER),
                600,
                250_000,
                new FluidStack(Fluids.COLD_PERFLUOROMETHYL, 6_000),
                ingredients(
                        stack(NtmItems.CIRCUIT_SOLID_STATE_QUANTUM_PROCESSOR, 16),
                        stack(NtmItems.CIRCUIT_VERSATILE_INTEGRATED, 48),
                        stack(NtmItems.CIRCUIT_ATOMIC_CLOCK, 8)
                ),
                ingredients(
                        stack(NtmItems.CIRCUIT_CONTROL_UNIT_CASING, 2),
                        stack(NtmItems.UPGRADE_OVERDRIVE_1, 1)
                ),
                ingredients(
                        stack(NtmItems.WIRE_LEAD, 32)
                )
        );

        addRecipe(
                out(NtmItems.UPGRADE_SPEED_1),
                200,
                1_000,
                null,
                ingredients(
                        stack(NtmItems.CIRCUIT_VACUUM_TUBE, 4),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 1)
                ),
                ingredients(
                        stack(NtmItems.UPGRADE_TEMPLATE, 1),
                        stack(NtmItems.POWDER_GRADE_COPPER, 4)
                ),
                ingredients()
        );

        addRecipe(
                out(NtmItems.UPGRADE_EFFECT_1),
                200,
                1_000,
                null,
                ingredients(
                        stack(NtmItems.CIRCUIT_VACUUM_TUBE, 4),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 1)
                ),
                ingredients(
                        stack(NtmItems.UPGRADE_TEMPLATE, 1),
                        stack(NtmItems.POWDER_EMERALD, 4)
                ),
                ingredients()
        );

        addRecipe(
                out(NtmItems.UPGRADE_POWER_1),
                200,
                1_000,
                null,
                ingredients(
                        stack(NtmItems.CIRCUIT_VACUUM_TUBE, 4),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 1)
                ),
                ingredients(
                        stack(NtmItems.UPGRADE_TEMPLATE, 1),
                        stack(NtmItems.POWDER_GOLD, 4)
                ),
                ingredients()
        );

        addRecipe(
                out(NtmItems.UPGRADE_FORTUNE_1),
                200,
                1_000,
                null,
                ingredients(
                        stack(NtmItems.CIRCUIT_VACUUM_TUBE, 4),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 1)
                ),
                ingredients(
                        stack(NtmItems.UPGRADE_TEMPLATE, 1),
                        stack(NtmItems.POWDER_NIOBIUM, 4)
                ),
                ingredients()
        );

        addRecipe(
                out(NtmItems.UPGRADE_AFTERBURN_1),
                200,
                1_000,
                null,
                ingredients(
                        stack(NtmItems.CIRCUIT_VACUUM_TUBE, 4),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 1)
                ),
                ingredients(
                        stack(NtmItems.UPGRADE_TEMPLATE, 1),
                        stack(NtmItems.POWDER_TUNGSTEN, 4)
                ),
                ingredients()
        );

        addRecipe(
                out(NtmItems.UPGRADE_RADIUS),
                200,
                1_000,
                null,
                ingredients(
                        stack(NtmItems.CIRCUIT_MICROCHIP, 4),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 4)
                ),
                ingredients(
                        stack(NtmItems.UPGRADE_TEMPLATE, 1),
                        stack(Items.GLOWSTONE_DUST, 4)
                ),
                ingredients()
        );

        addRecipe(
                out(NtmItems.UPGRADE_HEALTH),
                200,
                1_000,
                null,
                ingredients(
                        stack(NtmItems.CIRCUIT_MICROCHIP, 4),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 4)
                ),
                ingredients(
                        stack(NtmItems.UPGRADE_TEMPLATE, 1),
                        stack(NtmItems.POWDER_LITHIUM, 4)
                ),
                ingredients()
        );

        addFirstUpgrade(NtmItems.UPGRADE_SPEED_1, NtmItems.UPGRADE_SPEED_2);
        addSecondUpgrade(NtmItems.UPGRADE_SPEED_2, NtmItems.UPGRADE_SPEED_3);
        addFirstUpgrade(NtmItems.UPGRADE_EFFECT_1, NtmItems.UPGRADE_EFFECT_2);
        addSecondUpgrade(NtmItems.UPGRADE_EFFECT_2, NtmItems.UPGRADE_EFFECT_3);
        addFirstUpgrade(NtmItems.UPGRADE_POWER_1, NtmItems.UPGRADE_POWER_2);
        addSecondUpgrade(NtmItems.UPGRADE_POWER_2, NtmItems.UPGRADE_POWER_3);
        addFirstUpgrade(NtmItems.UPGRADE_FORTUNE_1, NtmItems.UPGRADE_FORTUNE_2);
        addSecondUpgrade(NtmItems.UPGRADE_FORTUNE_2, NtmItems.UPGRADE_FORTUNE_3);
        addFirstUpgrade(NtmItems.UPGRADE_AFTERBURN_1, NtmItems.UPGRADE_AFTERBURN_2);
        addSecondUpgrade(NtmItems.UPGRADE_AFTERBURN_2, NtmItems.UPGRADE_AFTERBURN_3);
    }

    private static void addFirstUpgrade(DeferredItem<Item> lower, DeferredItem<Item> higher) {
        addRecipe(
                out(higher),
                300,
                10_000,
                null,
                ingredients(
                        stack(NtmItems.CIRCUIT_MICROCHIP, 8),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 4)
                ),
                ingredients(
                        stack(lower, 1),
                        stack(NtmItems.INGOT_POLYMER, 4)
                ),
                ingredients()
        );

        addRecipe(
                out(higher),
                300,
                10_000,
                null,
                ingredients(
                        stack(NtmItems.CIRCUIT_MICROCHIP, 8),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 4)
                ),
                ingredients(
                        stack(lower, 1),
                        stack(NtmItems.INGOT_BAKELITE, 4)
                ),
                ingredients()
        );
    }

    private static void addSecondUpgrade(DeferredItem<Item> lower, DeferredItem<Item> higher) {
        addRecipe(
                out(higher),
                400,
                25_000,
                new FluidStack(Fluids.SOLVENT, 500),
                ingredients(
                        stack(NtmItems.CIRCUIT_MICROCHIP, 16),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 16)
                ),
                ingredients(
                        stack(lower, 1),
                        stack(NtmItems.INGOT_BIORUBBER, 4)
                ),
                ingredients()
        );

        addRecipe(
                out(higher),
                400,
                25_000,
                new FluidStack(Fluids.SOLVENT, 500),
                ingredients(
                        stack(NtmItems.CIRCUIT_MICROCHIP, 16),
                        stack(NtmItems.CIRCUIT_CAPACITOR, 16)
                ),
                ingredients(
                        stack(lower, 1),
                        stack(NtmItems.INGOT_RUBBER, 4)
                ),
                ingredients()
        );
    }

    public static SolderingRecipe getRecipe(ItemStack[] inputs) {
        for(SolderingRecipe recipe : recipes) {
            if(matchesIngredients(new ItemStack[] { inputs[0], inputs[1], inputs[2] }, recipe.toppings)
                    && matchesIngredients(new ItemStack[] { inputs[3], inputs[4] }, recipe.pcb)
                    && matchesIngredients(new ItemStack[] { inputs[5] }, recipe.solder)) {
                return recipe;
            }
        }

        return null;
    }

    public static boolean matchesIngredients(ItemStack[] stacks, AStack[] ingredients) {
        if(ingredients == null || ingredients.length == 0) return true;

        ItemStack[] remaining = new ItemStack[stacks.length];
        for(int i = 0; i < stacks.length; i++) {
            remaining[i] = stacks[i].copy();
        }

        for(AStack ingredient : ingredients) {
            boolean matched = false;
            for(ItemStack stack : remaining) {
                if(ingredient.matchesRecipe(stack, false)) {
                    stack.shrink(ingredient.stacksize);
                    matched = true;
                    break;
                }
            }
            if(!matched) return false;
        }

        return true;
    }

    @Override
    public String getFileName() {
        return "hbmSoldering.json";
    }

    @Override
    public Object getRecipeObject() {
        return recipes;
    }

    @Override
    public void readRecipe(JsonElement recipe) {
        JsonObject obj = (JsonObject) recipe;

        AStack[] toppings = SerializableRecipe.readAStackArray(obj.get("toppings").getAsJsonArray());
        AStack[] pcb = SerializableRecipe.readAStackArray(obj.get("pcb").getAsJsonArray());
        AStack[] solder = SerializableRecipe.readAStackArray(obj.get("solder").getAsJsonArray());
        FluidStack fluid = obj.has("fluid") ? SerializableRecipe.readFluidStack(obj.get("fluid").getAsJsonArray()) : null;
        ItemStack output = SerializableRecipe.readItemStack(obj.get("output").getAsJsonArray());
        int duration = obj.get("duration").getAsInt();
        long consumption = obj.get("consumption").getAsLong();

        recipes.add(new SolderingRecipe(output, duration, consumption, fluid, toppings, pcb, solder));
    }

    @Override
    public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
        SolderingRecipe entry = (SolderingRecipe) recipe;

        writer.name("toppings").beginArray();
        for(AStack aStack : entry.toppings) SerializableRecipe.writeAStack(aStack, writer);
        writer.endArray();

        writer.name("pcb").beginArray();
        for(AStack aStack : entry.pcb) SerializableRecipe.writeAStack(aStack, writer);
        writer.endArray();

        writer.name("solder").beginArray();
        for(AStack aStack : entry.solder) SerializableRecipe.writeAStack(aStack, writer);
        writer.endArray();

        if(entry.fluid != null) {
            writer.name("fluid");
            SerializableRecipe.writeFluidStack(entry.fluid, writer);
        }

        writer.name("output");
        SerializableRecipe.writeItemStack(entry.output, writer);

        writer.name("duration").value(entry.duration);
        writer.name("consumption").value(entry.consumption);
    }

    @Override
    public void deleteRecipes() {
        recipes.clear();
        toppings.clear();
        pcb.clear();
        solder.clear();
    }

    @Override
    public String getComment() {
        return "Soldering station recipes.";
    }

    public static class SolderingRecipe {
        public final ItemStack output;
        public final int duration;
        public final long consumption;
        public final FluidStack fluid;
        public final AStack[] toppings;
        public final AStack[] pcb;
        public final AStack[] solder;

        public SolderingRecipe(ItemStack output, int duration, long consumption, FluidStack fluid, AStack[] toppings, AStack[] pcb, AStack[] solder) {
            this.output = output;
            this.duration = duration;
            this.consumption = consumption;
            this.fluid = fluid;
            this.toppings = toppings;
            this.pcb = pcb;
            this.solder = solder;

            Collections.addAll(SolderingRecipes.toppings, toppings);
            Collections.addAll(SolderingRecipes.pcb, pcb);
            Collections.addAll(SolderingRecipes.solder, solder);
            recipes.add(this);
        }

        public SolderingRecipe(ItemStack output, int duration, long consumption, AStack[] toppings, AStack[] pcb, AStack[] solder) {
            this(output, duration, consumption, null, toppings, pcb, solder);
        }
    }

    private static void addRecipe(ItemStack output, int duration, long consumption, FluidStack fluid, AStack[] toppings, AStack[] pcb, AStack[] solder) {
        new SolderingRecipe(output, duration, consumption, fluid, toppings, pcb, solder);
    }

    private static AStack[] ingredients(AStack... ingredients) {
        return ingredients;
    }

    private static ComparableStack stack(DeferredItem<Item> item) {
        return new ComparableStack(item.get());
    }

    private static ComparableStack stack(DeferredItem<Item> item, int count) {
        return new ComparableStack(item.get(), count);
    }

    private static ComparableStack stack(Item item) {
        return new ComparableStack(item);
    }

    private static ComparableStack stack(Item item, int count) {
        return new ComparableStack(item, count);
    }

    private static ItemStack out(DeferredItem<Item> item) {
        return new ItemStack(item.get());
    }
}
