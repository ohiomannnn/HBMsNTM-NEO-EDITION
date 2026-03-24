package com.hbm.render.block.loader;

import com.hbm.NuclearTechMod;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BlockRendererDispatcher {

    public static final BlockRendererDispatcher INSTANCE = new BlockRendererDispatcher();

    /** Block with renderer */
    private final Map<Block, BlockRenderer> renderers = new HashMap<>();

    /** ChunkPos.toLong(), set of BlockPoses in chunk with renderer */
    private final Map<Long, Set<BlockPos>> trackedPositions = new ConcurrentHashMap<>();

    public void init() {
        renderers.clear();
        BlockRenderers.getProviders().forEach((block, provider) -> {
            renderers.put(block, provider.create());
        });
        NuclearTechMod.LOGGER.info("Initialized {} custom block renderers", renderers.size());
    }

    public BlockRenderer getRenderer(Block block) {
        return renderers.get(block);
    }

    public boolean hasRenderer(Block block) {
        return renderers.containsKey(block);
    }

    public void registerItemRenderers(RegisterClientExtensionsEvent event) {

        for (Entry<Block, BlockRenderer> entry : renderers.entrySet()) {
            Block block = entry.getKey();
            BlockRenderer renderer = entry.getValue();

            BlockEntityWithoutLevelRenderer bewlr = renderer.getItemRenderer();
            if (bewlr == null) continue;

            Item item = block.asItem();
            if (item == Items.AIR) {
                NuclearTechMod.LOGGER.warn("Block {} has an item renderer but no BlockItem!", block);
                continue;
            }

            event.registerItem(new IClientItemExtensions() {
                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return bewlr;
                }
            }, item);
        }
    }

    public void onChunkLoaded(LevelChunk chunk) {
        if (renderers.isEmpty()) return;

        Set<BlockPos> positions = ConcurrentHashMap.newKeySet();
        ChunkPos chunkPos = chunk.getPos();
        LevelChunkSection[] sections = chunk.getSections();

        for (int i = 0; i < sections.length; i++) {
            LevelChunkSection section = sections[i];
            if (section == null || section.hasOnlyAir()) continue;

            if (!section.maybeHas(state -> this.hasRenderer(state.getBlock()))) continue;

            int sectionY = chunk.getMinBuildHeight() + i * 16;

            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 16; y++) {
                        BlockState state = section.getBlockState(x, y, z);
                        if (this.hasRenderer(state.getBlock())) {
                            positions.add(new BlockPos(chunkPos.getMinBlockX() + x, sectionY + y, chunkPos.getMinBlockZ() + z));
                        }
                    }
                }
            }
        }

        long key = chunkPos.toLong();
        if (!positions.isEmpty()) {
            trackedPositions.put(key, positions);
        } else {
            trackedPositions.remove(key);
        }
    }

    public void onChunkUnloaded(ChunkPos chunkPos) {
        trackedPositions.remove(chunkPos.toLong());
    }

    public void onBlockChanged(BlockPos pos, BlockState oldState, BlockState newState) {
        if (renderers.isEmpty()) return;

        boolean oldHas = this.hasRenderer(oldState.getBlock());
        boolean newHas = this.hasRenderer(newState.getBlock());
        if (!oldHas && !newHas) return;

        long chunkKey = ChunkPos.asLong(pos.getX() >> 4, pos.getZ() >> 4);
        BlockPos immutable = pos.immutable();

        if (newHas) {
            trackedPositions.computeIfAbsent(chunkKey, k -> ConcurrentHashMap.newKeySet()).add(immutable);
        } else {
            Set<BlockPos> set = trackedPositions.get(chunkKey);
            if (set != null) {
                set.remove(immutable);
                if (set.isEmpty()) {
                    trackedPositions.remove(chunkKey);
                }
            }
        }
    }

    public void clear() {
        trackedPositions.clear();
    }

    public void rescanAllChunks(@Nullable ClientLevel level) {
        trackedPositions.clear();

        if (level == null || renderers.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        int playerChunkX = mc.player.blockPosition().getX() >> 4;
        int playerChunkZ = mc.player.blockPosition().getZ() >> 4;

        int viewDist = mc.options.renderDistance().get() + 2;

        for (int cx = playerChunkX - viewDist; cx <= playerChunkX + viewDist; cx++) {
            for (int cz = playerChunkZ - viewDist; cz <= playerChunkZ + viewDist; cz++) {
                ChunkAccess chunkAccess = level.getChunkSource().getChunk(cx, cz, ChunkStatus.FULL, false);

                if (chunkAccess instanceof LevelChunk levelChunk) {
                    onChunkLoaded(levelChunk);
                }
            }
        }
    }

    public void renderAll(ClientLevel level, float partialTicks, MultiBufferSource buffer, Vec3 cameraPos) {
        if (renderers.isEmpty() || trackedPositions.isEmpty()) return;

        for (Entry<Long, Set<BlockPos>> entry : trackedPositions.entrySet()) {
            Set<BlockPos> positions = entry.getValue();
            Iterator<BlockPos> it = positions.iterator();

            while (it.hasNext()) {
                BlockPos pos = it.next();
                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();

                BlockRenderer renderer = renderers.get(block);
                if (renderer == null) {
                    it.remove();
                    continue;
                }

                if (!renderer.shouldRender(pos, cameraPos)) continue;

                PoseStack poseStack = new PoseStack();
                poseStack.pushPose();
                poseStack.translate(pos.getX() - cameraPos.x, pos.getY() - cameraPos.y, pos.getZ() - cameraPos.z);

                int packedLight = LevelRenderer.getLightColor(level, pos);

                try {
                    renderer.render(pos, state, poseStack, buffer, partialTicks, packedLight, OverlayTexture.NO_OVERLAY);
                } catch (Exception e) {
                    NuclearTechMod.LOGGER.error("Error rendering block renderer at {}", pos, e);
                }

                poseStack.popPose();
            }
        }
    }
}
