package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.NukeBalefireBlockEntity;
import com.hbm.entity.ModEntityTypes;
import com.hbm.entity.logic.NukeExplosionBalefire;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NukeBalefireBlock extends NukeBaseBlock {

    public NukeBalefireBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<NukeBalefireBlock> CODEC = simpleCodec(NukeBalefireBlock::new);
    @Override protected MapCodec<NukeBalefireBlock> codec() { return CODEC; }

    @Override
    protected void explode(Level level, double x, double y, double z) {
        NukeExplosionBalefire bf = new NukeExplosionBalefire(ModEntityTypes.NUKE_BALEFIRE.get(), level);
        bf.setPos(x, y, z);
        bf.destructionRange = 250;
        level.addFreshEntity(bf);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NukeBalefireBlockEntity(pos, state);
    }
}
