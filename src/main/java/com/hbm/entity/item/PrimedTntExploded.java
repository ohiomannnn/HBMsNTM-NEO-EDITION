package com.hbm.entity.item;

import api.hbm.block.IFuckingExplode;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PrimedTntExploded extends PrimedTnt {

    public boolean detonateOnCollision;

    public PrimedTntExploded(Level level, double x, double y, double z, @Nullable LivingEntity owner, BlockState state) {
        super(level, x, y, z, owner);

        this.setBlockState(state);
    }

    @Override
    public void tick() {

        this.handlePortal();
        this.applyGravity();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        if(this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));
        }

        int fuse = this.getFuse() - 1;
        this.setFuse(fuse);
        if(fuse <= 0 || (this.detonateOnCollision && (this.horizontalCollision || this.verticalCollision))) {
            this.discard();
            if(!this.level.isClientSide) {
                this.explode();
            }
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            if(this.level.isClientSide) {
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void explode() {
        if(this.getBlockState().getBlock() instanceof IFuckingExplode explode) {
            explode.explodeEntity(this.level, this.getX(), this.getY(), this.getZ(), this);
        } else {
            super.explode();
        }
    }


}
