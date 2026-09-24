package com.hbm.blocks.bomb;

import api.hbm.block.IFuckingExplode;
import com.hbm.blocks.generic.FlammableBlock;
import com.hbm.entity.item.PrimedTntExploded;
import com.hbm.entity.projectile.BulletBaseMK4;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public abstract class DetonatableBlock extends FlammableBlock implements IFuckingExplode {

    protected int popFuse; // A shorter fuse for when this explosive is dinked by another
    protected boolean detonateOnCollision;
    protected boolean detonateOnShot;

    public DetonatableBlock(Properties properties, int encouragement, int flammability, int popFuse, boolean detonateOnCollision, boolean detonateOnShot) {
        super(properties, encouragement, flammability);
        this.popFuse = popFuse;
        this.detonateOnCollision = detonateOnCollision;
        this.detonateOnShot = detonateOnShot;
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, level, pos, explosion);

        this.prime(level, state, pos, explosion.getIndirectSourceEntity());
    }

    @Override
    public boolean canDropFromExplosion(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return false;
    }

    public void prime(Level level, BlockState state, BlockPos pos, @Nullable LivingEntity igniter) {
        if(!level.isClientSide) {
            PrimedTntExploded tnt = new PrimedTntExploded(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, igniter, state);
            tnt.setFuse(popFuse <= 0 ? 0 : level.random.nextInt(popFuse) + popFuse / 2);
            tnt.detonateOnCollision = detonateOnCollision;
            level.addFreshEntity(tnt);
        }
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if(!level.isClientSide && shouldIgnite(level, pos)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            this.prime(level, state, pos, null);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if(!level.isClientSide && shouldIgnite(level, pos)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            this.prime(level, state, pos, null);
        }
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockPos pos = hit.getBlockPos();

        if(projectile instanceof BulletBaseMK4) {
            if(!detonateOnShot) return;

            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            this.explodeEntity(level, pos.getX(), pos.getY(), pos.getZ(), null); // insta-explod
            return;
        }

        if(projectile.isOnFire() && projectile.mayInteract(level, pos)) {
            Entity owner = projectile.getOwner();
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            this.onCaughtFire(state, level, pos, null, owner instanceof LivingEntity living ? living : null);
        }
    }
}
