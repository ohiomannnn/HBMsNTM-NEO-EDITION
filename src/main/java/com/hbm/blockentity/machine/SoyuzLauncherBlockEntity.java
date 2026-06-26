package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardReceiverMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.entity.missile.Soyuz;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.SoyuzLauncherMenu;
import com.hbm.items.IDesignatorItem;
import com.hbm.items.ISatChip;
import com.hbm.items.NtmItems;
import com.hbm.lib.Library;
import com.hbm.main.NuclearTechModClient;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.SoundUtils;
import com.hbm.util.TagsUtil;
import com.hbm.util.fauxpointtwelve.DirPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class SoyuzLauncherBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardReceiverMK2, IFluidCopiable, IControlReceiver {

    public long power;
    public long maxPower = 1_000_000;
    public FluidTank[] tanks;
    //0: sat, 1: cargo
    public byte mode;
    public boolean starting;
    public int countdown;
    public static final int maxCount = 600;
    public byte rocketType = -1;

    private AudioWrapper audio;

    public SoyuzLauncherBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.SOYUZ_LAUNCHER.get(), pos, state, 27);

        tanks = new FluidTank[2];
        tanks[0] = new FluidTank(Fluids.KEROSENE, 128_000);
        tanks[1] = new FluidTank(Fluids.OXYGEN, 128_000);
    }

    @Override protected Component getDefaultName() { return Component.translatable("container.soyuz_launcher"); }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player, 8F);
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {

            if(level.getGameTime() % 20 == 0) {
                for(DirPos pos : this.getConPos()) {
                    this.trySubscribe(level, pos);
                    this.trySubscribe(tanks[0].getTankType(), level, pos);
                    this.trySubscribe(tanks[1].getTankType(), level, pos);
                }
            }

            tanks[0].loadTank(level, 4, 5, slots);
            tanks[1].loadTank(level, 6, 7, slots);

            power = Library.chargeTEFromItems(slots, 8, power, maxPower);

            if(!starting || !this.canLaunch()) {
                countdown = maxCount;
                starting = false;
            } else if(countdown > 0) {
                countdown--;

                if(countdown % 100 == 0 && countdown > 0) SoundUtils.playAtBlockPos(level, this.getBlockPos(), NtmSoundEvents.ALARM_HATCH.get(), SoundSource.RECORDS, 100F, 1.1F);

            } else {
                this.liftOff();
            }

            this.networkPackNT(250);
        } else {

            if(!starting || !canLaunch()) {

                if(audio != null) {
                    audio.stopSound();
                    audio = null;
                }

                countdown = maxCount;

            } else if(countdown > 0) {

                if(audio == null) {
                    audio = this.createAudioLoop();
                    audio.updateVolume(100F);
                    audio.startSound();
                } else if(!audio.isPlaying()) {
                    audio = this.rebootAudio(audio);
                }
                audio.keepAlive();

                countdown--;
            }

            int x = this.getBlockPos().getX();
            int y = this.getBlockPos().getY();
            int z = this.getBlockPos().getZ();

            List<Soyuz> entities = this.level.getEntitiesOfClass(Soyuz.class, new AABB(x - 0.5, y, z - 0.5, x + 1.5, y + 10, z + 1.5));

            if(!entities.isEmpty()) {

                CompoundTag tag = new CompoundTag();
                tag.putString("type", "smoke");
                tag.putString("mode", "shockRand");
                tag.putInt("count", 50);
                tag.putDouble("strength", this.level.random.nextGaussian() * 3 + 6);
                tag.putDouble("posX", x + 0.5);
                tag.putDouble("posY", y - 3);
                tag.putDouble("posZ", z + 0.5);

                NuclearTechModClient.effectNT(tag);
            }
        }
    }

    protected List<DirPos> conPos;

    protected List<DirPos> getConPos() {

        if(conPos != null) return conPos;

        this.conPos = new ArrayList<>();

        for(Direction dir : new Direction[] {Library.POS_X, Library.POS_Z, Library.NEG_X, Library.NEG_Z}) {
            Direction rot = dir.getClockWise(Axis.Y);

            for(int i = -6; i <= 6; i++) {
                conPos.add(new DirPos(this.getBlockPos().relative(dir, 7).relative(rot, i), dir));
                conPos.add(new DirPos(this.getBlockPos().relative(dir, 7).relative(rot, i).offset(0, -1, 0), dir));
            }
        }

        return conPos;
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.SOYUZ_READY.get(), SoundSource.BLOCKS, this, 2.0F, 100F, 1.0F, 20);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();

        if(audio != null) { audio.stopSound(); audio = null; }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if(audio != null) { audio.stopSound(); audio = null; }
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(power);
        buf.writeByte(mode);
        buf.writeBoolean(starting);
        buf.writeByte(this.getRocketType());
        tanks[0].serialize(buf);
        tanks[1].serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        power = buf.readLong();
        mode = buf.readByte();
        starting = buf.readBoolean();
        rocketType = buf.readByte();
        tanks[0].deserialize(buf);
        tanks[1].deserialize(buf);
    }

    public void startCountdown() {

        if(this.canLaunch()) this.starting = true;
    }

    public void liftOff() {
        if(this.level == null) return;

        this.starting = false;

        int req = this.getFuelRequired();
        int pow = this.getPowerRequired();

        Soyuz soyuz = new Soyuz(this.level);
        soyuz.setSkin(this.getRocketType());
        soyuz.mode = this.mode;
        soyuz.moveTo(this.getBlockPos().getBottomCenter().add(0.0, 1.0, 0.0), 0F, 0F);
        this.level.addFreshEntity(soyuz);

        SoundUtils.playAtBlockPos(this.level, this.getBlockPos(), NtmSoundEvents.SOYUZ_TAKE_OFF.get(), SoundSource.AMBIENT, 100F, 1.1F);

        tanks[0].setFill(tanks[0].getFill() - req);
        tanks[1].setFill(tanks[1].getFill() - req);
        power -= pow;

        if(mode == 0) {
            soyuz.setSat(slots.get(2));
            if(this.orbital() == 2) slots.set(3, ItemStack.EMPTY);
            this.setItem(2, ItemStack.EMPTY);
        }

        if(mode == 1) {
            NonNullList<ItemStack> payload = NonNullList.withSize(27, ItemStack.EMPTY);

            for(int i = 9; i < 27; i++) {
                payload.add(slots.get(i));
                this.setItem(i, ItemStack.EMPTY);
            }

            soyuz.targetX = TagsUtil.getCData(this.slots.get(1)).getInt("x");
            soyuz.targetZ = TagsUtil.getCData(this.slots.get(1)).getInt("z");
            soyuz.setPayload(payload);
        }

        this.setItem(0, ItemStack.EMPTY);
    }

    public boolean canLaunch() {
        return this.hasRocket() && this.hasFuel() && this.hasRocket() && this.hasPower() && this.designator() != 1 && this.orbital() != 1 && this.satellite() != 1;
    }

    public boolean hasFuel() {
        return tanks[0].getFill() >= this.getFuelRequired();
    }

    public boolean hasOxy() {
        return tanks[1].getFill() >= this.getFuelRequired();
    }

    public int getFuelRequired() {
        if(mode == 1) return Math.min(5000 + this.getDist(), 128_000);
        return 128_000;
    }

    public int getDist() {

        if(designator() == 2) {
            int x = TagsUtil.getCData(this.slots.get(1)).getInt("x");
            int z = TagsUtil.getCData(this.slots.get(1)).getInt("z");

            return (int) new Vec3(this.getBlockPos().getX() - x, 0, this.getBlockPos().getZ() - z).length();
        }

        return 0;
    }

    public boolean hasPower() {
        return power >= this.getPowerRequired();
    }

    public int getPowerRequired() {
        return (int) (this.maxPower * 0.75);
    }

    private byte getRocketType() {
        if(!this.hasRocket()) return -1;
        return (byte) MetaHelper.getMeta(slots.get(0));
    }

    public long getPowerScaled(long i) {
        return (power * i) / this.maxPower;
    }

    public boolean hasRocket() {
        return slots.get(0).is(NtmItems.MISSILE_SOYUZ.get());
    }

    //0: designator not required
    //1: designator required but not present
    //2: designator present
    public int designator() {
        if(this.level == null) return 0;

        if(mode == 0) return 0;

        if(slots.get(1).getItem() instanceof IDesignatorItem idi && idi.isReady(this.level, slots.get(1), this.getBlockPos())) {
            return 2;
        }

        return 1;
    }

    //0: sat not required
    //1: sat required but not present
    //2: sat present
    public int satellite() {
        if(mode == 1) return 0;
        if(!slots.get(2).isEmpty()) return 2;
        return 1;
    }

    //0: module not required
    //1: module required but not present
    //2: module present
    public int orbital() {

        if(mode == 1) return 0;

        // todo make sats
//        if(slots[2] != null && (slots[2].getItem() == ModItems.sat_gerald || slots[2].getItem() == ModItems.sat_lunar_miner)) {
//            if(slots[3] != null && slots[3].getItem() == ModItems.missile_soyuz_lander)
//                return 2;
//            return 1;
//        }
        return 0;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        this.tanks[0].writeToNBT(tag, "fuel");
        this.tanks[1].writeToNBT(tag, "oxidizer");
        tag.putLong("power", this.power);
        tag.putLong("maxPower", this.maxPower);
        tag.putByte("mode", this.mode);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.tanks[0].readFromNBT(tag, "fuel");
        this.tanks[1].readFromNBT(tag, "oxidizer");
        this.power = tag.getLong("power");
        this.maxPower = tag.getLong("maxPower");
        this.mode = tag.getByte("mode");
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
//        if(slot == 0 && stack.getItem() == NtmItems.MISSILE_SOYUZ.get()) return true;
//        if(slot >= 1 && slot <= 2 && stack.getItem() instanceof ISatChip) return true;
//        if(slot == 3 && stack == FluidContainerRegistry.getFullContainer(stack, this.tanks[0].getTankType())) return true;
//        if(slot == 4 && stack == FluidContainerRegistry.getFullContainer(stack, this.tanks[1].getTankType())) return true;
//        if(slot == 8) return true;
//        if(slot > 9) return true;
        return true;
    }

    @Override public long getPower() { return this.power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }

    @Override public FluidTank[] getReceivingTanks() {return tanks; }
    @Override public FluidTank[] getAllTanks() { return tanks; }

    @Override
    public FluidTank getTankToPaste() {
        return null;
    }

    @Override
    public boolean canConnect(FluidType type, Direction dir) {
        return dir != null && dir != Direction.UP && dir != Direction.DOWN;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new SoyuzLauncherMenu(id, inventory, this);
    }

    @Override public boolean hasPermission(Player player) { return this.stillValid(player); }

    @Override
    public void receiveControl(CompoundTag tag) {

        if(tag.contains("mode")) this.mode = tag.getByte("mode");
        if(tag.getBoolean("start")) this.startCountdown();

        this.setChanged();
    }
}
