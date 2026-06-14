package com.hbm.blocks.generic;

import com.hbm.blocks.NtmBlocks;
import com.hbm.extprop.HbmLivingAttachments;
import com.hbm.lib.ModEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FalloutBlock extends Block {

    public FalloutBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean moved) {
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, false);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(!level.isClientSide && entity instanceof LivingEntity livingEntity) {
            if(livingEntity instanceof Player player && player.isCreative()) return;
            if(livingEntity instanceof Player player && player.isSpectator()) return;

            livingEntity.addEffect(new MobEffectInstance(ModEffect.RADIATION, 10 * 60 * 20, 0));
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide) {
            HbmLivingAttachments.addCont(player, new HbmLivingAttachments.ContaminationEffect(1F, 200, false));
        }

        return InteractionResult.CONSUME;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canPlaceBlockAt(level, pos);
    }

    public static boolean canPlaceBlockAt(LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);

        if(isPlant(belowState)) return false;
        if(!level.getFluidState(pos).isEmpty()) return false;
        if(belowState.is(NtmBlocks.FALLOUT.get())) return false;
        if(belowState.isAir()) return false;

        return belowState.isSolidRender(level, below);
    }

    public static boolean isPlant(BlockState state) {
        return state.is(BlockTags.CROPS) || state.is(BlockTags.FLOWERS) || state.is(BlockTags.LEAVES) || state.is(BlockTags.SAPLINGS);
    }
}
