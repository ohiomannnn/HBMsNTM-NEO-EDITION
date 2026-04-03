package com.hbm.render.util;

import com.hbm.main.NuclearTechMod;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@EventBusSubscriber(Dist.CLIENT)
public class NtmShaders {

    private static ShaderInstance aShader;
    public static ShaderInstance getAShader() { return aShader; }

    private static ShaderInstance aShaderNL;
    public static ShaderInstance getAShaderNL() { return aShaderNL; }

    @SubscribeEvent
    public static void register(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(), NuclearTechMod.withDefaultNamespace("a_shader"), NtmVertexFormat.POSITION_TEX_NORMAL),
                shader -> aShader = shader
        );

        event.registerShader(
                new ShaderInstance(event.getResourceProvider(), NuclearTechMod.withDefaultNamespace("a_shader_nl"), NtmVertexFormat.POSITION_TEX_NORMAL),
                shader -> aShaderNL = shader
        );
    }

    public static class NtmVertexFormat {
        public static final VertexFormat POSITION_TEX_NORMAL = VertexFormat.builder().add("Position", VertexFormatElement.POSITION).add("UV0", VertexFormatElement.UV0).add("Normal", VertexFormatElement.NORMAL).padding(1).build();
    }
}
