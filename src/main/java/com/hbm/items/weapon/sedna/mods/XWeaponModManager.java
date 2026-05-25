package com.hbm.items.weapon.sedna.mods;

import com.google.common.collect.HashBiMap;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.items.weapon.sedna.factory.GunFactory.ModCaliber;
import com.hbm.items.weapon.sedna.factory.GunFactory.ModGeneric;
import com.hbm.items.weapon.sedna.factory.GunFactory.ModSpecial;
import com.hbm.util.TagsUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

public class XWeaponModManager {

    public static final String KEY_MOD_LIST = "KEY_MOD_LIST_";

    /** Mapping of mods to IDs, keep the register order consistent! */
    public static HashBiMap<Integer, IWeaponMod> idToMod = HashBiMap.create();
    /** Mapping of mod items to mod definitions */
    public static HashMap<ComparableStack, WeaponModDefinition> stackToMod = new HashMap<>();
    /** Map for turning individual mods back into their item form, used when uninstaling mods */
    public static HashMap<IWeaponMod, ItemStack> modToStack = new HashMap<>();

    /** Scrapes all upgrades, iterates over them and evaluates the given value. The parent (i.e. holder of the base value)
     * is passed for context (so upgrades can differentiate primary and secondary receivers for example). Passing a null
     * stack causes the base value to be returned. */
    public static <T> T eval(T base, ItemStack stack, String key, Object parent, int cfg) {
        if(stack == null) return base;
        if(!TagsUtil.hasCData(stack)) return base;
        CompoundTag tag = TagsUtil.getCData(stack);

        for(int i : tag.getIntArray(KEY_MOD_LIST + cfg)) {
            IWeaponMod mod = idToMod.get(i);
            if(mod != null) base = mod.eval(base, stack, key, parent);
        }

        return base;
    }

    public static class WeaponModDefinition {

        /** Holds the weapon mod handlers for each given gun. Key null refers to mods that apply to ALL guns that are otherwise not included. */
        public HashMap<ComparableStack, IWeaponMod> modByGun = new HashMap();
        public ItemStack stack;

        public WeaponModDefinition(ItemStack stack) {
            this.stack = stack;
            stackToMod.put(new ComparableStack(stack), this);
        }

//        public WeaponModDefinition(ModGeneric num) {
//            this(new ItemStack(ModItems.weapon_mod_generic, 1, num.ordinal()));
//        }
//
//        public WeaponModDefinition(ModSpecial num) {
//            this(new ItemStack(ModItems.weapon_mod_special, 1, num.ordinal()));
//        }
//
//        public WeaponModDefinition(ModCaliber num) {
//            this(new ItemStack(ModItems.weapon_mod_caliber, 1, num.ordinal()));
//        }

        public WeaponModDefinition addMod(ItemStack gun, IWeaponMod mod) { return addMod(new ComparableStack(gun), mod); }
        public WeaponModDefinition addMod(Item gun, IWeaponMod mod) { return addMod(new ComparableStack(gun), mod); }
        public WeaponModDefinition addMod(Item[] gun, IWeaponMod mod) { for(Item item : gun) addMod(new ComparableStack(item), mod); return this; }
        public WeaponModDefinition addMod(ComparableStack gun, IWeaponMod mod) {
            modByGun.put(gun, mod);
            modToStack.put(mod, stack);
//            if(gun != null) {
//                ItemGunBaseNT nt = (ItemGunBaseNT) gun.item;
//                ComparableStack comp = new ComparableStack(stack);
//                if(!nt.recognizedMods.contains(comp)) nt.recognizedMods.add(comp);
//            }
            return this;
        }

        public WeaponModDefinition addDefault(IWeaponMod mod) {
            return addMod((ComparableStack) null, mod);
        }
    }
}
