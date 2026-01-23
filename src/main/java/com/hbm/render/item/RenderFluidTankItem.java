package com.hbm.render.item;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.IPersistentNBT;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.main.ResourceManager;
import com.hbm.render.blockentity.RenderFluidTank;
import com.hbm.util.TagsUtilDegradation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderFluidTankItem extends BlockEntityWithoutLevelRenderer {

    public RenderFluidTankItem() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        FluidTank tank = new FluidTank(Fluids.NONE, 0);
        boolean exploded = false;
        if (TagsUtilDegradation.getTag(stack).contains(IPersistentNBT.NBT_PERSISTENT_KEY)) {
            CompoundTag persistentTag = TagsUtilDegradation.getTag(stack).getCompound(IPersistentNBT.NBT_PERSISTENT_KEY);
            tank.readFromNBT(persistentTag, "Tank");
            exploded = persistentTag.getBoolean("HasExploded");
        }

        FluidType type = tank.getTankType();

        if (!exploded) {
            VertexConsumer frameConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.TANK_TEX));
            ResourceManager.fluid_tank.renderPart("Frame", poseStack, frameConsumer, packedLight, packedOverlay);
            VertexConsumer tankConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(HBMsNTM.withDefaultNamespaceNT(RenderFluidTank.getTextureFromType(type))));
            ResourceManager.fluid_tank.renderPart("Tank", poseStack, tankConsumer, packedLight, packedOverlay);
        } else {
            VertexConsumer frameConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.TANK_TEX));
            ResourceManager.fluid_tank_exploded.renderPart("Frame", poseStack, frameConsumer, packedLight, packedOverlay);
            VertexConsumer innerConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(ResourceManager.TANK_INNER_TEX));
            ResourceManager.fluid_tank_exploded.renderPart("TankInner", poseStack, innerConsumer, packedLight, packedOverlay);
            VertexConsumer tankConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(HBMsNTM.withDefaultNamespaceNT(RenderFluidTank.getTextureFromType(type))));
            ResourceManager.fluid_tank_exploded.renderPart("Tank", poseStack, tankConsumer, packedLight, packedOverlay);
        }
    }
}
