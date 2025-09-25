package com.hbm.handler;

import com.hbm.items.armor.ItemArmorMod;
import com.hbm.util.TagsUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class ArmorModHandler {

    private static Level level;

    public static final int helmet_only = 0;
    public static final int plate_only = 1;
    public static final int legs_only = 2;
    public static final int boots_only = 3;
    public static final int servos = 4;
    public static final int cladding = 5;
    public static final int kevlar = 6;
    public static final int extra = 7;
    public static final int battery = 8;

    public static final int MOD_SLOTS = 9;

    public static final UUID[] UUIDs = new UUID[] {
            UUID.fromString("8d6e5c77-133e-4056-9c80-a9e42a1a0b65"),
            UUID.fromString("b1b7ee0e-1d14-4400-8037-f7f2e02f21ca"),
            UUID.fromString("30b50d2a-4858-4e5b-88d4-3e3612224238"),
            UUID.fromString("426ee0d0-7587-4697-aaef-4772ab202e78")
    };

    public static final UUID[] fixedUUIDs = new UUID[] {
            UUID.fromString("e572caf4-3e65-4152-bc79-c4d4048cbd29"),
            UUID.fromString("bed30902-8a6a-4769-9f65-2a9b67469fff"),
            UUID.fromString("baebf7b3-1eda-4a14-b233-068e2493e9a2"),
            UUID.fromString("28016c1b-d992-4324-9409-a9f9f0ffb85c")
    };

    public static final String MOD_COMPOUND_KEY = "ntm_armor_mods";
    public static final String MOD_SLOT_KEY = "mod_slot_";

    public static boolean isApplicable(ItemStack armor, ItemStack mod) {

        if (!(armor.getItem() instanceof ArmorItem armorItem)) return false;
        if (!(mod.getItem() instanceof ItemArmorMod aMod)) return false;

        return switch (armorItem.getEquipmentSlot()) {
            case HEAD -> aMod.helmet;
            case CHEST -> aMod.chestplate;
            case LEGS -> aMod.leggings;
            case FEET -> aMod.boots;
            default -> false;
        };
    }


    public static void applyMod(ItemStack armor, ItemStack mod) {
        CompoundTag nbt = TagsUtil.getOrCreateTag(armor);
        CompoundTag mods = nbt.getCompound(MOD_COMPOUND_KEY);

        if (!(mod.getItem() instanceof ItemArmorMod aMod)) return;

        int slot = aMod.type;

        CompoundTag saved = (CompoundTag) mod.save(level.registryAccess());
        mods.put(MOD_SLOT_KEY + slot, saved);

        nbt.put(MOD_COMPOUND_KEY, mods);
    }

    public static void removeMod(ItemStack armor, int slot) {
        if (armor.isEmpty()) return;

        CompoundTag nbt = TagsUtil.getOrCreateTag(armor);
        CompoundTag mods = nbt.getCompound(MOD_COMPOUND_KEY);
        mods.remove(MOD_SLOT_KEY + slot);

        if (mods.isEmpty()) clearMods(armor);
    }

    public static void clearMods(ItemStack armor) {
        if (!TagsUtil.hasTag(armor)) return;
        TagsUtil.removeTag(armor, MOD_COMPOUND_KEY);
    }

    public static boolean hasMods(ItemStack armor) {
        if (TagsUtil.hasTag(armor)) return false;
        return TagsUtil.contains(armor, MOD_COMPOUND_KEY);
    }

    public static ItemStack[] pryMods(ItemStack armor) {
        ItemStack[] slots = new ItemStack[MOD_SLOTS];
        if (!hasMods(armor)) return slots;

        CompoundTag nbt = TagsUtil.getTag(armor);
        CompoundTag mods = nbt.getCompound(MOD_COMPOUND_KEY);

        for (int i = 0; i < MOD_SLOTS; i++) {
            if (mods.contains(MOD_SLOT_KEY + i)) {
                CompoundTag cmp = mods.getCompound(MOD_SLOT_KEY + i);
                ItemStack stack = ItemStack.parse(level.registryAccess(), cmp).orElse(ItemStack.EMPTY);
                if (!stack.isEmpty()) {
                    slots[i] = stack;
                } else {
                    removeMod(armor, i);
                }
            }
        }
        return slots;
    }

    public static ItemStack pryMod(ItemStack armor, int slot) {
        if (!hasMods(armor)) return ItemStack.EMPTY;

        CompoundTag tag = TagsUtil.getTag(armor);
        CompoundTag mods = tag.getCompound(MOD_COMPOUND_KEY);
        CompoundTag cmp = mods.getCompound(MOD_SLOT_KEY + slot);
        ItemStack stack = ItemStack.parse(level.registryAccess(), cmp).orElse(ItemStack.EMPTY);
        if (!stack.isEmpty()) return stack;

        removeMod(armor, slot);
        return ItemStack.EMPTY;
    }
}
