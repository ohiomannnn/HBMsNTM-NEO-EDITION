package com.hbm.render.test;

import com.hbm.main.NuclearTechMod;
import com.hbm.render.util.NtmShaders.NtmVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MaterialShaderCache {

    private static final Map<Material, ShaderInstance> CACHE = new ConcurrentHashMap<>();

    public static ShaderInstance get(Material material) {
        return CACHE.computeIfAbsent(material, MaterialShaderCache::compile);
    }

    private static ShaderInstance compile(Material material) {
        String vsh = MaterialShaderSource.vertex(material);
        String fsh = MaterialShaderSource.fragment(material);
        String json = buildJson();

        GeneratedShaderResources resources = new GeneratedShaderResources(Minecraft.getInstance().getResourceManager());
        resources.put(NuclearTechMod.withDefaultNamespace("shaders/core/generated.json"), json);
        resources.put(NuclearTechMod.withDefaultNamespace("shaders/core/generated.vsh"), vsh);
        resources.put(NuclearTechMod.withDefaultNamespace("shaders/core/generated.fsh"), fsh);

        try {
            return new ShaderInstance(resources, NuclearTechMod.withDefaultNamespace("generated"), NtmVertexFormat.POSITION_TEX_NORMAL);
        } catch(IOException e) {
            throw new IllegalStateException("Shader compile failed " + material, e);
        }
    }

    private static String buildJson() {
        return """
            {
              "vertex": "hbmsntm:generated",
              "fragment": "hbmsntm:generated",
              "samplers": [
                { "name": "Sampler0" },
                { "name": "Sampler1" },
                { "name": "Sampler2" }
              ],
              "uniforms": [
                { "name": "ModelViewMat", "type": "matrix4x4", "count": 16, "values": [1.0,0.0,0.0,0.0, 0.0,1.0,0.0,0.0, 0.0,0.0,1.0,0.0, 0.0,0.0,0.0,1.0] },
                { "name": "ProjMat", "type": "matrix4x4", "count": 16, "values": [1.0,0.0,0.0,0.0, 0.0,1.0,0.0,0.0, 0.0,0.0,1.0,0.0, 0.0,0.0,0.0,1.0] },
                { "name": "PoseMat", "type": "matrix4x4", "count": 16, "values": [1.0,0.0,0.0,0.0, 0.0,1.0,0.0,0.0, 0.0,0.0,1.0,0.0, 0.0,0.0,0.0,1.0] },
                { "name": "TextureMat", "type": "matrix4x4", "count": 16, "values": [1.0,0.0,0.0,0.0, 0.0,1.0,0.0,0.0, 0.0,0.0,1.0,0.0, 0.0,0.0,0.0,1.0] },
                { "name": "Color", "type": "float", "count": 4, "values": [ 1.0, 1.0, 1.0, 1.0 ] },
                { "name": "UV1", "type": "int", "count": 2, "values": [0, 10] },
                { "name": "UV2", "type": "int", "count": 2, "values": [240, 240] },
                { "name": "Light0_Direction", "type": "float", "count": 3, "values": [0.0, 0.0, 0.0] },
                { "name": "Light1_Direction", "type": "float", "count": 3, "values": [0.0, 0.0, 0.0] },
                { "name": "FogStart", "type": "float", "count": 1, "values": [0.0] },
                { "name": "FogEnd", "type": "float", "count": 1, "values": [1.0] },
                { "name": "FogColor", "type": "float", "count": 4, "values": [0.0,0.0,0.0,0.0] },
                { "name": "FogShape", "type": "int", "count": 1, "values": [0] }
              ]
            }
            """;
    }
}