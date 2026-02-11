package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.NukeGadgetBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RenderNukeGadget extends BlockEntityRendererNT<NukeGadgetBlockEntity> implements IBEWLRProvider {

    public RenderNukeGadget(Context ignored) { }

    @Override
    public BlockEntityRenderer<NukeGadgetBlockEntity> create(Context context) {
        return new RenderNukeGadget(context);
    }

    @Override
    public void render(NukeGadgetBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 0F;
            case SOUTH -> 90F;
            case EAST -> 180F;
            case NORTH -> 270F;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entitySmoothCutout(ResourceManager.NUKE_GADGET_TEX));
        ResourceManager.nuke_gadget.renderPart("Body", poseStack, consumer, packedLight, packedOverlay);

        GraphicsStatus graphics = Minecraft.getInstance().options.graphicsMode().get();

        if (graphics == GraphicsStatus.FANCY || graphics == GraphicsStatus.FABULOUS) {
            ResourceManager.nuke_gadget.renderPart("Wires", poseStack, consumer, packedLight, packedOverlay);
        }

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.NUKE_GADGET.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(0F, -3F, 0F);
                poseStack.scale(5F, 5F, 5F);
            }

            @Override
            public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.mulPose(Axis.YN.rotationDegrees(90F));
                VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.NUKE_GADGET_TEX));
                ResourceManager.nuke_gadget.renderPart("Body", poseStack, consumer, packedLight, packedOverlay);

                GraphicsStatus graphics = Minecraft.getInstance().options.graphicsMode().get();

                if (graphics == GraphicsStatus.FANCY || graphics == GraphicsStatus.FABULOUS) {
                    ResourceManager.nuke_gadget.renderPart("Wires", poseStack, consumer, packedLight, packedOverlay);
                }
            }
        };
    }
}
