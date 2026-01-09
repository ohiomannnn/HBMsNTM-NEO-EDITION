package com.hbm.config;

import net.neoforged.neoforge.common.ModConfigSpec;

// YES im making one class for every config
public class CommonConfig {

    // GENERAL (01)
    public final ModConfigSpec.BooleanValue ENABLE_MOTD;
    public final ModConfigSpec.BooleanValue ENABLE_EXTENDED_LOGGING;
    public final ModConfigSpec.BooleanValue ENABLE_BOMBER_SHORT_MODE;
    public final ModConfigSpec.BooleanValue ENABLE_SILENT_COMPSTACK_ERRORS;

    // NUKES (03)
    public final ModConfigSpec.IntValue GADGET_RADIUS;
    public final ModConfigSpec.IntValue BOY_RADIUS;
    public final ModConfigSpec.IntValue MAN_RADIUS;
    public final ModConfigSpec.IntValue MIKE_RADIUS;
    public final ModConfigSpec.IntValue TSAR_RADIUS;
    public final ModConfigSpec.IntValue PROTOTYPE_RADIUS;
    public final ModConfigSpec.IntValue FLEIJA_RADIUS;
    public final ModConfigSpec.IntValue SOLINIUM_RADIUS;
    public final ModConfigSpec.IntValue N2_RADIUS;
    public final ModConfigSpec.IntValue MISSLE_RADIUS;
    public final ModConfigSpec.IntValue MIRV_RADIUS;
    public final ModConfigSpec.IntValue FATMAN_RADIUS;
    public final ModConfigSpec.IntValue NUKA_RADIUS;
    public final ModConfigSpec.IntValue ASCHRAB_RADIUS;

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

    // TOOLS (11)
    public final ModConfigSpec.IntValue RECURSION_DEPTH;
    public final ModConfigSpec.BooleanValue RECURSION_STONE;
    public final ModConfigSpec.BooleanValue RECURSION_NETHERRACK;
    public final ModConfigSpec.BooleanValue ABILITY_HAMMER;
    public final ModConfigSpec.BooleanValue ABILITY_VEIN;
    public final ModConfigSpec.BooleanValue ABILITY_LUCK;
    public final ModConfigSpec.BooleanValue ABILITY_SILK;
    public final ModConfigSpec.BooleanValue ABILITY_FURNACE;
    public final ModConfigSpec.BooleanValue ABILITY_SHREDDER;
    public final ModConfigSpec.BooleanValue ABILITY_CENTRIFUGE;
    public final ModConfigSpec.BooleanValue ABILITY_CRYSTALLIZER;
    public final ModConfigSpec.BooleanValue ABILITY_MERCURY;
    public final ModConfigSpec.BooleanValue ABILITY_EXPLOSION;

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
                .define("enableMOTD", true);
        ENABLE_EXTENDED_LOGGING = builder
                .comment("Logs uses of the detonator, nuclear explosions, missile launches, grenades, etc.")
                .translation("hbmsntm.configuration.enableExtendedLogging")
                .define("enableExtendedLogging", true);
        ENABLE_BOMBER_SHORT_MODE = builder
                .comment("Has bomber planes spawn in closer to the target for use with smaller render distances.")
                .translation("hbmsntm.configuration.enableBomberShortMode")
                .define("enableBomberShortMode", false);
        ENABLE_SILENT_COMPSTACK_ERRORS = builder
                .comment("Enabling this will disable log spam created by unregistered items in ComparableStack instances.")
                .translation("hbmsntm.configuration.enableSilentCompStackErrors")
                .define("enableSilentCompStackErrors", false);

        builder.pop();

        /// NUKES ///
        builder.push("nukes");

