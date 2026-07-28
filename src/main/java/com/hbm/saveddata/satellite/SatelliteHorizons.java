package com.hbm.saveddata.satellite;

import com.hbm.entity.projectile.Tom;
import com.hbm.saveddata.SatelliteSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Locale;

public class SatelliteHorizons extends SatelliteBase {

    public static final String CMD_FIRE = "fire";
    public static final String CMD_CANFIRE = "settarget";

    private boolean used = false;

    public SatelliteHorizons() { }

    @Override public String getType() { return "PAYLOAD_UNKNOWN"; }

    @Override
    public void writeToNBT(CompoundTag tag) {
        tag.putBoolean("used", used);
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        used = nbt.getBoolean("used");
    }

    @Override
    public void onCommandImpl(Level level, String... cmd) {
        if(cmd.length <= 0) return;

        if(cmd[0].equals(CMD_FIRE)) {
            theHorizons(level, targetX, targetZ);
            return;
        }

        if(cmd[0].equals(CMD_CANFIRE)) {
            this.tx = (!used) + "";
            this.tx = this.tx.toUpperCase(Locale.US);
            return;
        }
    }

    @Override
    public void onCoordAction(Level level, Player player, BlockPos pos) {
        this.setTarget(pos.getX(), pos.getZ());
        this.theHorizons(level, pos.getX(), pos.getZ());
    }

    public void theHorizons(Level level, int x, int z) {
        if(!(level instanceof ServerLevel serverLevel)) return;
        if(used) return;

        used = true;
        SatelliteSavedData.getData(serverLevel).setDirty();

        Tom tom = new Tom(level);
        tom.setPos(x + 0.5, 600, z + 0.5);

        level.getChunkSource().getChunk(x >> 4, z >> 4, true);

        level.addFreshEntity(tom);

        // todo
//        for(Object p : world.playerEntities)
//            ((EntityPlayer)p).triggerAchievement(MainRegistry.horizonsEnd);

        for(Player player : level.players()) {
            player.sendSystemMessage(Component.literal("Horizons has been activated.").withStyle(ChatFormatting.RED));
        }
    }

}
