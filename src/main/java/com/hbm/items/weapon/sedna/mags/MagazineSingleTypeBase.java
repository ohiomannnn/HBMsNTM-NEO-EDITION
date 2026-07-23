package com.hbm.items.weapon.sedna.mags;

import com.hbm.items.weapon.sedna.BulletConfig;
import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.particle.SpentCasing;
import com.hbm.util.BobMathUtil;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Base class for typical magazines, i.e. ones that hold bullets, shells, grenades, etc, any ammo item. Stores a single type of BulletConfigs */
public abstract class MagazineSingleTypeBase implements IMagazine<BulletConfig> {

    public static final String KEY_MAG_COUNT = "magcount";
    public static final String KEY_MAG_TYPE = "magtype";
    public static final String KEY_MAG_PREV = "magprev";
    public static final String KEY_MAG_AFTER = "magafter";

    public List<BulletConfig> acceptedBullets = new ArrayList<>();

    /** A number so the gun tell multiple mags apart */
    public int index;
    /** How much ammo this mag can hold */
    public int capacity;

    public MagazineSingleTypeBase(int index, int capacity) {
        this.index = index;
        this.capacity = capacity;
    }

    public MagazineSingleTypeBase addConfigs(BulletConfig... cfgs) { acceptedBullets.addAll(Arrays.asList(cfgs)); return this; }

    @Override
    public BulletConfig getType(ItemStack stack, Container container) {
        int type = getMagType(stack, index);
        if(type >= 0 && type < BulletConfig.configs.size()) {
            BulletConfig cfg = BulletConfig.configs.get(type);
            if(acceptedBullets.contains(cfg)) return cfg;
            return acceptedBullets.get(0);
        }
        return null;
    }

    @Override
    public void setType(ItemStack stack, BulletConfig type) {
        int i = BulletConfig.configs.indexOf(type);
        if(i >= 0) setMagType(stack, index, i);
    }

    @Override
    public ItemStack getIconForHUD(ItemStack stack, Player player) {
        BulletConfig config = this.getType(stack, player.inventory);
        if(config != null) return new ItemStack(config.ammo);
        return null;
    }

    @Override
    public String reportAmmoStateForHUD(ItemStack stack, Player player) {
        return getAmount(stack, player.inventory) + " / " + getCapacity(stack);
    }

    @Override
    public SpentCasing getCasing(ItemStack stack, Container container) {
        return this.getType(stack, container).casing;
    }

    @Override
    public void useUpAmmo(ItemStack stack, Container container, int amount) {
        if(!IMagazine.shouldUseUpTrenchie(container) && getCapacity(stack) != 1) return;
        this.setAmount(stack, this.getAmount(stack, container) - amount);
        IMagazine.handleAmmoBag(container, this.getType(stack, container), amount);
    }

    /** Returns true if the player has the same ammo if partially loaded, or any valid ammo if not */
    @Override
    public boolean canReload(ItemStack stack, Container container) {
        if(this.getAmount(stack, container) >= this.getCapacity(stack)) return false;
        if(container == null) return true;
        BulletConfig nextConfig = getFirstConfig(stack, container);
        return nextConfig != null;
    }

    public void standardReload(ItemStack stack, Container container, int loadLimit) {

        if(container == null) {
            BulletConfig config = this.getType(stack, container);
            if(config == null) { config = this.acceptedBullets.get(0); this.setType(stack, config); } //fixing broken NBT
            this.setAmount(stack, this.capacity);
            return;
        }

        for(int i = 0; i < container.getContainerSize(); i++) {
            ItemStack slot = container.getItem(i);

            if(loadLimit <= 0) return;

            if(!slot.isEmpty()) {

                //mag is empty, assume next best type
                if(this.getAmount(stack, null) == 0) {

                    for(BulletConfig config : this.acceptedBullets) {
                        if(ItemStack.isSameItem(new ItemStack(config.ammo), slot)) {
                            this.setType(stack, config);
                            int wantsToLoad = (int) Math.ceil((double) this.getCapacity(stack) / (double) config.ammoReloadCount);
                            int toLoad = BobMathUtil.min(wantsToLoad, slot.getCount(), loadLimit);
                            this.setAmount(stack, Math.min(toLoad * config.ammoReloadCount, this.capacity));
                            container.removeItem(i, toLoad);
                            loadLimit -= toLoad;
                            break;
                        }
                    }
                    //mag has a type set, only load that
                } else {
                    BulletConfig config = this.getType(stack, null);
                    if(config == null) { config = this.acceptedBullets.get(0); this.setType(stack, config); } //fixing broken NBT

                    if(ItemStack.isSameItem(new ItemStack(config.ammo), slot)) {
                        int alreadyLoaded = this.getAmount(stack, null);
                        int wantsToLoad = (int) Math.ceil((double) (this.getCapacity(stack) - alreadyLoaded) / (double) config.ammoReloadCount);
                        int toLoad = BobMathUtil.min(wantsToLoad, slot.getCount(), loadLimit);
                        this.setAmount(stack, Math.min((toLoad * config.ammoReloadCount) + alreadyLoaded, this.capacity));
                        container.removeItem(i, toLoad);
                        loadLimit -= toLoad;
                    }
                }

//                boolean infBag = slot.getItem() == ModItems.ammo_bag_infinite;
//                if(slot.getItem() == ModItems.ammo_bag || infBag) {
//                    InventoryAmmoBag bag = new InventoryAmmoBag(slot);
//
//                    for(int j = 0; j < bag.getSizeInventory(); j++) {
//                        ItemStack bagslot = bag.getStackInSlot(j);
//
//                        if(bagslot != null) {
//
//                            //mag is empty, assume next best type
//                            if(this.getAmount(stack, null) == 0) {
//
//                                for(BulletConfig config : this.acceptedBullets) {
//                                    if(config.ammo.matchesRecipe(bagslot, true)) {
//                                        this.setType(stack, config);
//                                        int wantsToLoad = (int) Math.ceil((double) this.getCapacity(stack) / (double) config.ammoReloadCount);
//                                        int toLoad = BobMathUtil.min(wantsToLoad, infBag ? 9_999 : bagslot.stackSize, loadLimit);
//                                        this.setAmount(stack, Math.min(toLoad * config.ammoReloadCount, this.capacity));
//                                        if(!infBag) bag.decrStackSize(j, toLoad);
//                                        loadLimit -= toLoad;
//                                        break;
//                                    }
//                                }
//                                //mag has a type set, only load that
//                            } else {
//                                BulletConfig config = this.getType(stack, null);
//                                if(config == null) { config = this.acceptedBullets.get(0); this.setType(stack, config); } //fixing broken NBT
//
//                                if(config.ammo.matchesRecipe(bagslot, true)) {
//                                    int alreadyLoaded = this.getAmount(stack, bag);
//                                    int wantsToLoad = (int) Math.ceil((double) (this.getCapacity(stack) - alreadyLoaded) / (double) config.ammoReloadCount);
//                                    int toLoad = BobMathUtil.min(wantsToLoad, infBag ? 9_999 : bagslot.stackSize, loadLimit);
//                                    this.setAmount(stack, Math.min((toLoad * config.ammoReloadCount) + alreadyLoaded, this.capacity));
//                                    if(!infBag) bag.decrStackSize(j, toLoad);
//                                    loadLimit -= toLoad;
//                                }
//                            }
//                        }
//                    }
//                }
            }
        }
    }

