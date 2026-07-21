package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyProviderMK2;
import api.hbm.fluidmk2.IFluidStandardReceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Flammable;
import com.hbm.inventory.menus.MachineWoodBurnerMenu;
import com.hbm.items.NtmItems;
import com.hbm.lib.Library;
import com.hbm.modules.ModuleBurnTime;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Locale;

public class MachineWoodBurnerBlockEntity extends MachineBaseBlockEntity implements IEnergyProviderMK2, IFluidStandardReceiverMK2, IControlReceiver, IFluidCopiable {

    public static final long MAX_POWER = 100_000L;
    public static final int POWER_PER_TICK = 100;
    private static final int ASH_THRESHOLD = 2_000;

    public static final int SLOT_FUEL = 0;
    public static final int SLOT_ASH = 1;
    public static final int SLOT_IDENTIFIER = 2;
    public static final int SLOT_FLUID_IN = 3;
    public static final int SLOT_FLUID_OUT = 4;
    public static final int SLOT_BATTERY = 5;

    public static final ModuleBurnTime BURN_MODULE = new ModuleBurnTime()
            .setLogTimeMod(4.0D)
            .setWoodTimeMod(2.0D);

    public long power;
    public int burnTime;
    public int maxBurnTime;
    public boolean liquidBurn;
    public boolean isOn;
    public int powerGen;
    public int ashLevelWood;
    public int ashLevelCoal;
    public int ashLevelMisc;

    public final FluidTank tank = new FluidTank(Fluids.WOODOIL, 16_000);
    private AABB renderBox;

