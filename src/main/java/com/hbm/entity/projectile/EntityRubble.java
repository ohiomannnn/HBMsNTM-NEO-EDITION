package com.hbm.entity.projectile;

import com.hbm.entity.ModEntities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import static com.hbm.lib.ModDamageSource.RUBBLE;

public class EntityRubble extends ThrowableProjectile {

    private static final EntityDataAccessor<Integer> BLOCK_ID =
            SynchedEntityData.defineId(EntityRubble.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BLOCK_META =
            SynchedEntityData.defineId(EntityRubble.class, EntityDataSerializers.INT);

    public EntityRubble(EntityType<? extends EntityRubble> type, Level level) {
        super(type, level);
    }

//    public EntityRubble(Level level, double x, double y, double z) {
//        super(ModEntities.RUBBLE.get(), x, y, z, level);
//    }
//
//    public EntityRubble(Level level, LivingEntity shooter) {
//        super(ModEntities.RUBBLE.get(), shooter, level);
//    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BLOCK_ID, 0);
        builder.define(BLOCK_META, 0);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (result instanceof EntityHitResult ehr) {
            DamageSource src = new DamageSource(
                    ehr.getEntity().level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(RUBBLE)
            );
            ehr.getEntity().hurt(src, 15);
        }

        if (this.tickCount > 2) {
            this.discard();

            level().playSound(null, getX(), getY(), getZ(),
                    SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1.5F, 1.0F);

//            if (!level().isClientSide) {
//                PacketDispatcher.sendToAllAround(
//                        new ParticleBurstPacket(
//                                (int) Math.floor(getX()),
//                                (int) getY(),
//                                (int) Math.floor(getZ()),
//                                entityData.get(BLOCK_ID),
//                                entityData.get(BLOCK_META)
//                        ),
//                        level().dimension(),
//                        getX(), getY(), getZ(),
//                        50
//                );
//            }
        }
    }

    public void setMetaBasedOnBlock(Block block, int meta) {
        entityData.set(BLOCK_ID, BuiltInRegistries.BLOCK.getId(block));
        entityData.set(BLOCK_META, meta);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("block", entityData.get(BLOCK_ID));
        tag.putInt("meta", entityData.get(BLOCK_META));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        entityData.set(BLOCK_ID, tag.getInt("block"));
        entityData.set(BLOCK_META, tag.getInt("meta"));
    }
}
