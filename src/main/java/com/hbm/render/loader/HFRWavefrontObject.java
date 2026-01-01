package com.hbm.render.loader;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.joml.Matrix4f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class HFRWavefrontObject implements IModelCustom {

    public final List<Vertex> vertices = new ArrayList<>();
    public final List<Vertex> vertexNormals = new ArrayList<>();
    public final List<TextureCoordinate> textureCoordinates = new ArrayList<>();
    public final Map<String, ObjGroupObject> groupObjectsMap = new LinkedHashMap<>();
    public final List<ObjGroupObject> groupObjects = new ArrayList<>();

    private ObjGroupObject currentGroupObject;
    private final String fileName;
    private final boolean smoothing;

    public HFRWavefrontObject(ResourceLocation resource) {
        this(resource, true);
    }

    public HFRWavefrontObject(ResourceLocation resource, boolean smoothing) {
        this.fileName = resource.toString();
        this.smoothing = smoothing;

        try {
            Optional<Resource> resourceOpt = Minecraft.getInstance()
                    .getResourceManager()
                    .getResource(resource);

            if (resourceOpt.isEmpty()) {
                throw new RuntimeException("Could not find OBJ model: " + resource);
            }

            try (InputStream stream = resourceOpt.get().open()) {
                loadObjModel(stream);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load OBJ model: " + resource, e);
        }
    }

    public HFRWavefrontObject(InputStream inputStream, String fileName, boolean smoothing) {
        this.fileName = fileName;
        this.smoothing = smoothing;
        loadObjModel(inputStream);
    }

    private void loadObjModel(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                currentLine = currentLine.replaceAll("\\s+", " ").trim();

                if (currentLine.startsWith("#") || currentLine.isEmpty()) {
                    continue;
                } else if (currentLine.startsWith("v ")) {
                    Vertex vertex = parseVertex(currentLine);
                    if (vertex != null) vertices.add(vertex);
                } else if (currentLine.startsWith("vn ")) {
                    Vertex vertex = parseVertexNormal(currentLine);
                    if (vertex != null) vertexNormals.add(vertex);
                } else if (currentLine.startsWith("vt ")) {
                    TextureCoordinate tc = parseTextureCoordinate(currentLine);
                    if (tc != null) textureCoordinates.add(tc);
                } else if (currentLine.startsWith("f ")) {
                    if (currentGroupObject == null) {
                        currentGroupObject = new ObjGroupObject("Default");
                    }
                    ObjFace face = parseFace(currentLine);
                    currentGroupObject.faces.add(face);
                } else if (currentLine.startsWith("g ") || currentLine.startsWith("o ")) {
                    String name = currentLine.substring(2).trim();
                    if (!name.isEmpty()) {
                        if (currentGroupObject != null && !currentGroupObject.faces.isEmpty()) {
                            groupObjects.add(currentGroupObject);
                            groupObjectsMap.put(currentGroupObject.name, currentGroupObject);
                        }
                        currentGroupObject = new ObjGroupObject(name);
                    }
                }
            }

            if (currentGroupObject != null && !currentGroupObject.faces.isEmpty()) {
                groupObjects.add(currentGroupObject);
                groupObjectsMap.put(currentGroupObject.name, currentGroupObject);
            }
        } catch (IOException e) {
            throw new RuntimeException("IO Exception reading model: " + fileName, e);
        }
    }

    @Override
    public void renderAll(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        renderAll(poseStack, buffer, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void renderAll(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        for (ObjGroupObject group : groupObjects) {
            renderGroup(group, poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        }
    }

    @Override
    public void renderPart(String partName, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        renderPart(partName, poseStack, buffer, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void renderPart(String partName, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        ObjGroupObject group = groupObjectsMap.get(partName);
        if (group != null) {
            renderGroup(group, poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        }
    }

    @Override
    public void renderOnly(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, String... groupNames) {
        Set<String> names = new HashSet<>(Arrays.asList(groupNames));
        for (ObjGroupObject group : groupObjects) {
            if (names.contains(group.name)) {
                renderGroup(group, poseStack, buffer, packedLight, packedOverlay, 1, 1, 1, 1);
            }
        }
    }

    @Override
    public void renderAllExcept(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, String... excludedGroupNames) {
        Set<String> excluded = new HashSet<>(Arrays.asList(excludedGroupNames));
        for (ObjGroupObject group : groupObjects) {
            if (!excluded.contains(group.name)) {
                renderGroup(group, poseStack, buffer, packedLight, packedOverlay, 1, 1, 1, 1);
            }
        }
    }

    private void renderGroup(ObjGroupObject group, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        Matrix4f matrix = poseStack.last().pose();
        PoseStack.Pose pose = poseStack.last();

        for (ObjFace face : group.faces) {
            renderFace(face, matrix, pose, buffer, packedLight, packedOverlay, r, g, b, a);
        }
    }

    private void renderFace(ObjFace face, Matrix4f matrix, PoseStack.Pose pose, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        if (face.vertices == null) return;

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
            if (smoothing && face.vertexNormals != null && i < face.vertexNormals.length) {
                normal = face.vertexNormals[i];
            }

            buffer.addVertex(matrix, v.x, v.y, v.z)
                    .setColor(r, g, b, a)
                    .setUv(u, vCoord)
                    .setOverlay(packedOverlay)
                    .setLight(packedLight)
                    .setNormal(pose, normal.x, normal.y, normal.z);
        }
    }


    private Vertex parseVertex(String line) {
        String[] tokens = line.substring(2).trim().split(" ");
        if (tokens.length >= 3) {
            return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
        }
        return null;
    }

    private Vertex parseVertexNormal(String line) {
        String[] tokens = line.substring(3).trim().split(" ");
        if (tokens.length >= 3) {
            return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
        }
        return null;
    }

    private TextureCoordinate parseTextureCoordinate(String line) {
        String[] tokens = line.substring(3).trim().split(" ");
        if (tokens.length >= 2) {
            return new TextureCoordinate(Float.parseFloat(tokens[0]), 1 - Float.parseFloat(tokens[1]));
        }
        return null;
    }

    private ObjFace parseFace(String line) {
        ObjFace face = new ObjFace();
        String[] tokens = line.substring(2).trim().split(" ");

        if (tokens[0].contains("//")) {
            // f v//vn
            face.vertices = new Vertex[tokens.length];
            face.vertexNormals = new Vertex[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                String[] parts = tokens[i].split("//");
                face.vertices[i] = vertices.get(Integer.parseInt(parts[0]) - 1);
                face.vertexNormals[i] = vertexNormals.get(Integer.parseInt(parts[1]) - 1);
            }
        } else if (tokens[0].contains("/")) {
            String[] testParts = tokens[0].split("/");
            if (testParts.length == 3) {
                // f v/vt/vn
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    String[] parts = tokens[i].split("/");
                    face.vertices[i] = vertices.get(Integer.parseInt(parts[0]) - 1);
                    face.textureCoordinates[i] = textureCoordinates.get(Integer.parseInt(parts[1]) - 1);
                    face.vertexNormals[i] = vertexNormals.get(Integer.parseInt(parts[2]) - 1);
                }
            } else {
                // f v/vt
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    String[] parts = tokens[i].split("/");
                    face.vertices[i] = vertices.get(Integer.parseInt(parts[0]) - 1);
                    face.textureCoordinates[i] = textureCoordinates.get(Integer.parseInt(parts[1]) - 1);
                }
            }
        } else {
            // f v
            face.vertices = new Vertex[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                face.vertices[i] = vertices.get(Integer.parseInt(tokens[i]) - 1);
            }
        }

        face.faceNormal = face.calculateFaceNormal();
        return face;
    }

    public WavefrontObjVBO asVBO() {
        return new WavefrontObjVBO(this);
    }

    public List<String> getPartNames() {
        return new ArrayList<>(groupObjectsMap.keySet());
    }
}