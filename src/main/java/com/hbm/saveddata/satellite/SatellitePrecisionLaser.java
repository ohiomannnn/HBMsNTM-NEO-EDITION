package com.hbm.saveddata.satellite;

import api.hbm.redstoneoverradio.IRORInteractive;
import com.hbm.entity.logic.OrbitalLaser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Locale;

public class SatellitePrecisionLaser extends SatelliteBase {


    public static final String CMD_FIRE = "fire";
    public static final String CMD_CANFIRE = "canfire";
    public static final String CMD_SETENTITYTARGET = "setentitytarget";

    public static final int MAX_TARGET_RANGE = 1_000;
    public static final int CHARGE_TIME = 5 * 20;

    public long lastShot;
    public int targetedEntity = -1;

    public SatellitePrecisionLaser() { }

    @Override public String getType() { return "ORBITAL_TATOO_REMOVER"; }

    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);

        tag.putLong("lastShot", lastShot);
        tag.putInt("targetedEntity", targetedEntity);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);

        lastShot = tag.getLong("lastShot");
        targetedEntity = tag.getInt("targetedEntity");
    }

    @Override
    public void onCommandImpl(Level level, String... cmd) {
        if(cmd.length <= 0) return;

        if(cmd[0].equals(CMD_FIRE)) {

            if(this.targetedEntity != -1) {
                Entity e = level.getEntity(this.targetedEntity);
                this.targetedEntity = -1;

                if(e == null || e.isRemoved()) return;

                int x = (int) Math.floor(e.getX());
                int z = (int) Math.floor(e.getZ());

                double dX = x - targetX;
                double dZ = z - targetZ;

                if(dX * dX + dZ * dZ <= MAX_TARGET_RANGE * MAX_TARGET_RANGE) {
                    this.deathBlast(level, e.getX(), e.getY(), e.getZ());
                    return;
                }
            }

            this.deathBlast(level, targetX, targetZ);
            return;
        }

        if(cmd[0].equals(CMD_CANFIRE)) {
            this.tx = (lastShot + CHARGE_TIME < level.getGameTime()) + "";
            this.tx = this.tx.toUpperCase(Locale.US);
            return;
        }

        if(cmd[0].equals(CMD_SETENTITYTARGET)) {
            this.targetedEntity = IRORInteractive.parseInt(cmd[1]);
            return;
        }
    }

    @Override
    public void onCoordAction(Level level, Player player, BlockPos pos) {
        this.setTarget(pos.getX(), pos.getZ());
        this.deathBlast(level, targetX, targetZ);
    }

    public void deathBlast(Level level, int x, int z) {
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
        deathBlast(level, x + 0.5, y, z + 0.5);
    }

    public void deathBlast(Level level, double x, double y, double z) {

        if(lastShot + CHARGE_TIME < level.getGameTime()) {
            lastShot = level.getGameTime();

            OrbitalLaser blast = new OrbitalLaser(level);
            blast.setPos(x, y, z);
            blast.explode();

            level.addFreshEntity(blast);
        }
    }
}