        GADGET_RADIUS = builder
                .comment("Radius of the Gadget.")
                .translation("hbmsntm.configuration.gadgetRadius")
                .defineInRange("gadgetRadius", 150, 0, Integer.MAX_VALUE);
        BOY_RADIUS = builder
                .comment("Radius of Little Boy.")
                .translation("hbmsntm.configuration.boyRadius")
                .defineInRange("boyRadius", 120, 0, Integer.MAX_VALUE);
        MAN_RADIUS = builder
                .comment("Radius of Fat Man.")
                .translation("hbmsntm.configuration.manRadius")
                .defineInRange("manRadius", 175, 0, Integer.MAX_VALUE);
        MIKE_RADIUS = builder
                .comment("Radius of Ivy Mike.")
                .translation("hbmsntm.configuration.mikeRadius")
                .defineInRange("mikeRadius", 250, 0, Integer.MAX_VALUE);
        TSAR_RADIUS = builder
                .comment("Radius of Tsar Bomba.")
                .translation("hbmsntm.configuration.tsarRadius")
                .defineInRange("tsarRadius", 500, 0, Integer.MAX_VALUE);
        PROTOTYPE_RADIUS = builder
                .comment("Radius of the Prototype.")
                .translation("hbmsntm.configuration.prototypeRadius")
                .defineInRange("prototypeRadius", 150, 0, Integer.MAX_VALUE);
        FLEIJA_RADIUS = builder
                .comment("Radius of F.L.E.I.J.A.")
                .translation("hbmsntm.configuration.fleijaRadius")
                .defineInRange("fleijaRadius", 50, 0, Integer.MAX_VALUE);
        SOLINIUM_RADIUS = builder
                .comment("Radius of the blue rinse.")
                .translation("hbmsntm.configuration.soliniumRadius")
                .defineInRange("soliniumRadius", 150, 0, Integer.MAX_VALUE);
        N2_RADIUS = builder
                .comment("Radius of the N2 mine.")
                .translation("hbmsntm.configuration.n2Radius")
                .defineInRange("n2Radius", 200, 0, Integer.MAX_VALUE);
        MISSLE_RADIUS = builder
                .comment("Radius of the nuclear missile")
                .translation("hbmsntm.configuration.missileRadius")
                .defineInRange("missileRadius", 100, 0, Integer.MAX_VALUE);
        MIRV_RADIUS = builder
                .comment("Radius of a MIRV.")
                .translation("hbmsntm.configuration.mirvRadius")
                .defineInRange("mirvRadius", 100, 0, Integer.MAX_VALUE);
        FATMAN_RADIUS = builder
                .comment("Radius of the Fatman Launcher.")
                .translation("hbmsntm.configuration.fatmanRadius")
                .defineInRange("fatmanRadius", 35, 0, Integer.MAX_VALUE);
        NUKA_RADIUS = builder
                .comment("Radius of the nuka grenade.")
                .translation("hbmsntm.configuration.nukaRadius")
                .defineInRange("nukaRadius", 25, 0, Integer.MAX_VALUE);
        ASCHRAB_RADIUS = builder
                .comment("Radius of dropped anti schrabidium.")
                .translation("hbmsntm.configuration.aSchrabRadius")
                .defineInRange("aSchrabRadius", 20, 0, Integer.MAX_VALUE);

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

        /// TOOLS ///
        builder.push("tools");

        RECURSION_DEPTH = builder
                .comment("Limits veinminer's recursive function")
                .translation("hbmsntm.configuration.recursionDepth")
                .defineInRange("recursionDepth", 1000, 0, Integer.MAX_VALUE);
        RECURSION_STONE = builder
                .comment("Determines whether veinminer can break stone")
                .translation("hbmsntm.configuration.recursionStone")
                .define("recursionStone", false);
        RECURSION_NETHERRACK = builder
                .comment("Determines whether veinminer can break netherrack")
                .translation("hbmsntm.configuration.recursionNetherrack")
                .define("recursionNetherrack", false);
        ABILITY_HAMMER = builder
                .comment("Allows AoE ability")
                .translation("hbmsntm.configuration.hammerAbility")
                .define("hammerAbility", true);
        ABILITY_VEIN = builder
                .comment("Allows veinminer ability")
                .translation("hbmsntm.configuration.abilityVein")
                .define("abilityVein", true);
        ABILITY_LUCK = builder
                .comment("Allow luck (fortune) ability")
                .translation("hbmsntm.configuration.abilityLuck")
                .define("abilityLuck", true);
        ABILITY_SILK = builder
                .comment("Allow silk touch ability")
                .translation("hbmsntm.configuration.abilitySilk")
                .define("abilitySilk", true);
        ABILITY_FURNACE = builder
                .comment("Allow auto-smelter ability")
                .translation("hbmsntm.configuration.abilityFurnace")
                .define("abilityFurnace", true);
        ABILITY_SHREDDER = builder
                .comment("Allow auto-shredder ability")
                .translation("hbmsntm.configuration.abilityShredder")
                .define("abilityShredder", true);
        ABILITY_CENTRIFUGE = builder
                .comment("Allow auto-centrifuge ability")
                .translation("hbmsntm.configuration.abilityCentrifuge")
                .define("abilityCentrifuge", true);
        ABILITY_CRYSTALLIZER = builder
                .comment("Allow auto-crystallizer ability")
                .translation("hbmsntm.configuration.abilityCrystallizer")
                .define("abilityCrystallizer", true);
        ABILITY_MERCURY = builder
                .comment("Allow mercury touch ability (digging redstone gives mercury)")
                .translation("hbmsntm.configuration.abilityMercury")
                .define("abilityMercury", true);
        ABILITY_EXPLOSION = builder
                .comment("Allow explosion ability")
                .translation("hbmsntm.configuration.abilityExplosion")
                .define("abilityExplosion", true);

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
