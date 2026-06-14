package com.hbm.blocks.network;

import api.hbm.conveyor.IConveyorBelt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;

public class ConveyorBaseBlock extends Block implements IConveyorBelt {

    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public ConveyorBaseBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canItemStay(Level level, BlockPos pos, Vec3 itemPos) {
        return true;
    }

    @Override
    public Vec3 getTravelLocation(Level level, BlockPos pos, Vec3 itemPos, double speed) {

        Direction dir = this.getTravelDirection(level, pos, itemPos);
        //snapping point
        Vec3 snap = this.getClosestSnappingPosition(level, pos, itemPos);
        //snapping point + speed
        Vec3 dest = new Vec3(snap.x - dir.getStepX() * speed, snap.y - dir.getStepY() * speed, snap.z - dir.getStepZ() * speed);
        //delta to get to that point
        Vec3 motion = new Vec3((dest.x - itemPos.x), (dest.y - itemPos.y), (dest.z - itemPos.z));
        double len = motion.length();
        //the effective destination towards "dest" after taking speed into consideration
        return new Vec3(itemPos.x + motion.x / len * speed, itemPos.y + motion.y / len * speed, itemPos.z + motion.z / len * speed);
    }

    public Direction getInputDirection(Level level, BlockPos pos) {
        return level.getBlockState(pos).getValue(HORIZONTAL_FACING);
    }

    public Direction getOutputDirection(Level level, BlockPos pos) {
        return level.getBlockState(pos).getValue(HORIZONTAL_FACING).getOpposite();
    }

    public Direction getTravelDirection(Level level, BlockPos pos, Vec3 itemPos) {
        return level.getBlockState(pos).getValue(HORIZONTAL_FACING);
    }

    @Override
    public Vec3 getClosestSnappingPosition(Level level, BlockPos pos, Vec3 itemPos) {

        Direction dir = this.getTravelDirection(level, pos, itemPos);

        double posX = Mth.clamp(itemPos.x, pos.getX(), pos.getX() + 1);
        double posZ = Mth.clamp(itemPos.z, pos.getZ(), pos.getZ() + 1);

        double x = pos.getX() + 0.5;
        double z = pos.getZ() + 0.5;
        double y = pos.getY() + 0.25;

        if(dir.getAxis() == Direction.Axis.X) {
            x = posX;
        } else if(dir.getAxis() == Direction.Axis.Z) {
            z = posZ;
        }

        return new Vec3(x, y, z);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if(!level.isClientSide) {
            if(entity instanceof ItemEntity && entity.tickCount > 10 && entity.isAlive()) {

            }
        }
    }
}
