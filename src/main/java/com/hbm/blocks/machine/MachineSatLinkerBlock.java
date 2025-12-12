package com.hbm.blocks.machine;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blockentity.machine.MachineSatLinkerBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class MachineSatLinkerBlock extends BaseEntityBlock {
    public static final MapCodec<MachineSatLinkerBlock> CODEC = simpleCodec(MachineSatLinkerBlock::new);
    public MapCodec<MachineSatLinkerBlock> codec() { return CODEC; }

    public MachineSatLinkerBlock(Properties properties) {
        super(properties);
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof MachineSatLinkerBlockEntity satLinkerBlockEntity) {
                NonNullList<ItemStack> stacks = NonNullList.create();
                for (int i = 0; i < satLinkerBlockEntity.getItems().getSlots(); i++) {
                    stacks.add(satLinkerBlockEntity.getItems().getStackInSlot(i));
                }
                if (level instanceof ServerLevel) {
                    Containers.dropContents(level, pos, stacks);
                }
                super.onRemove(state, level, pos, newState, isMoving);
            } else {
                super.onRemove(state, level, pos, newState, isMoving);
            }
        }

    }


    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof MachineSatLinkerBlockEntity entity) {
            if (!level.isClientSide) {
                player.openMenu(new SimpleMenuProvider(entity, entity.getDisplayName()), pos);
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MachineSatLinkerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : BaseEntityBlock.createTickerHelper(type, ModBlockEntities.MACHINE_SATLINKER.get(), MachineSatLinkerBlockEntity::serverTick);
    }
}
