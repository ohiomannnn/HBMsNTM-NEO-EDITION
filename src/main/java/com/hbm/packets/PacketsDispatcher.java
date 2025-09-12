package com.hbm.packets;

import com.hbm.HBMsNTM;
import com.hbm.extprop.LivingProperties;
import com.hbm.handler.gui.GeigerGUI;
import com.hbm.packets.toclient.ParticleBurstPacket;
import com.hbm.packets.toserver.GetRadPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public final class PacketsDispatcher {
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(HBMsNTM.MODID);

        registrar.playToServer(
                GetRadPacket.TYPE,
                GetRadPacket.STREAM_CODEC,
                PacketsDispatcher::getRad
        );
        registrar.playToClient(
                ParticleBurstPacket.TYPE,
                ParticleBurstPacket.STREAM_CODEC,
                PacketsDispatcher::handle
        );
    }
    private static void getRad(GetRadPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer sender)) return;

            MinecraftServer server = sender.server;
            UUID target = packet.targetId();

            for (ServerLevel level : server.getAllLevels()) {
                var entity = level.getEntity(target);
                if (entity instanceof LivingEntity living) {
                    float rad = LivingProperties.getRadiation(living);
                    GeigerGUI.getRadFromServer(rad);
                }
            }
        });
    }
    public static void handle(ParticleBurstPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Block block = BuiltInRegistries.BLOCK.get(packet.blockId());
            BlockState state = block.defaultBlockState();
            try {
                Minecraft.getInstance().particleEngine.destroy(packet.pos(), state);
            } catch (Exception ignored) { }
        });
    }
}
