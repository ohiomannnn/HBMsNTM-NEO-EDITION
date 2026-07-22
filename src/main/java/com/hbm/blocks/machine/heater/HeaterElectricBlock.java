package com.hbm.blocks.machine.heater;

import api.hbm.block.IToolable;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.heater.HeaterElectricBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.util.BobMathUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class HeaterElectricBlock extends AbstractHeaterBlock implements ITooltipProvider, ILookOverlay, IToolable {

    public HeaterElectricBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<HeaterElectricBlock> CODEC = simpleCodec(HeaterElectricBlock::new);
    @Override public MapCodec<HeaterElectricBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new HeaterElectricBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).power();
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
        return new int[] { 0, 0, 1, 2, 1, 1 };
    }

    @Override
    public int getOffset() {
        return 2;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockState result = super.playerWillDestroy(level, pos, state, player);
        if(!level.isClientSide && !safeRem) {
            this.destroyHeater(level, pos);
        }

        return result;
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
            this.clearMatching(level, center, -2, 2, -1, 3, -2, 2);
        } finally {
            safeRem = false;
        }
    }

    @Override
    public boolean onScrew(Level level, Player player, BlockPos pos, Direction direction, ToolType tool) {
        if(tool != ToolType.SCREWDRIVER && tool != ToolType.HAND_DRILL) {
            return false;
        }

        if(level.isClientSide) return true;

        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return false;

        BlockEntity te = level.getBlockEntity(corePos);
        if(!(te instanceof HeaterElectricBlockEntity heater)) return false;

        if(tool == ToolType.SCREWDRIVER) {
            heater.toggleSettingUp();
        } else {
            heater.toggleSettingDown();
        }
        heater.setChanged();
        return true;
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return;

        BlockEntity te = level.getBlockEntity(corePos);
        if(!(te instanceof HeaterElectricBlockEntity heater)) return;

        List<Component> text = new ArrayList<>();
        text.add(Component.literal(String.format("%,d", heater.heatEnergy) + " TU"));
        text.add(Component.literal("-> ").withStyle(ChatFormatting.GREEN).append(Component.literal(BobMathUtil.format(heater.getConsumption()) + " HE/t")));
        text.add(Component.literal("<- ").withStyle(ChatFormatting.RED).append(Component.literal(BobMathUtil.format(heater.getHeatGen()) + " TU/t")));
        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        this.addStandardInfo(components);
    }
}
