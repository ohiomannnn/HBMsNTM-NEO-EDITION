package com.hbm.render.entity.effect;


import com.hbm.HBMsNTM;
import com.hbm.entity.effect.BlackHole;
import com.hbm.entity.effect.RagingVortex;
import com.hbm.entity.effect.Vortex;
import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Random;

public class RenderBlackHole<T extends Entity> extends EntityRenderer<T> {

    protected static final ResourceLocation HOLE = HBMsNTM.withDefaultNamespaceNT("textures/models/blackhole.png");
    protected static final ResourceLocation SWIRL = HBMsNTM.withDefaultNamespaceNT("textures/entity/bhole.png");
    protected static final ResourceLocation DISC = HBMsNTM.withDefaultNamespaceNT("textures/entity/bholedisc.png");

    public RenderBlackHole(EntityRendererProvider.Context context) { super(context); }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        float size = getEntitySize(entity);

        poseStack.scale(size, size, size);

        Matrix4f matrix = poseStack.last().pose();

        renderSphere(matrix, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);

        if (entity instanceof Vortex) {
            renderSwirl(entity, partialTick, poseStack);
        } else if (entity instanceof RagingVortex) {
            renderSwirl(entity, partialTick, poseStack);
            renderJets(entity, poseStack);
        } else {
            renderDisc(entity, partialTick, poseStack);
            renderJets(entity, poseStack);
        }

        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    protected float getEntitySize(T entity) {
        if (entity instanceof BlackHole blackHole) {
            return blackHole.getSize();
        }
        return 1.0f;
    }

    private static final int STACKS = 16;
    private static final int SLICES = 16;

    protected void renderSphere(Matrix4f matrix4f, float radius, float r, float g, float b, float a) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

        int colorR = (int) (r * 255);
        int colorG = (int) (g * 255);
        int colorB = (int) (b * 255);
        int colorA = (int) (a * 255);

        for (int i = 0; i < STACKS; i++) {
            double phi1 = Math.PI * i / STACKS;
            double phi2 = Math.PI * (i + 1) / STACKS;

            for (int j = 0; j < SLICES; j++) {
                double theta1 = 2.0 * Math.PI * j / SLICES;
                double theta2 = 2.0 * Math.PI * (j + 1) / SLICES;

                float x1 = (float) (radius * Math.sin(phi1) * Math.cos(theta1));
                float y1 = (float) (radius * Math.cos(phi1));
                float z1 = (float) (radius * Math.sin(phi1) * Math.sin(theta1));

                float x2 = (float) (radius * Math.sin(phi2) * Math.cos(theta1));
                float y2 = (float) (radius * Math.cos(phi2));
                float z2 = (float) (radius * Math.sin(phi2) * Math.sin(theta1));

                float x3 = (float) (radius * Math.sin(phi2) * Math.cos(theta2));
                float y3 = (float) (radius * Math.cos(phi2));
                float z3 = (float) (radius * Math.sin(phi2) * Math.sin(theta2));

                float x4 = (float) (radius * Math.sin(phi1) * Math.cos(theta2));
                float y4 = (float) (radius * Math.cos(phi1));
                float z4 = (float) (radius * Math.sin(phi1) * Math.sin(theta2));

                buffer.addVertex(matrix4f, x1, y1, z1).setColor(colorR, colorG, colorB, colorA);
                buffer.addVertex(matrix4f, x2, y2, z2).setColor(colorR, colorG, colorB, colorA);
                buffer.addVertex(matrix4f, x3, y3, z3).setColor(colorR, colorG, colorB, colorA);

                buffer.addVertex(matrix4f, x1, y1, z1).setColor(colorR, colorG, colorB, colorA);
                buffer.addVertex(matrix4f, x3, y3, z3).setColor(colorR, colorG, colorB, colorA);
                buffer.addVertex(matrix4f, x4, y4, z4).setColor(colorR, colorG, colorB, colorA);
            }
        }

        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    protected ResourceLocation discTex() {
        return DISC;
    }

