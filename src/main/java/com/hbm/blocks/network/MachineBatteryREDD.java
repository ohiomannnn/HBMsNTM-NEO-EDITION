package com.hbm.blocks.network;

import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.storage.BatteryREDDBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.util.BobMathUtil;
import com.hbm.util.TagsUtilDegradation;
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
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;

import java.math.BigInteger;
import java.util.List;

public class MachineBatteryREDD extends DummyableBlock {

    public MachineBatteryREDD(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch (type) {
            case CORE -> new BatteryREDDBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).power().conductor();
            default -> null;
        };
    }

    @Override public int[] getDimensions() { return new int[] {9, 0, 2, 2, 4, 4}; }
    @Override public int getOffset() { return 2; }

    public static final MapCodec<MachineBatteryREDD> CODEC = simpleCodec(MachineBatteryREDD::new);
    @Override protected MapCodec<MachineBatteryREDD> codec() { return CODEC; }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        x += dir.getStepX() * offset;
        z += dir.getStepZ() * offset;

        Direction rot = dir.getClockWise();

        this.makeExtra(level, new BlockPos(x + dir.getStepX() * 2 + rot.getStepX() * 2, y, z + dir.getStepZ() * 2 + rot.getStepZ() * 2));
        this.makeExtra(level, new BlockPos(x + dir.getStepX() * 2 - rot.getStepX() * 2, y, z + dir.getStepZ() * 2 - rot.getStepZ() * 2));
        this.makeExtra(level, new BlockPos(x - dir.getStepX() * 2 + rot.getStepX() * 2, y, z - dir.getStepZ() * 2 + rot.getStepZ() * 2));
        this.makeExtra(level, new BlockPos(x - dir.getStepX() * 2 - rot.getStepX() * 2, y, z - dir.getStepZ() * 2 - rot.getStepZ() * 2));
        this.makeExtra(level, new BlockPos(x + rot.getStepX() * 4, y, z + rot.getStepZ() * 4));
        this.makeExtra(level, new BlockPos(x - rot.getStepX() * 4, y, z - rot.getStepZ() * 4));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player, 0);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return IPersistentNBT.getDropsFromLootParams(state, params);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        CompoundTag persistent = TagsUtilDegradation.getTag(stack).getCompound("persistent");
        if (persistent.contains("power")) {
            components.add(Component.literal(BobMathUtil.format(new BigInteger(persistent.getByteArray("power"))) + " HE").withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return BaseEntityBlock.createTickerHelper(type, ModBlockEntityTypes.BATTERY_REDD.get(), BatteryREDDBlockEntity::tick);
    }
}
