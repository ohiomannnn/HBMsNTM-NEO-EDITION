package com.hbm.blocks.network;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.storage.BatterySocketBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.util.BobMathUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class MachineBatterySocketBlock extends DummyableBlock implements ITooltipProvider, ILookOverlay {

    public MachineBatterySocketBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch (type) {
            case CORE -> new BatterySocketBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().power().conductor();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return level.isClientSide ? null : BaseEntityBlock.createTickerHelper(type, ModBlockEntities.BATTERY_SOCKET.get(), BatterySocketBlockEntity::serverTick);
    }

    @Override public int[] getDimensions() { return new int[] {1, 0, 1, 0, 1, 0}; }
    @Override public int getOffset() { return 0; }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        Direction rot = dir.getClockWise();
        this.makeExtra(level, new BlockPos(x - dir.getStepX(), y, z - dir.getStepZ()));
        this.makeExtra(level, new BlockPos(x + rot.getStepX(), y, z + rot.getStepZ()));
        this.makeExtra(level, new BlockPos(x - dir.getStepX() + rot.getStepX(), y, z - dir.getStepZ() + rot.getStepZ()));
    }

    public static final MapCodec<MachineBatterySocketBlock> CODEC = simpleCodec(MachineBatterySocketBlock::new);
    @Override protected MapCodec<MachineBatterySocketBlock> codec() { return CODEC; }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player, 0);
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if (corePos == null) return 0;
        if (level.getBlockEntity(corePos) instanceof BatterySocketBlockEntity be) {
            return be.getComparatorPower();
        }
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        this.addStandardInfo(components);
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if (corePos == null) return;
        if (level.getBlockEntity(corePos) instanceof BatterySocketBlockEntity be) {
            if (be.syncStack.isEmpty()) return;

            List<Component> text = new ArrayList<>();
            text.add(Component.literal(BobMathUtil.getShortNumber(be.syncPower) + " / " + BobMathUtil.getShortNumber(be.syncMaxPower) + "HE"));

            double percent = (double) be.syncPower / be.syncMaxPower;
            int charge = (int) Math.floor(percent * 10_000D);
            int color = ((int) (0xFF - 0xFF * percent)) << 16 | ((int)(0xFF * percent) << 8);

            text.add(Component.literal((charge / 100D) + "%").withColor(color));

            ILookOverlay.printGeneric(event, be.syncStack.getDisplayName(), 0xffff00, 0x404000, text);
        }
    }
}
