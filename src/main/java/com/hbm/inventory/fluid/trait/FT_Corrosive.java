package com.hbm.inventory.fluid.trait;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.List;

public class FT_Corrosive extends FluidTrait {

    /* 0-100 */
    private int rating;

    public FT_Corrosive() { }

    public FT_Corrosive(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public boolean isHighlyCorrosive() {
        return rating > 50;
    }

    @Override
    public void addInfo(List<Component> info) {
        if (this.isHighlyCorrosive()) {
            info.add(Component.translatable("fluid.trait.highly_corrosive").withStyle(ChatFormatting.GOLD));
        } else  {
            info.add(Component.translatable("fluid.trait.corrosive").withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("rating").value(rating);
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        this.rating = obj.get("rating").getAsInt();
    }
}
