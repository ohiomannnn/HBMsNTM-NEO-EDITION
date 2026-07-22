package com.hbm.blockentity.machine;

import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import api.hbm.tile.IHeatSource;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineFurnaceCombinationMenu;
import com.hbm.inventory.recipes.CombinationRecipes;
import com.hbm.util.Tuple;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MachineFurnaceCombinationBlockEntity extends MachineBaseBlockEntity implements IFluidStandardTransceiverMK2 {

    public static final int PROCESS_TIME = 20_000;
    public static final int MAX_HEAT = 100_000;
    public static final double DIFFUSION = 0.25D;

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int SLOT_FLUID_IN = 2;
    public static final int SLOT_FLUID_OUT = 3;

    public int progress;
    public int heat;
    public boolean wasOn;
    public final FluidTank tank = new FluidTank(Fluids.NONE, 24_000);

    public MachineFurnaceCombinationBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.FURNACE_COMBINATION.get(), pos, state, 4);
        CombinationRecipes.registerDefaults();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.furnace_combination");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;
        if(this.level.isClientSide) {
            this.spawnWorkingParticles(this.level);
            return;
        }

        this.checkTilt(LoadedBaseBlockEntity.TiltType.CONFIG, false);
        this.tryPullHeat();
        this.tank.unloadTank(this.level, SLOT_FLUID_IN, SLOT_FLUID_OUT, this.slots);

        if(this.level.getGameTime() % 20 == 0) {
            for(DirPos pos : this.getConPos()) {
                if(this.tank.getFill() > 0) {
                    this.tryProvide(this.tank, this.level, pos);
                }
            }
        }

        if(this.canProcess()) {
            int speed = this.heat / 100;
            if(speed > 0) {
                this.progress += speed;
                this.heat = Math.max(0, this.heat - speed);
                this.wasOn = true;

                if(this.level.getGameTime() % 10 == 0) {
                    this.level.playSound(null, this.worldPosition, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.4F, 0.9F + this.level.random.nextFloat() * 0.2F);
                }

                if(this.progress >= PROCESS_TIME) {
                    this.progress -= PROCESS_TIME;
                    this.finishRecipe();
                }
            } else {
                this.wasOn = false;
            }
        } else {
            this.progress = 0;
            this.wasOn = false;
        }

        this.networkPackNT(50);
    }

    private void spawnWorkingParticles(Level level) {
        if(!this.wasOn || level.random.nextInt(15) != 0) return;

        level.addParticle(
                ParticleTypes.LAVA,
                this.worldPosition.getX() + 0.5D + level.random.nextGaussian() * 0.5D,
                this.worldPosition.getY() + 2.0D,
                this.worldPosition.getZ() + 0.5D + level.random.nextGaussian() * 0.5D,
                0.0D,
                0.0D,
                0.0D
        );
    }

    private boolean canProcess() {
        Tuple.Pair<ItemStack, FluidStack> recipe = CombinationRecipes.getOutput(this.slots.get(SLOT_INPUT));
        if(recipe == null) return false;

        ItemStack itemOutput = recipe.getKey();
        FluidStack fluidOutput = recipe.getValue();

        if(!itemOutput.isEmpty()) {
            ItemStack slot = this.slots.get(SLOT_OUTPUT);
            if(!slot.isEmpty() && (!ItemStack.isSameItemSameComponents(slot, itemOutput) || slot.getCount() + itemOutput.getCount() > slot.getMaxStackSize())) {
                return false;
            }
        }

        if(fluidOutput != null) {
            if(this.tank.getTankType() != Fluids.NONE && this.tank.getTankType() != fluidOutput.type) return false;
            if(this.tank.getFill() + fluidOutput.fill > this.tank.getMaxFill()) return false;
        }

        return true;
    }

    private void finishRecipe() {
        Tuple.Pair<ItemStack, FluidStack> recipe = CombinationRecipes.getOutput(this.slots.get(SLOT_INPUT));
        if(recipe == null) return;

        ItemStack itemOutput = recipe.getKey();
        FluidStack fluidOutput = recipe.getValue();

        if(!itemOutput.isEmpty()) {
            ItemStack slot = this.slots.get(SLOT_OUTPUT);
            if(slot.isEmpty()) {
                this.slots.set(SLOT_OUTPUT, itemOutput.copy());
            } else if(ItemStack.isSameItemSameComponents(slot, itemOutput)) {
                slot.grow(itemOutput.getCount());
            }
        }

        if(fluidOutput != null) {
            if(this.tank.getTankType() == Fluids.NONE) {
                this.tank.setTankType(fluidOutput.type);
            }
            this.tank.setFill(this.tank.getFill() + fluidOutput.fill);
        }

        this.slots.get(SLOT_INPUT).shrink(1);
        if(this.slots.get(SLOT_INPUT).isEmpty()) {
            this.slots.set(SLOT_INPUT, ItemStack.EMPTY);
        }

        this.setChanged();
    }

    private void tryPullHeat() {
        if(this.heat < MAX_HEAT) {
            BlockEntity be = this.level.getBlockEntity(this.worldPosition.below());
            if(be instanceof IHeatSource source) {
                int pulled = (int) Math.ceil((source.getHeatStored() - this.heat) * DIFFUSION);
                if(pulled > 0) {
                    int accepted = Math.min(pulled, MAX_HEAT - this.heat);
                    this.heat += accepted;
                    source.useUpHeat(accepted);
                    return;
                }
            }
        }

        if(this.heat > 0) {
            this.heat = Math.max(0, this.heat - Math.max(1, this.heat / 1000));
        }
    }

    public DirPos[] getConPos() {
        BlockPos pos = this.getBlockPos();
        return new DirPos[] {
                new DirPos(pos.getX() + 2, pos.getY(), pos.getZ(), Direction.EAST),
                new DirPos(pos.getX() - 2, pos.getY(), pos.getZ(), Direction.WEST),
                new DirPos(pos.getX(), pos.getY(), pos.getZ() + 2, Direction.SOUTH),
                new DirPos(pos.getX(), pos.getY(), pos.getZ() - 2, Direction.NORTH)
        };
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == SLOT_INPUT || slot == SLOT_FLUID_IN;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == SLOT_OUTPUT || index == SLOT_FLUID_OUT;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { SLOT_INPUT, SLOT_OUTPUT };
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.progress = tag.getInt("progress");
        this.heat = tag.getInt("heat");
        this.wasOn = tag.getBoolean("wasOn");
        this.tank.readFromNBT(tag, "tank");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("progress", this.progress);
        tag.putInt("heat", this.heat);
        tag.putBoolean("wasOn", this.wasOn);
        this.tank.writeToNBT(tag, "tank");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.progress);
        buf.writeInt(this.heat);
        buf.writeBoolean(this.wasOn);
        this.tank.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.progress = buf.readInt();
        this.heat = buf.readInt();
        this.wasOn = buf.readBoolean();
        this.tank.deserialize(buf);
    }

    public int getProgressScaled(int pixels) {
        return this.progress * pixels / PROCESS_TIME;
    }

    public int getHeatScaled(int pixels) {
        return this.heat * pixels / MAX_HEAT;
    }

    @Override public boolean canConnect(FluidType type, Direction dir) { return dir != null && dir.getAxis().isHorizontal(); }
    @Override public FluidTank[] getReceivingTanks() { return FluidTank.EMPTY_ARRAY; }
    @Override public FluidTank[] getSendingTanks() { return new FluidTank[] { this.tank }; }
    @Override public FluidTank[] getAllTanks() { return new FluidTank[] { this.tank }; }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineFurnaceCombinationMenu(id, inventory, this);
    }
}
