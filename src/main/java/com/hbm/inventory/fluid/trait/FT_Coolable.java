package com.hbm.inventory.fluid.trait;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.fluid.FluidType;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class FT_Coolable extends FluidTrait {

    private final List<CoolingStep> steps = new ArrayList<>();
    private final HashMap<CoolingType, Double> efficiency = new HashMap<>();

    public FluidType coolsTo;
    public int amountReq;
    public int heatEnergy;
    public int amountProduced;

    public FT_Coolable addStep(int heat, int req, FluidType type, int prod) {
        this.steps.add(new CoolingStep(req, heat, type, prod));
        if(this.coolsTo == null) {
            this.amountReq = req;
            this.heatEnergy = heat;
            this.coolsTo = type;
            this.amountProduced = prod;
        }
        return this;
    }

    public FT_Coolable setEff(CoolingType type, double eff) {
        this.efficiency.put(type, eff);
        return this;
    }

    public double getEfficiency(CoolingType type) {
        return this.efficiency.getOrDefault(type, 0D);
    }

    public CoolingStep getFirstStep() {
        return this.steps.isEmpty() ? null : this.steps.get(0);
    }

    @Override
    public void addInfo(List<Component> info) {
        // intentionally left simple; the fluid tooltip already explains the fluid itself
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("heatEnergy").value(this.heatEnergy);
        writer.name("amountReq").value(this.amountReq);
        writer.name("amountProduced").value(this.amountProduced);
        writer.name("coolsTo").value(this.coolsTo == null ? "" : this.coolsTo.getInternalName());
        writer.name("efficiency").beginObject();
        for(Entry<CoolingType, Double> entry : this.efficiency.entrySet()) {
            writer.name(entry.getKey().name()).value(entry.getValue());
        }
        writer.endObject();
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        this.heatEnergy = obj.has("heatEnergy") ? obj.get("heatEnergy").getAsInt() : this.heatEnergy;
        this.amountReq = obj.has("amountReq") ? obj.get("amountReq").getAsInt() : this.amountReq;
        this.amountProduced = obj.has("amountProduced") ? obj.get("amountProduced").getAsInt() : this.amountProduced;
    }

    public enum CoolingType {
        HEATEXCHANGER;

        public static final CoolingType[] VALUES = values();
    }

    public record CoolingStep(int amountReq, int heatReq, FluidType typeProduced, int amountProduced) { }
}
