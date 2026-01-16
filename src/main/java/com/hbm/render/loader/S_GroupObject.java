package com.hbm.render.loader;

import java.util.ArrayList;
import java.util.List;

public class S_GroupObject {
    public String name;
    public List<S_Face> faces = new ArrayList<>();

    public S_GroupObject(String name) {
        this.name = name;
    }
}