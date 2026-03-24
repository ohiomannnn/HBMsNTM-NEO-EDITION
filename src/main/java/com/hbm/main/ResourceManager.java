package com.hbm.main;

import com.hbm.NuclearTechMod;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.IModelCustom;
import net.minecraft.resources.ResourceLocation;

public class ResourceManager {
    public static final ResourceLocation EMPTY = ResourceLocation.withDefaultNamespace("missingno");

    public static final ResourceLocation MINE_AP_STONE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/mine_ap.png");
    public static final ResourceLocation MINE_AP_SNOW_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/mine_ap_snow.png");
    public static final ResourceLocation MINE_AP_GRASS_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/mine_ap_grass.png");
    public static final ResourceLocation MINE_AP_DESERT_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/mine_ap_desert.png");
    public static final ResourceLocation MINE_HE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/mine_he.png");
    public static final ResourceLocation MINE_SHRAPNEL_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/mine_shrap.png");
    public static final ResourceLocation MINE_NAVAL_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/mine_naval.png");
    public static final ResourceLocation MINE_FAT_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/mine_fat.png");

    public static final ResourceLocation NUKE_GADGET_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/nuke_gadget.png");
    public static final ResourceLocation NUKE_LITTLE_BOY_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/nuke_little_boy.png");
    public static final ResourceLocation NUKE_FAT_MAN_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/nuke_fatman.png");
    public static final ResourceLocation NUKE_IVY_MIKE = NuclearTechMod.withDefaultNamespace("textures/models/bombs/nuke_ivy_mike.png");
    public static final ResourceLocation NUKE_TSAR_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/nuke_tsar.png");
    public static final ResourceLocation NUKE_FLEIJA_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/nuke_fleija.png");
    public static final ResourceLocation NUKE_N2_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/nuke_n2.png");
    public static final ResourceLocation NUKE_FSTBMB_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/nuke_fstbmb.png");

    public static final ResourceLocation DUD_BALEFIRE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/dud_balefire.png");
    public static final ResourceLocation DUD_CONVENTIONAL_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/dud_conventional.png");
    public static final ResourceLocation DUD_NUKE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/dud_nuke.png");
    public static final ResourceLocation DUD_SALTED_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bombs/dud_salted.png");

    public static final ResourceLocation NO9 = NuclearTechMod.withDefaultNamespace("textures/armor/no9.png");
    public static final ResourceLocation NO9_INSIGNIA = NuclearTechMod.withDefaultNamespace("textures/armor/no9_insignia.png");

    public static final ResourceLocation CABLE_NEO_TEX = NuclearTechMod.withDefaultNamespace("textures/block/red_cable.png");
    public static final ResourceLocation DIFURNACE_EXTENSION_TEX = NuclearTechMod.withDefaultNamespace("textures/block/difurnace_extension.png");
    public static final ResourceLocation DIFURNACE_EXTENSION_TOP_TEX = NuclearTechMod.withDefaultNamespace("textures/block/difurnace_top_off_alt.png");
    public static final ResourceLocation DIFURNACE_EXTENSION_BOTTOM_TEX = NuclearTechMod.withDefaultNamespace("textures/block/brick_fire.png");
    public static final ResourceLocation CABLE_CLASSIC_TEX = NuclearTechMod.withDefaultNamespace("textures/block/red_cable_classic.png");
    public static final ResourceLocation DET_CORD_TEX = NuclearTechMod.withDefaultNamespace("textures/block/det_cord.png");

    public static final ResourceLocation PIPE_NEO_TEX = NuclearTechMod.withDefaultNamespace("textures/block/pipe_neo.png");
    public static final ResourceLocation PIPE_NEO_OVERLAY_TEX = NuclearTechMod.withDefaultNamespace("textures/block/pipe_neo_overlay.png");

