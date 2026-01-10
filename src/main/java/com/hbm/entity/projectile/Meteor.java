package com.hbm.entity.projectile;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.config.MainConfig;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.lib.ModSounds;
import com.hbm.sound.AudioWrapper;
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

        // Get current block info
        BlockState state = level.getBlockState(pos);
        float hardness = state.getDestroySpeed(level, pos);

        // Check if the block is weak and can be destroyed
        if (hardness >= 0 && hardness <= 0.3F) {
            // Destroy the block
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        } else {
            // Found solid block
            if (hardness < 0 || hardness > 5F) return;

            if (random.nextInt(6) == 1) {
                // Turn blocks into damaged variants
//                if (state.is(Blocks.DIRT)) {
//                    world.setBlock(blockX, blockY, blockZ, ModBlocks.dirt_dead);
//                } else if (block == Blocks.sand) {
//                    if (random.nextInt(2) == 1) {
//                        world.setBlock(blockX, blockY, blockZ, Blocks.sandstone);
//                    } else {
//                        world.setBlock(blockX, blockY, blockZ, Blocks.glass);
//                    }
//                } else if(block == Blocks.stone) {
//                    world.setBlock(blockX, blockY, blockZ, Blocks.cobblestone);
//                } else if(block == Blocks.grass) {
//                    world.setBlock(blockX, blockY, blockZ, ModBlocks.waste_earth);
//                }
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
        if (!this.level().isClientSide && !MainConfig.COMMON.ENABLE_METEOR_STRIKES.get()) {
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

                if (MainConfig.COMMON.ENABLE_METEOR_TAILS.get()) {
                    if (this.level() instanceof ServerLevel serverLevel) {
                        ExplosionLarge.spawnRubble(serverLevel, this.getX(), this.getY(), this.getZ(), 15);

                        ExplosionLarge.spawnParticles(serverLevel, this.getX(), this.getY() + 5, this.getZ(), 75);
                        ExplosionLarge.spawnParticles(serverLevel, this.getX() + 5, this.getY(), this.getZ(), 75);
                        ExplosionLarge.spawnParticles(serverLevel, this.getX() - 5, this.getY(), this.getZ(), 75);
                        ExplosionLarge.spawnParticles(serverLevel, this.getX(), this.getY(), this.getZ() + 5, 75);
                        ExplosionLarge.spawnParticles(serverLevel, this.getX(), this.getY(), this.getZ() - 5, 75);
                    }

                    // Bury the meteor into the ground
                    BlockPos spawnPos = new BlockPos((int) (Math.round(this.getX() - 0.5D) + (safe ? 0 : (this.getDeltaMovement().z * 4))), (int) Math.round(this.getY() - (safe ? 0 : 4)), (int) (Math.round(this.getZ() - 0.5D) + (safe ? 0 : (this.getDeltaMovement().z * 4))));

                    clearMeteorPath(level(), spawnPos);

                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.OLD_EXPLOSION, SoundSource.AMBIENT, 10000.0F, 0.5F + this.random.nextFloat() * 0.1F);

                    this.discard();
                    return;
                }
            }
        }

        // Sound
        if (level().isClientSide) {
            if (this.audioFly == null) this.audioFly = HBMsNTMClient.getLoopedSound(ModSounds.METEORITE_FALLING_LOOP.get(), SoundSource.AMBIENT,  0, 0, 0, 1F, 200F, 0.9F + this.random.nextFloat() * 0.2F, 10);

            if (this.audioFly.isPlaying()) {
                // Update sound
                this.audioFly.keepAlive();
                this.audioFly.updateVolume(1F);
                this.audioFly.updatePosition((float) this.getX(), (float) (this.getY() + this.getBbHeight() / 2), (float) this.getZ());
            } else {
                // Start playing the sound
                playSoundClient();
            }

            if (MainConfig.COMMON.ENABLE_METEOR_TAILS.get()) {
                CompoundTag tag = new CompoundTag();
                tag.putString("type", "exhaust");
                tag.putString("mode", "meteor");
                tag.putInt("count", 10);
                tag.putDouble("width", 1);
                tag.putDouble("posX", getX() - getDeltaMovement().z);
                tag.putDouble("posY", getY() - getDeltaMovement().y);
                tag.putDouble("posZ", getZ() - getDeltaMovement().z);

                HBMsNTMClient.effectNT(tag);
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
