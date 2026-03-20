package com.hbm.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard;
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

    // Entity Cutout, No Crumbling
    public static final Function<ResourceLocation, RenderType> EC_NC = Util.memoize(
            texture -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setShaderState(RenderType.RENDERTYPE_ENTITY_CUTOUT_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderType.OVERLAY)
                        .createCompositeState(false);
                return RenderType.create("ec_nc", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 15346, false, false, state);
            }
    );

    // Entity Cutout, No Crumbling, No Cull
    public static final Function<ResourceLocation, RenderType> EC_NC_NC = Util.memoize(
            texture -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setShaderState(RenderType.RENDERTYPE_ENTITY_CUTOUT_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setCullState(RenderStateShard.NO_CULL)
                        .setOverlayState(RenderType.OVERLAY)
                        .createCompositeState(false);
                return RenderType.create("ec_nc_nc", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 15346, false, false, state);
            }
    );

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

    public static final ShaderStateShard POSITION_TEX_COLOR_SHADER = new ShaderStateShard(GameRenderer::getPositionTexColorShader);

    public static final Function<ResourceLocation, RenderType> GLINT_NT = Util.memoize(
            texture -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setShaderState(POSITION_TEX_COLOR_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setTransparencyState(ADDITIVE_BLEND)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.NO_LIGHTMAP)
                        .setOverlayState(RenderType.NO_OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setOutputState(RenderType.TRANSLUCENT_TARGET)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .createCompositeState(false);
                return RenderType.create("glint_nt", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 234256, false, false, state);
            }
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
                        .setOutputState(RenderType.TRANSLUCENT_TARGET)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .createCompositeState(false);
                return RenderType.create("additive", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, state);
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
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setTransparencyState(SEVEN_SEVEN10)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderType.NO_OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setOutputState(RenderType.CLOUDS_TARGET)
                        .createCompositeState(false);
                return RenderType.create("smoth", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, state);
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
                        .setOutputState(RenderType.TRANSLUCENT_TARGET)
                        .createCompositeState(false);
                return RenderType.create("smoth2", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 3123124, false, false, state);
            }
    );

    public static final Function<ResourceLocation, RenderType> CLOUD = Util.memoize(
            texture -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setShaderState(POSITION_TEX_COLOR_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.NO_LIGHTMAP)
                        .setOverlayState(RenderType.NO_OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setOutputState(RenderType.TRANSLUCENT_TARGET)
                        .createCompositeState(false);
                return RenderType.create("cloud", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 234256, false, false, state);
            }
    );

    public static final RenderType CLOUD_RAINBOW = RenderType.create(
            "cloud_rainbow",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            234256, false, false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.POSITION_COLOR_SHADER)
                    .setCullState(RenderType.NO_CULL)
                    .setLightmapState(RenderType.NO_LIGHTMAP)
                    .setOverlayState(RenderType.NO_OVERLAY)
                    .setWriteMaskState(RenderType.COLOR_WRITE)
                    .setOutputState(RenderType.TRANSLUCENT_TARGET)
                    .createCompositeState(false)
    );

    public static final RenderType CLOUD_RAINBOW_ADDITIVE = RenderType.create(
            "cloud_rainbow_addtive",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            234256, false, false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.POSITION_COLOR_SHADER)
                    .setTransparencyState(ADDITIVE_BLEND)
                    .setCullState(RenderType.NO_CULL)
                    .setLightmapState(RenderType.NO_LIGHTMAP)
                    .setOverlayState(RenderType.NO_OVERLAY)
                    .setWriteMaskState(RenderType.COLOR_WRITE)
                    .setOutputState(RenderType.TRANSLUCENT_TARGET)
                    .createCompositeState(false)
    );

    public static final Function<ResourceLocation, RenderType> NUKE_CLOUDS = Util.memoize(
            texture -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setTransparencyState(SEVEN_SEVEN10)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderType.NO_OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setOutputState(RenderType.TRANSLUCENT_TARGET)
                        .createCompositeState(false);
                return RenderType.create("nuke", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 239120, true, true, state);
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
                        .setOverlayState(RenderType.NO_OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setOutputState(RenderType.TRANSLUCENT_TARGET)
                        .createCompositeState(false);
                return RenderType.create("nuke_flash", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 545234, true, true, state);
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