package com.hbm.render.loader.bakedLoader;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hbm.HBMsNTM;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

import java.util.ArrayList;
import java.util.List;

public class HFRObjGeometryLoader implements IGeometryLoader<HFRObjGeometry> {

    public static final HFRObjGeometryLoader INSTANCE = new HFRObjGeometryLoader();
    public static final ResourceLocation ID = HBMsNTM.withDefaultNamespaceNT("hfr_obj");

    private HFRObjGeometryLoader() {}

    @Override
    public HFRObjGeometry read(JsonObject json, JsonDeserializationContext context) {
        String modelPath = GsonHelper.getAsString(json, "model");
        ResourceLocation modelLocation = ResourceLocation.parse(modelPath);

        boolean smoothing = GsonHelper.getAsBoolean(json, "smoothing", true);
        boolean asVbo = GsonHelper.getAsBoolean(json, "as_vbo", false);
        boolean renderAll = GsonHelper.getAsBoolean(json, "render_all", true);

        List<String> renderOnly = parseStringList(json, "render_only");
        List<String> exclude = parseStringList(json, "exclude");

        return new HFRObjGeometry(modelLocation, smoothing, asVbo, renderAll, renderOnly, exclude);
    }

    private List<String> parseStringList(JsonObject json, String key) {
        List<String> list = new ArrayList<>();
        if (json.has(key)) {
            JsonArray array = GsonHelper.getAsJsonArray(json, key);
            for (JsonElement element : array) {
                list.add(element.getAsString());
            }
        }
        return list;
    }
}