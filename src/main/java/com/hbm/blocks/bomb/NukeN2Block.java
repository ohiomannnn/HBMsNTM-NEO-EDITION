package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.NukeN2BlockEntity;
import com.hbm.config.MainConfig;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.particle.helper.NukeTorexCreator;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NukeN2Block extends NukeBaseBlock {

    public NukeN2Block(Properties properties) {
        super(properties);
    }

    public static final MapCodec<NukeN2Block> CODEC = simpleCodec(NukeN2Block::new);
    @Override protected MapCodec<NukeN2Block> codec() { return CODEC; }

    @Override
    protected void explode(Level level, double x, double y, double z) {
        NukeExplosionMK5.statFac(level, MainConfig.COMMON.N2_RADIUS.get(), x, y, z).setNoRad();
        NukeTorexCreator.statFacStandard(level, x, y, z, MainConfig.COMMON.N2_RADIUS.get());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NukeN2BlockEntity(pos, state);
    }
}
