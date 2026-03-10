package com.hbm.render.blockentity;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.machine.storage.BatterySocketBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ModBlocks;
import com.hbm.items.ModItems;
import com.hbm.items.machine.BatteryPackItem;
import com.hbm.main.ResourceManager;
import com.hbm.render.CustomRenderTypes;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.BeamPronter;
import com.hbm.render.util.BeamPronter.BeamType;
import com.hbm.render.util.BeamPronter.WaveType;
import com.hbm.render.util.HorsePronter;
import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.Random;

public class RenderBatterySocket extends BlockEntityRendererNT<BatterySocketBlockEntity> implements IBEWLRProvider {

    private static final ResourceLocation blorbo = HBMsNTM.withDefaultNamespaceNT("textures/models/horse/sunburst.png");

    @Override public BlockEntityRenderer<BatterySocketBlockEntity> create(Context context) { return new RenderBatterySocket(); }

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

        VertexConsumer consumerSocket = buffer.getBuffer(CustomRenderTypes.EC_NC_NC.apply(ResourceManager.BATTERY_SOCKET_TEX));
        ResourceManager.battery_socket.renderPart("Socket", poseStack, consumerSocket, packedLight, packedOverlay);

        ItemStack render = be.syncStack;
        if (!render.isEmpty()) {
            if (render.getItem() instanceof BatteryPackItem packItem) {
                VertexConsumer consumerPack = buffer.getBuffer(CustomRenderTypes.EC_NC_NC.apply(packItem.getPack().texture));
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
    public AABB getRenderBoundingBox(BatterySocketBlockEntity be) {

        if (bb == null) {
            int x = be.getBlockPos().getX();
            int y = be.getBlockPos().getY();
            int z = be.getBlockPos().getZ();

            bb = new AABB(
                    x - 1,
                    y - 0,
                    z - 1,
                    x - 2,
                    y - 2,
                    z - 2
            );
        }

        return bb;
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.MACHINE_BATTERY_SOCKET.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(0F, -2F, 0F);
                poseStack.scale(5F, 5F, 5F);
            }

            @Override
            public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                VertexConsumer consumer = buffer.getBuffer(CustomRenderTypes.EC_NC.apply(ResourceManager.BATTERY_SOCKET_TEX));
                ResourceManager.battery_socket.renderPart("Socket", poseStack, consumer, packedLight, packedOverlay);
            }
        };
    }
}
