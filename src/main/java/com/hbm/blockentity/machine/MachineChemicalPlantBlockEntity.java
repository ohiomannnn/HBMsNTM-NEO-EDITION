package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineChemicalPlantMenu;
import com.hbm.inventory.recipes.ChemicalPlantRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.main.NuclearTechMod;
import com.hbm.module.machine.ModuleMachineChemicalPlant;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.BobMathUtil;
import com.hbm.util.fauxpointtwelve.DirPos;
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
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.List;

public class MachineChemicalPlantBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IUpgradeInfoProvider, IControlReceiver {

    public final FluidTank[] inputTanks = new FluidTank[3];
    public final FluidTank[] outputTanks = new FluidTank[3];

    public long power;
    public long maxPower = 100_000;
    public boolean didProcess = false;
    public boolean frame = false;
    public int anim;
    public int prevAnim;
    private AudioWrapper audio;

    public final ModuleMachineChemicalPlant chemicalModule;
    public final UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    private AABB renderBox;

    public MachineChemicalPlantBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_CHEMICAL_PLANT.get(), pos, state, 22);

        for(int i = 0; i < 3; i++) {
            this.inputTanks[i] = new FluidTank(Fluids.NONE, 24_000);
            this.outputTanks[i] = new FluidTank(Fluids.NONE, 24_000);
        }

        this.chemicalModule = new ModuleMachineChemicalPlant(0, this, this.slots)
                .itemInput(4, 5, 6)
                .itemOutput(7, 8, 9)
                .fluidInput(this.inputTanks[0], this.inputTanks[1], this.inputTanks[2])
                .fluidOutput(this.outputTanks[0], this.outputTanks[1], this.outputTanks[2]);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machineChemicalPlant");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(this.maxPower <= 0) {
            this.maxPower = 1_000_000;
        }

        if(!this.level.isClientSide) {
            GenericRecipe recipe = ChemicalPlantRecipes.INSTANCE.recipeNameMap.get(this.chemicalModule.recipe);
            if(recipe != null) {
                this.maxPower = recipe.power * 100;
            }

            this.maxPower = BobMathUtil.max(this.power, this.maxPower, 100_000);
            this.power = Library.chargeTEFromItems(this.slots, 0, this.power, this.maxPower);
            this.upgradeManager.checkSlots(this.slots, 2, 3);

            if(recipe != null && recipe.inputFluid != null) {
                for(int i = 0; i < Math.min(3, recipe.inputFluid.length); i++) {
                    this.inputTanks[i].loadTank(this.level, 10 + i, 13 + i, this.slots);
                }
            }

            this.outputTanks[0].unloadTank(this.level, 16, 19, this.slots);
            this.outputTanks[1].unloadTank(this.level, 17, 20, this.slots);
            this.outputTanks[2].unloadTank(this.level, 18, 21, this.slots);

            for(DirPos pos : this.getConPos()) {
                this.trySubscribe(this.level, pos);

                for(FluidTank tank : this.inputTanks) {
                    if(tank.getTankType() != Fluids.NONE) {
                        this.trySubscribe(tank.getTankType(), this.level, pos);
                    }
                }

                for(FluidTank tank : this.outputTanks) {
                    if(tank.getFill() > 0) {
                        this.tryProvide(tank, this.level, pos);
                    }
                }
            }

            double speed = 1D;
            double pow = 1D;
            speed += Math.min(this.upgradeManager.getLevel(UpgradeType.SPEED), 3) / 3D;
            speed += Math.min(this.upgradeManager.getLevel(UpgradeType.OVERDRIVE), 3);
            pow -= Math.min(this.upgradeManager.getLevel(UpgradeType.POWER), 3) * 0.25D;
            pow += Math.min(this.upgradeManager.getLevel(UpgradeType.SPEED), 3);
            pow += Math.min(this.upgradeManager.getLevel(UpgradeType.OVERDRIVE), 3) * 10D / 3D;

            this.chemicalModule.update(speed, pow, true, this.slots.get(1));
            this.didProcess = this.chemicalModule.didProcess;
            if(this.chemicalModule.markDirty) {
                this.setChanged();
            }

            this.networkPackNT(100);
        } else {
            this.prevAnim = this.anim;
            if(this.didProcess) {
                this.anim++;
            }

            if(this.level.getGameTime() % 20 == 0) {
                this.frame = !this.level.getBlockState(this.worldPosition.above(3)).isAir();
            }

            if(this.didProcess && NuclearTechMod.proxy.me() != null && Math.sqrt(NuclearTechMod.proxy.me().distanceToSqr(this.getBlockPos().getBottomCenter())) < 30) {
                if(this.audio == null) {
                    this.audio = this.createAudioLoop();
                    this.audio.startSound();
                } else if(!this.audio.isPlaying()) {
                    this.audio = rebootAudio(this.audio);
                }

                this.audio.keepAlive();
                this.audio.updateVolume(this.getVolume(1F));
            } else if(this.audio != null) {
                this.audio.stopSound();
                this.audio = null;
            }
        }
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.CHEMICAL_PLANT_OPERATE.get(), SoundSource.BLOCKS, this, 1F, 15F, 1F, 20);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if(this.audio != null) {
            this.audio.stopSound();
            this.audio = null;
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(this.audio != null) {
            this.audio.stopSound();
            this.audio = null;
        }
    }

    public DirPos[] getConPos() {
        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();
        return new DirPos[] {
                new DirPos(x + 2, y, z - 1, Library.POS_X),
                new DirPos(x + 2, y, z, Library.POS_X),
                new DirPos(x + 2, y, z + 1, Library.POS_X),
                new DirPos(x - 2, y, z - 1, Library.NEG_X),
                new DirPos(x - 2, y, z, Library.NEG_X),
                new DirPos(x - 2, y, z + 1, Library.NEG_X),
                new DirPos(x - 1, y, z + 2, Library.POS_Z),
                new DirPos(x, y, z + 2, Library.POS_Z),
                new DirPos(x + 1, y, z + 2, Library.POS_Z),
                new DirPos(x - 1, y, z - 2, Library.NEG_Z),
                new DirPos(x, y, z - 2, Library.NEG_Z),
                new DirPos(x + 1, y, z - 2, Library.NEG_Z)
        };
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        for(FluidTank tank : this.inputTanks) tank.serialize(buf);
        for(FluidTank tank : this.outputTanks) tank.serialize(buf);
        buf.writeLong(this.power);
        buf.writeLong(this.maxPower);
        buf.writeBoolean(this.didProcess);
        this.chemicalModule.serialize(buf);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        for(FluidTank tank : this.inputTanks) tank.deserialize(buf);
        for(FluidTank tank : this.outputTanks) tank.deserialize(buf);
        this.power = buf.readLong();
        this.maxPower = buf.readLong();
        this.didProcess = buf.readBoolean();
        this.chemicalModule.deserialize(buf);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        for(int i = 0; i < 3; i++) {
            this.inputTanks[i].readFromNBT(tag, "i" + i);
            this.outputTanks[i].readFromNBT(tag, "o" + i);
        }
        this.power = tag.getLong("power");
        this.maxPower = tag.getLong("maxPower");
        this.chemicalModule.readFromNBT(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        for(int i = 0; i < 3; i++) {
            this.inputTanks[i].writeToNBT(tag, "i" + i);
            this.outputTanks[i].writeToNBT(tag, "o" + i);
        }
        tag.putLong("power", this.power);
        tag.putLong("maxPower", this.maxPower);
        this.chemicalModule.writeToNBT(tag);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return true;
        if(slot == 1 && stack.getItem() == NtmItems.BLUEPRINTS.get()) return true;
        if(slot >= 2 && slot <= 3 && stack.getItem() instanceof MachineUpgradeItem) return true;
        if(slot >= 10 && slot <= 12) return true;
        if(slot >= 16 && slot <= 18) return true;
        return this.chemicalModule.isItemValid(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index >= 7 && index <= 9;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] {4, 5, 6, 7, 8, 9};
    }

    @Override public long getPower() { return this.power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return this.maxPower; }

    @Override public FluidTank[] getReceivingTanks() { return this.inputTanks; }
    @Override public FluidTank[] getSendingTanks() { return this.outputTanks; }
    @Override
    public FluidTank[] getAllTanks() {
        return new FluidTank[] { this.inputTanks[0], this.inputTanks[1], this.inputTanks[2], this.outputTanks[0], this.outputTanks[1], this.outputTanks[2] };
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineChemicalPlantMenu(id, inventory, this);
    }

    @Override public boolean hasPermission(Player player) { return this.stillValid(player); }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("index") && tag.contains("selection")) {
            int index = tag.getInt("index");
            if(index == 0) {
                this.chemicalModule.recipe = tag.getString("selection");
                this.setChanged();
            }
        }
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED || type == UpgradeType.POWER || type == UpgradeType.OVERDRIVE;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new HashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.POWER, 3);
        upgrades.put(UpgradeType.OVERDRIVE, 3);
        return upgrades;
    }

    public AABB getRenderBoundingBox() {
        if(this.renderBox == null) {
            int x = this.worldPosition.getX();
            int y = this.worldPosition.getY();
            int z = this.worldPosition.getZ();
            this.renderBox = new AABB(x - 1, y, z - 1, x + 2, y + 3, z + 2);
        }
        return this.renderBox;
    }
}
