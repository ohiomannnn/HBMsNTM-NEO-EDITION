package com.hbm.packets;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.blockentity.IBufPacketReceiver;
import com.hbm.extprop.LivingProperties;
import com.hbm.handler.gui.GeigerGUI;
import com.hbm.packets.toclient.AuxParticlePacket;
import com.hbm.packets.toclient.BufPacket;
import com.hbm.packets.toclient.ParticleBurstPacket;
import com.hbm.packets.toclient.SendRadPacket;
import com.hbm.packets.toserver.GetRadPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
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

public final class PacketsDispatcher {
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(HBMsNTM.MODID);

        registrar.playToServer(
                GetRadPacket.TYPE,
                GetRadPacket.STREAM_CODEC,
                PacketsDispatcher::handleGetRad
        );
        registrar.playToClient(
                ParticleBurstPacket.TYPE,
                ParticleBurstPacket.STREAM_CODEC,
                PacketsDispatcher::handleBurst
        );
        registrar.playToClient(
                AuxParticlePacket.TYPE,
                AuxParticlePacket.STREAM_CODEC,
                PacketsDispatcher::handleAuxParticle
        );
        registrar.playToClient(
                SendRadPacket.TYPE,
                SendRadPacket.STREAM_CODEC,
                PacketsDispatcher::handleSendRad
        );
        registrar.playToClient(
                BufPacket.TYPE,
                BufPacket.STREAM_CODEC,
                PacketsDispatcher::handleBufPacket
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
}
