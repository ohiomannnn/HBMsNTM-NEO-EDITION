package com.hbm.entity.projectile;

import com.hbm.HBMsNTM;
import com.hbm.lib.ModDamageSource;
import com.hbm.lib.ModSounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EntityRubble extends ThrowableProjectile {

    private static final EntityDataAccessor<String> BLOCK_ID = SynchedEntityData.defineId(EntityRubble.class, EntityDataSerializers.STRING);

    public EntityRubble(EntityType<? extends EntityRubble> type, Level level) {
        super(type, level);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        HBMsNTM.LOGGER.info("{}", BuiltInRegistries.BLOCK.get(ResourceLocation.parse(entityData.get(BLOCK_ID))));
        HBMsNTM.LOGGER.info("{}", BuiltInRegistries.BLOCK.getKey(Blocks.STONE));

        if (result instanceof EntityHitResult entityHitResult) {
            entityHitResult.getEntity().hurt(new DamageSource(entityHitResult.getEntity().level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageSource.RUBBLE)), 15);
        }

        if (this.tickCount > 2) {
            this.discard();

            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.DEBRIS, SoundSource.BLOCKS, 1.5F, 1.0F);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BLOCK_ID, "minecraft:stone");
    }

    public Block getBlock() { return BuiltInRegistries.BLOCK.get(ResourceLocation.parse(entityData.get(BLOCK_ID))); }
    public void setBlock(Block block) { entityData.set(BLOCK_ID, BuiltInRegistries.BLOCK.getKey(block).toString()); }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putString("Block", entityData.get(BLOCK_ID));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        entityData.set(BLOCK_ID, tag.getString("Block"));
    }
}
