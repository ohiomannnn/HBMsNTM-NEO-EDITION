package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineWoodBurnerBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ITooltipProvider;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class MachineWoodBurnerBlock extends DummyableBlock implements ITooltipProvider {

    public MachineWoodBurnerBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<MachineWoodBurnerBlock> CODEC = simpleCodec(MachineWoodBurnerBlock::new);
    @Override public MapCodec<MachineWoodBurnerBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new MachineWoodBurnerBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().power().fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    @Override public int[] getDimensions() { return new int[] { 1, 0, 1, 0, 1, 0 }; }
    @Override public int getOffset() { return 0; }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        Direction side = dir.getClockWise();
        this.makeExtra(level, pos.relative(dir.getOpposite()));
        this.makeExtra(level, pos.relative(dir.getOpposite()).relative(side));
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if(!level.isClientSide && !safeRem) {
            this.destroyWoodBurner(level, pos);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!level.isClientSide && state.getBlock() == this && !state.is(newState.getBlock()) && !safeRem) {
            this.destroyWoodBurner(level, pos);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    private void destroyWoodBurner(Level level, BlockPos pos) {
        BlockPos core = this.findCore(level, pos);
        if(core == null) core = pos;

        safeRem = true;
        try {
            for(int dx = -2; dx <= 2; dx++) {
                for(int dy = 0; dy <= 2; dy++) {
                    for(int dz = -2; dz <= 2; dz++) {
                        BlockPos target = core.offset(dx, dy, dz);
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        this.addStandardInfo(components);
    }
}
