package com.hbm.main;

import com.hbm.HBMsNTM;
import com.hbm.render.loader.HFRWavefrontObjectVBO;
import com.hbm.render.loader.HFRWavefrontObject;
import net.minecraft.resources.ResourceLocation;

public class ResourceManager {
    public static final ResourceLocation MINE_AP_STONE_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/bomb/mine_ap.png");
    public static final ResourceLocation MINE_AP_SNOW_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/bomb/mine_ap_snow.png");
    public static final ResourceLocation MINE_AP_GRASS_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/bomb/mine_ap_grass.png");
    public static final ResourceLocation MINE_AP_DESERT_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/bomb/mine_ap_desert.png");
    public static final ResourceLocation MINE_HE_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/bomb/mine_he.png");
    public static final ResourceLocation MINE_SHRAPNEL_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/bomb/mine_shrap.png");
    public static final ResourceLocation MINE_NAVAL_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/bomb/mine_naval.png");
    public static final ResourceLocation MINE_FAT_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/bomb/mine_fat.png");

    public static final ResourceLocation NUKE_TSAR_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/bomb/nuke_tsar.png");
    public static final ResourceLocation NUKE_FAT_MAN_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/bomb/nuke_fatman.png");
    public static final ResourceLocation DUD_BALEFIRE_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/bomb/dud_balefire.png");
    public static final ResourceLocation DUD_CONVENTIONAL_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/bomb/dud_conventional.png");
    public static final ResourceLocation DUD_NUKE_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/bomb/dud_nuke.png");
    public static final ResourceLocation DUD_SALTED_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/bomb/dud_salted.png");

    public static final ResourceLocation CABLE_NEO_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/cable_neo.png");

    public static final ResourceLocation GEIGER_TEX = HBMsNTM.withDefaultNamespaceNT("textures/block/geiger.png");

    public static HFRWavefrontObject mine_ap;
    public static HFRWavefrontObject mine_he;
    public static HFRWavefrontObject mine_shrapnel;
    public static HFRWavefrontObject mine_naval;
    public static HFRWavefrontObject mine_fat;

    public static HFRWavefrontObject nuke_fat_man;
    public static HFRWavefrontObject nuke_tsar;
    public static HFRWavefrontObject dud_balefire;
    public static HFRWavefrontObject dud_conventional;
    public static HFRWavefrontObject dud_nuke;
    public static HFRWavefrontObject dud_salted;

    public static HFRWavefrontObject cable_neo;

    public static HFRWavefrontObject geiger;

    public static void init() {
        mine_ap = new HFRWavefrontObjectVBO(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/mine_ap.obj")).buildVBO();
        mine_he = new HFRWavefrontObjectVBO(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/mine_he.obj")).buildVBO();
        mine_shrapnel = new HFRWavefrontObjectVBO(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/mine_shrap.obj")).buildVBO();
        mine_naval = new HFRWavefrontObjectVBO(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/mine_naval.obj")).buildVBO();
        mine_fat = new HFRWavefrontObjectVBO(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/mine_fat.obj")).buildVBO();

        nuke_fat_man = new HFRWavefrontObjectVBO(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/nuke_fatman.obj")).buildVBO();
        nuke_tsar = new HFRWavefrontObjectVBO(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/nuke_tsar.obj")).buildVBO();
        dud_balefire = new HFRWavefrontObjectVBO(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/dud_balefire.obj")).buildVBO();
        dud_conventional = new HFRWavefrontObjectVBO(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/dud_conventional.obj")).buildVBO();
        dud_nuke = new HFRWavefrontObjectVBO(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/dud_nuke.obj")).buildVBO();
        dud_salted = new HFRWavefrontObjectVBO(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/dud_salted.obj")).buildVBO();

        cable_neo = new HFRWavefrontObjectVBO(HBMsNTM.withDefaultNamespaceNT("models/obj/block/cable_neo.obj")).buildVBO();

        geiger = new HFRWavefrontObjectVBO(HBMsNTM.withDefaultNamespaceNT("models/obj/block/geiger.obj")).buildVBO();
    }
}
