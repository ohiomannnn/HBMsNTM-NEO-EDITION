package com.hbm.inventory.material;

import java.util.ArrayList;
import java.util.List;

public class MaterialShapes {

    public static final List<MaterialShapes> allShapes = new ArrayList<>();

    public static final MaterialShapes ANY = new MaterialShapes(0, "any").noAutogen();
    public static final MaterialShapes ONLY_ORE = new MaterialShapes(0, "ore").noAutogen();
    public static final MaterialShapes ORE = new MaterialShapes(0, "ore").noAutogen();

    public static final MaterialShapes QUANTUM = new MaterialShapes(1); // 1/72 of an ingot, allows the ingot to be divisible through 2, 4, 6, 8, 9, 12, 24 and 36
    public static final MaterialShapes NUGGET = new MaterialShapes(8, "nuggets", "tiny");
    public static final MaterialShapes TINY = new MaterialShapes(8, "tiny").noAutogen();
    public static final MaterialShapes FRAGMENT = new MaterialShapes(8, "bedrockorefragment");
    public static final MaterialShapes DUSTTINY = new MaterialShapes(NUGGET.quantity, "tiny_dusts");
    public static final MaterialShapes WIRE = new MaterialShapes(9, "fine_wires");
    public static final MaterialShapes BOLT = new MaterialShapes(9, "bolts");
    public static final MaterialShapes BILLET = new MaterialShapes(NUGGET.quantity * 6, "billets");
    public static final MaterialShapes INGOT = new MaterialShapes(NUGGET.quantity * 9, "ingots");
    public static final MaterialShapes GEM = new MaterialShapes(INGOT.quantity, "gems");
    public static final MaterialShapes CRYSTAL = new MaterialShapes(INGOT.quantity, "crystals");
    public static final MaterialShapes DUST = new MaterialShapes(INGOT.quantity, "dusts");
    public static final MaterialShapes DENSEWIRE = new MaterialShapes(INGOT.quantity, "dense_wires");
    public static final MaterialShapes PLATE = new MaterialShapes(INGOT.quantity, "plates");
    public static final MaterialShapes CASTPLATE = new MaterialShapes(INGOT.quantity * 3, "triple_plates");
    public static final MaterialShapes WELDEDPLATE = new MaterialShapes(INGOT.quantity * 6, "sextuple_plates");
    public static final MaterialShapes SHELL = new MaterialShapes(INGOT.quantity * 4, "shells");
    public static final MaterialShapes PIPE = new MaterialShapes(INGOT.quantity * 3, "pipes");
    public static final MaterialShapes QUART = new MaterialShapes(162);
    public static final MaterialShapes BLOCK = new MaterialShapes(INGOT.quantity * 9, "blocks");

    public static final MaterialShapes LIGHTBARREL =	new MaterialShapes(INGOT.quantity * 3, "light_barrels");
    public static final MaterialShapes HEAVYBARREL =	new MaterialShapes(INGOT.quantity * 6, "heavy_barrels");
    public static final MaterialShapes LIGHTRECEIVER =	new MaterialShapes(INGOT.quantity * 4, "light_receivers");
    public static final MaterialShapes HEAVYRECEIVER =	new MaterialShapes(INGOT.quantity * 9, "heavy_receivers");
    public static final MaterialShapes MECHANISM =		new MaterialShapes(INGOT.quantity * 4, "gun_mechanisms");
    public static final MaterialShapes STOCK =			new MaterialShapes(INGOT.quantity * 4, "gun_stocks");
    public static final MaterialShapes GRIP =			new MaterialShapes(INGOT.quantity * 2, "gun_grips");

    public boolean noAutogen = false;
    private final int quantity;
    public final String[] prefixes;

    private MaterialShapes(int quantity, String... prefixes) {
        this.quantity = quantity;
        this.prefixes = prefixes;

        for(String prefix : prefixes) {
            Mats.prefixByName.put(prefix, this);
        }

        allShapes.add(this);
    }

    /** Disables recipe autogen for special cases like compatibility prefixes (TINY, ORENETHER), technical prefixes (ANY) or prefixes that have to be handled manually (ORE) */
    public MaterialShapes noAutogen() {
        this.noAutogen = true;
        return this;
    }

    public int q(int amount) {
        return this.quantity * amount;
    }

    public int q(int unitsUsed, int itemsProduced) { //eg rails: INOGT.q(6, 16) since the recipe uses 6 iron ingots producing 16 individual rail blocks
        return this.quantity * unitsUsed / itemsProduced;
    }

    public String name() {
        return (prefixes != null && prefixes.length > 0) ? prefixes[0] : "unknown";
    }

    public String make(NTMMaterial mat) {
        return this.name() + mat.names[0];
    }
}
