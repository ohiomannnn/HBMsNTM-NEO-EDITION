package com.hbm.util;

import net.minecraft.world.entity.EquipmentSlot;

public class ArmorUtil {

    private static final EquipmentSlot[] SLOT_MAP = {
            EquipmentSlot.FEET,   // 0
            EquipmentSlot.LEGS,   // 1
            EquipmentSlot.CHEST,  // 2
            EquipmentSlot.HEAD    // 3
    };

    public static EquipmentSlot toEquipmentSlot(int oldSlot) {
        if (oldSlot < 0 || oldSlot >= SLOT_MAP.length) {
            throw new IllegalArgumentException("Illegal armor slot: " + oldSlot);
        }
        return SLOT_MAP[oldSlot];
    }
}
