package com.hbm.inventory.recipes.anvil;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.machine.NTMAnvilBlock;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.items.BoltItem;
import com.hbm.items.CastPlateItem;
import com.hbm.items.NtmItems;
import com.hbm.util.InventoryUtil;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AnvilRecipes {

    private static final List<AnvilSmithingRecipe> SMITHING_RECIPES = new ArrayList<>();
    private static final List<AnvilConstructionRecipe> CONSTRUCTION_RECIPES = new ArrayList<>();

    static {
        registerSmithing();
        registerConstruction();
    }

    public static List<AnvilSmithingRecipe> getSmithing() {
        return SMITHING_RECIPES;
    }

    public static List<AnvilConstructionRecipe> getConstruction() {
        return CONSTRUCTION_RECIPES;
    }

    public static AnvilConstructionRecipe getConstruction(int index) {
        if(index < 0 || index >= CONSTRUCTION_RECIPES.size()) {
            return null;
        }
        return CONSTRUCTION_RECIPES.get(index);
    }

    public static boolean executeConstruction(Player player, int tier, int recipeIndex, int mode) {
        AnvilConstructionRecipe recipe = getConstruction(recipeIndex);
        if(recipe == null || !recipe.isTierValid(tier)) {
            return false;
        }

        int repeat = mode == 1 ? 64 : 1;
        boolean crafted = false;

        for(int i = 0; i < repeat; i++) {
            if(!InventoryUtil.doesPlayerHaveAStacks(player, recipe.input, true)) {
                break;
            }

            for(AnvilOutput output : recipe.output) {
                if(player.getRandom().nextFloat() <= output.chance) {
                    givePlayerStack(player, output.stack.copy());
                }
            }

            crafted = true;
        }

        if(crafted) {
            player.getInventory().setChanged();
            player.containerMenu.broadcastChanges();
        }

        return crafted;
    }

    private static void givePlayerStack(Player player, ItemStack stack) {
        if(stack.isEmpty()) {
            return;
        }

        ItemStack copy = stack.copy();
        if(!player.getInventory().add(copy)) {
            player.drop(copy, false);
        }
    }

    private static void registerSmithing() {
        if(!SMITHING_RECIPES.isEmpty()) {
            return;
        }

        addUpgrade(NTMAnvilBlock.Variant.IRON, NTMAnvilBlock.Variant.STEEL, NtmItems.INGOT_STEEL.get(), 10);
        addUpgrade(NTMAnvilBlock.Variant.LEAD, NTMAnvilBlock.Variant.STEEL, NtmItems.INGOT_STEEL.get(), 10);

        addUpgrade(NTMAnvilBlock.Variant.IRON, NTMAnvilBlock.Variant.DESH, NtmItems.INGOT_DESH.get(), 10);
        addUpgrade(NTMAnvilBlock.Variant.LEAD, NTMAnvilBlock.Variant.DESH, NtmItems.INGOT_DESH.get(), 10);

        addUpgrade(NTMAnvilBlock.Variant.IRON, NTMAnvilBlock.Variant.FERROURANIUM, NtmItems.INGOT_FERROURANIUM.get(), 10);
        addUpgrade(NTMAnvilBlock.Variant.LEAD, NTMAnvilBlock.Variant.FERROURANIUM, NtmItems.INGOT_FERROURANIUM.get(), 10);

        addUpgrade(NTMAnvilBlock.Variant.IRON, NTMAnvilBlock.Variant.SATURNITE, NtmItems.INGOT_SATURNITE.get(), 10);
        addUpgrade(NTMAnvilBlock.Variant.LEAD, NTMAnvilBlock.Variant.SATURNITE, NtmItems.INGOT_SATURNITE.get(), 10);

        addUpgrade(NTMAnvilBlock.Variant.IRON, NTMAnvilBlock.Variant.BISMUTH_BRONZE, NtmItems.INGOT_BISMUTH_BRONZE.get(), 10);
        addUpgrade(NTMAnvilBlock.Variant.LEAD, NTMAnvilBlock.Variant.BISMUTH_BRONZE, NtmItems.INGOT_BISMUTH_BRONZE.get(), 10);

        addUpgrade(NTMAnvilBlock.Variant.IRON, NTMAnvilBlock.Variant.ARSENIC_BRONZE, NtmItems.INGOT_ARSENIC_BRONZE.get(), 10);
        addUpgrade(NTMAnvilBlock.Variant.LEAD, NTMAnvilBlock.Variant.ARSENIC_BRONZE, NtmItems.INGOT_ARSENIC_BRONZE.get(), 10);

        addUpgrade(NTMAnvilBlock.Variant.IRON, NTMAnvilBlock.Variant.FERRIC_SCHRABIDATE, NtmItems.INGOT_SCHRABIDATE.get(), 10);
        addUpgrade(NTMAnvilBlock.Variant.LEAD, NTMAnvilBlock.Variant.FERRIC_SCHRABIDATE, NtmItems.INGOT_SCHRABIDATE.get(), 10);

        addUpgrade(NTMAnvilBlock.Variant.IRON, NTMAnvilBlock.Variant.DINEUTRONIUM, NtmItems.INGOT_DINEUTRONIUM.get(), 10);
        addUpgrade(NTMAnvilBlock.Variant.LEAD, NTMAnvilBlock.Variant.DINEUTRONIUM, NtmItems.INGOT_DINEUTRONIUM.get(), 10);

        addUpgrade(NTMAnvilBlock.Variant.IRON, NTMAnvilBlock.Variant.OSMIRIDIUM, NtmItems.INGOT_OSMIRIDIUM.get(), 10);
        addUpgrade(NTMAnvilBlock.Variant.LEAD, NTMAnvilBlock.Variant.OSMIRIDIUM, NtmItems.INGOT_OSMIRIDIUM.get(), 10);

        SMITHING_RECIPES.add(new AnvilSmithingCyanideRecipe());
        SMITHING_RECIPES.add(new AnvilSmithingRenameRecipe());
    }

    private static void addUpgrade(NTMAnvilBlock.Variant input, NTMAnvilBlock.Variant output, ItemLike ingredient, int amount) {
        ItemStack result = new ItemStack(NtmBlocks.ANVIL.asItem());
        MetaHelper.setMeta(result, output.ordinal());

        SMITHING_RECIPES.add(new AnvilSmithingRecipe(
                1,
                result,
                new ComparableStack(NtmBlocks.ANVIL.asItem(), 1, input.ordinal()),
                new ComparableStack(ingredient.asItem(), amount)
        ));
    }

    private static void registerConstruction() {
        if(!CONSTRUCTION_RECIPES.isEmpty()) {
            return;
        }

        addAnvilRecipe(Items.IRON_INGOT, 1, NtmItems.PLATE_IRON.get(), 1,3);
        addAnvilRecipe(Items.GOLD_INGOT, 1,NtmItems.PLATE_GOLD.get(), 1,3);
        addAnvilRecipe(Items.COPPER_INGOT, 1,NtmItems.PLATE_COPPER.get(), 1,3);
        addAnvilRecipe(NtmItems.INGOT_TITANIUM.get(), 1, NtmItems.PLATE_TITANIUM.get(), 1,3);
        addAnvilRecipe(NtmItems.INGOT_ALUMINIUM.get(), 1,NtmItems.PLATE_ALUMINIUM.get(), 1,3);
        addAnvilRecipe(NtmItems.INGOT_STEEL.get(), 1,NtmItems.PLATE_STEEL.get(), 1, 3);
        addAnvilRecipe(NtmItems.INGOT_LEAD.get(), 1,NtmItems.PLATE_LEAD.get(), 1,3);
        addAnvilRecipe(NtmItems.INGOT_GUNMETAL.get(), 1,NtmItems.PLATE_GUNMETAL.get(), 1,3);
        addAnvilRecipe(NtmItems.INGOT_WEAPON_STEEL.get(), 1,NtmItems.PLATE_WEAPON_STEEL.get(), 1,3);
        addAnvilRecipe(NtmItems.INGOT_SATURNITE.get(), 1,NtmItems.PLATE_SATURNITE.get(), 1, 3);
        addAnvilRecipe(NtmItems.INGOT_DURA_STEEL.get(), 1,NtmItems.PLATE_DURA_STEEL.get(), 1,3);
        addAnvilRecipe(NtmItems.INGOT_SCHRABIDIUM.get(), 1,NtmItems.PLATE_SCHRABIDIUM.get(), 1,3);
        addAnvilRecipe(NtmItems.INGOT_COMBINE_STEEL.get(), 1,NtmItems.PLATE_COMBINE_STEEL.get(), 1, 3);
        addAnvilRecipe(NtmItems.INGOT_BISMUTH.get(), 1,NtmItems.PLATE_BISMUTH.get(), 1,3);
        addAnvilRecipe(Items.GOLD_INGOT, 1,NtmItems.WIRE_GOLD.get(), 8, 4);
        addAnvilRecipe(Items.COPPER_INGOT, 1,NtmItems.WIRE_COPPER.get(), 8, 4);
        addAnvilRecipe(NtmItems.INGOT_ALUMINIUM.get(), 1,NtmItems.WIRE_ALUMINIUM.get(), 8, 4);
        addAnvilRecipe(NtmItems.INGOT_ZIRCONIUM.get(), 1,NtmItems.WIRE_ZIRCONIUM.get(), 8, 4);
        addAnvilRecipe(NtmItems.INGOT_LEAD.get(), 1,NtmItems.WIRE_LEAD.get(), 8, 4);
        addAnvilRecipe(NtmItems.INGOT_TUNGSTEN.get(), 1,NtmItems.WIRE_TUNGSTEN.get(), 8, 4);
        addAnvilRecipe(NtmItems.INGOT_SCHRABIDIUM.get(), 1,NtmItems.WIRE_SCHRABIDIUM.get(), 8, 4);
        addAnvilRecipe(NtmItems.INGOT_STEEL.get(), 1,NtmItems.WIRE_STEEL.get(), 8, 4);
        addAnvilRecipe(NtmItems.INGOT_MAGNETIZED_TUNGSTEN.get(), 1,NtmItems.WIRE_MAGNETIZED_TUNGSTEN.get(), 8, 4);
        addAnvilRecipe(NtmItems.INGOT_GRAPHITE.get(), 1,NtmItems.WIRE_CARBON.get(), 8, 4);
        addAnvilRecipe(NtmItems.INGOT_RED_COPPER.get(), 1,NtmItems.WIRE_RED_COPPER.get(), 8, 4);
        CONSTRUCTION_RECIPES.add(new AnvilConstructionRecipe(
                new AStack[] {
                        new ComparableStack(NtmItems.INGOT_STEEL.get(), 8),
                        new ComparableStack(NtmItems.PLATE_COPPER.get(), 4),
                        new ComparableStack(NtmItems.MOTOR.get(), 2),
                        new ComparableStack(NtmItems.CIRCUIT_VACUUM_TUBE.get(), 4)
                },
                new AnvilOutput(new ItemStack(NtmBlocks.MACHINE_ASSEMBLY_MACHINE.asItem()))
        ).setTier(1).setOverlay(OverlayType.CONSTRUCTION));

        CONSTRUCTION_RECIPES.add(new AnvilConstructionRecipe(
                new AStack[] {
                        new ComparableStack(Blocks.STONE_BRICKS, 4),
                        new ComparableStack(NtmItems.INGOT_FIREBRICK.get(), 32),
                        new ComparableStack(NtmItems.PLATE_COPPER.get(), 8)
                },
                new AnvilOutput(new ItemStack(NtmBlocks.MACHINE_BLAST_FURNACE.asItem()))
        ).setTier(1).setOverlay(OverlayType.CONSTRUCTION));

        CONSTRUCTION_RECIPES.add(new AnvilConstructionRecipe(
                new AStack[] {
                        new ComparableStack(Blocks.STONE_BRICKS, 8),
                        new ComparableStack(Items.BRICK, 16),
                        NtmItems.castPlateIngredient(CastPlateItem.Type.COPPER, 2),
                        new RecipesCommon.TagStack(ItemTags.LOGS, 16),
                },
                new AnvilOutput(new ItemStack(NtmBlocks.FURNACE_COMBINATION.asItem()))
        ).setTier(2).setOverlay(OverlayType.CONSTRUCTION));

        CONSTRUCTION_RECIPES.add(new AnvilConstructionRecipe(
                new AStack[] {
                        new ComparableStack(NtmItems.CIRCUIT_VACUUM_TUBE.get(), 2),
                        new ComparableStack(NtmItems.COIL_COPPER.get(), 4),
                        NtmItems.castPlateIngredient(CastPlateItem.Type.STEEL, 2),
                        new ComparableStack(NtmItems.BOLT.get(), 1, BoltItem.Type.TUNGSTEN.meta)
                },
                new AnvilOutput(new ItemStack(NtmBlocks.MACHINE_SOLDERING_STATION.asItem()))
        ).setTier(2).setOverlay(OverlayType.CONSTRUCTION));

        CONSTRUCTION_RECIPES.add(new AnvilConstructionRecipe(
                new AStack[] {
                        new ComparableStack(NtmItems.COIL_COPPER.get(), 1),
                        new ComparableStack(NtmItems.PLATE_IRON.get(), 2),
                        NtmItems.castPlateIngredient(CastPlateItem.Type.STEEL, 2),
                        NtmItems.castPlateIngredient(CastPlateItem.Type.DURA_STEEL, 1),
                },
                new AnvilOutput(new ItemStack(NtmItems.MOTOR.asItem()))

        ).setTier(2).setOverlay(OverlayType.CONSTRUCTION));
        addAnvilRecipe(NtmItems.PLATE_TITANIUM.get(), 4, NtmItems.SHELL_TITANIUM.get(), 1, 1);
        addAnvilRecipe(NtmItems.PLATE_ALUMINIUM.get(), 4, NtmItems.SHELL_ALUMINIUM.get(), 1, 1);
        addAnvilRecipe(NtmItems.PLATE_COPPER.get(), 4, NtmItems.SHELL_COPPER.get(), 1, 1);
        addAnvilRecipe(NtmItems.PLATE_STEEL.get(), 4, NtmItems.SHELL_STEEL.get(), 1, 1);
        addAnvilRecipe(NtmItems.PLATE_WEAPON_STEEL.get(), 4, NtmItems.SHELL_WEAPON_STEEL.get(), 1, 1);
        addAnvilRecipe(NtmItems.PLATE_SATURNITE.get(), 4, NtmItems.SHELL_SATURNITE.get(), 1, 1);
        addAnvilRecipe(NtmItems.PLATE_IRON.get(), 3, NtmItems.PIPE_IRON.get(), 1, 1);
        addAnvilRecipe(NtmItems.PLATE_COPPER.get(), 3, NtmItems.PIPE_COPPER.get(), 1, 1);
        addAnvilRecipe(NtmItems.PLATE_ALUMINIUM.get(), 3, NtmItems.PIPE_ALUMINIUM.get(), 1, 1);
        addAnvilRecipe(NtmItems.PLATE_LEAD.get(), 3, NtmItems.PIPE_LEAD.get(), 1, 1);
        addAnvilRecipe(NtmItems.PLATE_STEEL.get(), 3, NtmItems.PIPE_STEEL.get(), 1, 1);
        addAnvilRecipe(NtmItems.PLATE_DURA_STEEL.get(), 3, NtmItems.PIPE_DURA_STEEL.get(), 1, 1);
        addAnvilRecipe(NtmItems.INGOT_RUBBER.get(), 3, NtmItems.PIPE_RUBBER.get(), 1, 1);
        addAnvilRecipe(NtmItems.COIL_COPPER.get(), 2, NtmItems.COIL_COPPER_RING.get(), 1, 1);
        addAnvilRecipe(NtmItems.COIL_GOLD.get(), 2, NtmItems.COIL_GOLD_RING.get(), 1, 1);
        addAnvilRecipe(NtmItems.STAMP_STONE_FLAT.get(), 1, NtmItems.STAMP_STONE_PLATE.get(), 1, 1);
        addAnvilRecipe(NtmItems.STAMP_IRON_FLAT.get(), 1, NtmItems.STAMP_IRON_PLATE.get(), 1, 1);
        addAnvilRecipe(NtmItems.STAMP_TITANIUM_FLAT.get(), 1, NtmItems.STAMP_STEEL_PLATE.get(), 1, 2);
        addAnvilRecipe(NtmItems.STAMP_STEEL_FLAT.get(), 1, NtmItems.STAMP_TITANIUM_PLATE.get(), 1, 2);
        addAnvilRecipe(NtmItems.STAMP_OBSIDIAN_FLAT.get(), 1, NtmItems.STAMP_OBSIDIAN_PLATE.get(), 1, 2);
        addAnvilRecipe(NtmItems.STAMP_DESH_FLAT.get(), 1, NtmItems.STAMP_DESH_PLATE.get(), 1, 3);
        addAnvilRecipe(NtmItems.STAMP_STONE_FLAT.get(), 1, NtmItems.STAMP_STONE_WIRE.get(), 1, 1);
        addAnvilRecipe(NtmItems.STAMP_IRON_FLAT.get(), 1, NtmItems.STAMP_IRON_WIRE.get(), 1, 1);
        addAnvilRecipe(NtmItems.STAMP_TITANIUM_FLAT.get(), 1, NtmItems.STAMP_STEEL_WIRE.get(), 1, 2);
        addAnvilRecipe(NtmItems.STAMP_STEEL_FLAT.get(), 1, NtmItems.STAMP_TITANIUM_WIRE.get(), 1, 2);
        addAnvilRecipe(NtmItems.STAMP_OBSIDIAN_FLAT.get(), 1, NtmItems.STAMP_OBSIDIAN_WIRE.get(), 1, 2);
        addAnvilRecipe(NtmItems.STAMP_DESH_FLAT.get(), 1, NtmItems.STAMP_DESH_WIRE.get(), 1, 3);
        addAnvilRecipe(NtmItems.STAMP_STONE_FLAT.get(), 1, NtmItems.STAMP_STONE_CIRCUIT.get(), 1, 1);
        addAnvilRecipe(NtmItems.STAMP_IRON_FLAT.get(), 1, NtmItems.STAMP_IRON_CIRCUIT.get(), 1, 1);
        addAnvilRecipe(NtmItems.STAMP_TITANIUM_FLAT.get(), 1, NtmItems.STAMP_STEEL_CIRCUIT.get(), 1, 2);
        addAnvilRecipe(NtmItems.STAMP_STEEL_FLAT.get(), 1, NtmItems.STAMP_TITANIUM_CIRCUIT.get(), 1, 2);
        addAnvilRecipe(NtmItems.STAMP_OBSIDIAN_FLAT.get(), 1, NtmItems.STAMP_OBSIDIAN_CIRCUIT.get(), 1, 2);
        addAnvilRecipe(NtmItems.STAMP_DESH_FLAT.get(), 1, NtmItems.STAMP_DESH_CIRCUIT.get(), 1, 3);


        addRecycleRecipe(NtmBlocks.DECO_ALUMINIUM.get(), NtmItems.INGOT_ALUMINIUM.get(), 4, 1);
        addRecycleRecipe(NtmBlocks.DECO_BERYLLIUM.get(), NtmItems.INGOT_BERYLLIUM.get(), 4, 1);
        addRecycleRecipe(NtmBlocks.DECO_LEAD.get(), NtmItems.INGOT_LEAD.get(), 4, 1);
        addRecycleRecipe(NtmBlocks.DECO_RED_COPPER.get(), NtmItems.INGOT_RED_COPPER.get(), 4, 1);
        addRecycleRecipe(NtmBlocks.DECO_STEEL.get(), NtmItems.INGOT_STEEL.get(), 4, 1);
        addRecycleRecipe(NtmBlocks.DECO_RUSTY_STEEL.get(), NtmItems.INGOT_STEEL.get(), 8, 1);
        addRecycleRecipe(NtmBlocks.DECO_TITANIUM.get(), NtmItems.INGOT_TITANIUM.get(), 4, 1);
        addRecycleRecipe(NtmBlocks.DECO_TUNGSTEN.get(), NtmItems.INGOT_TUNGSTEN.get(), 4, 1);
        addRecycleRecipe(NtmBlocks.DECO_ASBESTOS.get(), NtmItems.INGOT_ASBESTOS.get(), 4, 1);

    }

    private static void addAnvilRecipe(ItemLike input, int inputCount, ItemLike output, int outputCount, int tier) {
        CONSTRUCTION_RECIPES.add(new AnvilConstructionRecipe(
                new ComparableStack(input.asItem(), inputCount),
                new AnvilOutput(new ItemStack(output.asItem(), outputCount))
        ).setTier(tier).setOverlay(OverlayType.CONSTRUCTION));
    }

    private static void addRecycleRecipe(ItemLike inputBlock, ItemLike output, int inputCount, int tier) {
        CONSTRUCTION_RECIPES.add(new AnvilConstructionRecipe(
                new ComparableStack(inputBlock.asItem(), inputCount),
                new AnvilOutput(new ItemStack(output.asItem()))
        ).setTier(tier).setOverlay(OverlayType.RECYCLING));
    }

    public static class AnvilConstructionRecipe {
        public final List<AStack> input = new ArrayList<>();
        public final List<AnvilOutput> output = new ArrayList<>();
        public int tierLower = 0;
        public int tierUpper = -1;
        private OverlayType overlay = OverlayType.NONE;

        public AnvilConstructionRecipe(AStack input, AnvilOutput output) {
            this.input.add(input);
            this.output.add(output);
            this.setOverlay(OverlayType.SMITHING);
        }

        public AnvilConstructionRecipe(AStack[] input, AnvilOutput output) {
            Collections.addAll(this.input, input);
            this.output.add(output);
            this.setOverlay(OverlayType.CONSTRUCTION);
        }

        public AnvilConstructionRecipe(AStack input, AnvilOutput[] output) {
            this.input.add(input);
            Collections.addAll(this.output, output);
            this.setOverlay(OverlayType.RECYCLING);
        }

        public AnvilConstructionRecipe(AStack[] input, AnvilOutput[] output) {
            Collections.addAll(this.input, input);
            Collections.addAll(this.output, output);
            this.setOverlay(OverlayType.NONE);
        }

        public AnvilConstructionRecipe setTier(int tier) {
            this.tierLower = tier;
            this.tierUpper = -1;
            return this;
        }

        public AnvilConstructionRecipe setTierRange(int lower, int upper) {
            this.tierLower = lower;
            this.tierUpper = upper;
            return this;
        }

        public boolean isTierValid(int tier) {
            if(this.tierUpper == -1) {
                return tier >= this.tierLower;
            }
            return tier >= this.tierLower && tier <= this.tierUpper;
        }

        public AnvilConstructionRecipe setOverlay(OverlayType overlay) {
            this.overlay = overlay;
            return this;
        }

        public OverlayType getOverlay() {
            return this.overlay;
        }

        public ItemStack getDisplay() {
            return switch(this.overlay) {
                case RECYCLING -> this.getFirstComparableInput();
                default -> this.output.getFirst().stack.copy();
            };
        }

        public boolean matchesSearch(String search) {
            String needle = search.toLowerCase(Locale.US);
            for(String term : this.searchTerms()) {
                if(term.contains(needle)) {
                    return true;
                }
            }
            return false;
        }

        public List<String> searchTerms() {
            List<String> terms = new ArrayList<>();
            for(AStack stack : this.input) {
                for(ItemStack itemStack : stack.extractForJEI()) {
                    terms.add(itemStack.getHoverName().getString().toLowerCase(Locale.US));
                }
            }
            for(AnvilOutput stack : this.output) {
                terms.add(stack.stack.getHoverName().getString().toLowerCase(Locale.US));
            }
            terms.add(Integer.toString(this.tierLower));
            terms.add(Integer.toString(this.tierUpper));
            terms.add(this.overlay.name().toLowerCase(Locale.US));
            return terms;
        }

        private ItemStack getFirstComparableInput() {
            for(AStack stack : this.input) {
                if(stack instanceof ComparableStack comparable) {
                    return comparable.toStack();
                }
            }
            return this.output.getFirst().stack.copy();
        }
    }

    public static class AnvilOutput {
        public final ItemStack stack;
        public final float chance;

        public AnvilOutput(ItemStack stack) {
            this(stack, 1F);
        }

        public AnvilOutput(ItemStack stack, float chance) {
            this.stack = stack;
            this.chance = chance;
        }
    }

    public enum OverlayType {
        NONE,
        CONSTRUCTION,
        RECYCLING,
        SMITHING
    }
}
