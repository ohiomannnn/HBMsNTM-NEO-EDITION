package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.oil.MachineRefineryBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.main.NuclearTechMod;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

public class MachineRefinery extends DummyableBlock {

    public MachineRefinery(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new MachineRefineryBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().power().fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    public static final MapCodec<MachineRefinery> CODEC = simpleCodec(MachineRefinery::new);
    @Override public MapCodec<MachineRefinery> codec() { return CODEC; }

    @Override public int[] getDimensions() { return new int[] {8, 0, 1, 1, 1, 1}; }
    @Override public int getOffset() { return 1; }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        NuclearTechMod.LOGGER.debug("[OilRefinery] placed at {} state={} placer={}", pos, state, placer == null ? "null" : placer.getName().getString());
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, net.minecraft.world.level.block.Block block, BlockPos fromPos, boolean isMoving) {
        // Like the derrick, the refinery uses a custom footprint and the generic orphan cascade is too eager.
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockState result = super.playerWillDestroy(level, pos, state, player);
        if(!level.isClientSide && !safeRem) {
            this.destroyRefinery(level, pos);
        }

        return result;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!level.isClientSide && state.getBlock() == this && !state.is(newState.getBlock()) && !safeRem) {
            this.destroyRefinery(level, pos);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    private void destroyRefinery(Level level, BlockPos pos) {
        BlockPos center = this.findCore(level, pos);
        if(center == null) {
            center = pos;
        }

        if(level.getBlockState(center).getBlock() != this) {
            return;
        }

        safeRem = true;
        try {
            this.clearArea(level, center);
        } finally {
            safeRem = false;
        }
    }

    private void clearArea(Level level, BlockPos center) {
        int x = center.getX();
        int y = center.getY();
        int z = center.getZ();

        for(int dx = -4; dx <= 4; dx++) {
            for(int dy = -2; dy <= 10; dy++) {
                for(int dz = -4; dz <= 4; dz++) {
                    BlockPos target = new BlockPos(x + dx, y + dy, z + dz);
                    if(level.getBlockState(target).getBlock() == this) {
                        level.removeBlock(target, false);
                    }
                }
            }
        }
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        this.makeExtra(level, new BlockPos(pos.getX() - dir.getStepX() + 1, pos.getY(), pos.getZ() - dir.getStepZ() + 1));
        this.makeExtra(level, new BlockPos(pos.getX() - dir.getStepX() + 1, pos.getY(), pos.getZ() - dir.getStepZ() - 1));
        this.makeExtra(level, new BlockPos(pos.getX() - dir.getStepX() - 1, pos.getY(), pos.getZ() - dir.getStepZ() + 1));
        this.makeExtra(level, new BlockPos(pos.getX() - dir.getStepX() - 1, pos.getY(), pos.getZ() - dir.getStepZ() - 1));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }
}
