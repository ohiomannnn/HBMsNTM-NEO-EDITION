package com.hbm.blockentity.machine;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blocks.machine.GeigerCounterBlock;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.lib.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class GeigerBlockEntity extends BlockEntity {

    public GeigerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.GEIGER_COUNTER.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GeigerBlockEntity blockEntity) {
        if (!(state.getBlock() instanceof GeigerCounterBlock geiger)) return;
        if (level.isClientSide) return;

        geiger.timer++;

        if (geiger.timer == 10) {
            geiger.timer = 0;
            geiger.ticker = ChunkRadiationManager.proxy.getRadiation(level, pos);

            // To update the adjacent comparators
            level.updateNeighborsAt(pos, state.getBlock());
        }

        if (level.getGameTime() % 5 == 0) {
            if (geiger.ticker > 1E-5) {
                List<Integer> list = new ArrayList<>();

                if (geiger.ticker < 1) list.add(0);
                if (geiger.ticker < 5) list.add(0);
                if (geiger.ticker < 10) list.add(1);
                if (geiger.ticker > 5 && geiger.ticker < 15) list.add(2);
                if (geiger.ticker > 10 && geiger.ticker < 20) list.add(3);
                if (geiger.ticker > 15 && geiger.ticker < 25) list.add(4);
                if (geiger.ticker > 20 && geiger.ticker < 30) list.add(5);
                if (geiger.ticker > 25) list.add(6);

                int r = list.get(geiger.rand.nextInt(list.size()));

                if (r > 0) {
                    switch (r) {
                        case 1 -> playSnd(level, pos, ModSounds.GEIGER1.get());
                        case 2 -> playSnd(level, pos, ModSounds.GEIGER2.get());
                        case 3 -> playSnd(level, pos, ModSounds.GEIGER3.get());
                        case 4 -> playSnd(level, pos, ModSounds.GEIGER4.get());
                        case 5 -> playSnd(level, pos, ModSounds.GEIGER5.get());
                        case 6 -> playSnd(level, pos, ModSounds.GEIGER6.get());
                    }
                }
            } else if (geiger.rand.nextInt(50) == 0) {
                playSoundRand(level, pos, 1 + geiger.rand.nextInt(1));
            }
        }
    }

    // enough coding for today...
    protected static void playSnd(Level level, BlockPos pos, SoundEvent soundEvent) {
        level.playSound(null, pos, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
    protected static void playSoundRand(Level level, BlockPos pos, int i) {
        switch (i) {
            case 1 -> level.playSound(null, pos, ModSounds.GEIGER1.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            case 2 -> level.playSound(null, pos, ModSounds.GEIGER2.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
}
