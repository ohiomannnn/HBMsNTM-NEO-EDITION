package com.hbm.blockentity.machine;

import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.menus.MachinePressMenu;
import com.hbm.inventory.recipes.PressRecipes;
import com.hbm.items.machine.StampItem;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.SoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class MachinePressBlockEntity extends MachineBaseBlockEntity {

    public int speed = 0; // speed ticks up once (or four times if preheated) when operating
    public static final int maxSpeed = 400; // max speed ticks for acceleration
    public static final int progressAtMax = 25; // max progress speed when hot
    public int burnTime = 0; // burn ticks of the loaded fuel, 200 ticks equal one operation

    public int press; // extension of the press, operation is completed if maxPress is reached
    public float renderPress; // client-side version of the press var, a float for smoother rendering
    public float lastPress; // for interp
    private int syncPress; // for interp
    private int turnProgress; // for interp 3: revenge of the sith
    public final static int maxPress = 200; // max tick count per operation assuming speed is 1
    boolean isRetracting = false; // direction the press is currently going
    private int delay; // delay between direction changes to look a bit more appealing

    public ItemStack syncStack = ItemStack.EMPTY;

    public MachinePressBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.PRESS.get(), pos, state, 13);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.press"); }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {

            boolean preheated = false;

            for(Direction dir : Direction.values()) {
                if(level.getBlockState(this.getBlockPos().relative(dir)).is(NtmBlocks.PRESS_PREHEATER.get())) {
                    preheated = true;
                    break;
                }
            }

            boolean canProcess = this.canProcess();

            if((canProcess || this.isRetracting) && this.burnTime >= 200) {
                this.speed += preheated ? 4 : 1;

                if(this.speed > maxSpeed) {
                    this.speed = maxSpeed;
                }
            } else {
                this.speed -= 1;
                if(this.speed < 0) {
                    this.speed = 0;
                }
            }

            if(delay <= 0) {

                int stampSpeed = speed * progressAtMax / maxSpeed;

                if(this.isRetracting) {
                    this.press -= stampSpeed;

                    if(this.press <= 0) {
                        this.isRetracting = false;
                        this.delay = 5;
                    }
                } else if(canProcess) {
                    this.press += stampSpeed;

                    if(this.press >= maxPress) {
                        SoundUtils.playAtVec3(this.level, this.getBlockPos().getCenter(), NtmSoundEvents.PRESS_OPERATE.get(), SoundSource.BLOCKS, this.getVolume(1.5F), 1F);
                        ItemStack output = PressRecipes.getOutput(this.slots.get(2), this.slots.get(1));
                        if(this.slots.get(3).isEmpty()) {
                            this.slots.set(3, output.copy());
                        } else {
                            this.slots.get(3).grow(output.getCount());
                        }
                        this.removeItem(2, 1);

                        if(this.slots.get(1).getMaxDamage() != 0) {
                            ItemStack stackToDamage = this.slots.get(1);
                            int damage = stackToDamage.getDamageValue() + 1;
                            stackToDamage.setDamageValue(damage);
                            if(damage >= stackToDamage.getMaxDamage()) {
                                stackToDamage.shrink(1);
                            }
                        }

                        this.isRetracting = true;
                        this.delay = 5;
                        if(this.burnTime >= 200) this.burnTime -= 200; // only subtract fuel if operation was actually successful

                        this.setChanged();
                    }
                } else if(this.press > 0) {
                    this.isRetracting = true;
                }
            } else {
                delay--;
            }

            if(burnTime < 200 && this.slots.get(0).getBurnTime(null) > 0) { // less than one operation stored? burn more fuel!
                burnTime += this.slots.get(0).getBurnTime(null);

                if(this.slots.get(0).getCount() == 1 && this.slots.get(0).hasCraftingRemainingItem()) {
                    this.slots.set(0, this.slots.get(0).getCraftingRemainingItem().copy());
                } else {
                    this.removeItem(0, 1);
                }
                this.setChanged();
            }

            this.networkPackNT(50);

        } else {

            // approach-based interpolation, GO!
            this.lastPress = this.renderPress;

            if(this.turnProgress > 0) {
                this.renderPress = this.renderPress + ((this.syncPress - this.renderPress) / (float) this.turnProgress);
                --this.turnProgress;
            } else {
                this.renderPress = this.syncPress;
            }
        }
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);

        buf.writeInt(this.speed);
        buf.writeInt(this.burnTime);
        buf.writeInt(this.press);
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, this.slots.get(2));
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);

        this.speed = buf.readInt();
        this.burnTime = buf.readInt();
        this.syncPress = buf.readInt();
        this.syncStack = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);

        this.turnProgress = 2;
    }

    public boolean canProcess() {
        if(burnTime < 200) return false;
        if(slots.get(1).isEmpty() || slots.get(2).isEmpty()) return false;

        ItemStack output = PressRecipes.getOutput(slots.get(2), slots.get(1));
        if(output.isEmpty()) return false;

        ItemStack outSlot = slots.get(3);
        if(outSlot.isEmpty()) return true;

        if(outSlot.getCount() + output.getCount() <= outSlot.getMaxStackSize() && ItemStack.isSameItemSameComponents(outSlot, output)) return true;
        return false;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {

        if(stack.getItem() instanceof StampItem) return slot == 1;

        if(stack.getBurnTime(null) > 0 && slot == 0) return true;

        return slot == 2 || slot >= 4;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { 0, 1, 2, 3 };
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 3;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachinePressMenu(id, inventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putInt("press", this.press);
        tag.putInt("burnTime", this.burnTime);
        tag.putInt("speed", this.speed);
        tag.putBoolean("ret", this.isRetracting);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.press = tag.getInt("press");
        this.burnTime = tag.getInt("burnTime");
        this.speed = tag.getInt("speed");
        this.isRetracting = tag.getBoolean("ret");
    }

}
