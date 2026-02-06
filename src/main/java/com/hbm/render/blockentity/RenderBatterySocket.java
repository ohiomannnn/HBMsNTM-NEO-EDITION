package com.hbm.render.blockentity;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.machine.storage.BatterySocketBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.items.ModItems;
import com.hbm.items.machine.BatteryPackItem;
import com.hbm.main.ResourceManager;
import com.hbm.render.util.BeamPronter;
import com.hbm.render.util.BeamPronter.BeamType;
import com.hbm.render.util.BeamPronter.WaveType;
import com.hbm.render.util.HorsePronter;
import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.Random;

public class RenderBatterySocket implements BlockEntityRenderer<BatterySocketBlockEntity> {

    private static final ResourceLocation blorbo = HBMsNTM.withDefaultNamespaceNT("textures/models/horse/sunburst.png");

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
            } else if (render.is(ModItems.BATTERY_CREATIVE)) {
                poseStack.pushPose();
                poseStack.scale(0.75F, 0.75F, 0.75F);
                poseStack.mulPose(Axis.YN.rotationDegrees((be.getLevel().getGameTime() % 360 + partialTicks) * 25F));

                HorsePronter.reset();
                HorsePronter.enableHorn();
                HorsePronter.pront(buffer, poseStack, packedLight, packedOverlay, blorbo);
                poseStack.popPose();

                Random rand = new Random(be.getLevel().getGameTime() / 5);
                rand.nextBoolean();

                for (int i = -1; i <= 1; i += 2) {
                    for (int j = -1; j <= 1; j += 2) {
                        if (rand.nextInt(4) == 0) {
                            poseStack.pushPose();
                            poseStack.translate(0, 0.75, 0);
                            BeamPronter.prontBeam(poseStack, buffer, new Vec3NT(0.4375 * i, 1.1875, 0.4375 * j), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int) (System.currentTimeMillis() % 1000) / 50, 15, 0.0625F, 3, 0.025F);
                            BeamPronter.prontBeam(poseStack, buffer, new Vec3NT(0.4375 * i, 1.1875, 0.4375 * j), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int) (System.currentTimeMillis() % 1000) / 50, 1, 0, 3, 0.025F);
                            poseStack.popPose();
                        }
                    }
                }
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
