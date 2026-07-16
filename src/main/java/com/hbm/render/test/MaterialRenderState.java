package com.hbm.render.test;

import com.hbm.render.test.Material.DepthTest;
import com.hbm.render.test.Material.Transparency;
import com.hbm.render.test.Material.WriteMask;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.lwjgl.opengl.GL11;

public class MaterialRenderState {

    public static void setup(Material material) {
        bindLightAndOverlay();
        setupTexture(material);
        setupBackfaceCulling(material.backfaceCulling());
        setupDepthTest(material.depthTest());
        setupTransparency(material.transparency());
        setupWriteMask(material.writeMask());
    }

    private static void setupTexture(Material material) {
        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(material.texture());
        texture.setFilter(material.blur(), material.mipmap());
        int textureId = texture.getId();
        RenderSystem.setShaderTexture(0, textureId);
        RenderSystem.bindTexture(textureId);
    }

    private static void setupBackfaceCulling(boolean backfaceCulling) {
        if(backfaceCulling) {
            RenderSystem.enableCull();
        } else {
            RenderSystem.disableCull();
        }
    }

    private static void setupDepthTest(DepthTest depthTest) {
        switch(depthTest) {
            case OFF -> {
                RenderSystem.disableDepthTest();
            }
            case EQUAL -> {
                RenderSystem.enableDepthTest();
                RenderSystem.depthFunc(GL11.GL_EQUAL);
            }
            case LEQUAL -> {
                RenderSystem.enableDepthTest();
                RenderSystem.depthFunc(GL11.GL_LEQUAL);
            }
            case GREATER -> {
                RenderSystem.enableDepthTest();
                RenderSystem.depthFunc(GL11.GL_GREATER);
            }
            case ALWAYS -> {
                RenderSystem.enableDepthTest();
                RenderSystem.depthFunc(GL11.GL_ALWAYS);
            }
        }
    }

    private static void setupTransparency(Transparency transparency) {
        switch (transparency) {
            case OPAQUE -> {
                RenderSystem.disableBlend();
            }
            case ADDITIVE -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(SourceFactor.ONE, DestFactor.ONE);
            }
            case LIGHTNING -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
            }
            case GLINT -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(SourceFactor.SRC_COLOR, DestFactor.ONE, SourceFactor.ZERO, DestFactor.ONE);
            }
            case CRUMBLING -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(SourceFactor.DST_COLOR, DestFactor.SRC_COLOR, SourceFactor.ONE, DestFactor.ZERO);
            }
            case TRANSLUCENT -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ONE_MINUS_SRC_ALPHA);
            }
        }
    }

    private static void setupWriteMask(WriteMask mask) {
        RenderSystem.depthMask(mask.depth());
        boolean writeColor = mask.color();
        RenderSystem.colorMask(writeColor, writeColor, writeColor, writeColor);
    }

    public static void bindLightAndOverlay() {
        GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;

        gameRenderer.overlayTexture().setupOverlayColor();
        RenderSystem.bindTexture(RenderSystem.getShaderTexture(1));

        gameRenderer.lightTexture().turnOnLightLayer();
        RenderSystem.bindTexture(RenderSystem.getShaderTexture(2));
    }

    public static void reset() {
        resetTexture();
        resetBackfaceCulling();
        resetPolygonOffset();
        resetDepthTest();
        resetTransparency();
        resetWriteMask();
        resetLightAndOverlay();
    }

    private static void resetTexture() {
        RenderSystem.setShaderTexture(0, 0);
    }

    private static void resetBackfaceCulling() {
        RenderSystem.enableCull();
    }

    private static void resetPolygonOffset() {
        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();
    }

    private static void resetDepthTest() {
        RenderSystem.disableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
    }

    private static void resetTransparency() {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }

    private static void resetWriteMask() {
        RenderSystem.depthMask(true);
        RenderSystem.colorMask(true, true, true, true);
    }

    public static void resetLightAndOverlay() {
        GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;

        gameRenderer.overlayTexture().teardownOverlayColor();
        gameRenderer.lightTexture().turnOffLightLayer();
    }
}