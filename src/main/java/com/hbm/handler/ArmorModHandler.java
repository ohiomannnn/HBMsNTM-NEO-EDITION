package com.hbm.handler;

import com.hbm.items.armor.ItemArmorMod;
import com.hbm.util.TagsUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class ArmorModHandler {

    public static final int HELMET_ONLY = 0;
    public static final int PLATE_ONLY = 1;
    public static final int LEGS_ONLY = 2;
    public static final int BOOTS_ONLY = 3;
    public static final int SERVOS = 4;
    public static final int CLADDING = 5;
    public static final int KEVLAR = 6;
    public static final int EXTRA = 7;
    public static final int BATTERY = 8;

    public static final int MOD_SLOTS = 9;

    //The key for the NBTTagCompound that holds the armor mods
    public static final String MOD_COMPOUND_KEY = "ntm_armor_mods";
    //The key for the specific slot inside the armor mod NBT Tag
    public static final String MOD_SLOT_KEY = "mod_slot_";

    /**
     * Checks if a mod can be applied to an armor piece
     * Needs to be used to prevent people from inserting invalid items into the armor table
     */
    public static boolean isApplicable(ItemStack armor, ItemStack mod) {

        if (armor.isEmpty() || mod.isEmpty()) return false;
        if (!(armor.getItem() instanceof ArmorItem aItem)) return false;
        if (!(mod.getItem() instanceof ItemArmorMod aMod)) return false;

        Type type = aItem.getType();

        return (type == Type.HELMET && aMod.helmet) || (type == Type.CHESTPLATE && aMod.chestplate) || (type == Type.LEGGINGS && aMod.leggings) || (type == Type.BOOTS && aMod.boots);
    }

    /**
     * Applies a mod to the given armor piece
     * Make sure to check for applicability first
     * Will override present mods so make sure to only use unmodded armor pieces
     */
    public static void applyMod(Level level, ItemStack armor, ItemStack mod) {

        if (!TagsUtil.hasTag(armor)) TagsUtil.setTag(armor, new CompoundTag());
        CompoundTag tag = TagsUtil.getTag(armor);

        if (!tag.contains(MOD_COMPOUND_KEY)) tag.put(MOD_COMPOUND_KEY, new CompoundTag());

        CompoundTag mods = tag.getCompound(MOD_COMPOUND_KEY);

        ItemArmorMod aMod = (ItemArmorMod) mod.getItem();
        int slot = aMod.type;

        CompoundTag cmp = new CompoundTag();
        // REGISTRY ACCESS REGISTRY ACCESS REGISTRY ACCESS REGISTRY ACCESSREGISTRY ACCESS REGISTRY ACCESSREGISTRY ACCESS REGISTRY ACCESS
        mod.save(level.registryAccess(), cmp);

        mods.put(MOD_SLOT_KEY + slot, cmp);
    }

    /**
     * Removes the mod from the given slot
     */
    public static void removeMod(ItemStack armor, int slot) {

        if (armor.isEmpty()) return;

        if (!TagsUtil.hasTag(armor)) TagsUtil.setTag(armor, new CompoundTag());

        CompoundTag tag = TagsUtil.getTag(armor);

        if (!tag.contains(MOD_COMPOUND_KEY)) tag.put(MOD_COMPOUND_KEY, new CompoundTag());

        CompoundTag mods = tag.getCompound(MOD_COMPOUND_KEY);
        mods.remove(MOD_SLOT_KEY + slot);

        if (mods.isEmpty()) clearMods(armor);
    }

    /**
     * Removes ALL mods
     * Should be used when the armor piece is put in the armor table slot AFTER the armor pieces have been separated
     */
    public static void clearMods(ItemStack armor) {

        if (!TagsUtil.hasTag(armor)) return;

        CompoundTag nbt = TagsUtil.getTag(armor);
        nbt.remove(MOD_COMPOUND_KEY);
    }

    /**
     * Does what the name implies. Returns true if the stack has NBT and that NBT has the MOD_COMPOUND_KEY tag.
     */
    public static boolean hasMods(ItemStack armor) {

        if (!TagsUtil.hasTag(armor)) return false;

        CompoundTag nbt = TagsUtil.getTag(armor);
        return nbt.contains(MOD_COMPOUND_KEY);
    }

    /**
     * Gets all the modifications in the provided armor
     */
    public static ItemStack[] pryMods(Level level, ItemStack armor) {

        ItemStack[] slots = new ItemStack[MOD_SLOTS];

        if (!hasMods(armor)) return slots;

        CompoundTag tag = TagsUtil.getTag(armor);
        CompoundTag mods = tag.getCompound(MOD_COMPOUND_KEY);

        for (int i = 0; i < MOD_SLOTS; i++) {

            CompoundTag cmp = mods.getCompound(MOD_SLOT_KEY + i);

            Optional<ItemStack> stack = ItemStack.parse(level.registryAccess(), cmp);

            if (stack.isPresent() && !stack.get().isEmpty()) {
                slots[i] = stack.get();
            } else {
                // Any non-existing armor mods will be sorted out automatically
                removeMod(armor, i);
            }
        }

        return slots;
    }

    public static ItemStack pryMod(Level level, ItemStack armor, int slot) {

        if (!hasMods(armor)) return ItemStack.EMPTY;

        CompoundTag nbt = TagsUtil.getTag(armor);
        CompoundTag mods = nbt.getCompound(MOD_COMPOUND_KEY);
        CompoundTag cmp = mods.getCompound(MOD_SLOT_KEY + slot);
        Optional<ItemStack> stack = ItemStack.parse(level.registryAccess(), cmp);

        if (stack.isPresent() && !stack.get().isEmpty()) return stack.get();

        removeMod(armor, slot);

        return ItemStack.EMPTY;
    }
}
