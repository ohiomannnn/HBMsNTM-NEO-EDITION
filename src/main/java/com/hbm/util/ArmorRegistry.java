package com.hbm.util;

import api.hbm.item.IGasMask;
import com.hbm.handler.ArmorModHandler;
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

    public static boolean hasAllProtection(LivingEntity entity, int slot, HazardClass... clazz) {

        if(ArmorUtil.checkArmorNull(entity, slot))
            return false;

        EquipmentSlot eqSlot = ArmorUtil.toEquipmentSlot(slot);
        ItemStack stack = entity.getItemBySlot(eqSlot);

        List<HazardClass> list = getProtectionFromItem(stack, entity);
        return new HashSet<>(list).containsAll(Arrays.asList(clazz));
    }

    public static boolean hasAnyProtection(LivingEntity entity, int slot, HazardClass... clazz) {

        if(ArmorUtil.checkArmorNull(entity, slot))
            return false;

        EquipmentSlot eqSlot = ArmorUtil.toEquipmentSlot(slot);
        ItemStack stack = entity.getItemBySlot(eqSlot);

        List<HazardClass> list = getProtectionFromItem(stack, entity);

        if (list == null)
            return false;

        for (HazardClass haz : clazz) {
            if(list.contains(haz)) return true;
        }

        return false;
    }

    public static boolean hasProtection(LivingEntity entity, int slot, HazardClass clazz) {

        if(ArmorUtil.checkArmorNull(entity, slot))
            return false;

        EquipmentSlot eqSlot = ArmorUtil.toEquipmentSlot(slot);
        ItemStack stack = entity.getItemBySlot(eqSlot);

        List<HazardClass> list = getProtectionFromItem(stack, entity);

        if(list == null)
            return false;

        return list.contains(clazz);
    }

    public static List<HazardClass> getProtectionFromItem(ItemStack stack, LivingEntity entity) {

        List<HazardClass> prot = new ArrayList<>();

        Item item = stack.getItem();

        //if the item has HazardClasses assigned to it, add those
        if (hazardClasses.containsKey(item))
            prot.addAll(hazardClasses.get(item));

        if (item instanceof IGasMask mask) {
            ItemStack filter = mask.getFilter(stack, entity);

            if(filter != null) {
                //add the HazardClasses from the filter, then remove the ones blacklisted by the mask
                List<HazardClass> filProt = (List<HazardClass>) hazardClasses.get(filter.getItem()).clone();

                for(HazardClass c : mask.getBlacklist(stack, entity))
                    filProt.remove(c);

                prot.addAll(filProt);
            }
        }

        if(ArmorModHandler.hasMods(stack)) {

            ItemStack[] mods = ArmorModHandler.pryMods(stack);

            for(ItemStack mod : mods) {

                //recursion! run the exact same procedure on every mod, in case future mods will have filter support
                if(mod != null)
                    prot.addAll(getProtectionFromItem(mod, entity));
            }
        }

        return prot;
    }

    public enum HazardClass {
        GAS_LUNG(Component.translatable("hazard.gasChlorine")),                     //also attacks eyes -> no half mask
        GAS_MONOXIDE(Component.translatable("hazard.gasMonoxide")),                 //only affects lungs
        GAS_INERT(Component.translatable("hazard.gasInert")),					     //SA
        PARTICLE_COARSE(Component.translatable("hazard.particleCoarse")),		     //only affects lungs
        PARTICLE_FINE(Component.translatable("hazard.particleFine")),			     //only affects lungs
        BACTERIA(Component.translatable("hazard.bacteria")),					     //no half masks
        GAS_BLISTERING(Component.translatable("hazard.corrosive")),				 //corrosive substance, also attacks skin
        SAND(Component.translatable("hazard.sand")),							     //blinding sand particles
        LIGHT(Component.translatable("hazard.light"));

        public final Component component;

        HazardClass(Component component) {
            this.component = component;
        }
    }
}
