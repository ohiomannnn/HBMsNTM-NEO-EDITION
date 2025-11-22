package com.hbm.explosion;

import com.hbm.entity.ModEntities;
import com.hbm.entity.projectile.Rubble;
import com.hbm.entity.projectile.Shrapnel;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.util.ParticleUtil;
import com.hbm.util.Vec3NT;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ExplosionLarge {

    private static final Random rand = new Random();

    @Deprecated
    public static void spawnParticles(ServerLevel serverLevel, double x, double y, double z, int count) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "smoke");
        tag.putString("mode", "cloud");
        tag.putInt("count", count);
        PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, 250, new AuxParticle(tag, x, y, z));
    }

    public static void spawnParticlesRadial(ServerLevel serverLevel, double x, double y, double z, int count) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "smoke");
        tag.putString("mode", "radial");
        tag.putInt("count", count);
        PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, 250, new AuxParticle(tag, x, y, z));
    }

    public static void spawnFoam(ServerLevel serverLevel, double x, double y, double z, int count) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "smoke");
        tag.putString("mode", "foamSplash");
        tag.putInt("count", count);
        PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, 250, new AuxParticle(tag, x, y, z));
    }

    public static void spawnShock(ServerLevel serverLevel, double x, double y, double z, int count, double strength) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "smoke");
        tag.putString("mode", "shock");
        tag.putInt("count", count);
        tag.putDouble("strength", strength);
        PacketDistributor.sendToPlayersNear(serverLevel, null, x, y, z, 250, new AuxParticle(tag, x, y, z));
    }

    public static void spawnBurst(ServerLevel serverLevel, double x, double y, double z, int count, double strength) {
        Vec3NT vec = new Vec3NT(strength, 0, 0);
        vec.rotateAroundXRad(rand.nextInt(360));
        for (int i = 0; i < count; i++) {
            ParticleUtil.spawnGasFlame(serverLevel, x, y, z, vec.xCoord, 0.0, vec.zCoord);
            vec.rotateAroundXRad(360 / count);
        }
    }

    public static void spawnRubble(ServerLevel serverLevel, double x, double y, double z, int count) {
        for (int i = 0; i < count; i++) {
            Rubble rubble = new Rubble(ModEntities.RUBBLE.get(), serverLevel);
            rubble.setPos(x, y, z);
            rubble.setDeltaMovement(
                    rand.nextGaussian() * 0.75 * (1 + (count / 50.0)),
                    0.75 * (1 + ((count + rand.nextInt(count * 5))) / 25.0),
                    rand.nextGaussian() * 0.75 * (1 + (count / 50.0))
            );
            rubble.setBlock(Blocks.STONE);
            serverLevel.addFreshEntity(rubble);
        }
    }

    public static void spawnShrapnels(ServerLevel serverLevel, double x, double y, double z, int count) {
        for (int i = 0; i < count; i++) {
            Shrapnel shrapnel = new Shrapnel(ModEntities.SHRAPNEL.get(), serverLevel);
            shrapnel.setPos(x, y, z);
            shrapnel.setDeltaMovement(
                    rand.nextGaussian() * (1 + count / 50.0),
                    ((rand.nextFloat() * 0.5) + 0.5) * (1 + ((double) count / (15 + rand.nextInt(21)))) + (rand.nextFloat() / 50 * count),
                    rand.nextGaussian() * (1 + count / 50.0)
            );
            shrapnel.setTrail(rand.nextInt(3) == 0);
            serverLevel.addFreshEntity(shrapnel);
        }
    }

    public static void spawnTracers(ServerLevel serverLevel, double x, double y, double z, int count) {
        for (int i = 0; i < count; i++) {
            Shrapnel shrapnel = new Shrapnel(ModEntities.SHRAPNEL.get(), serverLevel);
            shrapnel.setPos(x, y, z);
            shrapnel.setDeltaMovement(
                    rand.nextGaussian() * (1 + count / 50.0) * 0.25,
                    (((rand.nextFloat() * 0.5) + 0.5) * (1 + ((double) count / (15 + rand.nextInt(21))))) + (rand.nextFloat() / 50 * count) * 0.25,
                    rand.nextGaussian() * (1 + count / 50.0) * 0.25
            );
            shrapnel.setTrail(true);
            serverLevel.addFreshEntity(shrapnel);
        }
    }

    public static void spawnShrapnelShower(ServerLevel serverLevel, double x, double y, double z, double motionX, double motionY, double motionZ, int count, double deviation) {
        for (int i = 0; i < count; i++) {
            Shrapnel shrapnel = new Shrapnel(ModEntities.SHRAPNEL.get(), serverLevel);
            shrapnel.setPos(x, y, z);
            shrapnel.setDeltaMovement(
                    motionX + rand.nextGaussian() * deviation,
                    motionY + rand.nextGaussian() * deviation,
                    motionZ + rand.nextGaussian() * deviation
            );
            shrapnel.setTrail(rand.nextInt(3) == 0);
            serverLevel.addFreshEntity(shrapnel);
        }
    }

    public static void spawnMissileDebris(ServerLevel serverLevel, double x, double y, double z, double motionX, double motionY, double motionZ, double deviation, @Nullable List<ItemStack> debris, @Nullable ItemStack rareDrop) {
        if (debris != null) {
            for (ItemStack stack : debris) {
                if (!stack.isEmpty()) {
                    int k = rand.nextInt(stack.getCount() + 1);
                    for (int j = 0; j < k; j++) {
                        ItemEntity item = new ItemEntity(serverLevel, x, y, z, stack);
                        item.setDeltaMovement(
                                (motionX + rand.nextGaussian() * deviation) * 0.85,
                                (motionY + rand.nextGaussian() * deviation) * 0.85,
                                (motionZ + rand.nextGaussian() * deviation) * 0.85
                        );
                        item.moveTo(item.getX() + item.getDeltaMovement().x * 2,
                                item.getY() + item.getDeltaMovement().y * 2,
                                item.getZ() + item.getDeltaMovement().z * 2);
                        serverLevel.addFreshEntity(item);
                    }
                }
            }
        }

        if (rareDrop != null && rand.nextInt(10) == 0) {
            ItemEntity item = new ItemEntity(serverLevel, x, y, z, rareDrop);
            item.setDeltaMovement(
                    motionX + rand.nextGaussian() * deviation * 0.1,
                    motionY + rand.nextGaussian() * deviation * 0.1,
                    motionZ + rand.nextGaussian() * deviation * 0.1
            );
            serverLevel.addFreshEntity(item);
        }
    }

    @Deprecated
    public static void explode(ServerLevel serverLevel, double x, double y, double z, int strength, boolean cloud, boolean rubble, boolean shrapnel, @Nullable Entity exploder) {
        serverLevel.explode(exploder, x, y, z, strength, Level.ExplosionInteraction.BLOCK);
        if (cloud) spawnParticles(serverLevel, x, y, z, cloudFunction(strength));
        if (rubble) spawnRubble(serverLevel, x, y, z, rubbleFunction(strength));
        if (shrapnel) spawnShrapnels(serverLevel, x, y, z, shrapnelFunction(strength));
    }

    @Deprecated
    public static void explode(ServerLevel serverLevel, double x, double y, double z, int strength, boolean cloud, boolean rubble, boolean shrapnel) {
        explode(serverLevel, x, y, z, strength, cloud, rubble, shrapnel, null);
    }

    @Deprecated
    public static void explodeFire(ServerLevel serverLevel, double x, double y, double z, int strength, boolean cloud, boolean rubble, boolean shrapnel) {
        serverLevel.explode(null, x, y, z, strength, true, Level.ExplosionInteraction.BLOCK);
        if (cloud) spawnParticles(serverLevel, x, y, z, cloudFunction(strength));
        if (rubble) spawnRubble(serverLevel, x, y, z, rubbleFunction(strength));
        if (shrapnel) spawnShrapnels(serverLevel, x, y, z, shrapnelFunction(strength));
    }

    public static void buster(ServerLevel serverLevel, double x, double y, double z, Vec3 vector, float strength, float depth) {
        vector = vector.normalize();
        for (int i = 0; i < depth; i += 2) {
            serverLevel.explode(null, x + vector.x * i, y + vector.y * i, z + vector.z * i, strength, Level.ExplosionInteraction.BLOCK);
        }
    }

    public static void jolt(ServerLevel serverLevel, double posX, double posY, double posZ, double strength, int count, double vel) {
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

                if (serverLevel.getBlockState(bpos).getFluidState().isEmpty()) {
                    serverLevel.setBlock(bpos, Blocks.AIR.defaultBlockState(), 3);
                }

                if (!serverLevel.isEmptyBlock(bpos)) {
                    float resistance = serverLevel.getBlockState(bpos).getBlock().getExplosionResistance(serverLevel.getBlockState(bpos), serverLevel, bpos, null);
                    if (resistance > 70) continue;

                    Rubble rubble = new Rubble(ModEntities.RUBBLE.get(), serverLevel);
                    rubble.setPos(bx + 0.5, by + 0.5, bz + 0.5);
                    rubble.setBlock(serverLevel.getBlockState(bpos).getBlock());

                    Vec3 dir = new Vec3(posX - rubble.getX(), posY - rubble.getY(), posZ - rubble.getZ()).normalize();
                    rubble.setDeltaMovement(dir.x * vel, dir.y * vel, dir.z * vel);

                    serverLevel.addFreshEntity(rubble);
                    serverLevel.setBlock(bpos, Blocks.AIR.defaultBlockState(), 3);
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
