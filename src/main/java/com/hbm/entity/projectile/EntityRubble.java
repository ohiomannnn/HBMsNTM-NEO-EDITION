package com.hbm.entity.projectile;

import com.hbm.network.toclient.ParticleBurstPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.registries.BuiltInRegistries;

import static com.hbm.lib.ModDamageSource.RUBBLE;

public class EntityRubble extends ThrowableProjectile {

    private static final EntityDataAccessor<String> BLOCK_ID =
            SynchedEntityData.defineId(EntityRubble.class, EntityDataSerializers.STRING);

    public EntityRubble(EntityType<? extends EntityRubble> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BLOCK_ID, "minecraft:stone");
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (result instanceof EntityHitResult ehr) {
            DamageSource src = new DamageSource( ehr.getEntity().level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(RUBBLE) );
            ehr.getEntity().hurt(src, 15);
        }

        if (this.tickCount > 2) {
            this.discard();

            level().playSound(null, getX(), getY(), getZ(),
                    SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1.5F, 1.0F);

            BlockPos pos = new BlockPos((int) getX(), (int) getY(), (int) getZ());

            if (!level().isClientSide && level() instanceof ServerLevel) {
                ResourceLocation blockId = ResourceLocation.withDefaultNamespace(entityData.get(BLOCK_ID));

                new ParticleBurstPacket(pos, blockId);
            }
        }
    }

    public void setMetaBasedOnBlock(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        entityData.set(BLOCK_ID, id.toString());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putString("block", entityData.get(BLOCK_ID));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        entityData.set(BLOCK_ID, tag.getString("block"));
    }
}
