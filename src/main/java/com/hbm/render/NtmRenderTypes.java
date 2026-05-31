package com.hbm.render;

import com.hbm.render.util.NtmShaders;
import com.hbm.render.util.NtmShaders.NtmVertexFormat;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.RenderStateShard.TransparencyStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderType.CompositeState;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class NtmRenderTypes {

    public static final TransparencyStateShard SEVEN_SEVEN_ONE_ZERO = new TransparencyStateShard(
            "7710",
            () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
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

    // original NO_TRANSPARENCY disabled blending... why mojang???
    public static final TransparencyStateShard NO_TRANSPARENCY = new TransparencyStateShard("no_transparency", () -> {}, () -> {});

    public static final ShaderStateShard VBO_SHADER = new ShaderStateShard(NtmShaders::getVboShader);
    public static final RenderType VBO = RenderType.create("vbo", NtmVertexFormat.POSITION_TEX_NORMAL, Mode.QUADS, 1024,
            CompositeState.builder()
                    .setShaderState(VBO_SHADER)
                    .setTransparencyState(NO_TRANSPARENCY)
                    .setLightmapState(RenderType.LIGHTMAP)
                    .setOverlayState(RenderType.OVERLAY)
                    .createCompositeState(false)
    );

    public static final RenderType GLOW = RenderType.create(
            "glow",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            6543,
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
                        .setOverlayState(RenderType.OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
                        .createCompositeState(false);
                return RenderType.create("additive", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, state);
            }
    );

    public static final Function<ResourceLocation, RenderType> SMOTH = Util.memoize(
            texture -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, true))
                        .setTransparencyState(SEVEN_SEVEN_ONE_ZERO)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderType.OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_DEPTH_WRITE)
                        .setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
                        .createCompositeState(false);
                return RenderType.create("smoth", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, state);
            }
    );

    public static final Function<ResourceLocation, RenderType> SMOTH_NO_DEPTH = Util.memoize(
            texture -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setTransparencyState(SEVEN_SEVEN_ONE_ZERO)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderType.OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
                        .createCompositeState(false);
                return RenderType.create("smoth", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, state);
            }
    );

    public static final Function<ResourceLocation, RenderType> NUKE_CLOUDS = Util.memoize(
            texture -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setTransparencyState(SEVEN_SEVEN_ONE_ZERO)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderType.OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
                        .createCompositeState(false);
                return RenderType.create("nuke", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 23912, true, true, state);
            }
    );

    public static final Function<ResourceLocation, RenderType> NUKE_FLASH = Util.memoize(
            texture -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setTransparencyState(ADDITIVE_BLEND)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderType.OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
                        .createCompositeState(false);
                return RenderType.create("nuke_flash", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 2342, true, true, state);
            }
    );

    public static RenderType entitySmoth(ResourceLocation location) {
        return SMOTH.apply(location);
    }

    public static RenderType entitySmothNoDepth(ResourceLocation location) {
        return SMOTH_NO_DEPTH.apply(location);
    }

    public static RenderType entityAdditive(ResourceLocation location) {
        return ADDITIVE.apply(location);
    }
}