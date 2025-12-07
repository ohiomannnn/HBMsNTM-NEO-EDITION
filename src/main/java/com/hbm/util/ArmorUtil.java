package com.hbm.util;

import com.hbm.util.ArmorRegistry.HazardClass;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ArmorUtil {

    /*
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
}
