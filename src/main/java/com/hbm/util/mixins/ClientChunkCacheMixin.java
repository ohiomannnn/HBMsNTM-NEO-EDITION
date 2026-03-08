package com.hbm.util.mixins;

import com.hbm.render.block.loader.BlockRendererDispatcher;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ClientChunkCache.class)
public abstract class ClientChunkCacheMixin {

    @Inject(method = "replaceWithPacketData", at = @At("RETURN"))
    private void replaceWithPacketData(int x, int z, FriendlyByteBuf buffer, CompoundTag heightmaps, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> consumer, CallbackInfoReturnable<LevelChunk> cir) {
        LevelChunk chunk = cir.getReturnValue();
        if (chunk != null) {
            BlockRendererDispatcher.INSTANCE.onChunkLoaded(chunk);
        }
    }

    @Inject(method = "drop", at = @At("HEAD"))
    private void onChunkDropped(ChunkPos chunkPos, CallbackInfo ci) {
        BlockRendererDispatcher.INSTANCE.onChunkUnloaded(chunkPos);
    }
}
