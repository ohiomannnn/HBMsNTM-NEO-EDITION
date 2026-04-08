package com.hbm.render.loader.old;

// code from minecraft 1.7.10 (net.minecraftforge.client.model.obj)
public class Vertex
{
    public float x, y, z;

    public Vertex(float x, float y)
    {
        this(x, y, 0F);
    }

    public Vertex(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}