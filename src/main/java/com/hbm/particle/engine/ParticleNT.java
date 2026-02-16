package com.hbm.particle.engine;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleGroup;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public abstract class ParticleNT {
    private static final AABB INITIAL_AABB = new AABB(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100.0D);
    protected final ClientLevel level;
    public double xo;
    public double yo;
    public double zo;
    public double x;
    public double y;
    public double z;
    public double xd;
    public double yd;
    public double zd;
    public float quadSize;
    private AABB bb;
    public boolean onGround;
    public boolean noClip;
    public boolean dead;
    protected float bbWidth;
    protected float bbHeight;
    protected final RandomSource random;
    public int age;
    public int lifetime;
    public float gravity;
    public float rCol;
    public float gCol;
    public float bCol;
    public float alpha;
    protected float roll;
    protected float oRoll;
    protected float friction;
    protected boolean speedUpWhenYMotionIsBlocked;

    protected ParticleNT(ClientLevel level, double x, double y, double z) {
        this.bb = INITIAL_AABB;
        this.noClip = false;
        this.bbWidth = 0.6F;
        this.bbHeight = 1.8F;
        this.random = RandomSource.create();
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.alpha = 1.0F;
        this.friction = 0.98F;
        this.speedUpWhenYMotionIsBlocked = false;
        this.level = level;
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.lifetime = (int)(4.0F / (this.random.nextFloat() * 0.9F + 0.1F));
    }

    public ParticleNT(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this(level, x, y, z);
        this.xd = xSpeed + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.4F;
        this.yd = ySpeed + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.4F;
        this.zd = zSpeed + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.4F;
        double d0 = (Math.random() + Math.random() + (double)1.0F) * (double)0.15F;
        double d1 = Math.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
        this.xd = this.xd / d1 * d0 * (double)0.4F;
        this.yd = this.yd / d1 * d0 * (double)0.4F + (double)0.1F;
        this.zd = this.zd / d1 * d0 * (double)0.4F;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.dead = true;
        } else {
            this.yd -= 0.04 * (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);
            if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
                this.xd *= 1.1;
                this.zd *= 1.1;
            }

            this.xd *= this.friction;
            this.yd *= this.friction;
            this.zd *= this.friction;
            if (this.onGround) {
                this.xd *= 0.7F;
                this.zd *= 0.7F;
            }
        }
    }

    public abstract void render(VertexConsumer consumer, Camera camera, float partialTicks);
    public abstract RenderType getRenderType();

    public String toString() {
        return this.getClass().getSimpleName() + ", Pos (" + this.x + "," + this.y + "," + this.z + "), RGBA (" + this.rCol + "," + this.gCol + "," + this.bCol + "," + this.alpha + "), Age " + this.age;
    }

    public void setPos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        float f = this.bbWidth / 2.0F;
        float f1 = this.bbHeight;
        this.setBoundingBox(new AABB(x - (double)f, y, z - (double)f, x + (double)f, y + (double)f1, z + (double)f));
    }

    public void move(double x, double y, double z) {
        double d0 = x;
        double d1 = y;
        double d2 = z;

        if (!this.noClip && (x != 0.0 || y != 0.0 || z != 0.0) && x * x + y * y + z * z < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
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

    protected void setLocationFromBoundingbox() {
        AABB aabb = this.getBoundingBox();
        this.x = (aabb.minX + aabb.maxX) / (double)2.0F;
        this.y = aabb.minY;
        this.z = (aabb.minZ + aabb.maxZ) / (double)2.0F;
    }

    protected int getLightColor() {
        BlockPos blockpos = BlockPos.containing(this.x, this.y, this.z);
        return this.level.hasChunkAt(blockpos) ? LevelRenderer.getLightColor(this.level, blockpos) : 0;
    }

    public AABB getBoundingBox() {
        return this.bb;
    }

    public void setBoundingBox(AABB bb) {
        this.bb = bb;
    }

    public AABB getRenderBoundingBox() {
        return this.getBoundingBox().inflate(1.0F);
    }

    public void remove() {
        this.dead = true;
    }
}
