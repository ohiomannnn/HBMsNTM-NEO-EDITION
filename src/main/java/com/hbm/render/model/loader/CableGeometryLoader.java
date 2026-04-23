package com.hbm.render.model.loader;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hbm.main.NuclearTechMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class CableGeometryLoader implements IGeometryLoader<CableGeometry> {

    public static final CableGeometryLoader INSTANCE = new CableGeometryLoader();
    public static final ResourceLocation ID = NuclearTechMod.withDefaultNamespace("cable_geometry_loader");

    private CableGeometryLoader() {}

    @Override
    public CableGeometry read(JsonObject object, JsonDeserializationContext context) throws JsonParseException {

        boolean block = GsonHelper.getAsBoolean(object, "block", false);

        return new CableGeometry(block);
    }
}
