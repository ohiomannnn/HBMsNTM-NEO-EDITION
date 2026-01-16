package com.hbm.render.loader;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;

import java.util.*;

// not actual gpu vbo, were just caching everything and whats all
public class WavefrontObjVBO implements IModelCustom {

    private static class VertexVBO {
        final float x, y, z;
        final float u, v;
        final float nx, ny, nz;

        VertexVBO(float x, float y, float z, float u, float v, float nx, float ny, float nz) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.u = u;
            this.v = v;
            this.nx = nx;
            this.ny = ny;
            this.nz = nz;
        }
    }

    private static class GroupVBO {
        String name;
        VertexVBO[] vertices;

        GroupVBO(String name, VertexVBO[] vertices) {
            this.name = name;
            this.vertices = vertices;
        }
    }

    private final List<GroupVBO> groups = new ArrayList<>();
    private final Map<String, GroupVBO> groupMap = new HashMap<>();

    public WavefrontObjVBO(HFRWavefrontObject object) {
        for (S_GroupObject group : object.groupObjects) {
            List<VertexVBO> vertexList = new ArrayList<>();

            for (S_Face face : group.faces) {
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

                    vertexList.add(new VertexVBO(v.x, v.y, v.z, u, vCoord, normal.x, normal.y, normal.z));
                }
            }

            GroupVBO cachedGroup = new GroupVBO(group.name, vertexList.toArray(new VertexVBO[0]));
            groups.add(cachedGroup);
            groupMap.put(group.name.toLowerCase(), cachedGroup);
        }
    }

    @Override
    public void renderAll(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        renderAll(poseStack, buffer, packedLight, packedOverlay, 1f, 1f, 1f, 1f);
    }

    public void renderAll(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        Matrix4f matrix = poseStack.last().pose();
        PoseStack.Pose pose = poseStack.last();

        for (GroupVBO group : groups) {
            renderCachedGroup(group, matrix, pose, buffer, packedLight, packedOverlay, r, g, b, a);
        }
    }
    @Override
    public void renderPart(String partName, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        renderPart(partName, poseStack, buffer, packedLight, packedOverlay, 1f, 1f, 1f, 1f);
    }

    public void renderPart(String partName, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        GroupVBO group = groupMap.get(partName.toLowerCase());
        if (group != null) {
            Matrix4f matrix = poseStack.last().pose();
            PoseStack.Pose pose = poseStack.last();
            renderCachedGroup(group, matrix, pose, buffer, packedLight, packedOverlay, r, g, b, a);
        }
    }

    @Override
    public void renderOnly(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, String... groupNames) {
        Matrix4f matrix = poseStack.last().pose();
        PoseStack.Pose pose = poseStack.last();

        Set<String> names = new HashSet<>();
        for (String name : groupNames) {
            names.add(name.toLowerCase());
        }

        for (GroupVBO group : groups) {
            if (names.contains(group.name.toLowerCase())) {
                renderCachedGroup(group, matrix, pose, buffer, packedLight, packedOverlay, 1, 1, 1, 1);
            }
        }
    }

    @Override
    public void renderAllExcept(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, String... excludedGroupNames) {
        Matrix4f matrix = poseStack.last().pose();
        PoseStack.Pose pose = poseStack.last();

        Set<String> excluded = new HashSet<>();
        for (String name : excludedGroupNames) {
            excluded.add(name.toLowerCase());
        }

        for (GroupVBO group : groups) {
            if (!excluded.contains(group.name.toLowerCase())) {
                renderCachedGroup(group, matrix, pose, buffer, packedLight, packedOverlay, 1, 1, 1, 1);
            }
        }
    }

    private void renderCachedGroup(GroupVBO group, Matrix4f matrix, PoseStack.Pose pose, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        for (VertexVBO cv : group.vertices) {
            buffer.addVertex(matrix, cv.x, cv.y, cv.z)
                    .setColor(r, g, b, a)
                    .setUv(cv.u, cv.v)
                    .setOverlay(packedOverlay)
                    .setLight(packedLight)
                    .setNormal(pose, cv.nx, cv.ny, cv.nz);
        }
    }

    public List<String> getPartNames() {
        List<String> names = new ArrayList<>();
        for (GroupVBO group : groups) {
            names.add(group.name);
        }
        return names;
    }
}