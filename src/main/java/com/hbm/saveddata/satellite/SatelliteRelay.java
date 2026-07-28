package com.hbm.saveddata.satellite;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SatelliteRelay extends SatelliteBase {

    public SatelliteRelay() { }

    @Override public String getType() { return "RX/TX"; }

    @Override
    public void onOrbit(Level level, double x, double y, double z) {

        // todo you get it
        for(Player p : level.players()) p.sendSystemMessage(Component.literal("you got noting lmao"));
    }
}