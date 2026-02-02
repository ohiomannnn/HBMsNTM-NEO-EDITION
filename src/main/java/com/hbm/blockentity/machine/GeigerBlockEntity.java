package com.hbm.blockentity.machine;

import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.lib.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class GeigerBlockEntity extends BlockEntity {

    public GeigerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public int timer = 0;
    public float radiation = 0;

    public static void serverTick(Level level, BlockPos pos, BlockState state, GeigerBlockEntity be) {
        RandomSource random = level.random;
        be.timer++;

        if (be.timer == 10) {
            be.timer = 0;
            be.radiation = ChunkRadiationManager.proxy.getRadiation(level, pos);

            // To update the adjacent comparators
            level.updateNeighborsAt(pos, state.getBlock());
        }

        if (level.getGameTime() % 5 == 0) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;
            if (be.radiation > 0) {
                List<Integer> list = new ArrayList<>();

                if (be.radiation < 1) list.add(0);
                if (be.radiation < 5) list.add(0);
                if (be.radiation < 10) list.add(1);
                if (be.radiation > 5 && be.radiation < 15) list.add(2);
                if (be.radiation > 10 && be.radiation < 20) list.add(3);
                if (be.radiation > 15 && be.radiation < 25) list.add(4);
                if (be.radiation > 20 && be.radiation < 30) list.add(5);
                if (be.radiation > 25) list.add(6);

                int r = list.get(random.nextInt(list.size()));

                if (r > 0) {
                    switch (r) {
                        case 1 -> level.playSound(null, x, y, z, ModSounds.GEIGER1, SoundSource.BLOCKS, 1.0F, 1.0F);
                        case 2 -> level.playSound(null, x, y, z, ModSounds.GEIGER2, SoundSource.BLOCKS, 1.0F, 1.0F);
                        case 3 -> level.playSound(null, x, y, z, ModSounds.GEIGER3, SoundSource.BLOCKS, 1.0F, 1.0F);
                        case 4 -> level.playSound(null, x, y, z, ModSounds.GEIGER4, SoundSource.BLOCKS, 1.0F, 1.0F);
                        case 5 -> level.playSound(null, x, y, z, ModSounds.GEIGER5, SoundSource.BLOCKS, 1.0F, 1.0F);
                        case 6 -> level.playSound(null, x, y, z, ModSounds.GEIGER6, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                }
            } else if (random.nextInt(50) == 0) {
                int i = 1 + random.nextInt(2);
                switch (i) {
                    case 1 -> level.playSound(null, x, y, z, ModSounds.GEIGER1, SoundSource.BLOCKS, 1.0F, 1.0F);
                    case 2 -> level.playSound(null, x, y, z, ModSounds.GEIGER2, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }
}
