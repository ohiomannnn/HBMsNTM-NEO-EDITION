package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.SoyuzLauncherBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.handler.MultiblockHandlerXR;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SoyuzLauncherBlock extends DummyableBlock {

    public SoyuzLauncherBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);

        return switch(type) {
            case CORE -> new SoyuzLauncherBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).power().fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    public static final MapCodec<SoyuzLauncherBlock> CODEC = simpleCodec(SoyuzLauncherBlock::new);
    @Override public MapCodec<SoyuzLauncherBlock> codec() { return CODEC; }

    public static final int HEIGHT = 4;

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if(!(placer instanceof Player player)) return;

        int offset = -getOffset();

        Direction dir = Direction.EAST;

        if(!checkRequirement(level, pos, dir, offset)) {
            level.removeBlock(pos, false);

            if(!player.hasInfiniteMaterials()) {
                ItemStack mainHandStack = player.getMainHandItem();
                ItemStack offHandStack = player.getOffhandItem();
                Item item = this.asItem();

                boolean added = false;

                if(mainHandStack.is(item) && mainHandStack.getCount() < mainHandStack.getMaxStackSize()) {
                    mainHandStack.grow(1);
                    added = true;
                } else if(offHandStack.is(item) && offHandStack.getCount() < offHandStack.getMaxStackSize()) {
                    offHandStack.grow(1);
                    added = true;
                } else if(mainHandStack.isEmpty()) {
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(this));
                    added = true;
                } else if(offHandStack.isEmpty()) {
                    player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(this));
                    added = true;
                }

                if(!added) {
                    player.getInventory().add(new ItemStack(this));
                }
            }
            return;
        }

        if(!level.isClientSide) {
            BlockPos corePos = pos.relative(dir, offset).offset(0, HEIGHT, 0);
            BlockState coreState = this.getStateForCore(level, corePos, player, dir);

            level.setBlock(corePos, coreState, 3);

            this.fillSpace(level, pos, dir, offset);
        }

        level.scheduleTick(pos, this, 1);
        level.scheduleTick(pos, this, 2);
    }

    @Override
    protected boolean checkRequirement(Level level, BlockPos pos, Direction dir, int offset) {

        BlockPos relativePos = pos.relative(dir, offset);
        relativePos = relativePos.offset(0, HEIGHT, 0);

        if(!MultiblockHandlerXR.checkSpace(level, relativePos, new int[] { 0, 1, 6, 6, 6, 6 }, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, relativePos, new int[] { -2, 4, -3, 6, -3, 6 }, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, relativePos, new int[] { -2, 4, 6, -3, -3, 6 }, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, relativePos, new int[] { -2, 4, 6, -3, 6, -3 }, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, relativePos, new int[] { -2, 4, -3, 6, 6, -3 }, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, relativePos, new int[] { 0, 4, 1, 1, -6, 8 }, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, relativePos, new int[] { 0, 4, 2, 2, 9, -5 }, pos, dir)) return false;

        return true;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {

        BlockPos relativePos = pos.relative(dir, offset);
        relativePos = relativePos.offset(0, HEIGHT, 0);

        MultiblockHandlerXR.fillSpace(level, relativePos, new int[] { 0, 1, 6, 6, 6, 6 }, this, dir);
        MultiblockHandlerXR.fillSpace(level, relativePos, new int[] { -2, 4, -3, 6, -3, 6 }, this, dir);
        MultiblockHandlerXR.fillSpace(level, relativePos, new int[] { -2, 4, 6, -3, -3, 6 }, this, dir);
        MultiblockHandlerXR.fillSpace(level, relativePos, new int[] { -2, 4, 6, -3, 6, -3 }, this, dir);
        MultiblockHandlerXR.fillSpace(level, relativePos, new int[] { -2, 4, -3, 6, 6, -3 }, this, dir);
        MultiblockHandlerXR.fillSpace(level, relativePos, new int[] { 0, 4, 1, 1, -6, 8 }, this, dir);
        MultiblockHandlerXR.fillSpace(level, relativePos, new int[] { 0, 4, 2, 2, 9, -5 }, this, dir);

        for(int ix = -6; ix <= 6; ix++) {
            for(int iz = -6; iz <= 6; iz++) {

                if(ix == 6 || ix == -6 || iz == 6 || iz == -6) {
                    this.makeExtra(level, relativePos.offset(ix, 0, iz));
                    this.makeExtra(level, relativePos.offset(ix, 1, iz));
                }
            }
        }
    }

    //because we'll implement our own gnarly behavior here
    @Override public int[] getDimensions() { return new int[] { 0, 0, 0, 0, 0, 0 }; }
    @Override public int getOffset() { return 0; }
}
