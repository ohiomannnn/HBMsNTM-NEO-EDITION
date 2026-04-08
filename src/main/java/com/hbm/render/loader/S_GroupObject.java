package com.hbm.render.loader;

import com.mojang.blaze3d.vertex.VertexFormat;

import java.util.ArrayList;

public class S_GroupObject {

    public String name;
    public ArrayList<S_Face> faces = new ArrayList<>();
    public VertexFormat.Mode mode;

    public S_GroupObject() {
        this("");
    }

    public S_GroupObject(String name) {
        this(name, null);
    }

    public S_GroupObject(String name, VertexFormat.Mode mode) {
        this.name = name;
        this.mode = mode;
    }
}