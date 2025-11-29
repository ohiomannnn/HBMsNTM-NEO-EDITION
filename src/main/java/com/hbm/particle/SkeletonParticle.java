package com.hbm.particle;

import com.hbm.particle.helper.SkeletonCreator.EnumSkeletonType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SkeletonParticle extends TextureSheetParticle {

    protected EnumSkeletonType type;

    public ResourceLocation useTexture;
    public ResourceLocation useTextureExt;

    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square((double)100.0F);

    private float momentumYaw;
    private float momentumPitch;
    private int initialDelay;

    public double prevRotationPitch;
    public double prevRotationYaw;
    public double rotationPitch;
    public double rotationYaw;


    public SkeletonParticle(ClientLevel level, double x, double y, double z, float r, float g, float b, EnumSkeletonType type) {
        super(level, x, y, z);
        this.setSpriteFromAge(ModParticles.BASE_PARTICLE_SPRITES);
        this.type = type;

        this.lifetime = 1200 + random.nextInt(20);

        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.gravity = 0.02F;
        this.initialDelay = 20;

        this.momentumPitch = random.nextFloat() * 5 * (random.nextBoolean() ? 1 : -1);
        this.momentumYaw = random.nextFloat() * 5 * (random.nextBoolean() ? 1 : -1);

        //this.useTexture = texture;
        //this.useTextureExt = texture_ext;
    }

    public SkeletonParticle makeGib() {
        this.initialDelay = -2; // skip post delay motion randomization
        //this.useTexture = texture_blood;
        //this.useTextureExt = texture_blood_ext;
        this.gravity = 0.04F;
        this.lifetime = 600 + random.nextInt(20);
        return this;
    }

    @Override
    public void move(double x, double y, double z) {
        double d0 = x;
        double d1 = y;
        double d2 = z;

        if (this.hasPhysics && (x != 0.0 || y != 0.0 || z != 0.0) && x * x + y * y + z * z < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
            Vec3 vec3 = Entity.collideBoundingBox(null, new Vec3(x, y, z), this.getBoundingBox(), this.level, List.of());
            x = vec3.x;
            y = vec3.y;
            z = vec3.z;
        }

        if (x != 0.0 || y != 0.0 || z != 0.0) {
            this.setBoundingBox(this.getBoundingBox().move(x, y, z));
            this.setLocationFromBoundingbox();
        }

        this.onGround = d1 != y && d1 < 0.0;

        if (this.onGround) {
            this.xd = 0.0;
            this.yd = 0.0;
            this.zd = 0.0;
        } else {
            if (d0 != x) this.xd = 0.0;
            if (d2 != z) this.zd = 0.0;
        }
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;

        if (initialDelay-- > 0) return;

        if (initialDelay == -1) {
            this.xd = random.nextGaussian() * 0.025;
            this.zd = random.nextGaussian() * 0.025;
        }

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        boolean wasOnGround = this.onGround;

        this.yd -= this.gravity;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.98D;
        this.yd *= 0.98D;
        this.zd *= 0.98D;

        if (!this.onGround) {
            this.rotationPitch += this.momentumPitch;
            this.rotationYaw += this.momentumYaw;
        } else {
            this.xd = 0;
            this.yd = 0;
            this.zd = 0;

            if (!wasOnGround) {
                level.playSound(null, x, y, z, SoundEvents.SKELETON_HURT, SoundSource.AMBIENT, 0.25F, 0.8F + random.nextFloat() * 0.4F);
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public TextureSheetParticle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double dx, double dy, double dz) {
            return new SkeletonParticle(world, x, y, z, 1F, 1F ,1F, EnumSkeletonType.SKULL);
        }
    }
}
