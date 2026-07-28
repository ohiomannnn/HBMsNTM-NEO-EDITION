package com.hbm.saveddata.satellite;

import com.hbm.util.SoundUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SatelliteResonator extends SatelliteBase {

    public SatelliteResonator() { }

    @Override public String getType() { return "XEN_RELAY"; }

    @Override
    public void onCoordAction(Level level, Player player, BlockPos pos) {

        if(!(player instanceof ServerPlayer serverPlayer)) return;

        SoundUtils.playAtVec3(player.level, player.position(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS);

        player.stopRiding();
        serverPlayer.connection.teleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, player.getYRot(), player.getXRot());

        SoundUtils.playAtVec3(player.level, player.position(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS);
    }
}
