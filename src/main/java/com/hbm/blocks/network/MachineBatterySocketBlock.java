package com.hbm.blocks.network;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.storage.BatterySocketBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ITooltipProvider;
import com.mojang.serialization.MapCodec;
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

import java.util.List;

public class MachineBatterySocketBlock extends DummyableBlock implements ITooltipProvider {

    public MachineBatterySocketBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch (type) {
            case CORE -> new BatterySocketBlockEntity(ModBlockEntities.BATTERY_SOCKET.get(), pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().power().conductor();
            default -> null;
        };
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

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        this.addStandardInfo(components);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return level.isClientSide ? null : BaseEntityBlock.createTickerHelper(type, ModBlockEntities.BATTERY_SOCKET.get(), BatterySocketBlockEntity::serverTick);
    }
}
