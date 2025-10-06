package com.hbm.config;

import net.neoforged.neoforge.common.ModConfigSpec;

// YES im making one class for every config
public class CommonConfig {

    // GENERAL (01)
    public final ModConfigSpec.BooleanValue ENABLE_MOTD;
    public final ModConfigSpec.BooleanValue ENABLE_EXTENDED_LOGGING;

    // EXPLOSIONS (06)
    public final ModConfigSpec.IntValue MK5;
    public final ModConfigSpec.IntValue FALLOUT_RANGE;
    public final ModConfigSpec.IntValue FALLOUT_DELAY;

    // RADIATION (13)
    public final ModConfigSpec.IntValue FOG_RAD;
    public final ModConfigSpec.IntValue FOG_RAD_CH;
    public final ModConfigSpec.DoubleValue HELL_RAD;
    public final ModConfigSpec.BooleanValue WORLD_RAD_EFFECTS;

    public final ModConfigSpec.BooleanValue ENABLE_CONTAMINATION;
    public final ModConfigSpec.BooleanValue ENABLE_CHUNK_RADS;

    // DANGEROUS DROPS (10)
    public final ModConfigSpec.BooleanValue DROP_CELL;
    public final ModConfigSpec.BooleanValue DROP_SINGULARITY;
    public final ModConfigSpec.BooleanValue DROP_STAR;
    public final ModConfigSpec.BooleanValue DROP_CRYSTAL;
    public final ModConfigSpec.BooleanValue DROP_DEAD_MANS_EXPLOSIVE;

    // HAZARDS (14)
    public final ModConfigSpec.BooleanValue DISABLE_ASBESTOS;
    public final ModConfigSpec.BooleanValue DISABLE_COAL;
    public final ModConfigSpec.BooleanValue DISABLE_HOT;
    public final ModConfigSpec.BooleanValue DISABLE_EXPLOSIVE;
    public final ModConfigSpec.BooleanValue DISABLE_HYDROACTIVE;
    public final ModConfigSpec.BooleanValue DISABLE_BLINDING;

    // BIOMES (17)
    public final ModConfigSpec.BooleanValue ENABLE_CRATER_BIOMES;
    public final ModConfigSpec.DoubleValue CRATER_RAD;
    public final ModConfigSpec.DoubleValue CRATER_INNER_RAD;
    public final ModConfigSpec.DoubleValue CRATER_OUTER_RAD;
    public final ModConfigSpec.DoubleValue CRATER_WATER_MULT;

    // 528
    public final ModConfigSpec.BooleanValue ENABLE_528;

