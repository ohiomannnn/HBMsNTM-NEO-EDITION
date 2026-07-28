package com.hbm.saveddata.satellite;

import com.hbm.entity.logic.DeathBlast;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Locale;

public class SatelliteDeathRay extends SatelliteBase {

    public static final String CMD_FIRE = "fire";
    public static final String CMD_CANFIRE = "canfire";

    public static final int CHARGE_TIME = 5 * 60 * 20;

    public long lastShot;

    public SatelliteDeathRay() { }

    @Override public String getType() { return "ORBITAL_FUN_PLATFORM_:)"; }

    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);

        tag.putLong("lastShot", lastShot);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);

        lastShot = tag.getLong("lastShot");
    }

    @Override
    public void onCommandImpl(Level level, String... cmd) {
        if(cmd.length <= 0) return;

        if(cmd[0].equals(CMD_FIRE)) {
            this.deathBlast(level, targetX, targetZ);
            return;
        }

        if(cmd[0].equals(CMD_CANFIRE)) {
            this.tx = (lastShot + CHARGE_TIME < level.getGameTime()) + "";
            this.tx = this.tx.toUpperCase(Locale.US);
            return;
        }
    }

    @Override
    public void onCoordAction(Level level, Player player, BlockPos pos) {
        this.setTarget(pos.getX(), pos.getZ());
        this.deathBlast(level, targetX, targetZ);
    }

    public void deathBlast(Level level, int x, int z) {

        if(lastShot + CHARGE_TIME < level.getGameTime()) {
            lastShot = level.getGameTime();

            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);

            DeathBlast blast = new DeathBlast(level);
            blast.setPos(x, y, z);

            level.addFreshEntity(blast);
        }
    }
}
