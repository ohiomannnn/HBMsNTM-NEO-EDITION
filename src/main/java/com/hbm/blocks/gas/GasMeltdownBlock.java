package com.hbm.blocks.gas;

import com.hbm.blocks.ModBlocks;
import com.hbm.extprop.LivingProperties;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.lib.ModEffect;
import com.hbm.util.ArmorRegistry;
import com.hbm.util.ArmorRegistry.HazardClass;
import com.hbm.util.ArmorUtil;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GasMeltdownBlock extends GasBaseBlock {

    public GasMeltdownBlock(Properties properties) {
        super(properties, 0.1F, 0.4F, 0.1F);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {

            ContaminationUtil.contaminate(livingEntity, HazardType.RADIATION, ContaminationType.CREATIVE, 0.5F);
            livingEntity.addEffect(new MobEffectInstance(ModEffect.RADIATION, 60 * 20, 2));

            if (ArmorRegistry.hasProtection(livingEntity, EquipmentSlot.HEAD, HazardClass.PARTICLE_FINE)) {
                ArmorUtil.damageGasMaskFilter(livingEntity, 1);
            } else {
                LivingProperties.incrementAsbestos(livingEntity, 5); // Mesothelioma can be developed as a result of exposure to radiation in the lungs
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        level.addParticle(ParticleTypes.MYCELIUM, pos.getX() + random.nextFloat(), pos.getY() + random.nextFloat(), pos.getZ() + random.nextFloat(), 0.0, 0.0, 0.0);
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

            Direction dir = Direction.values()[random.nextInt(Direction.values().length)];

            BlockPos relative = pos.relative(dir);

            if (random.nextInt(7) == 0 && level.isEmptyBlock(relative)) {
                level.setBlock(pos, ModBlocks.GAS_RADON_DENSE.get().defaultBlockState(), 3);
            }

            if (level.canSeeSky(pos)) {
                ChunkRadiationManager.proxy.incrementRad(level, pos, 5);
            }

            if (random.nextInt(350) == 0) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                return;
            }
        }
        super.tick(state, level, pos, random);
    }
}
