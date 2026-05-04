package com.hbm.blockentity.bomb;

import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.entity.ModEntityTypes;
import com.hbm.entity.logic.NukeExplosionBalefire;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.menus.NukeFstbmbMenu;
import com.hbm.items.NtmItems;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.particle.helper.NukeTorexCreator;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class NukeBalefireBlockEntity extends MachineBaseBlockEntity implements IControlReceiver {

    public boolean loaded;
    public boolean started;
    public int timer;

    public NukeBalefireBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.NUKE_FSTBMB.get(), pos, state, 2);
        timer = 18000;
    }

    @Override public Component getDefaultName() { return Component.translatable("container.nukeFstbmb"); }

    @Override public int getMaxStackSize() { return 1; }

    @Override
    public void updateEntity() {
        if (level == null) return;

        if (!level.isClientSide) {
            this.loaded = this.isLoaded();

            if (!loaded) {
                started = false;
            }

            if (started) {
                timer--;

                if (timer % 20 == 0) {
                    level.playSound(null, this.worldPosition, NtmSoundEvents.FSTBMB_PING.get(), SoundSource.PLAYERS, 5.0F, 1.0F);
                }
            }

            if (timer <= 0) {
                this.explode();
            }

            this.networkPackNT(250);
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);

        buf.writeInt(this.timer);
        buf.writeBoolean(this.started);
        buf.writeBoolean(this.loaded);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);

        this.timer = buf.readInt();
        this.started = buf.readBoolean();
        this.loaded = buf.readBoolean();
    }

    @Override public boolean hasPermission(Player player) { return this.stillValid(player); }

    @Override
    public void receiveControl(CompoundTag tag) {
        if (level == null) return;

        if (tag.contains("start") && this.isLoaded()) {
            level.playSound(null, this.worldPosition, NtmSoundEvents.FSTBMB_START.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            started = true;
        }
        if (tag.contains("setTimer")) {
            timer = tag.getInt("timer") * 20;
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        Item item = stack.getItem();
        return item == NtmItems.EGG_BALEFIRE.get() ||
                item == NtmItems.BATTERY_SPARK.get() ||
                item == NtmItems.BATTERY_TRIXITE.get();
    }

    @Override
    public boolean isLoaded() {
        return hasEgg() && hasBattery();
    }

    public boolean hasEgg() {
        return slots.get(0).is(NtmItems.EGG_BALEFIRE.get());
    }

    public boolean hasBattery() {
        return getBattery() > 0;
    }

    public int getBattery() {
        if (slots.get(1).is(NtmItems.BATTERY_SPARK.get())) return 1;
        if (slots.get(1).is(NtmItems.BATTERY_TRIXITE.get())) return 2;
        return 0;
    }

    public void explode() {
        if (level == null) return;

        this.slots.clear();
        this.level.setBlock(this.worldPosition, Blocks.AIR.defaultBlockState(), 3);

        NukeExplosionBalefire bf = new NukeExplosionBalefire(ModEntityTypes.NUKE_BALEFIRE.get(), level);
        bf.setPos(Vec3.atCenterOf(this.worldPosition));
        bf.destructionRange = 250;
        level.addFreshEntity(bf);
        NukeTorexCreator.statFacBale(level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, 250);
    }

    public String getMinutes() {

        String mins = "" + (timer / 1200);

        if (mins.length() == 1) mins = "0" + mins;

        return mins;
    }

    public String getSeconds() {

        String mins = "" + ((timer / 20) % 60);

        if (mins.length() == 1) mins = "0" + mins;

        return mins;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        started = tag.getBoolean("Started");
        timer = tag.getInt("Timer");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putBoolean("Started", started);
        tag.putInt("Timer", timer);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new NukeFstbmbMenu(id, inventory, this);
    }
}
