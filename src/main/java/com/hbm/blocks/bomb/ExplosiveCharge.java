package com.hbm.blocks.bomb;

import com.hbm.blocks.ModBlocks;
import com.hbm.entity.logic.EntityNukeExplosionMK5;
import com.hbm.entity.logic.EntityNukeTorex;
import com.hbm.interfaces.IBomb;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ExplosiveCharge extends DetonatableBlock implements IBomb {
    public ExplosiveCharge(Properties properties) {
        super(properties, 0, 0, 0, false, false);
    }

    @Override
    public BombReturnCode explode(Level level, int x, int y, int z) {
        if (this == ModBlocks.DET_NUKE.get()) {
            level.addFreshEntity(EntityNukeExplosionMK5.statFac(level, 100, x + 0.5, y + 0.5, z + 0.5));
            EntityNukeTorex.statFacStandard(level, x + 0.5, y + 0.5, z + 0.5, 100);
        }
        return BombReturnCode.DETONATED;
    }

    @Override
    public void explodeEntity(Level level, double x, double y, double z, Entity entity) {
        explode(level, (int) x, (int) y, (int) z);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.hasNeighborSignal(pos)) {
            this.explode(level, pos.getX(), pos.getY(), pos.getZ());
        }
    }
}
