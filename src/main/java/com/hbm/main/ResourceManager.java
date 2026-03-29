package com.hbm.main;

import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.IModelCustomOld;
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
    public static final ResourceLocation PIPE_SILVER_TEX = NuclearTechMod.withDefaultNamespace("textures/block/pipe_silver.png");
    public static final ResourceLocation PIPE_COLORED_TEX = NuclearTechMod.withDefaultNamespace("textures/block/pipe_colored.png");

    public static final ResourceLocation PIPE_NEO_OVERLAY_TEX = NuclearTechMod.withDefaultNamespace("textures/block/pipe_neo_overlay.png");
    public static final ResourceLocation PIPE_SILVER_OVERLAY_TEX = NuclearTechMod.withDefaultNamespace("textures/block/pipe_silver_overlay.png");
    public static final ResourceLocation PIPE_COLORED_OVERLAY_TEX = NuclearTechMod.withDefaultNamespace("textures/block/pipe_colored_overlay.png");

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

    public static IModelCustomOld mine_ap;
    public static IModelCustomOld mine_he;
    public static IModelCustomOld mine_naval;
    public static IModelCustomOld mine_fat;

    public static IModelCustomOld nuke_gadget;
    public static IModelCustomOld nuke_little_boy;
    public static IModelCustom nuke_fat_man;
    public static IModelCustomOld nuke_ivy_mike;
    public static IModelCustomOld nuke_tsar;
    public static IModelCustomOld nuke_fleija;
    public static IModelCustomOld nuke_n2;
    public static IModelCustomOld nuke_fstbmb;

    public static IModelCustomOld dud_balefire;
    public static IModelCustomOld dud_conventional;
    public static IModelCustomOld dud_nuke;
    public static IModelCustomOld dud_salted;

    public static IModelCustomOld armor_no9;

    public static IModelCustomOld cable_neo;
    public static IModelCustomOld pipe_neo;
    public static IModelCustomOld difurnace_extension;

    public static IModelCustomOld fluid_tank;
    public static IModelCustomOld fluid_tank_exploded;

    public static IModelCustomOld geiger;

    public static IModelCustomOld battery_socket;
    public static IModelCustom battery_redd;

    //Bomber
    public static IModelCustomOld dornier;
    public static IModelCustomOld b29;
    public static IModelCustomOld c130;

    //Missiles
    public static IModelCustomOld missileV2;
    public static IModelCustomOld missileABM;
    public static IModelCustomOld missileStealth;
    public static IModelCustomOld missileStrong;
    public static IModelCustomOld missileHuge;
    public static IModelCustomOld missileNuclear;
    public static IModelCustomOld missileMicro;
    public static IModelCustomOld missileShuttle;

    //Missile Parts
    public static IModelCustomOld missile_pad;

    public static IModelCustomOld bomblet_zeta;

    public static IModelCustomOld detonator_laser;

    public static IModelCustomOld barrel;

    public static IModelCustomOld sphere;

    public static void init() {
        mine_ap = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/mine_ap.obj")).render();
        mine_he = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/mine_he.obj")).render();
        mine_naval = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/mine_naval.obj")).render();
        mine_fat = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/mine_fat.obj")).render();

        nuke_gadget = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_gadget.obj")).render();
        nuke_little_boy = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_little_boy.obj")).render();
        nuke_fat_man = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_fatman.obj")).asVBO();
        nuke_ivy_mike = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_ivy_mike.obj")).render();
        nuke_tsar = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_tsar.obj")).render();
        nuke_fleija = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_fleija.obj")).render();
        nuke_n2 = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_n2.obj")).render();
        nuke_fstbmb = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/nuke_fstbmb.obj")).render();

        dud_balefire = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/dud_balefire.obj")).render();
        dud_conventional = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/dud_conventional.obj")).render();
        dud_nuke = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/dud_nuke.obj")).render();
        dud_salted = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomb/dud_salted.obj")).render();

        armor_no9 = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/armor/no9.obj")).render();

        cable_neo = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/block/cable_neo.obj")).render();
        pipe_neo = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/block/pipe_neo.obj")).render();
        difurnace_extension = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/block/difurnace_extension.obj")).render();

        fluid_tank = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/machines/fluid_tank.obj")).render();
        fluid_tank_exploded = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/machines/fluid_tank_exploded.obj")).render();

        geiger = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/block/geiger.obj")).render();

        battery_socket = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/machines/battery.obj")).render();
        battery_redd = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/machines/fensu2.obj")).asVBO();

        dornier = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/dornier.obj")).render();
        b29 = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/b29.obj")).render();
        c130 = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/weapons/c130.obj")).render();

        missileV2 = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_v2.obj")).render();
        missileABM = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_abm.obj")).render();
        missileStealth = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_stealth.obj")).render();
        missileStrong = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_strong.obj")).render();
        missileHuge = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_huge.obj")).render();
        missileNuclear = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_atlas.obj")).render();
        missileMicro = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_micro.obj")).render();
        missileShuttle = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/missile_shuttle.obj")).render();

        missile_pad = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/weapons/launch_pad_silo.obj")).render();

        bomblet_zeta = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/bomblet_zeta.obj")).render();

        detonator_laser = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/weapons/detonator_laser.obj")).render();

        barrel = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/block/barrel.obj")).render();

        sphere = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/sphere.obj")).render();

        yomiModel = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/trinkets/yomi.obj")).render();
        hundunModel = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/trinkets/hundun.obj")).render();
        dergModel = new HFRWavefrontObject(NuclearTechMod.withDefaultNamespace("models/obj/trinkets/derg.obj")).render();
    }

    public static IModelCustomOld yomiModel;
    public static IModelCustomOld hundunModel;
    public static IModelCustomOld dergModel;
}
