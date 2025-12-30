package com.hbm.render.loader;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.joml.Vector3f;

public class ObjFace {
    public Vertex[] vertices;
    public TextureCoordinate[] textureCoordinates;
    public Vertex[] vertexNormals;
    public Vertex faceNormal;
    private boolean smoothing;

    public ObjFace(boolean smoothing) {
        this.smoothing = smoothing;
    }

    public Vertex calculateFaceNormal() {
        if (vertices == null || vertices.length < 3) {
            return new Vertex(0, 1, 0);
        }

        Vector3f v1 = new Vector3f(
                vertices[1].x - vertices[0].x,
                vertices[1].y - vertices[0].y,
                vertices[1].z - vertices[0].z
        );

        Vector3f v2 = new Vector3f(
                vertices[2].x - vertices[0].x,
                vertices[2].y - vertices[0].y,
                vertices[2].z - vertices[0].z
        );

        Vector3f normal = new Vector3f();
        v1.cross(v2, normal);
        normal.normalize();

        return new Vertex(normal.x, normal.y, normal.z);
    }

    // for future
    public BakedQuad bake(TextureAtlasSprite sprite, int tintIndex) {
        int[] vertexData;

        // triangle
        if (vertices.length == 3) {
            vertexData = new int[32];
            for (int i = 0; i < 4; i++) {
                int vertIndex = i < 3 ? i : 2;
                packVertex(vertexData, i, vertIndex, sprite);
            }
        } else {
            // quad
            vertexData = new int[32];
            for (int i = 0; i < 4; i++) {
                packVertex(vertexData, i, i, sprite);
            }
        }

        Direction facing = calculateFacing();
        return new BakedQuad(vertexData, tintIndex, facing, sprite, true);
    }

    private void packVertex(int[] data, int dataIndex, int vertIndex, TextureAtlasSprite sprite) {
        int offset = dataIndex * 8;

        Vertex v = vertices[vertIndex];
        data[offset] = Float.floatToRawIntBits(v.x);
        data[offset + 1] = Float.floatToRawIntBits(v.y);
        data[offset + 2] = Float.floatToRawIntBits(v.z);

        data[offset + 3] = 0xFFFFFFFF;

        float u = 0, vCoord = 0;
        if (textureCoordinates != null && textureCoordinates.length > vertIndex) {
            u = textureCoordinates[vertIndex].u;
            vCoord = textureCoordinates[vertIndex].v;
        }
        data[offset + 4] = Float.floatToRawIntBits(sprite.getU(u));
        data[offset + 5] = Float.floatToRawIntBits(sprite.getV(vCoord));

        data[offset + 6] = 0x00F000F0;

        Vertex normal = smoothing && vertexNormals != null && vertexNormals.length > vertIndex ? vertexNormals[vertIndex] : faceNormal;
        if (normal == null) normal = new Vertex(0, 1, 0);
        data[offset + 7] = packNormal(normal);
    }

    private int packNormal(Vertex normal) {
        int nx = (int) (normal.x * 127) & 0xFF;
        int ny = (int) (normal.y * 127) & 0xFF;
        int nz = (int) (normal.z * 127) & 0xFF;
        return nx | (ny << 8) | (nz << 16);
    }

    private Direction calculateFacing() {
        if (faceNormal == null) return Direction.UP;

        float absX = Math.abs(faceNormal.x);
        float absY = Math.abs(faceNormal.y);
        float absZ = Math.abs(faceNormal.z);

        if (absY >= absX && absY >= absZ) {
            return faceNormal.y > 0 ? Direction.UP : Direction.DOWN;
        } else if (absX >= absZ) {
            return faceNormal.x > 0 ? Direction.EAST : Direction.WEST;
        } else {
            return faceNormal.z > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }
}