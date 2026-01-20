package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.storage.BatterySocketBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.items.machine.BatteryPackItem;
import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderBatterySocket implements BlockEntityRenderer<BatterySocketBlockEntity> {

    public RenderBatterySocket(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(BatterySocketBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        float rot = switch (facing) {
            case NORTH -> 270f;
            case SOUTH -> 90f;
            case WEST -> 0f;
            default -> 180f;
        };


        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));
        poseStack.translate(0.5, 0, -0.5);

        VertexConsumer consumerSocket = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.BATTERY_SOCKET_TEX));
        ResourceManager.battery_socket.renderPart("Socket", poseStack, consumerSocket, packedLight, packedOverlay);

        ItemStack render = be.syncStack;
        if (!render.isEmpty()) {
            if (render.getItem() instanceof BatteryPackItem packItem) {
                VertexConsumer consumerPack = buffer.getBuffer(RenderType.entityCutoutNoCull(packItem.getPack().texture));
                ResourceManager.battery_socket.renderPart(packItem.getPack().isCapacitor() ? "Capacitor" : "Battery", poseStack, consumerPack, packedLight, packedOverlay);
            }
        }

        poseStack.popPose();
    }

    private AABB bb = null;

    @Override
    public AABB getRenderBoundingBox(BatterySocketBlockEntity blockEntity) {

        if (bb == null) {
            bb = new AABB(
                    blockEntity.getBlockPos().getX() - 1,
                    blockEntity.getBlockPos().getY(),
                    blockEntity.getBlockPos().getZ() - 1,
                    blockEntity.getBlockPos().getX() - 2,
                    blockEntity.getBlockPos().getY() - 2,
                    blockEntity.getBlockPos().getZ() - 2
            );
        }

        return bb;
    }
}
