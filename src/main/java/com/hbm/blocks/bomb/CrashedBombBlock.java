package com.hbm.blocks.bomb;

import com.hbm.CommonEvents;
import com.hbm.blockentity.bomb.CrashedBombBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.config.MainConfig;
import com.hbm.entity.ModEntities;
import com.hbm.entity.logic.NukeExplosionBalefire;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.*;
import com.hbm.interfaces.IBomb;
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
    public BombReturnCode explode(Level level, int x, int y, int z) {
        if (!level.isClientSide) {
            level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);

            if (this == ModBlocks.CRASHED_BOMB_BALEFIRE.get()) {
                NukeExplosionBalefire balefire = new NukeExplosionBalefire(ModEntities.NUKE_BALEFIRE.get(), level);
                balefire.setPos(x, y, z);
                balefire.destructionRange = (int) (MainConfig.COMMON.FATMAN_RADIUS.get() * 1.25);
                level.addFreshEntity(balefire);
                spawnMush(level, x, y, z, true);
            }
            if (this == ModBlocks.CRASHED_BOMB_CONVENTIONAL.get()) {
                ExplosionVNT vnt = new ExplosionVNT(level, x + 0.5, y + 0.5, z + 0.5, 35F);
                vnt.setBlockAllocator(new BlockAllocatorStandard(24));
                vnt.setBlockProcessor(new BlockProcessorStandard().setNoDrop());
                vnt.setEntityProcessor(new EntityProcessorCross(5D).withRangeMod(1.5F));
                vnt.setPlayerProcessor(new PlayerProcessorStandard());
                vnt.explode();
                ExplosionCreator.composeEffectLarge(level, x + 0.5, y + 0.5, z + 0.5);
            }
            if (this == ModBlocks.CRASHED_BOMB_NUKE.get()) {
                level.addFreshEntity(NukeExplosionMK5.statFac(level, 35, x + 0.5, y + 0.5, z + 0.5));
                spawnMush(level, x, y, z, CommonEvents.polaroidID == 11 || level.random.nextInt(100) == 0);
            }
            if (this == ModBlocks.CRASHED_BOMB_SALTED.get()) {
                level.addFreshEntity(NukeExplosionMK5.statFac(level, 25, x + 0.5, y + 0.5, z + 0.5).moreFallout(25));
                spawnMush(level, x, y, z, CommonEvents.polaroidID == 11 || level.random.nextInt(100) == 0);
            }
        }
        return BombReturnCode.DETONATED;
    }

    public static void spawnMush(Level level, int x, int y, int z, boolean balefire) {
        level.playSound(null,x + 0.5, y + 0.5, z + 0.5, ModSounds.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 15.0F, 1.0F);
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "muke");
        tag.putBoolean("balefire", balefire);
        PacketDistributor.sendToPlayersNear(
                (ServerLevel) level,
                null,
                x + 0.5, y + 0.5, z + 0.5,
                250,
                new AuxParticle(tag, x + 0.5, y + 0.5, z + 0.5)
        );
    }
}
