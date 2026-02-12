package com.hbm.blocks.bomb;

import com.hbm.lib.ModEffect;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class TaintBlock extends Block {

    public TaintBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(TAINT_LEVEL, 0));
    }

    public static final IntegerProperty TAINT_LEVEL = IntegerProperty.create("taint_level", 0, 15);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TAINT_LEVEL);
    }

    public static final MapCodec<TaintBlock> CODEC = simpleCodec(TaintBlock::new);
    @Override protected MapCodec<TaintBlock> codec() {
        return CODEC;
    }

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {

        int taintLevel = state.getValue(TAINT_LEVEL);
        if (taintLevel >= 15) return;

        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                for (int k = -3; k <= 3; k++) {
                    if (Math.abs(i) + Math.abs(j) + Math.abs(k) > 4) continue;
                    if (random.nextFloat() > 0.25F) continue;

                    BlockPos targetPos = pos.offset(i, j, k);
                    BlockState targetState = serverLevel.getBlockState(targetPos);

                    if (targetState.isAir() || targetState.is(Blocks.BEDROCK)) continue;

                    int targetLevel = taintLevel + 1;

                    boolean hasAir = false;
                    for (Direction dir : Direction.values()) {
                        if (serverLevel.getBlockState(targetPos.relative(dir)).isAir()) {
                            hasAir = true;
                            break;
                        }
                    }
                    if (!hasAir) targetLevel = taintLevel + 3;
                    if (targetLevel > 15) continue;
                    if (targetState.is(this) && targetState.getValue(TAINT_LEVEL) >= targetLevel) continue;

                    serverLevel.setBlock(targetPos, this.defaultBlockState().setValue(TAINT_LEVEL, taintLevel), 3);
                }
            }
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        int taintLevel = state.getValue(TAINT_LEVEL);
        int potionLevel = 15 - taintLevel;

        Vec3 motion = entity.getDeltaMovement();
        entity.setDeltaMovement(motion.x * 0.6, motion.y, motion.z * 0.6);

        if (entity instanceof LivingEntity livingEntity) {
            MobEffectInstance effect = new MobEffectInstance(ModEffect.TAINT, 15 * 20, potionLevel);

            if (level.random.nextInt(50) == 0) {
                livingEntity.addEffect(effect);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("DO NOT TOUCH, BREATHE OR STARE AT.").withStyle(ChatFormatting.GRAY));
    }
}