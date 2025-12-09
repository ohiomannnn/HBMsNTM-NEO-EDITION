package com.hbm.blocks.gas;

import com.hbm.extprop.LivingProperties;
import com.hbm.util.ArmorRegistry;
import com.hbm.util.ArmorRegistry.HazardClass;
import com.hbm.util.ArmorUtil;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GasRadonBlock extends GasBaseBlock {

    public GasRadonBlock(Properties properties) {
        super(properties, 0.1F, 0.8F, 0.1F);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            if (!ArmorRegistry.hasProtection(livingEntity, EquipmentSlot.HEAD, HazardClass.PARTICLE_FINE)) {
                ArmorUtil.damageGasMaskFilter(livingEntity, 1);
            } else {
                ContaminationUtil.contaminate(livingEntity, HazardType.RADIATION, ContaminationType.RAD_BYPASS, 0.05F);
                LivingProperties.incrementAsbestos(livingEntity, 1);
            }
        }
    }

    @Override
    public Direction getFirstDirection(Level level, BlockPos pos) {
        if (level.getRandom().nextInt(5) == 0) {
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
        if (random.nextInt(50) == 0) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            return;
        }
        super.tick(state, level, pos, random);
    }
}
