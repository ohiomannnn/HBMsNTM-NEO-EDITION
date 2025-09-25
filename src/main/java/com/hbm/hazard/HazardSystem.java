package com.hbm.hazard;

import com.hbm.HBMsNTM;
import com.hbm.hazard.modifier.HazardModifier;
import com.hbm.hazard.transformer.HazardTransformerBase;
import com.hbm.hazard.type.HazardTypeBase;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.*;

public class HazardSystem {

    public static final HashMap<TagKey<Item>, HazardData> tagMap = new HashMap<>();
    public static final HashMap<Item, HazardData> itemMap = new HashMap<>();
    public static final HashMap<ComparableStack, HazardData> stackMap = new HashMap<>();
    public static final HashSet<ComparableStack> stackBlacklist = new HashSet<>();
    public static final HashSet<TagKey<Item>> tagBlacklist = new HashSet<>();
    public static final List<HazardTransformerBase> trafos = new ArrayList<>();

    public static void register(Object obj, HazardData data) {
        if (obj instanceof Item item) {
            itemMap.put(item, data);
            HBMsNTM.LOGGER.info("obj is item");
        } else if (obj instanceof Block block) {
            itemMap.put(block.asItem(), data);
            HBMsNTM.LOGGER.info("obj is block");
        } else if (obj instanceof ItemStack stack) {
            stackMap.put(new ComparableStack(stack), data);
            HBMsNTM.LOGGER.info("obj is stack");
        } else if (obj instanceof ComparableStack comp) {
            stackMap.put(comp, data);
            HBMsNTM.LOGGER.info("obj is comp");
        }
    }

    public static void blacklist(Object obj) {
        if (obj instanceof TagKey<?> tagKey && tagKey.isFor(Registries.ITEM)) {
            tagBlacklist.add((TagKey<Item>) tagKey);
        } else if (obj instanceof ItemStack stack) {
            stackBlacklist.add(new ComparableStack(stack).makeSingular());
        }
    }

    public static void blacklist(ResourceLocation tagLocation) {
        blacklist(TagKey.create(Registries.ITEM, tagLocation));
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

    @OnlyIn(Dist.CLIENT)
    public static void addFullTooltip(ItemStack stack, List<Component> list) {
        Player player = Minecraft.getInstance().player;
        List<HazardEntry> hazards = getHazardsFromStack(stack);

        for (HazardEntry hazard : hazards) {
            hazard.type.addHazardInformation(player, list, hazard.baseLevel, stack, hazard.mods);
        }
    }
}
