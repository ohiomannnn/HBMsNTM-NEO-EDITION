package com.hbm.blocks;

import com.hbm.inventory.MetaHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public abstract class MultiBlock extends Block implements IMultiBlock {

    public MultiBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
        MetaHelper.setMeta(stack, rectify(this.getMeta(state)));
        return stack;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, Builder params) {
        List<ItemStack> droppedStacks = super.getDrops(state, params);
        droppedStacks.forEach(stack -> MetaHelper.setMeta(stack, rectify(this.getMeta(state))));
        return droppedStacks;
    }
}
