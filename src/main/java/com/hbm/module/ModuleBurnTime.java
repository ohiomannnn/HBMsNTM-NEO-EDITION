package com.hbm.module;

import com.hbm.items.NtmItems;
import com.hbm.util.ItemStackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple module for determining the burn time of a stack with added options to define bonuses
 * @author hbm
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
        for(int i = 0; i < modTime.length; i++) {
            modTime[i] = 1.0;
            modHeat[i] = 1.0;
        }
    }

    public int getBurnTime(ItemStack stack, double def) {
        int fuel = stack.getBurnTime(null);
        if(fuel == 0) return 0;
        return (int) (fuel * getMod(stack, modTime, def));
    }

    public int getBurnTime(ItemStack stack) {
        return getBurnTime(stack, 1.0);
    }

    public int getBurnHeat(int base, ItemStack stack, double def) {
        if(base <= 0) return 0;
        return (int) (base * getMod(stack, modHeat));
    }

    public int getBurnHeat(int base, ItemStack stack) {
        return getBurnHeat(base, stack, 1.0);
    }

    public double getMod(ItemStack stack, double[] mod, double def) {
        if(stack == null || stack.isEmpty()) return 0.0;

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

    public double getMod(ItemStack stack, double[] mod) {
        return getMod(stack, mod, 1.0);
    }

    public List<Component> getDesc() {
        List<Component> desc = new ArrayList<>();
        desc.addAll(this.getTimeDesc());
        desc.addAll(this.getHeatDesc());
        return desc;
    }

    public List<Component> getTimeDesc() {
        List<Component> components = new ArrayList<>();

        components.add(Component.translatable("container.burntime.time_bonuses").withStyle(ChatFormatting.GOLD));

        this.addIf(components, Component.translatable("container.burntime.log"), this.modTime[MOD_LOG]);
        this.addIf(components, Component.translatable("container.burntime.wood"), this.modTime[MOD_WOOD]);
        this.addIf(components, Component.translatable("container.burntime.coal"), this.modTime[MOD_COAL]);
        this.addIf(components, Component.translatable("container.burntime.lignite"), this.modTime[MOD_LIGNITE]);
        this.addIf(components, Component.translatable("container.burntime.coke"), this.modTime[MOD_COKE]);
        this.addIf(components, Component.translatable("container.burntime.solid"), this.modTime[MOD_SOLID]);
        this.addIf(components, Component.translatable("container.burntime.rocket"), this.modTime[MOD_ROCKET]);
        this.addIf(components, Component.translatable("container.burntime.balefire"), this.modTime[MOD_BALEFIRE]);

        if(components.size() == 1) components.clear();
        return components;
    }

    public List<Component> getHeatDesc() {
        List<Component> components = new ArrayList<>();

        components.add(Component.translatable("container.burntime.heat_bonuses").withStyle(ChatFormatting.RED));

        this.addIf(components, Component.translatable("container.burntime.log"), this.modHeat[MOD_LOG]);
        this.addIf(components, Component.translatable("container.burntime.wood"), this.modHeat[MOD_WOOD]);
        this.addIf(components, Component.translatable("container.burntime.coal"), this.modHeat[MOD_COAL]);
        this.addIf(components, Component.translatable("container.burntime.lignite"), this.modHeat[MOD_LIGNITE]);
        this.addIf(components, Component.translatable("container.burntime.coke"), this.modHeat[MOD_COKE]);
        this.addIf(components, Component.translatable("container.burntime.solid"), this.modHeat[MOD_SOLID]);
        this.addIf(components, Component.translatable("container.burntime.rocket"), this.modHeat[MOD_ROCKET]);
        this.addIf(components, Component.translatable("container.burntime.balefire"), this.modHeat[MOD_BALEFIRE]);

        if(components.size() == 1) components.clear();
        return components;
    }

    private void addIf(List<Component> components, Component name, double mod) {
        if(mod != 1.0) components.add(Component.literal("- ").append(name).append(": ").withStyle(ChatFormatting.YELLOW).append(this.getPercent(mod)));
    }

    private String getPercent(double mod) {
        mod -= 1D;
        String num = ((int) (mod * 100)) + "%";

        num = mod < 0 ? ChatFormatting.RED + num : ChatFormatting.GREEN + "+" + num;
        return num;
    }

    public double[] getModHeat() {
        return modHeat;
    }

    public double[] getModTime() {
        return modTime;
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
}
