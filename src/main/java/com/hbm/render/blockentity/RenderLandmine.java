package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.LandMineBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class RenderLandmine extends BlockEntityRendererNT<LandMineBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<LandMineBlockEntity> create(Context context) { return new RenderLandmine(); }

    @Override
    public void render(LandMineBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        RenderContext.setup(poseStack, packedLight, packedOverlay);
        RenderContext.translate(0.5, 0.0, 0.5);
        RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
        RenderContext.enableCull(false);

        BlockState state = be.getBlockState();

        if (state.getBlock() == ModBlocks.MINE_AP.get()) {
            RenderContext.scale(0.375F, 0.375F, 0.375F);
            RenderContext.translate(0, -0.0625F * 3.5F, 0);

            Level level = be.getLevel();
            BlockPos pos = be.getBlockPos();
            if (level == null) return;

            Biome biome = level.getBiome(pos).value();

            ResourceLocation texture;
            if (level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()) > pos.getY() + 2)              texture = ResourceManager.MINE_AP_STONE_TEX;
            else if (biome.coldEnoughToSnow(pos))                                                                     texture = ResourceManager.MINE_AP_SNOW_TEX;
            else if (biome.getBaseTemperature() >= 1.5F && biome.getPrecipitationAt(pos) == Biome.Precipitation.NONE) texture = ResourceManager.MINE_AP_DESERT_TEX;
            else                                                                                                      texture = ResourceManager.MINE_AP_GRASS_TEX;

            RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(texture));
            ResourceManager.mine_ap.renderAll();
        }

        if (state.getBlock() == ModBlocks.MINE_HE.get()) {
            RenderContext.mulPose(Axis.YN.rotationDegrees(90F));
            RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.MINE_HE_TEX));
            ResourceManager.mine_he.renderAll();
        }

        if (state.getBlock() == ModBlocks.MINE_SHRAP.get()) {
            RenderContext.scale(0.375F, 0.375F, 0.375F);
            RenderContext.translate(0, -0.0625F * 3.5F, 0);
            RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.MINE_SHRAPNEL_TEX));
            ResourceManager.mine_ap.renderAll();
        }

        if (state.getBlock() == ModBlocks.MINE_FAT.get()) {
            RenderContext.scale(0.25F, 0.25F, 0.25F);
            RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.MINE_FAT_TEX));
            ResourceManager.mine_fat.renderAll();
        }

        if (state.getBlock() == ModBlocks.MINE_NAVAL.get()) {
            RenderContext.scale(1F, 1F, 1F);
            RenderContext.translate(0, 0.5F, 0);
            RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.MINE_NAVAL_TEX));
            ResourceManager.mine_naval.renderAll();
        }

        RenderContext.enableCull(true);
        RenderContext.end();
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
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                if (stack.is(ModBlocks.MINE_AP.asItem()) || stack.is(ModBlocks.MINE_SHRAP.asItem())) {
                    RenderContext.scale(8F, 8F, 8F);
                }
                if (stack.is(ModBlocks.MINE_HE.asItem())) {
                    RenderContext.scale(6F, 6F, 6F);
                }
                if (stack.is(ModBlocks.MINE_NAVAL.asItem())) {
                    RenderContext.translate(0F, 2F, -1F);
                    RenderContext.scale(5F, 5F, 5F);
                }
                if (stack.is(ModBlocks.MINE_FAT.asItem())) {
                    RenderContext.translate(0F, -1F, 0F);
                    RenderContext.scale(7F, 7F, 7F);
                }
            }

            @Override
            public void renderNonInv(ItemStack stack, MultiBufferSource buffer, boolean rightHand) {
                if (stack.is(ModBlocks.MINE_HE.asItem())) {
                    if (rightHand) {
                        RenderContext.translate(0.25F, 0.6F, 0F);
                        RenderContext.mulPose(Axis.YP.rotationDegrees(45F));
                        RenderContext.mulPose(Axis.ZN.rotationDegrees(15F));
                    } else {
                        RenderContext.translate(0.25F, 0.6F, 0F);
                        RenderContext.mulPose(Axis.YN.rotationDegrees(45F));
                        RenderContext.mulPose(Axis.ZN.rotationDegrees(15F));
                    }
                }
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.enableCull(false);
                if (stack.is(ModBlocks.MINE_AP.asItem())) {
                    RenderContext.scale(1.25F, 1.25F, 1.25F);
                    RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(ResourceManager.MINE_AP_GRASS_TEX));
                    ResourceManager.mine_ap.renderAll();
                }
                if (stack.is(ModBlocks.MINE_HE.asItem())) {
                    RenderContext.scale(4F, 4F, 4F);
                    RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(ResourceManager.MINE_HE_TEX));
                    ResourceManager.mine_he.renderAll();
                }
                if (stack.is(ModBlocks.MINE_SHRAP.asItem())) {
                    RenderContext.scale(1.25F, 1.25F, 1.25F);
                    RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(ResourceManager.MINE_SHRAPNEL_TEX));
                    ResourceManager.mine_ap.renderAll();
                }
                if (stack.is(ModBlocks.MINE_NAVAL.asItem())) {
                    RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(ResourceManager.MINE_NAVAL_TEX));
                    ResourceManager.mine_naval.renderAll();
                }
                if (stack.is(ModBlocks.MINE_FAT.asItem())) {
                    RenderContext.translate(0.25F, 0F, 0F);
                    RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                    RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(ResourceManager.MINE_FAT_TEX));
                    ResourceManager.mine_fat.renderAll();
                }
                RenderContext.enableCull(true);
            }
        };
    }
}
