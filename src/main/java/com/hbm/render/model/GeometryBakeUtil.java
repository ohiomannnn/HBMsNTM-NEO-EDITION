package com.hbm.render.model;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class GeometryBakeUtil {

    public static float[] rotateX(float x, float y, float z, float angle) {
        float cos = Mth.cos(angle);
        float sin = Mth.sin(angle);
        float ny = y * cos + z * sin;
        float nz = z * cos - y * sin;
        return new float[]{x, ny, nz};
    }

    public static float[] rotateY(float x, float y, float z, float angle) {
        float cos = Mth.cos(angle);
        float sin = Mth.sin(angle);
        float nx = x * cos + z * sin;
        float nz = -x * sin + z * cos;
        return new float[]{nx, y, nz};
    }

    public static float[] rotateZ(float x, float y, float z, float angle) {
        float cos = Mth.cos(angle);
        float sin = Mth.sin(angle);
        float nx = x * cos + y * sin;
        float ny = y * cos - x * sin;
        return new float[]{nx, ny, z};
    }

    public static void putVertex(int[] vertexData, int vertexIndex, float x, float y, float z, float u, float v, Vector3f normal, TextureAtlasSprite sprite) {
        int offset = vertexIndex * 8;
        vertexData[offset + 0] = Float.floatToRawIntBits(x);
        vertexData[offset + 1] = Float.floatToRawIntBits(y);
        vertexData[offset + 2] = Float.floatToRawIntBits(z);
        vertexData[offset + 3] = -1;
        vertexData[offset + 4] = Float.floatToRawIntBits(Mth.lerp(u, sprite.getU0(), sprite.getU1()));
        vertexData[offset + 5] = Float.floatToRawIntBits(Mth.lerp(v, sprite.getV0(), sprite.getV1()));
        vertexData[offset + 6] = 0;
        vertexData[offset + 7] = packNormal(normal);
    }

    private static int packNormal(Vector3f normal) {
        int nx = ((byte)(normal.x * 127F)) & 0xFF;
        int ny = ((byte)(normal.y * 127F)) & 0xFF;
        int nz = ((byte)(normal.z * 127F)) & 0xFF;
        return nx | (ny << 8) | (nz << 16);
    }
}
