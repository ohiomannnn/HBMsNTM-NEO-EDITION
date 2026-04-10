package com.hbm.items.machine;

import net.minecraft.world.item.Item;

public class MachineUpgradeItem extends Item {

    public UpgradeType type;
    public int tier;

    public MachineUpgradeItem(Properties properties, UpgradeType type, int tier) {
        super(properties.stacksTo(1));
        this.type = type;
        this.tier = tier;
    }

    public MachineUpgradeItem(Properties properties, UpgradeType type) {
        this(properties, type, 0);
    }

    public MachineUpgradeItem(Properties properties) {
        this(properties, UpgradeType.SPECIAL, 0);
    }

    public enum UpgradeType {
        SPEED,
        EFFECT,
        POWER,
        FORTUNE,
        AFTERBURN,
        OVERDRIVE,
        SPECIAL,
        LM_DESROYER,
        LM_SCREM,
        LM_SMELTER(true),
        LM_SHREDDER(true),
        LM_CENTRIFUGE(true),
        LM_CRYSTALLIZER(true),
        GS_SPEED;

        public boolean mutex = false;

        UpgradeType() { }

        UpgradeType(boolean mutex) {
            this.mutex = mutex;
        }
    }
}
