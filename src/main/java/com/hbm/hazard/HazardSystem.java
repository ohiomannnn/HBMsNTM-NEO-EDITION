package com.hbm.hazard;

import com.hbm.hazard.modifier.HazardModifier;
import com.hbm.hazard.transformer.HazardTransformerBase;
import com.hbm.hazard.type.HazardTypeBase;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class HazardSystem {

    /*
     * Map for OreDict entries, always evaluated first. Avoid registering HazardData with 'doesOverride', as internal order is based on the item's ore dict keys.
     */
    public static final HashMap<TagKey<Item>, HazardData> tagMap = new HashMap<>();
    /*
     * Map for items, either with wildcard meta or stuff that's expected to have a variety of damage values, like tools.
     */
    public static final HashMap<Item, HazardData> itemMap = new HashMap<>();
    /*
     * Very specific stacks with item and meta matching. ComparableStack does not support NBT matching, to scale hazards with NBT please use HazardModifiers.
     */
    public static final HashMap<ComparableStack, HazardData> stackMap = new HashMap<>();
    /*
     * For items that should, for whichever reason, be completely exempt from the hazard system.
     */
    public static final HashSet<ComparableStack> stackBlacklist = new HashSet<>();
    public static final HashSet<TagKey<Item>> tagBlacklist = new HashSet<>();
    /*
     * List of hazard transformers, called in order before and after unrolling all the HazardEntries.
     */
    public static final List<HazardTransformerBase> trafos = new ArrayList<>();

    /**
     * Automatically casts the first parameter and registers it to the HazSys
     */
    @SuppressWarnings("unchecked")
    public static void register(Object o, HazardData data) {

        if (o instanceof TagKey<?> tagKey && tagKey.isFor(Registries.ITEM)) tagMap.put((TagKey<Item>) tagKey, data);

        if (o instanceof Item item)   itemMap.put(item, data);
        if (o instanceof Block block) itemMap.put(block.asItem(), data);

        if (o instanceof ItemStack stack)       stackMap.put(new ComparableStack(stack), data);
        if (o instanceof ComparableStack stack) stackMap.put(stack, data);
    }

    /**
     * Prevents the stack from returning any HazardData
     */
    @SuppressWarnings("unchecked")
    public static void blacklist(Object o) {
        if (o instanceof ItemStack stack)       stackBlacklist.add(new ComparableStack(stack).makeSingular());
        if (o instanceof TagKey<?> tagKey && tagKey.isFor(Registries.ITEM)) tagBlacklist.add((TagKey<Item>) tagKey);
    }

    public static boolean isItemBlacklisted(ItemStack stack) {
        if (stack.isEmpty()) return true;

        ComparableStack comp = new ComparableStack(stack).makeSingular();
        if (stackBlacklist.contains(comp)) {
            return true;
        }

        return stack.getTags().anyMatch(tagBlacklist::contains);
    }

    public static List<HazardEntry> getHazardsFromStack(ItemStack stack) {
        if (isItemBlacklisted(stack)) {
            return new ArrayList<>();
        }

        List<HazardData> chronological = new ArrayList<>();

        stack.getTags().forEach(tagKey -> {
            if (tagMap.containsKey(tagKey)) {
                chronological.add(tagMap.get(tagKey));
            }
        });

        if (itemMap.containsKey(stack.getItem())) {
            chronological.add(itemMap.get(stack.getItem()));
        }

        ComparableStack comp = new ComparableStack(stack).makeSingular();
        if (stackMap.containsKey(comp)) {
            chronological.add(stackMap.get(comp));
        }

        List<HazardEntry> entries = new ArrayList<>();

        for (HazardTransformerBase trafo : trafos) {
            trafo.transformPre(stack, entries);
        }

        int mutex = 0;

        for (HazardData data : chronological) {
            if (data.doesOverride) {
                entries.clear();
            }

            if ((data.getMutex() & mutex) == 0) {
                entries.addAll(data.entries);
                mutex |= data.getMutex();
            }
        }

        for (HazardTransformerBase trafo : trafos) {
            trafo.transformPost(stack, entries);
        }

        return entries;
    }

    public static float getHazardLevelFromStack(ItemStack stack, HazardTypeBase hazard) {
        List<HazardEntry> entries = getHazardsFromStack(stack);

        for (HazardEntry entry : entries) {
            if (entry.type == hazard) {
                return HazardModifier.evalAllModifiers(stack, null, entry.baseLevel, entry.mods);
            }
        }
        return 0F;
    }

    public static void applyHazards(ItemStack stack, LivingEntity entity) {
        List<HazardEntry> hazards = getHazardsFromStack(stack);

        for (HazardEntry hazard : hazards) {
            hazard.applyHazard(stack, entity);
        }
    }

    public static void updatePlayerInventory(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty()) applyHazards(stack, player);
        }
        for (ItemStack stack : player.getInventory().armor) {
            if (!stack.isEmpty()) applyHazards(stack, player);
        }
        for (ItemStack stack : player.getInventory().offhand) {
            if (!stack.isEmpty()) applyHazards(stack, player);
        }
    }

    public static void updateLivingInventory(LivingEntity entity) {

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = entity.getItemBySlot(slot);

            if (!stack.isEmpty()) {
                applyHazards(stack, entity);
            }
        }
    }

    public static void updateDroppedItem(ItemEntity entity) {

        if (entity.isRemoved()) return;
        ItemStack stack = entity.getItem();
        if (stack.isEmpty()) return;

        List<HazardEntry> hazards = getHazardsFromStack(stack);
        for (HazardEntry entry : hazards) {
            entry.type.updateEntity(entity, HazardModifier.evalAllModifiers(stack, null, entry.baseLevel, entry.mods));
        }
    }

    /** Only in client! */
    public static void addFullTooltip(ItemStack stack, List<Component> list) {

        Player player = Minecraft.getInstance().player;
        List<HazardEntry> hazards = getHazardsFromStack(stack);

        for (HazardEntry hazard : hazards) {
            hazard.type.addHazardInformation(player, list, hazard.baseLevel, stack, hazard.mods);
        }
    }
}
