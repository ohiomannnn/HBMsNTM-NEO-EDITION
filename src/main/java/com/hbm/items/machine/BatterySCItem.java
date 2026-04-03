package com.hbm.items.machine;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.inventory.MetaHelper;
import com.hbm.items.EnumMultiItem;
import com.hbm.util.BobMathUtil;
import com.hbm.util.EnumUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class BatterySCItem extends EnumMultiItem implements IBatteryItem {

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

        public final long power;

        BatterySCType(long power) {
            this.power = power;
        }
    }

    public BatterySCItem(Properties properties) {
        super(properties.stacksTo(1), BatterySCType.class, true, true);
    }

    @Override public void chargeBattery(ItemStack stack, long i) { }
    @Override public void setCharge(ItemStack stack, long i) { }
    @Override public void dischargeBattery(ItemStack stack, long i) { }
    @Override public long getChargeRate(ItemStack stack) { return 0; }

    @Override public long getCharge(ItemStack stack) { return getMaxCharge(stack); }
    @Override public long getDischargeRate(ItemStack stack) { return getMaxCharge(stack); }

    @Override public long getMaxCharge(ItemStack stack) {
        BatterySCType pack = EnumUtil.grabEnumSafely(BatterySCType.class, MetaHelper.getMeta(stack));
        return pack.power;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        BatterySCType pack = EnumUtil.grabEnumSafely(BatterySCType.class, MetaHelper.getMeta(stack));
        if (pack.power > 0) components.add(Component.translatable("item.hbmsntm.battery_sc.desc.discharge_rate", BobMathUtil.getShortNumber(pack.power) + "HE/t").withStyle(ChatFormatting.YELLOW));
    }
}
