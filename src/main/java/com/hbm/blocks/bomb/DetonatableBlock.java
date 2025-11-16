package com.hbm.blocks.bomb;

import api.hbm.block.IFuckingExplode;
import com.hbm.blocks.generic.FlammableBlock;
import com.hbm.entity.item.EntityTNTPrimedBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiConsumer;

public abstract class DetonatableBlock extends FlammableBlock implements IFuckingExplode {

    protected int popFuse; // A shorter fuse for when this explosive is dinked by another
    protected boolean detonateOnCollision;
    protected boolean detonateOnShot;

    public DetonatableBlock(Properties properties,
                            int encouragement,
                            int flammability,
                            int popFuse,
                            boolean detonateOnCollision,
                            boolean detonateOnShot) {
        super(properties, encouragement, flammability);
        this.popFuse = popFuse;
        this.detonateOnCollision = detonateOnCollision;
        this.detonateOnShot = detonateOnShot;
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            EntityTNTPrimedBase tntPrimed = new EntityTNTPrimedBase(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, explosion.getIndirectSourceEntity(), this);
            tntPrimed.fuse = popFuse <= 0 ? 0 : level.random.nextInt(popFuse) + popFuse / 2;
            tntPrimed.detonateOnCollision = detonateOnCollision;
            level.addFreshEntity(tntPrimed);
        }
    }

    @Override
    public boolean canDropFromExplosion(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return false;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide && shouldIgnite(level, pos)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            wasExploded(level, pos, null);
        }
    }

    public void onShot(Level level, BlockPos pos) {
        if (!detonateOnShot) return;

        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        explodeEntity(level, pos.getX(), pos.getY(), pos.getZ(), null); // insta-explod
    }
}
