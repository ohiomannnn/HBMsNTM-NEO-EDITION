package com.hbm.blocks.generic;

import com.hbm.blocks.EnumMultiBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.MetaHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.HitResult;

public class OreBasaltBlock extends EnumMultiBlock {

    public enum BasaltOreType {
        SULFUR,
        FLUORITE,
        ASBESTOS,
        GEM,
        MOLYSITE
    }

    public static final IntegerProperty SUBTYPE = IntegerProperty.create("subtype", 0, BasaltOreType.values().length - 1);

    public OreBasaltBlock(Properties properties) {
        super(properties, BasaltOreType.class, true, true);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(SUBTYPE, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SUBTYPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(SUBTYPE, MetaHelper.getMeta(context.getItemInHand()));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
        MetaHelper.setMeta(stack, rectify(this.getMeta(state)));
        return stack;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        BlockPos above = pos.above();
        if(this.getMeta(state) == BasaltOreType.ASBESTOS.ordinal() && level.getBlockState(above).isAir()) {
            if(level.random.nextInt(10) == 0) level.setBlock(above, NtmBlocks.GAS_ASBESTOS.get().defaultBlockState(), 3);
            for(int i = 0; i < 5; i++) level.addParticle(ParticleTypes.MYCELIUM, pos.getX() + level.random.nextFloat(), pos.getY() + 1.1, pos.getZ() + level.random.nextFloat(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        if(this.getMeta(state) == BasaltOreType.ASBESTOS.ordinal()) level.setBlock(pos, NtmBlocks.GAS_ASBESTOS.get().defaultBlockState(), 3);
    }

    @Override public int getMeta(BlockState state) { return state.getValue(SUBTYPE); }
    @Override public int getSubCount() { return BasaltOreType.values().length; }
}
