package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.LaunchPadBlockEntity;
import com.hbm.blockentity.bomb.NukeLittleBoyBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.item.ItemRenderMissileGeneric;
import com.hbm.render.item.RenderStarter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Consumer;

public class RenderLaunchPad extends BlockEntityRendererNT<LaunchPadBlockEntity> implements IBEWLRProvider {

    public RenderLaunchPad(Context ignored) { }

    @Override
    public BlockEntityRenderer<LaunchPadBlockEntity> create(Context context) {
        return new RenderLaunchPad(context);
    }

    @Override
    public void render(LaunchPadBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        float rot = switch (facing) {
            case DOWN, UP -> 0.0F;
            case WEST -> 90F;
            case SOUTH -> 180F;
            case EAST -> 270F;
            case NORTH -> 0F;
        };

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.MISSILE_PAD_TEX));
        ResourceManager.missile_pad.renderAll(poseStack, consumer, packedLight, packedOverlay);

        ItemStack toRender = be.toRender;

        if (!toRender.isEmpty()) {
            poseStack.translate(0F, 1F, 0F);
            Consumer<RenderStarter> render = ItemRenderMissileGeneric.renderers.get(toRender.getItem());
            if (render != null) render.accept(new RenderStarter(buffer, poseStack, packedLight, packedOverlay));
        }

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.LAUNCH_PAD.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(0F, -1F, 0F);
                poseStack.scale(3F, 3F, 3F);
            }

            @Override
            public void renderCommon(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(ResourceManager.MISSILE_PAD_TEX));
                ResourceManager.missile_pad.renderAll(poseStack, consumer, packedLight, packedOverlay);
            }
        };
    }
}
