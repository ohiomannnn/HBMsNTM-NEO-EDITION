package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeFatManBlockEntity;
import com.hbm.blocks.bomb.NukeManBlock;
import com.hbm.main.ResourceManager;
import com.hbm.render.loader.IModelCustom;
import com.hbm.render.loader.bakedLoader.HFRObjGeometry.HFRObjBakedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class RenderNukeFatMan implements BlockEntityRenderer<NukeFatManBlockEntity> {

    // you need to register every standalone model resource location??? fuck this
    private static IModelCustom getModel(ResourceLocation location) {
        HFRObjBakedModel bakedModel = (HFRObjBakedModel) Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(location));
        return bakedModel.getRenderModel();
    }

    public RenderNukeFatMan(BlockEntityRendererProvider.Context ignored) { }

    @Override
    public void render(NukeFatManBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

//        if (model == null) {
//            model = getModel(HBMsNTM.withDefaultNamespaceNT("block/nuke_fatman"));
//            return;
//        }

        Direction facing = be.getBlockState().getValue(NukeManBlock.FACING);
        float rot = switch (facing) {
            case EAST -> 90f;
            case NORTH -> 180f;
            case WEST -> 270f;
            default -> 0f;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.NUKE_FAT_MAN_TEX));
        ResourceManager.nuke_fat_man.renderAll(poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(NukeFatManBlockEntity blockEntity) {
        return true;
    }
}
