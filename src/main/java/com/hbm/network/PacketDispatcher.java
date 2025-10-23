package com.hbm.network;

import com.hbm.network.toclient.*;
import com.hbm.network.toserver.*;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketDispatcher {
    private static final String PROTOCOL_VERSION = "1";

    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        registrar.playToServer(GetRadPacket.TYPE, GetRadPacket.STREAM_CODEC, GetRadPacket::handleServer);
        registrar.playToServer(KeybindPacket.TYPE, KeybindPacket.STREAM_CODEC, KeybindPacket::handleServer);
//        registrar.playToClient( // fuck you
//                ExplosionVanillaNewTechnologyCompressedAffectedBlockPositionDataForClientEffectsAndParticleHandlingPacket.TYPE,
//                ExplosionVanillaNewTechnologyCompressedAffectedBlockPositionDataForClientEffectsAndParticleHandlingPacket.STREAM_CODEC,
//                ExplosionVanillaNewTechnologyCompressedAffectedBlockPositionDataForClientEffectsAndParticleHandlingPacket::handleClient
//        );
        registrar.playToClient(InformPlayerPacket.TYPE, InformPlayerPacket.STREAM_CODEC, InformPlayerPacket::handleClient);
        registrar.playToClient(ParticleBurstPacket.TYPE, ParticleBurstPacket.STREAM_CODEC, ParticleBurstPacket::handleClient);
        registrar.playToClient(AuxParticle.TYPE, AuxParticle.STREAM_CODEC, AuxParticle::handleClient);
        registrar.playToClient(SendRadPacket.TYPE, SendRadPacket.STREAM_CODEC, SendRadPacket::handleClient);
        registrar.playToClient(BufNT.TYPE, BufNT.STREAM_CODEC, BufNT::handleClient);
    }
}
