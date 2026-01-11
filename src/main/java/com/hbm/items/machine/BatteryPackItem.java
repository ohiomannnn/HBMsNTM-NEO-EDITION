package com.hbm.items.machine;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.HBMsNTM;
import com.hbm.util.BobMathUtil;
import com.hbm.util.TagsUtilDegradation;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class BatteryPackItem extends Item implements IBatteryItem {

    public enum BatteryPack {
        BATTERY_REDSTONE	("battery_redstone",	      100L, false),
        BATTERY_LEAD		("battery_lead",		    1_000L, false),
        BATTERY_LITHIUM		("battery_lithium",		   10_000L, false),
        BATTERY_SODIUM		("battery_sodium",		   50_000L, false),
        BATTERY_SCHRABIDIUM	("battery_schrabidium",	  250_000L, false),
        BATTERY_QUANTUM		("battery_quantum",		1_000_000L, 20 * 60 * 60),

        CAPACITOR_COPPER	("capacitor_copper",	     1_000L, true),
        CAPACITOR_GOLD		("capacitor_gold",		    10_000L, true),
        CAPACITOR_NIOBIUM	("capacitor_niobium",	   100_000L, true),
        CAPACITOR_TANTALUM	("capacitor_tantalum",	   500_000L, true),
        CAPACITOR_BISMUTH	("capacitor_bismuth",	 2_500_000L, true),
        CAPACITOR_SPARK		("capacitor_spark",		10_000_000L, true);

        public ResourceLocation texture;
        public long capacity;
        public long chargeRate;
        public long dischargeRate;

        BatteryPack(String tex, long dischargeRate, boolean capacitor) {
            this(tex,
                    capacitor ? (dischargeRate * 20 * 30) : (dischargeRate * 20 * 60 * 15),
                    capacitor ? dischargeRate : dischargeRate * 10,
                    dischargeRate);
        }

        BatteryPack(String tex, long dischargeRate, long duration) {
            this(tex, dischargeRate * duration, dischargeRate * 10, dischargeRate);
        }

        BatteryPack(String tex, long capacity, long chargeRate, long dischargeRate) {
            this.texture = HBMsNTM.withDefaultNamespaceNT("textures/models/machines/" + tex + ".png");
            this.capacity = capacity;
            this.chargeRate = chargeRate;
            this.dischargeRate = dischargeRate;
        }

        public boolean isCapacitor() { return this.ordinal() > BATTERY_QUANTUM.ordinal(); }
    }

    private BatteryPack pack;

    public BatteryPackItem(Properties properties, BatteryPack batteryPack) {
        super(properties.stacksTo(1));
        this.pack = batteryPack;
    }

    @Override
    public void chargeBattery(ItemStack stack, long i) {
        if (TagsUtilDegradation.containsAnyTag(stack)) {
            CompoundTag tag = TagsUtilDegradation.getTag(stack);
            tag.putLong("Charge", tag.getLong("Charge") + i);
            TagsUtilDegradation.putTag(stack, tag);
        } else {
            CompoundTag tag = new CompoundTag();
            tag.putLong("Charge", i);
            TagsUtilDegradation.putTag(stack, tag);
        }
    }

    @Override
    public void setCharge(ItemStack stack, long i) {
        if (TagsUtilDegradation.containsAnyTag(stack)) {
            CompoundTag tag = TagsUtilDegradation.getTag(stack);
            tag.putLong("Charge", i);
            TagsUtilDegradation.putTag(stack, tag);
        } else {
            CompoundTag tag = new CompoundTag();
            tag.putLong("Charge", i);
            TagsUtilDegradation.putTag(stack, tag);
        }
    }

    @Override
    public void dischargeBattery(ItemStack stack, long i) {
        if (TagsUtilDegradation.containsAnyTag(stack)) {
            CompoundTag tag = TagsUtilDegradation.getTag(stack);
            tag.putLong("Charge", tag.getLong("Charge") - i);
            TagsUtilDegradation.putTag(stack, tag);
        } else {
            CompoundTag tag = new CompoundTag();
            tag.putLong("Charge", 0);
            TagsUtilDegradation.putTag(stack, tag);
        }
    }

    @Override
    public long getCharge(ItemStack stack) {
        if (!TagsUtilDegradation.containsAnyTag(stack)) {
            CompoundTag tag = new CompoundTag();
            tag.putLong("Charge", 0);
            TagsUtilDegradation.putTag(stack, tag);
        }
        return TagsUtilDegradation.getTag(stack).getLong("Charge");
    }

    @Override
    public long getMaxCharge(ItemStack stack) {
        return pack.capacity;
    }

    @Override
    public long getChargeRate(ItemStack stack) {
        return pack.chargeRate;
    }

    @Override
    public long getDischargeRate(ItemStack stack) {
        return pack.dischargeRate;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return super.getUseDuration(stack, entity);
    }

    @Override public boolean isBarVisible(ItemStack stack) { return getCharge(stack) < getMaxCharge(stack); }
    @Override public int getBarWidth(ItemStack stack) { return Math.round(13.0F * getCharge(stack) / (float) getMaxCharge(stack)); }
    @Override public int getBarColor(ItemStack stack) {
        float ratio = (float) getCharge(stack) / (float) getMaxCharge(stack);
        return Mth.hsvToRgb(ratio / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        long maxCharge = this.getMaxCharge(stack);
        long chargeRate = this.getChargeRate(stack);
        long dischargeRate = this.getDischargeRate(stack);
        long charge = maxCharge;

        if (TagsUtilDegradation.containsAnyTag(stack)) charge = getCharge(stack);

        components.add(Component.translatable("item.hbmsntm.battery_pack.desc.energy_stored", BobMathUtil.getShortNumber(charge) + "/" + BobMathUtil.getShortNumber(maxCharge) + "HE (" + (charge * 1000 / maxCharge / 10D) + "%)").withStyle(ChatFormatting.GREEN));
        components.add(Component.translatable("item.hbmsntm.battery_pack.desc.charge_rate", BobMathUtil.getShortNumber(chargeRate) + "HE/t").withStyle(ChatFormatting.YELLOW));
        components.add(Component.translatable("item.hbmsntm.battery_pack.desc.discharge_rate", BobMathUtil.getShortNumber(dischargeRate) + "HE/t").withStyle(ChatFormatting.YELLOW));
        components.add(Component.translatable("item.hbmsntm.battery_pack.desc.time_for_full_charge", (maxCharge / chargeRate / 20 / 60D) + "min").withStyle(ChatFormatting.GOLD));
        components.add(Component.translatable("item.hbmsntm.battery_pack.desc.charge_lasts_for", (maxCharge / dischargeRate / 20 / 60D) + "min").withStyle(ChatFormatting.GOLD));
    }

    public static ItemStack makeEmptyBattery(ItemStack stack) {
        CompoundTag tag = new CompoundTag();
        tag.putLong("Charge", 0);
        TagsUtilDegradation.putTag(stack, tag);
        return stack;
    }

    public static ItemStack makeFullBattery(ItemStack stack) {
        CompoundTag tag = new CompoundTag();
        tag.putLong("Charge", ((BatteryPackItem) stack.getItem()).getMaxCharge(stack));
        TagsUtilDegradation.putTag(stack, tag);
        return stack;
    }
}


