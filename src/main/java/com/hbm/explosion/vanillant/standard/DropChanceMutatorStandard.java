package com.hbm.explosion.vanillant.standard;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.interfaces.IDropChanceMutator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public class DropChanceMutatorStandard implements IDropChanceMutator {

    private final float chance;

    public DropChanceMutatorStandard(float chance) {
        this.chance = chance;
    }

    @Override
    public float mutateDropChance(ExplosionVNT explosion, Block block, BlockPos pos, float chance) {
        return this.chance;
    }
}
