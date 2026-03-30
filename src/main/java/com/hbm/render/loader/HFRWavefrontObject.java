package com.hbm.render.loader;

import com.hbm.render.newloader.old.TextureCoordinate;
import com.hbm.render.newloader.old.Vertex;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HFRWavefrontObject {

    public final List<Vertex> vertices = new ArrayList<>();
    public final List<Vertex> vertexNormals = new ArrayList<>();
    public final List<TextureCoordinate> textureCoordinates = new ArrayList<>();
    public final Map<String, S_GroupObject> groupObjectsMap = new LinkedHashMap<>();
    public final List<S_GroupObject> groupObjects = new ArrayList<>();

    private S_GroupObject currentGroupObject;
    private final String fileName;

    public HFRWavefrontObject(ResourceLocation resource) {
        this.fileName = resource.toString();

        try {
            Resource res = Minecraft.getInstance().getResourceManager().getResourceOrThrow(resource);

            try (InputStream stream = res.open()) {
                loadObjModel(stream);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load OBJ model: " + resource, e);
        }
    }

    public HFRWavefrontObject(InputStream inputStream, String fileName) {
        this.fileName = fileName;
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
                        currentGroupObject = new S_GroupObject("Default");
                    }
                    S_Face face = parseFace(currentLine);
                    currentGroupObject.faces.add(face);
                } else if (currentLine.startsWith("g ") || currentLine.startsWith("o ")) {
                    String name = currentLine.substring(2).trim();
                    if (!name.isEmpty()) {
                        if (currentGroupObject != null && !currentGroupObject.faces.isEmpty()) {
                            groupObjects.add(currentGroupObject);
                            groupObjectsMap.put(currentGroupObject.name, currentGroupObject);
                        }
                        currentGroupObject = new S_GroupObject(name);
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

    private S_Face parseFace(String line) {
        S_Face face = new S_Face();
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

    public WavefrontObjRender render() { return new WavefrontObjRender(this); }
}