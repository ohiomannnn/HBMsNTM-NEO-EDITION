package com.hbm.blocks;

import com.hbm.blocks.states.NtmBlockStateProperties;
import com.hbm.inventory.MetaHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public abstract class MultiBlock extends Block implements IMultiBlock {

    protected static final IntegerProperty META = NtmBlockStateProperties.META;

    public MultiBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(META, 0)
        );
    }

    @Override public int getMeta(BlockState state) { return state.getValue(META); }
    @Override public int getSubCount() { return 15; }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(META, MetaHelper.getMeta(context.getItemInHand()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(META);
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
