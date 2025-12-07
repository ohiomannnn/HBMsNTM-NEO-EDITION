package com.hbm.util;

import api.hbm.item.IGasMask;
import com.hbm.handler.ArmorModHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class ArmorRegistry {

    public static HashMap<Item, ArrayList<HazardClass>> hazardClasses = new HashMap<>();

    public static void registerHazard(Item item, HazardClass... hazards) {
        hazardClasses.put(item, new ArrayList<>(Arrays.asList(hazards)));
    }

    public static void addTooltip(List<Component> components, ItemStack stack) {
        List<HazardClass> hazInfo = ArmorRegistry.hazardClasses.get(stack.getItem());

        if (hazInfo != null) {
            if (Screen.hasShiftDown()) {
                components.add(Component.translatable("hazard.protect").withStyle(ChatFormatting.GOLD));
                for (HazardClass clazz : hazInfo) {
                    components.add(Component.translatable(clazz.unlocalizedMessage).withStyle(ChatFormatting.YELLOW));
                }
            } else {
                components.add(
                        Component.literal("Hold <")
                                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
                                .append(Component.literal("LSHIFT").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC))
                                .append(Component.literal("> to display protection info").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC))
                );
            }
        }
    }

    public static boolean hasAllProtection(LivingEntity entity, EquipmentSlot slot, HazardClass... clazz) {

        if (ArmorUtil.checkArmorEmpty(entity, slot))
            return false;

        List<HazardClass> list = getProtectionFromItem(entity.getItemBySlot(slot), entity);
        return new HashSet<>(list).containsAll(Arrays.asList(clazz));
    }

    public static boolean hasAnyProtection(LivingEntity entity, EquipmentSlot slot, HazardClass... clazz) {

        if (ArmorUtil.checkArmorEmpty(entity, slot))
            return false;

        List<HazardClass> list = getProtectionFromItem(entity.getItemBySlot(slot), entity);

        if (list == null)
            return false;

        for (HazardClass haz : clazz) {
            if(list.contains(haz)) return true;
        }

        return false;
    }

    public static boolean hasProtection(LivingEntity entity, EquipmentSlot slot, HazardClass clazz) {

        if (ArmorUtil.checkArmorEmpty(entity, slot))
            return false;

        List<HazardClass> list = getProtectionFromItem(entity.getItemBySlot(slot), entity);

        if (list == null)
            return false;

        return list.contains(clazz);
    }


    public static List<HazardClass> getProtectionFromItem(ItemStack stack, LivingEntity entity) {

        List<HazardClass> prot = new ArrayList<>();

        Item item = stack.getItem();

        //if the item has HazardClasses assigned to it, add those
        if (hazardClasses.containsKey(item)) prot.addAll(hazardClasses.get(item));

        if (item instanceof IGasMask gasMask) {
            ItemStack filter = gasMask.getFilter(stack, entity);

            if (!filter.isEmpty()) {
                // add the HazardClasses from the filter, then remove the ones blacklisted by the mask
                List<HazardClass> filProt = (List<HazardClass>) hazardClasses.get(filter.getItem()).clone();

                for (HazardClass c : gasMask.getBlacklist(stack, entity))
                    filProt.remove(c);

                prot.addAll(filProt);
            }
        }

        if (ArmorModHandler.hasMods(stack)) {

            ItemStack[] mods = ArmorModHandler.pryMods(entity.level(), stack);

            for (ItemStack mod : mods) {

                //recursion! run the exact same procedure on every mod, in case future mods will have filter support
                if (mod != null) prot.addAll(getProtectionFromItem(mod, entity));
            }
        }

        return prot;
    }

    public enum HazardClass {
        // Also attacks eyes -> no half mask
        GAS_LUNG("hazard.gas_chlorine"),
        // Only affects lungs
        GAS_MONOXIDE("hazard.gas_monoxide"),
        // SA
        GAS_INERT("hazard.gas_inert"),
        // Only affects lungs
        PARTICLE_COARSE("hazard.particle_coarse"),
        // Only affects lungs
        PARTICLE_FINE("hazard.particle_fine"),
        // No half masks
        BACTERIA("hazard.bacteria"),
        // Corrosive substance, also attacks skin
        GAS_BLISTERING("hazard.corrosive"),
        // Blinding sand particles
        SAND("hazard.sand"),
        // Blinding light
        LIGHT("hazard.light");

        public final String unlocalizedMessage;

        HazardClass(String unlocalizedMessage) { this.unlocalizedMessage = unlocalizedMessage; }
    }
}
