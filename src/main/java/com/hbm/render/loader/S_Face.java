package com.hbm.render.loader;

import com.hbm.render.loader.old.obj.TextureCoordinate;
import com.hbm.render.loader.old.obj.Vertex;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec3;

public class S_Face {
    public Vertex[] vertices;
    public Vertex[] vertexNormals;
    public Vertex faceNormal;
    public TextureCoordinate[] textureCoordinates;
    private boolean smoothing;

    public S_Face(boolean smoothing) {
        this.smoothing = smoothing;
    }

    public void addFaceForRender(VertexConsumer consumer) {
        addFaceForRender(consumer, 0.0F);
    }

    public void addFaceForRender(VertexConsumer consumer, float textureOffset) {

        if (this.faceNormal == null) {
            this.faceNormal = calculateFaceNormal();
        }

        if(!this.smoothing) {
            consumer.setNormal(this.faceNormal.x, this.faceNormal.y, this.faceNormal.z);
        }

        float averageU = 0.0F;
        float averageV = 0.0F;

        if ((this.textureCoordinates != null) && (this.textureCoordinates.length > 0)) {

            for (int i = 0; i < this.textureCoordinates.length; i++) {
                averageU += this.textureCoordinates[i].u;
                averageV += this.textureCoordinates[i].v;
            }

            averageU /= this.textureCoordinates.length;
            averageV /= this.textureCoordinates.length;
        }

        for(int i = 0; i < this.vertices.length; i++) {

            if((this.textureCoordinates != null) && (this.textureCoordinates.length > 0)) {

                float offsetU = textureOffset;
                float offsetV = textureOffset;

                if(this.textureCoordinates[i].u > averageU) {
                    offsetU = -offsetU;
                }
                if(this.textureCoordinates[i].v > averageV) {
                    offsetV = -offsetV;
                }
                if(this.smoothing && (this.vertexNormals != null) && (i < this.vertexNormals.length)) {
                    consumer.setNormal(this.vertexNormals[i].x, this.vertexNormals[i].y, this.vertexNormals[i].z);
                }

                consumer.addVertex(this.vertices[i].x, this.vertices[i].y, this.vertices[i].z).setUv( this.textureCoordinates[i].u + offsetU, this.textureCoordinates[i].v + offsetV);

            } else {
                consumer.addVertex(this.vertices[i].x, this.vertices[i].y, this.vertices[i].z);
            }
        }
    }

    public Vertex calculateFaceNormal() {

        Vec3 v1 = new Vec3(this.vertices[1].x - this.vertices[0].x, this.vertices[1].y - this.vertices[0].y, this.vertices[1].z - this.vertices[0].z);
        Vec3 v2 = new Vec3(this.vertices[2].x - this.vertices[0].x, this.vertices[2].y - this.vertices[0].y, this.vertices[2].z - this.vertices[0].z);
        Vec3 normalVector;

        normalVector = v1.cross(v2).normalize();

        return new Vertex((float) normalVector.x, (float) normalVector.y, (float) normalVector.z);
    }
}
