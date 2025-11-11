package com.hbm.blocks.bomb;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blockentity.bomb.LandMineBlockEntity;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.*;
import com.hbm.interfaces.IBomb;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LandmineBlock extends BaseEntityBlock implements IBomb {

    private static final VoxelShape SHAPE_AP = Block.box(5, 0, 5, 11, 1, 11);
    private static final VoxelShape SHAPE_HE = Block.box(4, 0, 4, 12, 2, 12);
    private static final VoxelShape SHAPE_SHRAP = Block.box(5, 0, 5, 11, 1, 11);
    private static final VoxelShape SHAPE_FAT = Block.box(5, 0, 4, 11, 6, 12);

    public static boolean safeMode = false;

    public double range;
    public double height;

    public static final MapCodec<LandmineBlock> CODEC = simpleCodec(LandmineBlock::new);

    protected LandmineBlock(Properties properties) {
        super(properties);
    }

    public LandmineBlock(Properties properties, double range, double height) {
        this(properties);
        this.range = range;
        this.height = height;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LandMineBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : BaseEntityBlock.createTickerHelper(type, ModBlockEntities.LANDMINE.get(), LandMineBlockEntity::serverTick);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        BlockPos below = pos.below();
        if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
            Block.dropResources(level.getBlockState(pos), level, pos);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide) {
            if (level.hasNeighborSignal(pos)) {
                this.explode(level, pos);
            }
            BlockPos below = pos.below();
            if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
                if (!safeMode) {
                    this.explode(level, pos);
                } else {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {

        if (!safeMode) {
            this.explode(level, pos);
        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos) {

        if (!level.isClientSide) {
            LandmineBlock.safeMode = true;
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            LandmineBlock.safeMode = false;

//            ExplosionVNT vnt = new ExplosionVNT(level, x + 0.5, y + 0.5, z + 0.5, 10);
//            vnt.setBlockAllocator(new BlockAllocatorStandard(64));
//            vnt.setBlockProcessor(new BlockProcessorStandard());
//            vnt.setEntityProcessor(new EntityProcessorCrossSmooth(2, 100F).withRangeMod(1.5F));
//            vnt.setPlayerProcessor(new PlayerProcessorStandard());
//            vnt.setSFX(new ExplosionEffectWeapon(10, 5F, 0.5F));
//            vnt.explode();

            //ExplosionNukeGeneric.incrementRad(level, x, y, z, 1.5F);

            //spawnMush(level, x, y, z, CommonEvents.polaroidID == 11 || level.random.nextInt(100) == 0);

//            ExplosionVNT vnt = new ExplosionVNT(level, x + 0.5, y + 0.5, z + 0.5, 3F);
//            vnt.setEntityProcessor(new EntityProcessorCrossSmooth(0.5, 7.5F));
//            vnt.setPlayerProcessor(new PlayerProcessorStandard());
//            vnt.setSFX(new ExplosionEffectWeapon(5, 1F, 0.5F));
//            vnt.explode();
//
//
//            ExplosionLarge.spawnShrapnelShower(level, x + 0.5, y + 0.5, z + 0.5, 0, 1D, 0, 45, 0.2D);
//            ExplosionLarge.spawnShrapnels(level, x + 0.5, y + 0.5, z + 0.5, 5);

            ExplosionVNT vnt = new ExplosionVNT(level, pos.getX() + 5, pos.getY() + 5, pos.getZ() + 5, 25F);
            vnt.setBlockAllocator(new BlockAllocatorWater(32));
            vnt.setBlockProcessor(new BlockProcessorStandard());
            vnt.setEntityProcessor(new EntityProcessorCrossSmooth(0.5, 75F).setupPiercing(5F, 0.2F));
            vnt.setPlayerProcessor(new PlayerProcessorStandard());
            vnt.setSFX(new ExplosionEffectWeapon(10, 1F, 0.5F));
            vnt.explode();

            ExplosionLarge.spawnParticlesRadial((ServerLevel) level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 30);
            ExplosionLarge.spawnRubble(level ,pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5);

            // Only spawn water effects if there's water above the mine
            if (isWaterAbove(level, pos)) {
                ExplosionLarge.spawnFoam((ServerLevel) level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 60);
            }
        }

        return BombReturnCode.DETONATED;
    }

    public boolean isWaterAbove(Level level, BlockPos pos) {
        for (int xo = -1; xo <= 1; xo++) {
            for (int zo = -1; zo <= 1; zo++) {
                if (level.getFluidState(new BlockPos(pos.getX() + xo, pos.getY() + 1, pos.getZ() + zo)).is(FluidTags.WATER)) {
                    return true;
                }
            }
        }
        return false;
    }
}