    /** Returns the config of the first potential loadable round, either what's already chambered or the first valid one if empty */
    public BulletConfig getFirstConfig(ItemStack stack, Container container) {
        if(container == null) return null;

        for(int i = 0; i < container.getContainerSize(); i++) {
            ItemStack slot = container.getItem(i);

            if(!slot.isEmpty()) {
                if(this.getAmount(stack, null) == 0) {
                    for(BulletConfig config : this.acceptedBullets) {
                        if(ItemStack.isSameItem(new ItemStack(config.ammo), slot)) return config;
                    }
                } else {
                    BulletConfig config = this.getType(stack, null);
                    if(config == null) { config = this.acceptedBullets.get(0); this.setType(stack, config); }
                    if(ItemStack.isSameItem(new ItemStack(config.ammo), slot)) return config;
                }

//                if(slot.getItem() == ModItems.ammo_bag || slot.getItem() == ModItems.ammo_bag_infinite) {
//                    InventoryAmmoBag bag = new InventoryAmmoBag(slot);
//
//                    for(int j = 0; j < bag.getSizeInventory(); j++) {
//                        ItemStack bagslot = bag.getStackInSlot(j);
//
//                        if(bagslot != null) {
//                            if(this.getAmount(stack, null) == 0) {
//                                for(BulletConfig config : this.acceptedBullets) {
//                                    if(config.ammo.matchesRecipe(bagslot, true)) return config;
//                                }
//                            } else {
//                                BulletConfig config = this.getType(stack, null);
//                                if(config == null) { config = this.acceptedBullets.get(0); this.setType(stack, config); }
//                                if(config.ammo.matchesRecipe(bagslot, true)) return config;
//                            }
//                        }
//                    }
//                }
            }
        }

        return null;
    }

    @Override public void initNewType(ItemStack stack, Container container) {
        if(container == null) return;
        BulletConfig nextConfig = getFirstConfig(stack, container);
        if(nextConfig != null) {
            int i = BulletConfig.configs.indexOf(nextConfig);
            this.setMagType(stack, index, i);
        }
    }

    @Override public int getCapacity(ItemStack stack) { return capacity; }
    @Override public int getAmount(ItemStack stack, Container container) { return getMagCount(stack, index); }
    @Override public void setAmount(ItemStack stack, int amount) { setMagCount(stack, index, Math.max(amount, 0)); }

    @Override public void setAmountBeforeReload(ItemStack stack, int amount) { GunBaseNTItem.setValueInt(stack, KEY_MAG_PREV + index, amount); }
    @Override public int getAmountBeforeReload(ItemStack stack) { return GunBaseNTItem.getValueInt(stack, KEY_MAG_PREV + index); }
    @Override public void setAmountAfterReload(ItemStack stack, int amount) { GunBaseNTItem.setValueInt(stack, KEY_MAG_AFTER + index, amount); }
    @Override public int getAmountAfterReload(ItemStack stack) { return GunBaseNTItem.getValueInt(stack, KEY_MAG_AFTER + index); }

    // MAG TYPE //
    public static int getMagType(ItemStack stack, int index) { return GunBaseNTItem.getValueInt(stack, KEY_MAG_TYPE + index); }
    public static void setMagType(ItemStack stack, int index, int value) { GunBaseNTItem.setValueInt(stack, KEY_MAG_TYPE + index, value); }

    // MAG COUNT //
    public static int getMagCount(ItemStack stack, int index) { return GunBaseNTItem.getValueInt(stack, KEY_MAG_COUNT + index); }
    public static void setMagCount(ItemStack stack, int index, int value) { GunBaseNTItem.setValueInt(stack, KEY_MAG_COUNT + index, value); }
}
