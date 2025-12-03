package com.hbm.handler.ability;

import com.hbm.config.MainConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
        BlockState refState = level.getBlockState(refPos);

        if (state.is(refState.getBlock())) {
            List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, level.getBlockEntity(pos), player, player.getMainHandItem());
            for (ItemStack stack : drops) {
                if (!stack.isEmpty()) {
                    Block.popResource(level, refPos, stack);
                }
            }
            level.removeBlock(pos, false);

            player.getMainHandItem().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
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

    IToolHarvestAbility LUCK = new IToolHarvestAbility() {
        @Override
        public String getName() {
            return "tool.ability.luck";
        }

        @Override
        public boolean isAllowed() {
            return MainConfig.COMMON.ABILITY_LUCK.get();
        }

        public final int[] powerAtLevel = { 1, 2, 3, 4, 5, 9 };

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
            Holder<Enchantment> fortune = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE);
            if (!tool.isEmpty()) {
                EnchantmentHelper.updateEnchantments(tool, mutable -> mutable.set(fortune, 1));
            }
        }

        @Override
        public void postHarvestAll(int lvl, Level level, Player player, ItemStack tool) {
            Holder<Enchantment> fortune = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE);
            if (!tool.isEmpty()) {
                EnchantmentHelper.updateEnchantments(tool, mutable -> mutable.set(fortune, 0));
            }
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
