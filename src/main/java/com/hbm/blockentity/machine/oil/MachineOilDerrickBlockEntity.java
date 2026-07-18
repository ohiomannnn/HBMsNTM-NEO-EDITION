package com.hbm.blockentity.machine.oil;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineOilDerrickMenu;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.util.BobMathUtil;
import com.hbm.util.fauxpointtwelve.DirPos;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;

public class MachineOilDerrickBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IUpgradeInfoProvider {

    private static final int INV_SIZE = 8;

    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    public long power;
    public int indicator;

    public long maxPower = 100_000L;
    public int consumption = 100;
    public int delay = 50;
    public int oilPerDeposit = 500;
    public int gasPerDepositMin = 100;
    public int gasPerDepositMax = 500;
    public double drainChance = 0.05D;

    public int speedLevel;
    public int energyLevel;
    public int afterburnLevel;
    public int overdriveLevel;

    public final FluidTank[] tanks = new FluidTank[] {
            new FluidTank(Fluids.OIL_CRUDE, 64_000),
            new FluidTank(Fluids.GAS, 64_000)
    };

    private final HashSet<BlockPos> processed = new HashSet<>();
    private AABB renderBox;

    public MachineOilDerrickBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_OIL_DERRICK.get(), pos, state, INV_SIZE);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.oil_derrick");
    }

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        this.tanks[0].unloadTank(this.level, 1, 2, this.slots);
        this.tanks[1].unloadTank(this.level, 3, 4, this.slots);

        this.upgradeManager.checkSlots(this.slots, 5, 7);
        this.speedLevel = Math.min(this.upgradeManager.getLevel(UpgradeType.SPEED), 3);
        this.energyLevel = Math.min(this.upgradeManager.getLevel(UpgradeType.POWER), 3);
        this.afterburnLevel = Math.min(this.upgradeManager.getLevel(UpgradeType.AFTERBURN), 3);
        this.overdriveLevel = Math.min(this.upgradeManager.getLevel(UpgradeType.OVERDRIVE), 3);

        int toBurn = Math.min(this.tanks[1].getFill(), this.afterburnLevel * 10);
        if(toBurn > 0) {
            this.tanks[1].setFill(this.tanks[1].getFill() - toBurn);
            this.power += toBurn * 5L;
            if(this.power > this.maxPower) this.power = this.maxPower;
        }

        this.power = Library.chargeTEFromItems(this.slots, 0, this.power, this.maxPower);

        for(DirPos dirPos : this.getConPos()) {
            this.trySubscribe(this.level, dirPos);
            this.trySubscribe(this.tanks[0].getTankType(), this.level, dirPos);
            this.trySubscribe(this.tanks[1].getTankType(), this.level, dirPos);
            this.tryProvide(this.tanks[0], this.level, dirPos);
            this.tryProvide(this.tanks[1], this.level, dirPos);
        }

        if(this.power >= this.getPowerReqEff() && this.tanks[0].getFill() < this.tanks[0].getMaxFill() && this.tanks[1].getFill() < this.tanks[1].getMaxFill()) {
            this.power -= this.getPowerReqEff();

            if(this.level.getGameTime() % this.getDelayEff() == 0) {
                this.indicator = 0;
                for(int y = this.getBlockPos().getY() - 1; y >= this.getDrillDepth(); y--) {
                    BlockPos drillPos = new BlockPos(this.getBlockPos().getX(), y, this.getBlockPos().getZ());
                    if(this.level.getBlockState(drillPos).getBlock() != NtmBlocks.OIL_PIPE.get()) {
                        if(!this.trySuck(y)) {
                            this.tryDrill(y);
                        }
                        break;
                    }

                    if(y == this.getDrillDepth()) this.indicator = 1;
                }
            }
        } else {
            this.indicator = 2;
        }

        this.networkPackNT(25);
    }

    public int getPowerReqEff() {
        int req = this.getPowerReq();
        return (req + (req / 4 * this.speedLevel) - (req / 4 * this.energyLevel)) * (Math.min(this.overdriveLevel, 3) + 1);
    }

    public int getDelayEff() {
        int delay = this.getDelay();
        int overLevel = Math.min(this.overdriveLevel, 3) + 1;
        return Math.max((delay - (delay / 4 * this.speedLevel) + (delay / 10 * this.energyLevel)) / overLevel, 1);
    }

    public int getPowerReq() {
        return this.consumption;
    }

    public int getDelay() {
        return this.delay;
    }

    public void tryDrill(int y) {
        if(this.level == null) return;

        BlockPos drillPos = new BlockPos(this.getBlockPos().getX(), y, this.getBlockPos().getZ());
        BlockState state = this.level.getBlockState(drillPos);
        Block block = state.getBlock();

        if(block.getExplosionResistance() < 1000F) {
            this.onDrill(y);
            this.level.setBlock(drillPos, NtmBlocks.OIL_PIPE.get().defaultBlockState(), 3);
        } else {
            this.indicator = 2;
        }
    }

    public void onDrill(int y) {
        if(this.level == null) return;

        BlockPos drillPos = new BlockPos(this.getBlockPos().getX(), y, this.getBlockPos().getZ());
        Block block = this.level.getBlockState(drillPos).getBlock();
        String path = BuiltInRegistries.BLOCK.getKey(block).getPath();

        Block gas = null;
        if(path.contains("uranium")) {
            gas = NtmBlocks.GAS_RADON_DENSE.get();
        } else if(path.contains("asbestos")) {
            gas = NtmBlocks.GAS_ASBESTOS.get();
        }

        if(gas == null) return;

        BlockPos origin = this.getBlockPos().above(10);
        for(int dx = -1; dx <= 1; dx++) {
            for(int dz = -1; dz <= 1; dz++) {
                BlockPos target = origin.offset(dx, 0, dz);
                if(this.level.getBlockState(target).canBeReplaced()) {
                    this.level.setBlock(target, gas.defaultBlockState(), 3);
                }
            }
        }
    }

    public void onSuck(BlockPos pos) {
        if(this.level == null) return;

        this.level.playSound(null, this.getBlockPos(), SoundEvents.GENERIC_SWIM, SoundSource.BLOCKS, 2.0F, 0.5F);
        BlockState state = this.level.getBlockState(pos);
        Block block = state.getBlock();

        if(block == NtmBlocks.ORE_OIL.get()) {
            this.tanks[0].setTankType(Fluids.OIL_CRUDE);
            this.tanks[1].setTankType(Fluids.GAS);

            this.tanks[0].setFill(Math.min(this.tanks[0].getFill() + this.oilPerDeposit, this.tanks[0].getMaxFill()));
            int gas = this.gasPerDepositMin + this.level.random.nextInt(this.gasPerDepositMax - this.gasPerDepositMin + 1);
            this.tanks[1].setFill(Math.min(this.tanks[1].getFill() + gas, this.tanks[1].getMaxFill()));

            if(this.level.random.nextDouble() < this.drainChance) {
                this.level.setBlock(pos, NtmBlocks.ORE_OIL_EMPTY.get().defaultBlockState(), 3);
            }
        }
    }

    public boolean trySuck(int y) {
        if(this.level == null) return false;

        BlockPos startPos = new BlockPos(this.getBlockPos().getX(), y, this.getBlockPos().getZ());
        Block startBlock = this.level.getBlockState(startPos).getBlock();
        if(!this.canSuckBlock(startBlock)) return false;
        if(!this.canPump()) return true;

        Queue<BlockPos> queue = new ArrayDeque<>();
        this.processed.clear();
        queue.offer(startPos);
        this.processed.add(startPos);

        int nodesVisited = 0;
        while(!queue.isEmpty() && nodesVisited < 256) {
            BlockPos currentPos = queue.poll();
            nodesVisited++;
            Block currentBlock = this.level.getBlockState(currentPos).getBlock();

            if(currentBlock == NtmBlocks.ORE_OIL.get()) {
                this.onSuck(currentPos);
                return true;
            }

            if(currentBlock != NtmBlocks.ORE_OIL_EMPTY.get()) continue;

            for(Direction dir : new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST }) {
                BlockPos neighborPos = currentPos.relative(dir);
                if(!this.processed.contains(neighborPos) && this.canSuckBlock(this.level.getBlockState(neighborPos).getBlock())) {
                    this.processed.add(neighborPos);
                    queue.offer(neighborPos);
                }
            }
        }

        return false;
    }

    public boolean canSuckBlock(Block block) {
        return block == NtmBlocks.ORE_OIL.get() || block == NtmBlocks.ORE_OIL_EMPTY.get();
    }

    public boolean canPump() {
        return true;
    }

    public int getDrillDepth() {
        return 5;
    }

    public DirPos[] getConPos() {
        BlockPos pos = this.getBlockPos();
        return new DirPos[] {
                new DirPos(pos.getX() + 1, pos.getY(), pos.getZ(), Direction.EAST),
                new DirPos(pos.getX() - 1, pos.getY(), pos.getZ(), Direction.WEST),
                new DirPos(pos.getX(), pos.getY(), pos.getZ() + 1, Direction.SOUTH),
                new DirPos(pos.getX(), pos.getY(), pos.getZ() - 1, Direction.NORTH)
        };
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) {
            return stack.getItem() instanceof api.hbm.energymk2.IBatteryItem;
        }

        if(slot == 1) {
            return !FluidContainerRegistry.getFullContainer(stack, this.tanks[0].getTankType()).isEmpty();
        }

        if(slot == 3) {
            return !FluidContainerRegistry.getFullContainer(stack, this.tanks[1].getTankType()).isEmpty();
        }

        if(slot >= 5 && slot <= 7) {
            return stack.getItem() instanceof com.hbm.items.machine.MachineUpgradeItem;
        }

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 2 || index == 4;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.indicator = tag.getInt("indicator");
        for(int i = 0; i < this.tanks.length; i++) {
            this.tanks[i].readFromNBT(tag, "t" + i);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putInt("indicator", this.indicator);
        for(int i = 0; i < this.tanks.length; i++) {
            this.tanks[i].writeToNBT(tag, "t" + i);
        }
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeInt(this.indicator);
        for(FluidTank tank : this.tanks) {
            tank.serialize(buf);
        }
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.indicator = buf.readInt();
        for(FluidTank tank : this.tanks) {
            tank.deserialize(buf);
        }
    }

    @Override
    public long getPower() {
        return Math.max(Math.min(this.power, this.maxPower), 0);
    }

    @Override
    public void setPower(long i) {
        this.power = i;
    }

    @Override
    public long getMaxPower() {
        return this.maxPower;
    }

    @Override
    public long transferPower(long power) {
        if(power + this.getPower() <= this.getMaxPower()) {
            this.setPower(power + this.getPower());
            return 0;
        }

        long capacity = this.getMaxPower() - this.getPower();
        long overshoot = power - capacity;
        this.setPower(this.getMaxPower());
        return overshoot;
    }

    @Override
    public boolean canConnect(Direction dir) {
        return dir != null && dir.getAxis().isHorizontal();
    }

    @Override
    public boolean canConnect(FluidType type, Direction dir) {
        return dir != null && dir.getAxis().isHorizontal();
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return this.tanks;
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return FluidTank.EMPTY_ARRAY;
    }

    @Override
    public FluidTank[] getAllTanks() {
        return this.tanks;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineOilDerrickMenu(id, inventory, this);
    }

    public boolean hasPermission(Player player) {
        return this.stillValid(player);
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED || type == UpgradeType.POWER || type == UpgradeType.AFTERBURN || type == UpgradeType.OVERDRIVE;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        info.add(IUpgradeInfoProvider.getStandardLabel(NtmBlocks.MACHINE_OIL_DERRICK.get()).getString());

        if(type == UpgradeType.SPEED) {
            info.add(ChatFormatting.GREEN + I18nUtil.resolveKey(IUpgradeInfoProvider.KEY_DELAY, "-" + (level * 25) + "%"));
            info.add(ChatFormatting.RED + I18nUtil.resolveKey(IUpgradeInfoProvider.KEY_CONSUMPTION, "+" + (level * 25) + "%"));
        }

        if(type == UpgradeType.POWER) {
            info.add(ChatFormatting.GREEN + I18nUtil.resolveKey(IUpgradeInfoProvider.KEY_CONSUMPTION, "-" + (level * 25) + "%"));
            info.add(ChatFormatting.RED + I18nUtil.resolveKey(IUpgradeInfoProvider.KEY_DELAY, "+" + (level * 10) + "%"));
        }

        if(type == UpgradeType.AFTERBURN) {
            info.add(ChatFormatting.GREEN + I18nUtil.resolveKey(IUpgradeInfoProvider.KEY_BURN, level * 10, level * 50));
        }

        if(type == UpgradeType.OVERDRIVE) {
            info.add((BobMathUtil.getBlink() ? ChatFormatting.RED : ChatFormatting.DARK_GRAY) + "YES");
        }
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new HashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.POWER, 3);
        upgrades.put(UpgradeType.AFTERBURN, 3);
        upgrades.put(UpgradeType.OVERDRIVE, 3);
        return upgrades;
    }
}
