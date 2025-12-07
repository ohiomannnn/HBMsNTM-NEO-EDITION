package com.hbm.blockentity.machine;

import com.hbm.HBMsNTMClient;
import com.hbm.blockentity.ModBlockEntities;
import com.hbm.extprop.LivingProperties;
import com.hbm.lib.ModEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class DecontaminatorBlockEntity extends BlockEntity {

    public DecontaminatorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DECONTAMINATOR.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DecontaminatorBlockEntity blockEntity) {
        if (!level.isClientSide) {
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(pos.getX() - 0.5, pos.getY(), pos.getZ() - 0.5, pos.getX() + 1.5, pos.getY() + 2, pos.getZ() + 1.5));

            if (!entities.isEmpty()) {
                for (LivingEntity livingEntity : entities) {
                    LivingProperties.incrementRadiation(livingEntity, -0.5F);
                    livingEntity.removeEffect(ModEffect.RADIATION);
                    LivingProperties.getCont(livingEntity).clear();
                }
            }

            RandomSource rand = level.random;

            CompoundTag tag = new CompoundTag();
            tag.putString("type", "vanillaExt");
            tag.putString("mode", "townaura");
            tag.putDouble("posX", pos.getX() + 0.125 + rand.nextDouble() * 0.75);
            tag.putDouble("posY", pos.getY() + 1.1);
            tag.putDouble("posZ", pos.getZ() + 0.125 + rand.nextDouble() * 0.75);
            tag.putDouble("mX", 0.0);
            tag.putDouble("mY", 0.04);
            tag.putDouble("mZ", 0.0);
            HBMsNTMClient.effectNT(tag);
        }
    }
}
