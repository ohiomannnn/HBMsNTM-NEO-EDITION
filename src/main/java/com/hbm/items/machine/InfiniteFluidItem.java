package com.hbm.items.machine;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.ModItems;
import net.minecraft.world.item.Item;

public class InfiniteFluidItem extends Item {

    private FluidType type;
    private int amount;
    private int chance;

    public InfiniteFluidItem(Properties properties, FluidType type, int amount) {
        this(properties, type, amount, 1);
    }

    public InfiniteFluidItem(Properties properties, FluidType type, int amount, int chance) {
        super(properties);
        this.type = type;
        this.amount = amount;
        this.chance = chance;
    }

    public FluidType getType() { return this.type; }
    public int getAmount() { return this.amount; }
    public int getChance() { return this.chance; }
    public boolean allowPressure(int pressure) { return this == ModItems.FLUID_BARREL_INFINITE.get() || pressure == 0; }
}
