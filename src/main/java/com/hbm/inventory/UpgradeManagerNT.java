package com.hbm.inventory;

import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.stream.Collectors;

/*
 Steps for use:
 1. TE implements IUpgradeInfoProvider
 2. TE creates a new instance of UpgradeManagerNT
 3. Upgrades and their levels can then be pulled from there.
 */

/**
 * Upgrade system, now with caching!
 * @author BallOfEnergy1
 */
public class UpgradeManagerNT {

    public BlockEntity owner;
    public NonNullList<ItemStack> cachedSlots;

    private UpgradeType mutexType;
    public HashMap<UpgradeType, Integer> upgrades = new HashMap<>();

    public UpgradeManagerNT(BlockEntity be) { this.owner = be; }

    public void checkSlots(NonNullList<ItemStack> slots, int start, int end) { checkSlotsInternal(owner, slots, start, end); }

    private void checkSlotsInternal(BlockEntity be, NonNullList<ItemStack> slots, int start, int end) {

        if(!(be instanceof IUpgradeInfoProvider upgradable)) return;

        NonNullList<ItemStack> upgradeSlots = NonNullList.create();
        upgradeSlots.addAll(slots.subList(start, end + 1));

        if(areStackListsEqual(upgradeSlots, cachedSlots)) return;

        cachedSlots = upgradeSlots.stream().map(ItemStack::copy).collect(Collectors.toCollection(() -> NonNullList.withSize(upgradeSlots.size(), ItemStack.EMPTY)));

        upgrades.clear();

        for (int i = 0; i <= end - start; i++) {

            if(upgradeSlots.get(i).getItem() instanceof MachineUpgradeItem item) {

                if(upgradable.getValidUpgrades() == null) return;

                if(upgradable.getValidUpgrades().containsKey(item.type)) { // Check if upgrade can even be accepted by the machine.
                    if(item.type.mutex) {
                        if(mutexType == null) {
                            upgrades.put(item.type, 1);
                            mutexType = item.type;
                        } else if(item.type.ordinal() > mutexType.ordinal()) {
                            upgrades.remove(mutexType);
                            upgrades.put(item.type, 1);
                            mutexType = item.type;
                        }
                    } else {

                        Integer levelBefore = upgrades.get(item.type);
                        int upgradeLevel = (levelBefore == null ? 0 : levelBefore);
                        upgradeLevel += item.tier;
                        // Add additional check to make sure it doesn't go over the max.
                        upgrades.put(item.type, Math.min(upgradeLevel, upgradable.getValidUpgrades().get(item.type)));
                    }
                }
            }
        }
    }

    private boolean areStackListsEqual(NonNullList<ItemStack> list1, NonNullList<ItemStack> list2) {
        if (list1.size() != list2.size())
            return false;

        for (int i = 0; i < list1.size(); i++) {
            if (!ItemStack.matches(list1.get(i), list2.get(i)))
                return false;
        }

        return true;
    }

    public Integer getLevel(UpgradeType type) {
        return upgrades.getOrDefault(type, 0);
    }
}
