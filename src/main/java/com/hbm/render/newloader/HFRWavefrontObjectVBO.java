package com.hbm.render.newloader;

import com.hbm.render.loader.IModelCustomNamed;
import com.hbm.render.newloader.old.TextureCoordinate;
import com.hbm.render.newloader.old.Vertex;
import com.hbm.render.util.NtmShaders.NtmVertexFormat;
import com.hbm.render.util.RenderStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class HFRWavefrontObjectVBO implements IModelCustomNamed {

    private static class GroupVBO {
        final String name;
        final VertexBuffer vertexBuffer;

        GroupVBO(String name, VertexBuffer vertexBuffer) {
            this.name = name;
            this.vertexBuffer = vertexBuffer;
        }
    }

    private final List<GroupVBO> groups = new ArrayList<>();

    public HFRWavefrontObjectVBO(HFRWavefrontObject obj) {
        for (S_GroupObject g : obj.groupObjects) {
            VertexBuffer buffer = buildGroupBuffer(g);

            GroupVBO cachedGroup = new GroupVBO(g.name, buffer);
            groups.add(cachedGroup);
        }
    }

    private VertexBuffer buildGroupBuffer(S_GroupObject g) {
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder builder = tess.begin(VertexFormat.Mode.TRIANGLES, NtmVertexFormat.POSITION_TEX_NORMAL);

        for(S_Face face : g.faces) {
            for(int i = 0; i < face.vertices.length; i++) {
                Vertex vert = face.vertices[i];
                TextureCoordinate tex = new TextureCoordinate(0, 0);
                Vertex normal = face.vertexNormals[i];

                if(face.textureCoordinates != null && face.textureCoordinates.length > 0) {
                    tex = face.textureCoordinates[i];
                }

                builder.addVertex(vert.x, vert.y, vert.z)
                        .setUv(tex.u, tex.v)
                        .setNormal(normal.x, normal.y, normal.z);
            }
        }

        MeshData meshData = builder.buildOrThrow();

        VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        buffer.bind();
        buffer.upload(meshData);
        VertexBuffer.unbind();
        return buffer;
    }

    // truth be told, i have no fucking idea what i'm doing
    // i know the VBO sends data to the GPU to be saved there directly which is where the optimization comes from in the first place
    // so logically, if we want to get rid of this, we need to blow the data up
    // documentation on GL15 functions seems nonexistant so fuck it we ball i guess
    public void destroy() {
        for(GroupVBO data : groups) {
            data.vertexBuffer.close();
        }
        groups.clear();
    }

    private void renderGroup(GroupVBO groupVBO) {

        RenderType type = RenderStateManager.renderType();
        PoseStack poseStack = RenderStateManager.poseStack();

        int packedLight = RenderStateManager.light();
        int packedOverlay = RenderStateManager.overlay();
        float r = RenderStateManager.r();
        float g = RenderStateManager.g();
        float b = RenderStateManager.b();
        float a = RenderStateManager.a();

        type.setupRenderState();
        ShaderInstance shader = RenderSystem.getShader();
        if (shader == null) return;

        Matrix4f modelViewMatrix = RenderSystem.getModelViewMatrix().mul(poseStack.last().pose(), new Matrix4f());
        Matrix4f projectionMatrix = RenderSystem.getProjectionMatrix();
        Matrix4f normalMatrix = new Matrix4f(poseStack.last().pose());

        shader.safeGetUniform("UV1").set(packedOverlay & '\uffff', packedOverlay >> 16 & '\uffff');
        shader.safeGetUniform("UV2").set(packedLight & '\uffff', packedLight >> 16 & '\uffff');
        shader.safeGetUniform("Col").set(r, g, b, a);
        shader.safeGetUniform("NormalMat").set(normalMatrix);

        groupVBO.vertexBuffer.bind();
        groupVBO.vertexBuffer.drawWithShader(modelViewMatrix, projectionMatrix, shader);
        VertexBuffer.unbind();

        type.clearRenderState();
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
        for (GroupVBO data : groups) {
            names.add(data.name);
        }
        return names;
    }
}