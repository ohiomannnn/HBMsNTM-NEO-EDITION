package com.hbm.blocks.machine;

import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.storage.BatteryREDDBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class MachinePressBlock extends DummyableBlock {

    public MachinePressBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch (type) {
//            case CORE -> new BatteryREDDBlockEntity(pos, pressed);
//            case EXTRA -> new ProxyComboBlockEntity(pos, pressed).inventory();
            default -> null;
        };
    }

    @Override public int[] getDimensions() { return new int[] {2, 0, 0, 0, 0, 0}; }
    @Override public int getOffset() { return 0; }

    public static final MapCodec<MachinePressBlock> CODEC = simpleCodec(MachinePressBlock::new);
    @Override public MapCodec<MachinePressBlock> codec() { return CODEC; }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player, 0);
    }
}
