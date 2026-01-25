package com.hbm.inventory.fluid.trait;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.util.BobMathUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.List;

public class FT_Flammable extends FluidTrait {
    /** How much heat energy (usually translates into HE 1:1) 1000mB hold */
    private long energy;

    public FT_Flammable() { }

    public FT_Flammable(long energy) {
        this.energy = energy;
    }

    public long getHeatEnergy() {
        return this.energy;
    }

    @Override
    public void addInfo(List<Component> info) {

        info.add(Component.translatable("fluid.trait.flammable").withStyle(ChatFormatting.YELLOW));

        if (energy > 0) info.add(Component.translatable("fluid.trait.flammable_provides", ChatFormatting.RED + BobMathUtil.getShortNumber(energy)).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("energy").value(energy);
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        this.energy = obj.get("energy").getAsLong();
    }
}