    protected void renderDisc(Entity entity, float interp, PoseStack poseStack) {
        float glow = 0.75F;

        RenderSystem.setShaderTexture(0, discTex());

        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getId() % 90 - 45));
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getId() % 360));

        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        int count = 16;
        Vec3NT vec = new Vec3NT(1, 0, 0);

        for (int k = 0; k < steps(); k++) {
            poseStack.pushPose();

            float rotation = (entity.tickCount + interp % 360) * -((float) Math.pow(k + 1, 1.25));
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

            Matrix4f matrix = poseStack.last().pose();
            double s = 3 - k * 0.175D;

            for (int j = 0; j < 2; j++) {
                RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

                for (int i = 0; i < count; i++) {
                    int[] color1 = (j == 0) ? getColorFromIteration(k, 1F) : new int[]{255, 255, 255, (int)(glow * 255)};
                    int[] color2 = getColorFromIteration(k, 0F);

                    buffer.addVertex(matrix, (float)(vec.xCoord * s), 0, (float)(vec.zCoord * s))
                            .setUv((float)(0.5 + vec.xCoord * 0.25), (float)(0.5 + vec.zCoord * 0.25))
                            .setColor(color1[0], color1[1], color1[2], color1[3]);

                    buffer.addVertex(matrix, (float)(vec.xCoord * s * 2), 0, (float)(vec.zCoord * s * 2))
                            .setUv((float)(0.5 + vec.xCoord * 0.5), (float)(0.5 + vec.zCoord * 0.5))
                            .setColor(color2[0], color2[1], color2[2], color2[3]);

                    vec.rotateAroundYRad((float) (Math.PI * 2 / count));

                    buffer.addVertex(matrix, (float)(vec.xCoord * s * 2), 0, (float)(vec.zCoord * s * 2))
                            .setUv((float)(0.5 + vec.xCoord * 0.5), (float)(0.5 + vec.zCoord * 0.5))
                            .setColor(color2[0], color2[1], color2[2], color2[3]);

                    int[] color3 = (j == 0) ? getColorFromIteration(k, 1F) : new int[]{255, 255, 255, (int)(glow * 255)};
                    buffer.addVertex(matrix, (float)(vec.xCoord * s), 0, (float)(vec.zCoord * s))
                            .setUv((float)(0.5 + vec.xCoord * 0.25), (float)(0.5 + vec.zCoord * 0.25))
                            .setColor(color3[0], color3[1], color3[2], color3[3]);
                }

                BufferUploader.drawWithShader(buffer.buildOrThrow());

                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            }

            poseStack.popPose();
        }

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        poseStack.popPose();
    }

    protected int steps() {
        return 15;
    }

    protected int[] getColorFromIteration(int iteration, float alpha) {
        int a = (int) (alpha * 255);

        if (iteration < 5) {
            float g = 0.125F + iteration * (1F / 10F);
            return new int[]{255, (int)(g * 255), 0, a};
        }

        if (iteration == 5) {
            return new int[]{255, 255, 0, a};
        }

        if (iteration > 5) {
            int i = iteration - 6;
            float r = 1.0F - i * (1F / 9F);
            float g = 1F - i * (1F / 9F);
            float b = i * (1F / 5F);
            return new int[]{(int)(r * 255), (int)(g * 255), (int)(b * 255), a};
        }
        return null;
    }

    protected void renderSwirl(Entity entity, float interp, PoseStack poseStack) {
        float glow = 0.75F;

        if (entity instanceof RagingVortex) {
            glow = 0.25F;
        }

        RenderSystem.setShaderTexture(0, SWIRL);

        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getId() % 90 - 45));
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getId() % 360));
        poseStack.mulPose(Axis.YP.rotationDegrees((entity.tickCount + interp % 360) * -5));

        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        Matrix4f matrix = poseStack.last().pose();
        Vec3NT vec = new Vec3NT(1, 0, 0);

        double s = 3;
        int count = 16;

        int[] colorFull = getColorFull(entity);
        int[] colorNone = getColorNone(entity);

        //swirl, inner part (solid)
        for (int j = 0; j < 2; j++) {
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

            for (int i = 0; i < count; i++) {
                buffer.addVertex(matrix, (float) (vec.xCoord * 0.9f), 0, (float) (vec.zCoord * 0.9f))
                        .setUv((float)(0.5 + vec.xCoord * 0.25 / s * 0.9), (float)(0.5 + vec.zCoord * 0.25 / s * 0.9))
                        .setColor(0, 0, 0, 255);

                int[] color1 = (j == 0) ? colorFull : new int[]{255, 255, 255, (int)(glow * 255)};
                buffer.addVertex(matrix, (float)(vec.xCoord * s), 0, (float)(vec.zCoord * s))
                        .setUv((float)(0.5 + vec.xCoord * 0.25), (float)(0.5 + vec.zCoord * 0.25))
                        .setColor(color1[0], color1[1], color1[2], color1[3]);

                vec.rotateAroundYRad(Math.PI * 2 / count);

                int[] color2 = (j == 0) ? colorFull : new int[]{255, 255, 255, (int)(glow * 255)};
                buffer.addVertex(matrix, (float)(vec.xCoord * s), 0, (float)(vec.zCoord * s))
                        .setUv((float)(0.5 + vec.xCoord * 0.25), (float)(0.5 + vec.zCoord * 0.25))
                        .setColor(color2[0], color2[1], color2[2], color2[3]);

                buffer.addVertex(matrix, (float) (vec.xCoord * 0.9f), 0, (float) (vec.zCoord * 0.9f))
                        .setUv((float)(0.5 + vec.xCoord * 0.25 / s * 0.9), (float)(0.5 + vec.zCoord * 0.25 / s * 0.9))
                        .setColor(0, 0, 0, 255);
            }

            BufferUploader.drawWithShader(buffer.buildOrThrow());

            RenderSystem.blendFunc(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE
            );
        }

        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        //swirl, outer part (fade)
        for (int j = 0; j < 2; j++) {
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

            vec.setComponents(1, 0, 0);

            for (int i = 0; i < count; i++) {
                int[] color1 = (j == 0) ? colorFull : new int[]{255, 255, 255, (int)(glow * 255)};
                buffer.addVertex(matrix, (float)(vec.xCoord * s), 0, (float)(vec.zCoord * s))
                        .setUv((float)(0.5 + vec.xCoord * 0.25), (float)(0.5 + vec.zCoord * 0.25))
                        .setColor(color1[0], color1[1], color1[2], color1[3]);

                buffer.addVertex(matrix, (float)(vec.xCoord * s * 2), 0, (float)(vec.zCoord * s * 2))
                        .setUv((float)(0.5 + vec.xCoord * 0.5), (float)(0.5 + vec.zCoord * 0.5))
                        .setColor(colorNone[0], colorNone[1], colorNone[2], colorNone[3]);

                vec.rotateAroundYRad(Math.PI * 2 / count);

                buffer.addVertex(matrix, (float)(vec.xCoord * s * 2), 0, (float)(vec.zCoord * s * 2))
                        .setUv((float)(0.5 + vec.xCoord * 0.5), (float)(0.5 + vec.zCoord * 0.5))
                        .setColor(colorNone[0], colorNone[1], colorNone[2], colorNone[3]);

                int[] color2 = (j == 0) ? colorFull : new int[]{255, 255, 255, (int)(glow * 255)};
                buffer.addVertex(matrix, (float)(vec.xCoord * s), 0, (float)(vec.zCoord * s))
                        .setUv((float)(0.5 + vec.xCoord * 0.25), (float)(0.5 + vec.zCoord * 0.25))
                        .setColor(color2[0], color2[1], color2[2], color2[3]);
            }

            BufferUploader.drawWithShader(buffer.buildOrThrow());

            RenderSystem.blendFunc(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE
            );
        }

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        poseStack.popPose();
    }

    protected void renderJets(Entity entity, PoseStack poseStack) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getId() % 90 - 45));
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getId() % 360));

        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

        Matrix4f matrix = poseStack.last().pose();

        for (int j = -1; j <= 1; j += 2) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);

            buffer.addVertex(matrix, 0, 0, 0).setColor(255, 255, 255, (int)(0.35f * 255));

            Vec3NT jet = new Vec3NT(0.5, 0, 0);

            for (int i = 0; i <= 12; i++) {
                buffer.addVertex(matrix, (float) jet.xCoord, (float) (10 * j), (float) jet.zCoord).setColor(255, 255, 255, 0);
                jet.rotateAroundYRad(Math.PI / 6 * -j);
            }

            BufferUploader.drawWithShader(buffer.buildOrThrow());
        }

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        poseStack.popPose();
    }

    // fuck
    protected int[] getColorFull(Entity entity) {
        if (entity instanceof Vortex) {
            return new int[]{0x38, 0x98, 0xb3, 255}; // 0x3898b3
        } else if (entity instanceof RagingVortex) {
            return new int[]{0xe8, 0x39, 0x0d, 255}; // 0xe8390d
        } else {
            return new int[]{0xFF, 0xB9, 0x00, 255}; // 0xFFB900
        }
    }

    protected int[] getColorNone(Entity entity) {
        if (entity instanceof Vortex) {
            return new int[]{0x38, 0x98, 0xb3, 0};
        } else if (entity instanceof RagingVortex) {
            return new int[]{0xe8, 0x39, 0x0d, 0};
        } else {
            return new int[]{0xFF, 0xB9, 0x00, 0};
        }
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return HOLE;
    }
}