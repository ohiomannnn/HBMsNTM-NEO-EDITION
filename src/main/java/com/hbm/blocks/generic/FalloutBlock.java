package com.hbm.blocks.generic;

import com.hbm.blocks.ModBlocks;
import com.hbm.extprop.LivingProperties;
import com.hbm.lib.ModEffect;
import com.hbm.world.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
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
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean moved) {
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, false);
        }
    }
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        return false;
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return false;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity livingEntity) {
            if (livingEntity instanceof Player player && player.isCreative()) return;
            if (livingEntity instanceof Player player && player.isSpectator()) return;

            livingEntity.addEffect(new MobEffectInstance(ModEffect.RADIATION, 5 * 20, 0));
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            LivingProperties.addCont(player, new LivingProperties.ContaminationEffect(1F, 200, false));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos.below());
        return !blockstate.is(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON);
    }

    public static boolean canPlaceBlockAt(Level level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);

        if (isPlant(belowState)) return false;
        if (belowState.is(ModBlocks.FALLOUT.get())) return false;
        if (belowState.isAir()) return false;

        return belowState.isSolidRender(level, below);
    }

    public static boolean isPlant(BlockState state) {
        return state.is(BlockTags.CROPS) || state.is(BlockTags.FLOWERS) || state.is(BlockTags.LEAVES) || state.is(BlockTags.SAPLINGS);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }
}
