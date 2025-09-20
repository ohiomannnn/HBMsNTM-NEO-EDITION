package com.hbm.explosion;

import java.util.*;

import com.hbm.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ExplosionNT extends Explosion {

    private final double centerX;
    private final double centerY;
    private final double centerZ;

    public Set<ExAttrib> atttributes = new HashSet<>();

    private final Random explosionRNG = new Random();
    private final Level level;
    protected int resolution = 16;
    protected Map<Player, Vec3> affectedEntities = new HashMap<>();
    private LivingEntity exploder;


    public static final List<ExAttrib> nukeAttribs = Arrays.asList(ExAttrib.FIRE, ExAttrib.NOPARTICLE, ExAttrib.NOSOUND, ExAttrib.NODROP, ExAttrib.NOHURT);

    public ExplosionNT(Level level, Entity exploder, double x, double y, double z, float strength, boolean fire, BlockInteraction interaction) {
        super(level, exploder, x, y, z, strength, fire, interaction);
        this.level = level;
        this.centerX = x;
        this.centerY = y;
        this.centerZ = z;
        if (exploder instanceof LivingEntity living) {
            this.exploder = living;
        }
    }

    public void addAttrib(ExAttrib attrib) {
        atttributes.add(attrib);
    }

    public ExplosionNT addAllAttrib(List<ExAttrib> attrib) {
        atttributes.addAll(attrib);
        return this;
    }

    public ExplosionNT addAllAttrib(ExAttrib... attrib) {
        atttributes.addAll(Arrays.asList(attrib));
        return this;
    }

    public ExplosionNT overrideResolution(int res) {
        resolution = res;
        return this;
    }

    public void explode() {
        doExplosionA();
        doExplosionB(true);
    }

    public void doExplosionA() {
        HashSet<BlockPos> hashset = new HashSet<>();
        int i, j, k;
        double currentX, currentY, currentZ;

        Entity source = this.getDirectSourceEntity();
        double sourceX = (source != null ? source.getX() : this.getCenterX());
        double sourceY = (source != null ? source.getY() : this.getCenterY());
        double sourceZ = (source != null ? source.getZ() : this.getCenterZ());

        for (i = 0; i < this.resolution; ++i) {
            for (j = 0; j < this.resolution; ++j) {
                for (k = 0; k < this.resolution; ++k) {

                    if (i == 0 || i == this.resolution - 1 || j == 0 || j == this.resolution - 1 || k == 0 || k == this.resolution - 1) {

                        double d0 = (float) i / ((float) this.resolution - 1.0F) * 2.0F - 1.0F;
                        double d1 = (float) j / ((float) this.resolution - 1.0F) * 2.0F - 1.0F;
                        double d2 = (float) k / ((float) this.resolution - 1.0F) * 2.0F - 1.0F;

                        double dist = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= dist;
                        d1 /= dist;
                        d2 /= dist;

                        float remainingPower = this.radius() * (0.7F + this.level.random.nextFloat() * 0.6F);
                        currentX = sourceX;
                        currentY = sourceY;
                        currentZ = sourceZ;

                        for (float step = 0.3F; remainingPower > 0.0F; remainingPower -= step * 0.75F) {
                            int xPos = Mth.floor(currentX);
                            int yPos = Mth.floor(currentY);
                            int zPos = Mth.floor(currentZ);
                            BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                            BlockState state = level.getBlockState(blockPos);
                            Block b = state.getBlock();

                            if (!state.isAir()) {
                                float resistance = this.exploder != null
                                        ? this.exploder.getBlockExplosionResistance(this, this.level, blockPos, state, state.getFluidState(), remainingPower)
                                        : b.getExplosionResistance(state, level, blockPos, this);
                                remainingPower -= (resistance + 0.3F) * step;
                            }

                            if (!state.isAir() && remainingPower > 0.0F &&
                                    (this.exploder == null || this.exploder.shouldBlockExplode(this, this.level, blockPos, b.defaultBlockState(), remainingPower))) {
                                hashset.add(blockPos);

                            } else if (this.has(ExAttrib.ERRODE) && errosion.containsKey(b)) {
                                hashset.add(blockPos);
                            }

                            currentX += d0 * (double) step;
                            currentY += d1 * (double) step;
                            currentZ += d2 * (double) step;
                        }
                    }
                }
            }
        }

        this.getToBlow().addAll(hashset);

        if (!has(ExAttrib.NOHURT)) {
            float exprad = radius() * 2.0F;
            AABB box = getAabb(exprad);
            List<Entity> list = level.getEntities(this.exploder, box);

            // используем источник если есть, иначе центр взрыва
            Vec3 vec3 = new Vec3(sourceX, sourceY, sourceZ);

            for (Entity entity : list) {
                double d4 = Math.sqrt(entity.distanceToSqr(vec3)) / exprad;

                if (d4 <= 1.0D) {
                    currentX = entity.getX() - sourceX;
                    currentY = entity.getY() + (double) entity.getEyeHeight() - sourceY;
                    currentZ = entity.getZ() - sourceZ;
                    double d9 = Mth.square(currentX * currentX + currentY * currentY + currentZ * currentZ);

                    if (d9 != 0.0D) {
                        currentX /= d9;
                        currentY /= d9;
                        currentZ /= d9;
                        double d10 = Explosion.getSeenPercent(vec3, entity);
                        double d11 = (1.0D - d4) * d10;
                        entity.hurt(setExplosionSource(this.level, this),
                                (float) ((int) ((d11 * d11 + d11) / 2.0D * 8.0D * (double) exprad + 1.0D)));
                        Vec3 knockback = new Vec3(currentX, currentY, currentZ).scale(d11);
                        entity.setDeltaMovement(entity.getDeltaMovement().add(knockback));

                        if (entity instanceof Player player) {
                            Vec3 knockbackforhash = new Vec3(currentX * d11, currentY * d11, currentZ * d11);
                            this.affectedEntities.put(player, knockbackforhash);
                        }
                    }
                }
            }
        }
    }


    private AABB getAabb(float exprad) {
        assert this.getDirectSourceEntity() != null;
        int minX = Mth.floor(this.getDirectSourceEntity().getX() - exprad - 1.0D);
        int minY = Mth.floor(this.getDirectSourceEntity().getY() - exprad - 1.0D);
        int minZ = Mth.floor(this.getDirectSourceEntity().getZ() - exprad - 1.0D);
        int maxX = Mth.floor(this.getDirectSourceEntity().getX() + exprad + 1.0D);
        int maxY = Mth.floor(this.getDirectSourceEntity().getY() + exprad + 1.0D);
        int maxZ = Mth.floor(this.getDirectSourceEntity().getZ() + exprad + 1.0D);

        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static DamageSource setExplosionSource(Level level, Explosion explosion) {
        return explosion != null
                ? level.damageSources().explosion(explosion)
                : level.damageSources().explosion(null, null);
    }

    public void doExplosionB(boolean isSmoking) {

        if (!has(ExAttrib.NOSOUND)) {
            this.level.playSound(
                    null,
                    this.centerX, this.centerY, this.centerZ,
                    SoundEvents.GENERIC_EXPLODE,
                    SoundSource.BLOCKS,
                    4.0F,
                    (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F
            );
        }

        if (!has(ExAttrib.NOPARTICLE)) {
            if (this.radius() >= 2.0F) {
                level.addParticle(ParticleTypes.EXPLOSION_EMITTER, centerX, centerY, centerZ, 1.0D, 0.0D, 0.0D);
            } else {
                level.addParticle(ParticleTypes.EXPLOSION, centerX, centerY, centerZ, 1.0D, 0.0D, 0.0D);
            }
        }

        if (!isSmoking) return;

        for (BlockPos pos : this.getToBlow()) {
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();

            if (state.isAir()) continue;

            if (!has(ExAttrib.NOPARTICLE)) {
                double px = pos.getX() + level.random.nextDouble();
                double py = pos.getY() + level.random.nextDouble();
                double pz = pos.getZ() + level.random.nextDouble();
                double dx = px - centerX;
                double dy = py - centerY;
                double dz = pz - centerZ;
                double dist2 = dx * dx + dy * dy + dz * dz;
                if (dist2 < 1.0E-6D) dist2 = 1.0D;
                double scale = 0.5D / (Math.sqrt(dist2) / (double)this.radius() + 0.1D);
                scale *= level.random.nextFloat() * level.random.nextFloat() + 0.3F;
                dx *= scale; dy *= scale; dz *= scale;
                level.addParticle(ParticleTypes.EXPLOSION, px, py, pz, dx, dy, dz);
                level.addParticle(ParticleTypes.SMOKE, px, py, pz, dx, dy, dz);
            }

            block.onBlockExploded(state, level, pos, this);

            boolean doesErrode = false;
            Block erodeInto = Blocks.AIR;

            if (this.has(ExAttrib.ERRODE) && errosion.containsKey(block) && this.explosionRNG.nextFloat() < 0.6F) {
                doesErrode = true;
                erodeInto = errosion.get(block);
            }

            if (!has(ExAttrib.NODROP) && !doesErrode) {
                float dropChance = has(ExAttrib.ALLDROP) ? 1.0F : 1.0F / this.radius();
                if (level.random.nextFloat() <= dropChance) {
                    Block.dropResources(state, level, pos);
                }
            }

            if (doesErrode) {
                level.setBlock(pos, erodeInto.defaultBlockState(), 3);
            } else if (has(ExAttrib.DIGAMMA)) {
                level.setBlock(pos, ModBlocks.ASH_DIGAMMA.get().defaultBlockState(), 3);
                BlockPos above = pos.above();
                if (level.getBlockState(above).isAir() && explosionRNG.nextInt(5) == 0) {
                    level.setBlock(above, ModBlocks.FIRE_DIGAMMA.get().defaultBlockState(), 3);
                }
            } else if (has(ExAttrib.DIGAMMA_CIRCUIT)) {
                if (pos.getX() % 3 == 0 && pos.getZ() % 3 == 0) {
                    level.setBlock(pos, ModBlocks.PRIBRIS_DIGAMMA.get().defaultBlockState(), 3);
                } else if ((pos.getY() % 3 == 0 || pos.getZ() % 3 == 0) && explosionRNG.nextBoolean()) {
                    level.setBlock(pos, ModBlocks.PRIBRIS_DIGAMMA.get().defaultBlockState(), 3);
                } else {
                    level.setBlock(pos, ModBlocks.ASH_DIGAMMA.get().defaultBlockState(), 3);
                }
            } else if (has(ExAttrib.LAVA_V)) {
                level.setBlock(pos, ModBlocks.VOLCANIC_LAVA_BLOCK.get().defaultBlockState(), 3);
            } else if (has(ExAttrib.LAVA_R)) {
                level.setBlock(pos, ModBlocks.RAD_LAVA_BLOCK.get().defaultBlockState(), 3);
            } else {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
        }

        if (has(ExAttrib.FIRE) || has(ExAttrib.BALEFIRE) || has(ExAttrib.LAVA)) {
            for (BlockPos pos : this.getToBlow()) {
                BlockPos above = pos.above();
                if (!level.getBlockState(above).isAir()) continue;
                if (!has(ExAttrib.ALLMOD) && !has(ExAttrib.DIGAMMA) && explosionRNG.nextInt(3) != 0) continue;

                if (has(ExAttrib.FIRE)) {
                    level.setBlock(above, Blocks.FIRE.defaultBlockState(), 3);
                } else if (has(ExAttrib.BALEFIRE)) {
                    level.setBlock(above, ModBlocks.BALEFIRE.get().defaultBlockState(), 3);
                } else if (has(ExAttrib.LAVA)) {
                    level.setBlock(above, Blocks.LAVA.defaultBlockState(), 3);
                }
            }
        }
    }

    public Map getAffectedEntities() {
        return this.affectedEntities;
    }

    public LivingEntity getExplosivePlacedBy() {
        Entity indirect = this.getIndirectSourceEntity();
        if (indirect instanceof LivingEntity living) {
            return living;
        }

        Entity direct = this.getDirectSourceEntity();
        if (direct instanceof LivingEntity living) {
            return living;
        }

        return null;
    }

    // unconventional name, sure, but it's short
    public boolean has(ExAttrib attrib) {
        return this.atttributes.contains(attrib);
    }

    public double getCenterX() { return centerX; }
    public double getCenterY() { return centerY; }
    public double getCenterZ() { return centerZ; }

    //this solution is a bit hacky but in the end easier to work with
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

    public static final HashMap<Block, Block> errosion = new HashMap<>();

    static {
//        errosion.put(ModBlocks.concrete, Blocks.gravel);
//        errosion.put(ModBlocks.concrete_smooth, Blocks.gravel);
        errosion.put(ModBlocks.BRICK_CONCRETE.get(), ModBlocks.BRICK_CONCRETE_BROKEN.get());
        errosion.put(ModBlocks.BRICK_CONCRETE.get(), Blocks.GRAVEL);
    }
}