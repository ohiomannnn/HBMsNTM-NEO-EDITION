package com.hbm.blocks.generic;

import com.hbm.blocks.NtmBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class OutgasBlock extends Block {

    private final boolean randomTick;
    private final boolean onBreak;
    private boolean onNeighbour;

    public OutgasBlock(boolean randomTick, boolean onBreak, Properties properties) {
        super(randomTick ? properties.randomTicks() : properties);

        this.randomTick = randomTick;
        this.onBreak = onBreak;
        this.onNeighbour = false;
    }

    public OutgasBlock(boolean randomTick, boolean onBreak, boolean onNeighbour, Properties properties) {
        this(randomTick, onBreak, properties);
        this.onNeighbour = onNeighbour;
    }

    public BlockState getGas() {

        if(this == NtmBlocks.BRICK_ASBESTOS.get()) {
            return NtmBlocks.GAS_ASBESTOS.get().defaultBlockState();
        }

        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {

        if(this.randomTick && this.getGas().is(NtmBlocks.GAS_ASBESTOS.get())) {
            BlockPos posAbove = pos.above();
            if(level.getBlockState(posAbove).isAir()) {
                if(level.random.nextInt(10) == 0) level.setBlock(posAbove, NtmBlocks.GAS_ASBESTOS.get().defaultBlockState(), 3);

                for(int i = 0; i < 5; i++) {
                    level.addParticle(ParticleTypes.MYCELIUM, pos.getX() + level.random.nextFloat(), pos.getY() + 1.1F, pos.getZ() + level.random.nextFloat(), 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Direction direction = Direction.values()[random.nextInt(6)];

        BlockPos relativePos = pos.relative(direction);
        if(level.getBlockState(relativePos).isAir()) {
            level.setBlock(relativePos, this.getGas(), 3);
        }
    }

    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean dropExperience) {
        if(onBreak) level.setBlock(pos, this.getGas(), 3);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block nBlock, BlockPos nPos, boolean movedByPiston) {

        if(onNeighbour && level.random.nextInt(3) == 0) {
            for(Direction direction : Direction.values()) {
                BlockPos relativePos = pos.relative(direction);
                if(level.getBlockState(relativePos).isAir()) {
                    level.setBlock(relativePos, this.getGas(), 3);
                }
            }
        }
    }
}
