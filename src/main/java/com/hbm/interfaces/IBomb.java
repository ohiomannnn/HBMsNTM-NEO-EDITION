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
        UNDEFINED(false, Component.empty()),				                                    //non-null type for passing to clients that don't process the return code
        DETONATED(true, Component.translatable("bomb.detonated")),						    //success for blowing up bombs
        TRIGGERED(true, Component.translatable("bomb.triggered")),						    //success for triggering other things
        LAUNCHED(true, Component.translatable("bomb.launched")),							//success for launching missiles
        ERROR_MISSING_COMPONENT(false, Component.translatable("bomb.missingComponent")),	//error for bomb parts missing
        ERROR_INCOMPATIBLE(false, Component.translatable("bomb.incompatible")),			//error for target being incompatible (but still implements IBomb for some reason), like locked blast doors
        ERROR_NO_BOMB(false, Component.translatable("bomb.nobomb"));						//not to be used by the bombs themselves, this is the generic error when trying to trigger no-bomb blocks

        private final Component unloc;
        private final boolean success;

        BombReturnCode(boolean success, Component unloc) {
            this.unloc = unloc;
            this.success = success;
        }

        public Component getUnlocalizedMessage() {
            return this.unloc;
        }

        public boolean wasSuccessful() {
            return this.success;
        }
    }
}