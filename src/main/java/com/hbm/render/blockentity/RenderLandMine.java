package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.LandMineBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class RenderLandMine extends BlockEntityRendererNT<LandMineBlockEntity> implements IBEWLRProvider {

    public RenderLandMine(Context context) { }

    @Override
    public BlockEntityRenderer<LandMineBlockEntity> create(Context context) {
        return new RenderLandMine(context);
    }

    @Override
    public void render(LandMineBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(180F));

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
            poseStack.mulPose(Axis.YN.rotationDegrees(90F));
            VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(ResourceManager.MINE_HE_TEX));
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
            VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(ResourceManager.MINE_NAVAL_TEX));
            ResourceManager.mine_naval.renderAll(poseStack, consumer, packedLight, packedOverlay);
        }

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public Item getItemForRenderer() {
        return null;
    }

    @Override
    public Item[] getItemsForRenderer() {
        return new Item[] {
                ModBlocks.MINE_AP.asItem(),
                ModBlocks.MINE_HE.asItem(),
                ModBlocks.MINE_SHRAP.asItem(),
                ModBlocks.MINE_FAT.asItem(),
                ModBlocks.MINE_NAVAL.asItem()
        };
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                if (stack.is(ModBlocks.MINE_AP.asItem()) || stack.is(ModBlocks.MINE_SHRAP.asItem())) {
                    poseStack.scale(8F, 8F, 8F);
                }
                if (stack.is(ModBlocks.MINE_HE.asItem())) {
                    poseStack.scale(6F, 6F, 6F);
                }
                if (stack.is(ModBlocks.MINE_NAVAL.asItem())) {
                    poseStack.translate(0F, 2F, -1F);
                    poseStack.scale(5F, 5F, 5F);
                }
                if (stack.is(ModBlocks.MINE_FAT.asItem())) {
                    poseStack.translate(0F, -1F, 0F);
                    poseStack.scale(7F, 7F, 7F);
                }
            }

            @Override
            public void renderNonInv(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean righthand) {
                if (stack.is(ModBlocks.MINE_HE.asItem())) {
                    if (righthand) {
                        poseStack.translate(0.25F, 0.6F, 0F);
                        poseStack.mulPose(Axis.YP.rotationDegrees(45F));
                        poseStack.mulPose(Axis.ZN.rotationDegrees(15F));
                    } else {
                        poseStack.translate(0.25F, 0.6F, 0F);
                        poseStack.mulPose(Axis.YN.rotationDegrees(45F));
                        poseStack.mulPose(Axis.ZN.rotationDegrees(15F));
                    }
                }
            }

            @Override
            public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                if (stack.is(ModBlocks.MINE_AP.asItem())) {
                    poseStack.scale(1.25F, 1.25F, 1.25F);
                    VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MINE_AP_GRASS_TEX));
                    ResourceManager.mine_ap.renderAll(poseStack, consumer, packedLight, packedOverlay);
                }
                if (stack.is(ModBlocks.MINE_HE.asItem())) {
                    poseStack.scale(4F, 4F, 4F);
                    VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(ResourceManager.MINE_HE_TEX));
                    ResourceManager.mine_he.renderAll(poseStack, consumer, packedLight, packedOverlay);
                }
                if (stack.is(ModBlocks.MINE_SHRAP.asItem())) {
                    poseStack.scale(1.25F, 1.25F, 1.25F);
                    VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MINE_SHRAPNEL_TEX));
                    ResourceManager.mine_ap.renderAll(poseStack, consumer, packedLight, packedOverlay);
                }
                if (stack.is(ModBlocks.MINE_NAVAL.asItem())) {
                    VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(ResourceManager.MINE_NAVAL_TEX));
                    ResourceManager.mine_naval.renderAll(poseStack, consumer, packedLight, packedOverlay);
                }
                if (stack.is(ModBlocks.MINE_FAT.asItem())) {
                    poseStack.translate(0.25F, 0F, 0F);
                    poseStack.mulPose(Axis.YP.rotationDegrees(90F));
                    VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MINE_FAT_TEX));
                    ResourceManager.mine_fat.renderAll(poseStack, consumer, packedLight, packedOverlay);
                }
            }
        };
    }
}
