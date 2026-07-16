package com.hbm.render.test;

import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.IModelCustomNamed;
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
import java.util.List;

public class HFRWavefrontObjectTEST implements IModelCustomNamed {

    public static class GroupVBO {
        final String name;
        final VertexBuffer vertexBuffer;

        GroupVBO(String name, VertexBuffer vertexBuffer) {
            this.name = name;
            this.vertexBuffer = vertexBuffer;
        }
    }

    private final Material material;
    private final List<GroupVBO> groups = new ArrayList<>();

    public HFRWavefrontObjectTEST(HFRWavefrontObject obj, Material material) {
        this.material = material;
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

            GroupVBO cachedGroup = new GroupVBO(g.name, buffer);
            groups.add(cachedGroup);
        }
    }

    private void renderGroup(GroupVBO group) {

        RenderContext context = RenderContext.INSTANCE.get();
        int packedLight = context.packedLight;
        int packedOverlay = context.packedOverlay;

        ShaderInstance shader = MaterialShaderCache.get(material);

        shader.safeGetUniform("UV1").set(packedOverlay & '\uffff', packedOverlay >> 16 & '\uffff');
        shader.safeGetUniform("UV2").set(packedLight & '\uffff', packedLight >> 16 & '\uffff');
        shader.safeGetUniform("Color").set(context.color);
        shader.safeGetUniform("PoseMat").set(context.poseStack.last().pose());

        MaterialRenderState.setup(material);

        group.vertexBuffer.bind();
        group.vertexBuffer.drawWithShader(RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix(), shader);
        VertexBuffer.unbind();

        MaterialRenderState.reset();
    }

    @Override
    public void renderAll() {
        for(GroupVBO group : groups) {
            renderGroup(group);
        }
    }

    @Override
    public void renderOnly(String... groupNames) {
        for(GroupVBO group : groups) {
            for(String name : groupNames) {
                if(group.name.equalsIgnoreCase(name)) {
                    renderGroup(group);
                }
            }
        }
    }

    @Override
    public void renderPart(String partName) {
        for(GroupVBO group : groups) {
            if(group.name.equalsIgnoreCase(partName)) {
                renderGroup(group);
            }
        }
    }

    @Override
    public void renderAllExcept(String... excludedGroupNames) {
        for(GroupVBO group : groups) {
            boolean skip = false;
            for(String name : excludedGroupNames) {
                if(group.name.equalsIgnoreCase(name)) {
                    skip = true;
                    break;
                }
            }
            if(!skip) {
                renderGroup(group);
            }
        }
    }

    @Override
    public List<String> getPartNames() {
        List<String> names = new ArrayList<>();
        for(GroupVBO data : groups) {
            names.add(data.name);
        }
        return names;
    }
}