package com.hbm.items.machine;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.util.BobMathUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class BatterySC extends Item implements IBatteryItem {

    public enum BatterySCType {

        EMPTY(	    0),
        WASTE(	  150),
        RA226(	  200),
        TC99(	  500),
        CO60(	  750),
        PU238(	1_000),
        PO210(	1_250),
        AU198(	1_500),
        PB209(	2_000),
        AM241(	2_500);

        public long power;

        BatterySCType(long power) {
            this.power = power;
        }
    }

    private BatterySCType type;

    public BatterySC(Properties properties, BatterySCType type) {
        super(properties.stacksTo(1));
        this.type = type;
    }

    @Override public void chargeBattery(ItemStack stack, long i) { }
    @Override public void setCharge(ItemStack stack, long i) { }
    @Override public void dischargeBattery(ItemStack stack, long i) { }
    @Override public long getChargeRate(ItemStack stack) { return 0; }

    @Override public long getCharge(ItemStack stack) { return getMaxCharge(stack); }
    @Override public long getDischargeRate(ItemStack stack) { return getMaxCharge(stack); }

    @Override public long getMaxCharge(ItemStack stack) { return type.power; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        if (type.power > 0) components.add(Component.translatable("item.hbmsntm.battery_sc.desc.discharge_rate", BobMathUtil.getShortNumber(type.power) + "HE/t").withStyle(ChatFormatting.YELLOW));
    }
}
