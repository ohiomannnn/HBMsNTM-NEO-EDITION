package com.hbm.blocks.fluids;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.generic.OreBasaltBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.common.Tags;

public class VolcanicLiquidBlock extends LiquidBlock {

    public VolcanicLiquidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        for(Direction dir : Direction.values()) {
            BlockPos targetPos = pos.relative(dir);
            BlockState resultState = getReaction(level, targetPos);
            if(resultState != null) level.setBlock(targetPos, resultState, 3);
        }
    }

    public BlockState getReaction(Level level, BlockPos pos) {
        BlockState b = level.getBlockState(pos);

        if(b.getFluidState().is(FluidTags.WATER)) return Blocks.STONE.defaultBlockState();
        if(b.is(BlockTags.LOGS)) return NtmBlocks.WASTE_LOG.get().defaultBlockState();
        if(b.is(BlockTags.PLANKS)) return NtmBlocks.WASTE_PLANKS.get().defaultBlockState();
        if(b.is(BlockTags.LEAVES)) return Blocks.FIRE.defaultBlockState();
        if(b.is(Tags.Blocks.ORES_DIAMOND)) return NtmBlocks.ORE_BASALT.get().defaultBlockState().setValue(OreBasaltBlock.SUBTYPE, 3);
        return null;
    }

    public static void baseTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if(state.getBlock() instanceof VolcanicLiquidBlock block) {
            int lavaCount = 0;
            int basaltCount = 0;

            for(Direction dir : Direction.values()) {
                BlockState b = level.getBlockState(pos.relative(dir));

                if(b.is(block)) lavaCount++;
                if(b.is(block.getBasaltForCheck())) basaltCount++;
            }

            boolean isSource = state.getValue(LEVEL) == 0;
            boolean hasNoLavaBelow = !level.getBlockState(pos.below()).is(block);

            if(((!isSource && lavaCount < 2) || (random.nextInt(5) == 0 && lavaCount < 5)) && hasNoLavaBelow) {
                block.onSolidify(level, pos, lavaCount, basaltCount, random);
            }
        }
    }

    public Block getBasaltForCheck() {
        return NtmBlocks.BASALT.get();
    }

    public void onSolidify(ServerLevel level, BlockPos pos, int lavaCount, int basaltCount, RandomSource random) {
        int r = random.nextInt(200);

        BlockState above = level.getBlockState(pos.above(10));
        boolean canMakeGem = (lavaCount + basaltCount == 6) && (lavaCount < 3) && (above.is(NtmBlocks.BASALT.get()) || above.is(this));

        BlockState resultState;

        if(r < 2) resultState = NtmBlocks.ORE_BASALT.get().defaultBlockState();
        else if(r == 2) resultState = NtmBlocks.ORE_BASALT.get().defaultBlockState().setValue(OreBasaltBlock.SUBTYPE, 1);
        else if(r == 3) resultState = NtmBlocks.ORE_BASALT.get().defaultBlockState().setValue(OreBasaltBlock.SUBTYPE, 2);
        else if(r == 4) resultState = NtmBlocks.ORE_BASALT.get().defaultBlockState().setValue(OreBasaltBlock.SUBTYPE, 4);
        else if(r < 15 && canMakeGem) resultState = NtmBlocks.ORE_BASALT.get().defaultBlockState().setValue(OreBasaltBlock.SUBTYPE, 3);
        else resultState = NtmBlocks.BASALT.get().defaultBlockState();

        level.setBlock(pos, resultState, 3);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(!level.isClientSide) {
            entity.igniteForSeconds(15.0F);
            if(entity.hurt(entity.damageSources().lava(), 4.0F)) {
                entity.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + entity.random.nextFloat() * 0.4F);
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockPos blockpos = pos.above();
        if(level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
            if(random.nextInt(100) == 0) {
                double d0 = pos.getX() + random.nextDouble();
                double d1 = pos.getY() + 1.0;
                double d2 = pos.getZ() + random.nextDouble();
                level.addParticle(ParticleTypes.LAVA, d0, d1, d2, 0.0, 0.0, 0.0);
                level.playLocalSound(d0, d1, d2, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }

            if(random.nextInt(200) == 0) {
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }
        }
    }
}
