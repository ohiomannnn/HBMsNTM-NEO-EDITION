package com.hbm.render.entity.effect;

import com.hbm.entity.logic.OrbitalLaser;
import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RenderOrbitalLaser extends EntityRenderer<OrbitalLaser> {

    public RenderOrbitalLaser(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(OrbitalLaser laser, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        VertexConsumer consumer = buffer.getBuffer(RenderDeathBlast.BLAST);

        Matrix4f matrix = poseStack.last().pose();
        Vector3f vector = new Vector3f(0.5F, 0F, 0F);

        for(int i = 0; i < 8; i++) {
            consumer.addVertex(matrix, vector.x, 250F, vector.z).setColor(1F, 0, 0, 1F);
            consumer.addVertex(matrix, vector.x, 0F, vector.z).setColor(1F, 0, 0, 1F);
            vector.rotateY(45);
            consumer.addVertex(matrix, vector.x, 0F, vector.z).setColor(1F, 0, 0, 1F);
            consumer.addVertex(matrix, vector.x, 250F, vector.z).setColor(1F, 0, 0, 1F);
        }

        for(int i = 0; i < 8; i++) {
            consumer.addVertex(matrix, vector.x / 2, 250F, vector.z / 2).setColor(1F, 1F, 1F, 1F);
            consumer.addVertex(matrix, vector.x / 2, 0F, vector.z / 2).setColor(1F, 1F, 1F, 1F);
            vector.rotateY(45);
            consumer.addVertex(matrix, vector.x / 2, 0F, vector.z / 2).setColor(1F, 1F, 1F, 1F);
            consumer.addVertex(matrix, vector.x / 2, 250F, vector.z / 2).setColor(1F, 1F, 1F, 1F);
        }
    }

    @Override public ResourceLocation getTextureLocation(OrbitalLaser laser) { return ResourceManager.EMPTY; }
}
