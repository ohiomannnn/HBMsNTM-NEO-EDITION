package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.CrashedBombBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.config.MainConfig;
import com.hbm.entity.ModEntities;
import com.hbm.entity.logic.NukeExplosionBalefire;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.explosion.vanillant.standard.EntityProcessorCross;
import com.hbm.explosion.vanillant.standard.PlayerProcessorStandard;
import com.hbm.interfaces.IBomb;
import com.hbm.items.special.PolaroidItem;
import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.particle.helper.ExplosionCreator;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public class CrashedBombBlock extends BaseEntityBlock implements IBomb {

    public CrashedBombBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<CrashedBombBlock> CODEC = simpleCodec(CrashedBombBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (this == ModBlocks.CRASHED_BOMB_BALEFIRE.get()) return CrashedBombBlockEntity.balefire(blockPos, blockState);
        if (this == ModBlocks.CRASHED_BOMB_CONVENTIONAL.get()) return CrashedBombBlockEntity.conventional(blockPos, blockState);
        if (this == ModBlocks.CRASHED_BOMB_NUKE.get()) return CrashedBombBlockEntity.nuke(blockPos, blockState);
        if (this == ModBlocks.CRASHED_BOMB_SALTED.get()) return CrashedBombBlockEntity.salted(blockPos, blockState);
        return null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof CrashedBombBlockEntity bomb) { CrashedBombBlockEntity.serverTick(lvl, pos, st, bomb); }
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

            if (this == ModBlocks.CRASHED_BOMB_BALEFIRE.get()) {
                NukeExplosionBalefire balefire = new NukeExplosionBalefire(ModEntities.NUKE_BALEFIRE.get(), level);
                balefire.setPos(pos.getX(), pos.getY(), pos.getZ());
                balefire.destructionRange = (int) (MainConfig.COMMON.FATMAN_RADIUS.get() * 1.25);
                level.addFreshEntity(balefire);
                spawnMush(level, pos, true);
            }
            if (this == ModBlocks.CRASHED_BOMB_CONVENTIONAL.get()) {
                ExplosionVNT vnt = new ExplosionVNT(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 35F)
                        .setBlockAllocator(new BlockAllocatorStandard(24))
                        .setBlockProcessor(new BlockProcessorStandard().setNoDrop())
                        .setEntityProcessor(new EntityProcessorCross(5D).withRangeMod(1.5F))
                        .setPlayerProcessor(new PlayerProcessorStandard());
                ExplosionCreator.composeEffectLarge(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                vnt.explode();
            }
            if (this == ModBlocks.CRASHED_BOMB_NUKE.get()) {
                NukeExplosionMK5.statFac(level, 35, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                spawnMush(level, pos, PolaroidItem.polaroidID == 11 || level.random.nextInt(100) == 0);
            }
            if (this == ModBlocks.CRASHED_BOMB_SALTED.get()) {
                NukeExplosionMK5.statFac(level, 25, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5).moreFallout(25);
                spawnMush(level, pos, PolaroidItem.polaroidID == 11 || level.random.nextInt(100) == 0);
            }
        }
        return BombReturnCode.DETONATED;
    }

    public static void spawnMush(Level level, BlockPos pos, boolean balefire) {
        level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ModSounds.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 15.0F, 1.0F);
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "muke");
        tag.putBoolean("balefire", balefire);
        PacketDistributor.sendToPlayersNear((ServerLevel) level, null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 250, new AuxParticle(tag, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }
}
