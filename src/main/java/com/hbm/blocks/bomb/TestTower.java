package com.hbm.blocks.bomb;

import com.hbm.packets.toclient.AuxParticlePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public class TestTower extends Block {
    public TestTower(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 2);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("type", "tower");
        nbt.putFloat("lift", 1F);
        nbt.putFloat("base", 0.5F);
        nbt.putFloat("max", 4F);
        nbt.putInt("life", 250 + random.nextInt(250));

        PacketDistributor.sendToPlayersNear(
                level,
                null,
                pos.getX(), pos.getY(), pos.getZ(),
                250,
                new AuxParticlePacket(nbt, pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5)
        );

        level.scheduleTick(pos, this, 2);
    }
}
