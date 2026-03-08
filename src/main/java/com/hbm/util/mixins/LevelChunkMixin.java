package com.hbm.util.mixins;

import com.hbm.render.block.loader.BlockRendererDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {

    @Inject(method = "setBlockState", at = @At("RETURN"))
    private void setBlockState(BlockPos pos, BlockState newState, boolean isMoving, CallbackInfoReturnable<BlockState> cir) {
        LevelChunk self = (LevelChunk) (Object) this;
        if (self.getLevel() == null || !self.getLevel().isClientSide()) return;

        BlockState oldState = cir.getReturnValue();
        if (oldState == null) return;

        BlockRendererDispatcher.INSTANCE.onBlockChanged(pos, oldState, newState);
    }
}
