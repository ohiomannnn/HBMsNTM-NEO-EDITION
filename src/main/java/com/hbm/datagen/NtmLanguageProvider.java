package com.hbm.datagen;

import com.hbm.blocks.NtmBlocks;
import com.hbm.main.NuclearTechMod;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public class NtmLanguageProvider extends LanguageProvider {

    public NtmLanguageProvider(PackOutput output) {
        super(output, NuclearTechMod.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add("block.hbmsntm.desc.blastRes", "Blast Resistance: %s");
        this.add("block.hbmsntm.desc.speedy", "Increases speed by %s");
        this.add(NtmBlocks.ASPHALT, "Asphalt");
        this.add(NtmBlocks.ASPHALT_LIGHT, "Glowing Asphalt");

        this.add("block.hbmsntm.desc.nospawn", "Mobs cannot spawn on this block!");
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

        this.add(NtmBlocks.BARBED_WIRE, ".standard",   "Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, ".fire",       "Flaming Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, ".poison",     "Poisoned Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, ".acid",       "Caustic Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, ".wither",     "Withered Barbed Wire");
        this.add(NtmBlocks.BARBED_WIRE, ".ultradeath", "Radioactive Barbed Wire");
        this.add(NtmBlocks.SPIKES, "Spikes");
    }

    // HELPERS //
    public void add(DeferredBlock<? extends Block> key, String name) { this.add(key.get(), name); }
    public void add(DeferredItem<? extends Item> key, String name) { this.add(key.get(), name); }

    public void add(DeferredBlock<? extends Block> key, String toAppend, String name) { this.add(key.get().getDescriptionId() + toAppend, name); }
}
