package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.NukeLittleBoyBlockEntity;
import com.hbm.config.NtmConfig;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.particle.helper.NukeTorexCreator;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NukeLittleBoyBlock extends NukeBaseBlock {

    public NukeLittleBoyBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<NukeLittleBoyBlock> CODEC = simpleCodec(NukeLittleBoyBlock::new);
    @Override protected MapCodec<NukeLittleBoyBlock> codec() { return CODEC; }

    @Override
    protected void explode(Level level, double x, double y, double z) {
        NukeExplosionMK5.statFac(level, NtmConfig.COMMON.BOY_RADIUS.get(), x, y, z);
        NukeTorexCreator.statFacStandard(level, x, y, z, NtmConfig.COMMON.BOY_RADIUS.get());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NukeLittleBoyBlockEntity(pos, state);
    }
}
