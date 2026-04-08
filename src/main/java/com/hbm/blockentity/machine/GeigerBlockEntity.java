package com.hbm.blockentity.machine;

import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blockentity.Tickable;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.registry.NtmSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class GeigerBlockEntity extends BlockEntity implements Tickable {

    public GeigerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.GEIGER_COUNTER.get(), pos, blockState);
    }

    public int timer = 0;
    public float radiation = 0;

    @Override
    public void updateEntity() {
        if (this.level == null) return;
        if (this.level.isClientSide) return;

        RandomSource random = level.random;
        this.timer++;

        if (this.timer == 10) {
            this.timer = 0;
            this.radiation = ChunkRadiationManager.proxy.getRadiation(level, this.getBlockPos());

            // To update the adjacent comparators
            level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
        }

        if (level.getGameTime() % 5 == 0) {
            BlockPos pos = this.getBlockPos();
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;
            if (this.radiation > 0) {
                List<Integer> list = new ArrayList<>();

                if (this.radiation < 1) list.add(0);
                if (this.radiation < 5) list.add(0);
                if (this.radiation < 10) list.add(1);
                if (this.radiation > 5 && this.radiation < 15) list.add(2);
                if (this.radiation > 10 && this.radiation < 20) list.add(3);
                if (this.radiation > 15 && this.radiation < 25) list.add(4);
                if (this.radiation > 20 && this.radiation < 30) list.add(5);
                if (this.radiation > 25) list.add(6);

                int r = list.get(random.nextInt(list.size()));

                if (r > 0) {
                    switch (r) {
                        case 1 -> level.playSound(null, x, y, z, NtmSoundEvents.GEIGER1, SoundSource.BLOCKS, 1.0F, 1.0F);
                        case 2 -> level.playSound(null, x, y, z, NtmSoundEvents.GEIGER2, SoundSource.BLOCKS, 1.0F, 1.0F);
                        case 3 -> level.playSound(null, x, y, z, NtmSoundEvents.GEIGER3, SoundSource.BLOCKS, 1.0F, 1.0F);
                        case 4 -> level.playSound(null, x, y, z, NtmSoundEvents.GEIGER4, SoundSource.BLOCKS, 1.0F, 1.0F);
                        case 5 -> level.playSound(null, x, y, z, NtmSoundEvents.GEIGER5, SoundSource.BLOCKS, 1.0F, 1.0F);
                        case 6 -> level.playSound(null, x, y, z, NtmSoundEvents.GEIGER6, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                }
            } else if (random.nextInt(50) == 0) {
                int i = 1 + random.nextInt(2);
                switch (i) {
                    case 1 -> level.playSound(null, x, y, z, NtmSoundEvents.GEIGER1, SoundSource.BLOCKS, 1.0F, 1.0F);
                    case 2 -> level.playSound(null, x, y, z, NtmSoundEvents.GEIGER2, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }
}
