package com.hbm.network;

import com.hbm.handler.pollution.PollutionHandler;
import com.hbm.handler.pollution.PollutionHandler.PollutionData;
import com.hbm.handler.pollution.PollutionHandler.PollutionType;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Utility for permanently synchronizing values every tick with a player in the given context of a world.
 * Uses the Byte Buffer directly instead of NBT to cut back on unnecessary data.
 * @author hbm
 */
public class PermaSyncHandler {

//    public static HashSet<Integer> boykissers = new HashSet();
    public static float[] pollution = new float[PollutionType.values().length];

    public static void writePacket(ByteBuf buf, Level level, ServerPlayer player) {

//  todo        /// TOM IMPACT DATA ///
//        TomSaveData data = TomSaveData.forWorld(world);
//        buf.writeFloat(data.fire);
//        buf.writeFloat(data.dust);
//        buf.writeBoolean(data.impact);
//        /// TOM IMPACT DATA ///
//
//       /// SHITTY MEMES ///
//        List<Integer> ids = new ArrayList();
//        for(Object o : world.playerEntities) {
//            EntityPlayer p = (EntityPlayer) o;
//            if(p.isPotionActive(HbmPotion.death.id)) {
//                ids.add(p.getEntityId());
//            }
//        }
//        buf.writeShort((short) ids.size());
//        for(Integer i : ids) buf.writeInt(i);
//        /// SHITTY MEMES ///

        /// POLLUTION ///
        PollutionData pollution = PollutionHandler.getPollutionData(level, player.blockPosition());
        if(pollution == null) pollution = new PollutionData();
        for(int i = 0; i < PollutionType.values().length; i++) {
            buf.writeFloat(pollution.pollution[i]);
        }
        /// POLLUTION ///
    }

    public static void readPacket(ByteBuf buf, Level level, Player player) {

// todo       /// TOM IMPACT DATA ///
//        ImpactWorldHandler.lastSyncWorld = player.worldObj;
//        ImpactWorldHandler.fire = buf.readFloat();
//        ImpactWorldHandler.dust = buf.readFloat();
//        ImpactWorldHandler.impact = buf.readBoolean();
//        /// TOM IMPACT DATA ///
//
//        /// SHITTY MEMES ///
//        boykissers.clear();
//        int ids = buf.readShort();
//        for(int i = 0; i < ids; i++) boykissers.add(buf.readInt());
//        /// SHITTY MEMES ///

        /// POLLUTION ///
        for(int i = 0; i < PollutionType.values().length; i++) {
            pollution[i] = buf.readFloat();
        }
        /// POLLUTION ///
    }
}
