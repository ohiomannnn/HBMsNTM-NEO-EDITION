package com.hbm.datagen.loaders;

import com.google.gson.JsonObject;
import com.hbm.main.NuclearTechMod;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BarrelItemModelBuilder extends CustomLoaderBuilder<ItemModelBuilder> {

    private final Map<String, ResourceLocation> textures = new LinkedHashMap<>();

    public BarrelItemModelBuilder(ItemModelBuilder parent, ExistingFileHelper existingFileHelper) {
        super(NuclearTechMod.withDefaultNamespace("barrel_geometry_loader"), parent, existingFileHelper, false);
    }

    public BarrelItemModelBuilder texture(String key, ResourceLocation location) {
        this.textures.put(key, location);
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        super.toJson(json);

        JsonObject texturesObject = new JsonObject();
        for(Entry<String, ResourceLocation> entry : this.textures.entrySet()) {
            texturesObject.addProperty(entry.getKey(), entry.getValue().toString());
        }
        json.add("textures", texturesObject);

        return json;
    }
}
