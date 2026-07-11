package com.hbm.blockentity.bomb;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
import com.hbm.entity.missile.MissileBase;
import com.hbm.items.weapon.MissileItem;
import com.hbm.items.weapon.MissileItem.MissileFormFactor;
import com.hbm.lib.Library;
import com.hbm.particle.NtmParticles;
import com.hbm.particle.vanilla.NbtParticleOptions;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.fauxpointtwelve.DirPos;
import com.hbm.util.particle.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class LaunchPadLargeBlockEntity extends LaunchPadBaseBlockEntity {

    public int formFactor = -1;
    /** Whether the missile has already been placed on the launchpad. Missile will render statically on the pad if true */
    public boolean erected = false;
    /** Whether the missile can be lifted. Missile will not render at all if false and not erected */
    public boolean readyToLoad = false;
    /** Instead of setting erected to true outright, this makes it so that it ties into the delay,
     * which prevents a jerky transition due to the animation of the erector lagging behind a bit */
    public boolean scheduleErect = false;
    public float lift = 1F;
    public float erector = 90F;
    public float prevLift = 1F;
    public float prevErector = 90F;
    public float syncLift;
    public float syncErector;
    private int sync;
    /** Delay between erector movements */
    public int delay = 20;

    private AudioWrapper audioLift;
    private AudioWrapper audioErector;

    protected boolean liftMoving = false;
    protected boolean erectorMoving = false;

    public LaunchPadLargeBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.LAUNCH_PAD_LARGE.get(), pos, state);
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!this.level.isClientSide) {
            this.prevLift = this.lift;
            this.prevErector = this.erector;

            float erectorSpeed = 1.5F;
            float liftSpeed = 0.025F;

            if(this.isMissileValid()) {
                if(slots.get(0).getItem() instanceof MissileItem missile) {
                    this.formFactor = missile.formFactor.ordinal();

                    if(missile.formFactor == MissileFormFactor.ATLAS || missile.formFactor == MissileFormFactor.HUGE) {
                        erectorSpeed /= 2F;
                        liftSpeed /= 2F;
                    }
                }

                if(this.erector == 90F && this.lift == 1F) {
                    this.readyToLoad = true;
                }
            } else {
                readyToLoad = false;
                erected = false;
                delay = 20;
            }

            if(this.power >= 75_000) {
                if(delay > 0) {
                    delay--;

                    if(delay < 10 && scheduleErect) {
                        this.erected = true;
                        this.scheduleErect = false;
                    }

                    // if there is no missile or the missile isn't ready (i.e. the erector hasn't returned to zero position yet), retract
                    if(slots.get(0).isEmpty() || !readyToLoad) {
                        //fold back erector
                        if(erector < 90F) {
                            erector = Math.min(erector + erectorSpeed, 90F);
                            if(erector == 90F) delay = 20;
                            //extend lift
                        } else if(lift < 1F) {
                            lift = Math.min(lift + liftSpeed, 1F);
                            if(erector == 1F) {
                                //if the lift is fully extended, the loading can begin
                                readyToLoad = true;
                                delay = 20;
                            }
                        }
                    }

                } else {

                    //only extend if the erector isn't up yet and the missile can be loaded
                    if(!erected && readyToLoad) {
                        this.state = STATE_LOADING;

                        //first, rotate the erector
                        if(erector != 0F) {
                            erector = Math.max(erector - erectorSpeed, 0F);
                            if(erector == 0F) delay = 20;
                            //then retract the lift
                        } else if(lift > 0) {
                            lift = Math.max(lift - liftSpeed, 0F);
                            if(lift == 0F) {
                                //once the lift is at the bottom, the missile is deployed
                                scheduleErect = true;
                                delay = 20;
                            }
                        }
                    } else {
                        //first, fold back the erector
                        if(erector < 90F) {
                            erector = Math.min(erector + erectorSpeed, 90F);
                            if(erector == 90F) delay = 20;
                            //then extend the lift again
                        } else if(lift < 1F) {
                            lift = Math.min(lift + liftSpeed, 1F);
                            if(erector == 1F) {
                                //if the lift is fully extended, the loading can begin
                                readyToLoad = true;
                                delay = 20;
                            }
                        }
                    }
                }
            }

            if(!this.hasFuel() || !this.isMissileValid()) this.state = STATE_MISSING;
            if(this.erected && this.canLaunch()) this.state = STATE_READY;

            boolean prevLiftMoving = this.liftMoving;
            boolean prevErectorMoving = this.erectorMoving;
            this.liftMoving = false;
            this.erectorMoving = false;
            if(this.prevLift != this.lift) this.liftMoving = true;
            if(this.prevErector != this.erector) this.erectorMoving = true;

            if(prevLiftMoving && !this.liftMoving) level.playSound(null, this.getBlockPos(), NtmSoundEvents.WGH_STOP.get(), SoundSource.BLOCKS, 2F, 1F);
            if(prevErectorMoving && !this.erectorMoving) level.playSound(null, this.getBlockPos(), NtmSoundEvents.GARAGE_STOP.get(), SoundSource.BLOCKS, 2F, 1F);

        } else {

            this.prevLift = this.lift;
            this.prevErector = this.erector;

            if(this.sync > 0) {
                this.lift = this.lift + ((this.syncLift - this.lift) / (float) this.sync);
                this.erector = this.erector + ((this.syncErector - this.erector) / (float) this.sync);
                --this.sync;
            } else {
                this.lift = this.syncLift;
                this.erector = this.syncErector;
            }

            if(this.liftMoving) {
                if(this.audioLift == null || !this.audioLift.isPlaying()) {
                    this.audioLift = AudioWrapper.getLoopedSound(NtmSoundEvents.WGH_START.get(), SoundSource.BLOCKS, this, 0.75F, 25F, 1.0F, 5);
                    this.audioLift.startSound();
                }
                this.audioLift.keepAlive();
            } else {
                if(this.audioLift != null) {
                    this.audioLift.stopSound();
                    this.audioLift = null;
                }
            }

            if(this.erectorMoving) {
                if(this.audioErector == null || !this.audioErector.isPlaying()) {
                    this.audioErector = AudioWrapper.getLoopedSound(NtmSoundEvents.GARAGE_MOVE.get(), SoundSource.BLOCKS, this, 1.5F, 25F, 1.0F, 5);
                    this.audioErector.startSound();
                }
                this.audioErector.keepAlive();
            } else {
                if(this.audioErector != null) {
                    this.audioErector.stopSound();
                    this.audioErector = null;
                }
            }

            int x = this.getBlockPos().getX();
            int y = this.getBlockPos().getY();
            int z = this.getBlockPos().getZ();

            if(this.erected && (this.formFactor == MissileFormFactor.HUGE.ordinal() || this.formFactor == MissileFormFactor.ATLAS.ordinal()) && this.tanks[1].getFill() > 0) {
                CompoundTag tag = new CompoundTag();
                tag.putFloat("lift", 0F);
                tag.putFloat("base", 0.5F);
                tag.putFloat("max", 2F);
                tag.putInt("life", 70 + level.random.nextInt(30));
                tag.putBoolean("noWind", true);
                tag.putFloat("alphaMod", 2F);
                tag.putFloat("strafe", 0.05F);
                for(int i = 0; i < 3; i++) ParticleUtil.addParticle(this.level, new NbtParticleOptions(NtmParticles.COOLING_TOWER.get(), tag), x + 0.5 + level.random.nextGaussian() * 0.5, y + 2, z + 0.5 + level.random.nextGaussian() * 0.5, 0F, 0F, 0F);
            }

            List<MissileBase> entities = level.getEntitiesOfClass(MissileBase.class, new AABB(x - 0.5, y, z - 0.5, x + 1.5, y + 10, z + 1.5));

            if(!entities.isEmpty()) {
                for(int i = 0; i < 15; i++) {

                    Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
                    if(level.random.nextBoolean()) dir = dir.getOpposite();
                    float xd = (float) (level.random.nextGaussian() * 0.15F + 0.75) * dir.getStepX();
                    float zd = (float) (level.random.nextGaussian() * 0.15F + 0.75) * dir.getStepZ();

                    ParticleUtil.addParticle(this.level, NtmParticles.LAUNCH_SMOKE.get(), x + 0.5, y + 0.1, z + 0.5, xd, 0, zd);
                }
            }
        }

        super.updateEntity();
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);

        buf.writeBoolean(this.liftMoving);
        buf.writeBoolean(this.erectorMoving);
        buf.writeBoolean(this.erected);
        buf.writeBoolean(this.readyToLoad);
        buf.writeByte((byte) this.formFactor);
        buf.writeFloat(this.lift);
        buf.writeFloat(this.erector);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);

        this.liftMoving = buf.readBoolean();
        this.erectorMoving = buf.readBoolean();
        this.erected = buf.readBoolean();
        this.readyToLoad = buf.readBoolean();
        this.formFactor = buf.readByte();
        this.syncLift = buf.readFloat();
        this.syncErector = buf.readFloat();

        if(this.lift != this.syncLift || this.erector != this.syncErector) {
            this.sync = 3;
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, Provider registries) {
        super.loadAdditional(tag, registries);

        this.erected = tag.getBoolean("Erected");
        this.readyToLoad = tag.getBoolean("ReadyToLoad");
        this.lift = tag.getFloat("Lift");
        this.erector = tag.getFloat("Erector");
        this.formFactor = tag.getInt("FormFactor");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putBoolean("Erected", erected);
        tag.putBoolean("ReadyToLoad", readyToLoad);
        tag.putFloat("Lift", lift);
        tag.putFloat("Erector", erector);
        tag.putInt("FormFactor", formFactor);
    }

    @Override
    public void finalizeLaunch(Entity missile) {
        super.finalizeLaunch(missile);
        this.erected = false;
    }

    @Override
    public DirPos[] getConPos() {
        return new DirPos[] {
                new DirPos(this.getBlockPos().offset(+5, 0, -2), Library.POS_X),
                new DirPos(this.getBlockPos().offset(+5, 0, +2), Library.POS_X),
                new DirPos(this.getBlockPos().offset(-5, 0, -2), Library.NEG_X),
                new DirPos(this.getBlockPos().offset(-5, 0, +2), Library.NEG_X),
                new DirPos(this.getBlockPos().offset(-2, 0, +5), Library.POS_Z),
                new DirPos(this.getBlockPos().offset(+2, 0, +5), Library.POS_Z),
                new DirPos(this.getBlockPos().offset(-2, 0, -5), Library.NEG_Z),
                new DirPos(this.getBlockPos().offset(+2, 0, -5), Library.NEG_Z)
        };
    }

    @Override public boolean isReadyForLaunch() { return this.erected && this.readyToLoad; }
    @Override public double getLaunchOffset() { return 2.0; }
}
