package com.hbm.render.material;

import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.S_Face;
import com.hbm.render.loader.S_GroupObject;
import com.hbm.render.loader.old.TextureCoordinate;
import com.hbm.render.loader.old.Vertex;
import com.hbm.render.util.NtmShaders.NtmVertexFormat;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.client.renderer.ShaderInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HFRWavefrontObjectTEST {

    public final Map<String, VertexBuffer> buffers = new HashMap<>();

    public HFRWavefrontObjectTEST(HFRWavefrontObject obj) {
        this.load(obj);
    }

    public void load(HFRWavefrontObject obj) {
        for(S_GroupObject g : obj.groupObjects) {
            BufferBuilder builder = Tesselator.getInstance().begin(g.mode, NtmVertexFormat.POSITION_TEX_NORMAL);

            for(S_Face face : g.faces) {
                for(int i = 0; i < face.vertices.length; i++) {
                    Vertex vert = face.vertices[i];
                    TextureCoordinate tex = face.textureCoordinates != null && face.textureCoordinates.length > 0
                            ? face.textureCoordinates[i]
                            : new TextureCoordinate(0, 0);
                    Vertex normal = face.vertexNormals[i];

                    builder.addVertex(vert.x, vert.y, vert.z).setUv(tex.u, tex.v).setNormal(normal.x, normal.y, normal.z);
                }
            }

            VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
            buffer.bind();
            buffer.upload(builder.buildOrThrow());
            VertexBuffer.unbind();

            buffers.put(g.name, buffer);
        }
    }

    public record ObjRendererAll(Material material, Map<String, VertexBuffer> buffers) {

        public void render(VertexBuffer data) {

            RenderContext context = RenderContext.INSTANCE.get();
            int packedLight = context.packedLight;
            int packedOverlay = context.packedOverlay;

            ShaderInstance shader = MaterialShaderCache.get(material);

            shader.safeGetUniform("UV1").set(packedOverlay & '\uffff', packedOverlay >> 16 & '\uffff');
            shader.safeGetUniform("UV2").set(packedLight & '\uffff', packedLight >> 16 & '\uffff');
            shader.safeGetUniform("Color").set(context.color);
            shader.safeGetUniform("PoseMat").set(context.poseStack.last().pose());

            MaterialRenderState.setup(material);

            data.bind();
            data.drawWithShader(RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix(), shader);
            VertexBuffer.unbind();

            MaterialRenderState.reset();
        }

        public void render() {
            buffers.values().forEach(this::render);
        }

        public List<String> getPartNames() {
            return new ArrayList<>(buffers.keySet());
        }
    }

    public record ObjRendererOnly(Material material, VertexBuffer data) {

        public void render() {

            RenderContext context = RenderContext.INSTANCE.get();
            int packedLight = context.packedLight;
            int packedOverlay = context.packedOverlay;

            ShaderInstance shader = MaterialShaderCache.get(material);

            shader.safeGetUniform("UV1").set(packedOverlay & '\uffff', packedOverlay >> 16 & '\uffff');
            shader.safeGetUniform("UV2").set(packedLight & '\uffff', packedLight >> 16 & '\uffff');
            shader.safeGetUniform("Color").set(context.color);
            shader.safeGetUniform("PoseMat").set(context.poseStack.last().pose());

            MaterialRenderState.setup(material);

            data.bind();
            data.drawWithShader(RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix(), shader);
            VertexBuffer.unbind();

            MaterialRenderState.reset();
        }
    }
}