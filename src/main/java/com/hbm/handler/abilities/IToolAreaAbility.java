package com.hbm.handler.abilities;

import com.hbm.config.MainConfig;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.explosion.vanillant.standard.ExplosionEffectStandard;
import com.hbm.items.tools.ToolAbilityItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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

        private Set<BlockPos> posSet = new HashSet<>();

        @Override
        public boolean onDig(int lvl, Level level, BlockPos pos, Player player, ToolAbilityItem tool) {
            BlockState state = level.getBlockState(pos);

            if (state.is(Blocks.STONE) && !MainConfig.COMMON.RECURSION_STONE.get()) return false;
            if (state.is(Blocks.NETHERRACK) && !MainConfig.COMMON.RECURSION_NETHERRACK.get()) return false;

            posSet.clear();

            recurse(level, pos, pos, player, tool, 0, radiusAtLevel[lvl]);

            return false;
        }

        private final List<BlockPos> offsets = new ArrayList<>(3 * 3 * 3 - 1) {
            {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            if (dx != 0 || dy != 0 || dz != 0) {
                                add(new BlockPos(dx, dy, dz));
                            }
                        }
                    }
                }
            }
        };

        private void recurse(Level level, BlockPos pos, BlockPos ref, Player player, ToolAbilityItem tool, int depth, int radius) {
            List<BlockPos> shuffled = new ArrayList<>(offsets);
            Collections.shuffle(shuffled);

            for (BlockPos offset : shuffled) {
                breakExtra(level, pos.offset(offset), ref, player, tool, depth, radius);
            }
        }

        private void breakExtra(Level level, BlockPos pos, BlockPos ref, Player player, ToolAbilityItem tool, int depth, int radius) {
            if (!posSet.add(pos)) return;

            depth++;
            if (depth > MainConfig.COMMON.RECURSION_DEPTH.get()) return;

            if (pos.equals(ref)) return;

            Vec3 delta = new Vec3(pos.getX() - ref.getX(), pos.getY() - ref.getY(), pos.getZ() - ref.getZ());
            if (delta.length() > radius) return;

            BlockState state = level.getBlockState(pos);
            BlockState refState = level.getBlockState(ref);

            if (!isSameBlock(state, refState)) return;

            if (player.getMainHandItem().isEmpty()) return;

            tool.breakExtraBlock(level, pos, player, ref);
            recurse(level, pos, ref, player, tool, depth, radius);
        }

        private boolean isSameBlock(BlockState blockStateA, BlockState blockStateB) {
            return blockStateA.is(blockStateB.getBlock());
        }
    };

    IToolAreaAbility HAMMER = new IToolAreaAbility() {
        @Override
        public String getName() {
            return "tool.ability.hammer";
        }

        @Override
        public boolean isAllowed() {
            return MainConfig.COMMON.ABILITY_HAMMER.get();
        }

        public final int[] rangeAtLevel = { 1, 2, 3, 4 };

        @Override
        public int levels() {
            return rangeAtLevel.length;
        }

        @Override
        public String getExtension(int level) {
            return " (" + rangeAtLevel[level] + ")";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 2;
        }

        @Override
        public boolean onDig(int lvl, Level level, BlockPos pos, Player player, ToolAbilityItem tool) {
            int range = rangeAtLevel[lvl];

            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            for (int a = x - range; a <= x + range; a++) {
                for (int b = y - range; b <= y + range; b++) {
                    for (int c = z - range; c <= z + range; c++) {
                        if (a == x && b == y && c == z) continue;

                        tool.breakExtraBlock(level, new BlockPos(a, b, c), player, pos);
                    }
                }
            }

            return false;
        }
    };

    IToolAreaAbility HAMMER_FLAT = new IToolAreaAbility() {
        @Override
        public String getName() {
            return "tool.ability.hammer_flat";
        }

        @Override
        public boolean isAllowed() {
            return MainConfig.COMMON.ABILITY_HAMMER.get();
        }

        public final int[] rangeAtLevel = { 1, 2, 3, 4 };

        @Override
        public int levels() {
            return rangeAtLevel.length;
        }

        @Override
        public String getExtension(int level) {
            return " (" + rangeAtLevel[level] + ")";
        }

        @Override
        public int sortOrder() {
            return SORT_ORDER_BASE + 3;
        }

        @Override
        public boolean onDig(int lvl, Level level, BlockPos pos, Player player, ToolAbilityItem tool) {

            int range = rangeAtLevel[lvl];

            HitResult hit = raytraceFromEntity(level, player, false, 4.5d);
            if (hit.getType() != HitResult.Type.BLOCK) {
                return true;
            }

            BlockHitResult blockHit = (BlockHitResult) hit;
            Direction sideHit = blockHit.getDirection();

            // we successfully destroyed a block. time to do AOE!
            int xRange = range;
            int yRange = range;
            int zRange = switch (sideHit) {
                case DOWN, UP -> {
                    yRange = 0;
                    yield range;
                }
                case NORTH, SOUTH -> 0;
                case WEST, EAST -> {
                    xRange = 0;
                    yield range;
                }
            };

            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            for(int a = x - xRange; a <= x + xRange; a++) {
                for (int b = y - yRange; b <= y + yRange; b++) {
                    for (int c = z - zRange; c <= z + zRange; c++) {
                        if (a == x && b == y && c == z) continue;

                        tool.breakExtraBlock(level, new BlockPos(a, b, c), player, pos);
                    }
                }
            }

            return false;
        }

        private HitResult raytraceFromEntity(Level level, Player player, boolean includeLiquids, double range) {
            Vec3 eyePos = player.getEyePosition(1.0F);
            Vec3 lookVec = player.getViewVector(1.0F);
            Vec3 targetPos = eyePos.add(lookVec.scale(range));
            ClipContext.Fluid fluidMode = includeLiquids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE;
            ClipContext context = new ClipContext(eyePos, targetPos, ClipContext.Block.COLLIDER, fluidMode, player);
            return level.clip(context);
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
        public boolean onDig(int lvl, Level level, BlockPos pos, Player player, ToolAbilityItem tool) {
            float strength = strengthAtLevel[lvl];

            ExplosionVNT vnt = new ExplosionVNT(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, strength);
            vnt.setBlockAllocator(new BlockAllocatorStandard());
            vnt.setBlockProcessor(new BlockProcessorStandard().setAllDrop());
            vnt.explode();

            level.explode(player,pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.1F, Level.ExplosionInteraction.NONE);

            return true;
        }
    };
    // endregion handlers

    IToolAreaAbility[] abilities = { NONE, RECURSION, HAMMER, HAMMER_FLAT, EXPLOSION };

    static IToolAreaAbility getByName(String name) {
        for (IToolAreaAbility ability : abilities) {
            if (ability.getName().equals(name)) {
                return ability;
            }
        }

        return NONE;
    }
}
