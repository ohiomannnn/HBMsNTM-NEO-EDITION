package com.hbm.util.fauxpointtwelve;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Adjusted code from MC 1.12 (com.minecraft.util.math.BlockPos)
 */
public class BlockPosNT {

    private int x;
    private int y;
    private int z;

    public BlockPosNT(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPosNT(BlockEntity te) {
        this(te.getBlockPos());
    }

    public BlockPosNT(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public BlockPosNT(double x, double y, double z) {
        this(Mth.floor(x), Mth.floor(y), Mth.floor(z));
    }

    /** Basically a setter for the coords. Violates the "muh unmutability" horseshit I don't care about and
     * lets me re-use the same instance for a ton of checks. RAM has priority over stupid religious bullshit. */
    public BlockPosNT mutate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public boolean compare(int x, int y, int z) {
        return this.x == x && this.y == y && this.z == z;
    }

    public BlockPosNT add(int x, int y, int z) {
        return x == 0 && y == 0 && z == 0 ? this : new BlockPosNT(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    public BlockPosNT add(double x, double y, double z) {
        return x == 0.0D && y == 0.0D && z == 0.0D ? this : new BlockPosNT((double) this.getX() + x, (double) this.getY() + y, (double) this.getZ() + z);
    }

    public BlockPosNT add(BlockPosNT pos) {
        return this.add(pos.getX(), pos.getY(), pos.getZ());
    }

    public BlockPosNT add(BlockPos pos) {
        return this.add(pos.getX(), pos.getY(), pos.getZ());
    }

    public BlockPosNT rotate(Rotation rotationIn) {
        return switch (rotationIn) {
            case CLOCKWISE_90 -> new BlockPosNT(-this.getZ(), this.getY(), this.getX());
            case CLOCKWISE_180 -> new BlockPosNT(-this.getX(), this.getY(), -this.getZ());
            case COUNTERCLOCKWISE_90 -> new BlockPosNT(this.getZ(), this.getY(), -this.getX());
            default -> this;
        };
    }

    public BlockPosNT offset(Direction dir) {
        return this.offset(dir, 1);
    }

    public BlockPosNT offset(Direction dir, int distance) {
        return new BlockPosNT(x + dir.getStepX() * distance, y + dir.getStepY() * distance, z + dir.getStepZ() * distance);
    }

    public BlockPos makeCompat() {
        return new BlockPos(this.getX(), this.getY(), this.getZ());
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    /** modified 1.12 vanilla implementation */
    @Override
    public int hashCode() {
        return getIdentity(this.getX(), this.getY(), this.getZ());
    }

    public static int getIdentity(int x, int y, int z) {
        return (y + z * 27644437) * 27644437 + x;
    }

    @Override
    public boolean equals(Object toCompare) {
        if(this == toCompare) {
            return true;
        } else if(!(toCompare instanceof BlockPosNT)) {
            return false;
        } else {
            BlockPosNT pos = (BlockPosNT) toCompare;
            if(this.getX() != pos.getX()) {
                return false;
            } else if(this.getY() != pos.getY()) {
                return false;
            } else {
                return this.getZ() == pos.getZ();
            }
        }
    }
}
