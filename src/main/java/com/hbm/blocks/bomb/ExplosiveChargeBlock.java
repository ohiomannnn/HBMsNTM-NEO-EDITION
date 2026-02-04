package com.hbm.blocks.bomb;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.MainConfig;
import com.hbm.entity.item.TNTPrimedBase;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.explosion.vanillant.standard.EntityProcessorCrossSmooth;
import com.hbm.interfaces.IBomb;
import com.hbm.particle.helper.ExplosionCreator;
import com.hbm.particle.helper.NukeTorexCreator;
import com.hbm.util.DamageResistanceHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ExplosiveChargeBlock extends DetonatableBlock implements IBomb, IDetConnectible {

    public ExplosiveChargeBlock(Properties properties) {
        super(properties, 0, 0, 0, false, false);
    }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            if (this == ModBlocks.DET_MINER.get()) {
                ExplosionVNT vnt = new ExplosionVNT(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 4F)
                        .setBlockAllocator(new BlockAllocatorStandard())
                        .setBlockProcessor(new BlockProcessorStandard().setAllDrop());
                vnt.explode();

                if (level instanceof ServerLevel serverLevel) {
                    ExplosionLarge.spawnParticles(serverLevel, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 30);
                    level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);
                }
            }
            if (this == ModBlocks.DET_CHARGE.get()) {
                ExplosionVNT vnt = new ExplosionVNT(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 15F)
                        .setBlockAllocator(new BlockAllocatorStandard(64))
                        .setBlockProcessor(new BlockProcessorStandard())
                        .setEntityProcessor(new EntityProcessorCrossSmooth(2.0, 999).setDamageClass(DamageResistanceHandler.DamageClass.LASER));
                ExplosionCreator.composeEffectStandard(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                vnt.explode();
            }
            if (this == ModBlocks.DET_NUKE.get()) {
                NukeExplosionMK5.statFac(level, MainConfig.COMMON.MISSLE_RADIUS.get(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                NukeTorexCreator.statFacStandard(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, MainConfig.COMMON.MISSLE_RADIUS.get());
            }
        }
        return BombReturnCode.DETONATED;
    }

    @Override
    public void explodeEntity(Level level, double x, double y, double z, TNTPrimedBase entity) {
        explode(level, BlockPos.containing(x, y, z));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.hasNeighborSignal(pos)) {
            this.explode(level, pos);
        }
    }
}
