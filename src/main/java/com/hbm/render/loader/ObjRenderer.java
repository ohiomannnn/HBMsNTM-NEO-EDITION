package com.hbm.render.loader;

import com.hbm.render.material.Material;
import com.hbm.render.material.MaterialRenderState;
import com.hbm.render.material.MaterialShaderCache;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.client.renderer.ShaderInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ObjRenderer(Map<String, VertexBuffer> buffers) implements IObjRenderer {

    public void renderGroup(Material material, VertexBuffer data) {

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

    @Override
    public void renderAll(Material material) {
        for(Map.Entry<String, VertexBuffer> entry : buffers.entrySet()) {
            this.renderGroup(material, entry.getValue());
        }
    }

    @Override
    public void renderPart(Material material, String partName) {
        for(Map.Entry<String, VertexBuffer> entry : buffers.entrySet()) {
            if(entry.getKey().equalsIgnoreCase(partName)) {
                this.renderGroup(material, entry.getValue());
            }
        }
    }

    @Override
    public void renderOnly(Material material, String... groupNames) {
        for(Map.Entry<String, VertexBuffer> entry : buffers.entrySet()) {
            for(String name : groupNames) {
                if(entry.getKey().equalsIgnoreCase(name)) {
                    this.renderGroup(material, entry.getValue());
                }
            }
        }
    }

    @Override
    public void renderAllExcept(Material material, String... excludedGroupNames) {
        for(Map.Entry<String, VertexBuffer> entry : buffers.entrySet()) {
            boolean skip = false;
            for(String name : excludedGroupNames) {
                if(entry.getKey().equalsIgnoreCase(name)) {
                    skip = true;
                    break;
                }
            }
            if(!skip) renderGroup(material, entry.getValue());
        }
    }

    @Override
    public List<String> getPartNames() {
        List<String> names = new ArrayList<>();
        for(Map.Entry<String, VertexBuffer> entry : buffers.entrySet()) names.add(entry.getKey());
        return names;
    }
}
