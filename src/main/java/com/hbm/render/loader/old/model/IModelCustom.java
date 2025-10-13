package com.hbm.render.loader.old.model;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IModelCustom {
    String getType();
    @OnlyIn(Dist.CLIENT)
    void renderAll();
    @OnlyIn(Dist.CLIENT)
    void renderOnly(String... groupNames);
    @OnlyIn(Dist.CLIENT)
    void renderPart(String partName);
    @OnlyIn(Dist.CLIENT)
    void renderAllExcept(String... excludedGroupNames);
}
