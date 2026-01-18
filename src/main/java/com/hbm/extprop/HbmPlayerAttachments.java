package com.hbm.extprop;

import com.hbm.HBMsNTMClient;
import com.hbm.entity.ModEntityTypes;
import com.hbm.entity.mob.Duck;
import com.hbm.handler.KeyHandler.EnumKeybind;
import com.hbm.lib.ModAttachments;
import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.InformPlayer;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class HbmPlayerAttachments {

    public static final StreamCodec<RegistryFriendlyByteBuf, HbmPlayerAttachments> STREAM_CODEC = StreamCodec.of(
            (buf, props) -> buf.writeNbt(props.saveNBTData()),
            buf -> {
                HbmPlayerAttachments props = new HbmPlayerAttachments();
                props.loadNBTData(buf.readNbt());
                return props;
            });
    public static final Codec<HbmPlayerAttachments> CODEC =
            CompoundTag.CODEC.xmap(
                    tag -> {
                        HbmPlayerAttachments props = new HbmPlayerAttachments();
                        props.loadNBTData(tag);
                        return props;
                    },
                    HbmPlayerAttachments::saveNBTData
            );


    public boolean hasReceivedBook = false;

    public boolean ducked = false;

    public boolean enableHUD = true;
    public boolean enableBackpack = true;
    public boolean enableMagnet = true;

    private final boolean[] keysPressed = new boolean[EnumKeybind.values().length];

    public boolean dashActivated = true;

    public static final int dashCooldownLength = 5;
    public int dashCooldown = 0;

    public int totalDashCount = 0;
    public int stamina = 0;

    public static final int plinkCooldownLength = 10;
    public int plinkCooldown = 0;

    public float shield = 0;
    public float maxShield = 0;
    public int lastDamage = 0;
    public static final float shieldCap = 100;

    public int reputation;

    public boolean isOnLadder = false;

    public HbmPlayerAttachments() { }

    public static HbmPlayerAttachments getData(Player player) {
        return player.getData(ModAttachments.PLAYER_ATTACHMENT);
    }

    public boolean getKeyPressed(EnumKeybind key) {
        return keysPressed[key.ordinal()];
    }

    public boolean isJetpackActive() {
        return this.enableBackpack && getKeyPressed(EnumKeybind.JETPACK);
    }

    public boolean isMagnetActive(){
        return this.enableMagnet;
    }

    public void setKeyPressed(Player player, EnumKeybind key, boolean pressed) {
        if (!getKeyPressed(key) && pressed) {
            if (key == EnumKeybind.TOGGLE_JETPACK) {
                if (!player.level().isClientSide) {
                    HbmPlayerAttachments props = getData(player);
                    props.enableBackpack = !props.enableBackpack;
                    player.setData(ModAttachments.PLAYER_ATTACHMENT, props);

                    if (props.enableBackpack) {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new InformPlayer(Component.literal("Jetpack ON").withStyle(ChatFormatting.GREEN), HBMsNTMClient.ID_JETPACK, 1000));
                    } else {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new InformPlayer(Component.literal("Jetpack OFF").withStyle(ChatFormatting.RED), HBMsNTMClient.ID_JETPACK, 1000));
                    }
                }
            }
            if (key == EnumKeybind.TOGGLE_MAGNET) {
                if (!player.level().isClientSide) {
                    HbmPlayerAttachments props = getData(player);
                    props.enableMagnet = !props.enableMagnet;
                    player.setData(ModAttachments.PLAYER_ATTACHMENT, props);


                    if (props.enableMagnet) {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new InformPlayer(Component.literal("Magnet ON").withStyle(ChatFormatting.GREEN), HBMsNTMClient.ID_MAGNET, 1000));
                    } else {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new InformPlayer(Component.literal("Magnet OFF").withStyle(ChatFormatting.RED), HBMsNTMClient.ID_MAGNET, 1000));
                    }
                }
            }
            if (key == EnumKeybind.TOGGLE_HEAD) {

                if (!player.level().isClientSide) {
                    HbmPlayerAttachments props = getData(player);
                    props.enableHUD = !props.enableHUD;
                    player.setData(ModAttachments.PLAYER_ATTACHMENT, props);

                    if (props.enableHUD) {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new InformPlayer(Component.literal("HUD ON").withStyle(ChatFormatting.GREEN), HBMsNTMClient.ID_HUD, 1000));
                    } else {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new InformPlayer(Component.literal("HUD OFF").withStyle(ChatFormatting.RED), HBMsNTMClient.ID_HUD, 1000));
                    }
                }
            }
            if (key == EnumKeybind.DUCK) {
                HbmPlayerAttachments props = getData(player);
                if (!props.ducked) {
                    Duck ducc = new Duck(ModEntityTypes.DUCK.get(), player.level());

                    ducc.setPos(player.getX(), player.getEyeY(), player.getZ());
                    ducc.setDeltaMovement(player.getLookAngle());
                    ducc.fallDistance = 0.0F;

                    player.level().addFreshEntity(ducc);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.DUCK, SoundSource.NEUTRAL, 1.0F, 1.0F);

                    props.ducked = !props.ducked;
                    player.setData(ModAttachments.PLAYER_ATTACHMENT, props);
                }
            }
            //TODO: add train lol
        }

        keysPressed[key.ordinal()] = pressed;
    }

    public void setDashCooldown(int cooldown) {
        this.dashCooldown = cooldown;
        return;
    }

    public int getDashCooldown() {
        return this.dashCooldown;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
        return;
    }

    public int getStamina() {
        return this.stamina;
    }

    public void setDashCount(int count) {
        this.totalDashCount = count;
        return;
    }

    public int getDashCount() {
        return this.totalDashCount;
    }
    // TODO: plink and maxshield

    public void serialize(ByteBuf buf) {
        buf.writeBoolean(this.hasReceivedBook);
        buf.writeBoolean(this.ducked);
        buf.writeFloat(this.shield);
        buf.writeFloat(this.maxShield);
        buf.writeBoolean(this.enableBackpack);
        buf.writeBoolean(this.enableHUD);
        buf.writeInt(this.reputation);
        buf.writeBoolean(this.isOnLadder);
        buf.writeBoolean(this.enableMagnet);
    }

    public void deserialize(ByteBuf buf) {
        if (buf.readableBytes() > 0) {
            this.hasReceivedBook = buf.readBoolean();
            this.ducked = buf.readBoolean();
            this.shield = buf.readFloat();
            this.maxShield = buf.readFloat();
            this.enableBackpack = buf.readBoolean();
            this.enableHUD = buf.readBoolean();
            this.reputation = buf.readInt();
            this.isOnLadder = buf.readBoolean();
            this.enableMagnet = buf.readBoolean();
        }
    }

    public CompoundTag saveNBTData() {
        CompoundTag props = new CompoundTag();

        props.putBoolean("hasReceivedBook", this.hasReceivedBook);
        props.putBoolean("ducked", this.ducked);
        props.putFloat("shield", this.shield);
        props.putFloat("maxShield", this.maxShield);
        props.putBoolean("enableBackpack", this.enableBackpack);
        props.putBoolean("enableHUD", this.enableHUD);
        props.putInt("reputation", this.reputation);
        props.putBoolean("isOnLadder", this.isOnLadder);
        props.putBoolean("enableMagnet", this.enableMagnet);

        return props;
    }

    public void loadNBTData(CompoundTag props) {
        if (props != null) {
            this.hasReceivedBook = props.getBoolean("hasReceivedBook");
            this.ducked = props.getBoolean("ducked");
            this.shield = props.getFloat("shield");
            this.maxShield = props.getFloat("maxShield");
            this.enableBackpack = props.getBoolean("enableBackpack");
            this.enableHUD = props.getBoolean("enableHUD");
            this.reputation = props.getInt("reputation");
            this.isOnLadder = props.getBoolean("isOnLadder");
            this.enableMagnet = props.getBoolean("enableMagnet");
        }
    }
}
