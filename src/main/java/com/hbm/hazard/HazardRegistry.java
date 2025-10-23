package com.hbm.hazard;

import com.hbm.blocks.ModBlocks;
import com.hbm.hazard.type.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class HazardRegistry {

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
    }

    private static HazardData makeData() { return new HazardData(); }
    private static HazardData makeData(HazardTypeBase hazard) { return new HazardData().addEntry(hazard); }
    private static HazardData makeData(HazardTypeBase hazard, float level) { return new HazardData().addEntry(hazard, level); }
    private static HazardData makeData(HazardTypeBase hazard, float level, boolean override) { return new HazardData().addEntry(hazard, level, override); }
}
