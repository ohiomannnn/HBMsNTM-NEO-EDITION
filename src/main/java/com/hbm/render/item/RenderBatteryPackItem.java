package com.hbm.render.item;

import com.hbm.items.machine.BatteryPackItem;
import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;

public class RenderBatteryPackItem extends ItemRenderBase {

    @Override
    public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.translate(0F, -3F, 0F);
        poseStack.scale(5F, 5F, 5F);
    }

    @Override
    public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (stack.getItem() instanceof BatteryPackItem item) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(item.getPack().texture));
            ResourceManager.battery_socket.renderPart(item.getPack().isCapacitor() ? "Capacitor" : "Battery", poseStack, consumer, packedLight, packedOverlay);
        }
    }
}
