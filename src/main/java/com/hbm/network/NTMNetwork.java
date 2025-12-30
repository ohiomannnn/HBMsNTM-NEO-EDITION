package com.hbm.network;

import com.hbm.network.toclient.*;
import com.hbm.network.toserver.*;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NTMNetwork {
    private static final String PROTOCOL_VERSION = "2";

    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        registrar.playToServer(KeybindReceiver.TYPE, KeybindReceiver.STREAM_CODEC, KeybindReceiver::handleServer);
        registrar.playToServer(CompoundTagItemControl.TYPE, CompoundTagItemControl.STREAM_CODEC, CompoundTagItemControl::handleServer);
        registrar.playToServer(SatelliteLaser.TYPE, SatelliteLaser.STREAM_CODEC, SatelliteLaser::handleServer);

        registrar.playToClient(VanillaExplosionLike.TYPE, VanillaExplosionLike.STREAM_CODEC, VanillaExplosionLike::handleClient);
        registrar.playToClient(InformPlayer.TYPE, InformPlayer.STREAM_CODEC, InformPlayer::handleClient);
        registrar.playToClient(ParticleBurst.TYPE, ParticleBurst.STREAM_CODEC, ParticleBurst::handleClient);
        registrar.playToClient(AuxParticle.TYPE, AuxParticle.STREAM_CODEC, AuxParticle::handleClient);
        registrar.playToClient(SatellitePanel.TYPE, SatellitePanel.STREAM_CODEC, SatellitePanel::handleClient);
        registrar.playToClient(BufPacket.TYPE, BufPacket.STREAM_CODEC, BufPacket::handleClient);
    }
}
