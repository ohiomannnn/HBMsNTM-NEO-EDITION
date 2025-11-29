package com.hbm.blocks.bomb;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.MainConfig;
import com.hbm.entity.effect.NukeTorex;
import com.hbm.entity.item.EntityTNTPrimedBase;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.*;
import com.hbm.interfaces.IBomb;
import com.hbm.particle.helper.ExplosionCreator;
import com.hbm.util.DamageResistanceHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ExplosiveChargeBlock extends DetonatableBlock implements IBomb {

    public ExplosiveChargeBlock(Properties properties) {
        super(properties, 0, 0, 0, false, false);
    }

    @Override
    public BombReturnCode explode(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            if (this == ModBlocks.DET_CHARGE.get()) {
                ExplosionVNT vnt = new ExplosionVNT(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 15F)
                        .setBlockAllocator(new BlockAllocatorStandard(64))
                        .setBlockProcessor(new BlockProcessorNoDamage())
                        .setEntityProcessor(new EntityProcessorCrossSmooth(2.0, 1000).setDamageClass(DamageResistanceHandler.DamageClass.IN_FIRE))
                        .setPlayerProcessor(new PlayerProcessorStandard());
                vnt.explode();
                //ExplosionCreator.composeEffectStandard(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            }
            if (this == ModBlocks.DET_NUKE.get()) {
                NukeExplosionMK5.statFac(level, MainConfig.COMMON.MISSLE_RADIUS.get(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                NukeTorex.statFacStandard(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, MainConfig.COMMON.MISSLE_RADIUS.get());
            }
        }
        return BombReturnCode.DETONATED;
    }

    @Override
    public void explodeEntity(Level level, double x, double y, double z, EntityTNTPrimedBase entity) {
        explode(level, BlockPos.containing(x, y, z));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.hasNeighborSignal(pos)) {
            this.explode(level, pos);
        }
    }
}
