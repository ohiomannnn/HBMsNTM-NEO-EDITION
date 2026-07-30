package com.hbm.blocks.machine;

import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.oil.MachineFrackingTowerBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.IPersistentInfoProvider;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.util.BobMathUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class MachineFrackingTowerBlock extends DummyableBlock implements IPersistentInfoProvider {

    public MachineFrackingTowerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new MachineFrackingTowerBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).power().fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    public static final MapCodec<MachineFrackingTowerBlock> CODEC = simpleCodec(MachineFrackingTowerBlock::new);
    @Override public MapCodec<MachineFrackingTowerBlock> codec() { return CODEC; }

    @Override public int[] getDimensions() { return new int[] {3, 0, 0, 0, 0, 0}; }
    @Override public int getOffset() { return 0; }

    @Override
    protected boolean checkRequirement(Level level, BlockPos pos, Direction dir, int offset) {

        if(!MultiblockHandlerXR.checkSpace(level, pos, this.getDimensions(), pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, pos.above(2), new int[] {1, 0, 3, 3, 3, 3}, pos, dir)) return false;

        if(!MultiblockHandlerXR.checkSpace(level, pos.offset(-2, 2, -2), new int[] {-1, 2, 0, 1, 0, 1}, pos, Direction.NORTH)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, pos.offset(-2, 2, 3), new int[] {-1, 2, 0, 1, 0, 1}, pos, Direction.NORTH)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, pos.offset(3, 2, -2), new int[] {-1, 2, 0, 1, 0, 1}, pos, Direction.NORTH)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, pos.offset(3, 2, 3), new int[] {-1, 2, 0, 1, 0, 1}, pos, Direction.NORTH)) return false;

        if(!MultiblockHandlerXR.checkSpace(level, pos, new int[] {10, -4, 2, 2, 2, 2}, pos, dir)) return false;
        if(!MultiblockHandlerXR.checkSpace(level, pos, new int[] {24, -9, 1, 1, 1, 1}, pos, dir)) return false;

        if(!MultiblockHandlerXR.checkSpace(level, pos.above(15), new int[] {1, 0, 1, 1, -2, 3}, pos, Direction.WEST)) return false;

        return super.checkRequirement(level, pos, dir, offset);
    }

    @Override
    public void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        MultiblockHandlerXR.fillSpace(level, pos, this.getDimensions(), this, dir);
        MultiblockHandlerXR.fillSpace(level, pos.above(2), new int[] {1, 0, 3, 3, 3, 3}, this, dir);

        MultiblockHandlerXR.fillSpace(level, pos.offset(-2, 2, -2), new int[] {-1, 2, 0, 1, 0, 1}, this, Direction.NORTH);
        MultiblockHandlerXR.fillSpace(level, pos.offset(-2, 2, 3), new int[] {-1, 2, 0, 1, 0, 1}, this, Direction.NORTH);
        MultiblockHandlerXR.fillSpace(level, pos.offset(3, 2, -2), new int[] {-1, 2, 0, 1, 0, 1}, this, Direction.NORTH);
        MultiblockHandlerXR.fillSpace(level, pos.offset(3, 2, 3), new int[] {-1, 2, 0, 1, 0, 1}, this, Direction.NORTH);

        MultiblockHandlerXR.fillSpace(level, pos, new int[] {10, -4, 2, 2, 2, 2}, this, dir);
        MultiblockHandlerXR.fillSpace(level, pos, new int[] {24, -9, 1, 1, 1, 1}, this, dir);

        MultiblockHandlerXR.fillSpace(level, pos.above(15), new int[] {1, 0, 1, 1, -2, 3}, this, Direction.WEST);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return IPersistentNBT.getDropsFromLootParams(state, params);
    }

    @Override
    public void appendHoverText(ItemStack stack, CompoundTag persistentTag, List<Component> components, Item.TooltipContext context, TooltipFlag flag) {

        components.add(Component.literal(BobMathUtil.getShortNumber(persistentTag.getLong("power")) + "HE").withStyle(ChatFormatting.GREEN));
        for(int i = 0; i < 2; i++) {
            FluidTank tank = new FluidTank(Fluids.NONE, 0);
            tank.readFromNBT(persistentTag, "t" + i);
            components.add(Component.literal(tank.getFill() + "/" + tank.getMaxFill() + "mB ").append(tank.getTankType().getName()).withStyle(ChatFormatting.YELLOW));
        }
    }
}
