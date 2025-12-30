package com.hbm.render.loader;

import java.util.ArrayList;
import java.util.List;

public class ObjGroupObject {
    public String name;
    public List<ObjFace> faces = new ArrayList<>();

    public ObjGroupObject(String name) {
        this.name = name;
    }
}