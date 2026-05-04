package com.hbm.render.model;

import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.S_Face;
import com.hbm.render.loader.S_GroupObject;
import com.hbm.render.loader.old.TextureCoordinate;
import com.hbm.render.loader.old.Vertex;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWavefrontBakedModel extends AbstractBakedModel {

    protected final HFRWavefrontObject model;

    protected AbstractWavefrontBakedModel(HFRWavefrontObject model, ItemTransforms transforms) {
        super(false, true, false, true, transforms, ItemOverrides.EMPTY);
        this.model = model;
    }

    protected List<BakedQuad> bakeSimpleQuads(List<String> partNames, float roll, float pitch, float yaw, BlockTranslate translate, TextureAtlasSprite sprite) {
        return bakeSimpleQuads(partNames, roll, pitch, yaw, translate, sprite, -1);
    }

    protected List<BakedQuad> bakeSimpleQuads(List<String> partNames, float roll, float pitch, float yaw, BlockTranslate translate, TextureAtlasSprite sprite, int tintIndex) {
        List<FaceGeometry> geometries = buildGeometry(partNames, roll, pitch, yaw, translate);
        List<BakedQuad> quads = new ArrayList<>(geometries.size());
        for(FaceGeometry geometry : geometries) quads.add(geometry.buildQuad(sprite, tintIndex));
        return quads;
    }

    protected List<FaceGeometry> buildGeometry(List<String> partNames, float roll, float pitch, float yaw, BlockTranslate translate) {
        List<FaceGeometry> geometries = new ArrayList<>();

        for(S_GroupObject group : model.groupObjects) {
            if(partNames != null && !partNames.contains(group.name)) continue;

            for(S_Face face : group.faces) {
                Vertex normal = face.faceNormal;

                float[] n1 = GeometryBakeUtil.rotateX(normal.x, normal.y, normal.z, roll);
                float[] n2 = GeometryBakeUtil.rotateZ(n1[0], n1[1], n1[2], pitch);
                float[] n3 = GeometryBakeUtil.rotateY(n2[0], n2[1], n2[2], yaw);

                int vertexCount = face.vertices.length;
                if(vertexCount < 3) continue;

                int[] indices = vertexCount >= 4 ? new int[]{0, 1, 2, 3} : new int[]{0, 1, 2, 2};

                float[] px = new float[4];
                float[] py = new float[4];
                float[] pz = new float[4];
                float[] uu = new float[4];
                float[] vv = new float[4];
                Vector3f[] vertexNormals = new Vector3f[4];

                for(int v = 0; v < 4; v++) {
                    int idx = indices[v];
                    Vertex vertex = face.vertices[idx];

                    float[] p1 = GeometryBakeUtil.rotateX(vertex.x, vertex.y, vertex.z, roll);
                    float[] p2 = GeometryBakeUtil.rotateZ(p1[0], p1[1], p1[2], pitch);
                    float[] p3 = GeometryBakeUtil.rotateY(p2[0], p2[1], p2[2], yaw);

                    float x = p3[0];
                    float y = p3[1];
                    float z = p3[2];

                    switch(translate) {
                        case CENTER -> {
                            x += 0.5F;
                            y += 0.5F;
                            z += 0.5F;
                        }
                        case CENTER_NO_Y_OFFSET -> {
                            x += 0.5F;
                            x += 0F;
                            z += 0.5F;
                        }
                    }

                    TextureCoordinate tex = face.textureCoordinates[idx];
                    uu[v] = tex.u;
                    vv[v] = tex.v;

                    Vertex vertexNormal = face.vertexNormals != null && idx < face.vertexNormals.length ? face.vertexNormals[idx] : null;
                    if(vertexNormal != null) {
                        float[] vn1 = GeometryBakeUtil.rotateX(vertexNormal.x, vertexNormal.y, vertexNormal.z, roll);
                        float[] vn2 = GeometryBakeUtil.rotateZ(vn1[0], vn1[1], vn1[2], pitch);
                        float[] vn3 = GeometryBakeUtil.rotateY(vn2[0], vn2[1], vn2[2], yaw);
                        Vector3f vectorNormal = new Vector3f(vn3[0], vn3[1], vn3[2]);
                        if(vectorNormal.lengthSquared() > 0.0F) {
                            vectorNormal.normalize();
                        } else {
                            vectorNormal.set(n3[0], n3[1], n3[2]);
                        }
                        vertexNormals[v] = vectorNormal;
                    } else {
                        vertexNormals[v] = new Vector3f(n3[0], n3[1], n3[2]);
                    }

                    px[v] = x;
                    py[v] = y;
                    pz[v] = z;
                }

                Direction direction = Direction.getNearest(n3[0], n3[1], n3[2]);
                geometries.add(new FaceGeometry(direction, px, py, pz, uu, vv, vertexNormals));
            }
        }

        return geometries;
    }

    protected enum BlockTranslate {
        CENTER,
        CENTER_NO_Y_OFFSET,
        NONE
    }

    protected static class FaceGeometry {

        private final Direction direction;
        private final float[] px;
        private final float[] py;
        private final float[] pz;
        private final float[] uu;
        private final float[] vv;
        private final Vector3f[] vertexNormals;

        FaceGeometry(Direction direction, float[] px, float[] py, float[] pz, float[] uu, float[] vv, Vector3f[] vertexNormals) {
            this.direction = direction;
            this.px = px;
            this.py = py;
            this.pz = pz;
            this.uu = uu;
            this.vv = vv;
            this.vertexNormals = vertexNormals;
        }

        public BakedQuad buildQuad(TextureAtlasSprite sprite, int tintIndex) {
            return buildQuad(sprite, tintIndex, 1.0F, 1.0F);
        }

        public BakedQuad buildQuad(TextureAtlasSprite sprite, int tintIndex, float uScale, float vScale) {
            int[] vertexData = new int[32];
            for(int i = 0; i < 4; i++) GeometryBakeUtil.putVertex(vertexData, i, px[i], py[i], pz[i], uu[i] * uScale, vv[i] * vScale, vertexNormals[i], sprite);
            return new BakedQuad(vertexData, tintIndex, direction, sprite, true, true);
        }
    }
}
