package com.hbm.entity.projectile;

import com.hbm.config.MainConfig;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.*;
import com.hbm.items.special.PolaroidItem;
import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.AuxParticle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.network.PacketDistributor;

public class BombletZeta extends ThrowableProjectile {

    public int type = 0;

    public BombletZeta(EntityType<BombletZeta> entityType, Level level) {
        super(entityType, level);
        this.rotation();
    }

    @Override
    public void tick() {
        this.xo = this.xOld = this.getX();
        this.yo = this.yOld = this.getY();
        this.zo = this.zOld = this.getZ();
        this.setPos(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);

        this.setDeltaMovement(this.getDeltaMovement().x * 0.99, this.getDeltaMovement().y - 0.05, this.getDeltaMovement().z * 0.99);

        this.rotation();

        if (!this.level().getBlockState(this.blockPosition()).isAir()) {
            if (!this.level().isClientSide) {
                if (this.type == 0) {
                    ExplosionVNT vnt = new ExplosionVNT(this.level(), this.getX() + 0.5F, this.getY() + 1.5F, this.getZ() + 0.5F, 4F);
                    vnt.setBlockAllocator(new BlockAllocatorStandard());
                    vnt.setBlockProcessor(new BlockProcessorStandard());
                    vnt.setEntityProcessor(new EntityProcessorCrossSmooth(1, 100));
                    vnt.setSFX(new ExplosionEffectWeapon(15, 3.5F, 1.25F));
                    vnt.explode();
                }
                if (this.type == 1) {
                    ExplosionVNT vnt = new ExplosionVNT(this.level(), this.getX() + 0.5F, this.getY() + 1.5F, this.getZ() + 0.5F, 4F);
                    vnt.setBlockAllocator(new BlockAllocatorStandard());
                    vnt.setBlockProcessor(new BlockProcessorStandard().withBlockEffect(new BlockMutatorFire()));
                    vnt.setEntityProcessor(new EntityProcessorCrossSmooth(1, 100));
                    vnt.setSFX(new ExplosionEffectWeapon(15, 5F, 1.75F));
                    vnt.explode();
                }
                if (this.type == 4) {
                    NukeExplosionMK5.statFac(this.level(), (int) (MainConfig.COMMON.FATMAN_RADIUS.get() * 1.5), this.getX(), this.getY(), this.getZ());

                    // this has to be the single worst solution ever
                    this.level().playSound(null, this.getX(), this.getY() + 0.5F, this.getZ(), ModSounds.MUKE_EXPLOSION.get(), SoundSource.BLOCKS, 15.0F, 1.0F);
                    CompoundTag tag = new CompoundTag();
                    tag.putString("type", "muke");
                    tag.putBoolean("balefire", PolaroidItem.polaroidID == 11 || random.nextInt(100) == 0);
                    if (this.level() instanceof ServerLevel serverLevel) {
                        PacketDistributor.sendToPlayersNear(serverLevel, null, this.getX(), this.getY() + 0.5F, this.getZ(), 250, new AuxParticle(tag, this.getX(), this.getY() + 0.5F, this.getZ()));
                    }
                }
                this.discard();
            }
        }
    }

    public void rotation() {
        float motionHorizontal = Mth.sqrt((float) (this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z));
        this.setYRot((float) (Math.atan2(this.getDeltaMovement().x, this.getDeltaMovement().z) * 180.0D / Math.PI));
        for (this.setXRot((float) (Math.atan2(this.getDeltaMovement().y, motionHorizontal) * 180.0D / Math.PI) - 90); this.getXRot() - this.xRotO < -180.0F; this.xRotO -= 360.0F);
        while (this.getXRot() - this.xRotO >= 180.0F) this.xRotO += 360.0F;
        while (this.getYRot() - this.yRotO < -180.0F) this.yRotO -= 360.0F;
        while (this.getYRot() - this.yRotO >= 180.0F) this.yRotO += 360.0F;
    }

    @Override protected void onHit(HitResult result) { }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) { }
}
