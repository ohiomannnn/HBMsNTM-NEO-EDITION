package com.hbm.inventory.fluid.trait;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.util.ChatBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.List;

public class FT_Pheromone extends  FluidTrait{

    public int type;
    public FT_Pheromone() {}

    public FT_Pheromone(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public void addInfo(List<Component> info) {

        if(type == 1) {
            info.add(ChatBuilder.start("[").nextTranslation("fluids.trait.glyphidPheromones").next("]").colorAll(ChatFormatting.AQUA).flush());
        } else {
            info.add(ChatBuilder.start("[").nextTranslation("fluids.trait.modifiedPheromones").next("]").colorAll(ChatFormatting.BLUE).flush());
        }
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("type").value(type);
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        this.type = obj.get("type").getAsInt();
    }
}
