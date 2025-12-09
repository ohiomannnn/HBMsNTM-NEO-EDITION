package com.hbm.util;

import api.hbm.item.IGasMask;
import com.hbm.handler.ArmorModHandler;
import com.hbm.util.ArmorRegistry.HazardClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class ArmorUtil {

    /**
     * The less horrifying part
     */
    public static void register() {
        ArmorRegistry.registerHazard(Items.DIAMOND_HELMET, HazardClass.PARTICLE_COARSE, HazardClass.GAS_LUNG);
    }

    public static boolean checkArmor(LivingEntity entity, Item... armor) {
        EquipmentSlot[] slots = {
                EquipmentSlot.FEET,   // 0
                EquipmentSlot.LEGS,   // 1
                EquipmentSlot.CHEST,  // 2
                EquipmentSlot.HEAD    // 3
        };

        for (int i = 0; i < slots.length; i++) {
            if (!checkArmorPiece(entity, armor[i], slots[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkArmorPiece(LivingEntity entity, Item armor, EquipmentSlot slot) {
        return !checkArmorEmpty(entity, slot) && entity.getItemBySlot(slot).getItem() == armor;
    }

    public static boolean checkArmorEmpty(LivingEntity player, EquipmentSlot slot) {
        return player.getItemBySlot(slot).isEmpty();
    }

    /*
     * Default implementations for IGasMask items
     */
    public static final String FILTER_KEY = "hfr_Filter";

    public static void installGasMaskFilter(Level level, ItemStack mask, ItemStack filter) {

        if (mask.isEmpty() || filter.isEmpty()) return;

        CompoundTag tag = TagsUtilDegradation.getTag(mask);

        CompoundTag attach = new CompoundTag();
        filter.save(level.registryAccess(), attach);

        tag.put(FILTER_KEY, attach);

        TagsUtilDegradation.putTag(mask, tag);
    }

    public static void removeFilter(ItemStack mask) {
        if (mask.isEmpty()) return;

        CompoundTag maskTag = TagsUtilDegradation.getTag(mask);

        if (maskTag.contains(FILTER_KEY)) {
            maskTag.remove(FILTER_KEY);

            TagsUtilDegradation.putTag(mask, maskTag);
        }
    }

    /**
     * Grabs the installed filter or the filter of the attachment, used for attachment rendering
     */
    public static ItemStack getGasMaskFilterRecursively(ItemStack mask, LivingEntity entity) {

        ItemStack filter = getGasMaskFilter(entity.level(), mask);

        if (filter.isEmpty() && ArmorModHandler.hasMods(mask)) {

            ItemStack[] mods = ArmorModHandler.pryMods(entity.level(), mask);

            if (mods[ArmorModHandler.HELMET_ONLY].isEmpty() && mods[ArmorModHandler.HELMET_ONLY].getItem() instanceof IGasMask)
                filter = ((IGasMask)mods[ArmorModHandler.HELMET_ONLY].getItem()).getFilter(mods[ArmorModHandler.HELMET_ONLY], entity);
        }

        return filter;
    }

    public static ItemStack getGasMaskFilter(Level level, ItemStack mask) {
        if (mask.isEmpty()) return ItemStack.EMPTY;

        CompoundTag maskTag = TagsUtilDegradation.getTag(mask);

        if (!maskTag.contains(FILTER_KEY)) return ItemStack.EMPTY;

        CompoundTag attach = maskTag.getCompound(FILTER_KEY);

        Optional<ItemStack> optionalStack = ItemStack.parse(level.registryAccess(), attach);

        return optionalStack.orElse(ItemStack.EMPTY);
    }

    public static void damageGasMaskFilter(LivingEntity entity, int damage) {

        ItemStack mask = entity.getItemBySlot(EquipmentSlot.HEAD);

        if (mask.isEmpty()) return;

        if (!(mask.getItem() instanceof IGasMask)) {

            if (ArmorModHandler.hasMods(mask)) {

                ItemStack[] mods = ArmorModHandler.pryMods(entity.level(), mask);

                if (mods[ArmorModHandler.HELMET_ONLY].isEmpty() && mods[ArmorModHandler.HELMET_ONLY].getItem() instanceof IGasMask)
                    mask = mods[ArmorModHandler.HELMET_ONLY];
            }
        }
        damageGasMaskFilter(entity.level(), mask, damage);
    }

    public static void damageGasMaskFilter(Level level, ItemStack mask, int damage) {
        ItemStack filter = getGasMaskFilter(level, mask);

        if (filter.isEmpty()) {
            if (ArmorModHandler.hasMods(mask)) {
                ItemStack[] mods = ArmorModHandler.pryMods(level, mask);

                if (mods[ArmorModHandler.HELMET_ONLY].isEmpty() && mods[ArmorModHandler.HELMET_ONLY].getItem() instanceof IGasMask)
                    filter = getGasMaskFilter(level, mods[ArmorModHandler.HELMET_ONLY]);
            }
        }

        if (filter.isEmpty() || filter.getMaxDamage() == 0)
            return;

        filter.setDamageValue(filter.getDamageValue() + damage);

        if (filter.getDamageValue() > filter.getMaxDamage()) {
            removeFilter(mask);
        } else {
            installGasMaskFilter(level, mask, filter);
        }
    }


    public static boolean isWearingEmptyMask(Player player) {

        ItemStack mask = player.getItemBySlot(EquipmentSlot.HEAD);

        if (mask.isEmpty()) return false;

        if (mask.getItem() instanceof IGasMask) {
            return getGasMaskFilter(player.level(), mask).isEmpty();
        }

        ItemStack mod = ArmorModHandler.pryMods(player.level(), mask)[ArmorModHandler.HELMET_ONLY];

        if (mod != null && mod.getItem() instanceof IGasMask) {
            return getGasMaskFilter(player.level(), mod).isEmpty();
        }

        return false;
    }
}