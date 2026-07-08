package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.NukeSoliniumBlockEntity;
import com.hbm.config.NtmConfig;
import com.hbm.entity.logic.NukeExplosionMK3;
import com.hbm.particle.helper.CloudCreator;
import com.hbm.particle.helper.CloudCreator.CloudType;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NukeSoliniumBlock extends NukeBaseBlock {

    public NukeSoliniumBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<NukeSoliniumBlock> CODEC = simpleCodec(NukeSoliniumBlock::new);
    @Override protected MapCodec<NukeSoliniumBlock> codec() { return CODEC; }

    @Override
    protected void explode(Level level, double x, double y, double z) {
        NukeExplosionMK3 explosion = NukeExplosionMK3.statFacFleija(level, x, y, z, NtmConfig.COMMON.SOLINIUM_RADIUS.get()).makeSol();
        if(!explosion.isRemoved()) {
            level.addFreshEntity(explosion);
            CloudCreator.composeEffect(level, x, y, z, CloudType.SOLINIUM);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NukeSoliniumBlockEntity(pos, state);
    }
}
