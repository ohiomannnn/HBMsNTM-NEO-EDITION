package com.hbm.blockentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineSolderingStationMenu;
import com.hbm.inventory.recipes.SolderingRecipes;
import com.hbm.inventory.recipes.SolderingRecipes.SolderingRecipe;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.blocks.NtmBlocks;
import com.hbm.util.fauxpointtwelve.DirPos;
import com.hbm.util.BobMathUtil;
import com.hbm.util.i18n.I18nUtil;
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

public class MachineSolderingStationBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IControlReceiver, IUpgradeInfoProvider, IFluidCopiable {

    private static final int INV_SIZE = 11;

    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);
    public long power;
    public long maxPower = 2_000L;
    public long consumption;
    public boolean collisionPrevention = false;
    public int progress;
    public int processTime = 1;
    public final FluidTank tank;
    public ItemStack display = ItemStack.EMPTY;

    private SolderingRecipe recipe;

    public MachineSolderingStationBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_SOLDERING_STATION.get(), pos, state, INV_SIZE);
        this.tank = new FluidTank(Fluids.NONE, 8_000);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_soldering_station");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {
            this.power = Library.chargeTEFromItems(this.slots, 7, this.getPower(), this.getMaxPower());
            this.tank.setType(8, this.slots);

            if(this.level.getGameTime() % 20 == 0) {
                for(DirPos dirPos : this.getConPos()) {
                    this.trySubscribe(this.level, dirPos);
                    if(this.tank.getTankType() != Fluids.NONE) {
                        this.trySubscribe(this.tank.getTankType(), this.level, dirPos);
                    }
                }
            }

            this.recipe = SolderingRecipes.getRecipe(new ItemStack[] {
                    this.slots.get(0),
                    this.slots.get(1),
                    this.slots.get(2),
                    this.slots.get(3),
                    this.slots.get(4),
                    this.slots.get(5)
            });

            long intendedMaxPower;

            this.upgradeManager.checkSlots(this.slots, 9, 10);
            int redLevel = this.upgradeManager.getLevel(UpgradeType.SPEED);
            int blueLevel = this.upgradeManager.getLevel(UpgradeType.POWER);
            int blackLevel = this.upgradeManager.getLevel(UpgradeType.OVERDRIVE);

            if(this.recipe != null) {
                this.processTime = this.recipe.duration - (this.recipe.duration * redLevel / 6) + (this.recipe.duration * blueLevel / 3);
                this.consumption = this.recipe.consumption + (this.recipe.consumption * redLevel) - (this.recipe.consumption * blueLevel / 6);
                this.consumption *= (long) Math.pow(2, blackLevel);
                intendedMaxPower = this.consumption * 20L;
                this.display = this.recipe.output.copy();

                if(this.canProcess(this.recipe)) {
                    this.progress += (1 + blackLevel);
                    this.power -= this.consumption;

                    if(this.progress >= this.processTime) {
                        this.progress = 0;
                        this.consumeItems(this.recipe);

                        if(this.slots.get(6).isEmpty()) {
                            this.slots.set(6, this.recipe.output.copy());
                        } else {
                            this.slots.get(6).grow(this.recipe.output.getCount());
                        }

                        this.setChanged();
                    }

                    if(this.level.getGameTime() % 20 == 0) {
                        this.setChanged();
                    }
                } else {
                    this.progress = 0;
                }
            } else {
                this.progress = 0;
                this.consumption = 100;
                intendedMaxPower = 2_000L;
                this.display = ItemStack.EMPTY;
            }

            this.maxPower = Math.max(intendedMaxPower, this.power);
            this.networkPackNT(25);
        }
    }

    public boolean canProcess(SolderingRecipe recipe) {
        if(this.power < this.consumption) return false;

        if(!SolderingRecipes.matchesIngredients(new ItemStack[] {
                this.slots.get(0),
                this.slots.get(1),
                this.slots.get(2)
        }, recipe.toppings)) return false;

        if(!SolderingRecipes.matchesIngredients(new ItemStack[] {
                this.slots.get(3),
                this.slots.get(4)
        }, recipe.pcb)) return false;

        if(!SolderingRecipes.matchesIngredients(new ItemStack[] {
                this.slots.get(5)
        }, recipe.solder)) return false;

        if(recipe.fluid != null) {
            if(this.tank.getTankType() != recipe.fluid.type) return false;
            if(this.tank.getFill() < recipe.fluid.fill) return false;
        }

        if(this.collisionPrevention && recipe.fluid == null && this.tank.getFill() > 0) return false;

        if(!this.slots.get(6).isEmpty()) {
            if(!ItemStack.isSameItemSameComponents(this.slots.get(6), recipe.output)) return false;
            if(this.slots.get(6).getCount() + recipe.output.getCount() > this.slots.get(6).getMaxStackSize()) return false;
        }

        return true;
    }

    public void consumeItems(SolderingRecipe recipe) {
        for(AStack aStack : recipe.toppings) {
            for(int i = 0; i < 3; i++) {
                ItemStack stack = this.slots.get(i);
                if(aStack.matchesRecipe(stack, true) && stack.getCount() >= aStack.stacksize) {
                    stack.shrink(aStack.stacksize);
                    break;
                }
            }
        }

        for(AStack aStack : recipe.pcb) {
            for(int i = 3; i < 5; i++) {
                ItemStack stack = this.slots.get(i);
                if(aStack.matchesRecipe(stack, true) && stack.getCount() >= aStack.stacksize) {
                    stack.shrink(aStack.stacksize);
                    break;
                }
            }
        }

        for(AStack aStack : recipe.solder) {
            for(int i = 5; i < 6; i++) {
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
            for(int i = 0; i < 3; i++) {
                if(i != slot && !this.slots.get(i).isEmpty() && ItemStack.isSameItemSameComponents(this.slots.get(i), stack)) return false;
            }
            for(AStack t : SolderingRecipes.toppings) if(t.matchesRecipe(stack, true)) return true;
        } else if(slot < 5) {
            for(int i = 3; i < 5; i++) {
                if(i != slot && !this.slots.get(i).isEmpty() && ItemStack.isSameItemSameComponents(this.slots.get(i), stack)) return false;
            }
            for(AStack t : SolderingRecipes.pcb) if(t.matchesRecipe(stack, true)) return true;
        } else if(slot < 6) {
            for(int i = 5; i < 6; i++) {
                if(i != slot && !this.slots.get(i).isEmpty() && ItemStack.isSameItemSameComponents(this.slots.get(i), stack)) return false;
            }
            for(AStack t : SolderingRecipes.solder) if(t.matchesRecipe(stack, true)) return true;
        } else if(slot == 7) {
            return stack.getItem() instanceof IBatteryItem;
        } else if(slot == 8) {
            return stack.getItem() instanceof IItemFluidIdentifier;
        } else if(slot == 9 || slot == 10) {
            return stack.getItem() instanceof MachineUpgradeItem;
        }

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 6;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { 0, 1, 2, 3, 4, 5, 6 };
    }

    public DirPos[] getConPos() {
        Direction dir = this.getBlockState().getValue(com.hbm.blocks.DummyableBlock.FACING);
        Direction rot = dir.getClockWise();

        BlockPos pos = this.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return new DirPos[] {
                new DirPos(x + dir.getStepX(), y, z + dir.getStepZ(), dir),
                new DirPos(x + dir.getStepX() + rot.getStepX(), y, z + dir.getStepZ() + rot.getStepZ(), dir),
                new DirPos(x - dir.getStepX() * 2, y, z - dir.getStepZ() * 2, dir.getOpposite()),
                new DirPos(x - dir.getStepX() * 2 + rot.getStepX(), y, z - dir.getStepZ() * 2 + rot.getStepZ(), dir.getOpposite()),
                new DirPos(x - rot.getStepX(), y, z - rot.getStepZ(), rot.getOpposite()),
                new DirPos(x - dir.getStepX() - rot.getStepX(), y, z - dir.getStepZ() - rot.getStepZ(), rot.getOpposite()),
                new DirPos(x + rot.getStepX() * 2, y, z + rot.getStepZ() * 2, rot),
                new DirPos(x - dir.getStepX() + rot.getStepX() * 2, y, z - dir.getStepZ() + rot.getStepZ() * 2, rot),
        };
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeLong(this.maxPower);
        buf.writeLong(this.consumption);
        buf.writeInt(this.progress);
        buf.writeInt(this.processTime);
        buf.writeBoolean(this.collisionPrevention);
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
        this.collisionPrevention = buf.readBoolean();
        CompoundTag displayTag = buf.readNbt();
        this.display = displayTag != null && this.level != null ? ItemStack.parseOptional(this.level.registryAccess(), displayTag) : ItemStack.EMPTY;

        this.tank.deserialize(buf);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.maxPower = tag.getLong("maxPower");
        this.progress = tag.getInt("progress");
        this.processTime = tag.getInt("processTime");
        this.collisionPrevention = tag.getBoolean("collisionPrevention");
        this.tank.readFromNBT(tag, "t");
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
        tag.putInt("progress", this.progress);
        tag.putInt("processTime", this.processTime);
        tag.putBoolean("collisionPrevention", this.collisionPrevention);
        this.tank.writeToNBT(tag, "t");
        if(!this.display.isEmpty()) {
            tag.put("display", this.display.save(registries));
        }
    }

    @Override
    public long getPower() {
        return Math.max(Math.min(this.power, this.maxPower), 0);
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
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { this.tank };
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] { };
    }

    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[] { this.tank };
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineSolderingStationMenu(id, inventory, this);
    }

    @Override
    public boolean hasPermission(Player player) {
        return this.stillValid(player);
    }

    @Override
    public void receiveControl(CompoundTag tag) {
        this.collisionPrevention = !this.collisionPrevention;
        this.setChanged();
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED || type == UpgradeType.POWER || type == UpgradeType.OVERDRIVE;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        info.add(IUpgradeInfoProvider.getStandardLabel(NtmBlocks.MACHINE_SOLDERING_STATION.get()).getString());
        if(type == UpgradeType.SPEED) {
            info.add("+" + (level * 100 / 6) + "% " + I18nUtil.resolveKey(KEY_DELAY));
            info.add("+" + (level * 100) + "% " + I18nUtil.resolveKey(KEY_CONSUMPTION));
        }
        if(type == UpgradeType.POWER) {
            info.add("-" + (level * 100 / 6) + "% " + I18nUtil.resolveKey(KEY_CONSUMPTION));
            info.add("+" + (level * 100 / 3) + "% " + I18nUtil.resolveKey(KEY_DELAY));
        }
        if(type == UpgradeType.OVERDRIVE) {
            info.add(BobMathUtil.getBlink() ? "YES" : "YES");
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
    public FluidTank getTankToPaste() {
        return this.tank;
    }
}
