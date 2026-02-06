package com.hbm.blockentity;

import com.hbm.HBMsNTMClient;
import com.hbm.blockentity.machine.LockableBaseBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.lib.Library;
import com.hbm.render.anim.HbmAnimations.Animation;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.fauxpointtwelve.BlockPosNT;
import com.hbm.util.fauxpointtwelve.RotationNT;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashSet;
import java.util.Set;

public class DoorGenericBlockEntity extends LockableBaseBlockEntity {

    public static final byte STATE_CLOSED =		0;
    public static final byte STATE_OPEN =		1;
    public static final byte STATE_CLOSING =	2;
    public static final byte STATE_OPENING =	3;

    //0: closed, 1: open, 2: closing, 3: opening
    public byte state = STATE_CLOSED;
    protected DoorDecl doorType;
    public int openTicks = 0;
    public long animStartTime = 0;
    public int redstonePower;
    public boolean shouldUseBB = false;
    private byte skinIndex = 0;

    public Set<BlockPos> activatedBlocks = new HashSet<>(4);

    private AudioWrapper audio;
    private AudioWrapper audio2;

    public Animation currentAnimation;

    public DoorGenericBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void updateEntity() {

        if (getDoorType().onDoorUpdate() != null) {
            getDoorType().onDoorUpdate().accept(this);
        }

        if (state == STATE_OPENING) {
            openTicks++;
            if (openTicks >= getDoorType().timeToOpen()) openTicks = getDoorType().timeToOpen();

        } else if(state == STATE_CLOSING) {
            openTicks--;
            if (openTicks <= 0) openTicks = 0;
        }

        if (level != null && !level.isClientSide) {
            BlockPos pos = this.getBlockPos();

            int[][] ranges = getDoorType().getDoorOpenRanges();
            Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);

            if (state == STATE_OPENING) {

                for (int i = 0; i < ranges.length; i++) {

                    int[] range = ranges[i];
                    BlockPosNT startPos = new BlockPosNT(range[0], range[1], range[2]);
                    float time = getDoorType().getDoorRangeOpenTime(openTicks, i);

                    for (int j = 0; j < Math.abs(range[3]); j++) {

                        if ((float)j / (Math.abs(range[3] - 1)) > time) break;

                        for (int k = 0; k < range[4]; k++) {
                            BlockPos add = new BlockPos(0, 0, 0);
                            add = switch (range[5]) {
                                case 0 -> new BlockPos(0, k, (int) Math.signum(range[3]) * j);
                                case 1 -> new BlockPos(k, (int) Math.signum(range[3]) * j, 0);
                                case 2 -> new BlockPos((int) Math.signum(range[3]) * j, k, 0);
                                default -> add;
                            };

                            Rotation r = RotationNT.getBlockRotation(dir);
                            if (dir == Library.POS_X || dir == Library.NEG_X)
                                r = r.getRotated(Rotation.CLOCKWISE_180);

                            BlockPosNT finalPos = startPos.add(add).rotate(r).add(pos);

                            if(finalPos.equals(pos)) {
                                this.shouldUseBB = false;
                            } else {
                                ((DummyableBlock)this.getBlockState().getBlock()).makeExtra(level, finalPos.makeCompat());
                            }
                        }
                    }
                }

            }
        }
    }

    @Override
    public void serialize(ByteBuf buf, RegistryAccess registryAccess) {
        buf.writeByte(state);
        buf.writeByte(skinIndex);
        buf.writeBoolean(shouldUseBB);
    }

    @Override
    public void deserialize(ByteBuf buf, RegistryAccess registryAccess) {
        handleNewState(buf.readByte());
        skinIndex = buf.readByte();
        shouldUseBB = buf.readBoolean();
    }

    @Override
    public void onChunkUnloaded() {
        if (audio != null) {
            audio.stopSound();
            audio = null;
        }
        if (audio2 != null) {
            audio2.stopSound();
            audio2 = null;
        }
    }

    public DoorDecl getDoorType() {

        //if (this.doorType == null && this.getBlockType() instanceof BlockDoorGeneric)
        //    this.doorType = ((BlockDoorGeneric)this.getBlockType()).type;

        return this.doorType;
    }

    public boolean tryToggle(Player player) {

        if (this.isLocked() && player == null) return false;

        if (state == STATE_CLOSED && redstonePower > 0) {
            //Redstone "power locks" doors, just like minecraft iron doors
            return false;
        }

        if (level != null) {
            if (this.state == STATE_CLOSED) {
                if (!level.isClientSide && canAccess(player)) this.state = STATE_OPENING;
                return true;

            } else if (this.state == STATE_OPEN) {
                if (!level.isClientSide && canAccess(player)) this.state = STATE_CLOSING;
                return true;
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleNewState(byte state) {

        if (level == null) return;
        int x = this.getBlockPos().getX();
        int y = this.getBlockPos().getX();
        int z = this.getBlockPos().getX();

        if (this.state != state) {
            DoorDecl doorType = getDoorType();

            if (this.state == STATE_CLOSED && state == STATE_OPENING) { // Door transitioning to open
                if (audio != null) {
                    audio.stopSound();
                    audio.setKeepAlive(0);
                }

                if (doorType.getOpenSoundLoop() != null) {
                    audio = HBMsNTMClient.getLoopedSound(doorType.getOpenSoundLoop(), SoundSource.BLOCKS, x, y, z, doorType.getSoundVolume(), 10F, 1F);
                    audio.startSound();
                }

                if (doorType.getOpenSoundStart() != null) {
                    level.playSound(null, x, y, z, doorType.getOpenSoundStart(), SoundSource.BLOCKS, doorType.getSoundVolume(), 1F);
                }

                if (doorType.getSoundLoop2() != null) {
                    if (audio2 != null) audio2.stopSound();

                    audio2 = HBMsNTMClient.getLoopedSound(doorType.getSoundLoop2(), SoundSource.BLOCKS, x, y, z, doorType.getSoundVolume(), 10F, 1F);
                    audio2.startSound();
                }
            }

            if (this.state == STATE_OPEN && state == STATE_CLOSING) { // Door transitioning to closed
                if (audio != null) {
                    audio.stopSound();
                }

                if (doorType.getCloseSoundLoop() != null) {
                    audio = HBMsNTMClient.getLoopedSound(doorType.getCloseSoundLoop(), SoundSource.BLOCKS, x, y, z, doorType.getSoundVolume(), 10F, 1F);
                    audio.startSound();
                }

                if (doorType.getCloseSoundStart() != null) {
                    level.playSound(null, x, y, z, doorType.getOpenSoundStart(), SoundSource.BLOCKS, doorType.getSoundVolume(), 1F);
                }

                if (doorType.getSoundLoop2() != null) {
                    if (audio2 != null) audio2.stopSound();

                    audio2 = HBMsNTMClient.getLoopedSound(doorType.getSoundLoop2(), SoundSource.BLOCKS, x, y, z, doorType.getSoundVolume(), 10F, 1F);
                    audio2.startSound();
                }
            }

            if (state == STATE_OPEN || state == STATE_CLOSED) { // Door finished any transition
                if (audio != null) {
                    audio.stopSound();
                    audio = null;
                }
                if (audio2 != null) {
                    audio2.stopSound();
                    audio2 = null;
                }
            }

            if (this.state == STATE_OPENING && state == STATE_OPEN) { // Door finished transitioning to open
                if (doorType.getOpenSoundEnd() != null) {
                    level.playSound(null, x, y, z, doorType.getOpenSoundEnd(), SoundSource.BLOCKS, doorType.getSoundVolume(), 1F);
                }
            }

            if (this.state == STATE_CLOSING && state == STATE_CLOSED) { // Door finished transitioning to closed
                if (doorType.getCloseSoundEnd() != null) {
                    level.playSound(null, x, y, z, doorType.getOpenSoundEnd(), SoundSource.BLOCKS, doorType.getSoundVolume(), 1F);
                }
            }


            this.state = state;

            if (state == STATE_OPENING || state == STATE_CLOSING) {
                animStartTime = System.currentTimeMillis();
                currentAnimation = this.doorType.getSEDNAAnim(state, this.skinIndex);
            }
        }
    }

    public int getSkinIndex() {
        return skinIndex;
    }

    public boolean cycleSkinIndex() {
        if (!getDoorType().hasSkins()) return false;
        this.skinIndex++;
        this.skinIndex %= (byte) getDoorType().getSkinCount();
        this.setChanged();
        return true;
    }

    /**Useful for logic block interactions, as a way to close/open doors**/
    public void open(){
        if (state == STATE_CLOSED) state = STATE_OPENING;
    }

    public void close() {
        if (state == STATE_OPEN) state = STATE_CLOSING;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if (audio != null) {
            audio.stopSound();
            audio = null;
        }
        if (audio2 != null) {
            audio2.stopSound();
            audio2 = null;
        }
    }

    public void updateRedstonePower(BlockPos pos) {
        // Drillgon200: Best I could come up with without having to use dummy tile entities
        boolean powered = level != null && level.hasNeighborSignal(pos);
        boolean contained = activatedBlocks.contains(pos);
        if (!contained && powered) {
            activatedBlocks.add(pos);
            if (redstonePower == -1) {
                redstonePower = 0;
            }
            redstonePower++;
        } else if (contained && !powered) {
            activatedBlocks.remove(pos);
            redstonePower--;
            if (redstonePower == 0) {
                redstonePower = -1;
            }
        }
    }

}
