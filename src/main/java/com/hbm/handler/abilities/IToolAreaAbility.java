package com.hbm.handler.abilities;

import com.hbm.config.MainConfig;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.items.tools.ToolAbilityItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public interface IToolAreaAbility extends IBaseAbility {
    // Should call tool.breakExtraBlock on a bunch of blocks.
    // The initial block is implicitly broken, so don't call breakExtraBlock on it.
    // Returning true skips the reference block from being broken
    boolean onDig(int lvl, Level level, BlockPos pos, Player player, ToolAbilityItem tool);

    // Whether breakExtraBlock is called at all. Currently only false for explosion
    default boolean allowsHarvest(int lvl) {
        return true;
    }

    int SORT_ORDER_BASE = 0;

    // region handlers
    IToolAreaAbility NONE = new IToolAreaAbility() {
        @Override
        public String getName() {
            return "";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 0;
        }

        @Override
        public boolean onDig(int lvl, Level level, BlockPos pos, Player player, ToolAbilityItem tool) {
            return false;
        }
    };

    IToolAreaAbility RECURSION = new IToolAreaAbility() {
        @Override
        public String getName() {
            return "tool.ability.recursion";
        }

        @Override
        public boolean isAllowed() {
            return MainConfig.COMMON.ABILITY_VEIN.get();
        }

        public final int[] radiusAtLevel = { 3, 4, 5, 6, 7, 9, 10 };

        @Override
        public int levels() {
            return radiusAtLevel.length;
        }

        @Override
        public String getExtension(int level) {
            return " (" + radiusAtLevel[level] + ")";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 1;
        }

        // Note: if reusing it across different instatces of a tool
        // is a problem here, then it had already been one before
        // the refactor! The solution is to simply make this a local
        // of the onDig method and pass it around as a parameter.
        private Set<BlockPos> pos = new HashSet<>();

        @Override
        public boolean onDig(int lvl, Level level, BlockPos bpos, Player player, ToolAbilityItem tool) {
            BlockState state = level.getBlockState(bpos);

            if (state.is(Blocks.STONE) && !MainConfig.COMMON.RECURSION_STONE.get()) return false;
            if (state.is(Blocks.NETHERRACK) && !MainConfig.COMMON.RECURSION_NETHERRACK.get()) return false;

            pos.clear();
            recurse(level, bpos, bpos, player, tool, 0, radiusAtLevel[lvl]);
            return false;
        }

        private final List<BlockPos> offsets = BlockPos.betweenClosedStream(-1, -1, -1, 1, 1, 1)
                .filter(p -> !(p.getX() == 0 && p.getY() == 0 && p.getZ() == 0))
                .map(BlockPos::immutable)
                .toList();

        private void recurse(Level level, BlockPos bpos, BlockPos ref, Player player, ToolAbilityItem tool, int depth, int radius) {
            List<BlockPos> shuffled = new ArrayList<>(offsets);
            Collections.shuffle(shuffled);

            for (BlockPos off : shuffled) {
                breakExtra(level, bpos.offset(off), ref, player, tool, depth, radius);
            }
        }

        private void breakExtra(Level level, BlockPos bpos, BlockPos ref, Player player, ToolAbilityItem tool, int depth, int radius) {
            if (!pos.add(bpos)) return;

            depth++;
            if (depth > MainConfig.COMMON.RECURSION_DEPTH.get()) return;

            if (bpos.equals(ref)) return;

            Vec3 delta = new Vec3(bpos.getX() - ref.getX(), bpos.getY() - ref.getY(), bpos.getZ() - ref.getZ());
            if (delta.length() > radius) return;

            BlockState state = level.getBlockState(bpos);
            BlockState refState = level.getBlockState(ref);

            if (!isSameBlock(state, refState)) return;

            if (player.getMainHandItem().isEmpty()) return;

            tool.breakExtraBlock(level, bpos, (ServerPlayer) player, ref);
            recurse(level, bpos, ref, player, tool, depth, radius);
        }

        private boolean isSameBlock(BlockState a, BlockState b) {
            return a.is(b.getBlock());
        }
    };

    IToolAreaAbility EXPLOSION = new IToolAreaAbility() {
        @Override
        public String getName() {
            return "tool.ability.explosion";
        }

        @Override
        public boolean isAllowed() {
            return MainConfig.COMMON.ABILITY_EXPLOSION.get();
        }

        public final float[] strengthAtLevel = { 2.5F, 5F, 10F, 15F };

        @Override
        public int levels() {
            return strengthAtLevel.length;
        }

        @Override
        public String getExtension(int level) {
            return " (" + strengthAtLevel[level] + ")";
        }

        @Override
        public boolean allowsHarvest(int level) {
            return false;
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 4;
        }

        @Override
        public boolean onDig(int level, Level world, BlockPos pos, Player player, ToolAbilityItem tool) {
            float strength = strengthAtLevel[level];

            ExplosionVNT vnt = new ExplosionVNT(player.level(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, strength);
            vnt.setBlockAllocator(new BlockAllocatorStandard());
            vnt.setBlockProcessor(new BlockProcessorStandard().setAllDrop());
            vnt.explode();

            player.level().explode(player, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.1F, Level.ExplosionInteraction.NONE);

            return true;
        }
    };
    // endregion handlers

    IToolAreaAbility[] abilities = { NONE, RECURSION, /*HAMMER, HAMMER_FLAT*/ EXPLOSION };

    static IToolAreaAbility getByName(String name) {
        for(IToolAreaAbility ability : abilities) {
            if(ability.getName().equals(name))
                return ability;
        }

        return NONE;
    }
}
