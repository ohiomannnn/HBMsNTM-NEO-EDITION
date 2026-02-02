package com.hbm.blockentity.machine.storage;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.menus.BatterySocketMenu;
import com.hbm.util.fauxpointtwelve.DirPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BatterySocketBlockEntity extends BatteryBaseBlockEntity {

    public long[] log = new long[20];
    public long delta = 0;

    public long syncPower = 0;
    public long syncMaxPower = 0;
    public ItemStack syncStack = ItemStack.EMPTY;

    public BatterySocketBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.BATTERY_SOCKET.get(), pos, blockState, 1);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.batterySocket"); }

    @Override
    public void updateEntity() {
        long prevPower = this.getPower();

        super.updateEntity();

        long avg = (this.getPower() + prevPower) / 2;
        this.delta = avg - this.log[0];

        for (int i = 1; i < this.log.length; i++) {
            this.log[i - 1] = this.log[i];
        }

        this.log[19] = avg;
    }

    public static void serverTick(Level ignored, BlockPos ignored1, BlockState ignored2, BatterySocketBlockEntity be) { be.updateEntity(); }

    @Override
    public void serialize(ByteBuf buf, RegistryAccess registryAccess) {
        super.serialize(buf, registryAccess);
        buf.writeLong(this.delta);
        buf.writeLong(this.getPower());
        buf.writeLong(this.getMaxPower());
        buf.writeInt(Item.getId(slots.get(0).getItem()));
    }

    @Override
    public void deserialize(ByteBuf buf, RegistryAccess registryAccess) {
        super.deserialize(buf, registryAccess);
        this.delta = buf.readLong();
        this.syncPower = buf.readLong();
        this.syncMaxPower = buf.readLong();
        this.syncStack = new ItemStack(Item.byId(buf.readInt()), 1);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        if (itemStack.getItem() instanceof IBatteryItem batteryItem) {
            if (i == mode_input && batteryItem.getCharge(itemStack) == 0) return true;
            if (i == mode_output && batteryItem.getCharge(itemStack) == batteryItem.getMaxCharge(itemStack)) return true;
        }
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) { return new int[] {0}; }

    @Override public long getPower() { return powerFromStack(slots.getFirst()); }
    @Override public long getMaxPower() { return maxPowerFromStack(slots.getFirst()); }

    @Override
    public void setPower(long power) {
        if (slots.getFirst().isEmpty() || !(slots.getFirst().getItem() instanceof IBatteryItem batteryItem)) return;
        batteryItem.setCharge(slots.getFirst(), power);
    }

    public static long powerFromStack(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof IBatteryItem batteryItem)) return 0;
        return batteryItem.getCharge(stack);
    }

    public static long maxPowerFromStack(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof IBatteryItem batteryItem)) return 0;
        return batteryItem.getMaxCharge(stack);
    }

    @Override
    public long getProviderSpeed() {
        if (slots.getFirst().isEmpty() || !(slots.getFirst().getItem() instanceof IBatteryItem batteryItem)) return 0;
        int mode = this.getRelevantMode(true);
        return mode == mode_output || mode == mode_buffer ? batteryItem.getDischargeRate(slots.getFirst()) : 0;
    }
    @Override
    public long getReceiverSpeed() {
        if (slots.getFirst().isEmpty() || !(slots.getFirst().getItem() instanceof IBatteryItem batteryItem)) return 0;
        int mode = this.getRelevantMode(true);
        return mode == mode_input || mode == mode_buffer ? batteryItem.getChargeRate(slots.getFirst()) : 0;
    }

    @Override
    public BlockPos[] getPortPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise();

        BlockPos pos = this.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int dirX = dir.getStepX();
        int dirZ = dir.getStepZ();
        int rotX = rot.getStepX();
        int rotZ = rot.getStepZ();

        return new BlockPos[] {
                new BlockPos(x, y, z),
                new BlockPos(x - dirX, y, z - dirZ),
                new BlockPos(x + rotX, y, z + rotZ),
                new BlockPos(x - dirX + rotX, y, z - dirZ + rotZ)
        };
    }

    @Override
    public DirPos[] getConPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getClockWise();

        BlockPos pos = this.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int dirX = dir.getStepX();
        int dirZ = dir.getStepZ();
        int rotX = rot.getStepX();
        int rotZ = rot.getStepZ();

        return new DirPos[] {
                new DirPos(x + dirX, y, z + dirZ, dir),
                new DirPos(x + dirX + rotX, y, z + dirZ + rotZ, dir),
                new DirPos(x - dirX * 2, y, z - dirZ * 2, dir.getOpposite()),
                new DirPos(x - dirX * 2 + rotX, y, z - dirZ * 2 + rotZ, dir.getOpposite()),
                new DirPos(x + rotX * 2, y, z + rotZ * 2, rot),
                new DirPos(x + rotX * 2 - dirX, y, z + rotZ * 2 - dirZ, rot),
                new DirPos(x - rotX, y, z - rotZ, rot.getOpposite()),
                new DirPos(x - rotX - dirX, y, z - rotZ - dirZ, rot.getOpposite())
        };
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BatterySocketMenu(id, inventory, this);
    }
}
