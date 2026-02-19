package com.hbm.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RayTraceResult {
    /** Used to determine what sub-segment is hit */
    public int subHit;
    /** Used to add extra hit info */
    public Object hitInfo;
    private BlockPos blockPos;
    public Type typeOfHit;
    public Direction sideHit;
    public Vec3 hitVec;
    public Entity entityHit;

    public RayTraceResult(Vec3 hitVec, Direction direction, BlockPos pos) {
        this(RayTraceResult.Type.BLOCK, hitVec, direction, pos);
    }

    public RayTraceResult(Vec3 hitVec, Direction direction) {
        this(RayTraceResult.Type.BLOCK, hitVec, direction, BlockPos.ZERO);
    }

    public RayTraceResult(Entity entity) {
        this(entity, new Vec3(entity.position.x, entity.position.y, entity.position.z));
    }

    public RayTraceResult(Type type, Vec3 hitVec, Direction direction, BlockPos pos) {
        this.subHit = -1;
        this.hitInfo = null;
        this.typeOfHit = type;
        this.blockPos = pos;
        this.sideHit = direction;
        this.hitVec = new Vec3(hitVec.x, hitVec.y, hitVec.z);
    }

    public RayTraceResult(Entity entity, Vec3 hitVec) {
        this.subHit = -1;
        this.hitInfo = null;
        this.typeOfHit = RayTraceResult.Type.ENTITY;
        this.entityHit = entity;
        this.hitVec = hitVec;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public String toString() {
        return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos=" + this.hitVec + ", entity=" + this.entityHit + '}';
    }

    public enum Type {
        MISS,
        BLOCK,
        ENTITY
    }
}
