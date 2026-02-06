package com.hbm.util.fauxpointtwelve;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DirPos extends BlockPosNT {

    protected Direction dir;

    public DirPos(int x, int y, int z, Direction dir) {
        super(x, y, z);
        this.dir = dir;
    }

    public DirPos(BlockEntity be, Direction dir) {
        super(be);
        this.dir = dir;
    }

    public DirPos(double x, double y, double z, Direction dir) {
        super(x, y, z);
        this.dir = dir;
    }

    @Override
    public DirPos rotate(Rotation rotationIn) {
        return switch (rotationIn) {
            case CLOCKWISE_90 -> new DirPos(-this.getZ(), this.getY(), this.getX(), rotationIn.rotate(this.getDir()));
            case CLOCKWISE_180 -> new DirPos(-this.getX(), this.getY(), -this.getZ(), this.getDir().getOpposite());
            case COUNTERCLOCKWISE_90 -> new DirPos(this.getZ(), this.getY(), -this.getX(), rotationIn.rotate(this.getDir()));
            default -> this;
        };
    }


    public Direction getDir() {
        return this.dir;
    }
}
