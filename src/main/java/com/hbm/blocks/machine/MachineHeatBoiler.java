package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.boiler.MachineHeatBoilerBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.trait.FT_Heatable;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.main.NuclearTechMod;
import com.hbm.util.BobMathUtil;
import com.hbm.util.TagsUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MachineHeatBoiler extends DummyableBlock implements ILookOverlay {

    public MachineHeatBoiler(Properties properties) {
        super(properties);
    }

    public static final MapCodec<MachineHeatBoiler> CODEC = simpleCodec(MachineHeatBoiler::new);
    @Override public MapCodec<MachineHeatBoiler> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new MachineHeatBoilerBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).fluid().heatSource();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    @Override
    public int[] getDimensions() {
        return new int[] { 3, 0, 1, 1, 1, 1 };
    }

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        if(player.isShiftKeyDown()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if(!(stack.getItem() instanceof IItemFluidIdentifier identifier)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) {
            return ItemInteractionResult.FAIL;
        }

        BlockEntity te = level.getBlockEntity(corePos);
        if(!(te instanceof MachineHeatBoilerBlockEntity boiler)) {
            return ItemInteractionResult.FAIL;
        }

        FluidType type = identifier.getType(level, corePos, stack);
        boiler.tanks[0].setTankType(type);
        boiler.setChanged();
        player.displayClientMessage(
                net.minecraft.network.chat.Component.translatable("block.hbmsntm.machine_boiler.changed_type_to", type.getName()).withStyle(ChatFormatting.YELLOW),
                false
        );

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if(!level.isClientSide && !safeRem) {
            this.destroyBoiler(level, pos);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!level.isClientSide && state.getBlock() == this && !state.is(newState.getBlock()) && !safeRem) {
            this.destroyBoiler(level, pos);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    private void destroyBoiler(Level level, BlockPos pos) {
        BlockPos center = this.findCore(level, pos);
        if(center == null) {
            center = pos;
        }

        if(level.getBlockState(center).getBlock() != this) {
            return;
        }

        safeRem = true;
        try {
            for(int dx = -2; dx <= 2; dx++) {
                for(int dy = -1; dy <= 4; dy++) {
                    for(int dz = -2; dz <= 2; dz++) {
                        BlockPos target = new BlockPos(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                        if(level.getBlockState(target).getBlock() == this) {
                            level.removeBlock(target, false);
                        }
                    }
                }
            }
        } finally {
            safeRem = false;
        }
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return;

        BlockEntity te = level.getBlockEntity(corePos);
        if(!(te instanceof MachineHeatBoilerBlockEntity boiler)) return;
        if(boiler.tanks == null || boiler.tanks.length < 2) return;

        List<Component> text = new ArrayList<>();
        text.add(Component.literal("-> ").withStyle(ChatFormatting.GREEN)
                .append(boiler.tanks[0].getTankType().getName())
                .append(Component.literal(" " + BobMathUtil.format(boiler.tanks[0].getFill()) + "/" + BobMathUtil.format(boiler.tanks[0].getMaxFill()) + " mB").withStyle(ChatFormatting.GRAY)));
        text.add(Component.literal("<- ").withStyle(ChatFormatting.RED)
                .append(boiler.tanks[1].getTankType().getName())
                .append(Component.literal(" " + BobMathUtil.format(boiler.tanks[1].getFill()) + "/" + BobMathUtil.format(boiler.tanks[1].getMaxFill()) + " mB").withStyle(ChatFormatting.GRAY)));

        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
    }
}
