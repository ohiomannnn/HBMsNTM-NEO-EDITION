package com.hbm.datagen;

import com.hbm.blocks.ModBlocks;
import com.hbm.items.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.BRICK_CONCRETE.get());
        dropSelf(ModBlocks.BRICK_CONCRETE_MOSSY.get());
        dropSelf(ModBlocks.BRICK_CONCRETE_BROKEN.get());
        dropSelf(ModBlocks.BRICK_CONCRETE_CRACKED.get());
        dropSelf(ModBlocks.BRICK_CONCRETE_MARKED.get());

        dropSelf(ModBlocks.BRICK_LIGHT.get());
        dropSelf(ModBlocks.BRICK_OBSIDIAN.get());
        dropSelf(ModBlocks.GRAVEL_OBSIDIAN.get());
        dropSelf(ModBlocks.BLOCK_SCRAP.get());

        dropSelf(ModBlocks.BRICK_CONCRETE_STAIRS.get());
        dropSelf(ModBlocks.BRICK_CONCRETE_MOSSY_STAIRS.get());
        dropSelf(ModBlocks.BRICK_CONCRETE_BROKEN_STAIRS.get());
        dropSelf(ModBlocks.BRICK_CONCRETE_CRACKED_STAIRS.get());

        dropSelf(ModBlocks.ORE_OIL.get());
        dropSelf(ModBlocks.ORE_URANIUM.get());
        dropSelf(ModBlocks.ORE_URANIUM_SCORCHED.get());
        dropSelf(ModBlocks.ORE_SCHRABIDIUM.get());
        dropSelf(ModBlocks.ORE_NETHER_URANIUM.get());
        dropSelf(ModBlocks.ORE_NETHER_URANIUM_SCORCHED.get());
        dropSelf(ModBlocks.ORE_NETHER_PLUTONIUM.get());
        dropSelf(ModBlocks.ORE_NETHER_SCHRABIDIUM.get());
        dropSelf(ModBlocks.ORE_TIKITE.get());
        dropSelf(ModBlocks.ORE_GNEISS_URANIUM.get());
        dropSelf(ModBlocks.ORE_GNEISS_URANIUM_SCORCHED.get());
        dropSelf(ModBlocks.ORE_GNEISS_SCHRABIDIUM.get());

        dropSelf(ModBlocks.DET_NUKE.get());
        dropSelf(ModBlocks.DET_CHARGE.get());

        add(ModBlocks.WASTE_EARTH.get(),
                block -> createSingleItemTable(Blocks.DIRT));
        add(ModBlocks.WASTE_LOG.get(), block ->
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(AlternativesEntry.alternatives(
                                        LootItem.lootTableItem(ModItems.BURNT_BARK.get())
                                                .when(LootItemRandomChanceCondition.randomChance(0.001f))
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))),
                                        LootItem.lootTableItem(Items.COAL)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
                                ))
                )
        );
        dropSelf(ModBlocks.WASTE_PLANKS.get());
        dropSelf(ModBlocks.WASTE_MYCELIUM.get());
        dropSelf(ModBlocks.WASTE_TRINITITE_RED.get());
        dropSelf(ModBlocks.WASTE_TRINITITE.get());

        dropSelf(ModBlocks.TAINT.get());

        add(ModBlocks.BRICK_CONCRETE_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.BRICK_CONCRETE_SLAB.get()));
        add(ModBlocks.BRICK_CONCRETE_MOSSY_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.BRICK_CONCRETE_SLAB.get()));
        add(ModBlocks.BRICK_CONCRETE_BROKEN_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.BRICK_CONCRETE_SLAB.get()));
        add(ModBlocks.BRICK_CONCRETE_CRACKED_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.BRICK_CONCRETE_SLAB.get()));

        dropSelf(ModBlocks.ASH_DIGAMMA.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
