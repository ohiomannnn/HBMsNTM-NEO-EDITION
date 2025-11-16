package com.hbm.blockentity.bomb;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blocks.bomb.CrashedBombBlock;
import com.hbm.blocks.bomb.LandmineBlock;
import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.HazardType;
import com.hbm.util.ContaminationUtil.ContaminationType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.BiConsumer;

public class CrashedBombBlockEntity extends BlockEntity {

    public enum EnumDudType {
        BALEFIRE,
        CONVENTIONAL,
        NUKE,
        SALTED
    }

    protected final EnumDudType dudType;

    public CrashedBombBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, EnumDudType dudType) {
        super(type, pos, state);
        this.dudType = dudType;
    }

    public static CrashedBombBlockEntity balefire(BlockPos pos, BlockState state) {
        return new CrashedBombBlockEntity(ModBlockEntities.CRASHED_BOMB_BALEFIRE.get(), pos, state, EnumDudType.BALEFIRE);
    }

    public static CrashedBombBlockEntity conventional(BlockPos pos, BlockState state) {
        return new CrashedBombBlockEntity(ModBlockEntities.CRASHED_BOMB_CONVENTIONAL.get(), pos, state, EnumDudType.CONVENTIONAL);
    }

    public static CrashedBombBlockEntity nuke(BlockPos pos, BlockState state) {
        return new CrashedBombBlockEntity(ModBlockEntities.CRASHED_BOMB_NUKE.get(), pos, state, EnumDudType.NUKE);
    }

    public static CrashedBombBlockEntity salted(BlockPos pos, BlockState state) {
        return new CrashedBombBlockEntity(ModBlockEntities.CRASHED_BOMB_SALTED.get(), pos, state, EnumDudType.SALTED);
    }

    public EnumDudType getDudType() {
        return dudType;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CrashedBombBlockEntity be) {
        if (!(state.getBlock() instanceof CrashedBombBlock)) return;

        if (level.getGameTime() % 2 == 0) {

            EnumDudType type = be.dudType;

            if (type == EnumDudType.BALEFIRE)
                be.affectEntities(level, pos, (entity, intensity) ->
                        ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, 1F * intensity), 15D);

            else if (type == EnumDudType.NUKE)
                be.affectEntities(level, pos, (entity, intensity) ->
                        ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, 0.25F * intensity), 10D);

            else if (type == EnumDudType.SALTED)
                be.affectEntities(level, pos, (entity, intensity) ->
                        ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, 0.5F * intensity), 10D);
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
