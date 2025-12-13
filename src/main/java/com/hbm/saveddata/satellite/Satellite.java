package com.hbm.saveddata.satellite;

import com.hbm.items.ModItems;
import com.hbm.saveddata.SatelliteSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Satellite {
    public static final List<Class<? extends Satellite>> SATELLITES = new ArrayList<>();
    public static final HashMap<Item, Class<? extends Satellite>> ITEM_TO_CLASS = new HashMap<>();

    public enum InterfaceActions {
        HAS_MAP,		//lets the interface display loaded chunks
        CAN_CLICK,		//enables onClick events
        SHOW_COORDS,	//enables coordinates as a mouse tooltip
        HAS_RADAR,		//lets the interface display loaded entities
        HAS_ORES		//like HAS_MAP but only shows ores
    }

    public enum CoordActions {
        HAS_Y		//enables the Y-coord field which is disabled by default
    }

    public enum Interfaces {
        NONE,		//does not interact with any sat interface (i.e. asteroid miners)
        SAT_PANEL,	//allows to interact with the sat interface panel (for graphical applications)
        SAT_COORD	//allows to interact with the sat coord remote (for teleportation or other coord related actions)
    }

    public List<InterfaceActions> ifaceAcs = new ArrayList<>();
    public List<CoordActions> coordAcs = new ArrayList<>();
    public Interfaces satIface = Interfaces.NONE;

    public static void register() {
        registerSatellite(SatelliteLaser.class, ModItems.SATELLITE_LASER.get());
        registerSatellite(SatelliteRadar.class, ModItems.SATELLITE_RADAR.get());
    }

    /**
     * Register satellite.
     * @param sat - Satellite class
     * @param item - Satellite item (which will be placed in a rocket)
     */
    public static void registerSatellite(Class<? extends Satellite> sat, Item item) {
        if (!ITEM_TO_CLASS.containsKey(item) && !ITEM_TO_CLASS.containsValue(sat)) {
            SATELLITES.add(sat);
            ITEM_TO_CLASS.put(item, sat);
        }
    }

    public static void orbit(ServerLevel serverLevel, int id, int freq, double x, double y, double z) {
        Satellite sat = create(id);

        if (sat != null) {
            SatelliteSavedData data = SatelliteSavedData.get(serverLevel);
            data.sats.put(freq, sat);
            sat.onOrbit(serverLevel, x, y, z);
            data.setDirty();
        }
    }

    public static Satellite create(int id) {
        Satellite sat = null;

        try {
            Class<? extends Satellite> c = SATELLITES.get(id);
            sat = c.newInstance();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return sat;
    }

    public static int getIDFromItem(Item item) {
        Class<? extends Satellite> sat = ITEM_TO_CLASS.get(item);

        return SATELLITES.indexOf(sat);
    }

    public int getID() {
        return SATELLITES.indexOf(this.getClass());
    }

    public void writeToNBT(CompoundTag tag) { }

    public void readFromNBT(CompoundTag tag) { }

    /**
     * Called when the satellite reaches space, used to trigger achievements and other funny stuff.
     * @param x posX of the rocket
     * @param y ditto
     * @param z ditto
     */
    public void onOrbit(Level level, double x, double y, double z) { }

    /**
     * Called by the sat interface when clicking on the screen
     * @param x the x-coordinate translated from the on-screen coords to actual world coordinates
     * @param z ditto
     */
    public void onClick(Level level, int x, int z) { }

    /**
     * Called by the coord sat interface
     * @param pos the specified block position
     */
    public void onCoordAction(Level level, Player player, BlockPos pos) { }
}
