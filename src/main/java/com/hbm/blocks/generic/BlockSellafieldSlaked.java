package com.hbm.blocks.generic;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.awt.*;

public class BlockSellafieldSlaked extends Block {

    public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 3);

    public BlockSellafieldSlaked(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
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

    @OnlyIn(Dist.CLIENT)
    public static class ColorHandler implements BlockColor {

        @Override
        public int getColor(BlockState blockState, BlockAndTintGetter blockAndTintGetter, BlockPos blockPos, int i) {
            int variant = blockState.getValue(BlockSellafieldSlaked.VARIANT);
            return Color.HSBtoRGB(0F, 0F, 1F - variant / 15F);
        }
    }
}
