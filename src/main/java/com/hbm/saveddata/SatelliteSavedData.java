package com.hbm.saveddata;

import com.hbm.saveddata.satellite.Satellite;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

public class SatelliteSavedData extends SavedData {

    public static Factory<SatelliteSavedData> factory() {
        return new Factory<>(
                SatelliteSavedData::new, SatelliteSavedData::load
        );
    }

    public final Map<Integer, Satellite> satellites = new HashMap<>();
    
    public SatelliteSavedData() {
        this.setDirty();
    }

    public boolean isFreqTaken(int freq) {
        return getSatFromFreq(freq) != null;
    }

    public Satellite getSatFromFreq(int freq) {
        return satellites.get(freq);
    }

    public static SatelliteSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        SatelliteSavedData data = new SatelliteSavedData();

        int satCount = tag.getInt("SatellitesCount");

        for(int i = 0; i < satCount; i++) {
            int id = tag.getInt("SatId_" + i);
            int freq = tag.getInt("SatData_" + i);

            CompoundTag satTag = tag.getCompound("SatFreq_" + i);

            Satellite sat = Satellite.create(id);
            sat.readAdditional(satTag);

            data.satellites.put(freq, sat);
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("SatellitesCount", satellites.size());

        int i = 0;
        for(Map.Entry<Integer, Satellite> entry : satellites.entrySet()) {
            Satellite sat = entry.getValue();

            CompoundTag satTag = new CompoundTag();
            sat.saveAdditional(satTag);

            tag.putInt("SatId_" + i, sat.getID());
            tag.put("SatData_" + i, satTag);
            tag.putInt("SatFreq_" + i, entry.getKey());
            i++;
        }

        return tag;
    }

    public static SatelliteSavedData getData(ServerLevel serverLevel) {
        return serverLevel.getDataStorage().computeIfAbsent(factory(), "satellites");
    }
}
