package com.hbm.blocks.machine.heater;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.heater.HeaterHeatexBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class HeaterHeatexBlock extends AbstractHeaterBlock implements ITooltipProvider, ILookOverlay {

    public HeaterHeatexBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<HeaterHeatexBlock> CODEC = simpleCodec(HeaterHeatexBlock::new);
    @Override public MapCodec<HeaterHeatexBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new HeaterHeatexBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).fluid();
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, net.minecraft.core.Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        BlockPos core = pos.relative(dir, offset);
        this.makeExtra(level, core.offset(1, 0, 1));
        this.makeExtra(level, core.offset(1, 0, -1));
        this.makeExtra(level, core.offset(-1, 0, 1));
        this.makeExtra(level, core.offset(-1, 0, -1));
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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        if(!player.isShiftKeyDown() || !(stack.getItem() instanceof IItemFluidIdentifier identifier)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) {
            return ItemInteractionResult.FAIL;
        }

        BlockEntity te = level.getBlockEntity(corePos);
        if(!(te instanceof HeaterHeatexBlockEntity heater)) {
            return ItemInteractionResult.FAIL;
        }

        FluidType type = identifier.getType(level, corePos, stack);
        heater.tanks[0].setTankType(type);
        heater.setChanged();
        player.displayClientMessage(Component.translatable("block.hbmsntm.machine_fluid_tank.changed_type_to", type.getName()).withStyle(ChatFormatting.YELLOW), false);
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return;

        BlockEntity te = level.getBlockEntity(corePos);
        if(!(te instanceof HeaterHeatexBlockEntity heater)) return;

        List<Component> text = new ArrayList<>();
        text.add(Component.literal(String.format("%,d", heater.heatEnergy) + " TU"));
        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        this.addStandardInfo(components);
    }
}
