package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.NukeFleijaBlockEntity;
import com.hbm.config.MainConfig;
import com.hbm.entity.logic.NukeExplosionMK3;
import com.hbm.particle.helper.CloudCreator;
import com.hbm.particle.helper.CloudCreator.CloudType;
import com.hbm.world.WorldUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NukeFleijaBlock extends NukeBaseBlock {

    public NukeFleijaBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<NukeFleijaBlock> CODEC = simpleCodec(NukeFleijaBlock::new);
    @Override protected MapCodec<NukeFleijaBlock> codec() { return CODEC; }

    @Override
    protected void explode(Level level, double x, double y, double z) {
        NukeExplosionMK3 ex = NukeExplosionMK3.statFacFleija(level, x, y, z, MainConfig.COMMON.FLEIJA_RADIUS.get());
        if (!ex.isRemoved()) {
            WorldUtil.loadAndSpawnEntityInWorld(ex);
            CloudCreator.composeEffect(level, x, y, z, CloudType.FLEIJA);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NukeFleijaBlockEntity(pos, state);
    }
}
