package com.hbm.saveddata.satellite;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SatelliteMapper extends SatelliteBase {

    public static final String CMD_TARGET_LOADED = "targetloaded";
    public static final String CMD_SPOT_PLAYER = "spotplayers";

    public static final int SPOT_PLAYER_MAX_RANGE = 250;

    public SatelliteMapper() { }

    @Override public String getType() { return "NOT_A_SPY_SATELLITE_:)"; }

    @Override
    public void onCommandImpl(Level level, String... cmd) {
        if(cmd.length <= 0) return;

        if(cmd[0].equals(CMD_TARGET_LOADED)) {
            this.tx = "" + level.getChunkSource().hasChunk(targetX >> 4, targetZ >> 4);
            this.tx = this.tx.toUpperCase(Locale.US);
            return;
        }

        if(cmd[0].equals(CMD_SPOT_PLAYER)) {

            List<String> names = new ArrayList<>();

            for(Player player : level.players()) {
                int x = (int) Math.floor(player.getX());
                int z = (int) Math.floor(player.getZ());

                double dX = x - targetX;
                double dZ = z - targetZ;

                if(dX * dX + dZ * dZ <= SPOT_PLAYER_MAX_RANGE * SPOT_PLAYER_MAX_RANGE) {
                    int height = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                    if(height < player.getY() + 2) names.add(player.getName().getString());
                }
            }

            if(names.isEmpty()) {
                this.tx = "NONE";
                return;
            }

            this.tx = String.join(";", names);
            return;
        }
    }
}
