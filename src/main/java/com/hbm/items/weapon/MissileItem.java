package com.hbm.items.weapon;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Locale;

public class MissileItem extends Item {

    public final MissileFormFactor formFactor;
    public final MissileTier tier;
    public final MissileFuel fuel;
    public int fuelCap;
    public boolean launchable = true;

    public MissileItem(MissileFormFactor form, MissileTier tier) {
        this(form, tier, form.defaultFuel);
    }

    public MissileItem(MissileFormFactor form, MissileTier tier, MissileFuel fuel) {
        super(new Properties().stacksTo(1));
        this.formFactor = form;
        this.tier = tier;
        this.fuel = fuel;
        this.setFuelCap(this.fuel.defaultCap);
    }

    public MissileItem notLaunchable() {
        this.launchable = false;
        return this;
    }

    public MissileItem setFuelCap(int fuelCap) {
        this.fuelCap = fuelCap;
        return this;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("item.missile.tier." + this.tier.name().toLowerCase(Locale.US)).withStyle(ChatFormatting.ITALIC));

        if (!this.launchable) {
            components.add(Component.translatable("item.missile.desc.notLaunchable").withStyle(ChatFormatting.RED));
        } else {
            components.add(Component.translatable("item.missile.desc.fuel").append(": ").append(Component.translatable(this.fuel.key)));
            if (this.fuelCap > 0) components.add(Component.translatable("item.missile.desc.fuelCapacity").append(": ").append(Component.translatable("fluid.info.mb", this.fuelCap)));
        }
    }

    public enum MissileFormFactor {
        ABM(MissileFuel.SOLID),
        MICRO(MissileFuel.SOLID),
        V2(MissileFuel.ETHANOL_PEROXIDE),
        STRONG(MissileFuel.KEROSENE_PEROXIDE),
        HUGE(MissileFuel.KEROSENE_LOXY),
        ATLAS(MissileFuel.JETFUEL_LOXY),
        OTHER(MissileFuel.KEROSENE_PEROXIDE);

        private final MissileFuel defaultFuel;

        MissileFormFactor(MissileFuel defaultFuel) {
            this.defaultFuel = defaultFuel;
        }
    }

    public enum MissileTier {
        TIER0,
        TIER1,
        TIER2,
        TIER3,
        TIER4
    }

    public enum MissileFuel {
        SOLID("item.missile.fuel.solid.prefueled", ChatFormatting.GOLD, 0),
        ETHANOL_PEROXIDE("item.missile.fuel.ethanol_peroxide", ChatFormatting.AQUA, 4_000),
        KEROSENE_PEROXIDE("item.missile.fuel.kerosene_peroxide", ChatFormatting.BLUE, 8_000),
        KEROSENE_LOXY("item.missile.fuel.kerosene_loxy", ChatFormatting.LIGHT_PURPLE, 12_000),
        JETFUEL_LOXY("item.missile.fuel.jetfuel_loxy", ChatFormatting.RED, 16_000);

        public final String key;
        public final ChatFormatting color;
        public final int defaultCap;

        MissileFuel(String key, ChatFormatting color, int defaultCap) {
            this.key = key;
            this.color = color;
            this.defaultCap = defaultCap;
        }
    }
}
