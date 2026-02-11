package com.hbm.hazard;

import com.hbm.blocks.ModBlocks;
import com.hbm.hazard.type.*;
import com.hbm.items.ModItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

@SuppressWarnings("unused") //shut the fuck up
public class HazardRegistry {

    // CO60		                 5a		β−	030.00Rad/s	Spicy
    // SR90		                29a		β−	015.00Rad/s Spicy
    // TC99		           211,000a		β−	002.75Rad/s	Spicy
    // I181		                92h		β−	150.00Rad/s	2 much spice :(
    // XE135		             9h		β−	aaaaaaaaaaaaaaaa
    // CS137		            30a		β−	020.00Rad/s	Spicy
    // AU198		            64h		β−	500.00Rad/s	2 much spice :(
    // PB209		             3h		β−	10,000.00Rad/s mama mia my face is melting off
    // AT209		             5h		β+	like 7.5k or sth idk bruv
    // PO210		           138d		α	075.00Rad/s	Spicy
    // RA226		         1,600a		α	007.50Rad/s
    // AC227		            22a		β−	030.00Rad/s Spicy
    // TH232		14,000,000,000a		α	000.10Rad/s
    // U233		           160,000a		α	005.00Rad/s
    // U235		       700,000,000a		α	001.00Rad/s
    // U238		     4,500,000,000a		α	000.25Rad/s
    // NP237		     2,100,000a		α	002.50Rad/s
    // PU238		            88a		α	010.00Rad/s	Spicy
    // PU239		        24,000a		α	005.00Rad/s
    // PU240		         6,600a		α	007.50Rad/s
    // PU241		            14a		β−	025.00Rad/s	Spicy
    // AM241		           432a		α	008.50Rad/s
    // AM242		           141a		β−	009.50Rad/s

    //simplified groups for ReC compat
    public static final float gen_S = 10_000F;
    public static final float gen_H = 2_000F;
    public static final float gen_10D = 100F;
    public static final float gen_100D = 80F;
    public static final float gen_1Y = 50F;
    public static final float gen_10Y = 30F;
    public static final float gen_100Y = 10F;
    public static final float gen_1K = 7.5F;
    public static final float gen_10K = 6.25F;
    public static final float gen_100K = 5F;
    public static final float gen_1M = 2.5F;
    public static final float gen_10M = 1.5F;
    public static final float gen_100M = 1F;
    public static final float gen_1B = 0.5F;
    public static final float gen_10B = 0.1F;

    public static final float co60 = 30.0F;
    public static final float sr90 = 15.0F;
    public static final float tc99 = 2.75F;
    public static final float i131 = 150.0F;
    public static final float xe135 = 1250.0F;
    public static final float cs137 = 20.0F;
    public static final float au198 = 500.0F;
    public static final float pb209 = 10000.0F;
    public static final float at209 = 7500.0F;
    public static final float po210 = 75.0F;
    public static final float ra226 = 7.5F;
    public static final float ac227 = 30.0F;
    public static final float th232 = 0.1F;
    public static final float thf = 1.75F;
    public static final float u = 0.35F;
    public static final float u233 = 5.0F;
    public static final float u235 = 1.0F;
    public static final float u238 = 0.25F;
    public static final float uf = 0.5F;
    public static final float np237 = 2.5F;
    public static final float npf = 1.5F;
    public static final float pu = 7.5F;
    public static final float purg = 6.25F;
    public static final float pu238 = 10.0F;
    public static final float pu239 = 5.0F;
    public static final float pu240 = 7.5F;
    public static final float pu241 = 25.0F;
    public static final float puf = 4.25F;
    public static final float am241 = 8.5F;
    public static final float am242 = 9.5F;
    public static final float amrg = 9.0F;
    public static final float amf = 4.75F;
    public static final float mox = 2.5F;
    public static final float sa326 = 15.0F;
    public static final float sa327 = 17.5F;
    public static final float saf = 5.85F;
    public static final float sas3 = 5F;
    public static final float gh336 = 5.0F;
    public static final float mud = 1.0F;
    public static final float radsource_mult = 3.0F;
    public static final float pobe = po210 * radsource_mult;
    public static final float rabe = ra226 * radsource_mult;
    public static final float pube = pu238 * radsource_mult;
    public static final float zfb_bi = u235 * 0.35F;
    public static final float zfb_pu241 = pu241 * 0.5F;
    public static final float zfb_am_mix = amrg * 0.5F;
    public static final float bf = 300_000.0F;
    public static final float bfb = 500_000.0F;

