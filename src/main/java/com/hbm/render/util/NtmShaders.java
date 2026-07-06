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

    private static ShaderInstance vboShader;
    public static ShaderInstance getVboShader() { return vboShader; }

    @SubscribeEvent
    public static void register(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(), NuclearTechMod.withDefaultNamespace("vbo_shader"), NtmVertexFormat.POSITION_TEX_NORMAL),
                shader -> vboShader = shader
        );
    }

    public static class NtmVertexFormat {
        public static final VertexFormat POSITION_TEX_NORMAL = VertexFormat.builder().add("Position", VertexFormatElement.POSITION).add("UV0", VertexFormatElement.UV0).add("Normal", VertexFormatElement.NORMAL).padding(1).build();
    }
}
