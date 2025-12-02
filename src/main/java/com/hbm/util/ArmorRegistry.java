package com.hbm.util;

public class ArmorRegistry {

    public enum HazardClass {
        GAS_LUNG("hazard.gasChlorine"),                    // Also attacks eyes -> no half mask
        GAS_MONOXIDE("hazard.gasMonoxide"),                // Only affects lungs
        GAS_INERT("hazard.gasInert"),					     // SA
        PARTICLE_COARSE("hazard.particleCoarse"),		     // Only affects lungs
        PARTICLE_FINE("hazard.particleFine"),			     // Only affects lungs
        BACTERIA("hazard.bacteria"),					     // No half masks
        GAS_BLISTERING("hazard.corrosive"),				 // Corrosive substance, also attacks skin
        SAND("hazard.sand"),							     // Blinding sand particles
        LIGHT("hazard.light");                             // Blinding light

        public final String name;

        HazardClass(String name) { this.name = name; }
    }
}