    public static final ResourceLocation BARREL_ANTIMATTER_TEX = NuclearTechMod.withDefaultNamespace("textures/block/barrel_antimatter.png");
    public static final ResourceLocation BARREL_CORRODED_TEX = NuclearTechMod.withDefaultNamespace("textures/block/barrel_corroded.png");
    public static final ResourceLocation BARREL_IRON_TEX = NuclearTechMod.withDefaultNamespace("textures/block/barrel_iron.png");
    public static final ResourceLocation BARREL_LOX_TEX = NuclearTechMod.withDefaultNamespace("textures/block/barrel_lox.png");
    public static final ResourceLocation BARREL_PINK_TEX = NuclearTechMod.withDefaultNamespace("textures/block/barrel_pink.png");
    public static final ResourceLocation BARREL_PLASTIC_TEX = NuclearTechMod.withDefaultNamespace("textures/block/barrel_plastic.png");
    public static final ResourceLocation BARREL_RED_TEX = NuclearTechMod.withDefaultNamespace("textures/block/barrel_red.png");
    public static final ResourceLocation BARREL_STEEL_TEX = NuclearTechMod.withDefaultNamespace("textures/block/barrel_steel.png");
    public static final ResourceLocation BARREL_TAINT_TEX = NuclearTechMod.withDefaultNamespace("textures/block/barrel_taint.png");
    public static final ResourceLocation BARREL_TCALLOY_TEX = NuclearTechMod.withDefaultNamespace("textures/block/barrel_tcalloy.png");
    public static final ResourceLocation BARREL_VITRIFIED_TEX = NuclearTechMod.withDefaultNamespace("textures/block/barrel_vitrified.png");
    public static final ResourceLocation BARREL_YELLOW_TEX = NuclearTechMod.withDefaultNamespace("textures/block/barrel_yellow.png");

    public static final ResourceLocation TANK_TEX = NuclearTechMod.withDefaultNamespace("textures/models/machines/tank.png");
    public static final ResourceLocation TANK_INNER_TEX = NuclearTechMod.withDefaultNamespace("textures/models/machines/tank_inner.png");

    public static final ResourceLocation BATTERY_SOCKET_TEX = NuclearTechMod.withDefaultNamespace("textures/models/machines/battery_socket.png");
    public static final ResourceLocation BATTERY_REDD_TEX = NuclearTechMod.withDefaultNamespace("textures/models/machines/fensu2.png");

    public static final ResourceLocation GEIGER_TEX = NuclearTechMod.withDefaultNamespace("textures/block/geiger.png");

    public static final ResourceLocation FUSION_PLASMA_TEX = NuclearTechMod.withDefaultNamespace("textures/models/fusion/plasma.png");
    public static final ResourceLocation FUSION_PLASMA_GLOW_TEX = NuclearTechMod.withDefaultNamespace("textures/models/fusion/plasma_glow.png");
    public static final ResourceLocation FUSION_PLASMA_SPARKLE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/fusion/plasma_sparkle.png");

    public static final ResourceLocation DORNIER_1_TEX = NuclearTechMod.withDefaultNamespace("textures/models/dornier_1.png");
    public static final ResourceLocation DORNIER_2_TEX = NuclearTechMod.withDefaultNamespace("textures/models/dornier_2.png");
    public static final ResourceLocation DORNIER_4_TEX = NuclearTechMod.withDefaultNamespace("textures/models/dornier_4.png");
    public static final ResourceLocation B29_0_TEX = NuclearTechMod.withDefaultNamespace("textures/models/b29_0.png");
    public static final ResourceLocation B29_1_TEX = NuclearTechMod.withDefaultNamespace("textures/models/b29_1.png");
    public static final ResourceLocation B29_2_TEX = NuclearTechMod.withDefaultNamespace("textures/models/b29_2.png");
    public static final ResourceLocation B29_3_TEX = NuclearTechMod.withDefaultNamespace("textures/models/b29_3.png");
    public static final ResourceLocation C130_0_TEX = NuclearTechMod.withDefaultNamespace("textures/models/weapon/c130_0.png");

