package com.hbm.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

public interface IRadarCommandReceiver {

    boolean sendCommandPosition(BlockPos pos);
    boolean sendCommandEntity(Entity target);
}