package com.hbm.blocks.bomb;

import com.hbm.blocks.ModBlocks;
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

public class BalefireBlock extends BaseFireBlock {

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 15);

    public BalefireBlock(Properties properties) {
        super(properties, 1.0F);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    public static final MapCodec<BalefireBlock> CODEC = simpleCodec(BalefireBlock::new);
    @Override protected MapCodec<BalefireBlock> codec() { return CODEC; }

    @Override protected VoxelShape getCollisionShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) { return Shapes.empty(); }
    @Override protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return Shapes.empty(); }

    @Override protected boolean canBurn(BlockState state) { return true; }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        level.scheduleTick(pos, this, this.tickRate(level) + level.random.nextInt(10));
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {

            if (!this.canSurvive(state, level, pos)) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }

            int age = state.getValue(AGE);

            if (age < 15) level.scheduleTick(pos, this, this.tickRate(level) + random.nextInt(10));

            if (!this.canNeighborBurn(level, pos) && !level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            } else {
                if (age < 15) {
                    int x = pos.getX();
                    int y = pos.getY();
                    int z = pos.getZ();
                    this.tryCatchFire(level, new BlockPos(x + 1, y, z), 500, random, age, Direction.WEST);
                    this.tryCatchFire(level, new BlockPos(x - 1, y, z), 500, random, age, Direction.EAST);
                    this.tryCatchFire(level, new BlockPos(x, y - 1, z), 300, random, age, Direction.UP);
                    this.tryCatchFire(level, new BlockPos(x, y + 1, z), 300, random, age, Direction.DOWN);
                    this.tryCatchFire(level, new BlockPos(x, y, z - 1), 500, random, age, Direction.SOUTH);
                    this.tryCatchFire(level, new BlockPos(x, y, z + 1), 500, random, age, Direction.NORTH);

                    int h = 3;

                    for (int ix = x - h; ix <= x + h; ++ix) {
                        for (int iz = z - h; iz <= z + h; ++iz) {
                            for (int iy = y - 1; iy <= y + 4; ++iy) {
                                if (ix != x || iy != y || iz != z) {
                                    BlockPos posNew = new BlockPos(ix, iy, iz);

                                    int fireLimit = 100;

                                    if (iy > y + 1) {
                                        fireLimit += (iy - (y + 1)) * 100;
                                    }

                                    if (level.getBlockState(posNew).is(ModBlocks.BALEFIRE.get()) && level.getBlockState(posNew).getValue(AGE) > age + 1) {
                                        level.setBlock(posNew, this.defaultBlockState().setValue(AGE, age + 1), 3);
                                        continue;
                                    }

                                    if (level.isEmptyBlock(posNew)) {
                                        int spread = getNeighborEncouragement(level, posNew);
                                        if (spread > 0) {
                                            int adjusted = (spread + 40 + level.getDifficulty().getId() * 7) / (age + 30);
                                            if (adjusted > 0 && random.nextInt(fireLimit) <= adjusted) {
                                                level.setBlock(posNew, this.defaultBlockState().setValue(AGE, age + 1), 3);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void tryCatchFire(Level level, BlockPos pos, int chance, RandomSource rand, int age, Direction face) {
        int flammability = level.getBlockState(pos).getFlammability(level, pos, face);

        if (rand.nextInt(chance) < flammability) {
            level.setBlock(pos, this.defaultBlockState().setValue(AGE, age + 1), 3);
            level.getBlockState(pos).onCaughtFire(level, pos, face, null);
        }
    }

    private boolean canNeighborBurn(LevelReader level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            if (level.getBlockState(pos.relative(dir)).isFlammable(level, pos.relative(dir), dir.getOpposite())) {
                return true;
            }
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

    /**
     * How many world ticks before ticking
     */
    public int tickRate(LevelReader level)
    {
        return 30;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.setRemainingFireTicks(200);
        if (entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(ModEffect.RADIATION, 100, 9));
        }
    }
}