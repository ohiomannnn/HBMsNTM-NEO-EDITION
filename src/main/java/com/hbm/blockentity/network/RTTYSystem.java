package com.hbm.blockentity.network;

import com.hbm.interfaces.NotableComments;
import com.hbm.util.Tuple.Pair;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RTTYSystem {

    /** Public frequency band for reading purposes, delayed by one tick */
    public static Map<Pair<Level, String>, RTTYChannel> broadcast = new HashMap<>();
    /** New message queue for writing, gets written into readable Map later on */
    public static Map<Pair<Level, String>, Object> newMessages = new HashMap<>();

    /** Pushes a new signal to be used next tick. Only the last signal pushed will be used. */
    public static void broadcast(Level level, String channelName, Object signal) {
        Pair<Level, String> identifier = new Pair<>(level, channelName);

        if(NumberUtils.isNumber("" + signal) && newMessages.containsKey(identifier)) {
            Object existing = newMessages.get(identifier);
            if(NumberUtils.isNumber("" + existing)) {
                try {
                    long first = Long.parseLong("" + signal);
                    long second = Long.parseLong("" + existing);
                    newMessages.put(identifier, "" + (first + second));
                    return;
                } catch(Exception ignored) { }
            }
        }

        newMessages.put(identifier, signal);
    }

    /** Returns the RTTY channel with that name, or null */
    public static RTTYChannel listen(Level level, String channelName) {
        return broadcast.get(new Pair<>(level, channelName));
    }

    /** Moves all new messages to the broadcast map, adding the appropriate timestamp and clearing the new message queue */
    public static void updateBroadcastQueue(MinecraftServer server) {

        for(Entry<Pair<Level, String>, Object> worldEntry : newMessages.entrySet()) {
            Pair<Level, String> identifier = worldEntry.getKey();
            Object lastSignal = worldEntry.getValue();

            RTTYChannel channel = new RTTYChannel();
            channel.timeStamp = identifier.getKey().getGameTime();
            channel.signal = lastSignal;

            broadcast.put(identifier, channel);
        }

        // todo funny music
//        HashMap<Pair<Level, String>, RTTYChannel> toAdd = new HashMap<>();
//        for(Level world : MinecraftServer.getServer().worldServers) {
//            RTTYChannel chan = new RTTYChannel();
//            chan.timeStamp = world.getGameTime();
//            chan.signal = getTestSender(chan.timeStamp);
//            toAdd.put(new Pair<>(world, "2012-08-06"), chan);
//        }
//
//        broadcast.putAll(toAdd);
        newMessages.clear();
    }

    @NotableComments
    public static class RTTYChannel {
        public long timeStamp = -1; //the totalWorldTime at the time of publishing, happens in the server tick event's PRE-phase. the publishing timestamp is that same number minus one
        public Object signal; // a signal can be anything, a number, an encoded string, an entire blue whale, Steve from accounting, the concept of death, 7492 hot dogs, etc.
    }

    /* Special objects for signifying specific signals to be used with RTTY machines (or telex) */
    public enum RTTYSpecialSignal {
        BEGIN_TTY,		//start a new message block
        STOP_TTY,		//end the message block
        PRINT_BUFFER	//print message, literally, it makes a paper printout
    }

}
