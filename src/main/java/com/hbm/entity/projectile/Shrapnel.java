package com.hbm.entity.projectile;

import com.hbm.blocks.ModBlocks;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockMutatorDebris;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.explosion.vanillant.standard.ExplosionEffectStandard;
import com.hbm.lib.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class Shrapnel extends ThrowableProjectile {

    private static final EntityDataAccessor<Byte> TYPE = SynchedEntityData.defineId(Shrapnel.class, EntityDataSerializers.BYTE);

    public Shrapnel(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(TYPE, (byte) 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level() instanceof ServerLevel serverLevel && this.entityData.get(TYPE) == 1) {
            serverLevel.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (result instanceof EntityHitResult ehr) {
            ehr.getEntity().hurt(this.level().damageSources().source(ModDamageTypes.SHRAPNEL), 15);
        }

        if (this.tickCount > 5) {
            if (!level().isClientSide) {
                this.discard();
            }

            int b = this.entityData.get(TYPE);
            if (b == 2 || b == 4) {
                if (!level().isClientSide) {
                    if (getDeltaMovement().y < -0.2D && result instanceof BlockHitResult bhr) {
                        BlockPos pos = bhr.getBlockPos().above();
                        if (level().getBlockState(pos).canBeReplaced()) {
                            level().setBlock(pos, (b == 2 ? ModBlocks.VOLCANIC_LAVA_BLOCK.get().defaultBlockState() : ModBlocks.RAD_LAVA_BLOCK.get().defaultBlockState()), 3);
                        }
                        BlockPos base = bhr.getBlockPos();
                        for (int x = -1; x <= 1; x++) {
                            for (int y = 0; y <= 2; y++) {
                                for (int z = -1; z <= 1; z++) {
                                    BlockPos p = base.offset(x, y, z);
                                    if (level().getBlockState(p).isAir()) {
                                        level().setBlock(p, ModBlocks.GAS_MONOXIDE.get().defaultBlockState(), 3);
                                    }
                                }
                            }
                        }
                    }

                    if (getDeltaMovement().y > 0 && result instanceof BlockHitResult bhr) {
                        BlockPos pos = bhr.getBlockPos();
                        ExplosionVNT vnt = new ExplosionVNT(level(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 7)
                                .setBlockAllocator(new BlockAllocatorStandard())
                                .setBlockProcessor(new BlockProcessorStandard().setNoDrop().withBlockEffect(new BlockMutatorDebris(b == 2 ? ModBlocks.VOLCANIC_LAVA_BLOCK.get() : ModBlocks.RAD_LAVA_BLOCK.get())))
                                .setSFX(new ExplosionEffectStandard());
                        vnt.explode();
                    }
                }
            } else if (b == 3 && result instanceof BlockHitResult bhr) {
                BlockPos pos = bhr.getBlockPos().above();
                if (level().getBlockState(pos).canBeReplaced()) {
                                                    //TODO: make mud_block
                    level().setBlock(pos, ModBlocks.BLOCK_SCRAP.get().defaultBlockState(), 3);
                }
            } else {
                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.LAVA, this.getX(), this.getY() + 0.5, this.getZ(), 5, 0.0, 0.0, 0.0, 0.0);
                }
            }

            level().playSound(null, getX(), getY(), getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    public byte getShrapnelType() {
       return this.entityData.get(TYPE);
    }

    public void setTrail(boolean b) {
        this.entityData.set(TYPE, (byte) (b ? 1 : 0));
    }

    public void setVolcano(boolean b) {
        this.entityData.set(TYPE, (byte) (b ? 2 : 0));
    }

    public void setWatz(boolean b) {
        this.entityData.set(TYPE, (byte) (b ? 3 : 0));
    }

    public void setRadVolcano(boolean b) {
        this.entityData.set(TYPE, (byte) (b ? 4 : 0));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putByte("Type", this.entityData.get(TYPE));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(TYPE, tag.getByte("Type"));
    }
}
