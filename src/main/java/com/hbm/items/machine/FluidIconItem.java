package com.hbm.items.machine;

import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ModItems;
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
        FluidType type = getFluidType(stack);
        if (type != null) {
            return Component.translatable(type.getConditionalName());
        }
        return Component.literal("Unknown Fluid");
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        if (TagsUtilDegradation.containsAnyTag(stack)) {
            if (getQuantity(stack) > 0) components.add(Component.literal(getQuantity(stack) + "mB").withStyle(ChatFormatting.GRAY));
            if (getPressure(stack) > 0) {
                components.add(Component.translatable("fluid.info.pressure", getPressure(stack)).withStyle(ChatFormatting.RED));
                components.add(Component.translatable("fluid.info.pressurized").withStyle(BobMathUtil.getBlink() ? ChatFormatting.RED : ChatFormatting.DARK_RED));
            }
        }

        FluidType type = getFluidType(stack);
        if (type != null) {
            type.addInfo(components);
        }
    }

    public static FluidType getFluidType(ItemStack stack) {
        CompoundTag tag = TagsUtilDegradation.getTag(stack);
        if (tag.contains("FluidId")) {
            return Fluids.fromID(tag.getInt("FluidId"));
        }
        return null;
    }

    public static ItemStack setFluidType(ItemStack stack, FluidType type) {
        CompoundTag tag = TagsUtilDegradation.getTag(stack);
        tag.putInt("FluidId", type.getID());
        TagsUtilDegradation.putTag(stack, tag);
        return stack;
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
        return make(fluidStack.type, fluidStack.fill, fluidStack.pressure);
    }

    public static ItemStack make(FluidType fluid, int amount) {
        return make(fluid, amount, 0);
    }

    public static ItemStack make(FluidType fluid, int amount, int pressure) {
        ItemStack stack = new ItemStack(ModItems.FLUID_ICON.get());
        setFluidType(stack, fluid);
        addQuantity(stack, amount);
        addPressure(stack, pressure);
        return stack;
    }

    public static int getColor(ItemStack stack) {
        FluidType type = getFluidType(stack);
        if (type != null) {
            int color = type.getColor();
            return color < 0 ? 0xFFFFFF : color;
        }
        return 0xFFFFFF;
    }
}