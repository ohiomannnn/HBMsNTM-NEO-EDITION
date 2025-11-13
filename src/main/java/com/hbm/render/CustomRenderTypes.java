package com.hbm.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.BiFunction;

public class CustomRenderTypes extends RenderType {

    private CustomRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupTask, Runnable clearTask) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupTask, clearTask);
    }

    public static RenderType additive(ResourceLocation texture) {
        return create(
                "additive",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                true,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setTransparencyState(ADDITIVE_TRANSPARENCY)
                        .setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(true)
        );
    }

    public static final TransparencyStateShard LE_SMOTH = new TransparencyStateShard(
            "smoth", () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(770, 771, 1, 0);
                RenderSystem.depthMask(false);
                }, () -> {
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.depthMask(true);
            });

    private static final RenderStateShard.TransparencyStateShard ADDITIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard(
            "additive", () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                RenderSystem.depthMask(false);
                }, () -> {
                RenderSystem.depthMask(true);
                RenderSystem.disableBlend();
            });

    public static final BiFunction<ResourceLocation, Boolean, RenderType> SMOTH = Util.memoize(
            (texture, outline) -> {
                RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setTransparencyState(LE_SMOTH)
                        .setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(outline);
                return create("entity_smooth", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$compositestate);
            }
    );

    public static RenderType entitySmoth(ResourceLocation location, boolean outline) {
        return SMOTH.apply(location, outline);
    }
}