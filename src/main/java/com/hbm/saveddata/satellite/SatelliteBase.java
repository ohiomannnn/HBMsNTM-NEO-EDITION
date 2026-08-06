package com.hbm.saveddata.satellite;

import api.hbm.redstoneoverradio.IRORInteractive;
import com.hbm.blockentity.network.RTTYSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class SatelliteBase {

    public static final String CHAN_SATLINK = "SAT_LINK";

    public static final String CMD_SETTARGET = "settarget";
    public static final String CMD_GETTARGET = "gettarget";
    public static final String CMD_GETTARGETX = "gettargetx";
    public static final String CMD_GETTARGETZ = "gettargetz";

    public int range = 1_000;
    public int targetX;
    public int targetZ;

    public String tx = "";

    public int getID() {
        return XSatelliteRegistry.satellites.indexOf(this.getClass());
    }

    public abstract String getType();

    public void writeToNBT(CompoundTag tag) {
        tag.putInt("targetX", targetX);
        tag.putInt("targetZ", targetZ);
        tag.putString("tx", tx);
    }

    public void readFromNBT(CompoundTag tag) {
        this.targetX = tag.getInt("targetX");
        this.targetZ = tag.getInt("targetZ");
        this.tx = tag.getString("tx");
    }

    /** When a satellite is created, i.e. this frequency is occupied for the first time */
    public void onOrbit(Level level, double x, double y, double z) {
        this.setTarget((int) Math.floor(x), (int) Math.floor(z));

        RTTYSystem.broadcast(level, CHAN_SATLINK, "Established connection to " + getType() + " at " + targetX + " / " + targetZ);
    }

    /** For subsequent items sent under the same frequency as an existing satellite */
    public void onPartDelivered(Level level, ItemStack part) { }

    public void onCommand(Level level, String... cmd) {
        this.onCommandTarget(level, cmd);
        this.onCommandImpl(level, cmd);
    }

    public void onCommandTarget(Level level, String... cmd) {
        if(cmd.length <= 0) return;

        if(cmd[0].equals(CMD_SETTARGET)) {
            if(cmd.length == 3) {
                targetX = IRORInteractive.parseInt(cmd[1]);
                targetZ = IRORInteractive.parseInt(cmd[2]);
            }
            if(cmd.length == 4) {
                targetX = IRORInteractive.parseInt(cmd[1]);
                targetZ = IRORInteractive.parseInt(cmd[3]);
            }
            return;
        }

        if(cmd[0].equals(CMD_GETTARGET)) {
            this.tx = targetX + ";" + targetZ;
            return;
        }

        if(cmd[0].equals(CMD_GETTARGETX)) {
            this.tx = "" + targetX;
            return;
        }

        if(cmd[0].equals(CMD_GETTARGETZ)) {
            this.tx = "" + targetZ;
            return;
        }
    }

    public void setTarget(int x, int z) {
        this.targetX = x;
        this.targetZ = z;
    }

    public void onCommandImpl(Level level, String... cmd) { }

    public void onCoordAction(Level level, Player player, BlockPos pos) { }
}
