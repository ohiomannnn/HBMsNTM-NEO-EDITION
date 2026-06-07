package com.hbm.render.model.loader;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hbm.main.NuclearTechMod;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class BarbedWireGeometryLoader implements IGeometryLoader<BarbedWireGeometry> {

    public static final BarbedWireGeometryLoader INSTANCE = new BarbedWireGeometryLoader();
    public static final ResourceLocation ID = NuclearTechMod.withDefaultNamespace("barbed_wire_geometry_loader");

    private BarbedWireGeometryLoader() {}

    @Override
    public BarbedWireGeometry read(JsonObject object, JsonDeserializationContext context) throws JsonParseException {
        return new BarbedWireGeometry();
    }
}
