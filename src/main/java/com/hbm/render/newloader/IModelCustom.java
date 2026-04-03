package com.hbm.render.newloader;

public interface IModelCustom {
    void renderAll();
    void renderPart(String partName);
    void renderOnly(String... groupNames);
    void renderAllExcept(String... excludedGroupNames);
}
