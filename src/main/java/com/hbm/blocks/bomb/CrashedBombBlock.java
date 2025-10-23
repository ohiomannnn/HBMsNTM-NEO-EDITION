package com.hbm.blocks.bomb;

import com.hbm.CommonEvents;
import com.hbm.blockentity.bomb.CrashedBombBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.config.ModConfigs;
import com.hbm.entity.ModEntities;
import com.hbm.entity.logic.NukeExplosionBalefire;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.explosion.vanillant.standard.EntityProcessorCross;
import com.hbm.explosion.vanillant.standard.PlayerProcessorStandard;
import com.hbm.interfaces.IBomb;
import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.particle.helper.ExplosionCreator;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public class CrashedBombBlock extends Block implements IBomb, EntityBlock {

    public CrashedBombBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public IBomb.BombReturnCode explode(Level level, int x, int y, int z) {
        if (!level.isClientSide) {
            level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);

            if (this == ModBlocks.CRASHED_BOMB_BALEFIRE.get()) {
                NukeExplosionBalefire bf = new NukeExplosionBalefire(ModEntities.NUKE_BALEFIRE.get(), level);
                bf.setPos(x, y, z);
                bf.destructionRange = (int) (ModConfigs.COMMON.FATMAN_RADIUS.get() * 1.25);
                level.addFreshEntity(bf);
                spawnMush(level, x, y, z, true);
            }
            if (this == ModBlocks.CRASHED_BOMB_CONVENTIONAL.get()) {
                ExplosionVNT xnt = new ExplosionVNT(level, x + 0.5, y + 0.5, z + 0.5, 35F);
                xnt.setBlockAllocator(new BlockAllocatorStandard(24));
                xnt.setBlockProcessor(new BlockProcessorStandard().setNoDrop());
                xnt.setEntityProcessor(new EntityProcessorCross(5D).withRangeMod(1.5F));
                xnt.setPlayerProcessor(new PlayerProcessorStandard());
                xnt.explode();
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
        return IBomb.BombReturnCode.DETONATED;
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
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return !level.isClientSide ? (lvl, pos, st, be) -> {
            if (be instanceof CrashedBombBlockEntity bomb) {
                CrashedBombBlockEntity.tick(lvl, pos, st, bomb);
            }
        } : null;
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
