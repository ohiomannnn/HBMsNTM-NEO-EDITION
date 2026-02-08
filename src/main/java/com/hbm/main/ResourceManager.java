package com.hbm.main;

import com.hbm.HBMsNTM;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.IModelCustom;
import net.minecraft.resources.ResourceLocation;

public class ResourceManager {
    public static final ResourceLocation MINE_AP_STONE_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/mine_ap.png");
    public static final ResourceLocation MINE_AP_SNOW_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/mine_ap_snow.png");
    public static final ResourceLocation MINE_AP_GRASS_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/mine_ap_grass.png");
    public static final ResourceLocation MINE_AP_DESERT_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/mine_ap_desert.png");
    public static final ResourceLocation MINE_HE_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/mine_he.png");
    public static final ResourceLocation MINE_SHRAPNEL_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/mine_shrap.png");
    public static final ResourceLocation MINE_NAVAL_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/mine_naval.png");
    public static final ResourceLocation MINE_FAT_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/mine_fat.png");

    public static final ResourceLocation NUKE_GADGET_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/nuke_gadget.png");
    public static final ResourceLocation NUKE_LITTLE_BOY_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/nuke_little_boy.png");
    public static final ResourceLocation NUKE_FAT_MAN_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/nuke_fatman.png");
    public static final ResourceLocation NUKE_IVY_MIKE = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/nuke_ivy_mike.png");
    public static final ResourceLocation NUKE_TSAR_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/nuke_tsar.png");
    public static final ResourceLocation NUKE_N2_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/nuke_n2.png");

    public static final ResourceLocation DUD_BALEFIRE_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/dud_balefire.png");
    public static final ResourceLocation DUD_CONVENTIONAL_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/dud_conventional.png");
    public static final ResourceLocation DUD_NUKE_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/dud_nuke.png");
    public static final ResourceLocation DUD_SALTED_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bombs/dud_salted.png");

    public static final ResourceLocation NO9 = HBMsNTM.withDefaultNamespaceNT("textures/armor/no9.png");
    public static final ResourceLocation NO9_INSIGNIA = HBMsNTM.withDefaultNamespaceNT("textures/armor/no9_insignia.png");

    public static final ResourceLocation CABLE_NEO_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/red_cable.png");
    public static final ResourceLocation DIFURNACE_EXTENSION_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/difurnace_extension.png");
    public static final ResourceLocation DIFURNACE_EXTENSION_TOP_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/difurnace_top_off_alt.png");
    public static final ResourceLocation DIFURNACE_EXTENSION_BOTTOM_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/brick_fire.png");
    public static final ResourceLocation CABLE_CLASSIC_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/red_cable_classic.png");
    public static final ResourceLocation DET_CORD_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/det_cord.png");

    public static final ResourceLocation BARREL_ANTIMATTER_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/barrel_antimatter.png");
    public static final ResourceLocation BARREL_CORRODED_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/barrel_corroded.png");
    public static final ResourceLocation BARREL_IRON_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/barrel_iron.png");
    public static final ResourceLocation BARREL_LOX_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/barrel_lox.png");
    public static final ResourceLocation BARREL_PINK_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/barrel_pink.png");
    public static final ResourceLocation BARREL_PLASTIC_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/barrel_plastic.png");
    public static final ResourceLocation BARREL_RED_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/barrel_red.png");
    public static final ResourceLocation BARREL_STEEL_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/barrel_steel.png");
    public static final ResourceLocation BARREL_TAINT_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/barrel_taint.png");
    public static final ResourceLocation BARREL_TCALLOY_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/barrel_tcalloy.png");
    public static final ResourceLocation BARREL_VITRIFIED_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/barrel_vitrified.png");
    public static final ResourceLocation BARREL_YELLOW_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/barrel_yellow.png");

    public static final ResourceLocation TANK_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/machines/tank.png");
    public static final ResourceLocation TANK_INNER_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/machines/tank_inner.png");

    public static final ResourceLocation BATTERY_SOCKET_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/machines/battery_socket.png");
    public static final ResourceLocation BATTERY_REDD_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/machines/fensu2.png");

    public static final ResourceLocation GEIGER_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/geiger.png");

