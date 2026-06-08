package com.hbm.render.model.loader;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hbm.main.NuclearTechMod;
import com.hbm.render.model.loader.NtmGeometry.BakedModelType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class NtmGeometryLoader implements IGeometryLoader<NtmGeometry> {

    public static final NtmGeometryLoader INSTANCE = new NtmGeometryLoader();
    public static final ResourceLocation ID = NuclearTechMod.withDefaultNamespace("ntm_geometry_loader");

    private NtmGeometryLoader() {}

    @Override
    public NtmGeometry read(JsonObject object, JsonDeserializationContext context) throws JsonParseException {
        BakedModelType type = BakedModelType.valueOf(object.get("type").getAsString().toUpperCase());
        return new NtmGeometry(type);
    }
}
