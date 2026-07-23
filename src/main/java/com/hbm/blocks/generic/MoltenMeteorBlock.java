package com.hbm.blocks.generic;

import com.hbm.blocks.NtmBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MoltenMeteorBlock extends HazardBlock {

    public MoltenMeteorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(3) == 0) {
            level.setBlock(pos, NtmBlocks.BLOCK_METEOR_COBBLE.get().defaultBlockState(), 3);
            level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 0.9F + random.nextFloat() * 0.2F);
        }
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable net.minecraft.world.level.block.entity.BlockEntity be, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, be, tool);
        if (!level.isClientSide) {
            level.setBlock(pos, net.minecraft.world.level.block.Blocks.LAVA.defaultBlockState(), 3);
        }
    }
}
