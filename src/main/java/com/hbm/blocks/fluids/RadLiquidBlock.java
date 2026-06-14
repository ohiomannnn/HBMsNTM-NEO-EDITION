package com.hbm.blocks.fluids;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.generic.SellafieldSlakedBlock;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.common.Tags;

public class RadLiquidBlock extends VolcanicLiquidBlock {

    public RadLiquidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(entity instanceof LivingEntity living) ContaminationUtil.contaminate(living, HazardType.RADIATION, ContaminationType.CREATIVE, 5F);
    }

    @Override
    public BlockState getReaction(Level level, BlockPos pos) {
        BlockState b = level.getBlockState(pos);

        if(b.getFluidState().is(FluidTags.WATER)) return Blocks.STONE.defaultBlockState();
        if(b.is(BlockTags.LOGS)) return NtmBlocks.WASTE_LOG.get().defaultBlockState();
        if(b.is(BlockTags.PLANKS)) return NtmBlocks.WASTE_PLANKS.get().defaultBlockState();
        if(b.is(BlockTags.LEAVES)) return Blocks.FIRE.defaultBlockState();
        if(b.is(Tags.Blocks.ORES_DIAMOND)) return NtmBlocks.ORE_SELLAFIELD_DIAMOND.get().defaultBlockState();
        return null;
    }

    public Block getBasaltForCheck() {
        return NtmBlocks.SELLAFIELD_SLAKED.get();
    }

    public void onSolidify(ServerLevel level, BlockPos pos, int lavaCount, int basaltCount, RandomSource random) {
        int r = random.nextInt(200);

        BlockState above = level.getBlockState(pos.above(10));
        boolean canMakeGem = (lavaCount + basaltCount == 6) && (lavaCount < 3) && (above.is(NtmBlocks.SELLAFIELD_SLAKED.get()) || above.is(this));
        int lvl = 5 + random.nextInt(3);
        BlockState resultState;

        if(r < 2) resultState = NtmBlocks.ORE_SELLAFIELD_DIAMOND.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, lvl);
        else if(r == 2) resultState = NtmBlocks.ORE_SELLAFIELD_EMERALD.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, lvl);
        else if(r < 15 && canMakeGem) resultState = NtmBlocks.ORE_SELLAFIELD_EMERALD.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, lvl);
        else resultState = NtmBlocks.SELLAFIELD_SLAKED.get().defaultBlockState().setValue(SellafieldSlakedBlock.COLOR_LEVEL, lvl);

        level.setBlock(pos, resultState, 3);
    }
}
