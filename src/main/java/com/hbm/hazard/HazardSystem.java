package com.hbm.hazard;

import com.hbm.hazard.modifier.HazardModifier;
import com.hbm.hazard.transformer.HazardTransformerBase;
import com.hbm.hazard.type.HazardTypeBase;
import com.hbm.inventory.RecipesCommon.ComparableStack;
//import com.hbm.interfaces.Untested;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class HazardSystem {

    public static final HashMap<String, HazardData> oreMap = new HashMap<>();
    public static final HashMap<Item, HazardData> itemMap = new HashMap<>();
    public static final HashMap<ComparableStack, HazardData> stackMap = new HashMap<>();
    public static final HashSet<ComparableStack> stackBlacklist = new HashSet<>();
    public static final HashSet<String> dictBlacklist = new HashSet<>();
    public static final List<HazardTransformerBase> trafos = new ArrayList<>();

    public static void register(Object o, HazardData data) {
        if (o instanceof String s) {
            oreMap.put(s, data);
        }
        if (o instanceof Item item) {
            itemMap.put(item, data);
        }
        if (o instanceof ItemStack stack) {
            stackMap.put(new ComparableStack(stack), data);
        }
        if (o instanceof ComparableStack comp) {
            stackMap.put(comp, data);
        }
    }

    public static void blacklist(Object o) {
        if (o instanceof ItemStack stack) {
            stackBlacklist.add(new ComparableStack(stack).makeSingular());
        } else if (o instanceof String s) {
            dictBlacklist.add(s);
        }
    }

    public static boolean isItemBlacklisted(ItemStack stack) {
        ComparableStack comp = new ComparableStack(stack).makeSingular();
        if (stackBlacklist.contains(comp)) {
            return true;
        }

        // TODO: OreDictionary заменён на Forge Tags
        // Если используешь теги, надо будет проверять stack.is(tag)
        return false;
    }

    public static List<HazardEntry> getHazardsFromStack(ItemStack stack) {
        if (isItemBlacklisted(stack)) {
            return new ArrayList<>();
        }

        List<HazardData> chronological = new ArrayList<>();

        // TODO: заменить на Forge Tags
        // пример: if (stack.is(Tags.Items.INGOTS_IRON)) { ... }

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
        // Основной инвентарь
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty()) {
                applyHazards(stack, player);
            }
        }

        // Броня
        for (ItemStack stack : player.getInventory().armor) {
            if (!stack.isEmpty()) {
                applyHazards(stack, player);
            }
        }

        // Оффхенд
        for (ItemStack stack : player.getInventory().offhand) {
            if (!stack.isEmpty()) {
                applyHazards(stack, player);
            }
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
        ItemStack stack = entity.getItem();

        if (entity.isRemoved() || stack.isEmpty()) return;

        List<HazardEntry> hazards = getHazardsFromStack(stack);
        for (HazardEntry entry : hazards) {
            entry.type.updateEntity(entity, HazardModifier.evalAllModifiers(stack, null, entry.baseLevel, entry.mods));
        }
    }

    public static void addFullTooltip(ItemStack stack, Player player, List<Component> list) {
        List<HazardEntry> hazards = getHazardsFromStack(stack);

        for (HazardEntry hazard : hazards) {
            hazard.type.addHazardInformation(player, list, hazard.baseLevel, stack, hazard.mods);
        }
    }
}
