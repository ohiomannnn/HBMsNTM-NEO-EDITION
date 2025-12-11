package com.hbm.blocks.machine;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blockentity.machine.MachineSatLinkerBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
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

    // TODO add spilling items

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
