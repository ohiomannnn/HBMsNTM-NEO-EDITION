package com.hbm.render.loader;

import com.hbm.render.loader.old.TextureCoordinate;
import com.hbm.render.loader.old.Vertex;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class S_Face {

    public Vertex[] vertices;
    public Vertex[] vertexNormals;
    public Vertex faceNormal;
    public TextureCoordinate[] textureCoordinates;

    public S_Face(boolean ignored) { }

    public Vertex calculateFaceNormal() {

        Vector3f v1 = new Vector3f(this.vertices[1].x - this.vertices[0].x, this.vertices[1].y - this.vertices[0].y, this.vertices[1].z - this.vertices[0].z);
        Vector3f v2 = new Vector3f(this.vertices[2].x - this.vertices[0].x, this.vertices[2].y - this.vertices[0].y, this.vertices[2].z - this.vertices[0].z);
        Vector3f normalVector = v1.cross(v2).normalize();

        return new Vertex(normalVector.x, normalVector.y, normalVector.z);
    }
}
