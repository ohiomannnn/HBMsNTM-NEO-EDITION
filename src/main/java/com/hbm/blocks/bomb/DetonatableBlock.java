package com.hbm.blocks.bomb;

import api.hbm.block.IFuckingExplode;
import com.hbm.blocks.generic.BlockFlammable;
import com.hbm.entity.item.EntityTNTPrimedBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiConsumer;

public abstract class DetonatableBlock extends BlockFlammable implements IFuckingExplode {

    protected int popFuse; // A shorter fuse for when this explosive is dinked by another
    protected boolean detonateOnCollision;
    protected boolean detonateOnShot;

    public DetonatableBlock(Properties properties, int en, int flam, int popFuse, boolean detonateOnCollision, boolean detonateOnShot) {
        super(properties, en, flam);
        this.popFuse = popFuse;
        this.detonateOnCollision = detonateOnCollision;
        this.detonateOnShot = detonateOnShot;
    }


    @Override
    protected void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
        if (!level.isClientSide) {
            EntityTNTPrimedBase tnt = new EntityTNTPrimedBase(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, explosion.getIndirectSourceEntity(), this);
            tnt.fuse = popFuse <= 0 ? 0 : level.random.nextInt(popFuse) + popFuse / 2;
            tnt.detonateOnCollision = detonateOnCollision;
            level.addFreshEntity(tnt);
        }
    }

    @Override
    public boolean canDropFromExplosion(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return false;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide && shouldIgnite(level, pos)) {
            level.removeBlock(pos, false);
            wasExploded(level, pos, null);
        }
    }

    public void onShot(Level level, BlockPos pos) {
        if (!detonateOnShot) return;

        level.removeBlock(pos, false);
        explodeEntity(level, pos.getX(), pos.getY(), pos.getZ(), null);
    }
}
