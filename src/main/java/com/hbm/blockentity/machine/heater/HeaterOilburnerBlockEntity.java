package com.hbm.blockentity.machine.heater;

import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Flammable;
import com.hbm.items.machine.InfiniteFluidItem;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class HeaterOilburnerBlockEntity extends AbstractHeaterMachineBlockEntity implements IFluidStandardTransceiverMK2, IControlReceiver, IFluidCopiable {

    public static final int maxHeatEnergy = 100_000;

    public final FluidTank tank = new FluidTank(Fluids.OIL_HEATING, 16_000);
    public boolean isOn = true;
    public int setting = 1;

    public HeaterOilburnerBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.HEATER_OILBURNER.get(), pos, state, 3);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.heaterOilburner");
    }

    @Override
    protected int getMaxHeat() {
        return maxHeatEnergy;
    }

    public DirPos[] getConPos() {
        return new DirPos[] {
                new DirPos(this.getBlockPos().getX() + 2, this.getBlockPos().getY(), this.getBlockPos().getZ(), Direction.EAST),
                new DirPos(this.getBlockPos().getX() - 2, this.getBlockPos().getY(), this.getBlockPos().getZ(), Direction.WEST),
                new DirPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() + 2, Direction.SOUTH),
                new DirPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ() - 2, Direction.NORTH)
        };
    }

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        this.tank.loadTank(this.level, 0, 1, this.slots);
        if(this.tank.setType(2, this.slots)) {
            this.setChanged();
        }

        for(DirPos pos : this.getConPos()) {
            this.trySubscribe(this.tank.getTankType(), this.level, pos);
        }

        boolean shouldCool = true;

        if(this.isOn && this.heatEnergy < maxHeatEnergy) {
            if(this.tank.getTankType().hasTrait(FT_Flammable.class)) {
                FT_Flammable trait = this.tank.getTankType().getTrait(FT_Flammable.class);
                int toBurn = Math.min(this.setting, this.tank.getFill());
                if(toBurn > 0) {
                    this.tank.setFill(this.tank.getFill() - toBurn);
                    this.heatEnergy += (int) (trait.getHeatEnergy() / 1000L) * toBurn;
                    shouldCool = false;
                }
            }
        }

        if(this.heatEnergy >= maxHeatEnergy) {
            shouldCool = false;
        }

        if(shouldCool) {
            this.heatEnergy = Math.max(this.heatEnergy - Math.max(this.heatEnergy / 1000, 1), 0);
        }

        this.networkPackNT(25);
    }

    public void toggleSettingUp() {
        this.setting++;
        if(this.setting > 100) this.setting = 1;
        this.setChanged();
    }

    public void toggleSettingDown() {
        this.setting--;
        if(this.setting < 1) this.setting = 100;
        this.setChanged();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new com.hbm.inventory.menus.HeaterOilburnerMenu(id, inventory, this);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 2) {
            return stack.getItem() instanceof com.hbm.items.machine.IItemFluidIdentifier;
        }

        if(slot == 0) {
            return !FluidContainerRegistry.getEmptyContainer(stack).isEmpty()
                    || stack.getItem() instanceof InfiniteFluidItem;
        }

        return false;
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { this.tank };
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return FluidTank.EMPTY_ARRAY;
    }

    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[] { this.tank };
    }

    @Override
    public boolean canConnect(FluidType type, Direction dir) {
        return dir != null && dir.getAxis().isHorizontal();
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.distanceToSqr(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5) <= 256.0D;
    }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("toggle")) {
            this.isOn = !this.isOn;
        }
        if(tag.contains("setting")) {
            this.setting = Math.max(1, Math.min(tag.getInt("setting"), 100));
        }
        this.setChanged();
    }

    @Override
    public CompoundTag getSettings(net.minecraft.world.level.Level level, BlockPos pos) {
        CompoundTag tag = IFluidCopiable.super.getSettings(level, pos);
        tag.putInt("setting", this.setting);
        tag.putBoolean("isOn", this.isOn);
        return tag;
    }

    @Override
    public void pasteSettings(CompoundTag tag, int index, net.minecraft.world.level.Level level, Player player, BlockPos pos) {
        IFluidCopiable.super.pasteSettings(tag, index, level, player, pos);
        if(tag.contains("setting")) this.setting = Math.max(1, Math.min(tag.getInt("setting"), 100));
        if(tag.contains("isOn")) this.isOn = tag.getBoolean("isOn");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        this.tank.serialize(buf);
        buf.writeBoolean(this.isOn);
        buf.writeInt(this.setting);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.tank.deserialize(buf);
        this.isOn = buf.readBoolean();
        this.setting = buf.readInt();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.tank.readFromNBT(tag, "tank");
        this.isOn = tag.getBoolean("isOn");
        this.setting = tag.getInt("setting");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        this.tank.writeToNBT(tag, "tank");
        tag.putBoolean("isOn", this.isOn);
        tag.putInt("setting", this.setting);
    }
}
