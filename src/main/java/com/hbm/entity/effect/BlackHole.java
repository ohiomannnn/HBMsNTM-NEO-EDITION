package com.hbm.entity.effect;

import com.hbm.entity.ModEntityTypes;
import com.hbm.entity.projectile.Rubble;
import com.hbm.items.ModItems;
import com.hbm.lib.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BlackHole extends Entity {

    private boolean breaksBlocks = true;

    protected static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(BlackHole.class, EntityDataSerializers.FLOAT);

    public BlackHole(EntityType<? extends BlackHole> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.noCulling = true;
    }

    public BlackHole setSize(float size) {
        this.entityData.set(SIZE, size);
        return this;
    }

    public BlackHole noBreak() {
        this.breaksBlocks = false;
        return this;
    }

    @Override
    public void tick() {
        super.tick();

        float size = this.entityData.get(SIZE);
        Level level = this.level();

        if (breaksBlocks && !level.isClientSide) {
            for (int k = 0; k < size * 2; k++) {
                double phi = random.nextDouble() * (Math.PI * 2);
                double costheta = random.nextDouble() * 2 - 1;
                double theta = Math.acos(costheta);
                double x = Math.sin(theta) * Math.cos(phi);
                double y = Math.sin(theta) * Math.sin(phi);
                double z = Math.cos(theta);

                Vec3 vec = new Vec3(x, y, z);

                int length = (int) Math.ceil(size * 15);

                for (int i = 0; i < length; i++) {
                    int x0 = (int) (this.getX() + (vec.x * i));
                    int y0 = (int) (this.getY() + (vec.y * i));
                    int z0 = (int) (this.getZ() + (vec.z * i));
                    BlockPos toChange = new BlockPos(x0, y0, z0);
                    if (!level.getBlockState(toChange).isAir()) {
                        Rubble rubble = new Rubble(ModEntityTypes.RUBBLE.get(), level);
                        rubble.setPos(x0 + 0.5F, y0, z0 + 0.5F);
                        rubble.setBlock(level.getBlockState(toChange).getBlock());
                        level.addFreshEntity(rubble);
                        level.setBlock(toChange, Blocks.AIR.defaultBlockState(), 3);
                        break;
                    }
                }
            }
        }

        double range = size * 15;
        List<Entity> entities = level.getEntities(null, new AABB(getX() - range, getY() - range, getZ() - range, getX() + range, getY() + range, getZ() + range));

        for (Entity entity : entities) {
            if (entity instanceof Player player) {
                if (player.isCreative() || player.isSpectator()) continue;
            }

            Vec3 toEntity = new Vec3(getX() - entity.getX(), getY() - entity.getY(), getZ() - entity.getZ());
            double dist = toEntity.length();
            if (dist > range) continue;

            toEntity = toEntity.normalize();
            if (!(entity instanceof ItemEntity)) {
                toEntity = toEntity.yRot((float) Math.toRadians(15));
            }

            double speed = 0.1D;
            entity.setDeltaMovement(entity.getDeltaMovement().add(
                    toEntity.x * speed,
                    toEntity.y * speed * 2,
                    toEntity.z * speed
            ));

            if (entity instanceof BlackHole) continue;

            if (dist < size * 1.5) {
                entity.hurt(level.damageSources().source(ModDamageTypes.BLACK_HOLE), Float.MAX_VALUE);

                if (!(entity instanceof LivingEntity)) {
                    entity.discard();
                }

                if (entity instanceof ItemEntity itemEntity && !level.isClientSide) {
                    ItemStack stack = itemEntity.getItem();
                    if (stack.getItem() == ModItems.PELLET_ANTIMATTER.get()) {
                        this.discard();
                        level.explode(null, this.getX(), this.getY(), this.getZ(), 5F, Level.ExplosionInteraction.BLOCK);
                        return;
                    }
                }
            }
        }

        this.setPos(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.99D, 0.99D, 0.99D));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(SIZE, tag.getFloat("Size"));
        this.breaksBlocks = tag.getBoolean("BreaksBlocks");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Size", this.getSize());
        tag.putBoolean("BreaksBlocks", this.breaksBlocks);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SIZE, 0.5F);
    }
    public float getSize() { return this.entityData.get(SIZE); }
}
