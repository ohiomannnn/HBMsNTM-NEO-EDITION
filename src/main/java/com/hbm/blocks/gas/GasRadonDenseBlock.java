package com.hbm.blocks.gas;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.FalloutBlock;
import com.hbm.extprop.LivingProperties;
import com.hbm.lib.ModEffect;
import com.hbm.util.ArmorRegistry;
import com.hbm.util.ArmorRegistry.HazardClass;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GasRadonDenseBlock extends GasBaseBlock {

    public GasRadonDenseBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            if (!ArmorRegistry.hasAllProtection(livingEntity, EquipmentSlot.HEAD, HazardClass.PARTICLE_FINE)) {
                //ArmorUtil.damageGasMaskFilter(entityLiving, 1);
            } else {
                ContaminationUtil.contaminate(livingEntity, HazardType.RADIATION, ContaminationType.RAD_BYPASS, 0.05F);
                livingEntity.addEffect(new MobEffectInstance(ModEffect.RADIATION, 15 * 20, 0));
                LivingProperties.incrementAsbestos(livingEntity, 1);
            }
        }
    }

    @Override
    public Direction getFirstDirection(Level level, BlockPos pos) {
        if (level.getRandom().nextInt(2) == 0) {
            return Direction.UP;
        }
        return Direction.DOWN;
    }

    @Override
    public Direction getSecondDirection(Level level, BlockPos pos) {
        return this.randomHorizontal(level.getRandom());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide) {
            if (random.nextInt(20) == 0) {
                if (level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK)) {
                    level.setBlock(pos.below(), ModBlocks.WASTE_EARTH.get().defaultBlockState(), 3);
                }
            }

            if (random.nextInt(30) == 0) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

                if (FalloutBlock.canPlaceBlockAt(level, pos)) {
                    level.setBlock(pos, ModBlocks.FALLOUT.get().defaultBlockState(), 3);
                }
            }
        }
        super.tick(state, level, pos, random);
    }
}
