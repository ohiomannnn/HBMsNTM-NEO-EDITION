package com.hbm.extprop;

import com.hbm.HBMsNTMClient;
import com.hbm.handler.HbmKeybinds.EnumKeybind;
import com.hbm.lib.ModAttachments;
import com.hbm.network.toclient.InformPlayerPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;

public class PlayerProperties {

    public Player player;

    public boolean hasReceivedBook = false;

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

    public PlayerProperties(IAttachmentHolder iAttachmentHolder) {
        if (iAttachmentHolder instanceof Player holder) {
            this.player = holder;
        }
    }

    public static PlayerProperties getData(Player player) {
        return player.getData(ModAttachments.PLAYER_PROPS);
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

    public void setKeyPressed(EnumKeybind key, boolean pressed) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (!getKeyPressed(key) && pressed) {
            if (key == EnumKeybind.TOGGLE_JETPACK) {
                if (!player.level().isClientSide) {
                    this.enableBackpack = !this.enableBackpack;

                    if (this.enableBackpack) {
                        PacketDistributor.sendToPlayer(serverPlayer, new InformPlayerPacket(true, null, Component.literal("Jetpack ON").withStyle(ChatFormatting.GREEN), HBMsNTMClient.ID_JETPACK, 1000));
                    } else {
                        HBMsNTMClient.displayTooltip(Component.literal("Jetpack OFF").withStyle(ChatFormatting.RED), HBMsNTMClient.ID_JETPACK);
                    }
                }
            }
            if (key == EnumKeybind.TOGGLE_MAGNET) {
                if (!player.level().isClientSide) {
                    this.enableMagnet = !this.enableMagnet;

                    if (this.enableMagnet) {
                        HBMsNTMClient.displayTooltip(Component.literal("Magnet ON").withStyle(ChatFormatting.GREEN), HBMsNTMClient.ID_MAGNET);
                    } else {
                        HBMsNTMClient.displayTooltip(Component.literal("Magnet OFF").withStyle(ChatFormatting.RED), HBMsNTMClient.ID_MAGNET);
                    }
                }
            }
            if (key == EnumKeybind.TOGGLE_HEAD) {

                if (!player.level().isClientSide) {
                    this.enableHUD = !this.enableHUD;

                    if (this.enableHUD) {
                        HBMsNTMClient.displayTooltip(Component.literal("HUD ON").withStyle(ChatFormatting.GREEN), HBMsNTMClient.ID_HUD);
                    } else {
                        HBMsNTMClient.displayTooltip(Component.literal("HUD OFF").withStyle(ChatFormatting.RED), HBMsNTMClient.ID_HUD);
                    }
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

    public CompoundTag serializeNBT() {
        CompoundTag props = new CompoundTag();

        props.putBoolean("hasReceivedBook", this.hasReceivedBook);
        props.putFloat("shield", this.shield);
        props.putFloat("maxShield", this.maxShield);
        props.putBoolean("enableBackpack", this.enableBackpack);
        props.putBoolean("enableHUD", this.enableHUD);
        props.putInt("reputation", this.reputation);
        props.putBoolean("isOnLadder", this.isOnLadder);
        props.putBoolean("enableMagnet", this.enableMagnet);

        return props;
    }

    public void deserializeNBT(CompoundTag props) {
        if (props != null) {
            this.hasReceivedBook = props.getBoolean("hasReceivedBook");
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
