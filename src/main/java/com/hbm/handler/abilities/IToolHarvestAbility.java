package com.hbm.handler.abilities;

import com.hbm.config.MainConfig;
import com.hbm.items.tools.ToolAbilityItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public interface IToolHarvestAbility extends IBaseAbility {
    default void preHarvestAll(int lvl, Level level, Player player) { }
    default void postHarvestAll(int lvl, Level level, Player player) { }

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
            player.getItemInHand(InteractionHand.MAIN_HAND).setDamageValue(stack.getDamageValue() - 1);
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
        public void preHarvestAll(int lvl, Level level, Player player) {
        }

        @Override
        public void postHarvestAll(int lvl, Level level, Player player) {
            // ToC-ToU mismatch should be impossible
            // because both calls happen on the same tick.
            // Even if can be forced somehow, the player doesn't gain any
            // benefit from it.
        }
    };

    IToolHarvestAbility[] abilities = { NONE, SILK, /*LUCK, SMELTER, SHREDDER, CENTRIFUGE, CRYSTALLIZER, MERCURY*/ };

    static IToolHarvestAbility getByName(String name) {
        for(IToolHarvestAbility ability : abilities) {
            if(ability.getName().equals(name))
                return ability;
        }

        return NONE;
    }
}
