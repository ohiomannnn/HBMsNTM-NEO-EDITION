package com.hbm.items.machine;

import com.hbm.items.EnumMultiItem;

public class BreedingRodItem extends EnumMultiItem {

    public BreedingRodItem(Properties properties) {
        super(properties, BreedingRodType.class, true, true);
    }

    public enum BreedingRodType {
        LITHIUM,
        TRITIUM,
        CO,
        CO60,
        TH232,
        THF,
        U235,
        NP237,
        U238,
        PU238,
        PU239,
        RGP,
        WASTE,

        //Required for prototype
        LEAD,
        URANIUM,

        RA226,
        AC227
    }
}
