package com.hbm.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.RenderStateShard.TransparencyStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class CustomRenderTypes {

    public static final TransparencyStateShard SEVEN_SEVEN10 = new TransparencyStateShard(
            "7710",
            () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(770, 771, 1, 0);
            },
            () -> {
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableBlend();
            });

    private static final TransparencyStateShard ADDITIVE_BLEND = new TransparencyStateShard(
            "additive",
            () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            },
            () -> {
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableBlend();
            });

    public static final RenderType GLOW = RenderType.create(
            "glow",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256,
            false,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                    .setTransparencyState(ADDITIVE_BLEND)
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .createCompositeState(false)
    );

    public static final Function<ResourceLocation, RenderType> ADDITIVE = Util.memoize(
            texture -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setTransparencyState(ADDITIVE_BLEND)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderType.NO_OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setDepthTestState(RenderType.LEQUAL_DEPTH_TEST)
                        .createCompositeState(false);
                return RenderType.create("additive", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, state);
            }
    );

    public static final Function<ResourceLocation, RenderType> SMOTH = Util.memoize(
            texture -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, true))
                        .setTransparencyState(SEVEN_SEVEN10)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderType.OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_DEPTH_WRITE)
                        .setOutputState(RenderType.CLOUDS_TARGET)
                        .createCompositeState(false);
                return RenderType.create("smoth", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, state);
            }
    );

    public static final Function<ResourceLocation, RenderType> SMOTH_NO_DEPTH = Util.memoize(
            texture -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, true))
                        .setTransparencyState(SEVEN_SEVEN10)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderType.NO_OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setOutputState(RenderType.CLOUDS_TARGET)
                        .createCompositeState(false);
                return RenderType.create("smoth", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, state);
            }
    );

    public static final Function<ResourceLocation, RenderType> SMOTH_NO_LIGHT = Util.memoize(
            texture -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setShaderState(RenderType.POSITION_COLOR_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setTransparencyState(SEVEN_SEVEN10)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.NO_LIGHTMAP)
                        .setOverlayState(RenderType.NO_OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setOutputState(RenderType.CLOUDS_TARGET)
                        .createCompositeState(false);
                return RenderType.create("smoth2", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, true, true, state);
            }
    );

    public static RenderType entitySmoth(ResourceLocation location) {
        return SMOTH.apply(location);
    }

    public static RenderType entitySmothNoLight(ResourceLocation location) {
        return SMOTH_NO_LIGHT.apply(location);
    }

    public static RenderType entitySmothNoDepth(ResourceLocation location) {
        return SMOTH_NO_DEPTH.apply(location);
    }

    public static RenderType entityAdditive(ResourceLocation location) {
        return ADDITIVE.apply(location);
    }
}