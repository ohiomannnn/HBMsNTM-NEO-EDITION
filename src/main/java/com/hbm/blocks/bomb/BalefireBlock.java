package com.hbm.blocks.bomb;

import com.hbm.lib.ModEffect;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.awt.*;

public class BalefireBlock extends BaseFireBlock {

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 15);

    public BalefireBlock(Properties props) {
        super(props, 1.0F);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    public static final MapCodec<BalefireBlock> CODEC = simpleCodec(BalefireBlock::new);

    @Override
    protected MapCodec<? extends BaseFireBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    }
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected boolean canBurn(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            level.scheduleTick(pos, this, getCustomFireTickDelay(level.getRandom()));
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) return;

        if (!canSurvive(state, level, pos)) {
            level.removeBlock(pos, false);
            return;
        }

        int age = state.getValue(AGE);

        if (age < 15) {
            level.scheduleTick(pos, this, getCustomFireTickDelay(rand));
        }

        if (!canCatchFireAround(level, pos) && !level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        } else {
            if (age < 15) {
                for (Direction dir : Direction.values()) {
                    tryCatchFire(level, pos.relative(dir), 300, rand, age, dir.getOpposite());
                }

                int h = 3;
                for (int dx = -h; dx <= h; dx++) {
                    for (int dz = -h; dz <= h; dz++) {
                        for (int dy = -1; dy <= 4; dy++) {
                            BlockPos check = pos.offset(dx, dy, dz);
                            if (check.equals(pos)) continue;

                            int fireLimit = 100;
                            if (dy > 1) fireLimit += (dy - 1) * 100;

                            BlockState bs = level.getBlockState(check);
                            if (bs.is(this)) {
                                int neighAge = bs.getValue(AGE);
                                if (neighAge > age + 1) {
                                    level.setBlock(check, this.defaultBlockState().setValue(AGE, Math.min(15, age + 1)), 3);
                                    continue;
                                }
                            }

                            if (level.isEmptyBlock(check)) {
                                int spread = getNeighborEncouragement(level, check);
                                if (spread > 0) {
                                    int adjusted = (spread + 40 + level.getDifficulty().getId() * 7) / (age + 30);
                                    if (adjusted > 0 && rand.nextInt(fireLimit) <= adjusted) {
                                        level.setBlock(check, this.defaultBlockState().setValue(AGE, Math.min(15, age + 1)), 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void tryCatchFire(ServerLevel level, BlockPos pos, int chance, RandomSource rand, int fireAge, Direction face) {
        BlockState target = level.getBlockState(pos);
        int flammability = target.getFlammability(level, pos, face);

        if (rand.nextInt(chance) < flammability) {
            boolean isTnt = target.is(Blocks.TNT);
            level.setBlock(pos, this.defaultBlockState().setValue(AGE, Math.min(15, fireAge + 1)), 3);
            if (isTnt) {
                Blocks.TNT.onCaughtFire(target, level, pos, null, null);
            }
        }
    }

    private boolean canCatchFireAround(LevelReader level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockPos np = pos.relative(dir);
            BlockState ns = level.getBlockState(np);
            if (ns.isFlammable(level, np, dir.getOpposite())) return true;
        }
        return false;
    }

    private int getNeighborEncouragement(LevelReader level, BlockPos pos) {
        if (!level.isEmptyBlock(pos)) return 0;
        int best = 0;
        for (Direction dir : Direction.values()) {
            BlockPos np = pos.relative(dir);
            BlockState ns = level.getBlockState(np);
            int f = ns.getFlammability(level, np, dir.getOpposite());
            if (f > best) best = f;
        }
        return best;
    }

    private int getCustomFireTickDelay(RandomSource rand) {
        return 30 + rand.nextInt(10);
    }

    @Override
    protected void entityInside(BlockState state, net.minecraft.world.level.Level level, BlockPos pos, Entity entity) {
        entity.setRemainingFireTicks(10 * 20);
        if (entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(ModEffect.RADIATION, 5 * 20, 9));
        }
    }

    public int getColor(BlockState state, BlockGetter getter, BlockPos pos) {
        int age = state.getValue(AGE);
        return Color.HSBtoRGB(0F, 0F, 1F - age / 30F);
    }
}