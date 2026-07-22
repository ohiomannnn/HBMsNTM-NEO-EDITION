package com.hbm.entity.projectile;

import com.hbm.config.NtmConfig;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.particle.NtmParticles;
import com.hbm.particle.vanilla.NbtParticleOptions;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.particle.ParticleUtil;
import com.hbm.world.MeteoriteStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class Meteor extends Entity {

    public boolean safe = false;
    private AudioWrapper audioFly;
    private final MeteoriteStructure meteorite = new MeteoriteStructure();

    public Meteor(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public List<BlockPos> getBlocksInRadius(BlockPos pos, int radius) {
        List<BlockPos> foundBlocks = new ArrayList<>();

        int rSq = radius * radius;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    // Check if point (dx, dy, dz) lies inside the sphere
                    if (dx * dx + dy * dy + dz * dz <= rSq) {
                        foundBlocks.add(pos.offset(dx, dy, dz));
                    }
                }
            }
        }
        return foundBlocks;
    }

    public void damageOrDestroyBlock(Level level, BlockPos pos) {
        if (safe) return;

        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return;

        float hardness = state.getDestroySpeed(level, pos);

        if (state.is(net.minecraft.tags.BlockTags.LEAVES) || state.is(net.minecraft.tags.BlockTags.LOGS) || (hardness >= 0 && hardness <= 0.3F)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        } else {
            if (hardness < 0 || hardness > 5F) return;

            if (random.nextInt(6) == 1) {
                if (state.is(Blocks.DIRT)) {
                    level.setBlock(pos, com.hbm.blocks.NtmBlocks.DIRT_DEAD.get().defaultBlockState(), 3);
                } else if (state.is(Blocks.SAND) || state.is(Blocks.RED_SAND)) {
                    level.setBlock(pos, random.nextInt(2) == 1 ? Blocks.SANDSTONE.defaultBlockState() : Blocks.GLASS.defaultBlockState(), 3);
                } else if (state.is(Blocks.STONE)) {
                    level.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 3);
                } else if (state.is(Blocks.GRASS_BLOCK)) {
                    level.setBlock(pos, com.hbm.blocks.NtmBlocks.WASTE_EARTH.get().defaultBlockState(), 3);
                }
            }
        }
    }

    public void clearMeteorPath(Level level, BlockPos pos) {
        for (BlockPos blockPos : getBlocksInRadius(pos, 5)) {
            damageOrDestroyBlock(level, blockPos);
        }
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide && !NtmConfig.COMMON.ENABLE_METEOR_STRIKES.get()) {
            this.discard();
            return;
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.03, this.getDeltaMovement().z);
        if (this.getDeltaMovement().y < -2.5) this.setDeltaMovement(this.getDeltaMovement().x, -2.5, this.getDeltaMovement().z);

        this.move(MoverType.SELF, this.getDeltaMovement());

        if (!this.level().isClientSide && this.getY() < 260) {
            clearMeteorPath(this.level(), this.blockPosition());
            if (this.onGround()) {
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), 5 + random.nextFloat(), !safe ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE);

                if (NtmConfig.COMMON.ENABLE_METEOR_TAILS.get()) {
                    if (this.level() instanceof ServerLevel serverLevel) {
                        ExplosionLarge.spawnRubble(serverLevel, this.getX(), this.getY(), this.getZ(), 15);

                        ExplosionLarge.spawnParticles(serverLevel, this.getX(), this.getY() + 5, this.getZ(), 75);
                        ExplosionLarge.spawnParticles(serverLevel, this.getX() + 5, this.getY(), this.getZ(), 75);
                        ExplosionLarge.spawnParticles(serverLevel, this.getX() - 5, this.getY(), this.getZ(), 75);
                        ExplosionLarge.spawnParticles(serverLevel, this.getX(), this.getY(), this.getZ() + 5, 75);
                        ExplosionLarge.spawnParticles(serverLevel, this.getX(), this.getY(), this.getZ() - 5, 75);
                    }

                    // Bury the meteor into the ground
                    BlockPos spawnPos = new BlockPos(
                            (int) (Math.round(this.getX() - 0.5D) + (safe ? 0 : (this.getDeltaMovement().x * 4))),
                            (int) Math.round(this.getY() - (safe ? 0 : 4)),
                            (int) (Math.round(this.getZ() - 0.5D) + (safe ? 0 : (this.getDeltaMovement().z * 4)))
                    );

                    meteorite.generate(level(), random, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), safe, true, true);
                    clearMeteorPath(level(), spawnPos);

                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), NtmSoundEvents.OLD_EXPLOSION, SoundSource.AMBIENT, 10000.0F, 0.5F + this.random.nextFloat() * 0.1F);

                    this.discard();
                    return;
                }
            }
        }

        // Sound
        if (level().isClientSide) {
            if (this.audioFly == null) this.audioFly = AudioWrapper.getLoopedSound(NtmSoundEvents.METEORITE_FALLING_LOOP.get(), SoundSource.BLOCKS,  0, 0, 0, 1F, 200F, 0.9F + this.random.nextFloat() * 0.2F, 10);

            if (this.audioFly.isPlaying()) {
                // Update sound
                this.audioFly.keepAlive();
                this.audioFly.updateVolume(1F);
                this.audioFly.updatePosition((float) this.getX(), (float) (this.getY() + this.getBbHeight() / 2), (float) this.getZ());
            } else {
                // Start playing the sound
                playSoundClient();
            }

            if (NtmConfig.COMMON.ENABLE_METEOR_TAILS.get()) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("count", 10);
                tag.putDouble("width", 1);

                ParticleUtil.addParticle(this.level, new NbtParticleOptions(NtmParticles.EXHAUST_METEOR.get(), tag), this.getX() - this.getDeltaMovement().z, this.getY() - this.getDeltaMovement().y, this.getZ() - this.getDeltaMovement().z, 350.0);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void playSoundClient() {
        Player player = Minecraft.getInstance().player;
        double distance = player.distanceToSqr(this.position());
        if (distance < 210 * 210) {
            this.audioFly.startSound();
        }
    }


    @Override
    public void onClientRemoval() {
        if (audioFly != null) {
            this.audioFly.stopSound();
        }
    }

    @Override public boolean shouldRenderAtSqrDistance(double distance) { return true; }

    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) { }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.safe = tag.getBoolean("Safe");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("Safe", this.safe);
    }
}
