package com.hbm.blocks.network;

import com.hbm.blockentity.Tickable;
import com.hbm.blockentity.network.PipeBaseBlockEntity;
import com.hbm.extprop.HbmPlayerAttachments;
import com.hbm.handler.HbmKeybinds.EnumKeybind;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.lib.ModAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class FluidDuctBaseBlock extends Block implements EntityBlock, IBlockFluidDuct {

    public FluidDuctBaseBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PipeBaseBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> { if (be instanceof Tickable tickable) tickable.updateEntity(); };
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof IItemFluidIdentifier iifi) {
            FluidType type = iifi.getType(level, pos, player.getItemInHand(InteractionHand.MAIN_HAND));

            HbmPlayerAttachments data = player.getData(ModAttachments.PLAYER_ATTACHMENT.get());

            if (!data.getKeyPressed(EnumKeybind.TOOL_CTRL) && !player.isShiftKeyDown()) {
                if (level.getBlockEntity(pos) instanceof PipeBaseBlockEntity pipe) {
                    if (pipe.getFluidType() != type) {
                        pipe.setFluidType(type);
                        if (level.getBlockState(pos).getBlock() instanceof FluidDuctConnectingBlock connectingDuct) connectingDuct.updateConnections(level, pos, type);

                        return ItemInteractionResult.SUCCESS;
                    }
                }
            } else {
                if (level.getBlockEntity(pos) instanceof PipeBaseBlockEntity) {
                    this.changeTypeRecursively(level, pos, type, 64);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        return ItemInteractionResult.FAIL;
    }


    @Override
    public void changeTypeRecursively(Level level, BlockPos pos, FluidType type, int loopsRemaining) {
        if (level.getBlockEntity(pos) instanceof PipeBaseBlockEntity pipe) {
            if (pipe.getFluidType() != type) {
                pipe.setFluidType(type);
                if (level.getBlockState(pos).getBlock() instanceof FluidDuctConnectingBlock connectingDuct) connectingDuct.updateConnections(level, pos, type);

                if (loopsRemaining > 0) {
                    for (Direction dir : Direction.values()) {
                        if (level.getBlockState(pos).getBlock() instanceof IBlockFluidDuct ibfi) {
                            ibfi.changeTypeRecursively(level, pos.relative(dir), type, loopsRemaining - 1);
                        }
                    }
                }
            }
        }
    }
}
