package com.hbm.handler.ability;

import com.hbm.config.NtmConfig;
import com.hbm.items.tools.ToolAbilityItem;
import com.hbm.inventory.recipes.ShredderRecipes;
import com.hbm.util.TagsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface IToolHarvestAbility extends IBaseAbility {
    default void preHarvestAll(int lvl, Level level, Player player, ItemStack tool) { }
    default void postHarvestAll(int lvl, Level level, Player player, ItemStack tool) { }

    default void onHarvestBlock(Level level, BlockPos pos, Player player, BlockPos refPos) {
        BlockState state = level.getBlockState(pos);
        List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, refPos, level.getBlockEntity(refPos), player, player.getMainHandItem());
        harvestBlock(level, pos, player, refPos, drops);
    }

    default void harvestBlock(Level level, BlockPos pos, Player player, BlockPos refPos, List<ItemStack> drops) {
        for (ItemStack stack : drops) {
            if (!stack.isEmpty()) {
                Block.popResource(level, pos, stack);
            }
        }
        level.removeBlock(pos, false);

        ToolAbilityItem.damageTool(player.getMainHandItem(), player, 1);
    }

    default ItemStack getSmeltedResult(Level level, ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        SingleRecipeInput input = new SingleRecipeInput(stack.copy());

        for (var recipeHolder : level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING)) {
            var recipe = recipeHolder.value();
            if (!recipe.matches(input, level)) {
                continue;
            }

            ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
            if (result.isEmpty()) {
                return ItemStack.EMPTY;
            }

            result.setCount(result.getCount() * stack.getCount());
            return result;
        }

        return ItemStack.EMPTY;
    }

    int SORT_ORDER_BASE = 100;
    String LUCK_BASE_FORTUNE_TAG = "hbm_luck_base_fortune";

    // region handlers
    IToolHarvestAbility NONE = new IToolHarvestAbility() {
        @Override
        public String getName() {
            return "";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 0;
        }
    };

    IToolHarvestAbility SILK = new IToolHarvestAbility() {
        @Override
        public String getName() {
            return "tool.ability.silktouch";
        }

        @Override
        public boolean isAllowed() {
            return NtmConfig.COMMON.ABILITY_SILK.get();
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 1;
        }

        @Override
        public void preHarvestAll(int lvl, Level level, Player player, ItemStack tool) {
            Holder<Enchantment> silkTouch = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH);
            if (!tool.isEmpty()) {
                EnchantmentHelper.updateEnchantments(tool, mutable -> mutable.set(silkTouch, 1));
            }
        }

        @Override
        public void postHarvestAll(int lvl, Level level, Player player, ItemStack tool) {
            Holder<Enchantment> silkTouch = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH);
            if (!tool.isEmpty()) {
                EnchantmentHelper.updateEnchantments(tool, mutable -> mutable.set(silkTouch, 0));
            }
        }
    };

    IToolHarvestAbility LUCK = new IToolHarvestAbility() {
        public final int[] powerAtLevel = { 1, 2, 3, 4, 5, 9 };

        @Override
        public String getName() {
            return "tool.ability.luck";
        }

        @Override
        public boolean isAllowed() {
            return NtmConfig.COMMON.ABILITY_LUCK.get();
        }

        @Override
        public int levels() {
            return powerAtLevel.length;
        }

        @Override
        public String getExtension(int level) {
            return " (" + powerAtLevel[level] + ")";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 2;
        }

        @Override
        public void preHarvestAll(int lvl, Level level, Player player, ItemStack tool) {
            if (tool.isEmpty()) {
                return;
            }

            Holder<Enchantment> fortune = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE);
            int baseFortune = EnchantmentHelper.getItemEnchantmentLevel(fortune, tool);
            int abilityFortune = powerAtLevel[lvl];

            CompoundTag tag = TagsUtil.getCustomData(tool);
            tag.putInt(LUCK_BASE_FORTUNE_TAG, baseFortune);
            TagsUtil.putCustomData(tool, tag);

            EnchantmentHelper.updateEnchantments(tool, mutable -> mutable.set(fortune, Math.max(baseFortune, abilityFortune)));
        }

        @Override
        public void postHarvestAll(int lvl, Level level, Player player, ItemStack tool) {
            if (tool.isEmpty()) {
                return;
            }

            Holder<Enchantment> fortune = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE);
            CompoundTag tag = TagsUtil.getCustomData(tool);
            int baseFortune = tag.contains(LUCK_BASE_FORTUNE_TAG) ? tag.getInt(LUCK_BASE_FORTUNE_TAG) : 0;
            tag.remove(LUCK_BASE_FORTUNE_TAG);
            TagsUtil.putCustomData(tool, tag);

            EnchantmentHelper.updateEnchantments(tool, mutable -> mutable.set(fortune, baseFortune));
        }
    };

    IToolHarvestAbility SMELTER = new IToolHarvestAbility() {

        @Override
        public String getName() {
            return "tool.ability.smelter";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 3;
        }

        @Override
        public void onHarvestBlock(Level level, BlockPos pos, Player player, BlockPos refPos) {
            BlockState state = level.getBlockState(pos);
            List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, refPos, level.getBlockEntity(refPos), player, player.getMainHandItem());
            boolean changed = false;

            for (int i = 0; i < drops.size(); i++) {
                ItemStack stack = drops.get(i);
                ItemStack result = getSmeltedResult(level, stack);
                if (!result.isEmpty()) {
                    drops.set(i, result);
                    changed = true;
                }
            }

            if (changed || !drops.isEmpty()) {
                harvestBlock(level, pos, player, refPos, drops);
            } else {
                harvestBlock(level, pos, player, refPos, Block.getDrops(state, (ServerLevel) level, refPos, level.getBlockEntity(refPos), player, player.getMainHandItem()));
            }
        }
    };

    IToolHarvestAbility SHREDDER = new IToolHarvestAbility() {

        @Override
        public String getName() {
            return "tool.ability.shredder";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 4;
        }

        @Override
        public void onHarvestBlock(Level level, BlockPos pos, Player player, BlockPos refPos) {
            BlockState state = level.getBlockState(pos);
            ItemStack input = new ItemStack(state.getBlock().asItem());

            if (!ShredderRecipes.hasRecipe(input)) {
                IToolHarvestAbility.super.onHarvestBlock(level, pos, player, refPos);
                return;
            }

            ItemStack result = ShredderRecipes.getShredderResult(input);
            if (result.isEmpty()) {
                IToolHarvestAbility.super.onHarvestBlock(level, pos, player, refPos);
                return;
            }

            harvestBlock(level, pos, player, refPos, List.of(result));
        }
    };

    IToolHarvestAbility CENTRIFUGE = new IToolHarvestAbility() {

        @Override
        public String getName() {
            return "tool.ability.centrifuge";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 5;
        }
    };

    IToolHarvestAbility CRYSTALLIZER = new IToolHarvestAbility() {

        @Override
        public String getName() {
            return "tool.ability.crystallizer";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 6;
        }
    };

    IToolHarvestAbility MERCURY = new IToolHarvestAbility() {

        @Override
        public String getName() {
            return "tool.ability.mercury";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 7;
        }
    };
    // endregion handlers

    IToolHarvestAbility[] abilities = { NONE, SILK, LUCK, SMELTER, SHREDDER, CENTRIFUGE, CRYSTALLIZER, MERCURY };

    static IToolHarvestAbility getByName(String name) {
        for (IToolHarvestAbility ability : abilities) {
            if (ability.getName().equals(name)) {
                return ability;
            }
        }

        return NONE;
    }
}
