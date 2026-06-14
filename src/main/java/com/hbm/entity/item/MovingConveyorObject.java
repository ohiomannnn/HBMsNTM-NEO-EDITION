package com.hbm.entity.item;

import api.hbm.conveyor.IConveyorBelt;
import api.hbm.conveyor.IEnterableBlock;
import com.hbm.lib.Library;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class MovingConveyorObject extends Entity {

    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected float lerpYRot;
    protected float lerpXRot;

    public MovingConveyorObject(EntityType<? extends MovingConveyorObject> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYRot = yRot;
        this.lerpXRot = xRot;
        this.lerpSteps = 10;
    }

    @Override
    public void lerpMotion(double x, double y, double z) {
        this.setDeltaMovement(x, y, z);
    }

    @Override
    public double lerpTargetX() {
        return this.lerpSteps > 0 ? this.lerpX : this.getX();
    }

    @Override
    public double lerpTargetY() {
        return this.lerpSteps > 0 ? this.lerpY : this.getY();
    }

    @Override
    public double lerpTargetZ() {
        return this.lerpSteps > 0 ? this.lerpZ : this.getZ();
    }

    @Override
    public float lerpTargetXRot() {
        return this.lerpSteps > 0 ? this.lerpXRot : this.xRot;
    }

    @Override
    public float lerpTargetYRot() {
        return this.lerpSteps > 0 ? this.lerpYRot : this.yRot;
    }

    @Override
    public void tick() {
        if(this.level.isClientSide) {
            if(this.lerpSteps > 0) {
                this.lerpPositionAndRotationStep(this.lerpSteps, this.lerpX, this.lerpY, this.lerpZ, this.lerpYRot, this.lerpXRot);
                this.lerpSteps--;
            } else {
                this.reapplyPosition();
            }
        } else {
            tickCount++;

            if(this.tickCount <= 5) {
                return;
            }

            BlockPos pos = this.blockPosition();
            Block b = this.level.getBlockState(pos).getBlock();
            boolean isOnConveyor = b instanceof IConveyorBelt icb && icb.canItemStay(this.level, pos, this.position());

            if(!isOnConveyor) {

                if(onLeaveConveyor()) {
                    return;
                }
            } else {

                Vec3 target = ((IConveyorBelt) b).getTravelLocation(this.level, pos, this.position(), this.getMoveSpeed());
                this.setDeltaMovement(
                        new Vec3(
                                target.x - this.position().x,
                                target.y - this.position().y,
                                target.z - this.position().z
                        )
                );

                BlockPos lastPos = this.blockPosition();
                this.move(MoverType.SELF, this.getDeltaMovement());
                BlockPos newPos = this.blockPosition();

                if(!lastPos.equals(newPos)) {
                    BlockState newState = level.getBlockState(newPos);
                    Block newBlock = newState.getBlock();

                    if(newBlock instanceof IEnterableBlock inb) {
                        Direction dir = null;

                        if(lastPos.getX() > newPos.getX() && lastPos.getY() == newPos.getY() && lastPos.getZ() == newPos.getZ()) dir = Library.POS_X;
                        else if(lastPos.getX() < newPos.getX() && lastPos.getY() == newPos.getY() && lastPos.getZ() == newPos.getZ()) dir = Library.NEG_X;
                        else if(lastPos.getX() == newPos.getX() && lastPos.getY() > newPos.getY() && lastPos.getZ() == newPos.getZ()) dir = Library.POS_Y;
                        else if(lastPos.getX() == newPos.getX() && lastPos.getY() < newPos.getY() && lastPos.getZ() == newPos.getZ()) dir = Library.NEG_Y;
                        else if(lastPos.getX() == newPos.getX() && lastPos.getY() == newPos.getY() && lastPos.getZ() > newPos.getZ()) dir = Library.POS_Z;
                        else if(lastPos.getX() == newPos.getX() && lastPos.getY() == newPos.getY() && lastPos.getZ() < newPos.getZ()) dir = Library.NEG_Z;

                        if(dir != null) {
                            this.enterBlock(inb, newPos, dir);
                        }
                    } else {
                        if(!newState.isSolidRender(level, newPos)) {
                            newState = level.getBlockState(newPos.below());
                            newBlock = newState.getBlock();

                            if(newBlock instanceof IEnterableBlock inb) {
                                this.enterBlockFalling(inb, newPos);
                            }
                        }
                    }
                }
            }
        }
    }

    public abstract void enterBlock(IEnterableBlock enterable, BlockPos pos, Direction dir);

    public void enterBlockFalling(IEnterableBlock enterable, BlockPos pos) {
        this.enterBlock(enterable, pos.offset(0, -1, 0), Direction.UP);
    }

    /**
     * @return true if the update loop should end
     */
    public abstract boolean onLeaveConveyor();

    public double getMoveSpeed() {
        return 0.0625D;
    }
}
