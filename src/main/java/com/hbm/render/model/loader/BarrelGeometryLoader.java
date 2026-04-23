package com.hbm.render.model.loader;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hbm.main.NuclearTechMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class BarrelGeometryLoader implements IGeometryLoader<BarrelGeometry> {

    public static final BarrelGeometryLoader INSTANCE = new BarrelGeometryLoader();
    public static final ResourceLocation ID = NuclearTechMod.withDefaultNamespace("barrel_geometry_loader");

    private BarrelGeometryLoader() {}

    @Override
    public BarrelGeometry read(JsonObject object, JsonDeserializationContext context) throws JsonParseException {
        return new BarrelGeometry();
    }
}
