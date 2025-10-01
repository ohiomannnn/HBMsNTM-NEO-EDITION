package com.hbm.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ArmorUtil {
    public void register() {

    }

    public static boolean checkArmor(LivingEntity entity, Item... armor) {

        for(int i = 0; i < 4; i++) {
            if(!checkArmorPiece(entity, armor[i], 3 - i))
                return false;
        }

        return true;
    }

//    public static void addGasMaskTooltip(ItemStack mask, Player player, List list, boolean ext) {
//
//        if(mask == null || !(mask.getItem() instanceof IGasMask))
//            return;
//
//        ItemStack filter = ((IGasMask)mask.getItem()).getFilter(mask, player);
//
//        if(filter == null) {
//            list.add(EnumChatFormatting.RED + "No filter installed!");
//            return;
//        }
//
//        list.add(EnumChatFormatting.GOLD + "Installed filter:");
//
//        int meta = filter.getItemDamage();
//        int max = filter.getMaxDamage();
//
//        String append = "";
//
//        if(max > 0) {
//            append = " (" + ((max - meta) * 100 / max) + "%)";
//        }
//
//        List<String> lore = new ArrayList<>();
//        list.add("  " + filter.getDisplayName() + append);
//        filter.getItem().addInformation(filter, player, lore, ext);
//        ForgeEventFactory.onItemTooltip(filter, player, lore, ext);
//        lore.forEach(x -> list.add(EnumChatFormatting.YELLOW + "  " + x));
//    }

    public static boolean checkArmorPiece(LivingEntity entity, Item armor, int slot) {
        EquipmentSlot eqSlot = toEquipmentSlot(slot);
        ItemStack stack = entity.getItemBySlot(eqSlot);
        return !stack.isEmpty() && stack.getItem() == armor;
    }

    public static void damageSuit(LivingEntity entity, int slot, int amount) {
        EquipmentSlot eqSlot = toEquipmentSlot(slot);
        ItemStack stack = entity.getItemBySlot(eqSlot);

        if(stack == ItemStack.EMPTY)
            return;

        stack.hurtAndBreak(amount, entity, eqSlot);
    }

    public static boolean checkArmorNull(LivingEntity entity, int slot) {
        EquipmentSlot eqSlot = toEquipmentSlot(slot);
        return entity.getItemBySlot(eqSlot).isEmpty();
    }

    private static final EquipmentSlot[] SLOT_MAP = {
            EquipmentSlot.FEET,   // 0
            EquipmentSlot.LEGS,   // 1
            EquipmentSlot.CHEST,  // 2
            EquipmentSlot.HEAD    // 3
    };

    protected static EquipmentSlot toEquipmentSlot(int oldSlot) {
        if (oldSlot < 0 || oldSlot >= SLOT_MAP.length) {
            throw new IllegalArgumentException("Illegal armor slot: " + oldSlot);
        }
        return SLOT_MAP[oldSlot];
    }
}
