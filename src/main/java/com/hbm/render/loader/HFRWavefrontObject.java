package com.hbm.render.loader;

import com.hbm.main.NuclearTechMod;
import com.hbm.render.loader.old.ModelFormatException;
import com.hbm.render.loader.old.TextureCoordinate;
import com.hbm.render.loader.old.Vertex;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HFRWavefrontObject implements IModelCustomNamed {

    /** For resource reloading */
    public static final LinkedHashSet<HFRWavefrontObject> allModels = new LinkedHashSet<>();
    public static final LinkedHashMap<HFRWavefrontObjectVBO, HFRWavefrontObject> allVBOs = new LinkedHashMap<>();

    private static final Pattern vertexPattern = Pattern.compile("(v( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *\\n)|(v( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *$)");
    private static final Pattern vertexNormalPattern = Pattern.compile("(vn( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *\\n)|(vn( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *$)");
    private static final Pattern textureCoordinatePattern = Pattern.compile("(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+(\\.\\d+)?){2,3} *$)");
    private static final Pattern face_V_VT_VN_Pattern = Pattern.compile("(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
    private static final Pattern face_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
    private static final Pattern face_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
    private static final Pattern face_V_Pattern = Pattern.compile("(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
    private static final Pattern groupObjectPattern = Pattern.compile("([go]( [\\w\\d\\.]+) *\\n)|([go]( [\\w\\d\\.]+) *$)");

    private static Matcher vertexMatcher, vertexNormalMatcher, textureCoordinateMatcher;
    private static Matcher face_V_VT_VN_Matcher, face_V_VT_Matcher, face_V_VN_Matcher, face_V_Matcher;
    private static Matcher groupObjectMatcher;

    public ArrayList<Vertex> vertices = new ArrayList<>();
    public ArrayList<Vertex> vertexNormals = new ArrayList<>();
    public ArrayList<TextureCoordinate> textureCoordinates = new ArrayList<>();
    public ArrayList<S_GroupObject> groupObjects = new ArrayList<>();
    private S_GroupObject currentGroupObject;
    public ResourceLocation resource;
    private final String fileName;
    private boolean smoothing = true;
    private boolean allowMixedMode = false;

    public HFRWavefrontObject(String path) throws ModelFormatException {
        this(NuclearTechMod.withDefaultNamespace(path), false);
    }

    public HFRWavefrontObject(String path, boolean mixedMode) throws ModelFormatException {
        this(NuclearTechMod.withDefaultNamespace(path), mixedMode);
    }

    public HFRWavefrontObject noSmooth() {
        this.smoothing = false;
        return this;
    }

    /** Provides a way for a model to have both tris and quads, however this means it can't be rendered directly.
     * Useful for ISBRHs which access vertices manually, allowing the quad to tri trick without forcing the entire model to be redundant tris. */
    public void mixedMode() { this.allowMixedMode = true; }

    public HFRWavefrontObject(ResourceLocation resource) throws ModelFormatException {
        this(resource, false);
    }

    public HFRWavefrontObject(ResourceLocation resource, boolean mixedMode) throws ModelFormatException {
        if(mixedMode) this.mixedMode();

        this.resource = resource;
        this.fileName = resource.toString();

        try {
            Resource res = Minecraft.getInstance().getResourceManager().getResourceOrThrow(resource);

            try (InputStream stream = res.open()) {
                loadObjModel(stream);
            }
        } catch (IOException e) {
            throw new ModelFormatException("Failed to load OBJ model: " + resource, e);
        }

        allModels.add(this);
    }

    public void destroy() {
        vertices.clear();
        vertexNormals.clear();
        textureCoordinates.clear();
        groupObjects.clear();
        currentGroupObject = null;
    }

    public void loadObjModel(InputStream inputStream) throws ModelFormatException {
        BufferedReader reader = null;

        String currentLine = null;
        int lineCount = 0;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));

            while((currentLine = reader.readLine()) != null) {
                lineCount++;
                currentLine = currentLine.replaceAll("\\s+", " ").trim();

                if(currentLine.startsWith("#") || currentLine.length() == 0) {
                    continue;
                } else if(currentLine.startsWith("v ")) {
                    Vertex vertex = parseVertex(currentLine, lineCount);
                    if(vertex != null) {
                        vertices.add(vertex);
                    }
                } else if(currentLine.startsWith("vn ")) {
                    Vertex vertex = parseVertexNormal(currentLine, lineCount);
                    if(vertex != null) {
                        vertexNormals.add(vertex);
                    }
                } else if(currentLine.startsWith("vt ")) {
                    TextureCoordinate textureCoordinate = parseTextureCoordinate(currentLine, lineCount);
                    if(textureCoordinate != null) {
                        textureCoordinates.add(textureCoordinate);
                    }
                } else if(currentLine.startsWith("f ")) {

                    if(currentGroupObject == null) {
                        currentGroupObject = new S_GroupObject("Default");
                    }

                    S_Face face = parseFace(currentLine, lineCount);

                    currentGroupObject.faces.add(face);

                } else if(currentLine.startsWith("g ") | currentLine.startsWith("o ")) {
                    S_GroupObject group = parseGroupObject(currentLine, lineCount);

                    if(group != null) {
                        if(currentGroupObject != null) {
                            groupObjects.add(currentGroupObject);
                        }
                    }

                    currentGroupObject = group;
                }
            }

            groupObjects.add(currentGroupObject);
        } catch (IOException e) {
            throw new ModelFormatException("caught IO Exception while reading model format", e);
        } finally {
            try {
                reader.close();
            } catch(IOException e) {
                // hush
            }

            try {
                inputStream.close();
            } catch(IOException e) {
                // hush
            }
        }
    }

    @Override
    public void renderAll() {
        throw new RuntimeException("You cant render models with just HFRWavefrontObject, render with vbo!");
    }

    @Override
    public void renderPart(String partName) {
        throw new RuntimeException("You cant render models with just HFRWavefrontObject, render with vbo!");
    }

    @Override
    public void renderOnly(String... groupNames) {
        throw new RuntimeException("You cant render models with just HFRWavefrontObject, render with vbo!");
    }

    @Override
    public void renderAllExcept(String... excludedGroupNames) {
        throw new RuntimeException("You cant render models with just HFRWavefrontObject, render with vbo!");
    }

    private Vertex parseVertex(String line, int lineCount) throws ModelFormatException {
        Vertex vertex = null;

        if(isValidVertexLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");

            try {
                if(tokens.length == 2) {
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
                } else if(tokens.length == 3) {
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
                }
            } catch(NumberFormatException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
        }

        return vertex;
    }

    private Vertex parseVertexNormal(String line, int lineCount) throws ModelFormatException {
        Vertex vertexNormal = null;

        if(isValidVertexNormalLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");

            try {
                if(tokens.length == 3)
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
            } catch(NumberFormatException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
        }

        return vertexNormal;
    }

    private TextureCoordinate parseTextureCoordinate(String line, int lineCount) throws ModelFormatException {
        TextureCoordinate textureCoordinate = null;

        if(isValidTextureCoordinateLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");

            try {
                if(tokens.length == 2)
                    return new TextureCoordinate(Float.parseFloat(tokens[0]), 1 - Float.parseFloat(tokens[1]));
                else if(tokens.length == 3)
                    return new TextureCoordinate(Float.parseFloat(tokens[0]), 1 - Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
            } catch(NumberFormatException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
        }

        return textureCoordinate;
    }

    private S_Face parseFace(String line, int lineCount) throws ModelFormatException {
        S_Face face;

        if(isValidFaceLine(line)) {
            face = new S_Face(this.smoothing);

            String trimmedLine = line.substring(line.indexOf(" ") + 1);
            String[] tokens = trimmedLine.split(" ");
            String[] subTokens = null;

            if(!this.allowMixedMode) {
                if(tokens.length == 3) {
                    if(currentGroupObject.mode == null) {
                        currentGroupObject.mode = VertexFormat.Mode.TRIANGLES;
                    } else if(currentGroupObject.mode != VertexFormat.Mode.TRIANGLES) {
                        throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName
                                + "' - Invalid number of points for face (expected 4, found " + tokens.length + ")");
                    }
                } else if(tokens.length == 4) {
                    if(currentGroupObject.mode == null) {
                        currentGroupObject.mode = VertexFormat.Mode.QUADS;
                    } else if(currentGroupObject.mode != VertexFormat.Mode.QUADS) {
                        throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName
                                + "' - Invalid number of points for face (expected 3, found " + tokens.length + ")");
                    }
                }
            }

            // f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3 ...
            if(isValidFace_V_VT_VN_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];

                for(int i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("/");

                    face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.textureCoordinates[i] = textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                    face.vertexNormals[i] = vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            }
            // f v1/vt1 v2/vt2 v3/vt3 ...
            else if(isValidFace_V_VT_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];

                for(int i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("/");

                    face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.textureCoordinates[i] = textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            }
            // f v1//vn1 v2//vn2 v3//vn3 ...
            else if(isValidFace_V_VN_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];

                for(int i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("//");

                    face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.vertexNormals[i] = vertexNormals.get(Integer.parseInt(subTokens[1]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            }
            // f v1 v2 v3 ...
            else if(isValidFace_V_Line(line)) {
                face.vertices = new Vertex[tokens.length];

                for(int i = 0; i < tokens.length; ++i) {
                    face.vertices[i] = vertices.get(Integer.parseInt(tokens[i]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            } else {
                throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
        }

        return face;
    }

    private S_GroupObject parseGroupObject(String line, int lineCount) throws ModelFormatException {
        S_GroupObject group = null;

        if(isValidGroupObjectLine(line)) {
            String trimmedLine = line.substring(line.indexOf(" ") + 1);

            if(trimmedLine.length() > 0) {
                group = new S_GroupObject(trimmedLine);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
        }

        return group;
    }

    private static boolean isValidVertexLine(String line) {
        if(vertexMatcher != null) {
            vertexMatcher.reset();
        }

        vertexMatcher = vertexPattern.matcher(line);
        return vertexMatcher.matches();
    }

    private static boolean isValidVertexNormalLine(String line) {
        if(vertexNormalMatcher != null) {
            vertexNormalMatcher.reset();
        }

        vertexNormalMatcher = vertexNormalPattern.matcher(line);
        return vertexNormalMatcher.matches();
    }

    private static boolean isValidTextureCoordinateLine(String line) {
        if(textureCoordinateMatcher != null) {
            textureCoordinateMatcher.reset();
        }

        textureCoordinateMatcher = textureCoordinatePattern.matcher(line);
        return textureCoordinateMatcher.matches();
    }

    private static boolean isValidFace_V_VT_VN_Line(String line) {
        if(face_V_VT_VN_Matcher != null) {
            face_V_VT_VN_Matcher.reset();
        }

        face_V_VT_VN_Matcher = face_V_VT_VN_Pattern.matcher(line);
        return face_V_VT_VN_Matcher.matches();
    }

    private static boolean isValidFace_V_VT_Line(String line) {
        if(face_V_VT_Matcher != null) {
            face_V_VT_Matcher.reset();
        }

        face_V_VT_Matcher = face_V_VT_Pattern.matcher(line);
        return face_V_VT_Matcher.matches();
    }

    private static boolean isValidFace_V_VN_Line(String line) {
        if(face_V_VN_Matcher != null) {
            face_V_VN_Matcher.reset();
        }

        face_V_VN_Matcher = face_V_VN_Pattern.matcher(line);
        return face_V_VN_Matcher.matches();
    }

    private static boolean isValidFace_V_Line(String line) {
        if(face_V_Matcher != null) {
            face_V_Matcher.reset();
        }

        face_V_Matcher = face_V_Pattern.matcher(line);
        return face_V_Matcher.matches();
    }

    private static boolean isValidFaceLine(String line) {
        return isValidFace_V_VT_VN_Line(line) || isValidFace_V_VT_Line(line) || isValidFace_V_VN_Line(line) || isValidFace_V_Line(line);
    }

    private static boolean isValidGroupObjectLine(String line) {
        if(groupObjectMatcher != null) {
            groupObjectMatcher.reset();
        }

        groupObjectMatcher = groupObjectPattern.matcher(line);
        return groupObjectMatcher.matches();
    }

    @Override
    public List<String> getPartNames() {
        List<String> names = new ArrayList<>();
        for(S_GroupObject data : groupObjects) {
            names.add(data.name);
        }
        return names;
    }

    public HFRWavefrontObjectVBO asVBO() {
        HFRWavefrontObjectVBO vbo = new HFRWavefrontObjectVBO(this);
        allVBOs.put(vbo, this);
        return vbo;
    }
}
