package com.hbm.inventory.fluid.trait;

import com.google.common.collect.HashBiMap;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FluidTrait {
    public static List<Class<? extends FluidTrait>> traitList = new ArrayList<>();
    public static HashBiMap<String, Class<? extends FluidTrait>> traitNameMap = HashBiMap.create();

    private static void registerTrait(String name, Class<? extends FluidTrait> clazz) {
        traitNameMap.put(name, clazz);
        traitList.add(clazz);
    }

    /** Important information that should always be displayed */
    public void addInfo(List<Component> info) { }
    /* General names of simple traits which are displayed when holding shift */
    public void addInfoHidden(List<Component> info) { }

    //public void onFluidRelease(Level level, int x, int y, int z, FluidTank tank, int overflowAmount, FluidReleaseType type) { }

    public void serializeJSON(JsonWriter writer) throws IOException { }
    public void deserializeJSON(JsonObject obj) { }

    public enum FluidReleaseType {
        VOID,	//if fluid is deleted entirely, shouldn't be used
        BURN,	//if fluid is burned or combusted
        SPILL	//if fluid is spilled via leakage or the container breaking
    }
}
