package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.oil.MachineOilDerrickBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.main.NuclearTechMod;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;

public class MachineOilDerrick extends DummyableBlock {

    public MachineOilDerrick(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new MachineOilDerrickBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).inventory().power().fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    public static final MapCodec<MachineOilDerrick> CODEC = simpleCodec(MachineOilDerrick::new);
    @Override public MapCodec<MachineOilDerrick> codec() { return CODEC; }

    @Override public int[] getDimensions() { return new int[] {9, 0, 1, 1, 1, 1}; }
    @Override public int getOffset() { return 0; }
    @Override public int getHeightOffset() { return 0; }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        NuclearTechMod.LOGGER.error("[OilDerrick] setPlacedBy pos={} state={} placer={} hand={} stack={}",
                pos,
                state,
                placer == null ? "null" : placer.getName().getString(),
                placer instanceof Player player && player.getMainHandItem() == stack ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND,
                stack);
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        // Derrick uses a wide custom footprint, so the generic orphan cascade from DummyableBlock
        // is too aggressive here and can delete a valid placement immediately.
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
            this.destroyDerrick(level, pos);
        }

        return result;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!level.isClientSide && state.getBlock() == this && !state.is(newState.getBlock()) && !safeRem) {
            this.destroyDerrick(level, pos);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected boolean checkRequirement(Level level, BlockPos pos, Direction dir, int offset) {
        boolean base = this.debugCheckSpace(level, pos, new int[] {1, -1, 0, 0, 0, 0}, pos, dir, "base");
        boolean top = this.debugCheckSpace(level, pos.above(1), new int[] {8, 0, 1, 1, 1, 1}, pos, dir, "top");
        boolean ne = this.debugCheckSpace(level, pos.offset(1, 1, 1), new int[] {-1, 1, 0, 0, 0, 0}, pos, dir, "north_east");
        boolean nw = this.debugCheckSpace(level, pos.offset(-1, 1, 1), new int[] {-1, 1, 0, 0, 0, 0}, pos, dir, "north_west");
        boolean se = this.debugCheckSpace(level, pos.offset(1, 1, -1), new int[] {-1, 1, 0, 0, 0, 0}, pos, dir, "south_east");
        boolean sw = this.debugCheckSpace(level, pos.offset(-1, 1, -1), new int[] {-1, 1, 0, 0, 0, 0}, pos, dir, "south_west");

        if(!(base && top && ne && nw && se && sw)) {
            NuclearTechMod.LOGGER.warn("[OilDerrick] placement rejected at {} facing {} (offset={})", pos, dir, offset);
        }

        return base && top && ne && nw && se && sw;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        MultiblockHandlerXR.fillSpace(level, pos, new int[] {1, -1, 0, 0, 0, 0}, this, dir);
        MultiblockHandlerXR.fillSpace(level, pos.above(1), new int[] {8, 0, 1, 1, 1, 1}, this, dir);
        MultiblockHandlerXR.fillSpace(level, pos.offset(1, 1, 1), new int[] {-1, 1, 0, 0, 0, 0}, this, dir);
        MultiblockHandlerXR.fillSpace(level, pos.offset(1, 1, -1), new int[] {-1, 1, 0, 0, 0, 0}, this, dir);
        MultiblockHandlerXR.fillSpace(level, pos.offset(-1, 1, 1), new int[] {-1, 1, 0, 0, 0, 0}, this, dir);
        MultiblockHandlerXR.fillSpace(level, pos.offset(-1, 1, -1), new int[] {-1, 1, 0, 0, 0, 0}, this, dir);
    }

    private void destroyDerrick(Level level, BlockPos pos) {
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

    private boolean debugCheckSpace(Level level, BlockPos corePos, int[] dim, BlockPos placedPos, Direction dir, String part) {
        boolean ok = MultiblockHandlerXR.checkSpace(level, corePos, dim, placedPos, dir);
        if(ok) {
            return true;
        }

        int[] rot = MultiblockHandlerXR.rotate(dim, dir);
        int x = corePos.getX();
        int y = corePos.getY();
        int z = corePos.getZ();

        for(int a = x - rot[4]; a <= x + rot[5]; a++) {
            for(int b = y - rot[1]; b <= y + rot[0]; b++) {
                for(int c = z - rot[2]; c <= z + rot[3]; c++) {
                    BlockPos checkPos = new BlockPos(a, b, c);
                    if(checkPos.equals(placedPos)) {
                        continue;
                    }

                    BlockState state = level.getBlockState(checkPos);
                    if(!state.canBeReplaced()) {
                        NuclearTechMod.LOGGER.warn(
                                "[OilDerrick] blocked part={} core={} dir={} placed={} blocked={} block={}",
                                part,
                                corePos,
                                dir,
                                placedPos,
                                checkPos,
                                BuiltInRegistries.BLOCK.getKey(state.getBlock())
                        );
                        return false;
                    }
                }
            }
        }

        NuclearTechMod.LOGGER.warn("[OilDerrick] blocked part={} core={} dir={} placed={} reason=unknown", part, corePos, dir, placedPos);
        return false;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }
}
