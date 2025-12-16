package com.hbm.particle.helper;

import com.hbm.HBMsNTMClient;
import com.hbm.particle.SkeletonParticle;
import com.hbm.util.Vec3NT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.function.Function;

public class SkeletonCreator implements IParticleCreator {

    public static HashMap<String, Function<LivingEntity, BoneDefinition[]>> skullanizer = new HashMap<>();

    public static void composeEffect(Level level, Entity toSkeletonize, float brightness) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "skeleton");
        tag.putInt("entityID", toSkeletonize.getId());
        tag.putFloat("brightness", brightness);
        if (level instanceof ServerLevel serverLevel) {
            IParticleCreator.sendPacket(serverLevel, toSkeletonize.getX(), toSkeletonize.getY(), toSkeletonize.getZ(), 100, tag);
        }
    }

    public static void composeEffectGib(Level level, Entity toSkeletonize, float force) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "skeleton");
        tag.putInt("entityID", toSkeletonize.getId());
        tag.putFloat("brightness", 1F);
        tag.putFloat("force", force);
        tag.putBoolean("gib", true);
        if (level instanceof ServerLevel serverLevel) {
            IParticleCreator.sendPacket(serverLevel, toSkeletonize.getX(), toSkeletonize.getY(), toSkeletonize.getZ(), 100, tag);
        }
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void makeParticle(ClientLevel level, Player player, RandomSource rand, double x, double y, double z, CompoundTag tag) {

        if (skullanizer.isEmpty()) init();

        boolean gib = tag.getBoolean("gib");
        float force = tag.getFloat("force");
        float brightness = tag.getFloat("brightness");
        int entityID = tag.getInt("entityID");
        Entity entity = level.getEntity(entityID);
        if (!(entity instanceof LivingEntity living)) return;
        boolean isSkeleton = entity instanceof Skeleton;

        HBMsNTMClient.vanish(entityID);

        Function<LivingEntity, BoneDefinition[]> bonealizer = skullanizer.get(entity.getClass().getSimpleName());

        if (bonealizer != null) {
            BoneDefinition[] bones = bonealizer.apply(living);
            for (BoneDefinition bone : bones) {
                if (gib && rand.nextBoolean() && !isSkeleton) continue;
                SkeletonParticle skeleton = new SkeletonParticle(level, bone.x, bone.y, bone.z, brightness, brightness, brightness, bone.type);
                skeleton.prevRotationYaw = skeleton.rotationYaw = bone.yRot;
                skeleton.prevRotationPitch = skeleton.rotationPitch = bone.xRot;
                if (gib) {
                    skeleton.makeGib();
                    if (isSkeleton) {
                        skeleton.useTexture = SkeletonParticle.TEXTURE;
                        skeleton.useTextureExt = SkeletonParticle.TEXTURE_EXT;
                    }
                    skeleton.setParticleSpeed(rand.nextGaussian() * force, (rand.nextGaussian() + 1) * force, rand.nextGaussian() * force);
                }
                Minecraft.getInstance().particleEngine.add(skeleton);
            }
        }
    }

    public static class BoneDefinition {
        public EnumSkeletonType type;
        public float yRot;
        public float xRot;
        public double x;
        public double y;
        public double z;

        public BoneDefinition(EnumSkeletonType type, float yRot, float xRot, double x, double y, double z) {
            this.type = type;
            this.yRot = yRot;
            this.xRot = xRot;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public enum EnumSkeletonType {
        SKULL,
        TORSO,
        LIMB,
        SKULL_VILLAGER
    }

    public static Function<LivingEntity, BoneDefinition[]> BONES_BIPED = entity -> {
        Vec3NT leftArm = new Vec3NT(0.3125, 0, 0).rotateAroundYDeg(-entity.yBodyRot);
        Vec3NT leftLeg = new Vec3NT(0.125, 0, 0).rotateAroundYDeg(-entity.yBodyRot);

        // so i dont call getter every fucking time
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        return new BoneDefinition[] {
                new BoneDefinition(EnumSkeletonType.SKULL, -entity.getYHeadRot(), entity.getXRot(), x, y + 1.5, z),
                new BoneDefinition(EnumSkeletonType.TORSO, -entity.yBodyRot, 0, x, y + 1.5, z),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, x + leftArm.xCoord, y + 1.375, z + leftArm.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, x - leftArm.xCoord, y + 1.375, z - leftArm.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, x + leftLeg.xCoord, y + 0.75, z + leftLeg.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, x - leftLeg.xCoord, y + 0.75, z - leftLeg.zCoord),
        };
    };

    public static Function<LivingEntity, BoneDefinition[]> BONES_ZOMBIE = entity -> {
        Vec3NT leftArm = new Vec3NT(0.3125, 0, 0).rotateAroundYDeg(-entity.yBodyRot);
        Vec3NT forward = new Vec3NT(0, 0, 0.1).rotateAroundYDeg(-entity.yBodyRot);
        Vec3NT leftLeg = new Vec3NT(0.125, 0, 0).rotateAroundYDeg(-entity.yBodyRot);

        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        return new BoneDefinition[] {
                new BoneDefinition(EnumSkeletonType.SKULL, -entity.getYHeadRot(), entity.getXRot(), x, y + 1.5, z),
                new BoneDefinition(EnumSkeletonType.TORSO, -entity.yBodyRot, 0, x, y + 1.5, z),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, -90, x + leftArm.xCoord + forward.xCoord, y + 1.375, z + leftArm.zCoord + forward.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, -90, x - leftArm.xCoord + forward.xCoord, y + 1.375, z - leftArm.zCoord + forward.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, x + leftLeg.xCoord, y + 0.75, z + leftLeg.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, x - leftLeg.xCoord, y + 0.75, z - leftLeg.zCoord),
        };
    };

    public static final Function<LivingEntity, BoneDefinition[]> BONES_VILLAGER = entity -> {
        Vec3NT leftArm = new Vec3NT(0.375, 0, 0).rotateAroundYDeg(-entity.yBodyRot);
        Vec3NT forward = new Vec3NT(0, 0, 0).rotateAroundYDeg(-entity.yBodyRot);
        Vec3NT leftLeg = new Vec3NT(0.125, 0, 0).rotateAroundYDeg(-entity.yBodyRot);

        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        return new BoneDefinition[] {
                new BoneDefinition(EnumSkeletonType.SKULL_VILLAGER, -entity.getYHeadRot(), entity.getXRot(), x, y + 1.5, z),
                new BoneDefinition(EnumSkeletonType.TORSO, -entity.yBodyRot, 0, x, y + 1.5, z),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, -45, x + leftArm.xCoord + forward.xCoord, y + 1.375, z + leftArm.zCoord + forward.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, -45, x - leftArm.xCoord + forward.xCoord, y + 1.375, z - leftArm.zCoord + forward.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, x + leftLeg.xCoord, y + 0.75, z + leftLeg.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, x - leftLeg.xCoord, y + 0.75, z - leftLeg.zCoord)
        };
    };
    public static void init() {
        skullanizer.put(RemotePlayer.class.getSimpleName(), BONES_BIPED);
        skullanizer.put(LocalPlayer.class.getSimpleName(), BONES_BIPED);

        skullanizer.put(Zombie.class.getSimpleName(), BONES_ZOMBIE);
        skullanizer.put(Skeleton.class.getSimpleName(), BONES_BIPED);

        skullanizer.put(Villager.class.getSimpleName(), BONES_VILLAGER);
        skullanizer.put(ZombieVillager.class.getSimpleName(), BONES_VILLAGER);
        skullanizer.put(Pillager.class.getSimpleName(), BONES_VILLAGER);
        skullanizer.put(Illusioner.class.getSimpleName(), BONES_VILLAGER);
        skullanizer.put(Vindicator.class.getSimpleName(), BONES_VILLAGER);
        skullanizer.put(Witch.class.getSimpleName(), BONES_VILLAGER);
    }
}
