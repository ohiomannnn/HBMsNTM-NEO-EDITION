package com.hbm.render.util;

import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.S_Face;
import com.hbm.render.loader.S_GroupObject;
import com.hbm.render.loader.old.TextureCoordinate;
import com.hbm.render.loader.old.Vertex;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class ObjUtil {

    public static List<BakedQuad> buildQuads(List<BakedQuadHelper> quadHelpers, TextureAtlasSprite sprite) {
        List<BakedQuad> quads = new ArrayList<>();

        for(BakedQuadHelper quadHelper : quadHelpers) {
            quads.add(quadHelper.buildQuads(sprite, -1));
        }

        return quads;
    }

    public static List<BakedQuadHelper> getQuadsFromParts(HFRWavefrontObject model, List<String> parts, float rot, float pitch, float roll, boolean applyShading, boolean centerToBlock) {
        List<BakedQuadHelper> quads = new ArrayList<>();

        for(S_GroupObject group : model.groupObjects) {
            if(!parts.contains(group.name)) {
                continue;
            }

            for(S_Face face : group.faces) {

                int vertexCount = face.vertices.length;
                int[] indices = vertexCount == 3 ? new int[]{0, 1, 2, 2} : new int[]{0, 1, 2, 3};

                int[] vertexData = new int[32];

                Vertex n = face.faceNormal;

                Vector3f normal = new Vector3f(n.x, n.y, n.z);
                normal.rotateZ(pitch);
                normal.rotateY(rot);

                Direction direction = Direction.getNearest(normal.x, normal.y, normal.z);

                for (int i = 0; i < 4; i++) {
                    int idx = indices[i];
                    idx = Math.min(idx, vertexCount - 1);

                    Vertex v = face.vertices[idx];

                    Vector3f vec = new Vector3f(v.x, v.y, v.z);
                    vec.rotateX(roll);
                    vec.rotateZ(pitch);
                    vec.rotateY(rot);

                    float x = vec.x;
                    float y = vec.y;
                    float z = vec.z;

                    if (centerToBlock) {
                        x += 0.5F;
                        y += 0.5F;
                        z += 0.5F;
                    }

                    float u = 0, vCoord = 0;
                    if (idx < face.textureCoordinates.length) {
                        u = face.textureCoordinates[idx].u;
                        vCoord = face.textureCoordinates[idx].v;
                    }

                    int offset = i * 8;
                    vertexData[offset + 0] = Float.floatToRawIntBits(x);
                    vertexData[offset + 1] = Float.floatToRawIntBits(y);
                    vertexData[offset + 2] = Float.floatToRawIntBits(z);
                    vertexData[offset + 3] = -1;
                    vertexData[offset + 4] = Float.floatToRawIntBits(u);
                    vertexData[offset + 5] = Float.floatToRawIntBits(vCoord);
                    vertexData[offset + 6] = 0;
                    vertexData[offset + 7] = packNormal(normal);

                    quads.add(new BakedQuadHelper(vertexData, direction, applyShading, false));
                }
            }
        }

        return quads;
    }

    private static int packNormal(Vector3f normal) {
        int nx = ((byte)(normal.x * 127F)) & 0xFF;
        int ny = ((byte)(normal.y * 127F)) & 0xFF;
        int nz = ((byte)(normal.z * 127F)) & 0xFF;
        return nx | (ny << 8) | (nz << 16);
    }
}
