package com.hbm.blockentity.door;

import com.hbm.blockentity.DoorGenericBlockEntity;
import com.hbm.render.anim.BusAnimation;
import com.hbm.render.anim.BusAnimationSequence;
import com.hbm.render.anim.HbmAnimations.Animation;
import com.hbm.util.Clock;

import java.nio.DoubleBuffer;

public interface IRenderDoors {

    void render(DoorGenericBlockEntity door, DoubleBuffer buf);

    static float[] getRelevantTransformation(String bus, Animation anim) {

        if(anim != null) {

            BusAnimation buses = anim.animation;
            int millis = (int)(Clock.get_ms() - anim.startMillis);

            BusAnimationSequence seq = buses.getBus(bus);

            if (seq != null) {
                float[] trans = seq.getTransformation(millis);
                if(trans != null) return trans;
            }
        }

        return new float[] {
                0F, 0F, 0F, // position
                0F, 0F, 0F, // rotation
                1F, 1F, 1F, // scale
                0F, 0F, 0F, // offset
                0F, 1F, 2F, // XYZ order
        };
    }
}
