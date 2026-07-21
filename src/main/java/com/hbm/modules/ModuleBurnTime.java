package com.hbm.modules;

import com.hbm.items.NtmItems;
import com.hbm.util.ItemStackUtil;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Lightweight 1.12-style burn helper for heater machines.
 * Keeps the old fuel category tuning while working with modern item tags and registry names.
 */
public class ModuleBurnTime {

    private static final int MOD_LOG = 0;
    private static final int MOD_WOOD = 1;
    private static final int MOD_COAL = 2;
    private static final int MOD_LIGNITE = 3;
    private static final int MOD_COKE = 4;
    private static final int MOD_SOLID = 5;
    private static final int MOD_ROCKET = 6;
    private static final int MOD_BALEFIRE = 7;

    private final double[] modTime = new double[8];
    private final double[] modHeat = new double[8];

    public ModuleBurnTime() {
        for(int i = 0; i < this.modTime.length; i++) {
            this.modTime[i] = 1.0D;
            this.modHeat[i] = 1.0D;
        }
    }

    public int getBurnTime(ItemStack stack) {
        return this.getBurnTime(stack, 1.0D);
    }

    public int getBurnTime(ItemStack stack, double def) {
        if(stack == null || stack.isEmpty()) return 0;

        int fuel = stack.getBurnTime(null);
        if(fuel <= 0) return 0;

        return (int) (fuel * this.getMod(stack, this.modTime, def));
    }

    public int getBurnHeat(int base, ItemStack stack) {
        return this.getBurnHeat(base, stack, 1.0D);
    }

    public int getBurnHeat(int base, ItemStack stack, double def) {
        if(base <= 0 || stack == null || stack.isEmpty()) return 0;
        return (int) (base * this.getMod(stack, this.modHeat, def));
    }

    public double getMod(ItemStack stack, double[] mod, double def) {
        if(stack == null || stack.isEmpty()) return 0.0D;

        if(stack.is(NtmItems.BALEFIRE_AND_STEEL.get())) {
            return mod[MOD_BALEFIRE];
        }

        String path = stack.getItem().builtInRegistryHolder().key().location().getPath().toLowerCase(Locale.US);
        List<String> tags = ItemStackUtil.getTags(stack);

        if(path.contains("rocket")) return mod[MOD_ROCKET];
        if(path.contains("solid_fuel") || path.contains("solidfuel")) return mod[MOD_SOLID];
        if(path.contains("coke")) return mod[MOD_COKE];
        if(path.contains("lignite")) return mod[MOD_LIGNITE];
        if(path.contains("coal")) return mod[MOD_COAL];
        if(path.contains("log")) return mod[MOD_LOG];
        if(path.contains("wood") || path.contains("plank")) return mod[MOD_WOOD];

        for(String tag : tags) {
            String lower = tag.toLowerCase(Locale.US);
            if(lower.contains("rocket")) return mod[MOD_ROCKET];
            if(lower.contains("solid_fuel")) return mod[MOD_SOLID];
            if(lower.contains("coke")) return mod[MOD_COKE];
            if(lower.contains("lignite")) return mod[MOD_LIGNITE];
            if(lower.contains("coal")) return mod[MOD_COAL];
            if(lower.contains("logs")) return mod[MOD_LOG];
            if(lower.contains("wood") || lower.contains("planks")) return mod[MOD_WOOD];
        }

        return def;
    }

