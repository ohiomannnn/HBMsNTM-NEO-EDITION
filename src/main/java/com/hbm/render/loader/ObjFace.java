package com.hbm.render.loader;

import org.joml.Vector3f;

public class ObjFace {
    public Vertex[] vertices;
    public TextureCoordinate[] textureCoordinates;
    public Vertex[] vertexNormals;
    public Vertex faceNormal;

    public ObjFace(boolean smoothing) { }

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
}