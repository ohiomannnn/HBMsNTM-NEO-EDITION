package com.hbm.entity.logic;

import api.hbm.energymk2.IEnergyHandlerMK2;
import com.hbm.network.toclient.ParticleBurst;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class EMP extends Entity {

    private List<BlockPos> machines;
    private static final int life = 10 * 60 * 20;

    public EMP(EntityType<? extends EMP> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            if (machines == null) {
                this.allocate();
            } else {
                this.shock();
            }

            if (this.tickCount > life) {
                this.discard();
            }
        }
    }

    private void allocate() {
        machines = new ArrayList<>();
        int radius = 100;

        for (int x = -radius; x <= radius; x++) {
            int x2 = (int) Math.pow(x, 2);
            for (int y = -radius; y <= radius; y++) {
                int y2 = (int) Math.pow(y, 2);
                for (int z = -radius; z <= radius; z++) {
                    int z2 = (int) Math.pow(z, 2);
                    if (Math.sqrt(x2 + y2 + z2) <= radius) {
                        this.add(BlockPos.containing(this.position.x + x, this.position.y + y, this.position.z + z));
                    }
                }
            }
        }
    }

    private void shock() {
        for (BlockPos pos : machines) {
            emp(pos);
        }
    }

    private void add(BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof IEnergyHandlerMK2) {
            machines.add(pos);
        }
    }

    private void emp(BlockPos pos) {

        BlockEntity be = level.getBlockEntity(pos);

        boolean flag = false;

        if (be instanceof IEnergyHandlerMK2 handler) {

            handler.setPower(0);
            flag = true;
        }

        if (flag && random.nextInt(20) == 0) {
            if (this.level instanceof ServerLevel serverLevel) {
                PacketDistributor.sendToPlayersNear(serverLevel, null, this.position.x, this.position.y, this.position.z, 50, new ParticleBurst(pos, Blocks.GRAY_STAINED_GLASS));
            }
        }
    }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) { }

    @Override protected void readAdditionalSaveData(CompoundTag tag) { }
    @Override protected void addAdditionalSaveData(CompoundTag tag) { }
}
