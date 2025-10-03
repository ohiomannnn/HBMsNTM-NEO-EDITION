package com.hbm.blocks.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SellafieldSlakedBlock extends Block {

    public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 3);
    public static final IntegerProperty COLOR_LEVEL = IntegerProperty.create("color_level", 0, 10);

    public SellafieldSlakedBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, 0).setValue(COLOR_LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT, COLOR_LEVEL);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        long l = (pos.getX() * 3129871L) ^ (long)pos.getY() * 116129781L ^ (long)pos.getZ();
        l = l * l * 42317861L + l * 11L;
        int i = (int)(l >> 16 & 3L);
        int variant = Math.abs(i) % 4;
        return this.defaultBlockState().setValue(VARIANT, variant);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide) {
            long l = (pos.getX() * 3129871L) ^ (long)pos.getY() * 116129781L ^ (long)pos.getZ();
            l = l * l * 42317861L + l * 11L;
            int textureVariant = (int)(Math.abs((l >> 16) & 3L));

            level.setBlock(pos, state.setValue(VARIANT, textureVariant), 2);
        }
    }
}
