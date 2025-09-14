package com.hbm.blocks.generic;

import com.hbm.HBMsNTM;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.hazard.HazardRegistry;
import com.hbm.hazard.HazardSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class HazardBlock extends Block {

    protected float rad = 0.0F;

    public HazardBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.rad > 0) {
            ChunkRadiationManager.proxy.incrementRad(level, pos.getX(), pos.getY(), pos.getZ(), rad);
            level.scheduleTick(pos, this, 20);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        rad = HazardSystem.getHazardLevelFromStack(new ItemStack(this), HazardRegistry.RADIATION) * 0.1F;
        if (this.rad > 0 && level instanceof ServerLevel server) {
            server.scheduleTick(pos, this, 20);
        }
    }

}
