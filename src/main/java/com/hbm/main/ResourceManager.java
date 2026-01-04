package com.hbm.main;

import com.hbm.HBMsNTM;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.IModelCustom;
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

    public static IModelCustom mine_ap;
    public static IModelCustom mine_he;
    public static IModelCustom mine_naval;
    public static IModelCustom mine_fat;

    public static IModelCustom nuke_fat_man;
    public static IModelCustom nuke_tsar;
    public static IModelCustom dud_balefire;
    public static IModelCustom dud_conventional;
    public static IModelCustom dud_nuke;
    public static IModelCustom dud_salted;

    public static IModelCustom cable_neo;

    public static IModelCustom geiger;

    public static void init() {
        mine_ap = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/mine_ap.obj")).asVBO();
        mine_he = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/mine_he.obj")).asVBO();
        mine_naval = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/mine_naval.obj")).asVBO();
        mine_fat = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/mine_fat.obj")).asVBO();

        nuke_fat_man = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/nuke_fatman.obj")).asVBO();
        nuke_tsar = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/nuke_tsar.obj")).asVBO();
        dud_balefire = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/dud_balefire.obj")).asVBO();
        dud_conventional = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/dud_conventional.obj")).asVBO();
        dud_nuke = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/dud_nuke.obj")).asVBO();
        dud_salted = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/bomb/dud_salted.obj")).asVBO();

        cable_neo = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/block/cable_neo.obj")).asVBO();

        geiger = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/block/geiger.obj")).asVBO();
    }
}