    public ModuleBurnTime setLogTimeMod(double mod) { this.modTime[MOD_LOG] = mod; return this; }
    public ModuleBurnTime setWoodTimeMod(double mod) { this.modTime[MOD_WOOD] = mod; return this; }
    public ModuleBurnTime setCoalTimeMod(double mod) { this.modTime[MOD_COAL] = mod; return this; }
    public ModuleBurnTime setLigniteTimeMod(double mod) { this.modTime[MOD_LIGNITE] = mod; return this; }
    public ModuleBurnTime setCokeTimeMod(double mod) { this.modTime[MOD_COKE] = mod; return this; }
    public ModuleBurnTime setSolidTimeMod(double mod) { this.modTime[MOD_SOLID] = mod; return this; }
    public ModuleBurnTime setRocketTimeMod(double mod) { this.modTime[MOD_ROCKET] = mod; return this; }
    public ModuleBurnTime setBalefireTimeMod(double mod) { this.modTime[MOD_BALEFIRE] = mod; return this; }

    public ModuleBurnTime setLogHeatMod(double mod) { this.modHeat[MOD_LOG] = mod; return this; }
    public ModuleBurnTime setWoodHeatMod(double mod) { this.modHeat[MOD_WOOD] = mod; return this; }
    public ModuleBurnTime setCoalHeatMod(double mod) { this.modHeat[MOD_COAL] = mod; return this; }
    public ModuleBurnTime setLigniteHeatMod(double mod) { this.modHeat[MOD_LIGNITE] = mod; return this; }
    public ModuleBurnTime setCokeHeatMod(double mod) { this.modHeat[MOD_COKE] = mod; return this; }
    public ModuleBurnTime setSolidHeatMod(double mod) { this.modHeat[MOD_SOLID] = mod; return this; }
    public ModuleBurnTime setRocketHeatMod(double mod) { this.modHeat[MOD_ROCKET] = mod; return this; }
    public ModuleBurnTime setBalefireHeatMod(double mod) { this.modHeat[MOD_BALEFIRE] = mod; return this; }

    public List<String> getDesc() {
        List<String> desc = new ArrayList<>();
        desc.addAll(this.getTimeDesc());
        desc.addAll(this.getHeatDesc());
        return desc;
    }

    public List<String> getTimeDesc() {
        List<String> list = new ArrayList<>();
        list.add("Burn time bonuses:");

        this.addIf(list, "Logs", this.modTime[MOD_LOG]);
        this.addIf(list, "Wood", this.modTime[MOD_WOOD]);
        this.addIf(list, "Coal", this.modTime[MOD_COAL]);
        this.addIf(list, "Lignite", this.modTime[MOD_LIGNITE]);
        this.addIf(list, "Coke", this.modTime[MOD_COKE]);
        this.addIf(list, "Solid Fuel", this.modTime[MOD_SOLID]);
        this.addIf(list, "Rocket Fuel", this.modTime[MOD_ROCKET]);
        this.addIf(list, "Balefire", this.modTime[MOD_BALEFIRE]);

        if(list.size() == 1) {
            list.clear();
        }

        return list;
    }

    public List<String> getHeatDesc() {
        List<String> list = new ArrayList<>();
        list.add("Burn heat bonuses:");

        this.addIf(list, "Logs", this.modHeat[MOD_LOG]);
        this.addIf(list, "Wood", this.modHeat[MOD_WOOD]);
        this.addIf(list, "Coal", this.modHeat[MOD_COAL]);
        this.addIf(list, "Lignite", this.modHeat[MOD_LIGNITE]);
        this.addIf(list, "Coke", this.modHeat[MOD_COKE]);
        this.addIf(list, "Solid Fuel", this.modHeat[MOD_SOLID]);
        this.addIf(list, "Rocket Fuel", this.modHeat[MOD_ROCKET]);
        this.addIf(list, "Balefire", this.modHeat[MOD_BALEFIRE]);

        if(list.size() == 1) {
            list.clear();
        }

        return list;
    }

    private void addIf(List<String> list, String name, double mod) {
        if(mod != 1.0D) {
            list.add("- " + name + ": " + this.getPercent(mod));
        }
    }

    private String getPercent(double mod) {
        mod -= 1D;
        String num = ((int) (mod * 100)) + "%";
        if(mod < 0) {
            return num;
        }
        return "+" + num;
    }
}
