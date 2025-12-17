package com.hbm.blocks.bomb;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blockentity.bomb.LandMineBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.config.MainConfig;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.explosion.ExplosionNukeGeneric;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.*;
import com.hbm.interfaces.IBomb;
import com.hbm.items.special.PolaroidItem;
import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.AuxParticle;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
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
import net.neoforged.neoforge.network.PacketDistributor;

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
        if (this == ModBlocks.MINE_AP.get()) return SHAPE_AP;
        if (this == ModBlocks.MINE_HE.get()) return SHAPE_HE;
        if (this == ModBlocks.MINE_SHRAP.get()) return SHAPE_SHRAP;
        if (this == ModBlocks.MINE_FAT.get()) return SHAPE_FAT;
        return Shapes.block();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
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
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (this == ModBlocks.MINE_NAVAL.get()) return true;
        BlockPos below = pos.below();
        if (level.getBlockState(below).is(BlockTags.LEAVES)) return false;
        if (level.getBlockState(below).getBlock() instanceof LandmineBlock) return false;
        if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) return false;
        return true;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {

        if (!safeMode) {
            this.explode(level, pos);
        } else {
            Block.dropResources(state, level, pos);
        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos) {

        if (!level.isClientSide) {
            LandmineBlock.safeMode = true;
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            LandmineBlock.safeMode = false;

            if (this == ModBlocks.MINE_AP.get()) {
                ExplosionVNT vnt = new ExplosionVNT(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3F)
                        .setEntityProcessor(new EntityProcessorCrossSmooth(0.5, (float) MainConfig.SERVER.MINE_AP_DAMAGE.getAsDouble()).setupPiercing(5F, 0.2F))
                        .setSFX(new ExplosionEffectWeapon(5, 1F, 0.5F));
                vnt.explode();
            }

            if (this == ModBlocks.MINE_HE.get()) {
                ExplosionVNT vnt = new ExplosionVNT(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 4F)
                        .setBlockAllocator(new BlockAllocatorStandard())
                        .setBlockProcessor(new BlockProcessorStandard())
                        .setEntityProcessor(new EntityProcessorCrossSmooth(1, (float) MainConfig.SERVER.MINE_HE_DAMAGE.getAsDouble()).setupPiercing(15F, 0.2F))
                        .setSFX(new ExplosionEffectWeapon(15, 3.5F, 1.25F));
                vnt.explode();
            }

            if (this == ModBlocks.MINE_SHRAP.get()) {
                ExplosionVNT vnt = new ExplosionVNT(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3F)
                        .setEntityProcessor(new EntityProcessorCrossSmooth(0.5, (float) MainConfig.SERVER.MINE_SHRAP_DAMAGE.getAsDouble()))
                        .setSFX(new ExplosionEffectWeapon(5, 1F, 0.5F));
                vnt.explode();

                if (level instanceof ServerLevel serverLevel) {
                    ExplosionLarge.spawnShrapnelShower(serverLevel, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 1D, 0, 45, 0.2D);
                    ExplosionLarge.spawnShrapnels(serverLevel, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5);
                }
            }

            if (this == ModBlocks.MINE_FAT.get()) {
                ExplosionVNT vnt = new ExplosionVNT(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10)
                        .setBlockAllocator(new BlockAllocatorStandard(64))
                        .setBlockProcessor(new BlockProcessorStandard())
                        .setEntityProcessor(new EntityProcessorCrossSmooth(2, (float) MainConfig.SERVER.MINE_NUKE_DAMAGE.getAsDouble()).withRangeMod(1.5F));
                vnt.explode();

                ExplosionNukeGeneric.incrementRad(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1.5F);

                // this has to be the single worst solution ever
                level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ModSounds.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 25.0F, 0.9F);
                CompoundTag tag = new CompoundTag();
                tag.putString("type", "muke");
                tag.putBoolean("balefire", PolaroidItem.polaroidID == 11 || level.random.nextInt(100) == 0);
                if (level instanceof ServerLevel serverLevel) {
                    PacketDistributor.sendToPlayersNear(serverLevel, null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 250, new AuxParticle(tag, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
                }
            }

            if (this == ModBlocks.MINE_NAVAL.get()) {
                ExplosionVNT vnt = new ExplosionVNT(level, pos.getX() + 5, pos.getY() + 5, pos.getZ() + 5, 25F)
                        .setBlockAllocator(new BlockAllocatorWater(32))
                        .setBlockProcessor(new BlockProcessorStandard())
                        .setEntityProcessor(new EntityProcessorCrossSmooth(0.5, (float) MainConfig.SERVER.MINE_NAVAL_DAMAGE.getAsDouble()).setupPiercing(5F, 0.2F))
                        .setSFX(new ExplosionEffectWeapon(10, 1F, 0.5F));
                vnt.explode();

                if (level instanceof ServerLevel serverLevel) {
                    ExplosionLarge.spawnParticlesRadial(serverLevel, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 30);
                    ExplosionLarge.spawnRubble(serverLevel, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5);

                    // Only spawn water effects if there's water above the mine
                    if (isWaterAbove(level, pos)) {
                        ExplosionLarge.spawnFoam(serverLevel, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 60);
                    }
                }
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
