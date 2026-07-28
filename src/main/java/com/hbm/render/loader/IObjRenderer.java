package com.hbm.render.loader;

import com.hbm.render.material.Material;

import java.util.List;

public interface IObjRenderer {

    void renderAll(Material material);
    void renderPart(Material material, String partName);
    void renderOnly(Material material, String... groupNames);
    void renderAllExcept(Material material, String... excludedGroupNames);

    List<String> getPartNames();
}
