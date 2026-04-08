package com.hbm.render.loader;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;

public class HFRModelReloader extends SimplePreparableReloadListener<Void> {

    @Override
    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        return null;
    }

    @Override
    protected void apply(Void unused, ResourceManager resourceManager, ProfilerFiller profilerFiller) {

        for(HFRWavefrontObject obj : HFRWavefrontObject.allModels) {

            try {
                obj.destroy();
                Resource res = Minecraft.getInstance().getResourceManager().getResourceOrThrow(obj.resource);
                try (InputStream stream = res.open()) {
                    obj.loadObjModel(stream);
                }
            } catch (IOException e) { }
        }
        for(Entry<HFRWavefrontObjectVBO, HFRWavefrontObject> entry : HFRWavefrontObject.allVBOs.entrySet()) {
            HFRWavefrontObjectVBO vbo = entry.getKey();
            HFRWavefrontObject obj = entry.getValue();

            vbo.destroy();
            vbo.load(obj);
        }
    }
}