    public static final float sr = sa326 * 0.1F;
    public static final float sb = sa326 * 0.1F;
    public static final float trx = 25.0F;
    public static final float trn = 0.1F;
    public static final float wst = 15.0F;
    public static final float wstv = 7.5F;
    public static final float yc = u;
    public static final float fo = 10F;

    public static final float nugget = 0.1F;
    public static final float ingot = 1.0F;
    public static final float gem = 1.0F;
    public static final float plate = ingot;
    public static final float plateCast = plate * 3;
    public static final float powder_mult = 3.0F;
    public static final float powder = ingot * powder_mult;
    public static final float powder_tiny = nugget * powder_mult;
    public static final float ore = ingot;
    public static final float block = 10.0F;
    public static final float crystal = block;
    public static final float billet = 0.5F;
    public static final float rtg = billet * 3;
    public static final float rod = 0.5F;
    public static final float rod_dual = rod * 2;
    public static final float rod_quad = rod * 4;
    public static final float rod_rbmk = rod * 8;

    public static final HazardTypeBase RADIATION = new HazardTypeRadiation();
    public static final HazardTypeBase DIGAMMA = new HazardTypeDigamma();
    public static final HazardTypeBase HOT = new HazardTypeHot();
    public static final HazardTypeBase BLINDING = new HazardTypeBlinding();
    public static final HazardTypeBase ASBESTOS = new HazardTypeAsbestos();
    public static final HazardTypeBase COAL = new HazardTypeCoal();
    public static final HazardTypeBase HYDROACTIVE = new HazardTypeHydroactive();
    public static final HazardTypeBase EXPLOSIVE = new HazardTypeExplosive();

    public static void registerItems() {
        HazardSystem.register(Items.GUNPOWDER, makeData(EXPLOSIVE, 1F));
        HazardSystem.register(Items.PUMPKIN_PIE, makeData(EXPLOSIVE, 1F));
        HazardSystem.register(Blocks.TNT, makeData(EXPLOSIVE, 4F));

        HazardSystem.register(Items.GOLD_INGOT, makeData(BLINDING, 4F));

        HazardSystem.register(Items.BLAZE_POWDER, makeData(HOT, 4F));
        HazardSystem.register(Blocks.GREEN_WOOL, makeData(RADIATION, 150F));
        HazardSystem.register(Blocks.WHITE_WOOL, makeData(ASBESTOS, 5F));
        HazardSystem.register(Items.COAL, makeData(COAL, 1F));

        HazardSystem.register(ModBlocks.FALLOUT.get(), makeData(RADIATION, 60F));

        HazardSystem.register(ModItems.PARTICLE_DIGAMMA.get(), makeData(DIGAMMA, 0.3333F));


        /*
         * Nuke parts
         */
        HazardSystem.register(ModItems.LITTLE_BOY_PROPELLANT.get(), makeData(EXPLOSIVE, 2F));
        HazardSystem.register(ModItems.GADGET_CORE.get(), makeData(RADIATION, pu239 * nugget * 10));
        HazardSystem.register(ModItems.LITTLE_BOY_TARGET.get(), makeData(RADIATION, u235 * ingot * 2));
        HazardSystem.register(ModItems.LITTLE_BOY_BULLET.get(), makeData(RADIATION, u235 * ingot));
        HazardSystem.register(ModItems.IVY_MIKE_CORE.get(), makeData(RADIATION, u238 * nugget * 10));
        HazardSystem.register(ModItems.FAT_MAN_CORE.get(), makeData(RADIATION, pu239 * nugget * 10));
    }

    private static HazardData makeData() { return new HazardData(); }
    private static HazardData makeData(HazardTypeBase hazard) { return new HazardData().addEntry(hazard); }
    private static HazardData makeData(HazardTypeBase hazard, float level) { return new HazardData().addEntry(hazard, level); }
    private static HazardData makeData(HazardTypeBase hazard, float level, boolean override) { return new HazardData().addEntry(hazard, level, override); }
}
