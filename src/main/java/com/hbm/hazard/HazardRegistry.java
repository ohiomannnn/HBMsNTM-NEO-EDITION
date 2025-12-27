package com.hbm.hazard;

import com.hbm.blocks.ModBlocks;
import com.hbm.hazard.type.*;
import com.hbm.items.ModItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

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

        HazardSystem.register(ModBlocks.URANIUM_BLOCK.get(), makeData(RADIATION, 160F));

        HazardSystem.register(ModItems.PARTICLE_DIGAMMA.get(), makeData(DIGAMMA, 0.3333F));
    }

    private static HazardData makeData() { return new HazardData(); }
    private static HazardData makeData(HazardTypeBase hazard) { return new HazardData().addEntry(hazard); }
    private static HazardData makeData(HazardTypeBase hazard, float level) { return new HazardData().addEntry(hazard, level); }
    private static HazardData makeData(HazardTypeBase hazard, float level, boolean override) { return new HazardData().addEntry(hazard, level, override); }
}
