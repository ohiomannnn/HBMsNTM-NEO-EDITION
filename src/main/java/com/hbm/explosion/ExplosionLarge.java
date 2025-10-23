package com.hbm.explosion;

import com.hbm.entity.ModEntities;
import com.hbm.entity.projectile.EntityRubble;
import com.hbm.entity.projectile.EntityShrapnel;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.particle.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Random;

public class ExplosionLarge {

    private static final Random rand = new Random();

    @Deprecated
    public static void spawnParticles(Level level, double x, double y, double z, int count) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "smoke");
        tag.putString("mode", "cloud");
        tag.putInt("count", count);
        PacketDistributor.sendToPlayersNear((ServerLevel) level, null, x, y, z, 250, new AuxParticle(tag, x, y, z));
    }

    public static void spawnParticlesRadial(Level level, double x, double y, double z, int count) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "smoke");
        tag.putString("mode", "radial");
        tag.putInt("count", count);
        PacketDistributor.sendToPlayersNear((ServerLevel) level, null, x, y, z, 250, new AuxParticle(tag, x, y, z));
    }

    public static void spawnFoam(Level level, double x, double y, double z, int count) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "smoke");
        tag.putString("mode", "foamSplash");
        tag.putInt("count", count);
        PacketDistributor.sendToPlayersNear((ServerLevel) level, null, x, y, z, 250, new AuxParticle(tag, x, y, z));
    }

    public static void spawnShock(Level level, double x, double y, double z, int count, double strength) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "smoke");
        tag.putString("mode", "shock");
        tag.putInt("count", count);
        tag.putDouble("strength", strength);
        PacketDistributor.sendToPlayersNear((ServerLevel) level, null, x, y, z, 250, new AuxParticle(tag, x, y, z));
    }

    public static void spawnBurst(Level level, double x, double y, double z, int count, double strength) {
        Vec3 vec = new Vec3(strength, 0, 0).yRot((float) Math.toRadians(rand.nextInt(360)));
        for (int i = 0; i < count; i++) {
            level.addParticle(ModParticles.GAS_FLAME.get(), x, y, z, vec.x, 0.0, vec.z);
            vec = vec.yRot((float) Math.toRadians(360.0 / count));
        }
    }

    public static void spawnRubble(Level level, double x, double y, double z, int count) {
        if (level.isClientSide) return;
        for (int i = 0; i < count; i++) {
            EntityRubble rubble = ModEntities.RUBBLE.get().create(level);
            rubble.setPos(x, y, z);
            rubble.setDeltaMovement(
                    rand.nextGaussian() * 0.75 * (1 + (count / 50.0)),
                    0.75 * (1 + ((count + rand.nextInt(count * 5))) / 25.0),
                    rand.nextGaussian() * 0.75 * (1 + (count / 50.0))
            );
            rubble.setMetaBasedOnBlock(Blocks.STONE);
            level.addFreshEntity(rubble);
        }
    }

    public static void spawnShrapnels(Level level, double x, double y, double z, int count) {
        if (level.isClientSide) return;
        for (int i = 0; i < count; i++) {
            EntityShrapnel shrapnel = ModEntities.SHRAPNEL.get().create(level);
            shrapnel.setPos(x, y, z);
            shrapnel.setDeltaMovement(
                    rand.nextGaussian() * (1 + count / 50.0),
                    ((rand.nextFloat() * 0.5) + 0.5) * (1 + ((double) count / (15 + rand.nextInt(21)))) + (rand.nextFloat() / 50 * count),
                    rand.nextGaussian() * (1 + count / 50.0)
            );
            shrapnel.setTrail(rand.nextInt(3) == 0);
            level.addFreshEntity(shrapnel);
        }
    }

    public static void spawnTracers(Level level, double x, double y, double z, int count) {
        if (level.isClientSide) return;
        for (int i = 0; i < count; i++) {
            EntityShrapnel shrapnel = ModEntities.SHRAPNEL.get().create(level);
            shrapnel.setPos(x, y, z);
            shrapnel.setDeltaMovement(
                    rand.nextGaussian() * (1 + count / 50.0) * 0.25,
                    (((rand.nextFloat() * 0.5) + 0.5) * (1 + ((double) count / (15 + rand.nextInt(21))))) + (rand.nextFloat() / 50 * count) * 0.25,
                    rand.nextGaussian() * (1 + count / 50.0) * 0.25
            );
            shrapnel.setTrail(true);
            level.addFreshEntity(shrapnel);
        }
    }

    public static void spawnShrapnelShower(Level level, double x, double y, double z,
                                           double motionX, double motionY, double motionZ,
                                           int count, double deviation) {
        if (level.isClientSide) return;
        for (int i = 0; i < count; i++) {
            EntityShrapnel shrapnel = ModEntities.SHRAPNEL.get().create(level);
            shrapnel.setPos(x, y, z);
            shrapnel.setDeltaMovement(
                    motionX + rand.nextGaussian() * deviation,
                    motionY + rand.nextGaussian() * deviation,
                    motionZ + rand.nextGaussian() * deviation
            );
            shrapnel.setTrail(rand.nextInt(3) == 0);
            level.addFreshEntity(shrapnel);
        }
    }

    public static void spawnMissileDebris(Level level, double x, double y, double z,
                                          double motionX, double motionY, double motionZ,
                                          double deviation, List<ItemStack> debris, ItemStack rareDrop) {
        if (level.isClientSide) return;

        if (debris != null) {
            for (ItemStack stack : debris) {
                if (!stack.isEmpty()) {
                    int k = rand.nextInt(stack.getCount() + 1);
                    for (int j = 0; j < k; j++) {
                        ItemEntity item = new ItemEntity(level, x, y, z, stack.copy());
                        item.setDeltaMovement(
                                (motionX + rand.nextGaussian() * deviation) * 0.85,
                                (motionY + rand.nextGaussian() * deviation) * 0.85,
                                (motionZ + rand.nextGaussian() * deviation) * 0.85
                        );
                        item.moveTo(item.getX() + item.getDeltaMovement().x * 2,
                                item.getY() + item.getDeltaMovement().y * 2,
                                item.getZ() + item.getDeltaMovement().z * 2);
                        level.addFreshEntity(item);
                    }
                }
            }
        }

        if (rareDrop != null && rand.nextInt(10) == 0) {
            ItemEntity item = new ItemEntity(level, x, y, z, rareDrop.copy());
            item.setDeltaMovement(
                    motionX + rand.nextGaussian() * deviation * 0.1,
                    motionY + rand.nextGaussian() * deviation * 0.1,
                    motionZ + rand.nextGaussian() * deviation * 0.1
            );
            level.addFreshEntity(item);
        }
    }

    @Deprecated
    public static void explode(Level level, double x, double y, double z,
                               float strength, boolean cloud, boolean rubble, boolean shrapnel, Entity exploder) {
        level.explode(exploder, x, y, z, strength, Level.ExplosionInteraction.BLOCK);
        if (cloud) spawnParticles(level, x, y, z, cloudFunction((int) strength));
        if (rubble) spawnRubble(level, x, y, z, rubbleFunction((int) strength));
        if (shrapnel) spawnShrapnels(level, x, y, z, shrapnelFunction((int) strength));
    }

    @Deprecated
    public static void explode(Level level, double x, double y, double z,
                               float strength, boolean cloud, boolean rubble, boolean shrapnel) {
        explode(level, x, y, z, strength, cloud, rubble, shrapnel, null);
    }

    @Deprecated
    public static void explodeFire(Level level, double x, double y, double z,
                                   float strength, boolean cloud, boolean rubble, boolean shrapnel) {
        level.explode(null, x, y, z, strength, Level.ExplosionInteraction.TNT);
        if (cloud) spawnParticles(level, x, y, z, cloudFunction((int) strength));
        if (rubble) spawnRubble(level, x, y, z, rubbleFunction((int) strength));
        if (shrapnel) spawnShrapnels(level, x, y, z, shrapnelFunction((int) strength));
    }

    public static void buster(Level level, double x, double y, double z, Vec3 vector, float strength, float depth) {
        vector = vector.normalize();
        for (int i = 0; i < depth; i += 2) {
            level.explode(null,
                    x + vector.x * i,
                    y + vector.y * i,
                    z + vector.z * i,
                    strength,
                    Level.ExplosionInteraction.BLOCK
            );
        }
    }

    public static void jolt(Level level, double posX, double posY, double posZ, double strength, int count, double vel) {
        if (level.isClientSide) return;

        for (int j = 0; j < count; j++) {
            double phi = rand.nextDouble() * (Math.PI * 2);
            double costheta = rand.nextDouble() * 2 - 1;
            double theta = Math.acos(costheta);
            double x = Math.sin(theta) * Math.cos(phi);
            double y = Math.sin(theta) * Math.sin(phi);
            double z = Math.cos(theta);

            Vec3 vec = new Vec3(x, y, z);

            for (int i = 0; i < strength; i++) {
                int bx = (int) (posX + vec.x * i);
                int by = (int) (posY + vec.y * i);
                int bz = (int) (posZ + vec.z * i);
                BlockPos bpos = new BlockPos(bx, by, bz);

                if (level.getBlockState(bpos).getFluidState().isEmpty()) {
                    level.setBlock(bpos, Blocks.AIR.defaultBlockState(), 3);
                }

                if (!level.isEmptyBlock(bpos)) {
                    float resistance = level.getBlockState(bpos).getBlock().getExplosionResistance(level.getBlockState(bpos), level, bpos, null);
                    if (resistance > 70) continue;

                    EntityRubble rubble = ModEntities.RUBBLE.get().create(level);
                    rubble.setPos(bx + 0.5, by + 0.5, bz + 0.5);
                    rubble.setMetaBasedOnBlock(level.getBlockState(bpos).getBlock());

                    Vec3 dir = new Vec3(posX - rubble.getX(), posY - rubble.getY(), posZ - rubble.getZ()).normalize();
                    rubble.setDeltaMovement(dir.x * vel, dir.y * vel, dir.z * vel);

                    level.addFreshEntity(rubble);
                    level.setBlock(bpos, Blocks.AIR.defaultBlockState(), 3);
                    break;
                }
            }
        }
    }

    public static int cloudFunction(int i) {
        return (int) (850 * (1 - Math.pow(Math.E, -i / 15.0)) + 15);
    }

    public static int rubbleFunction(int i) {
        return i / 10;
    }

    public static int shrapnelFunction(int i) {
        return i / 3;
    }
}
