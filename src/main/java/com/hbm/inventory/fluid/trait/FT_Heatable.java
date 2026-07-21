package com.hbm.inventory.fluid.trait;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class FT_Heatable extends FluidTrait {

    protected final List<HeatingStep> steps = new ArrayList<>();
    protected final HashMap<HeatingType, Double> efficiency = new HashMap<>();

    /** Add in ascending order, lowest heat required goes first! */
    public FT_Heatable addStep(int heat, int req, FluidType type, int prod) {
        this.steps.add(new HeatingStep(req, heat, type, prod));
        return this;
    }

    /** Sets efficiency for different types of heating, main difference is with water */
    public FT_Heatable setEff(HeatingType type, double eff) {
        this.efficiency.put(type, eff);
        return this;
    }

    public double getEfficiency(HeatingType type) {
        Double eff = this.efficiency.get(type);
        return eff != null ? eff : 0.0D;
    }

    public boolean hasSteps() {
        return !this.steps.isEmpty();
    }

    public HeatingStep getFirstStep() {
        return this.steps.get(0);
    }

    @Override
    public void addInfoHidden(List<Component> info) {
        if(this.steps.isEmpty()) return;

        info.add(Component.translatable("trait.thermalcap", this.getFirstStep().heatReq).withStyle(ChatFormatting.RED));

        for(HeatingType type : HeatingType.VALUES) {
            double eff = this.getEfficiency(type);
            if(eff > 0) {
                info.add(Component.translatable(
                        "trait.chefficiency",
                        Component.translatable(type.translationKey),
                        (int)(eff * 100D)
                ).withStyle(ChatFormatting.YELLOW));
            }
        }
    }

    public static class HeatingStep {
        public final int amountReq;
        public final int heatReq;
        public final FluidType typeProduced;
        public final int amountProduced;

        public HeatingStep(int req, int heat, FluidType type, int prod) {
            this.amountReq = req;
            this.heatReq = heat;
            this.typeProduced = type;
            this.amountProduced = prod;
        }
    }

    public enum HeatingType {
        BOILER("trait.htype.boiler"),
        HEATEXCHANGER("trait.htype.heatexch"),
        PWR("trait.htype.pwr"),
        ICF("trait.htype.icf"),
        PA("trait.htype.pa");

        public static final HeatingType[] VALUES = values();
        public final String translationKey;

        HeatingType(String translationKey) {
            this.translationKey = translationKey;
        }
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("steps").beginArray();

        for(HeatingStep step : this.steps) {
            writer.beginObject();
            writer.name("typeProduced").value(step.typeProduced.getInternalName());
            writer.name("amountReq").value(step.amountReq);
            writer.name("amountProd").value(step.amountProduced);
            writer.name("heatReq").value(step.heatReq);
            writer.endObject();
        }

        writer.endArray();

        for(Entry<HeatingType, Double> entry : this.efficiency.entrySet()) {
            writer.name(entry.getKey().name()).value(entry.getValue());
        }
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        JsonArray steps = obj.get("steps").getAsJsonArray();

        for(int i = 0; i < steps.size(); i++) {
            JsonObject step = steps.get(i).getAsJsonObject();
            this.steps.add(new HeatingStep(
                    step.get("amountReq").getAsInt(),
                    step.get("heatReq").getAsInt(),
                    Fluids.fromName(step.get("typeProduced").getAsString()),
                    step.get("amountProd").getAsInt()
            ));
        }

        for(HeatingType type : HeatingType.VALUES) {
            if(obj.has(type.name())) {
                this.efficiency.put(type, obj.get(type.name()).getAsDouble());
            }
        }
    }
}
