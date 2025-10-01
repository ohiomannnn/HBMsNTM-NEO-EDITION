package com.hbm.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemStackUtil {

    public static ItemStack carefulCopy(ItemStack stack) {
        if (stack == null) return null;
        return stack.copy();
    }

    public static ItemStack carefulCopyWithSize(ItemStack stack, int size) {
        if (stack == null) return null;
        ItemStack copy = stack.copy();
        copy.setCount(size);
        return copy;
    }

    public static ItemStack[] carefulCopyArray(ItemStack[] array) {
        return carefulCopyArray(array, 0, array.length - 1);
    }

    public static ItemStack[] carefulCopyArray(ItemStack[] array, int start, int end) {
        if (array == null) return null;

        ItemStack[] copy = new ItemStack[array.length];
        for (int i = start; i <= end; i++) {
            copy[i] = carefulCopy(array[i]);
        }
        return copy;
    }

    public static ItemStack[] carefulCopyArrayTruncate(ItemStack[] array, int start, int end) {
        if (array == null) return null;

        int length = end - start + 1;
        ItemStack[] copy = new ItemStack[length];
        for (int i = 0; i < length; i++) {
            copy[i] = carefulCopy(array[start + i]);
        }
        return copy;
    }

    public static ItemStack addTooltipToStack(ItemStack stack, String... lines) {
        CompoundTag tag = TagsUtil.getOrCreateTag(stack);
        CompoundTag display = new CompoundTag();
        ListTag lore = new ListTag();

        for (String line : lines) {
            lore.add(StringTag.valueOf("§r§7" + line));
        }

        display.put("Lore", lore);
        tag.put("display", display);
        return stack;
    }

    public static void addStacksToNBT(RegistryAccess registryAccess, ItemStack stack, ItemStack... stacks) {
        CompoundTag tag = TagsUtil.getOrCreateTag(stack);
        ListTag items = new ListTag();

        for (int i = 0; i < stacks.length; i++) {
            if (!stacks[i].isEmpty()) {
                CompoundTag slotNBT = new CompoundTag();
                slotNBT.putByte("slot", (byte) i);
                stacks[i].save(registryAccess, slotNBT);
                items.add(slotNBT);
            }
        }
        tag.put("items", items);
    }

    public static ItemStack[] readStacksFromNBT(ItemStack stack, HolderLookup.Provider provider, int count) {
        if (!TagsUtil.hasTag(stack)) return null;
        CompoundTag tag = TagsUtil.getTag(stack);
        if (!tag.contains("items")) return null;

        ListTag list = tag.getList("items", 10);
        if (count == 0) count = list.size();

        ItemStack[] stacks = new ItemStack[count];

        for (int i = 0; i < list.size(); i++) {
            CompoundTag slotNBT = list.getCompound(i);
            byte slot = slotNBT.getByte("slot");

            ItemStack loadedStack = ItemStack.parse(provider, slotNBT).orElse(ItemStack.EMPTY);
            if (slot >= 0 && slot < stacks.length && !loadedStack.isEmpty()) {
                stacks[slot] = loadedStack;
            }
        }
        return stacks;
    }


    public static ItemStack[] readStacksFromNBT(ItemStack stack, HolderLookup.Provider provider) {
        return readStacksFromNBT(stack, provider, 0);
    }

    public static List<String> getTags(ItemStack stack) {
        List<String> list = new ArrayList<>();
        stack.getTags().forEach(tagKey -> list.add(tagKey.location().toString()));
        return list;
    }

    public static String getModIdFromItemStack(ItemStack stack) {
        return stack.getItem().builtInRegistryHolder().key().location().getNamespace();
    }

    public static void spillItems(Level level, BlockPos pos, Container container, Random rand) {
        for (int slot = 0; slot < container.getContainerSize(); ++slot) {
            ItemStack stack = container.getItem(slot);

            if (!stack.isEmpty()) {
                float oX = rand.nextFloat() * 0.8F + 0.1F;
                float oY = rand.nextFloat() * 0.8F + 0.1F;
                float oZ = rand.nextFloat() * 0.8F + 0.1F;

                while (!stack.isEmpty()) {
                    int count = Math.min(rand.nextInt(21) + 10, stack.getCount());
                    ItemStack drop = stack.split(count);

                    ItemEntity itemEntity = new ItemEntity(level,
                            pos.getX() + oX,
                            pos.getY() + oY,
                            pos.getZ() + oZ,
                            drop);

                    float motion = 0.05F;
                    itemEntity.setDeltaMovement(
                            rand.nextGaussian() * motion,
                            rand.nextGaussian() * motion + 0.2F,
                            rand.nextGaussian() * motion
                    );

                    level.addFreshEntity(itemEntity);
                }
            }
        }
    }

    public static boolean areStacksCompatible(ItemStack a, ItemStack b) {
        return ItemStack.isSameItemSameComponents(a, b);
    }
}
