package com.hbm.particle;

import com.hbm.particle.vanilla.NbtParticleOptions;
import com.hbm.particle.vanilla.ParticleProviderBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;

import java.util.List;

public class ParticleDust extends TextureSheetParticle {
    private final BlockPos pos;
    private final float uo;
    private final float vo;

    public ParticleDust(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state) {
        this(level, x, y, z, xSpeed, ySpeed, zSpeed, state, BlockPos.containing(x, y, z));
    }

    public ParticleDust(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state, BlockPos pos) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.pos = pos;
        this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(state));
        this.gravity = 1.0F;
        this.rCol = 0.6F;
        this.gCol = 0.6F;
        this.bCol = 0.6F;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        if(IClientBlockExtensions.of(state).areBreakingParticlesTinted(state, level, pos)) {
            int i = Minecraft.getInstance().getBlockColors().getColor(state, level, pos, 0);
            this.rCol *= (float) (i >> 16 & 255) / 255.0F;
            this.gCol *= (float) (i >> 8 & 255) / 255.0F;
            this.bCol *= (float) (i & 255) / 255.0F;
        }

        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
    }

    public ParticleDust setOriginalSize() {
        this.quadSize /= 2.0F;
        return this;
    }

    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square((double) 100.0F);

    @Override
    public void move(double x, double y, double z) {
        double d0 = x;
        double d1 = y;
        double d2 = z;

        if(this.hasPhysics && (x != 0.0 || y != 0.0 || z != 0.0) && x * x + y * y + z * z < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
            Vec3 vec3 = Entity.collideBoundingBox(null, new Vec3(x, y, z), this.getBoundingBox(), this.level, List.of());
            x = vec3.x;
            y = vec3.y;
            z = vec3.z;
        }

        if(x != 0.0 || y != 0.0 || z != 0.0) {
            this.setBoundingBox(this.getBoundingBox().move(x, y, z));
            this.setLocationFromBoundingbox();
        }

        this.onGround = d1 != y && d1 < 0.0;

        if(this.onGround) {
            this.xd = 0.0;
            this.yd = 0.0;
            this.zd = 0.0;
        } else {
            if(d0 != x) this.xd = 0.0;
            if(d2 != z) this.zd = 0.0;
        }
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    protected float getU0() {
        return this.sprite.getU((this.uo + 1.0F) / 4.0F);
    }

    protected float getU1() {
        return this.sprite.getU(this.uo / 4.0F);
    }

    protected float getV0() {
        return this.sprite.getV(this.vo / 4.0F);
    }

    protected float getV1() {
        return this.sprite.getV((this.vo + 1.0F) / 4.0F);
    }

    public int getLightColor(float partialTick) {
        int i = super.getLightColor(partialTick);
        return i == 0 && this.level.hasChunkAt(this.pos) ? LevelRenderer.getLightColor(this.level, this.pos) : i;
    }

    public static abstract class VomitBaseProvider extends ParticleProviderBase<NbtParticleOptions> {
        @Override
        public void createParticle(NbtParticleOptions options, double x, double y, double z, double xd, double yd, double zd, ClientLevel level, LocalPlayer player, int particleSetting) {
            CompoundTag tag = options.tag;

            Entity e = level.getEntity(tag.getInt("entity"));
            if(e == null) return;
            int count = tag.getInt("count") / (particleSetting + 1);

            if(e instanceof LivingEntity living) {
                double ix = living.getX();
                double iy = living.getY() + living.getEyeHeight();
                double iz = living.getZ();

                Vec3 lookAngle = living.getLookAngle();

                for(int i = 0; i < count; i++) {
                    this.createVomit(level, Minecraft.getInstance().particleEngine, lookAngle, ix, iy, iz);
                }
            }
        }

        public abstract void createVomit(ClientLevel level, ParticleEngine engine, Vec3 lookAngle, double ix, double iy, double iz);
    }

    public static class VomitNormalProvider extends VomitBaseProvider {
        @Override
        public void createVomit(ClientLevel level, ParticleEngine engine, Vec3 lookAngle, double ix, double iy, double iz) {
            ParticleDust particle = new ParticleDust(level, ix, iy, iz, (lookAngle.x + level.random.nextGaussian() * 0.2) * 0.2, (lookAngle.y + level.random.nextGaussian() * 0.2) * 0.2, (lookAngle.z + level.random.nextGaussian() * 0.2) * 0.2, level.random.nextBoolean() ? Blocks.GREEN_TERRACOTTA.defaultBlockState() : Blocks.LIME_TERRACOTTA.defaultBlockState());
            particle.setLifetime(150 + level.random.nextInt(50));
            particle.setOriginalSize();
            engine.add(particle);
        }
    }
    public static class VomitBloodProvider extends VomitBaseProvider {
        @Override
        public void createVomit(ClientLevel level, ParticleEngine engine, Vec3 lookAngle, double ix, double iy, double iz) {
            ParticleDust particle = new ParticleDust(level, ix, iy, iz, (lookAngle.x + level.random.nextGaussian() * 0.2) * 0.2, (lookAngle.y + level.random.nextGaussian() * 0.2) * 0.2, (lookAngle.z + level.random.nextGaussian() * 0.2) * 0.2, Blocks.REDSTONE_BLOCK.defaultBlockState());
            particle.setLifetime(150 + level.random.nextInt(50));
            particle.setOriginalSize();
            engine.add(particle);
        }
    }
    public static class VomitSmokeProvider extends VomitBaseProvider {
        @Override
        public void createVomit(ClientLevel level, ParticleEngine engine, Vec3 lookAngle, double ix, double iy, double iz) {
            engine.createParticle(ParticleTypes.SMOKE, ix, iy, iz, (lookAngle.x + level.random.nextGaussian() * 0.1) * 0.05, (lookAngle.y + level.random.nextGaussian() * 0.1) * 0.05, (lookAngle.z + level.random.nextGaussian() * 0.1) * 0.05);
        }
    }

    public static class SweatProvider extends ParticleProviderBase<NbtParticleOptions> {
        @Override
        public void createParticle(NbtParticleOptions options, double x, double y, double z, double xd, double yd, double zd, ClientLevel level, LocalPlayer player, int particleSetting) {
            CompoundTag tag = options.tag;

            Entity e = level.getEntity(tag.getInt("entity"));
            if(e == null) return;
            BlockState state = NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), tag.getCompound("state"));
            int count = tag.getInt("count");

            if(e instanceof LivingEntity living) {
                for(int i = 0; i < count; i++) {
                    AABB bounding = living.getBoundingBox();
                    double ix = bounding.minX - 0.2 + (bounding.maxX - bounding.minX + 0.4) * level.random.nextDouble();
                    double iy = bounding.minY +       (bounding.maxY - bounding.minY + 0.2) * level.random.nextDouble();
                    double iz = bounding.minZ - 0.2 + (bounding.maxZ - bounding.minZ + 0.4) * level.random.nextDouble();

                    ParticleDust particle = new ParticleDust(level, ix, iy, iz, 0, 0, 0, state);
                    particle.setLifetime(150 + level.random.nextInt(50));
                    particle.setOriginalSize();

                    Minecraft.getInstance().particleEngine.add(particle);
                }
            }
        }
    }
}
