package com.hbm.render.blockentity;

import com.hbm.blockentity.network.PipeBaseBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBaseStandard;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;

import static com.hbm.blocks.network.FluidDuctConnectingBlock.*;

public class RenderPipe extends BlockEntityRendererNT<PipeBaseBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<PipeBaseBlockEntity> create(Context context) { return new RenderPipe(); }

    @Override
    public void render(PipeBaseBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Level level = be.getLevel();
        BlockState state = be.getBlockState();
        if (level == null) return;

        FluidType type = be.getFluidType();
        int color = type.getColor();

        boolean nX = state.getValue(WEST);
        boolean pX = state.getValue(EAST);
        boolean nY = state.getValue(DOWN);
        boolean pY = state.getValue(UP);
        boolean nZ = state.getValue(NORTH);
        boolean pZ = state.getValue(SOUTH);

        int mask = 0 + (pX ? 32 : 0) + (nX ? 16 : 0) + (pY ? 8 : 0) + (nY ? 4 : 0) + (pZ ? 2 : 0) + (nZ ? 1 : 0);

        int meta = state.getValue(META);
        ResourceLocation tex = getTexture(meta);
        ResourceLocation texOverlay = getOverlayTexture(meta);

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);

        if (mask == 0) {
            renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "pX", tex, texOverlay);
            renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "nX", tex, texOverlay);
            renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "pY", tex, texOverlay);
            renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "nY", tex, texOverlay);
            renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "pZ", tex, texOverlay);
            renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "nZ", tex, texOverlay);
        } else if (mask == 0b100000 || mask == 0b010000) {
            renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "pX", tex, texOverlay);
            renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "nX", tex, texOverlay);
        } else if (mask == 0b001000 || mask == 0b000100) {
            renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "pY", tex, texOverlay);
            renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "nY", tex, texOverlay);
        } else if (mask == 0b000010 || mask == 0b000001) {
            renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "pZ", tex, texOverlay);
            renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "nZ", tex, texOverlay);
        } else {

            if (pX) renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "pX", tex, texOverlay);
            if (nX) renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "nX", tex, texOverlay);
            if (pY) renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "pY", tex, texOverlay);
            if (nY) renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "nY", tex, texOverlay);
            if (pZ) renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "nZ", tex, texOverlay);
            if (nZ) renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "pZ", tex, texOverlay);

            if (!pX && !pY && !pZ) renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "ppn", tex, texOverlay);
            if (!pX && !pY && !nZ) renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "ppp", tex, texOverlay);
            if (!nX && !pY && !pZ) renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "npn", tex, texOverlay);
            if (!nX && !pY && !nZ) renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "npp", tex, texOverlay);
            if (!pX && !nY && !pZ) renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "pnn", tex, texOverlay);
            if (!pX && !nY && !nZ) renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "pnp", tex, texOverlay);
            if (!nX && !nY && !pZ) renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "nnn", tex, texOverlay);
            if (!nX && !nY && !nZ) renderDuct(buffer, poseStack, color, packedLight, packedOverlay, "nnp", tex, texOverlay);
        }

        poseStack.popPose();
    }

    private void renderDuct(MultiBufferSource buffer, PoseStack poseStack, int color, int packedLight, int packedOverlay, String part, ResourceLocation tex, ResourceLocation texOverlay) {
        Color col = new Color(color);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(tex));
        //ResourceManager.pipe_neo.renderPart(part, poseStack, consumer, packedLight, packedOverlay);
        VertexConsumer consumerOverlay = buffer.getBuffer(RenderType.entityCutout(texOverlay));
        //ResourceManager.pipe_neo.renderPart(part, poseStack, consumerOverlay, packedLight, packedOverlay, col);
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.FLUID_DUCT_NEO.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBaseStandard() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(0F, 0.15F, 0F);
            }

            @Override
            public void renderNonInv(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean righthand) {
                poseStack.translate(0F, 0.5F, 0F);
            }

            @Override
            public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
                poseStack.scale(1.25F, 1.25F, 1.25F);
                int meta = MetaHelper.getMeta(stack);
                ResourceLocation tex = getTexture(meta);
                ResourceLocation texOverlay = getOverlayTexture(meta);
                VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(tex));
                //ResourceManager.pipe_neo.renderPart("pX", poseStack, consumer, packedLight, packedOverlay);
                //ResourceManager.pipe_neo.renderPart("nX", poseStack, consumer, packedLight, packedOverlay);
                //ResourceManager.pipe_neo.renderPart("pZ", poseStack, consumer, packedLight, packedOverlay);
                //ResourceManager.pipe_neo.renderPart("nZ", poseStack, consumer, packedLight, packedOverlay);
                //VertexConsumer consumerOverlay = buffer.getBuffer(RenderType.entityCutout(texOverlay));
                //Color col = new Color(Fluids.NONE.getColor());
                //ResourceManager.pipe_neo.renderPart("pX", poseStack, consumerOverlay, packedLight, packedOverlay, col);
                //ResourceManager.pipe_neo.renderPart("nX", poseStack, consumerOverlay, packedLight, packedOverlay, col);
                //ResourceManager.pipe_neo.renderPart("pZ", poseStack, consumerOverlay, packedLight, packedOverlay, col);
                //ResourceManager.pipe_neo.renderPart("nZ", poseStack, consumerOverlay, packedLight, packedOverlay, col);
            }
        };
    }

    private static ResourceLocation getTexture(int meta) {
        return switch (meta) {
            case 1 -> ResourceManager.PIPE_NEO_TEX;
            case 2 -> ResourceManager.PIPE_SILVER_TEX;
            case 3 -> ResourceManager.PIPE_COLORED_TEX;
            default -> ResourceManager.EMPTY;
        };
    }

    private static ResourceLocation getOverlayTexture(int meta) {
        return switch (meta) {
            case 1 -> ResourceManager.PIPE_NEO_OVERLAY_TEX;
            case 2 -> ResourceManager.PIPE_SILVER_OVERLAY_TEX;
            case 3 -> ResourceManager.PIPE_COLORED_OVERLAY_TEX;
            default -> ResourceManager.EMPTY;
        };
    }
}