    public static final ResourceLocation FUSION_PLASMA_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/fusion/plasma.png");
    public static final ResourceLocation FUSION_PLASMA_GLOW_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/fusion/plasma_glow.png");
    public static final ResourceLocation FUSION_PLASMA_SPARKLE_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/fusion/plasma_sparkle.png");

    public static final ResourceLocation DORNIER_1_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/dornier_1.png");
    public static final ResourceLocation DORNIER_2_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/dornier_2.png");
    public static final ResourceLocation DORNIER_4_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/dornier_4.png");
    public static final ResourceLocation B29_0_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/b29_0.png");
    public static final ResourceLocation B29_1_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/b29_1.png");
    public static final ResourceLocation B29_2_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/b29_2.png");
    public static final ResourceLocation B29_3_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/b29_3.png");
    public static final ResourceLocation C130_0_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/weapon/c130_0.png");

    public static final ResourceLocation BOMBLET_ZETA_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/bomblet_zeta.png");

    public static final ResourceLocation DETONATOR_LASER_TEX = HBMsNTM.withDefaultNamespaceNT("textures/models/weapon/detonator_laser.png");

    public static IModelCustom mine_ap;
    public static IModelCustom mine_he;
    public static IModelCustom mine_naval;
    public static IModelCustom mine_fat;

    public static IModelCustom nuke_gadget;
    public static IModelCustom nuke_little_boy;
    public static IModelCustom nuke_fat_man;
    public static IModelCustom nuke_ivy_mike;
    public static IModelCustom nuke_tsar;
    public static IModelCustom nuke_n2;

    public static IModelCustom dud_balefire;
    public static IModelCustom dud_conventional;
    public static IModelCustom dud_nuke;
    public static IModelCustom dud_salted;

    public static IModelCustom armor_no9;

    public static IModelCustom cable_neo;
    public static IModelCustom difurnace_extension;

    public static IModelCustom fluid_tank;
    public static IModelCustom fluid_tank_exploded;

    public static IModelCustom geiger;

    public static IModelCustom battery_socket;
    public static IModelCustom battery_redd;

    public static IModelCustom dornier;
    public static IModelCustom b29;
    public static IModelCustom c130;

    public static IModelCustom bomblet_zeta;

    public static IModelCustom detonator_laser;

    public static IModelCustom barrel;

    public static void init() {
        mine_ap = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/mine_ap.obj")).asVBO();
        mine_he = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/mine_he.obj")).asVBO();
        mine_naval = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/mine_naval.obj")).asVBO();
        mine_fat = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/mine_fat.obj")).asVBO();

        nuke_gadget = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/nuke_gadget.obj")).asVBO();
        nuke_little_boy = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/nuke_little_boy.obj")).asVBO();
        nuke_fat_man = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/nuke_fatman.obj")).asVBO();
        nuke_ivy_mike = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/nuke_ivy_mike.obj")).asVBO();
        nuke_tsar = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/nuke_tsar.obj")).asVBO();
        nuke_n2 = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/nuke_n2.obj")).asVBO();

        dud_balefire = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/dud_balefire.obj")).asVBO();
        dud_conventional = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/dud_conventional.obj")).asVBO();
        dud_nuke = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/dud_nuke.obj")).asVBO();
        dud_salted = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/dud_salted.obj")).asVBO();

        armor_no9 = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/armor/no9.obj")).asVBO();

        cable_neo = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/block/cable_neo.obj")).asVBO();
        difurnace_extension = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/block/difurnace_extension.obj")).asVBO();

        fluid_tank = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/machines/fluid_tank.obj")).asVBO();
        fluid_tank_exploded = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/machines/fluid_tank_exploded.obj")).asVBO();

        geiger = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/block/geiger.obj")).asVBO();

        battery_socket = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/machines/battery.obj")).asVBO();
        battery_redd = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/machines/fensu2.obj")).asVBO();

        dornier = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/dornier.obj")).asVBO();
        b29 = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/b29.obj")).asVBO();
        c130 = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/weapon/c130.obj")).asVBO();

        bomblet_zeta = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomblet_zeta.obj")).asVBO();

        detonator_laser = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/weapon/detonator_laser.obj")).asVBO();

        barrel = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/block/barrel.obj")).asVBO();
    }
}
