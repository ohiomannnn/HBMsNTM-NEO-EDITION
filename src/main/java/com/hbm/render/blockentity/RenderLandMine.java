package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.LandMineBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public class RenderLandMine implements BlockEntityRenderer<LandMineBlockEntity> {

    public RenderLandMine(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(LandMineBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.translate(0.5, 0.0, 0.5);

        BlockState state = be.getBlockState();

        if (state.getBlock() == ModBlocks.MINE_AP.get()) {
            poseStack.scale(0.375F, 0.375F, 0.375F);
            poseStack.translate(0, -0.0625F * 3.5F, 0);

            Level level = be.getLevel();
            BlockPos pos = be.getBlockPos();
            if (level == null) return;

            Biome biome = level.getBiome(pos).value();

            VertexConsumer consumer;
            if (level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()) > pos.getY() + 2) consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MINE_AP_STONE_TEX));
            else if (biome.coldEnoughToSnow(pos)) consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MINE_AP_SNOW_TEX));
            else if (biome.getBaseTemperature() >= 1.5F && biome.getPrecipitationAt(pos) == Biome.Precipitation.NONE) consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MINE_AP_DESERT_TEX));
            else consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MINE_AP_GRASS_TEX));

            ResourceManager.mine_ap.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }

        if (state.getBlock() == ModBlocks.MINE_HE.get()) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180F));
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MINE_HE_TEX));
            ResourceManager.mine_he.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }

        if (state.getBlock() == ModBlocks.MINE_SHRAP.get()) {
            poseStack.scale(0.375F, 0.375F, 0.375F);
            poseStack.translate(0, -0.0625F * 3.5F, 0);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MINE_SHRAPNEL_TEX));
            ResourceManager.mine_ap.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }

        if (state.getBlock() == ModBlocks.MINE_FAT.get()) {
            poseStack.scale(0.25F, 0.25F, 0.25F);

            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MINE_FAT_TEX));
            ResourceManager.mine_fat.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }

        if (state.getBlock() == ModBlocks.MINE_NAVAL.get()) {
            poseStack.scale(1F, 1F, 1F);
            poseStack.translate(0, 0.5F, 0);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MINE_NAVAL_TEX));
            ResourceManager.mine_naval.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 128;
    }
}
