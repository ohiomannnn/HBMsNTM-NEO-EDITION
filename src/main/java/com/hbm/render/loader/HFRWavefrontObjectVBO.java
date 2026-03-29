package com.hbm.render.loader;

import com.hbm.render.util.NtmShaders.NtmVertexFormat;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import org.joml.Matrix4f;

import java.util.*;

/**
 * YES FINALLY
 */
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
    private final Map<String, GroupVBO> groupMap = new HashMap<>();

    public HFRWavefrontObjectVBO(HFRWavefrontObject obj) {
        for (S_GroupObject g : obj.groupObjects) {
            VertexBuffer buffer = buildGroupBuffer(g);

            GroupVBO cachedGroup = new GroupVBO(g.name, buffer);
            groups.add(cachedGroup);
            groupMap.put(g.name.toLowerCase(), cachedGroup);
        }
    }

    private VertexBuffer buildGroupBuffer(S_GroupObject g) {
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder builder = tess.begin(VertexFormat.Mode.QUADS, NtmVertexFormat.POSITION_TEX_NORMAL);

        for (S_Face face : g.faces) {
            if (face.vertices == null) continue;

            Vertex faceNormal = face.faceNormal != null ? face.faceNormal : new Vertex(0, 1, 0);
            int vertexCount = face.vertices.length;

            int[] indices = vertexCount == 3 ? new int[]{0, 1, 2, 2} : new int[]{0, 1, 2, 3};

            for (int idx : indices) {
                int i = Math.min(idx, vertexCount - 1);
                Vertex v = face.vertices[i];

                float u = 0, vCoord = 0;
                if (face.textureCoordinates != null && i < face.textureCoordinates.length) {
                    u = face.textureCoordinates[i].u;
                    vCoord = face.textureCoordinates[i].v;
                }

                Vertex normal = faceNormal;
                if (face.vertexNormals != null && i < face.vertexNormals.length) {
                    normal = face.vertexNormals[i];
                }

                builder.addVertex(v.x, v.y, v.z)
                        .setUv(u, vCoord)
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

    private void renderGroup(GroupVBO groupVBO) {

        RenderType type = RenderContext.renderType();
        PoseStack poseStack = RenderContext.poseStack();

        int packedLight = RenderContext.light();
        int packedOverlay = RenderContext.overlay();
        float r = RenderContext.r();
        float g = RenderContext.g();
        float b = RenderContext.b();
        float a = RenderContext.a();

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
        for (GroupVBO group : groups) {
            renderGroup(group);
        }
    }

    public void renderPart(String partName) {
        GroupVBO group = groupMap.get(partName.toLowerCase());
        if (group != null) {
            renderGroup(group);
        }
    }

    @Override
    public void renderOnly(String... groupNames) {
        Set<String> names = new HashSet<>();
        for (String name : groupNames) {
            names.add(name.toLowerCase());
        }

        for (GroupVBO group : groups) {
            if (names.contains(group.name.toLowerCase())) {
                renderGroup(group);
            }
        }
    }

    @Override
    public void renderAllExcept(String... excludedGroupNames) {
        Set<String> excluded = new HashSet<>();
        for (String name : excludedGroupNames) {
            excluded.add(name.toLowerCase());
        }

        for (GroupVBO group : groups) {
            if (!excluded.contains(group.name.toLowerCase())) {
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