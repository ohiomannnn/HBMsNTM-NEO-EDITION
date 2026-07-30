package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.oil.MachinePumpjackBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RenderPumpjack extends BlockEntityRendererNT<MachinePumpjackBlockEntity> implements IBEWLRProvider {

    // i have no idea how its actually called
    private static final RenderType WIRES = RenderType.create(
            "wires_render_type", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, 1024,
            RenderType.CompositeState.builder()
                    // shader with actual use of lightmap
                    .setShaderState(RenderType.RENDERTYPE_TEXT_BACKGROUND_SHADER)
                    .setTextureState(RenderType.NO_TEXTURE)
                    .setLightmapState(RenderType.LIGHTMAP)
                    .setOverlayState(RenderType.OVERLAY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .createCompositeState(false)
    );

    @Override public BlockEntityRenderer<MachinePumpjackBlockEntity> create(Context context) { return new RenderPumpjack(); }

    @Override
    public void render(MachinePumpjackBlockEntity be, MultiBufferSource buffer, float partialTick) {

        RenderContext.translate(0.5F, 0F, 0.5F);

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case EAST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case WEST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
        }

        float rotation = Mth.lerp(partialTick, be.prevRot, be.rot);

        bindTexture(ResourceManager.PUMPJACK_TEX);
        ResourceManager.pumpjack.renderPart("Base");

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.5F, -5.5F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(rotation - 90F));
        RenderContext.translate(0F, -1.5F, 5.5F);
        ResourceManager.pumpjack.renderPart("Rotor");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(0F, 3.5F, -3.5F);
        RenderContext.mulPose(Axis.XP.rotationDegrees((float) (Math.toDegrees(Math.sin(Math.toRadians(rotation))) * 0.25)));
        RenderContext.translate(0F, -3.5F, 3.5F);
        ResourceManager.pumpjack.renderPart("Head");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(0F, (float) -Math.sin(Math.toRadians(rotation)), 0F);
        ResourceManager.pumpjack.renderPart("Carriage");
        RenderContext.popPose();

        Vector3f backPos = new Vector3f(0F, 0F, -2F);
        backPos.rotateX((float) Math.sin(Math.toRadians(rotation)) * 0.25F);

        Vector3f rot = new Vector3f(0F, 0.5F, 0F);
        rot.rotateX((float) Math.toRadians(rotation - 90));

        VertexConsumer consumer = buffer.getBuffer(WIRES);
        int packedLight = RenderContext.light();
        Matrix4f matrix = RenderContext.poseStack().last().pose();

        for(int i = -1; i <= 1; i += 2) {

            consumer.addVertex(matrix, 0.53125F * i, 1.5F + rot.y, -5.5F + rot.z - 0.0625F).setColor(0.5F, 0.5F, 0.5F, 1).setLight(packedLight);
            consumer.addVertex(matrix, 0.53125F * i, 1.5F + rot.y, -5.5F + rot.z + 0.0625F).setColor(0.5F, 0.5F, 0.5F, 1).setLight(packedLight);

            consumer.addVertex(matrix, 0.53125F * i, 3.5F + backPos.y, -3.5F + backPos.z + 0.0625F).setColor(0.5F, 0.5F, 0.5F, 1).setLight(packedLight);
            consumer.addVertex(matrix, 0.53125F * i, 3.5F + backPos.y, -3.5F + backPos.z - 0.0625F).setColor(0.5F, 0.5F, 0.5F, 1).setLight(packedLight);
        }

        float pd = 0.03125F;
        float width = 0.25F;

        float height = (float) -Math.sin(Math.toRadians(rotation));

        for(int i = -1; i <= 1; i += 2) {

            float pRot = (float) (Math.sin(Math.toRadians(rotation)) * 0.25);

            Vector3f frontPos = new Vector3f(0F, 0F, 1F);
            frontPos.rotateX(pRot);

            float dist = 0.03125F;
            Vector3f frontRad = new Vector3f(0F, 0F, 2.5F + dist);
            float cutlet = 360F / 32F;
            frontRad.rotateX(pRot);
            frontRad.rotateX((float) Math.toRadians(cutlet * -3));

            for(int j = 0; j < 4; j++) {

                float sumY = frontPos.y + frontRad.y;
                float sumZ = frontPos.z + frontRad.z;
                if(frontRad.y < 0) sumZ = 3.5F + dist * 0.5F;

                consumer.addVertex(matrix, (width - pd) * i, 3.5F + sumY, -3.5F + sumZ).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);
                consumer.addVertex(matrix, (width + pd) * i, 3.5F + sumY, -3.5F + sumZ).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);

                frontRad.rotateX((float) Math.toRadians(cutlet));

                sumY = frontPos.y + frontRad.y;
                sumZ = frontPos.z + frontRad.z;
                if(frontRad.y < 0) sumZ = 3.5F + dist * 0.5F;

                consumer.addVertex(matrix, (width + pd) * i, 3.5F + sumY, -3.5F + sumZ).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);
                consumer.addVertex(matrix, (width - pd) * i, 3.5F + sumY, -3.5F + sumZ).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);
            }

            float sumY = frontPos.y + frontRad.y;
            float sumZ = frontPos.z + frontRad.z;
            if(frontRad.y < 0) sumZ = 3.5F + dist * 0.5F;

            consumer.addVertex(matrix, (width + pd) * i, 3.5F + sumY, -3.5F + sumZ).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);
            consumer.addVertex(matrix, (width - pd) * i, 3.5F + sumY, -3.5F + sumZ).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);

            consumer.addVertex(matrix, (width - pd) * i, 2F + height, 0F).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);
            consumer.addVertex(matrix, (width + pd) * i, 2F + height, 0F).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);
        }

        float p = 0.03125F;
        consumer.addVertex(matrix, p, height + 1.5F, p).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);
        consumer.addVertex(matrix, -p, height + 1.5F, -p).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);
        consumer.addVertex(matrix, -p, 0.75F, -p).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);
        consumer.addVertex(matrix, p, 0.75F,  p).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);
        consumer.addVertex(matrix, -p, height + 1.5F, p).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);
        consumer.addVertex(matrix, p, height + 1.5F, -p).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);
        consumer.addVertex(matrix, p, 0.75F, -p).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);
        consumer.addVertex(matrix, -p, 0.75F, p).setColor(0.2F, 0.2F, 0.2F, 1F).setLight(packedLight);
    }

    private AABB bb = null;

    @Override
    public AABB getRenderBoundingBox(MachinePumpjackBlockEntity be) {
        if(bb == null) {
            BlockPos pos = be.getBlockPos();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            bb = new AABB(
                    x - 7,
                    y - 0,
                    z - 7,
                    x + 8,
                    y + 6,
                    z + 8
            );
        }

        return bb;
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_PUMPJACK.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -2F, 0F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.scale(4F, 4F, 4F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderSystem.disableCull();
                RenderContext.scale(0.5F, 0.5F, 0.5F);
                RenderContext.translate(0F, 0F, 3F);
                bindTexture(ResourceManager.PUMPJACK_TEX); ResourceManager.pumpjack.renderAll();
                RenderSystem.enableCull();
            }
        };
    }
}
