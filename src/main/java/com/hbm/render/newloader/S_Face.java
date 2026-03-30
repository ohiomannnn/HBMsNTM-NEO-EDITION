package com.hbm.render.newloader;

import com.hbm.render.newloader.old.TextureCoordinate;
import com.hbm.render.newloader.old.Vertex;
import net.minecraft.world.phys.Vec3;

public class S_Face {

    public Vertex[] vertices;
    public Vertex[] vertexNormals;
    public Vertex faceNormal;
    public TextureCoordinate[] textureCoordinates;

    public S_Face(boolean ignored) { }

    public Vertex calculateFaceNormal() {

        Vec3 v1 = new Vec3(this.vertices[1].x - this.vertices[0].x, this.vertices[1].y - this.vertices[0].y, this.vertices[1].z - this.vertices[0].z);
        Vec3 v2 = new Vec3(this.vertices[2].x - this.vertices[0].x, this.vertices[2].y - this.vertices[0].y, this.vertices[2].z - this.vertices[0].z);
        Vec3 normalVector = null;

        normalVector = v1.cross(v2).normalize();

        return new Vertex((float) normalVector.x, (float) normalVector.y, (float) normalVector.z);
    }
}
