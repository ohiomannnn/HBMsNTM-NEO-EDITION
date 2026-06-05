package com.hbm.blocks.network;

import com.hbm.blockentity.network.PipeBaseBlockEntity;
import com.hbm.blocks.IMetaBlock;
import com.hbm.blocks.IMultiBlock;
import com.hbm.blocks.states.NtmBlockStateProperties;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.lib.Library;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;

import java.util.List;

public class FluidDuctConnectingBlock extends FluidDuctBaseBlock implements IMultiBlock {

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST  = BlockStateProperties.EAST;
    public static final BooleanProperty WEST  = BlockStateProperties.WEST;
    public static final BooleanProperty UP    = BlockStateProperties.UP;
    public static final BooleanProperty DOWN  = BlockStateProperties.DOWN;

    private static final IntegerProperty META = NtmBlockStateProperties.META;

    public FluidDuctConnectingBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, Boolean.FALSE)
                .setValue(SOUTH, Boolean.FALSE)
                .setValue(EAST,  Boolean.FALSE)
                .setValue(WEST,  Boolean.FALSE)
                .setValue(UP,    Boolean.FALSE)
                .setValue(DOWN,  Boolean.FALSE)
                .setValue(META, 0)
        );
    }

    @Override public int getMeta(BlockState state) { return state.getValue(META); }
    @Override public int getSubCount() { return 3; }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(META, MetaHelper.getMeta(context.getItemInHand()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, META);
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

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if(level.getBlockEntity(pos) instanceof PipeBaseBlockEntity pipe) {
            FluidType type = pipe.getFluidType();
            state = state.setValue(WEST,  canConnectTo(level, pos, Direction.WEST, type))
                    .setValue(EAST,     canConnectTo(level, pos, Direction.EAST, type))
                    .setValue(DOWN,     canConnectTo(level, pos, Direction.DOWN, type))
                    .setValue(UP,       canConnectTo(level, pos, Direction.UP, type))
                    .setValue(NORTH,    canConnectTo(level, pos, Direction.NORTH, type))
                    .setValue(SOUTH,    canConnectTo(level, pos, Direction.SOUTH, type));
        }
        return state;
    }

    public boolean canConnectTo(BlockGetter level, BlockPos pos, Direction dir, FluidType type) {
        return Library.canConnectFluid(level, pos.relative(dir), dir, type);
    }

    public void updateConnections(LevelAccessor level, BlockPos pos, FluidType type) {
        BlockState state = level.getBlockState(pos);
        BlockState newState = state
                .setValue(WEST,  canConnectTo(level, pos, Direction.WEST, type))
                .setValue(EAST,  canConnectTo(level, pos, Direction.EAST, type))
                .setValue(DOWN,  canConnectTo(level, pos, Direction.DOWN, type))
                .setValue(UP,    canConnectTo(level, pos, Direction.UP, type))
                .setValue(NORTH, canConnectTo(level, pos, Direction.NORTH, type))
                .setValue(SOUTH, canConnectTo(level, pos, Direction.SOUTH, type));

        if(state != newState) {
            level.setBlock(pos, newState, 3);
        }
    }

    @Override public void registerModel(BlockStateProvider provider, ResourceLocation modelLocation) { }
}