    public static final ResourceLocation MISSILE_MICRO_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_micro.png");
    public static final ResourceLocation MISSILE_MICRO_TAINT_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_micro_taint.png");
    public static final ResourceLocation MISSILE_MICRO_BHOLE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_micro_bhole.png");
    public static final ResourceLocation MISSILE_MICRO_SCHRABIDIUM_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_micro_schrab.png");
    public static final ResourceLocation MISSILE_MICRO_EMP_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_micro_emp.png");
    public static final ResourceLocation MISSILE_V2_HE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_v2.png");
    public static final ResourceLocation MISSILE_V2_IN_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_v2_inc.png");
    public static final ResourceLocation MISSILE_V2_CL_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_v2_cl.png");
    public static final ResourceLocation MISSILE_V2_BU_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_v2_bu.png");
    public static final ResourceLocation MISSILE_V2_DECOY_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_v2_decoy.png");
    public static final ResourceLocation MISSILE_AA_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_abm.png");
    public static final ResourceLocation MISSILE_STEALTH_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_stealth.png");
    public static final ResourceLocation MISSILE_STRONG_HE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_strong.png");
    public static final ResourceLocation MISSILE_STRONG_EMP_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_strong_emp.png");
    public static final ResourceLocation MISSILE_STRONG_IN_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_strong_inc.png");
    public static final ResourceLocation MISSILE_STRONG_CL_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_strong_cl.png");
    public static final ResourceLocation MISSILE_STRONG_BU_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_strong_bu.png");
    public static final ResourceLocation MISSILE_HUGE_HE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_huge.png");
    public static final ResourceLocation MISSILE_HUGE_IN_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_huge_inc.png");
    public static final ResourceLocation MISSILE_HUGE_CL_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_huge_cl.png");
    public static final ResourceLocation MISSILE_HUGE_BU_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_huge_bu.png");
    public static final ResourceLocation MISSILE_NUCLEAR_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_atlas_nuclear.png");
    public static final ResourceLocation MISSILE_THERMO_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_atlas_thermo.png");
    public static final ResourceLocation MISSILE_VOLCANO_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_atlas_tectonic.png");
    public static final ResourceLocation MISSILE_DOOMSDAY_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_atlas_doomsday.png");
    public static final ResourceLocation MISSILE_DOOMSDAY_RUSTED_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_atlas_doomsday_weathered.png");
    public static final ResourceLocation MISSILE_SHUTTLE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/missile/missile_shuttle.png");

    public static final ResourceLocation MISSILE_PAD_TEX = NuclearTechMod.withDefaultNamespace("textures/models/launchpad/silo.png");

    public static final ResourceLocation BOMBLET_ZETA_TEX = NuclearTechMod.withDefaultNamespace("textures/models/bomblet_zeta.png");

    public static final ResourceLocation DETONATOR_LASER_TEX = NuclearTechMod.withDefaultNamespace("textures/models/weapon/detonator_laser.png");

    public static IModelCustom mine_ap;
    public static IModelCustom mine_he;
    public static IModelCustom mine_naval;
    public static IModelCustom mine_fat;

    public static IModelCustom nuke_gadget;
    public static IModelCustom nuke_little_boy;
    public static IModelCustom nuke_fat_man;
    public static IModelCustom nuke_ivy_mike;
    public static IModelCustom nuke_tsar;
    public static IModelCustom nuke_fleija;
    public static IModelCustom nuke_n2;
    public static IModelCustom nuke_fstbmb;

    public static IModelCustom dud_balefire;
    public static IModelCustom dud_conventional;
    public static IModelCustom dud_nuke;
    public static IModelCustom dud_salted;

    public static IModelCustom armor_no9;

    public static IModelCustom cable_neo;
    public static IModelCustom pipe_neo;
    public static IModelCustom difurnace_extension;

