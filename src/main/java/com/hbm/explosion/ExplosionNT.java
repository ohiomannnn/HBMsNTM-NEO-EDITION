package com.hbm.explosion;

import com.hbm.blocks.ModBlocks;

import java.util.*;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ExplosionNT {
    private final Level level;
    private final Entity source;
    private final double x;
    private final double y;
    private final double z;
    private final float size;
    private int resolution = 16;
    private final Random explosionRNG = new Random();
    private final Set<BlockPos> affectedBlocks = new HashSet<>();
    private final Map<Player, Vec3> knockbackMap = new HashMap<>();
    private final EnumSet<ExAttrib> attributes = EnumSet.noneOf(com.hbm.explosion.ExplosionNT.ExAttrib.class);
    public static final HashMap<Block, Block> erosion = new HashMap<>();

    public ExplosionNT(Level level, Entity source, double x, double y, double z, float size) {
        this.level = level;
        this.source = source;
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
    }

    public ExplosionNT addAttrib(com.hbm.explosion.ExplosionNT.ExAttrib attrib) {
        this.attributes.add(attrib);
        return this;
    }


    public ExplosionNT addAllAttrib(ExAttrib... attrib) {
        attributes.addAll(Arrays.asList(attrib));
        return this;
    }

    public ExplosionNT overrideResolution(int res) {
        this.resolution = res;
        return this;
    }

    public void explode() {
        this.doExplosionA();
        this.doExplosionB();
    }

    private void doExplosionA() {
        double density;
        for(int i = 0; i < this.resolution; ++i) {
            for(int j = 0; j < this.resolution; ++j) {
                for(int k = 0; k < this.resolution; ++k) {
                    if (i == 0 || i == this.resolution - 1 || j == 0 || j == this.resolution - 1 || k == 0 || k == this.resolution - 1) {
                        double dx = (double)i / ((double)this.resolution - 1.0D) * 2.0D - 1.0D;
                        double dy = (double)j / ((double)this.resolution - 1.0D) * 2.0D - 1.0D;
                        double dz = (double)k / ((double)this.resolution - 1.0D) * 2.0D - 1.0D;
                        density = Math.sqrt(dx * dx + dy * dy + dz * dz);
                        dx /= density;
                        dy /= density;
                        dz /= density;
                        float power = this.size * (0.7F + this.level.random.nextFloat() * 0.6F);
                        double cx = this.x;
                        double cy = this.y;
                        double cz = this.z;

                        for(float step = 0.3F; power > 0.0F; power -= step * 0.75F) {
                            BlockPos pos = BlockPos.containing(cx, cy, cz);
                            BlockState state = this.level.getBlockState(pos);
                            if (!state.isAir()) {
                                float resistance = state.getBlock().getExplosionResistance();
                                power -= (resistance + 0.3F) * step;
                            }

                            if (power > 0.0F) {
                                this.affectedBlocks.add(pos);
                            }

                            if (this.attributes.contains(com.hbm.explosion.ExplosionNT.ExAttrib.ERRODE) && erosion.containsKey(state.getBlock())) {
                                this.affectedBlocks.add(pos);
                            }

                            cx += dx * (double)step;
                            cy += dy * (double)step;
                            cz += dz * (double)step;
                        }
                    }
                }
            }
        }

        if (!this.attributes.contains(com.hbm.explosion.ExplosionNT.ExAttrib.NOHURT)) {
            double radius = (double)this.size * 2.0D;
            AABB area = new AABB(this.x - radius, this.y - radius, this.z - radius, this.x + radius, this.y + radius, this.z + radius);
            List<Entity> entities = this.level.getEntities(this.source, area);

            for (Entity entity : entities) {
                double dist = entity.distanceToSqr(this.x, this.y, this.z) / (radius * radius);
                if (dist <= 1.0D) {
                    Vec3 delta = entity.position().subtract(this.x, this.y, this.z).normalize();
                    density = 1.0D;
                    double impact = (1.0D - dist) * density;
                    DamageSource dmg = this.level.damageSources().explosion(this.source, null);
                    entity.hurt(dmg, (float) ((impact * impact + impact) / 2.0D * 8.0D * (double) this.size + 1.0D));
                    entity.setDeltaMovement(entity.getDeltaMovement().add(delta.scale(impact)));
                    if (entity instanceof Player player) {
                        this.knockbackMap.put(player, delta.scale(impact));
                    }
                }
            }
        }

    }

    private void doExplosionB() {
        if (this.level instanceof ServerLevel serverLevel) {
            if (!this.attributes.contains(com.hbm.explosion.ExplosionNT.ExAttrib.NOSOUND)) {
                this.level.playSound(null, this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F);
            }

            boolean doesErode = false;

            for (BlockPos pos : this.affectedBlocks) {
                BlockState state = this.level.getBlockState(pos);
                if (!state.isAir()) {
                    Block erodesInto = (Block) erosion.get(state.getBlock());
                    if (this.attributes.contains(ExAttrib.ERRODE) && this.explosionRNG.nextFloat() < 0.6F && erosion.containsKey(state.getBlock())) {
                        doesErode = true;
                        erodesInto = (Block) erosion.get(state.getBlock());
                    }

                    if (this.attributes.contains(ExAttrib.NODROP)) {
                        this.level.removeBlock(pos, false);
                    }

                    if (this.level.random.nextFloat() < 0.15F) {
                        double px = (double) pos.getX() + this.level.random.nextDouble();
                        double py = (double) pos.getY() + this.level.random.nextDouble();
                        double pz = (double) pos.getZ() + this.level.random.nextDouble();
                        serverLevel.sendParticles(ParticleTypes.EXPLOSION, px, py, pz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }

                    if (doesErode && erodesInto != null) {
                        this.level.setBlock(pos, erodesInto.defaultBlockState(), 3);
                    }

                    BlockPos above;
                    if (this.attributes.contains(ExAttrib.FIRE)) {
                        above = pos.above();
                        if (this.level.isEmptyBlock(above)) {
                            this.level.setBlockAndUpdate(above, Blocks.FIRE.defaultBlockState());
                        }
                    } else if (this.attributes.contains(ExAttrib.LAVA)) {
                        above = pos.above();
                        if (this.level.isEmptyBlock(above)) {
                            this.level.setBlockAndUpdate(above, Blocks.LAVA.defaultBlockState());
                        }
                    }
                }
            }

        }
    }

    public Map<Player, Vec3> getKnockbackMap() {
        return this.knockbackMap;
    }

    public enum ExAttrib {
        FIRE,		//classic vanilla fire explosion
        BALEFIRE,	//same with but with balefire
        DIGAMMA,
        DIGAMMA_CIRCUIT,
        LAVA,		//again the same thing but lava
        LAVA_V,		//again the same thing but volcanic lava
        LAVA_R,		//again the same thing but radioactive lava
        ERRODE,		//will turn select blocks into gravel or sand
        ALLMOD,		//block placer attributes like fire are applied for all destroyed blocks
        ALLDROP,	//miner TNT!
        NODROP,		//the opposite
        NOPARTICLE,
        NOSOUND,
        NOHURT
    }
    static {
        erosion.put(ModBlocks.BRICK_CONCRETE.get(), ModBlocks.BRICK_CONCRETE_BROKEN.get());
        erosion.put(ModBlocks.BRICK_CONCRETE_BROKEN.get(), Blocks.GRAVEL);
    }
}