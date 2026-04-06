package com.hbm.blockentity.machine;

import com.hbm.main.NuclearTechModClient;
import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blockentity.Tickable;
import com.hbm.extprop.HbmLivingAttachments;
import com.hbm.lib.ModEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class DecontaminatorBlockEntity extends BlockEntity implements Tickable {

    public DecontaminatorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.DECONTAMINATOR.get(), pos, blockState);
    }

    @Override
    public void updateEntity() {
        if (level == null) return;

        BlockPos pos = this.worldPosition;

        if (!level.isClientSide) {
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(pos.getX() - 0.5, pos.getY(), pos.getZ() - 0.5, pos.getX() + 1.5, pos.getY() + 2, pos.getZ() + 1.5));

            if (!entities.isEmpty()) {
                for (LivingEntity livingEntity : entities) {
                    HbmLivingAttachments.incrementRadiation(livingEntity, -0.5F);
                    livingEntity.removeEffect(ModEffect.RADIATION);
                    HbmLivingAttachments.getCont(livingEntity).clear();
                }
            }
        } else {
            RandomSource random = level.getRandom();

            CompoundTag tag = new CompoundTag();
            tag.putString("type", "vanillaExt");
            tag.putString("mode", "townaura");
            tag.putDouble("posX", pos.getX() + 0.125 + random.nextDouble() * 0.75);
            tag.putDouble("posY", pos.getY() + 1.1);
            tag.putDouble("posZ", pos.getZ() + 0.125 + random.nextDouble() * 0.75);
            tag.putDouble("mX", 0.0);
            tag.putDouble("mY", 0.04);
            tag.putDouble("mZ", 0.0);
            NuclearTechModClient.effectNT(tag);
        }
    }
}
