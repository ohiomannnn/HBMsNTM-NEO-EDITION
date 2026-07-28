package com.hbm.render.entity.effect;

import com.hbm.entity.logic.DeathBlast;
import com.hbm.main.ResourceManager;
import com.hbm.render.material.Material;
import com.hbm.render.material.Material.CutoutMode;
import com.hbm.render.material.Material.ShadeMode;
import com.hbm.render.material.Material.Transparency;
import com.hbm.render.material.Material.WriteMask;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RenderDeathBlast extends EntityRenderer<DeathBlast> {

    private static Material material_7710;
    private static Material material_lightning;

    public static final RenderType BLAST = RenderType.create(
            "blast_render_type", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_FAN, 1024,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.POSITION_COLOR_SHADER)
                    .setLightmapState(RenderType.LIGHTMAP)
                    .setOverlayState(RenderType.OVERLAY)
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                    .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
                    .setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
                    .createCompositeState(false)
    );

    public RenderDeathBlast(EntityRendererProvider.Context context) {
        super(context);

        if(material_7710 == null || material_lightning == null) {
            material_7710 = Material.builder().texture(ResourceManager.WHITE_TEX).lightingMode(ShadeMode.OFF).writeMask(WriteMask.COLOR).cutout(CutoutMode.ZERO).transparency(Transparency.S7710).build();
            material_lightning = Material.builder().texture(ResourceManager.WHITE_TEX).lightingMode(ShadeMode.OFF).writeMask(WriteMask.COLOR).cutout(CutoutMode.ZERO).transparency(Transparency.LIGHTNING).build();
        }
    }

    @Override
    public void render(DeathBlast blast, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        VertexConsumer consumer = buffer.getBuffer(RenderDeathBlast.BLAST);

        Matrix4f matrix = poseStack.last().pose();
        Vector3f vector = new Vector3f(0.5F, 0F, 0F);

        for(int i = 0; i < 8; i++) {
            consumer.addVertex(matrix, vector.x, 250F, vector.z).setColor(0F, 1F, 0F, 1F);
            consumer.addVertex(matrix, vector.x, 0F, vector.z).setColor(0F, 1F, 0F, 1F);
            vector.rotateY(45);
            consumer.addVertex(matrix, vector.x, 0F, vector.z).setColor(0F, 1F, 0F, 1F);
            consumer.addVertex(matrix, vector.x, 250F, vector.z).setColor(0F, 1F, 0F, 1F);
        }

        for(int i = 0; i < 8; i++) {
            consumer.addVertex(matrix, vector.x / 2, 250F, vector.z / 2).setColor(1F, 0F, 1F, 1F);
            consumer.addVertex(matrix, vector.x / 2, 0F, vector.z / 2).setColor(1F, 0F, 1F, 1F);
            vector.rotateY(45);
            consumer.addVertex(matrix, vector.x / 2, 0F, vector.z / 2).setColor(1F, 0F, 1F, 1F);
            consumer.addVertex(matrix, vector.x / 2, 250F, vector.z / 2).setColor(1F, 0F, 1F, 1F);
        }

        renderOrb(blast, poseStack, partialTick);
    }

    private void renderOrb(DeathBlast blast, PoseStack poseStack, float partialTick) {

        RenderContext.setup(poseStack, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);

        float scale = 10 - 10F * ((blast.tickCount + partialTick) / (DeathBlast.MAX_AGE));
        float alpha = ((blast.tickCount + partialTick) / (DeathBlast.MAX_AGE));

        if(scale < 0) scale = 0;

        RenderContext.setColor(0.05F, 1F, 0.05F, alpha);

        RenderContext.scale(scale, scale, scale);
        ResourceManager.sphere.renderAll(material_7710);

        RenderContext.scale(1.25F, 1.25F, 1.25F);
        RenderContext.setColor(0F, 1F, 0F, alpha * 0.125F);

        for(int i = 0; i < 8; i++) {
            ResourceManager.sphere.renderAll(material_lightning);
            RenderContext.scale(1.05F, 1.05F, 1.05F);
        }

        RenderContext.setColor(1F, 1F, 1F, 1F);

        RenderContext.end();
    }

    @Override public ResourceLocation getTextureLocation(DeathBlast laser) { return ResourceManager.EMPTY; }
}
