package com.hbm.blockentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineArcWelderMenu;
import com.hbm.inventory.recipes.ArcWelderRecipes;
import com.hbm.inventory.recipes.ArcWelderRecipes.ArcWelderRecipe;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;

public class MachineArcWelderBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IControlReceiver, IUpgradeInfoProvider, IFluidCopiable {

    private static final int INV_SIZE = 8;

    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);
    public long power;
    public long maxPower = 2_000L;
    public long consumption = 100L;
    public int progress;
    public int processTime = 1;
    public final FluidTank tank = new FluidTank(Fluids.NONE, 24_000);
    public ItemStack display = ItemStack.EMPTY;

    private ArcWelderRecipe recipe;

    public MachineArcWelderBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_ARC_WELDER.get(), pos, state, INV_SIZE);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_arc_welder");
    }

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        this.power = Library.chargeTEFromItems(this.slots, 4, this.power, this.maxPower);
        this.tank.setType(5, this.slots);

        if(this.level.getGameTime() % 20 == 0) {
            for(DirPos dirPos : this.getConPos()) {
                this.trySubscribe(this.level, dirPos);
                if(this.tank.getTankType() != Fluids.NONE) {
                    this.trySubscribe(this.tank.getTankType(), this.level, dirPos);
                }
            }
        }

        this.recipe = ArcWelderRecipes.getRecipe(this.slots.get(0), this.slots.get(1), this.slots.get(2));

        this.upgradeManager.checkSlots(this.slots, 6, 7);
        int speed = this.upgradeManager.getLevel(UpgradeType.SPEED);
        int powerLevel = this.upgradeManager.getLevel(UpgradeType.POWER);
        int overdrive = this.upgradeManager.getLevel(UpgradeType.OVERDRIVE);

        long intendedMaxPower = 2_000L;
        if(this.recipe != null) {
            this.processTime = this.recipe.duration - (this.recipe.duration * speed / 6) + (this.recipe.duration * powerLevel / 3);
            this.consumption = this.recipe.consumption + (this.recipe.consumption * speed) - (this.recipe.consumption * powerLevel / 6);
            this.consumption *= (long) Math.pow(2, overdrive);
            intendedMaxPower = Math.max(this.consumption * 20L, 2_000L);
            this.display = this.recipe.output.copy();

            if(this.canProcess(this.recipe)) {
                this.progress += 1 + overdrive;
                this.power -= this.consumption;
                if(this.progress >= this.processTime) {
                    this.progress = 0;
                    this.consumeItems(this.recipe);
                    if(this.slots.get(3).isEmpty()) {
                        this.slots.set(3, this.recipe.output.copy());
                    } else {
                        this.slots.get(3).grow(this.recipe.output.getCount());
                    }
                    this.setChanged();
                }
            } else {
                this.progress = 0;
            }
        } else {
            this.progress = 0;
            this.display = ItemStack.EMPTY;
        }

        this.maxPower = Math.max(intendedMaxPower, this.power);
        this.networkPackNT(25);
    }

    private boolean canProcess(ArcWelderRecipe recipe) {
        if(this.power < this.consumption) return false;
        if(!ArcWelderRecipes.matchesIngredients(new ItemStack[] { this.slots.get(0), this.slots.get(1), this.slots.get(2) }, recipe.ingredients)) return false;
        if(recipe.fluid != null) {
            if(this.tank.getTankType() != recipe.fluid.type) return false;
            if(this.tank.getFill() < recipe.fluid.fill) return false;
        }
        if(!this.slots.get(3).isEmpty()) {
            if(!ItemStack.isSameItemSameComponents(this.slots.get(3), recipe.output)) return false;
            if(this.slots.get(3).getCount() + recipe.output.getCount() > this.slots.get(3).getMaxStackSize()) return false;
        }
        return true;
    }

    private void consumeItems(ArcWelderRecipe recipe) {
        for(AStack aStack : recipe.ingredients) {
            for(int i = 0; i < 3; i++) {
                ItemStack stack = this.slots.get(i);
                if(aStack.matchesRecipe(stack, true) && stack.getCount() >= aStack.stacksize) {
                    stack.shrink(aStack.stacksize);
                    break;
                }
            }
        }
        if(recipe.fluid != null) {
            this.tank.setFill(this.tank.getFill() - recipe.fluid.fill);
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot < 3) {
            for(AStack t : ArcWelderRecipes.ingredients) {
                if(t.matchesRecipe(stack, true)) return true;
            }
        } else if(slot == 4) {
            return stack.getItem() instanceof IBatteryItem;
        } else if(slot == 5) {
            return stack.getItem() instanceof IItemFluidIdentifier;
        } else if(slot == 6 || slot == 7) {
            return stack.getItem() instanceof MachineUpgradeItem;
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 3;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { 0, 1, 2, 3 };
    }

    public DirPos[] getConPos() {
        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction side = dir.getClockWise();
        BlockPos pos = this.getBlockPos();
        return new DirPos[] {
                new DirPos(pos.getX() + dir.getStepX(), pos.getY(), pos.getZ() + dir.getStepZ(), dir),
                new DirPos(pos.getX() + side.getStepX(), pos.getY(), pos.getZ() + side.getStepZ(), side)
        };
    }

    @Override
    public long getPower() {
        return this.power;
    }

    @Override
    public void setPower(long power) {
        this.power = power;
    }

    @Override
    public long getMaxPower() {
        return this.maxPower;
    }

    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[] { this.tank };
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { this.tank };
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineArcWelderMenu(id, inventory, this);
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return this.getValidUpgrades().containsKey(type);
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        switch(type) {
            case SPEED -> info.add("Speed level: " + level);
            case POWER -> info.add("Power level: " + level);
            case OVERDRIVE -> info.add("+" + level + " parallel progress");
            default -> { }
        }
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new HashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.POWER, 3);
        upgrades.put(UpgradeType.OVERDRIVE, 3);
        return upgrades;
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] { };
    }

    @Override
    public boolean hasPermission(Player player) {
        return this.stillValid(player);
    }

    @Override
    public void receiveControl(CompoundTag tag) { }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeLong(this.maxPower);
        buf.writeLong(this.consumption);
        buf.writeInt(this.progress);
        buf.writeInt(this.processTime);
        buf.writeNbt(this.display.isEmpty() || this.level == null ? null : this.display.save(this.level.registryAccess()));
        this.tank.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.maxPower = buf.readLong();
        this.consumption = buf.readLong();
        this.progress = buf.readInt();
        this.processTime = buf.readInt();
        CompoundTag displayTag = buf.readNbt();
        this.display = displayTag != null && this.level != null ? ItemStack.parseOptional(this.level.registryAccess(), displayTag) : ItemStack.EMPTY;
        this.tank.deserialize(buf);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.maxPower = tag.getLong("maxPower");
        this.consumption = tag.getLong("consumption");
        this.progress = tag.getInt("progress");
        this.processTime = tag.getInt("processTime");
        this.tank.readFromNBT(tag, "tank");
        if(tag.contains("display", 10)) {
            this.display = ItemStack.parse(registries, tag.getCompound("display")).orElse(ItemStack.EMPTY);
        } else {
            this.display = ItemStack.EMPTY;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", this.power);
        tag.putLong("maxPower", this.maxPower);
        tag.putLong("consumption", this.consumption);
        tag.putInt("progress", this.progress);
        tag.putInt("processTime", this.processTime);
        this.tank.writeToNBT(tag, "tank");
        if(!this.display.isEmpty()) {
            tag.put("display", this.display.save(registries));
        }
    }
}
