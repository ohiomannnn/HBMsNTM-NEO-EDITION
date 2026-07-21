package com.hbm.blocks.machine.heater;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.heater.HeaterFireboxBlockEntity;
import com.hbm.blockentity.machine.heater.HeaterOvenBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class HeaterOvenBlock extends AbstractHeaterBlock implements ITooltipProvider, ILookOverlay {

    public HeaterOvenBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<HeaterOvenBlock> CODEC = simpleCodec(HeaterOvenBlock::new);
    @Override public MapCodec<HeaterOvenBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new HeaterOvenBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().fluid();
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
        return new int[] { 0, 0, 1, 1, 1, 1 };
    }

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, net.minecraft.world.phys.BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if(!level.isClientSide && !safeRem) {
            this.destroyHeater(level, pos);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!level.isClientSide && state.getBlock() == this && !state.is(newState.getBlock()) && !safeRem) {
            this.destroyHeater(level, pos);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    private void destroyHeater(Level level, BlockPos pos) {
        BlockPos center = this.findCore(level, pos);
        if(center == null) {
            center = pos;
        }

        if(level.getBlockState(center).getBlock() != this) {
            return;
        }

        safeRem = true;
        try {
            this.clearMatching(level, center, -2, 2, -1, 2, -2, 2);
        } finally {
            safeRem = false;
        }
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return;

        BlockEntity te = level.getBlockEntity(corePos);
        if(!(te instanceof HeaterOvenBlockEntity heater)) return;

        List<Component> text = new ArrayList<>();
        text.add(Component.literal(String.format("%,d", heater.heatEnergy) + " TU"));
        text.add(Component.literal("<- ").withStyle(ChatFormatting.RED).append(Component.literal(String.format("%,d", heater.burnHeat) + " TU/t")));
        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        this.addStandardInfo(components);
    }
}
