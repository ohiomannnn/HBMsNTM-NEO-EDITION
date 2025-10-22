package com.hbm.packets;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.blockentity.IBufPacketReceiver;
import com.hbm.explosion.vanillant.standard.ExplosionEffectStandard;
import com.hbm.extprop.LivingProperties;
import com.hbm.handler.HbmKeybinds;
import com.hbm.handler.HbmKeybindsServer;
import com.hbm.handler.gui.GeigerGUI;
import com.hbm.packets.toclient.*;
import com.hbm.packets.toserver.GetRadPacket;
import com.hbm.packets.toserver.KeybindPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public final class PacketDispatcher {
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(HBMsNTM.MODID);

        registrar.playToServer(
                GetRadPacket.TYPE,
                GetRadPacket.STREAM_CODEC,
                PacketDispatcher::handleGetRad
        );
        registrar.playToServer(
                KeybindPacket.TYPE,
                KeybindPacket.STREAM_CODEC,
                PacketDispatcher::handleKeybind
        );
        registrar.playToClient(
                ExplosionVanillaNewTechnologyCompressedAffectedBlockPositionDataForClientEffectsAndParticleHandlingPacket.TYPE,
                ExplosionVanillaNewTechnologyCompressedAffectedBlockPositionDataForClientEffectsAndParticleHandlingPacket.STREAM_CODEC,
                PacketDispatcher::handleBigNamePacket
        );
        registrar.playToClient(
                InformPlayerPacket.TYPE,
                InformPlayerPacket.STREAM_CODEC,
                PacketDispatcher::handleInform
        );
        registrar.playToClient(
                ParticleBurstPacket.TYPE,
                ParticleBurstPacket.STREAM_CODEC,
                PacketDispatcher::handleBurst
        );
        registrar.playToClient(
                AuxParticlePacket.TYPE,
                AuxParticlePacket.STREAM_CODEC,
                PacketDispatcher::handleAuxParticle
        );
        registrar.playToClient(
                SendRadPacket.TYPE,
                SendRadPacket.STREAM_CODEC,
                PacketDispatcher::handleSendRad
        );
        registrar.playToClient(
                BufPacket.TYPE,
                BufPacket.STREAM_CODEC,
                PacketDispatcher::handleBufPacket
        );
    }
    private static void handleGetRad(GetRadPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer sender)) return;

            MinecraftServer server = sender.server;
            UUID target = packet.targetId();

            for (ServerLevel level : server.getAllLevels()) {
                var entity = level.getEntity(target);
                if (entity instanceof LivingEntity living) {
                    float rad = LivingProperties.getRadiation(living);

                    PacketDistributor.sendToPlayer(sender, new SendRadPacket(rad));
                }
            }
        });
    }
    private static void handleKeybind(KeybindPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer sender)) return;
            HbmKeybindsServer.onPressedServer(sender, HbmKeybinds.EnumKeybind.values()[packet.key().ordinal()], packet.pressed());
        });
    }
    private static void handleSendRad(SendRadPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> GeigerGUI.getRadFromServer(packet.rad()));
    }
    public static void handleBurst(ParticleBurstPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Block block = BuiltInRegistries.BLOCK.get(packet.blockId());
            BlockState state = block.defaultBlockState();
            try {
                Minecraft.getInstance().particleEngine.destroy(packet.pos(), state);
            } catch (Exception ignored) { }
        });
    }
    public static void handleAuxParticle(AuxParticlePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            packet.nbt().putDouble("posX", packet.x());
            packet.nbt().putDouble("posY", packet.y());
            packet.nbt().putDouble("posZ", packet.z());
            if (mc.level != null) {
                HBMsNTMClient.effectNT(packet.nbt());
            }
        });
    }
    public static void handleBufPacket(BufPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) return;

            BlockEntity be = level.getBlockEntity(packet.pos());
            if (be instanceof IBufPacketReceiver receiver) {
                try {
                    receiver.deserialize(packet.payload());
                } catch (Exception e) { // if im dumb
                    HBMsNTM.LOGGER.warn("Error reading ByteBuf package: {}", e.getMessage());
                    HBMsNTM.LOGGER.warn("Tile: {}", be.getBlockState().getBlock().getName().getString());
                } finally {
                    packet.payload().release();
                }
            }
        });
    }
    public static void handleInform(InformPlayerPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            try {
                if (packet.fancy()) {
                    HBMsNTMClient.displayTooltip(packet.component(), packet.millis(), packet.id());
                } else {
                    HBMsNTMClient.displayTooltip(Component.literal(packet.message()), packet.millis(), packet.id());
                }
            } catch (Exception ignored) {}
        });
    }
    public static void handleBigNamePacket(ExplosionVanillaNewTechnologyCompressedAffectedBlockPositionDataForClientEffectsAndParticleHandlingPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                ExplosionEffectStandard.performClient(
                        mc.level,
                        packet.posX(),
                        packet.posY(),
                        packet.posZ(),
                        packet.size(),
                        packet.affectedBlocks()
                );
            }
        });
    }
}