    CommonConfig(ModConfigSpec.Builder builder) {

        /// GENERAL ///
        builder.push("general");

        ENABLE_MOTD = builder
                .comment("If enabled, shows the 'Loaded mod!' chat message as well as update notifications when joining a world.")
                .translation("hbmsntm.configuration.enableMOTD")
                .define("enableMOTD", false);
        ENABLE_EXTENDED_LOGGING = builder
                .comment("Logs uses of the detonator, nuclear explosions, missile launches, grenades, etc.")
                .translation("hbmsntm.configuration.enableExtendedLogging")
                .define("enableExtendedLogging", true);

        builder.pop();

        /// EXPLOSIONS ///
        builder.push("explosion");

        MK5 = builder
                .comment("Minimum amount of milliseconds per tick allocated for mk5 chunk processing.")
                .translation("hbmsntm.configuration.mk5BlastTime")
                .defineInRange("mk5BlastTime", 50, 0, Integer.MAX_VALUE);
        FALLOUT_RANGE = builder
                .comment("Radius of fallout area (base radius * value in percent)")
                .translation("hbmsntm.configuration.falloutRange")
                .defineInRange("falloutRange", 100, 0, Integer.MAX_VALUE);
        FALLOUT_DELAY = builder
                .comment("How many ticks to wait for the next fallout chunk computation")
                .translation("hbmsntm.configuration.falloutDelay")
                .defineInRange("falloutDelay", 4, 0, Integer.MAX_VALUE);

        builder.pop();

        /// RADIATION ///
        builder.push("radiation");

        HELL_RAD = builder
                .comment("RAD/s in the nether.")
                .translation("hbmsntm.configuration.hellRad")
                .defineInRange("hellRad", 0.1D, 0, Double.MAX_VALUE);
        FOG_RAD = builder
                .comment("Radiation in RADs required for fog to spawn.")
                .translation("hbmsntm.configuration.fogRad")
                .defineInRange("fogRad", 100, 0, Integer.MAX_VALUE);
        FOG_RAD_CH = builder
                .comment("1:n chance of fog spawning every second.")
                .translation("hbmsntm.configuration.fogRadChance")
                .defineInRange("fogRadChance", 20, 0, Integer.MAX_VALUE);
        WORLD_RAD_EFFECTS = builder
                .comment("Whether high radiation levels should perform changes in the world.")
                .translation("hbmsntm.configuration.worldRadEffects")
                .define("worldRadEffects", true);

        ENABLE_CONTAMINATION = builder
                .comment("Toggles player contamination (and negative effects from radiation poisoning).")
                .translation("hbmsntm.configuration.enableContamination")
                .define("enableContamination", true);
        ENABLE_CHUNK_RADS = builder
                .comment("Toggles the world radiation system (chunk radiation only, some blocks use an AoE!).")
                .translation("hbmsntm.configuration.enableChunkRads")
                .define("enableChunkRads", true);

        builder.pop();

        /// DANGEROUS DROPS ///
        builder.push("dangerous_drops");

        DROP_CELL = builder
                .comment("Whether antimatter cells should explode when dropped.")
                .translation("hbmsntm.configuration.dropCell")
                .define("dropCell", true);
        DROP_SINGULARITY = builder
                .comment("Whether singularities and black holes should spawn when dropped.")
                .translation("hbmsntm.configuration.dropSing")
                .define("dropSing", true);
        DROP_STAR = builder
                .comment("Whether rigged star blaster cells should explode when dropped.")
                .translation("hbmsntm.configuration.dropStar")
                .define("dropStar", true);
        DROP_CRYSTAL = builder
                .comment("Whether xen crystals should move blocks when dropped.")
                .translation("hbmsntm.configuration.dropCrys")
                .define("dropCrys", true);
        DROP_DEAD_MANS_EXPLOSIVE = builder
                .comment("Whether dead man's explosives should explode when dropped.")
                .translation("hbmsntm.configuration.dropDead")
                .define("dropDead", true);

        builder.pop();

        /// HAZARDS ///
        builder.push("hazards");

        DISABLE_ASBESTOS = builder
                .comment("When turned on, all asbestos hazards are disabled.")
                .translation("hbmsntm.configuration.disableAsbestos")
                .define("disableAsbestos", false);
        DISABLE_COAL = builder
                .comment("When turned on, all coal dust hazards are disabled.")
                .translation("hbmsntm.configuration.disableCoaldust")
                .define("disableCoaldust", false);
        DISABLE_HOT = builder
                .comment("When turned on, all hot hazards are disabled.")
                .translation("hbmsntm.configuration.disableHot")
                .define("disableHot", false);
        DISABLE_EXPLOSIVE = builder
                .comment("When turned on, all explosive hazards are disabled.")
                .translation("hbmsntm.configuration.disableExplosive")
                .define("disableExplosive", false);
        DISABLE_HYDROACTIVE = builder
                .comment("When turned on, all hydroactive hazards are disabled.")
                .translation("hbmsntm.configuration.disableHydroactive")
                .define("disableHydroactive", false);
        DISABLE_BLINDING = builder
                .comment("When turned on, all blinding hazards are disabled.")
                .translation("hbmsntm.configuration.disableBlinding")
                .define("disableBlinding", false);

        builder.pop();

        /// BIOMES ///
        builder.push("biomes");

        ENABLE_CRATER_BIOMES = builder
                .comment("Enables the biome change caused by nuclear explosions")
                .translation("hbmsntm.configuration.craterBiome")
                .define("craterBiome", true);
        CRATER_RAD = builder
                .comment("RAD/s for the crater biome")
                .translation("hbmsntm.configuration.craterBiomeRad")
                .defineInRange("craterBiomeRad", 5D, 0D, Double.MAX_VALUE);
        CRATER_INNER_RAD = builder
                .comment("RAD/s for the inner crater biome")
                .translation("hbmsntm.configuration.craterBiomeInnerRad")
                .defineInRange("craterBiomeInnerRad", 25D, 0D, Double.MAX_VALUE);
        CRATER_OUTER_RAD = builder
                .comment("RAD/s for the outer crater biome")
                .translation("hbmsntm.configuration.craterBiomeOuterRad")
                .defineInRange("craterBiomeOuterRad", 0.5D, 0D, Double.MAX_VALUE);
        CRATER_WATER_MULT = builder
                .comment("Multiplier for RAD/s in crater biomes when in water")
                .translation("hbmsntm.configuration.craterBiomeWaterMultiplier")
                .defineInRange("craterBiomeWaterMultiplier", 5D, 0D, Double.MAX_VALUE);

        builder.pop();

        /// 528 ///
        builder.comment("528 Mode: Please proceed with caution!");
        builder.comment("528-Modus: Lassen Sie Vorsicht walten!");
        builder.comment("способ-528: действовать с осторожностью!");
        builder.push("528");

        ENABLE_528 = builder
                .comment("The central toggle for 528 mode.")
                .translation("hbmsntm.configuration.enable528")
                .define("enable528", false);

        builder.pop();
    }
}
