package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.NukeIvyMikeBlockEntity;
import com.hbm.config.MainConfig;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.particle.helper.NukeTorexCreator;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NukeIvyMikeBlock extends NukeBaseBlock {

    public NukeIvyMikeBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<NukeIvyMikeBlock> CODEC = simpleCodec(NukeIvyMikeBlock::new);
    @Override protected MapCodec<NukeIvyMikeBlock> codec() { return CODEC; }

    @Override
    protected void explode(Level level, double x, double y, double z) {
        NukeExplosionMK5.statFac(level, MainConfig.COMMON.MIKE_RADIUS.get(), x, y, z);
        NukeTorexCreator.statFacStandard(level, x, y, z, MainConfig.COMMON.MIKE_RADIUS.get());
    }

    @Override
    protected void explodeNotFull(Level level, double x, double y, double z) {
        NukeExplosionMK5.statFac(level, MainConfig.COMMON.MAN_RADIUS.get(), x, y, z);
        NukeTorexCreator.statFacStandard(level, x, y, z, MainConfig.COMMON.MAN_RADIUS.get());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NukeIvyMikeBlockEntity(pos, state);
    }
}
