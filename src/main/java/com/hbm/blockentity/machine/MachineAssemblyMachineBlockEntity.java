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
import com.hbm.inventory.menus.MachineAssemblyMachineMenu;
import com.hbm.inventory.recipes.AssemblyMachineRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.main.NuclearTechMod;
import com.hbm.module.machine.ModuleMachineAssembler;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.BobMathUtil;
import com.hbm.util.fauxpointtwelve.DirPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;

public class MachineAssemblyMachineBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2, IUpgradeInfoProvider, IControlReceiver {

    public FluidTank inputTank;
    public FluidTank outputTank;

    public long power;
    public long maxPower = 100_000;
    public boolean didProcess = false;

    public boolean frame = false;
    private AudioWrapper audio;

    public ModuleMachineAssembler assemblerModule;

    public AssemblerArm[] arms = new AssemblerArm[2];
    public float prevRing;
    public float ring;
    public float ringSpeed;
    public float ringTarget;
    public int ringDelay;

    public UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    public MachineAssemblyMachineBlockEntity(BlockPos pos, BlockState blockState) {
        super(NtmBlockEntityTypes.ASSEMBLY_MACHINE.get(), pos, blockState, 17);

        this.inputTank = new FluidTank(Fluids.NONE, 4_000);
        this.outputTank = new FluidTank(Fluids.NONE, 4_000);

        for(int i = 0; i < this.arms.length; i++) this.arms[i] = new AssemblerArm();

        this.assemblerModule = new ModuleMachineAssembler(0, this, slots)
                .itemInput(4).itemOutput(16)
                .fluidInput(inputTank).fluidOutput(outputTank);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_assembly_machine");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(maxPower <= 0) this.maxPower = 1_000_000;

        if(!level.isClientSide) {
            GenericRecipe recipe = AssemblyMachineRecipes.INSTANCE.recipeNameMap.get(assemblerModule.recipe);
            if(recipe != null) {
                this.maxPower = recipe.power * 100;
            }
            this.maxPower = BobMathUtil.max(this.power, this.maxPower, 100_000);

            this.power = Library.chargeTEFromItems(slots, 0, power, maxPower);
            upgradeManager.checkSlots(slots, 2, 3);

            for(DirPos pos : getConPos()) {
                this.trySubscribe(level, pos);
                if(inputTank.getTankType() != Fluids.NONE) this.trySubscribe(inputTank.getTankType(), level, pos);
                if(outputTank.getFill() > 0) this.tryProvide(outputTank, level, pos);
            }

            double speed = 1D;
            double pow = 1D;

            speed += Math.min(upgradeManager.getLevel(UpgradeType.SPEED), 3) / 3D;
            speed += Math.min(upgradeManager.getLevel(UpgradeType.OVERDRIVE), 3);

            pow -= Math.min(upgradeManager.getLevel(UpgradeType.POWER), 3) * 0.25D;
            pow += Math.min(upgradeManager.getLevel(UpgradeType.SPEED), 3) * 1D;
            pow += Math.min(upgradeManager.getLevel(UpgradeType.OVERDRIVE), 3) * 10D / 3D;

            this.assemblerModule.update(speed, pow, true, slots.get(1));
            this.didProcess = this.assemblerModule.didProcess;
            if(this.assemblerModule.markDirty) this.setChanged();

//            if(didProcess) {
//                if(slots[0] != null && slots[0].getItem() == ModItems.meteorite_sword_alloyed)
//                    slots[0] = new ItemStack(ModItems.meteorite_sword_machined);
//            }

            this.networkPackNT(100);
        } else {

            if(level.getGameTime() % 20 == 0) {
                frame = !level.getBlockState(this.worldPosition.above(3)).isAir();
            }

            if(this.didProcess && Math.sqrt(NuclearTechMod.proxy.me().distanceToSqr(this.getBlockPos().getBottomCenter())) < 50) {
                if(audio == null) {
                    audio = createAudioLoop();
                    audio.startSound();
                } else if(!audio.isPlaying()) {
                    audio = rebootAudio(audio);
                }
                audio.keepAlive();
                audio.updatePitch(0.75F);
                audio.updateVolume(this.getVolume(0.5F));

            } else {
                if(audio != null) {
                    audio.stopSound();
                    audio = null;
                }
            }

            for(AssemblerArm arm : arms) {
                arm.updateInterp();
                if(didProcess) {
                    arm.updateArm();
                } else{
                    arm.returnToNullPos();
                }

                if(!this.muffled && arm.prevAngles[3] != arm.angles[3] && arm.angles[3] == -0.75) {
                    NuclearTechMod.proxy.playLocalSound(this.getBlockPos().getCenter(), NtmSoundEvents.ASSEMBLER_STRIKE.get(), SoundSource.BLOCKS, this.getVolume(0.5F), 1F);
                }
            }

            this.prevRing = this.ring;

            if(didProcess) {
                if(this.ring != this.ringTarget) {
                    double ringDelta = Math.abs(this.ringTarget - this.ring);
                    if(ringDelta <= this.ringSpeed) this.ring = this.ringTarget;
                    if(this.ringTarget > this.ring) this.ring += this.ringSpeed;
                    if(this.ringTarget < this.ring) this.ring -= this.ringSpeed;
                    if(this.ringTarget == this.ring) {
                        float sub = ringTarget >= 360 ? -360F : 360F;
                        this.ringTarget += sub;
                        this.ring += sub;
                        this.prevRing += sub;
                        this.ringDelay = 20 + level.random.nextInt(21);
                    }
                } else {
                    if(this.ringDelay > 0) this.ringDelay--;
                    if(this.ringDelay <= 0) {
                        this.ringTarget += (level.random.nextFloat() * 2 - 1) * 135;
                        this.ringSpeed = 10F + level.random.nextFloat() * 5F;
                        if(!this.muffled) NuclearTechMod.proxy.playLocalSound(this.getBlockPos().getCenter(), NtmSoundEvents.ASSEMBLER_START.get(), SoundSource.BLOCKS, this.getVolume(0.25F), 1.25F + level.random.nextFloat() * 0.25F);
                    }
                }
            }
        }
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.ELECTRIC_MOTOR_LOOP.get(), SoundSource.BLOCKS, this, 0.5F, 15F, 0.75F, 20);
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

