package com.hbm.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public interface IBomb {
    /**
     * Triggers the bomb and generates a return code. Since most bombs have a serverside inventory, the return code
     * should only be processed serverside, what's returned on the client should be ignored.
     * Often invoked by onNeighborBlockChanged, so in any case make sure to check for level-clientside.
     */
    BombReturnCode explode(Level level, BlockPos pos);

    enum BombReturnCode {
        // Non-null type for passing to clients that don't process the return code
        UNDEFINED(false, ""),
        // Success for blowing up bombs
        DETONATED(true,"bomb.detonated"),
        // Success for triggering other things
        TRIGGERED(true,"bomb.triggered"),
        // Success for launching missiles
        LAUNCHED(true, "bomb.launched"),
        // Error for bomb parts missing
        ERROR_MISSING_COMPONENT(false, "bomb.missingComponent"),
        // Error for target being incompatible (but still implements IBomb for some reason), like locked blast doors
        ERROR_INCOMPATIBLE(false, "bomb.incompatible"),
        // Not to be used by the bombs themselves, this is the generic error when trying to trigger no-bomb blocks
        ERROR_NO_BOMB(false, "bomb.nobomb");

        private final String unlocalizedMessage;
        private final boolean success;

        BombReturnCode(boolean success, String unlocalizedMessage) {
            this.unlocalizedMessage = unlocalizedMessage;
            this.success = success;
        }

        public String getUnlocalizedMessage() { return this.unlocalizedMessage; }
        public boolean wasSuccessful() { return this.success; }
    }
}