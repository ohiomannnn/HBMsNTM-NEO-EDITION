package com.hbm.particle.helper;

import com.hbm.particle.SpentCasing;
import com.hbm.particle.SpentCasingParticle;
import com.hbm.particle.engine.ParticleEngineNT;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class CasingCreator implements IParticleCreator {

    /** Default casing without smoke */
    public static void composeEffect(Level level, LivingEntity player, double frontOffset, double heightOffset, double sideOffset, double frontMotion, double heightMotion, double sideMotion, double motionVariance, String casing) {
        composeEffect(level, player, frontOffset, heightOffset, sideOffset, frontMotion, heightMotion, sideMotion, motionVariance, 5F, 10F, casing, false, 0, 0, 0);
    }

    /** Casing without smoke */
    public static void composeEffect(Level level, LivingEntity player, double frontOffset, double heightOffset, double sideOffset, double frontMotion, double heightMotion, double sideMotion, double motionVariance, float multPitch, float multYaw, String casing) {
        composeEffect(level, player, frontOffset, heightOffset, sideOffset, frontMotion, heightMotion, sideMotion, motionVariance, multPitch, multYaw, casing, false, 0, 0, 0);
    }

    /** Default casing, but with smoke*/
    public static void composeEffect(Level level, LivingEntity player, double frontOffset, double heightOffset, double sideOffset, double frontMotion, double heightMotion, double sideMotion, double motionVariance, String casing, boolean smoking, int smokeLife, double smokeLift, int nodeLife) {
        composeEffect(level, player, frontOffset, heightOffset, sideOffset, frontMotion, heightMotion, sideMotion, motionVariance, 5F, 10F, casing, false, 0, 0, 0);
    }

    public static void composeEffect(Level level, double x, double y, double z, float yaw, float pitch, double frontMotion, double heightMotion, double sideMotion, double motionVariance, float mPitch, float mYaw, String casing, boolean smoking, int smokeLife, double smokeLift, int nodeLife) {

        Vec3 motion = new Vec3(sideMotion, heightMotion, frontMotion);
        motion = motion.xRot(-pitch / 180F * (float) Math.PI);
        motion = motion.yRot(-yaw / 180F * (float) Math.PI);

        double mX = motion.x + level.random.nextGaussian() * motionVariance;
        double mY = motion.y + level.random.nextGaussian() * motionVariance;
        double mZ = motion.z + level.random.nextGaussian() * motionVariance;

        CompoundTag data = new CompoundTag();
        data.putString("type", "casingNT");
        data.putDouble("mX", mX);
        data.putDouble("mY", mY);
        data.putDouble("mZ", mZ);
        data.putFloat("yaw", yaw);
        data.putFloat("pitch", pitch);
        data.putFloat("mPitch", mPitch);
        data.putFloat("mYaw", mYaw);
        data.putString("name", casing);
        data.putBoolean("smoking", smoking);
        data.putInt("smokeLife", smokeLife);
        data.putDouble("smokeLift", smokeLift);
        data.putInt("nodeLife", nodeLife);

        IParticleCreator.sendPacket(level, x, y, z, 50, data);
    }

    public static void composeEffect(Level level, LivingEntity player, double frontOffset, double heightOffset, double sideOffset, double frontMotion, double heightMotion, double sideMotion, double motionVariance, float mPitch, float mYaw, String casing, boolean smoking, int smokeLife, double smokeLift, int nodeLife) {

        if(player.isShiftKeyDown()) heightOffset -= 0.075F;

        Vec3 offset = new Vec3(sideOffset, heightOffset, frontOffset);
        offset = offset.xRot(-player.xRot / 180F * (float) Math.PI);
        offset = offset.yRot(-player.yRot / 180F * (float) Math.PI);

        double x = player.getX() + offset.x;
        double y = player.getY() + player.getEyeHeight() + offset.y;
        double z = player.getZ() + offset.z;

        Vec3 motion = new Vec3(sideMotion, heightMotion, frontMotion);
        motion = motion.xRot(-player.xRot / 180F * (float) Math.PI);
        motion = motion.yRot(-player.yRot / 180F * (float) Math.PI);

        double mX = player.getDeltaMovement().x + motion.x + player.random.nextGaussian() * motionVariance;
        double mY = player.getDeltaMovement().y + motion.y + player.random.nextGaussian() * motionVariance;
        double mZ = player.getDeltaMovement().z + motion.z + player.random.nextGaussian() * motionVariance;

        // todo
        //if(player instanceof Player && ((EntityPlayer) player).capabilities.isFlying) mY -= 0.04D;

        CompoundTag data = new CompoundTag();
        data.putString("type", "casingNT");
        data.putDouble("mX", mX);
        data.putDouble("mY", mY);
        data.putDouble("mZ", mZ);
        data.putFloat("yaw", player.yRot);
        data.putFloat("pitch", player.xRot);
        data.putFloat("mPitch", mPitch);
        data.putFloat("mYaw", mYaw);
        data.putString("name", casing);
        data.putBoolean("smoking", smoking);
        data.putInt("smokeLife", smokeLife);
        data.putDouble("smokeLift", smokeLift);
        data.putInt("nodeLife", nodeLife);

        IParticleCreator.sendPacket(level, x, y, z, 50, data);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void makeParticle(ClientLevel level, Player player, RandomSource rand, double x, double y, double z, CompoundTag tag) {

        String name = tag.getString("name");
        SpentCasing casingConfig = SpentCasing.casingMap.get(name);
        double mX = tag.getDouble("mX");
        double mY = tag.getDouble("mY");
        double mZ = tag.getDouble("mZ");
        float yaw = tag.getFloat("yaw");
        float pitch = tag.getFloat("pitch");
        float mPitch = tag.getFloat("mPitch");
        float mYaw = tag.getFloat("mYaw");
        boolean smoking = tag.getBoolean("smoking");
        int smokeLife = tag.getInt("smokeLife");
        double smokeLift = tag.getDouble("smokeLift");
        int nodeLife = tag.getInt("nodeLife");
        SpentCasingParticle casing = new SpentCasingParticle(level, x, y, z, mX, mY, mZ, mPitch, mYaw, casingConfig, smoking, smokeLife, smokeLift, nodeLife);
        casing.yRotO = casing.yRot = yaw;
        casing.xRotO = casing.xRot = pitch;
        ParticleEngineNT.INSTANCE.add(casing);
    }
}
