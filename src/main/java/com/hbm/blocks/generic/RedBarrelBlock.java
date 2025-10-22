package com.hbm.blocks.generic;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.bomb.DetonatableBlock;
import com.hbm.entity.item.EntityTNTPrimedBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RedBarrelBlock extends DetonatableBlock {

    // Flammable barrels also explode when shot
    public RedBarrelBlock(Properties properties, boolean flammable) {
        super(properties, flammable ? 2 : 0,  flammable ? 15 : 0, 100, true, flammable);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        float f = 0.0625F;
        return Block.box(2 * f, 0.0F, 2 * f, 14 * f, 1.0F, 14 * f);
    }

    @Override
    public void explodeEntity(Level level, double x, double y, double z, EntityTNTPrimedBase entity) {
        if (this == ModBlocks.RED_BARREL.get()) {
            level.explode(entity, x, y, z, 2.5F, true, Level.ExplosionInteraction.BLOCK);
        }
    }

}