    public static IModelCustom fluid_tank;
    public static IModelCustom fluid_tank_exploded;

    public static IModelCustom geiger;

    public static IModelCustom battery_socket;
    public static IModelCustom battery_redd;

    //Bomber
    public static IModelCustom dornier;
    public static IModelCustom b29;
    public static IModelCustom c130;

    //Missiles
    public static IModelCustom missileV2;
    public static IModelCustom missileABM;
    public static IModelCustom missileStealth;
    public static IModelCustom missileStrong;
    public static IModelCustom missileHuge;
    public static IModelCustom missileNuclear;
    public static IModelCustom missileMicro;
    public static IModelCustom missileShuttle;

    //Missile Parts
    public static IModelCustom missile_pad;

    public static IModelCustom bomblet_zeta;

    public static IModelCustom detonator_laser;

    public static IModelCustom barrel;

    public static IModelCustom sphere;

    public static void init() {
        mine_ap = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/mine_ap.obj")).asVBO();
        mine_he = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/mine_he.obj")).asVBO();
        mine_naval = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/mine_naval.obj")).asVBO();
        mine_fat = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/mine_fat.obj")).asVBO();

        nuke_gadget = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_gadget.obj")).asVBO();
        nuke_little_boy = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_little_boy.obj")).asVBO();
        nuke_fat_man = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_fatman.obj")).asVBO();
        nuke_ivy_mike = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_ivy_mike.obj")).asVBO();
        nuke_tsar = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_tsar.obj")).asVBO();
        nuke_fleija = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_fleija.obj")).asVBO();
        nuke_n2 = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_n2.obj")).asVBO();
        nuke_fstbmb = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_fstbmb.obj")).asVBO();

        dud_balefire = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/dud_balefire.obj")).asVBO();
        dud_conventional = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/dud_conventional.obj")).asVBO();
        dud_nuke = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/dud_nuke.obj")).asVBO();
        dud_salted = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/dud_salted.obj")).asVBO();

        armor_no9 = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/armor/no9.obj")).asVBO();

        cable_neo = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/block/cable_neo.obj")).asVBO();
        pipe_neo = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/block/pipe_neo.obj")).asVBO();
        difurnace_extension = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/block/difurnace_extension.obj")).asVBO();

        fluid_tank = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/machines/fluid_tank.obj")).asVBO();
        fluid_tank_exploded = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/machines/fluid_tank_exploded.obj")).asVBO();

        geiger = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/block/geiger.obj")).asVBO();

        battery_socket = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/machines/battery.obj")).asVBO();
        battery_redd = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/machines/fensu2.obj")).asVBO();

        dornier = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/dornier.obj")).asVBO();
        b29 = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/b29.obj")).asVBO();
        c130 = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/weapons/c130.obj")).asVBO();

        missileV2 = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_v2.obj")).asVBO();
        missileABM = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_abm.obj")).asVBO();
        missileStealth = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_stealth.obj"), false).asVBO();
        missileStrong = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_strong.obj")).asVBO();
        missileHuge = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_huge.obj")).asVBO();
        missileNuclear = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_atlas.obj")).asVBO();
        missileMicro = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_micro.obj")).asVBO();
        missileShuttle = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_shuttle.obj")).asVBO();

        missile_pad = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/weapons/launch_pad_silo.obj")).asVBO();

        bomblet_zeta = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomblet_zeta.obj")).asVBO();

        detonator_laser = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/weapons/detonator_laser.obj")).asVBO();

        barrel = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/block/barrel.obj")).asVBO();

        sphere = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/sphere.obj")).asVBO();

        yomiModel = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/trinkets/yomi.obj"), false).asVBO();
        hundunModel = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/trinkets/hundun.obj"), false).asVBO();
        dergModel = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/trinkets/derg.obj"), false).asVBO();
    }

    public static IModelCustom yomiModel;
    public static IModelCustom hundunModel;
    public static IModelCustom dergModel;
}