    public DirPos[] getConPos() {

        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();

        return new DirPos[] {
                new DirPos(x + 2, y, z - 1, Library.POS_X),
                new DirPos(x + 2, y, z + 0, Library.POS_X),
                new DirPos(x + 2, y, z + 1, Library.POS_X),
                new DirPos(x - 2, y, z - 1, Library.NEG_X),
                new DirPos(x - 2, y, z + 0, Library.NEG_X),
                new DirPos(x - 2, y, z + 1, Library.NEG_X),
                new DirPos(x - 1, y, z + 2, Library.POS_Z),
                new DirPos(x + 0, y, z + 2, Library.POS_Z),
                new DirPos(x + 1, y, z + 2, Library.POS_Z),
                new DirPos(x - 1, y, z - 2, Library.NEG_Z),
                new DirPos(x + 0, y, z - 2, Library.NEG_Z),
                new DirPos(x + 1, y, z - 2, Library.NEG_Z),
        };
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        this.inputTank.serialize(buf);
        this.outputTank.serialize(buf);
        buf.writeLong(power);
        buf.writeLong(maxPower);
        buf.writeBoolean(didProcess);
        this.assemblerModule.serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        boolean wasProcessing = this.didProcess;
        this.inputTank.deserialize(buf);
        this.outputTank.deserialize(buf);
        this.power = buf.readLong();
        this.maxPower = buf.readLong();
        this.didProcess = buf.readBoolean();
        this.assemblerModule.deserialize(buf);

        if(wasProcessing && !didProcess) {
            NuclearTechMod.proxy.playLocalSound(this.getBlockPos().getCenter(), NtmSoundEvents.ASSEMBLER_STOP.get(), SoundSource.BLOCKS, this.getVolume(0.25F), 1.5F);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.inputTank.readFromNBT(tag, "i");
        this.outputTank.readFromNBT(tag, "o");
        this.power = tag.getLong("power");
        this.maxPower = tag.getLong("maxPower");
        this.assemblerModule.readFromNBT(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        this.inputTank.writeToNBT(tag, "i");
        this.outputTank.writeToNBT(tag, "o");
        tag.putLong("power", power);
        tag.putLong("maxPower", maxPower);
        this.assemblerModule.writeToNBT(tag);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return true; // battery
        if(slot == 1 && stack.getItem() == NtmItems.BLUEPRINTS.get()) return true;
        if(slot >= 2 && slot <= 3 && stack.getItem() instanceof MachineUpgradeItem) return true; // upgrades
        if(this.assemblerModule.isItemValid(slot, stack)) return true; // recipe input crap
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] { 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }

    @Override public FluidTank[] getReceivingTanks() { return new FluidTank[] { inputTank }; }
    @Override public FluidTank[] getSendingTanks() { return new FluidTank[] { outputTank }; }
    @Override public FluidTank[] getAllTanks() { return new FluidTank[] { inputTank, outputTank }; }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineAssemblyMachineMenu(id, inventory, this);
    }

    @Override public boolean hasPermission(Player player) { return this.stillValid(player); }

    @Override
    public void receiveControl(CompoundTag tag) {
        if(tag.contains("index") && tag.contains("selection")) {
            int index = tag.getInt("index");
            String selection = tag.getString("selection");
            if(index == 0) {
                this.assemblerModule.recipe = selection;
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
        //info.add(IUpgradeInfoProvider.getStandardLabel(ModBlocks.machine_assembly_machine));
        //
        // if(type == UpgradeType.SPEED) {
        //
        //    info.add(EnumChatFormatting.GREEN + I18nUtil.resolveKey(KEY_SPEED, "+" + (level * 100 / 3) + "%"));
        //
        //    info.add(EnumChatFormatting.RED + I18nUtil.resolveKey(KEY_CONSUMPTION, "+" + (level * 50) + "%"));
        //
        // }
        //
        // if(type == UpgradeType.POWER) {
        //
        //    info.add(EnumChatFormatting.GREEN + I18nUtil.resolveKey(KEY_CONSUMPTION, "-" + (level * 25) + "%"));
        //
        // }
        //
        // if(type == UpgradeType.OVERDRIVE) {
        //
        //    info.add((BobMathUtil.getBlink() ? EnumChatFormatting.RED : EnumChatFormatting.DARK_GRAY) + "YES");
        //
        // }
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new HashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.POWER, 3);
        upgrades.put(UpgradeType.OVERDRIVE, 3);
        return upgrades;
    }

    public static class AssemblerArm {

        public float[] angles = new float[4];
        public float[] prevAngles = new float[4];
        public float[] targetAngles = new float[4];
        public float[] speed = new float[4];

        RandomSource rand = RandomSource.create();
        ArmActionState state = ArmActionState.ASSUME_POSITION;
        int actionDelay = 0;

        public enum ArmActionState {
            ASSUME_POSITION,
            EXTEND_STRIKER,
            RETRACT_STRIKER
        }

        public AssemblerArm() {
            this.resetSpeed();
        }

        private void updateInterp() {
            for(int i = 0; i < angles.length; i++) {
                prevAngles[i] = angles[i];
            }
        }

        private void returnToNullPos() {
            for(int i = 0; i < 4; i++) this.targetAngles[i] = 0;
            for(int i = 0; i < 3; i++) this.speed[i] = 3;
            this.speed[3] = 0.25F;
            this.state = ArmActionState.RETRACT_STRIKER;

            this.move();
        }

        private void resetSpeed() {
            speed[0] = 15F;	    //Pivot
            speed[1] = 15F;	    //Arm
            speed[2] = 15F;	    //Piston
            speed[3] = 0.5F;	//Striker
        }

        public void updateArm() {
            resetSpeed();

            if(actionDelay > 0) {
                actionDelay--;
                return;
            }

            switch(state) {
                // Move. If done moving, set a delay and progress to EXTEND
                case ASSUME_POSITION:
                    if(move()) {
                        actionDelay = 2;
                        state = ArmActionState.EXTEND_STRIKER;
                        targetAngles[3] = -0.75F;
                    }
                    break;
                case EXTEND_STRIKER:
                    if(move()) {
                        state = ArmActionState.RETRACT_STRIKER;
                        targetAngles[3] = 0F;
                    }
                    break;
                case RETRACT_STRIKER:
                    if(move()) {
                        actionDelay = 2 + rand.nextInt(5);
                        chooseNewArmPoistion();
                        state = ArmActionState.ASSUME_POSITION;
                    }
                    break;

            }
        }

        private float[][] pos = new float[][] { // possible positions for the arms
                {45, -15, -5},
                {15, 15, -15},
                {25, 10, -15},
                {30, 0, -10},
                {70, -10, -25},
        }; // sure it's not truly random like with the old assemfac, but at least now the striker always hits the center and doesn't clip through the board

        public void chooseNewArmPoistion() {
            int chosen = rand.nextInt(pos.length);
            this.targetAngles[0] = pos[chosen][0];
            this.targetAngles[1] = pos[chosen][1];
            this.targetAngles[2] = pos[chosen][2];
        }

        private boolean move() {
            boolean didMove = false;

            for(int i = 0; i < angles.length; i++) {
                if(angles[i] == targetAngles[i])
                    continue;

                didMove = true;

                float angle = angles[i];
                float target = targetAngles[i];
                float turn = speed[i];
                float delta = Math.abs(angle - target);

                if(delta <= turn) {
                    angles[i] = targetAngles[i];
                    continue;
                }

                if(angle < target) {
                    angles[i] += turn;
                } else {
                    angles[i] -= turn;
                }
            }

            return !didMove;
        }

        public float[] getPositions(float interp) {
            return new float[] {
                    BobMathUtil.interp(this.prevAngles[0], this.angles[0], interp),
                    BobMathUtil.interp(this.prevAngles[1], this.angles[1], interp),
                    BobMathUtil.interp(this.prevAngles[2], this.angles[2], interp),
                    BobMathUtil.interp(this.prevAngles[3], this.angles[3], interp)
            };
        }
    }
}
