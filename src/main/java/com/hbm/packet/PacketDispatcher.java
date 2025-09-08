package com.hbm.packet;

import com.hbm.HBMsNTM;
import com.hbm.packet.toclient.AuxParticlePacketNT;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public final class PacketDispatcher {

    public static void register(final RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(HBMsNTM.MODID);

        registrar.playToClient(
                AuxParticlePacketNT.TYPE,
                AuxParticlePacketNT.STREAM_CODEC,
                PacketDispatcher::handleParticleNT
        );
    }

    private static void handleParticleNT(final AuxParticlePacketNT auxParticlePacketNT, final IPayloadContext context) {
        context.enqueueWork(() -> {

        });
    }
}
