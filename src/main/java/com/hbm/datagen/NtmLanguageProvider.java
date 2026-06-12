package com.hbm.datagen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.generic.BarbedWireBlock.BarbedWireType;
import com.hbm.blocks.generic.PlushieBlock.PlushieType;
import com.hbm.main.NuclearTechMod;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Locale;

public class NtmLanguageProvider extends LanguageProvider {

    // helper keys
    private static final String DESC = ".desc";
    private static final String CONTAINER = "container.";

    public NtmLanguageProvider(PackOutput output) {
        super(output, NuclearTechMod.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        // ITEMS //

        // BLOCKS //
        this.add(NtmBlocks.BOBBLEHEAD, "Bobblehead");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.YOMI), "Yomi Plushie");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.YOMI) + DESC, "Hi! Can I be your rabbit friend");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.NUMBERNINE), "Number Nine Plushie");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.NUMBERNINE) + DESC, "None of y'all deserve coal.");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.HUNDUN), "Hundun Plushie");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.HUNDUN) + DESC, "混沌");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.DERG), "Dragon Plushie");
        this.add(NtmBlocks.PLUSHIE, this.getName(PlushieType.DERG) + DESC, "Squeeze him.");

        this.add(NtmBlocks.GRAVEL_OBSIDIAN, "Crushed Obsidian");
        this.add(NtmBlocks.GRAVEL_DIAMOND, "Diamond Gravel");
        this.add(NtmBlocks.GRAVEL_DIAMOND, DESC, "There is some kind of joke here,$but I can't quite tell what it is.$$Update, 2020-07-04:$We deny any implications of a joke on$the basis that it was so severely unfunny$that people started stabbing their eyes out.$$Update, 2020-17-04:$As it turns out, \"Diamond Gravel\" was$never really a thing, rendering what might$have been a joke as totally nonsensical.$We apologize for getting your hopes up with$this non-joke that hasn't been made.$$i added an item for a joke that isn't even here, what am i, stupid? can't even tell the difference between gravel and a gavel, how did i not forget how to breathe yet?");

        this.add("block.hbmsntm.obj_blast_info.desc", "Blast Resistance: %s");
        this.add("block.hbmsntm.obj_speedy.desc", "Increases speed by %s");
        this.add(NtmBlocks.ASPHALT, "Asphalt");
        this.add(NtmBlocks.ASPHALT_LIGHT, "Glowing Asphalt");

        this.add("block.hbmsntm.obj_no_spawn.desc", "Mobs cannot spawn on this block!");
        this.add(NtmBlocks.BRICK_CONCRETE, "Concrete Bricks");
        this.add(NtmBlocks.BRICK_CONCRETE_MOSSY, "Mossy Concrete Bricks");
        this.add(NtmBlocks.BRICK_CONCRETE_CRACKED, "Cracked Concrete Bricks");
        this.add(NtmBlocks.BRICK_CONCRETE_BROKEN, "Broken Concrete Bricks");
        this.add(NtmBlocks.BRICK_CONCRETE_MARKED, "Marked Concrete Bricks");
        this.add(NtmBlocks.BRICK_OBSIDIAN, "Obsidian Bricks");
        this.add(NtmBlocks.BRICK_LIGHT, "Light Bricks");
        this.add(NtmBlocks.BRICK_ASBESTOS, "Asbestos Bricks");
        this.add(NtmBlocks.BRICK_FIRE, "Firebricks");

        this.add(NtmBlocks.BRICK_CONCRETE_SLAB, "Concrete Brick Slab");
        this.add(NtmBlocks.BRICK_CONCRETE_MOSSY_SLAB, "Mossy Concrete Brick Slab");
        this.add(NtmBlocks.BRICK_CONCRETE_CRACKED_SLAB, "Cracked Concrete Brick Slab");
        this.add(NtmBlocks.BRICK_CONCRETE_BROKEN_SLAB, "Broken Concrete Brick Slab");

        this.add(NtmBlocks.BRICK_CONCRETE_STAIRS, "Concrete Brick Stairs");
        this.add(NtmBlocks.BRICK_CONCRETE_MOSSY_STAIRS, "Mossy Concrete Brick Stairs");
        this.add(NtmBlocks.BRICK_CONCRETE_CRACKED_STAIRS, "Cracked Concrete Brick Stairs");
        this.add(NtmBlocks.BRICK_CONCRETE_BROKEN_STAIRS, "Broken Concrete Brick Stairs");

        this.add(NtmBlocks.BARBED_WIRE, this.getName(BarbedWireType.STANDARD),   "Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, this.getName(BarbedWireType.FIRE),       "Flaming Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, this.getName(BarbedWireType.POISON),     "Poisoned Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, this.getName(BarbedWireType.ACID),       "Caustic Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, this.getName(BarbedWireType.WITHER),     "Withered Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, this.getName(BarbedWireType.ULTRADEATH), "Radioactive Barbed Wire");
        this.add(NtmBlocks.SPIKES, "Spikes");

        this.add(NtmBlocks.NUKE_GADGET, "The Gadget");
        this.add(NtmBlocks.NUKE_LITTLE_BOY, "Little Boy");
        this.add(NtmBlocks.NUKE_FAT_MAN, "Fat Man");
        this.add(NtmBlocks.NUKE_IVY_MIKE, "Ivy Mike");
        this.add(NtmBlocks.NUKE_TSAR_BOMBA, "Tsar Bomba");
        this.add(NtmBlocks.NUKE_PROTOTYPE, "The Prototype");
        this.add(NtmBlocks.NUKE_PROTOTYPE, DESC, "It didn't have to be like this.$ $You monster.");
        this.add(NtmBlocks.NUKE_FLEIJA, "F.L.E.I.J.A.");
        this.add(NtmBlocks.NUKE_N2, "N² Mine");
        this.add(NtmBlocks.NUKE_FSTBMB, "Balefire Bomb");

        this.add(NtmBlocks.CRASHED_BOMB, "Dud");
        this.add(NtmBlocks.DYNAMITE, "Dynamite");
        this.add(NtmBlocks.TNT, "Actual TNT");
        this.add(NtmBlocks.SEMTEX, "Semtex");
        this.add(NtmBlocks.C4, "C-4");
        this.add(NtmBlocks.FISSURE_BOMB, "Fissure Bomb");

        this.add(NtmBlocks.MINE_AP, "Anti-Personell Mine");
        this.add(NtmBlocks.MINE_HE, "Anti-Tank Mine");
        this.add(NtmBlocks.MINE_SHRAP, "Shrapnel Mine");
        this.add(NtmBlocks.MINE_FAT, "Fat Mine");
        this.add(NtmBlocks.MINE_NAVAL, "Naval Mine");

        this.add(NtmBlocks.DET_CHARGE, "Explosive Charge");
        this.add(NtmBlocks.DET_CORD, "Det Cord");
        this.add(NtmBlocks.DET_NUKE, "Nuclear Charge");
        this.add(NtmBlocks.DET_MINER, "Mining Charge");
        this.add("block.hbmsntm.obj_red_barrel.desc", "Static Fluid Barrel");
        this.add(NtmBlocks.BARREL_RED, "Explosive Barrel");
        this.add(NtmBlocks.BARREL_PINK, "Kerosene Barrel");
        this.add(NtmBlocks.BARREL_LOX, "LOX Barrel");
        this.add(NtmBlocks.BARREL_TAINT, "IMP Residue Barrel");

        /*
         * CONTAINERS
         */
        this.add("container.nuke_gadget", "The Gadget");
        this.add("container.nuke_gadget.desc", "§1Requires:§r$ * 4 Arrays of First-Generation$   High-Explosive Lenses$ * Large Plutonium Core$ * Wiring");
        this.add("container.nuke_fat_man", "Fat Man");
        this.add("container.nuke_fat_man.desc", "§1Requires:§r$ * 4 Arrays of First-Generation$   High-Explosive Lenses$ * Plutonium Core$ * Bomb Firing Unit");
        this.add("container.nuke_little_boy", "Little Boy");
        this.add("container.nuke_little_boy.desc", "§1Requires:§r$ * Neutron Shielding$ * U235 Projectile$ * Subcritical U235 Target$ * Propellant$ * Bomb Igniter");
        this.add("container.nuke_ivy_mike", "Ivy Mike");
        this.add("container.nuke_ivy_mike.desc", "§1Requires:§r$ * 4 Arrays of High-Explosive Lenses$ * Plutonium Core$ * Deuterium Cooling Unit$ * Uranium Coated Deuterium Tank$ * Deuterium Tank");
        this.add("container.nuke_tsar_bomba", "Tsar Bomba");
        this.add("container.nuke_tsar_bomba.desc", "§1Requires:§r$ * 4 Arrays of High-Explosive Lenses$ * Plutonium Core$§9Optional:§r$ * Tsar Bomba Core");
        this.add("container.nuke_prototype", "The Prototype");
        this.add("container.nuke_fleija", "F.L.E.I.J.A.");
        this.add("container.nuke_n2", "N² Mine");
        this.add("container.nuke_fstbmb", "Balefire Bomb");
    }

    // HELPERS //
    public void add(DeferredBlock<? extends Block> key, String name) { this.add(key.get(), name); }
    public void add(DeferredItem<? extends Item> key, String name) { this.add(key.get(), name); }

    public void add(DeferredBlock<? extends Block> key, String toAppend, String name) { this.add(key.get().getDescriptionId() + toAppend, name); }

    public String getName(Enum<?> theEnum) { return "." + theEnum.name().toLowerCase(Locale.US); }
}
