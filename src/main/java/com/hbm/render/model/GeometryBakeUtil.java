package com.hbm.render.model;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class GeometryBakeUtil {

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
