package com.hbm.blockentity;

import api.hbm.energymk2.IEnergyConductorMK2;
import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidReceiverMK2;
import api.hbm.redstoneoverradio.IRORInfo;
import api.hbm.redstoneoverradio.IRORInteractive;
import api.hbm.redstoneoverradio.IRORValueProvider;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ProxyComboBlockEntity extends ProxyBaseBlockEntity implements IEnergyReceiverMK2, IEnergyConductorMK2, WorldlyContainer, IFluidReceiverMK2, IRORValueProvider, IRORInteractive {

    private BlockEntity be;
    private boolean inventory;
    private boolean power;
    private boolean conductor;
    private boolean fluid;
    private boolean heat;
    public boolean moltenMetal;

    public ProxyComboBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PROXY_COMBO.get(), pos, blockState);
    }

    public ProxyComboBlockEntity inventory() {
        this.inventory = true;
        return this;
    }

    public ProxyComboBlockEntity power() {
        this.power = true;
        return this;
    }
    public ProxyComboBlockEntity conductor() {
        this.conductor = true;
        return this;
    }
    public ProxyComboBlockEntity moltenMetal() {
        this.moltenMetal = true;
        return this;
    }
    public ProxyComboBlockEntity fluid() {
        this.fluid = true;
        return this;
    }

    public ProxyComboBlockEntity heatSource() {
        this.heat = true;
        return this;
    }

    /** Returns the actual tile entity that represents the core. Only for internal use, and EnergyControl. */
    public BlockEntity getBlockEntity() {
        if (be == null || be.isRemoved() || (be instanceof LoadedBaseBlockEntity loadedBaseBE && !loadedBaseBE.isLoaded)) {
            be = this.getBE();
        }
        return be;
    }

    /** Returns the core tile entity, or a delegate object. */
    protected Object getCoreObject() {
        return this.getBlockEntity();
    }

    @Override
    public void setPower(long i) {
        if (!this.power) return;

        if (getCoreObject() instanceof IEnergyReceiverMK2 rec) {
            rec.setPower(i);
        }
    }

    @Override
    public long getPower() {
        if (!this.power) return 0;

        if (this.getCoreObject() instanceof IEnergyReceiverMK2 rec) {
            return rec.getPower();
        }

        return 0;
    }

    @Override
    public long getMaxPower() {
        if (!this.power) return 0;

        if (this.getCoreObject() instanceof IEnergyReceiverMK2 rec) {
            return rec.getMaxPower();
        }

        return 0;
    }

    @Override
    public long transferPower(long power) {
        if (!this.power) return power;

        if (this.getCoreObject() instanceof IEnergyReceiverMK2 rec) {
            return rec.transferPower(power);
        }

        return power;
    }

    @Override
    public boolean canConnect(Direction dir) {

        if (this.power && this.getCoreObject() instanceof IEnergyReceiverMK2 rec) {
            return rec.canConnect(dir);
        }

        if (this.conductor && this.getCoreObject() instanceof IEnergyConductorMK2 con) {
            return con.canConnect(dir);
        }

        return true;
    }

    @Override
    public String[] getFunctionInfo() {
        if (this.getCoreObject() instanceof IRORInfo info) return info.getFunctionInfo();
        return new String[0];
    }

    @Override
    public String provideRORValue(String name) {
        if (this.getCoreObject() instanceof IRORValueProvider provider) return provider.provideRORValue(name);
        return null;
    }

    @Override
    public String runRORFunction(String name, String[] params) {
        if (this.getCoreObject() instanceof IRORInteractive interactive) return interactive.runRORFunction(name, params);
        return null;
    }

    public static final FluidTank[] EMPTY_TANKS = FluidTank.EMPTY_ARRAY;

    @Override
    public FluidTank[] getAllTanks() {
        if (!fluid) return EMPTY_TANKS;

        if(getCoreObject() instanceof IFluidReceiverMK2) {
            return ((IFluidReceiverMK2)getCoreObject()).getAllTanks();
        }

        return EMPTY_TANKS;
    }

    @Override
    public long transferFluid(FluidType type, int pressure, long amount) {
        if (!fluid) return amount;

        if (getCoreObject() instanceof IFluidReceiverMK2 rec) {
            return rec.transferFluid(type, pressure, amount);
        }

        return amount;
    }

    @Override
    public long getDemand(FluidType type, int pressure) {
        if (!fluid) return 0;

        if (getCoreObject() instanceof IFluidReceiverMK2 rec) {
            return rec.getDemand(type, pressure);
        }

        return 0;
    }

    @Override
    public int getContainerSize() {
        if (!inventory) return 0;

        if (getCoreObject() instanceof Container container) {
            return container.getContainerSize();
        }

        return 0;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (!inventory) return ItemStack.EMPTY;

        if (getCoreObject() instanceof Container container) {
            return container.getItem(slot);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (!inventory) return ItemStack.EMPTY;

        if (getCoreObject() instanceof Container container) {
            return container.removeItem(slot, amount);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (!inventory) return ItemStack.EMPTY;

        if (getCoreObject() instanceof Container container) {
            return container.removeItemNoUpdate(slot);
        }

        return ItemStack.EMPTY;
    }


    @Override
    public void setItem(int slot, ItemStack itemStack) {
        if (!inventory) return;

        if (getCoreObject() instanceof Container container) {
            container.setItem(slot, itemStack);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (!inventory) return false;

        if (getCoreObject() instanceof Container container) {
            return container.stillValid(player);
        }

        return false;
    }

    @Override
    public void startOpen(Player player) {
        if (!inventory) return;

        if (getCoreObject() instanceof Container container) {
            container.startOpen(player);
        }
    }
    @Override
    public void stopOpen(Player player) {
        if (!inventory) return;

        if (getCoreObject() instanceof Container container) {
            container.stopOpen(player);
        }
    }

    @Override
    public boolean isEmpty() {
        if (!inventory) return false;

        if (getCoreObject() instanceof Container container) {
            container.isEmpty();
        }

        return false;
    }

    @Override
    public void clearContent() {
        if (!inventory) return;

        if (getCoreObject() instanceof Container container) {
            container.clearContent();
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (!inventory) return false;

        if (getCoreObject() instanceof Container container) {

            if (getCoreObject() instanceof IConditionalInvAccess) return ((IConditionalInvAccess) getCoreObject()).isItemValidForSlot(this.getBlockPos(), slot, stack);

            return container.canPlaceItem(slot, stack);
        }

        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (!inventory) return new int[0];

        if (getCoreObject() instanceof WorldlyContainer container) {

            if (getCoreObject() instanceof IConditionalInvAccess) return ((IConditionalInvAccess) getCoreObject()).getAccessibleSlotsFromSide(this.getBlockPos(), direction);

            return container.getSlotsForFace(direction);
        }

        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        if (!inventory) return false;

        if (getCoreObject() instanceof WorldlyContainer container) {

            if(getCoreObject() instanceof IConditionalInvAccess) return ((IConditionalInvAccess) getCoreObject()).canInsertItem(this.getBlockPos(), slot, itemStack, direction);

            return container.canPlaceItemThroughFace(slot, itemStack, direction);
        }

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        if (!inventory) return false;

        if (getCoreObject() instanceof WorldlyContainer container) {

            if (getCoreObject() instanceof IConditionalInvAccess) return ((IConditionalInvAccess) getCoreObject()).canExtractItem(this.getBlockPos(), slot, itemStack, direction);

            return container.canTakeItemThroughFace(slot, itemStack, direction);
        }

        return false;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.inventory = tag.getBoolean("Inventory");
        this.power = tag.getBoolean("Power");
        this.conductor = tag.getBoolean("Conductor");
        this.fluid = tag.getBoolean("Fluid");
        this.moltenMetal = tag.getBoolean("MoltenMetal");
        this.heat = tag.getBoolean("Heat");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putBoolean("Inventory", inventory);
        tag.putBoolean("Power", power);
        tag.putBoolean("Conductor", conductor);
        tag.putBoolean("Fluid", fluid);
        tag.putBoolean("MoltenMetal", moltenMetal);
        tag.putBoolean("Heat", heat);
    }
}
