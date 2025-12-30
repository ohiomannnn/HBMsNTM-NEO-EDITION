package com.hbm.blockentity.machine;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blocks.machine.GeigerCounterBlock;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.lib.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeigerBlockEntity extends BlockEntity {

    public GeigerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.GEIGER_COUNTER.get(), pos, blockState);
    }

    public int timer = 0;
    public float ticker = 0;

    public static void serverTick(Level level, BlockPos pos, BlockState state, GeigerBlockEntity be) {
        RandomSource rand = level.random;
        be.timer++;

        if (be.timer == 10) {
            be.timer = 0;
            be.ticker = ChunkRadiationManager.proxy.getRadiation(level, pos);

            // To update the adjacent comparators
            level.updateNeighborsAt(pos, state.getBlock());
        }

        if (level.getGameTime() % 5 == 0) {
            if (be.ticker > 1E-5) {
                List<Integer> list = new ArrayList<>();

                if (be.ticker < 1) list.add(0);
                if (be.ticker < 5) list.add(0);
                if (be.ticker < 10) list.add(1);
                if (be.ticker > 5 && be.ticker < 15) list.add(2);
                if (be.ticker > 10 && be.ticker < 20) list.add(3);
                if (be.ticker > 15 && be.ticker < 25) list.add(4);
                if (be.ticker > 20 && be.ticker < 30) list.add(5);
                if (be.ticker > 25) list.add(6);

                int r = list.get(rand.nextInt(list.size()));

                if (r > 0) {
                    switch (r) {
                        case 1 -> level.playSound(null, pos, ModSounds.GEIGER1.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
                        case 2 -> level.playSound(null, pos, ModSounds.GEIGER2.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
                        case 3 -> level.playSound(null, pos, ModSounds.GEIGER3.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
                        case 4 -> level.playSound(null, pos, ModSounds.GEIGER4.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
                        case 5 -> level.playSound(null, pos, ModSounds.GEIGER5.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
                        case 6 -> level.playSound(null, pos, ModSounds.GEIGER6.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
                    }
                }
            } else if (rand.nextInt(50) == 0) {
                int i = 1 + rand.nextInt(1);
                switch (i) {
                    case 1 -> level.playSound(null, pos, ModSounds.GEIGER1.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
                    case 2 -> level.playSound(null, pos, ModSounds.GEIGER2.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
                }
            }
        }
    }
}
