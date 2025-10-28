package com.hbm.blocks.bomb;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.MainConfig;
import com.hbm.entity.item.EntityTNTPrimedBase;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.entity.effect.NukeTorex;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.*;
import com.hbm.interfaces.IBomb;
import com.hbm.particle.helper.ExplosionCreator;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ExplosiveChargeBlock extends DetonatableBlock implements IBomb {

    public ExplosiveChargeBlock(Properties properties) {
        super(properties, 0, 0, 0, false, false);
    }

    @Override
    public BombReturnCode explode(Level level, int x, int y, int z) {
        if (!level.isClientSide) {
            level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);
            if (this == ModBlocks.DET_CHARGE.get()) {
                ExplosionVNT vnt = new ExplosionVNT(level, x, y, z, 15F);
                vnt.setBlockAllocator(new BlockAllocatorStandard(64));
                vnt.setBlockProcessor(new BlockProcessorStandard());
                vnt.setEntityProcessor(new EntityProcessorStandard());
                vnt.setPlayerProcessor(new PlayerProcessorStandard());
                vnt.explode();
                ExplosionCreator.composeEffectStandard(level, x + 0.5, y + 1, z + 0.5);
            }
            if (this == ModBlocks.DET_NUKE.get()) {
                level.addFreshEntity(NukeExplosionMK5.statFac(level, MainConfig.COMMON.MISSLE_RADIUS.get(), x + 0.5, y + 0.5, z + 0.5));
                NukeTorex.statFacStandard(level, x + 0.5, y + 0.5, z + 0.5, MainConfig.COMMON.MISSLE_RADIUS.get());
            }
        }
        return BombReturnCode.DETONATED;
    }

    @Override
    public void explodeEntity(Level level, double x, double y, double z, EntityTNTPrimedBase entity) {
        explode(level, Mth.floor(x), Mth.floor(y), Mth.floor(z));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.hasNeighborSignal(pos)) {
            this.explode(level, pos.getX(), pos.getY(), pos.getZ());
        }
    }
}
