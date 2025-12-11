package com.hbm.saveddata;

import com.hbm.saveddata.satellite.Satellite;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

public class SatelliteSavedData extends SavedData {

    public static final String NAME = "satellites";

    public final Map<Integer, Satellite> sats = new HashMap<>();

    public static SavedData.Factory<SatelliteSavedData> factory() {
        return new SavedData.Factory<>(SatelliteSavedData::new, SatelliteSavedData::load);
    }

    private SatelliteSavedData() {
        this.setDirty();
    }

    public static SatelliteSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        SatelliteSavedData data = new SatelliteSavedData();

        int satCount = tag.getInt("satCount");

        for (int i = 0; i < satCount; i++) {
            int id = tag.getInt("sat_id_" + i);
            int freq = tag.getInt("sat_freq_" + i);

            CompoundTag satTag = tag.getCompound("sat_data_" + i);

            Satellite sat = Satellite.create(id);
            sat.readFromNBT(satTag);

            data.sats.put(freq, sat);
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("satCount", sats.size());

        int i = 0;
        for (Map.Entry<Integer, Satellite> entry : sats.entrySet()) {
            Satellite sat = entry.getValue();

            CompoundTag satTag = new CompoundTag();
            sat.writeToNBT(satTag);

            tag.putInt("sat_id_" + i, sat.getID());
            tag.put("sat_data_" + i, satTag);
            tag.putInt("sat_freq_" + i, entry.getKey());
            i++;
        }

        return tag;
    }

    public boolean isFreqTaken(int freq) {
        return sats.containsKey(freq);
    }

    public Satellite getSatFromFreq(int freq) {
        return sats.get(freq);
    }

    public void putSatellite(int freq, Satellite sat) {
        sats.put(freq, sat);
        this.setDirty();
    }

    public void removeSatellite(int freq) {
        if (sats.remove(freq) != null) {
            this.setDirty();
        }
    }

    public static SatelliteSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(factory(), NAME);
    }
}
