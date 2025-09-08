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
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ExplosionNT extends Explosion {

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
        if (exploder instanceof LivingEntity living) {
            this.exploder = living;
        }
    }

    public ExplosionNT addAttrib(ExAttrib attrib) {
        atttributes.add(attrib);
        return this;
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
        doExplosionB(false);
    }

    public void doExplosionA() {
        HashSet hashset = new HashSet();
        int i;
        int j;
        int k;
        double currentX;
        double currentY;
        double currentZ;

        for(i = 0; i < this.resolution; ++i) {
            for(j = 0; j < this.resolution; ++j) {
                for(k = 0; k < this.resolution; ++k) {

                    if(i == 0 || i == this.resolution - 1 || j == 0 || j == this.resolution - 1 || k == 0 || k == this.resolution - 1) {

                        double d0 = (float) i / ((float) this.resolution - 1.0F) * 2.0F - 1.0F;
                        double d1 = (float) j / ((float) this.resolution - 1.0F) * 2.0F - 1.0F;
                        double d2 = (float) k / ((float) this.resolution - 1.0F) * 2.0F - 1.0F;

                        double dist = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= dist;
                        d1 /= dist;
                        d2 /= dist;

                        float remainingPower = this.radius() * (0.7F + this.level.random.nextFloat() * 0.6F);
                        currentX = this.getDirectSourceEntity().getX();
                        currentY = this.getDirectSourceEntity().getY();
                        currentZ = this.getDirectSourceEntity().getZ();

                        for(float step = 0.3F; remainingPower > 0.0F; remainingPower -= step * 0.75F) {
                            int xPos = Mth.floor(currentX);
                            int yPos = Mth.floor(currentY);
                            int zPos = Mth.floor(currentZ);
                            BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                            BlockState state = level.getBlockState(blockPos);
                            Block b = state.getBlock();

                            if(!state.isAir()) {
                                float resistance = this.exploder != null ? this.exploder.getBlockExplosionResistance(this, this.level, blockPos, state, state.getFluidState(), remainingPower) : b.getExplosionResistance(state, level, blockPos, this);
                                remainingPower -= (resistance + 0.3F) * step;
                            }

                            if(!state.isAir() && remainingPower > 0.0F && (this.exploder == null || this.exploder.shouldBlockExplode(this, this.level, blockPos, b.defaultBlockState(), remainingPower))) {
                                hashset.add(blockPos);

                            } else if(this.has(ExAttrib.ERRODE) && errosion.containsKey(b)) {
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

        if(!has(ExAttrib.NOHURT)) {

            float exprad = radius() * 2.0F;
            AABB box = getAabb(exprad);
            List<Entity> list = level.getEntities(this.exploder, box);
            Vec3 vec3 = new Vec3(this.getDirectSourceEntity().getX(), this.getDirectSourceEntity().getY(), this.getDirectSourceEntity().getZ());


            for (Entity entity : list) {
                double d4 = Math.sqrt(entity.distanceToSqr(vec3)) / exprad;

                if (d4 <= 1.0D) {
                    currentX = entity.getX() - this.getDirectSourceEntity().getX();
                    currentY = entity.getY() + (double) entity.getEyeHeight() - this.getDirectSourceEntity().getY();
                    currentZ = entity.getZ() - this.getDirectSourceEntity().getZ();
                    double d9 = Mth.square(currentX * currentX + currentY * currentY + currentZ * currentZ);

                    if (d9 != 0.0D) {
                        currentX /= d9;
                        currentY /= d9;
                        currentZ /= d9;
                        double d10 = Explosion.getSeenPercent(vec3, entity);
                        double d11 = (1.0D - d4) * d10;
                        entity.hurt(setExplosionSource(this.level, this), (float) ((int) ((d11 * d11 + d11) / 2.0D * 8.0D * (double) exprad + 1.0D)));
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

    private @NotNull AABB getAabb(float exprad) {
        int minX = Mth.floor(this.getDirectSourceEntity().getX() - exprad - 1.0D);
        int minY = Mth.floor(this.getDirectSourceEntity().getY() - exprad - 1.0D);
        int minZ = Mth.floor(this.getDirectSourceEntity().getZ() - exprad - 1.0D);
        int maxX = Mth.floor(this.getDirectSourceEntity().getX() + exprad + 1.0D);
        int maxY = Mth.floor(this.getDirectSourceEntity().getY() + exprad + 1.0D);
        int maxZ = Mth.floor(this.getDirectSourceEntity().getZ() + exprad + 1.0D);

        AABB box = new AABB(minX, minY, minZ, maxX, maxY, maxZ);
        return box;
    }

    public static DamageSource setExplosionSource(Level level, @Nullable Explosion explosion) {
        return explosion != null
                ? level.damageSources().explosion(explosion)
                : level.damageSources().explosion(null, null);
    }
    public void doExplosionB(boolean isSmoking) {

        if(!has(ExAttrib.NOSOUND))
            this.level.playSound(
                    null,
                    this.getDirectSourceEntity().getX(),
                    this.getDirectSourceEntity().getY(),
                    this.getDirectSourceEntity().getZ(),
                    SoundEvents.GENERIC_EXPLODE,
                    SoundSource.BLOCKS,
                    4.0F,
                    (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F
            );

        if(!has(ExAttrib.NOPARTICLE)) {
            if (this.radius() >= 2.0F) {
                level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getDirectSourceEntity().getX(), this.getDirectSourceEntity().getY(), this.getDirectSourceEntity().getZ(), 1.0D, 0.0D, 0.0D);
            } else {
                level.addParticle(ParticleTypes.EXPLOSION, this.getDirectSourceEntity().getX(), this.getDirectSourceEntity().getY(), this.getDirectSourceEntity().getZ(), 1.0D, 0.0D, 0.0D);
            }
        }

        if (isSmoking) {
            for (BlockPos pos : this.getToBlow()) {
                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();

                if(!has(ExAttrib.NOPARTICLE)) {
                    double d0 = (float) pos.getX() + this.level.random.nextFloat();
                    double d1 = (float) pos.getY() + this.level.random.nextFloat();
                    double d2 = (float) pos.getZ() + this.level.random.nextFloat();
                    double d3 = d0 - this.getDirectSourceEntity().getX();
                    double d4 = d1 - this.getDirectSourceEntity().getY();
                    double d5 = d2 - this.getDirectSourceEntity().getZ();
                    double d6 = Mth.square(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d7 = 0.5D / (d6 / (double) this.radius() + 0.1D);
                    d7 *= this.level.random.nextFloat() * this.level.random.nextFloat() + 0.3F;
                    d3 *= d7;
                    d4 *= d7;
                    d5 *= d7;
                    this.level.addParticle(ParticleTypes.EXPLOSION, (d0 + pos.getX() * 1.0D) / 2.0D, (d1 + pos.getY() * 1.0D) / 2.0D, (d2 + pos.getZ() * 1.0D) / 2.0D, d3, d4, d5);
                    this.level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, d3, d4, d5);
                }

                if(!state.isAir()) {

                    boolean doesErrode = false;
                    Block errodesInto = Blocks.AIR;

                    if(this.has(ExAttrib.ERRODE) && this.explosionRNG.nextFloat() < 0.6F) { //errosion has a 60% chance to occour

                        if(errosion.containsKey(block)) {
                            doesErrode = true;
                            errodesInto = errosion.get(block);
                        }
                    }

                    if(block.canDropFromExplosion(state, level, pos, this) && !has(ExAttrib.NODROP) && !doesErrode) {
                        float chance = this.has(ExAttrib.ALLDROP) ? 1.0F : 1.0F / this.radius();
                        Block.dropResources(state, level, pos);

                        if (chance == level.random.nextFloat()) {
                            Block.dropResources(state, level, pos);
                        }
                    }

                    block.onBlockExploded(state, level, pos, this);

                    if(state.isSolidRender(level, pos)) {

                        if (doesErrode) {
                            this.level.setBlock(pos, errodesInto.defaultBlockState(), 3);
                        }

                        if(has(ExAttrib.DIGAMMA)) {
                            this.level.setBlock(pos, ModBlocks.ASH_DIGAMMA.get().defaultBlockState(), 3);
                            BlockPos above = pos.above();
                            if(this.explosionRNG.nextInt(5) == 0 && this.level.getBlockState(above).isAir())
                                this.level.setBlock(above, ModBlocks.FIRE_DIGAMMA.get().defaultBlockState(), 3);

                        } else if(has(ExAttrib.DIGAMMA_CIRCUIT)) {

                            if(pos.getX() % 3 == 0 && pos.getZ() % 3 == 0) {
                                this.level.setBlock(pos, ModBlocks.PRIBRIS_DIGAMMA.get().defaultBlockState(), 3);
                            } else if((pos.getY() % 3 == 0 || pos.getZ() % 3 == 0) && this.explosionRNG.nextBoolean()) {
                                this.level.setBlock(pos, ModBlocks.PRIBRIS_DIGAMMA.get().defaultBlockState(), 3);
                            } else {
                                this.level.setBlock(pos, ModBlocks.ASH_DIGAMMA.get().defaultBlockState(), 3);

                                BlockPos above = pos.above();
                                if(this.explosionRNG.nextInt(5) == 0 && this.level.getBlockState(above).isAir())
                                    this.level.setBlock(above, ModBlocks.FIRE_DIGAMMA.get().defaultBlockState(), 3);
                            }
                        } else if(has(ExAttrib.LAVA_V)) {
                            this.level.setBlock(pos, ModBlocks.VOLCANIC_LAVA_BLOCK.get().defaultBlockState(), 3);
                        } else if(has(ExAttrib.LAVA_R)) {
                            this.level.setBlock(pos, ModBlocks.RAD_LAVA_BLOCK.get().defaultBlockState(), 3);
                        }
                    }
                }
            }
        }

        if(has(ExAttrib.FIRE) || has(ExAttrib.BALEFIRE) || has(ExAttrib.LAVA)) {

            for (BlockPos pos : this.getToBlow()) {

                BlockState state = level.getBlockState(pos);

                boolean shouldReplace = true;

                if(!has(ExAttrib.ALLMOD) && !has(ExAttrib.DIGAMMA))
                    shouldReplace = this.explosionRNG.nextInt(3) == 0;

                if(state.isAir() && state.isSolidRender(level, pos) && shouldReplace) {
                    if(has(ExAttrib.FIRE))
                        this.level.setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
                    else if(has(ExAttrib.BALEFIRE))
                        this.level.setBlock(pos, ModBlocks.BALEFIRE.get().defaultBlockState(), 3);
                    else if(has(ExAttrib.LAVA))
                        this.level.setBlock(pos, Blocks.LAVA.defaultBlockState(), 3);
                }
            }
        }
    }

    public Map getAffectedEntities() {
        return this.affectedEntities;
    }

    @Nullable
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