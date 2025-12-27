package com.hbm.util.fauxpointtwelve;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

public class DirPos extends BlockPos {

    protected Direction dir;

    public DirPos(int x, int y, int z, Direction dir) {
        super(x, y, z);
        this.dir = dir;
    }

    @Override
    public DirPos rotate(Rotation rotationIn) {
        switch(rotationIn) {
            case NONE:
            default: return this;
            case CLOCKWISE_90: return new DirPos(-this.getZ(), this.getY(), this.getX(), this.getDir());
            case CLOCKWISE_180: return new DirPos(-this.getX(), this.getY(), -this.getZ(), this.getDir().getOpposite());
            case COUNTERCLOCKWISE_90: return new DirPos(this.getZ(), this.getY(), -this.getX(), this.getDir());
        }
    }

    public Direction getDir() {
        return this.dir;
    }
}
