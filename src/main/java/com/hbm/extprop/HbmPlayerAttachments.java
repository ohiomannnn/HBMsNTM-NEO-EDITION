package com.hbm.extprop;

import com.hbm.HBMsNTMClient;
import com.hbm.handler.HbmKeybinds.EnumKeybind;
import com.hbm.lib.ModAttachments;
import com.hbm.network.toclient.InformPlayer;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
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

    public static HbmPlayerAttachments getData(Player player) {
        return player.getData(ModAttachments.PLAYER_ATTACHMENT);
    }

    public final boolean[] keysPressed = new boolean[EnumKeybind.values().length];

    public boolean getKeyPressed(EnumKeybind key) {
        return keysPressed[key.ordinal()];
    }

    public static void setKeyPressed(Player player, EnumKeybind key, boolean pressed) {
        HbmPlayerAttachments props = player.getData(ModAttachments.PLAYER_ATTACHMENT);

        if (!props.getKeyPressed(key) && pressed) {

            if (key == EnumKeybind.TOGGLE_JETPACK) {

                if (!player.level().isClientSide) {
                    props.enableBackpack = !props.enableBackpack;

                    if (props.enableBackpack) {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new InformPlayer(Component.literal("Jetpack ON").withStyle(ChatFormatting.GREEN), HBMsNTMClient.ID_JETPACK, 1000));
                    } else {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new InformPlayer(Component.literal("Jetpack OFF").withStyle(ChatFormatting.RED), HBMsNTMClient.ID_JETPACK, 1000));
                    }
                }
            }

            if (key == EnumKeybind.TOGGLE_MAGNET) {

                if (!player.level().isClientSide) {
                    props.enableMagnet = !props.enableMagnet;

                    if (props.enableMagnet) {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new InformPlayer(Component.literal("Magnet ON").withStyle(ChatFormatting.GREEN), HBMsNTMClient.ID_MAGNET, 1000));
                    } else {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new InformPlayer(Component.literal("Magnet OFF").withStyle(ChatFormatting.RED), HBMsNTMClient.ID_MAGNET, 1000));
                    }
                }
            }

            if (key == EnumKeybind.TOGGLE_HEAD) {

                if (!player.level().isClientSide) {
                    props.enableHUD = !props.enableHUD;

                    if (props.enableHUD) {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new InformPlayer(Component.literal("HUD ON").withStyle(ChatFormatting.GREEN), HBMsNTMClient.ID_HUD, 1000));
                    } else {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new InformPlayer(Component.literal("HUD OFF").withStyle(ChatFormatting.RED), HBMsNTMClient.ID_HUD, 1000));
                    }
                }
            }
        }

        props.keysPressed[key.ordinal()] = pressed;

        player.setData(ModAttachments.PLAYER_ATTACHMENT, props);
    }

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
