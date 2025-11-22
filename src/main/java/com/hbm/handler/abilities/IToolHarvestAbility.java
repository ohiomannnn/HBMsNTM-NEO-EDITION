package com.hbm.handler.abilities;

import com.hbm.config.MainConfig;
import com.hbm.items.tools.ToolAbilityItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public interface IToolHarvestAbility extends IBaseAbility {
    default void preHarvestAll(int lvl, Level level, Player player, ItemStack tool) { }
    default void postHarvestAll(int lvl, Level level, Player player, ItemStack tool) { }

    // You must call harvestBlock to actually break the block.
    // If you don't, visual glitches ensue
    default void onHarvestBlock(int lvl, Level level, BlockPos pos, Player player, BlockState state) {
        harvestBlock(false, level, pos, player);
    }

    static void harvestBlock(boolean skipDefaultDrops, Level level, BlockPos pos, Player player) {
        if (skipDefaultDrops) {
            // Emulate the block breaking without drops
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (!stack.isEmpty()) stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        } else if (player instanceof ServerPlayer serverPlayer) {
            // Break the block conventionally
            ToolAbilityItem.standardDigPost(level, pos, serverPlayer);
        }
    }

    int SORT_ORDER_BASE = 100;

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
            return MainConfig.COMMON.ABILITY_SILK.get();
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

    IToolHarvestAbility[] abilities = { NONE, SILK, /*LUCK, SMELTER, SHREDDER, CENTRIFUGE, CRYSTALLIZER, MERCURY*/ };

    static IToolHarvestAbility getByName(String name) {
        for (IToolHarvestAbility ability : abilities) {
            if (ability.getName().equals(name)) {
                return ability;
            }
        }

        return NONE;
    }
}
