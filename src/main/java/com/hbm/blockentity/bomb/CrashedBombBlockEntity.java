package com.hbm.blockentity.bomb;

import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blocks.bomb.CrashedBombBlock.DudType;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.BiConsumer;

public class CrashedBombBlockEntity extends BlockEntity {

    public DudType type;

    public CrashedBombBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.CRASHED_BOMB.get(), pos, state);
    }

    public void updateEntity() {
        if (level != null && !level.isClientSide) {
            BlockPos pos = this.getBlockPos();
            if (level.getGameTime() % 2 == 0) {
                if (type == DudType.BALEFIRE)	 affectEntities(level, pos, (entity, intensity) -> ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, 1F * intensity), 15D);
                if (type == DudType.NUKE)		 affectEntities(level, pos, (entity, intensity) -> ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, 0.25F * intensity), 10D);
                if (type == DudType.SALTED)		 affectEntities(level, pos, (entity, intensity) -> ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, 0.5F * intensity), 10D);
            }
        }
    }

    private void affectEntities(Level level, BlockPos pos, BiConsumer<LivingEntity, Float> effect, double range) {
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(range));

        for (LivingEntity entity : entities) {
            double dx = entity.getX() - (pos.getX() + 0.5);
            double dy = (entity.getY() + entity.getBbHeight() / 2) - (pos.getY() + 0.5);
            double dz = entity.getZ() - (pos.getZ() + 0.5);
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dist > range) continue;
            float intensity = (float) (1.0D - dist / range);
            effect.accept(entity, intensity);
        }
    }
}
