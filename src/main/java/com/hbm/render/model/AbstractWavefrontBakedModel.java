package com.hbm.render.model;

import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.S_Face;
import com.hbm.render.loader.S_GroupObject;
import com.hbm.render.loader.old.TextureCoordinate;
import com.hbm.render.loader.old.Vertex;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.QuadTransformers;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWavefrontBakedModel extends AbstractBakedModel {

    protected final HFRWavefrontObject model;

    protected AbstractWavefrontBakedModel(HFRWavefrontObject model, ItemTransforms transforms) {
        super(false, true, false, true, transforms, ItemOverrides.EMPTY);
        this.model = model;
    }

    @Deprecated
    protected List<BakedQuad> bakeSimpleQuads(@Nullable List<String> partNames, float roll, float pitch, float yaw, BlockTranslate translate, TextureAtlasSprite sprite) {
        return bakeSimpleQuads(partNames, roll, pitch, yaw, translate, sprite, -1);
    }

    @Deprecated
    protected List<BakedQuad> bakeSimpleQuads(@Nullable List<String> partNames, float roll, float pitch, float yaw, BlockTranslate translate, TextureAtlasSprite sprite, int tintIndex) {
        List<FaceGeometry> geometries = buildGeometry(partNames);
        List<BakedQuad> quads = new ArrayList<>(geometries.size());
        for(FaceGeometry geometry : geometries) quads.add(geometry.buildQuad(sprite, tintIndex));
        return quads;
    }

    protected List<BakedQuad> bakeSimpleQuads(@Nullable List<String> partNames, @Nullable Matrix4f matrix, TextureAtlasSprite sprite) {
        return bakeSimpleQuads(partNames, matrix, sprite, -1);
    }

    protected List<BakedQuad> bakeSimpleQuads(@Nullable List<String> partNames, @Nullable Matrix4f matrix, TextureAtlasSprite sprite, int tintIndex) {
        List<FaceGeometry> geometries = buildGeometry(partNames);
        List<BakedQuad> quads = new ArrayList<>(geometries.size());

        for(FaceGeometry geometry : geometries) quads.add(geometry.buildQuad(sprite, tintIndex));

        if(matrix != null) {
            IQuadTransformer transformer = QuadTransformers.applying(new Transformation(matrix));
            transformer.processInPlace(quads);
        }

        return quads;
    }

    protected List<FaceGeometry> buildGeometry(@Nullable List<String> partNames) {
        List<FaceGeometry> geometries = new ArrayList<>();

        for(S_GroupObject group : model.groupObjects) {
            if(partNames != null && !partNames.contains(group.name)) continue;

            for(S_Face face : group.faces) {
                Vertex normal = face.faceNormal;

                float fxn = normal.x;
                float fyn = normal.y;
                float fzn = normal.z;

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

                    float vx = vertex.x;
                    float vy = vertex.y;
                    float vz = vertex.z;

                    TextureCoordinate tex = face.textureCoordinates[idx];
                    uu[v] = tex.u;
                    vv[v] = tex.v;

                    Vertex vertexNormal = face.vertexNormals != null && idx < face.vertexNormals.length ? face.vertexNormals[idx] : null;
                    if(vertexNormal != null) {
                        float vxn = vertexNormal.x;
                        float vyn = vertexNormal.y;
                        float vzn = vertexNormal.z;
                        Vector3f vectorNormal = new Vector3f(vxn, vyn, vzn);
                        if(vectorNormal.lengthSquared() > 0.0F) {
                            vectorNormal.normalize();
                        } else {
                            vectorNormal.set(fxn, fyn, fzn);
                        }
                        vertexNormals[v] = vectorNormal;
                    } else {
                        vertexNormals[v] = new Vector3f(fxn, fyn, fzn);
                    }

                    px[v] = vx;
                    py[v] = vy;
                    pz[v] = vz;
                }

                Direction direction = Direction.getNearest(fxn, fyn, fzn);
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
