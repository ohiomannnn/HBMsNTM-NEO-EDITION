package com.hbm.render.model.loader;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hbm.main.NuclearTechMod;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class DetCordGeometryLoader implements IGeometryLoader<DetCordGeometry> {

    public static final DetCordGeometryLoader INSTANCE = new DetCordGeometryLoader();
    public static final ResourceLocation ID = NuclearTechMod.withDefaultNamespace("det_cord_geometry_loader");

    private DetCordGeometryLoader() {}

    @Override
    public DetCordGeometry read(JsonObject object, JsonDeserializationContext context) throws JsonParseException {
        return new DetCordGeometry();
    }
}
