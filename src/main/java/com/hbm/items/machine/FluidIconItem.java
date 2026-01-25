package com.hbm.items.machine;

import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ModItems;
import com.hbm.items.datacomps.FluidTypeComponent;
import com.hbm.items.datacomps.ModDataComponents;
import com.hbm.util.BobMathUtil;
import com.hbm.util.TagsUtilDegradation;
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
        return FluidTypeComponent.getFluidType(stack).getName();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        if (TagsUtilDegradation.containsAnyTag(stack)) {
            if (getQuantity(stack) > 0) components.add(Component.translatable("fluid.info.mb", getQuantity(stack)).withStyle(ChatFormatting.GRAY));
            if (getPressure(stack) > 0) {
                components.add(Component.translatable("fluid.info.pressure", getPressure(stack)).withStyle(ChatFormatting.RED));
                components.add(Component.translatable("fluid.info.pressurized").withStyle(BobMathUtil.getBlink() ? ChatFormatting.RED : ChatFormatting.DARK_RED));
            }
        }

        FluidType type = FluidTypeComponent.getFluidType(stack);
        if (type != null) {
            type.addInfo(components);
        }
    }

    public static ItemStack addQuantity(ItemStack stack, int amount) {
        if (amount <= 0) return stack;
        CompoundTag tag = TagsUtilDegradation.getTag(stack);
        tag.putInt("Fill", amount);
        TagsUtilDegradation.putTag(stack, tag);
        return stack;
    }

    public static ItemStack addPressure(ItemStack stack, int pressure) {
        CompoundTag tag = TagsUtilDegradation.getTag(stack);
        tag.putInt("Pressure", pressure);
        TagsUtilDegradation.putTag(stack, tag);
        return stack;
    }

    public static int getQuantity(ItemStack stack) {
        return TagsUtilDegradation.getTag(stack).getInt("Fill");
    }

    public static int getPressure(ItemStack stack) {
        return TagsUtilDegradation.getTag(stack).getInt("Pressure");
    }

    public static ItemStack make(FluidStack fluidStack) {
        return create(fluidStack.type, fluidStack.fill, fluidStack.pressure);
    }

    public static ItemStack make(FluidType fluid, int amount) {
        return create(fluid, amount, 0);
    }

    public static ItemStack create(FluidType type, int amount, int pressure) {
        ItemStack stack = new ItemStack(ModItems.FLUID_ICON.get());
        stack.set(ModDataComponents.FLUID_TYPE.get(), new FluidTypeComponent(type.getID()));
        addQuantity(stack, amount);
        addPressure(stack, pressure);
        return stack;
    }
}