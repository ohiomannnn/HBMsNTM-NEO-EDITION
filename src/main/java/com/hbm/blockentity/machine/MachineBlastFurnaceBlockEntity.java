package com.hbm.blockentity.machine;

import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineBlastFurnaceMenu;
import com.hbm.inventory.recipes.BlastFurnaceRecipe;
import com.hbm.inventory.recipes.BlastFurnaceRecipes;
import com.hbm.items.NtmItems;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MachineBlastFurnaceBlockEntity extends MachineBaseBlockEntity implements IFluidStandardTransceiverMK2 {

    public static final int MAX_FUEL = 25_600;
    public static final int FUEL_RATE = 800;
    public static final int FLUE_GAS = 100;

    public static final int SLOT_FUEL = 0;
    public static final int SLOT_INPUT_1 = 1;
    public static final int SLOT_INPUT_2 = 2;
    public static final int SLOT_OUTPUT_1 = 3;
    public static final int SLOT_OUTPUT_2 = 4;

    public final FluidTank[] tanks;
    public boolean isProgressing;
    public float progress;
    public float speed = 0.5F;
    public int fuel;

    public MachineBlastFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_BLAST_FURNACE.get(), pos, state, 5);
        BlastFurnaceRecipes.INSTANCE.registerDefaults();
        this.tanks = new FluidTank[] {
                new FluidTank(Fluids.AIRBLAST, 4_000),
                new FluidTank(Fluids.FLUE, 1_000)
        };
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_blast_furnace");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;
        if(this.level.isClientSide) return;

        this.checkTilt(LoadedBaseBlockEntity.TiltType.CONFIG, false);

        for(DirPos pos : this.getConPos()) {
            this.trySubscribe(this.tanks[0].getTankType(), this.level, pos);
            if(this.tanks[1].getFill() > 0) {
                this.tryProvide(this.tanks[1], this.level, pos);
            }
        }

        this.tryBurnFuel();

        BlastFurnaceRecipe recipe = BlastFurnaceRecipes.INSTANCE.getRecipe(this.slots.get(SLOT_INPUT_1), this.slots.get(SLOT_INPUT_2));
        if(!this.tilted && recipe != null && this.fuel >= FUEL_RATE && this.canOutput(recipe)) {
            this.isProgressing = true;
            this.speed = Math.max(0.5F, Math.min(5.0F, 0.5F + this.tanks[0].getFill() * 8.0F / this.tanks[0].getMaxFill()));
            this.progress += this.speed / Math.max(recipe.duration, 1);
            if(this.tanks[0].getFill() > 0) {
                this.tanks[0].setFill(Math.max(0, this.tanks[0].getFill() - 1));
            }

            if(this.level.getGameTime() % 10 == 0 && !this.muffled) {
                this.level.playSound(null, this.worldPosition, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 0.5F + this.level.random.nextFloat() * 0.25F);
            }

            if(this.progress >= 1.0F) {
                this.process(recipe);
                this.progress = 0.0F;
                this.fuel -= FUEL_RATE;
                this.tanks[1].setFill(Math.min(this.tanks[1].getMaxFill(), this.tanks[1].getFill() + FLUE_GAS));
                this.setChanged();
            }
        } else {
            this.isProgressing = false;
            this.progress = 0.0F;
            this.speed = 0.5F;
        }

        this.networkPackNT(100);
    }

    private void tryBurnFuel() {
        ItemStack stack = this.slots.get(SLOT_FUEL);
        int burnTime = getBurnTime(stack);
        if(burnTime <= 0 || this.fuel > MAX_FUEL - burnTime) return;

        this.fuel += burnTime;
        stack.shrink(1);
        if(stack.isEmpty()) {
            this.slots.set(SLOT_FUEL, ItemStack.EMPTY);
        }
        this.setChanged();
    }

    public static int getBurnTime(ItemStack stack) {
        if(stack.isEmpty()) return 0;
        if(stack.is(Items.COAL)) return 1_600;
        if(stack.is(Items.CHARCOAL)) return 1_600;
        if(stack.is(Blocks.COAL_BLOCK.asItem())) return 16_000;
        if(stack.is(Items.BLAZE_ROD)) return 8_000;
        if(stack.is(Items.FIRE_CHARGE)) return 2_400;
        if(stack.is(NtmItems.LIGNITE.get())) return 1_200;
        if(stack.is(NtmItems.POWDER_COAL.get())) return 1_760;
        if(stack.is(NtmItems.POWDER_LIGNITE.get())) return 1_200;
        if(stack.is(NtmItems.OIL_TAR_COAL.get())) return 3_200;
        return 0;
    }

    private boolean canOutput(BlastFurnaceRecipe recipe) {
        if(!BlastFurnaceRecipes.matchesInputs(recipe, this.slots.get(SLOT_INPUT_1), this.slots.get(SLOT_INPUT_2), false)) return false;
        ItemStack first = getOutput(recipe, 0);
        ItemStack second = getOutput(recipe, 1);
        return this.canOutputToSlot(SLOT_OUTPUT_1, first) && this.canOutputToSlot(SLOT_OUTPUT_2, second);
    }

    private boolean canOutputToSlot(int slot, ItemStack output) {
        if(output.isEmpty()) return true;
        ItemStack current = this.slots.get(slot);
        if(current.isEmpty()) return true;
        if(!ItemStack.isSameItemSameComponents(current, output)) return false;
        return current.getCount() + output.getCount() <= current.getMaxStackSize();
    }

    private void process(BlastFurnaceRecipe recipe) {
        this.consumeInputs(recipe);
        this.placeOutput(SLOT_OUTPUT_1, getOutput(recipe, 0));
        this.placeOutput(SLOT_OUTPUT_2, getOutput(recipe, 1));
    }

    private void consumeInputs(BlastFurnaceRecipe recipe) {
        if(recipe.inputItem == null || recipe.inputItem.length == 0) return;

        if(recipe.inputItem.length == 1) {
            if(recipe.inputItem[0].matchesRecipe(this.slots.get(SLOT_INPUT_1), false)) {
                this.slots.get(SLOT_INPUT_1).shrink(recipe.inputItem[0].stacksize);
            } else {
                this.slots.get(SLOT_INPUT_2).shrink(recipe.inputItem[0].stacksize);
            }
        } else if(recipe.inputItem[0].matchesRecipe(this.slots.get(SLOT_INPUT_1), false)
                && recipe.inputItem[1].matchesRecipe(this.slots.get(SLOT_INPUT_2), false)) {
            this.slots.get(SLOT_INPUT_1).shrink(recipe.inputItem[0].stacksize);
            this.slots.get(SLOT_INPUT_2).shrink(recipe.inputItem[1].stacksize);
        } else {
            this.slots.get(SLOT_INPUT_1).shrink(recipe.inputItem[1].stacksize);
            this.slots.get(SLOT_INPUT_2).shrink(recipe.inputItem[0].stacksize);
        }

        if(this.slots.get(SLOT_INPUT_1).isEmpty()) this.slots.set(SLOT_INPUT_1, ItemStack.EMPTY);
        if(this.slots.get(SLOT_INPUT_2).isEmpty()) this.slots.set(SLOT_INPUT_2, ItemStack.EMPTY);
    }

    private static ItemStack getOutput(BlastFurnaceRecipe recipe, int index) {
        if(recipe.outputItem == null || index >= recipe.outputItem.length) return ItemStack.EMPTY;
        ItemStack stack = recipe.outputItem[index].collapse();
        return stack == null ? ItemStack.EMPTY : stack;
    }

    private void placeOutput(int slot, ItemStack output) {
        if(output.isEmpty()) return;
        ItemStack current = this.slots.get(slot);
        if(current.isEmpty()) {
            this.slots.set(slot, output.copy());
        } else if(ItemStack.isSameItemSameComponents(current, output)) {
            current.grow(output.getCount());
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
        if(slot == SLOT_FUEL) return getBurnTime(stack) > 0;
        if(slot == SLOT_INPUT_1 || slot == SLOT_INPUT_2) return BlastFurnaceRecipes.INSTANCE.isItemValid(stack);
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == SLOT_OUTPUT_1 || index == SLOT_OUTPUT_2;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { SLOT_INPUT_1, SLOT_INPUT_2, SLOT_FUEL, SLOT_OUTPUT_1, SLOT_OUTPUT_2 };
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.fuel = tag.getInt("fuel");
        this.progress = tag.getFloat("progress");
        this.speed = tag.getFloat("speed");
        this.isProgressing = tag.getBoolean("isProgressing");
        this.tanks[0].readFromNBT(tag, "airblast");
        this.tanks[1].readFromNBT(tag, "flue");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("fuel", this.fuel);
        tag.putFloat("progress", this.progress);
        tag.putFloat("speed", this.speed);
        tag.putBoolean("isProgressing", this.isProgressing);
        this.tanks[0].writeToNBT(tag, "airblast");
        this.tanks[1].writeToNBT(tag, "flue");
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.fuel);
        buf.writeFloat(this.progress);
        buf.writeFloat(this.speed);
        buf.writeBoolean(this.isProgressing);
        this.tanks[0].serialize(buf);
        this.tanks[1].serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.fuel = buf.readInt();
        this.progress = buf.readFloat();
        this.speed = buf.readFloat();
        this.isProgressing = buf.readBoolean();
        this.tanks[0].deserialize(buf);
        this.tanks[1].deserialize(buf);
    }

    public int getFuelScaled(int pixels) {
        return Math.round(this.fuel * pixels / (float) MAX_FUEL);
    }

    public int getProgressScaled(int pixels) {
        return Math.round(this.progress * pixels);
    }

    @Override public boolean canConnect(FluidType type, Direction dir) { return dir != null && dir.getAxis().isHorizontal(); }
    @Override public FluidTank[] getReceivingTanks() { return new FluidTank[] { this.tanks[0] }; }
    @Override public FluidTank[] getSendingTanks() { return new FluidTank[] { this.tanks[1] }; }
    @Override public FluidTank[] getAllTanks() { return this.tanks; }

    @Override public int getFloorCount() { return 4; }
    @Override public BlockPos getFloorPosFromIndex(int index) { return this.standardFloor3x3(index); }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineBlastFurnaceMenu(id, inventory, this);
    }
}
