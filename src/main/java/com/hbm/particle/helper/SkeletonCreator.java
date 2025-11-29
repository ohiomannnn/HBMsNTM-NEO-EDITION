package com.hbm.particle.helper;

import com.hbm.HBMsNTMClient;
import com.hbm.particle.SkeletonParticle;
import com.hbm.util.Vec3NT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zombie;
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
        int entityID = tag.getInt("entityID");
        Entity entity = level.getEntity(entityID);
        boolean skel = entity instanceof Skeleton;
        if (!(entity instanceof LivingEntity livingEntity)) return;

        HBMsNTMClient.vanish(entityID);

        float brightness = tag.getFloat("brightness");

        Function<LivingEntity, BoneDefinition[]> bonealizer = skullanizer.get(entity.getClass().getSimpleName());

        if(bonealizer != null) {
            BoneDefinition[] bones = bonealizer.apply(livingEntity);
            for (BoneDefinition bone : bones) {
                if (gib && rand.nextBoolean() && !skel) continue;
                SkeletonParticle skeleton = new SkeletonParticle(level, bone.x, bone.y, bone.z, brightness, brightness, brightness, bone.type);
                skeleton.prevRotationYaw = skeleton.rotationYaw = bone.yRot;
                skeleton.prevRotationPitch = skeleton.rotationPitch = bone.xRot;
                if (gib) {
                    skeleton.makeGib();
//                    if(skel) {
//                        skeleton.useTexture = skeleton.texture;
//                        skeleton.useTextureExt = skeleton.texture_ext;
//                    }

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
        SKULL, TORSO, LIMB, SKULL_VILLAGER
    }

    public static Function<LivingEntity, BoneDefinition[]> BONES_BIPED = (entity) -> {
        Vec3NT leftarm = new Vec3NT(0.375, 0, 0).rotateAroundYDeg(-entity.yBodyRot);
        Vec3NT leftleg = new Vec3NT(0.125, 0, 0).rotateAroundYDeg(-entity.yBodyRot);
        return new BoneDefinition[] {
                new BoneDefinition(EnumSkeletonType.SKULL, -entity.yHeadRot, entity.getXRot(), entity.getX(), entity.getY() + 1.75, entity.getZ()),
                new BoneDefinition(EnumSkeletonType.TORSO, -entity.yBodyRot, 0, entity.getX(), entity.getY() + 1.125, entity.getZ()),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, entity.getX() + leftarm.xCoord, entity.getY() + 1.125, entity.getZ() + leftarm.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, entity.getX() - leftarm.xCoord, entity.getY() + 1.125, entity.getZ() - leftarm.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, entity.getX() + leftleg.xCoord, entity.getY() + 0.375, entity.getZ() + leftleg.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, entity.getX() - leftleg.xCoord, entity.getY() + 0.375, entity.getZ() - leftleg.zCoord),
        };
    };

    public static Function<LivingEntity, BoneDefinition[]> BONES_ZOMBIE = (entity) -> {
        Vec3NT leftarm = new Vec3NT(0.375, 0, 0).rotateAroundYDeg(-entity.yBodyRot);
        Vec3NT forward = new Vec3NT(0, 0, 0.25).rotateAroundYDeg(-entity.yBodyRot);
        Vec3NT leftleg = new Vec3NT(0.125, 0, 0).rotateAroundYDeg(-entity.yBodyRot);
        return new BoneDefinition[] {
                new BoneDefinition(EnumSkeletonType.SKULL, -entity.yHeadRot, entity.getXRot(), entity.getX(), entity.getY() + 1.75, entity.getZ()),
                new BoneDefinition(EnumSkeletonType.TORSO, -entity.yBodyRot, 0, entity.getX(), entity.getY() + 1.125, entity.getZ()),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, -90, entity.getX() + leftarm.xCoord + forward.xCoord, entity.getY() + 1.375, entity.getZ() + leftarm.zCoord + forward.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, -90, entity.getX() - leftarm.xCoord + forward.xCoord, entity.getY() + 1.375, entity.getZ() - leftarm.zCoord + forward.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, entity.getX() + leftleg.xCoord, entity.getZ() + 0.375, entity.getZ() + leftleg.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, entity.getX() - leftleg.xCoord, entity.getZ() + 0.375, entity.getZ() - leftleg.zCoord),
        };
    };

    public static Function<LivingEntity, BoneDefinition[]> BONES_VILLAGER = (entity) -> {
        Vec3NT leftarm = new Vec3NT(0.375, 0, 0).rotateAroundYDeg(-entity.yBodyRot);
        Vec3NT forward = new Vec3NT(0, 0, 0.25).rotateAroundYDeg(-entity.yBodyRot);
        Vec3NT leftleg = new Vec3NT(0.125, 0, 0).rotateAroundYDeg(-entity.yBodyRot);
        return new BoneDefinition[] {
                new BoneDefinition(EnumSkeletonType.SKULL_VILLAGER, -entity.yHeadRot, entity.getXRot(), entity.getX(), entity.getY() + 1.6875, entity.getZ()),
                new BoneDefinition(EnumSkeletonType.TORSO, -entity.yBodyRot, 0, entity.getX(), entity.getY() + 1, entity.getZ()),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, -45, entity.getX() + leftarm.xCoord + forward.xCoord, entity.getY() + 1.125, entity.getZ() + leftarm.zCoord + forward.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, -45, entity.getX() - leftarm.xCoord + forward.xCoord, entity.getY() + 1.125, entity.getZ() - leftarm.zCoord + forward.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, entity.getX() + leftleg.xCoord, entity.getY() + 0.375, entity.getZ() + leftleg.zCoord),
                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, entity.getX() - leftleg.xCoord, entity.getY() + 0.375, entity.getZ() - leftleg.zCoord),
        };
    };