    public MachineWoodBurnerBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_WOOD_BURNER.get(), pos, state, 6);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machineWoodBurner");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(this.level.isClientSide) {
            if(this.powerGen > 0) this.spawnSmoke(this.level);
            return;
        }

        this.powerGen = 0;
        this.tank.setType(SLOT_IDENTIFIER, this.slots);
        this.tank.loadTank(this.level, SLOT_FLUID_IN, SLOT_FLUID_OUT, this.slots);
        this.power = Library.chargeItemsFromTE(this.slots, SLOT_BATTERY, this.power, MAX_POWER);

        for(DirPos pos : this.getPowerConPos()) {
            if(this.power > 0) this.tryProvide(this.level, pos.makeCompat(), pos.getDir());
        }
        for(DirPos pos : this.getFluidConPos()) {
            if(this.level.getGameTime() % 20 == 0) {
                this.trySubscribe(this.tank.getTankType(), this.level, pos);
            }
        }

        if(!this.liquidBurn) {
            this.updateSolidBurn();
        } else {
            this.updateLiquidBurn();
        }

        this.power = Math.min(MAX_POWER, this.power + this.powerGen);
        this.networkPackNT(25);
    }

    private void updateSolidBurn() {
        if(this.burnTime <= 0) {
            ItemStack fuel = this.slots.get(SLOT_FUEL);
            if(fuel.isEmpty()) return;

            int time = BURN_MODULE.getBurnTime(fuel);
            if(time <= 0) return;

            AshType ash = getAshType(fuel);
            switch(ash) {
                case WOOD -> this.ashLevelWood += time;
                case COAL -> this.ashLevelCoal += time;
                case MISC -> this.ashLevelMisc += time;
            }
            this.processAsh();

            this.maxBurnTime = this.burnTime = time;
            ItemStack remainder = fuel.hasCraftingRemainingItem() ? fuel.getCraftingRemainingItem().copy() : ItemStack.EMPTY;
            fuel.shrink(1);
            if(fuel.isEmpty()) this.slots.set(SLOT_FUEL, remainder);
            this.setChanged();
        } else if(this.power < MAX_POWER && this.isOn) {
            this.burnTime--;
            this.powerGen += POWER_PER_TICK;
        }
    }

    private void updateLiquidBurn() {
        if(this.power >= MAX_POWER || this.tank.getFill() <= 0 || !this.isOn) return;
        FT_Flammable flammable = this.tank.getTankType().getTrait(FT_Flammable.class);
        if(flammable == null) return;

        int toBurn = Math.min(this.tank.getFill(), 2);
        if(toBurn <= 0) return;

        this.powerGen += (int)(flammable.getHeatEnergy() * toBurn / 2_000L);
        this.tank.setFill(this.tank.getFill() - toBurn);
    }

    private void processAsh() {
        while(this.ashLevelWood >= ASH_THRESHOLD && this.placeAsh(AshType.WOOD)) this.ashLevelWood -= ASH_THRESHOLD;
        while(this.ashLevelCoal >= ASH_THRESHOLD && this.placeAsh(AshType.COAL)) this.ashLevelCoal -= ASH_THRESHOLD;
        while(this.ashLevelMisc >= ASH_THRESHOLD && this.placeAsh(AshType.MISC)) this.ashLevelMisc -= ASH_THRESHOLD;
    }

    private boolean placeAsh(AshType type) {
        ItemStack ash = switch(type) {
            case WOOD -> new ItemStack(NtmItems.POWDER_ASH_WOOD.get());
            case COAL -> new ItemStack(NtmItems.POWDER_ASH_COAL.get());
            case MISC -> new ItemStack(NtmItems.POWDER_ASH_MISC.get());
        };

        for(int i = 0; i <= SLOT_FLUID_OUT; i++) {
            ItemStack stack = this.slots.get(i);
            if(stack.isEmpty()) {
                this.slots.set(i, ash.copy());
                return true;
            }
            if(ItemStack.isSameItemSameComponents(stack, ash) && stack.getCount() < stack.getMaxStackSize()) {
                stack.grow(1);
                return true;
            }
        }

        return false;
    }

    private static AshType getAshType(ItemStack stack) {
        String path = stack.getItem().builtInRegistryHolder().key().location().getPath().toLowerCase(Locale.US);
        if(path.contains("log") || path.contains("wood") || path.contains("plank")) return AshType.WOOD;
        if(path.contains("coal") || path.contains("coke") || path.contains("lignite")) return AshType.COAL;
        for(String tag : com.hbm.util.ItemStackUtil.getTags(stack)) {
            String lower = tag.toLowerCase(Locale.US);
            if(lower.contains("logs") || lower.contains("wood") || lower.contains("planks")) return AshType.WOOD;
            if(lower.contains("coal") || lower.contains("coke") || lower.contains("lignite")) return AshType.COAL;
        }
        return AshType.MISC;
    }

    private void spawnSmoke(Level level) {
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = facing.getClockWise();
        Direction back = facing.getOpposite();
        double centerX = this.worldPosition.getX() + 0.5D + back.getStepX() * 0.5D + side.getStepX() * 0.5D;
        double centerZ = this.worldPosition.getZ() + 0.5D + back.getStepZ() * 0.5D + side.getStepZ() * 0.5D;
        double[] stackOffset = this.rotateModelOffset(facing, -0.5D, -0.5D);
        level.addParticle(ParticleTypes.SMOKE,
                centerX + stackOffset[0],
                this.worldPosition.getY() + 4.0D,
                centerZ + stackOffset[1],
                0.0D, 0.05D, 0.0D);
    }

    private double[] rotateModelOffset(Direction facing, double x, double z) {
        return switch(facing) {
            case SOUTH -> new double[] { -x, -z };
            case WEST -> new double[] { z, -x };
            case EAST -> new double[] { -z, x };
            default -> new double[] { x, z };
        };
    }

    public DirPos[] getPowerConPos() {
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = facing.getClockWise();
        BlockPos leftFront = this.worldPosition.relative(facing);
        BlockPos rightFront = leftFront.relative(side);
        return new DirPos[] {
                new DirPos(leftFront.getX(), leftFront.getY(), leftFront.getZ(), facing),
                new DirPos(rightFront.getX(), rightFront.getY(), rightFront.getZ(), facing)
        };
    }

    public DirPos[] getFluidConPos() {
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = facing.getClockWise();
        BlockPos base = this.worldPosition.relative(facing.getOpposite(), 2);
        return new DirPos[] {
                new DirPos(base.getX(), base.getY(), base.getZ(), side.getOpposite()),
                new DirPos(base.relative(side).getX(), base.getY(), base.relative(side).getZ(), facing.getOpposite())
        };
    }

    @Override
    public boolean canConnect(Direction dir) {
        if(dir == null) return false;
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        return dir == facing;
    }

    @Override
    public boolean canConnect(FluidType type, Direction dir) {
        if(dir == null) return false;
        Direction facing = this.getBlockState().getValue(DummyableBlock.FACING);
        return dir == facing.getOpposite() || dir == facing.getClockWise().getOpposite();
    }
    @Override public FluidTank[] getAllTanks() { return new FluidTank[] { this.tank }; }
    @Override public FluidTank[] getReceivingTanks() { return new FluidTank[] { this.tank }; }
    @Override public FluidTank getTankToPaste() { return this.tank; }
    @Override public long getPower() { return this.power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return MAX_POWER; }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return switch(slot) {
            case SLOT_FUEL -> BURN_MODULE.getBurnTime(stack) > 0;
            case SLOT_IDENTIFIER -> stack.getItem() instanceof com.hbm.items.machine.IItemFluidIdentifier;
            case SLOT_FLUID_IN -> com.hbm.inventory.FluidContainerRegistry.getFluidContent(stack, this.tank.getTankType()) > 0;
            case SLOT_BATTERY -> stack.getItem() instanceof api.hbm.energymk2.IBatteryItem;
            default -> false;
        };
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == SLOT_ASH || index == SLOT_FLUID_OUT || index == SLOT_BATTERY;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { SLOT_FUEL, SLOT_ASH, SLOT_IDENTIFIER, SLOT_FLUID_IN, SLOT_FLUID_OUT, SLOT_BATTERY };
    }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("toggle")) {
            this.isOn = !this.isOn;
            this.setChanged();
        }
        if(tag.contains("switch")) {
            this.liquidBurn = !this.liquidBurn;
            this.setChanged();
        }
    }

    @Override
    public boolean hasPermission(Player player) {
        return this.stillValid(player);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.burnTime = tag.getInt("burnTime");
        this.maxBurnTime = tag.getInt("maxBurnTime");
        this.isOn = tag.getBoolean("isOn");
        this.liquidBurn = tag.getBoolean("liquidBurn");
        this.ashLevelWood = tag.getInt("ashLevelWood");
        this.ashLevelCoal = tag.getInt("ashLevelCoal");
        this.ashLevelMisc = tag.getInt("ashLevelMisc");
        this.tank.readFromNBT(tag, "t");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putInt("burnTime", this.burnTime);
        tag.putInt("maxBurnTime", this.maxBurnTime);
        tag.putBoolean("isOn", this.isOn);
        tag.putBoolean("liquidBurn", this.liquidBurn);
        tag.putInt("ashLevelWood", this.ashLevelWood);
        tag.putInt("ashLevelCoal", this.ashLevelCoal);
        tag.putInt("ashLevelMisc", this.ashLevelMisc);
        this.tank.writeToNBT(tag, "t");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeInt(this.burnTime);
        buf.writeInt(this.maxBurnTime);
        buf.writeBoolean(this.liquidBurn);
        buf.writeBoolean(this.isOn);
        buf.writeInt(this.powerGen);
        this.tank.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.burnTime = buf.readInt();
        this.maxBurnTime = buf.readInt();
        this.liquidBurn = buf.readBoolean();
        this.isOn = buf.readBoolean();
        this.powerGen = buf.readInt();
        this.tank.deserialize(buf);
    }

    public int getPowerScaled(int pixels) {
        return (int)(this.power * pixels / MAX_POWER);
    }

    public int getBurnScaled(int pixels) {
        if(this.maxBurnTime <= 0) return 0;
        return this.burnTime * pixels / this.maxBurnTime;
    }

    public AABB getRenderBoundingBox() {
        if(this.renderBox == null) {
            BlockPos pos = this.worldPosition;
            this.renderBox = new AABB(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 2, pos.getY() + 6, pos.getZ() + 2);
        }
        return this.renderBox;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineWoodBurnerMenu(id, inventory, this);
    }

    private enum AshType {
        WOOD,
        COAL,
        MISC
    }
}
