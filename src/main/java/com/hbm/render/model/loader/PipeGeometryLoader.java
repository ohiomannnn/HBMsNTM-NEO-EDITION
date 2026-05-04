package com.hbm.render.model.loader;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hbm.main.NuclearTechMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class PipeGeometryLoader implements IGeometryLoader<PipeGeometry> {

    public static final PipeGeometryLoader INSTANCE = new PipeGeometryLoader();
    public static final ResourceLocation ID = NuclearTechMod.withDefaultNamespace("pipe_geometry_loader");

    private PipeGeometryLoader() {}

    @Override
    public PipeGeometry read(JsonObject object, JsonDeserializationContext context) throws JsonParseException {
        return new PipeGeometry();
    }
}
