package com.hbm.items.machine;

import com.hbm.inventory.FluidStack;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.NtmItems;
import com.hbm.util.BobMathUtil;
import com.hbm.util.TagsUtil;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class FluidIconItem extends Item {

    public FluidIconItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Fluids.fromID(MetaHelper.getMeta(stack)).getName();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        if(TagsUtil.hasCData(stack)) {
            if(getQuantity(stack) > 0) components.add(Component.literal(getQuantity(stack) + I18nUtil.resolveKey("mb")).withStyle(ChatFormatting.GRAY));
            if(getPressure(stack) > 0) {
                components.add(Component.literal(getPressure(stack) + I18nUtil.resolveKey("pu")).withStyle(ChatFormatting.RED));
                components.add(Component.translatable("fluid.pressurized").withStyle(BobMathUtil.getBlink() ? ChatFormatting.RED : ChatFormatting.DARK_RED));
            }
        }

        FluidType type = Fluids.fromID(MetaHelper.getMeta(stack));
        if(type != null) {
            type.addInfo(components);
        }
    }

    public static ItemStack addQuantity(ItemStack stack, int amount) {
        if(amount <= 0) return stack;
        CompoundTag tag = TagsUtil.getCData(stack);
        tag.putInt("Fill", amount);
        TagsUtil.putCData(stack, tag);
        return stack;
    }

    public static ItemStack addPressure(ItemStack stack, int pressure) {
        CompoundTag tag = TagsUtil.getCData(stack);
        tag.putInt("Pressure", pressure);
        TagsUtil.putCData(stack, tag);
        return stack;
    }

    public static int getQuantity(ItemStack stack) {
        return TagsUtil.getCData(stack).getInt("Fill");
    }

    public static int getPressure(ItemStack stack) {
        return TagsUtil.getCData(stack).getInt("Pressure");
    }

    public static ItemStack make(FluidStack fluidStack) {
        return create(fluidStack.type, fluidStack.fill, fluidStack.pressure);
    }

    public static ItemStack make(FluidType fluid, int amount) {
        return create(fluid, amount, 0);
    }

    public static ItemStack create(FluidType type, int amount, int pressure) {
        ItemStack stack = MetaHelper.metaStack(new ItemStack(NtmItems.FLUID_ICON.get()), type.getID());
        addQuantity(stack, amount);
        addPressure(stack, pressure);
        return stack;
    }
}