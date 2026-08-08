package com.hbm.inventory.fluid.trait;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.handler.pollution.PollutionHandler;
import com.hbm.handler.pollution.PollutionHandler.PollutionType;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.util.ChatBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class FT_Polluting extends FluidTrait {

    //original draft had both of them inside a hashmap for the release type but honestly handling hash maps in hash maps adds more complexity than it removes
    public Map<PollutionType, Float> releaseMap = new HashMap<>();
    public Map<PollutionType, Float> burnMap = new HashMap<>();

    public FT_Polluting release(PollutionType type, float amount) {
        releaseMap.put(type, amount);
        return this;
    }

    public FT_Polluting burn(PollutionType type, float amount) {
        burnMap.put(type, amount);
        return this;
    }

    @Override
    public void addInfo(List<Component> info) {
        info.add(ChatBuilder.start("[").nextTranslation("fluids.trait.polluting").next("]").colorAll(ChatFormatting.GOLD).flush());
    }

    @Override
    public void addInfoHidden(List<Component> info) {

        if(!this.releaseMap.isEmpty()) {
            info.add(ChatBuilder.startTranslation("fluids.trait.spilled").next(":").colorAll(ChatFormatting.GREEN).flush());
            for(Entry<PollutionType, Float> entry : releaseMap.entrySet()) info.add(ChatBuilder.start(" - ").next(entry.getValue().toString() + " ").nextTranslation("pollution.trait." + entry.getKey().name().toLowerCase(Locale.US)).next(" ").nextTranslation("fluid.trait.perMB").colorAll(ChatFormatting.GREEN).flush());
        }

        if(!this.burnMap.isEmpty()) {
            info.add(ChatBuilder.startTranslation("fluids.trait.burned").next(":").colorAll(ChatFormatting.RED).flush());
            for(Entry<PollutionType, Float> entry : burnMap.entrySet()) info.add(ChatBuilder.start(" - ").next(entry.getValue().toString() + " ").nextTranslation("pollution.trait." + entry.getKey().name().toLowerCase(Locale.US)).next(" ").nextTranslation("fluid.trait.perMB").colorAll(ChatFormatting.RED).flush());
        }
    }

    @Override
    public void onFluidRelease(Level level, BlockPos pos, FluidTank tank, int overflowAmount, FluidReleaseType type) {
        if(type == FluidReleaseType.SPILL) for(Entry<PollutionType, Float> entry : releaseMap.entrySet()) PollutionHandler.incrementPollution(level, pos, entry.getKey(), entry.getValue());
        if(type == FluidReleaseType.BURN) for(Entry<PollutionType, Float> entry : burnMap.entrySet()) PollutionHandler.incrementPollution(level, pos, entry.getKey(), entry.getValue());
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("release").beginObject();
        for(Entry<PollutionType, Float> entry : releaseMap.entrySet()) {
            writer.name(entry.getKey().name()).value(entry.getValue());
        }
        writer.endObject();
        writer.name("burn").beginObject();
        for(Entry<PollutionType, Float> entry : burnMap.entrySet()) {
            writer.name(entry.getKey().name()).value(entry.getValue());
        }
        writer.endObject();
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        if(obj.has("release")) {
            JsonObject release = obj.get("release").getAsJsonObject();
            for(PollutionType type : PollutionType.values()) {
                if(release.has(type.name())) {
                    releaseMap.put(type, release.get(type.name()).getAsFloat());
                }
            }
        }
        if(obj.has("burn")) {
            JsonObject release = obj.get("burn").getAsJsonObject();
            for(PollutionType type : PollutionType.values()) {
                if(release.has(type.name())) {
                    burnMap.put(type, release.get(type.name()).getAsFloat());
                }
            }
        }
    }

    public static void pollute(Level level, BlockPos pos, FluidType type, FluidReleaseType release, float mB) {
        FT_Polluting trait = type.getTrait(FT_Polluting.class);
        if(trait == null) return;
        if(release == FluidReleaseType.VOID) return;

        Map<PollutionType, Float> map = release == FluidReleaseType.BURN ? trait.burnMap : trait.releaseMap;

        for(Entry<PollutionType, Float> entry : map.entrySet()) {
            PollutionHandler.incrementPollution(level, pos, entry.getKey(), entry.getValue() * mB);
        }
    }
}