//    public static Function<LivingEntity, BoneDefinition[]> BONES_DUMMY = (entity) -> {
//        Vec3NT leftarm = new Vec3NT(0.375, 0, 0).rotateAroundYDeg(-entity.yBodyRot);
//        Vec3NT leftleg = new Vec3NT(0.125, 0, 0).rotateAroundYDeg(-entity.yBodyRot);
//        return new BoneDefinition[] {
//                new BoneDefinition(EnumSkeletonType.SKULL, -entity.yHeadRot, entity.getXRot(), entity.getX(), entity.getY() + 1.75, entity.getZ()),
//                new BoneDefinition(EnumSkeletonType.TORSO, -entity.yBodyRot, 0, entity.getX(), entity.getY() + 1.125, entity.getZ()),
//                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, entity.getX() + leftarm.xCoord, entity.getY() + 1.125, entity.getZ() + leftarm.zCoord),
//                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, entity.getX() - leftarm.xCoord, entity.getY() + 1.125, entity.getZ() - leftarm.zCoord),
//                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, entity.getX() + leftleg.xCoord, entity.getY() + 0.375, entity.getZ() + leftleg.zCoord),
//                new BoneDefinition(EnumSkeletonType.LIMB, -entity.yBodyRot, 0, entity.getX() - leftleg.xCoord, entity.getY() + 0.375, entity.getZ() - leftleg.zCoord),
//        };
//    };

    public static void init() {
        skullanizer.put(ServerPlayer.class.getSimpleName(), BONES_BIPED);
        skullanizer.put(LocalPlayer.class.getSimpleName(), BONES_BIPED);
        skullanizer.put(Player.class.getSimpleName(), BONES_BIPED);

        skullanizer.put(Zombie.class.getSimpleName(), BONES_ZOMBIE);
        skullanizer.put(Skeleton.class.getSimpleName(), BONES_ZOMBIE);

        skullanizer.put(Villager.class.getSimpleName(), BONES_VILLAGER);
        skullanizer.put(Witch.class.getSimpleName(), BONES_VILLAGER);
    }
}
