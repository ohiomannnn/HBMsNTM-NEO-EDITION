package com.hbm.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;

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
        if (IClientBlockExtensions.of(state).areBreakingParticlesTinted(state, level, pos)) {
            int i = Minecraft.getInstance().getBlockColors().getColor(state, level, pos, 0);
            this.rCol *= (float)(i >> 16 & 255) / 255.0F;
            this.gCol *= (float)(i >> 8 & 255) / 255.0F;
            this.bCol *= (float)(i & 255) / 255.0F;
        }

        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
    }

    public ParticleDust setOriginalSize() {
        this.quadSize /= 2.0F;
        return this;
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

    static ParticleDust createTerrainParticle(BlockParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        BlockState blockstate = type.getState();
        return !blockstate.isAir() && !blockstate.is(Blocks.MOVING_PISTON) && blockstate.shouldSpawnTerrainParticles() ? (new ParticleDust(level, x, y, z, xSpeed, ySpeed, zSpeed, blockstate)).updateSprite(blockstate, type.getPos()) : null;
    }

    public ParticleDust updateSprite(BlockState state, BlockPos pos) {
        if (pos != null) {
            this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getTexture(state, this.level, pos));
        }

        return this;
    }

    public static class DustPillarProvider implements ParticleProvider<BlockParticleOption> {
        public Particle createParticle(BlockParticleOption p_338199_, ClientLevel p_338462_, double p_338552_, double p_338714_, double p_338211_, double p_338881_, double p_338238_, double p_338376_) {
            Particle particle = ParticleDust.createTerrainParticle(p_338199_, p_338462_, p_338552_, p_338714_, p_338211_, p_338881_, p_338238_, p_338376_);
            if (particle != null) {
                particle.setParticleSpeed(p_338462_.random.nextGaussian() / (double)30.0F, p_338238_ + p_338462_.random.nextGaussian() / (double)2.0F, p_338462_.random.nextGaussian() / (double)30.0F);
                particle.setLifetime(p_338462_.random.nextInt(20) + 20);
            }

            return particle;
        }
    }

    public static class Provider implements ParticleProvider<BlockParticleOption> {
        public Particle createParticle(BlockParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return ParticleDust.createTerrainParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
