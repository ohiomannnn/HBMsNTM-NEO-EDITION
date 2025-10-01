package com.hbm.explosion.vanillant.standard;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.interfaces.ICustomDamageHandler;
import com.hbm.explosion.vanillant.interfaces.IEntityProcessor;
import com.hbm.explosion.vanillant.interfaces.IEntityRangeMutator;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityProcessorCross implements IEntityProcessor {

    protected double nodeDist = 2D;
    protected IEntityRangeMutator range;
    protected ICustomDamageHandler damage;
    protected double knockbackMult = 1D;
    protected boolean allowSelfDamage = false;

    public EntityProcessorCross(double nodeDist) {
        this.nodeDist = nodeDist;
    }

    public EntityProcessorCross setAllowSelfDamage() {
        this.allowSelfDamage = true;
        return this;
    }

    public EntityProcessorCross setKnockback(double mult) {
        this.knockbackMult = mult;
        return this;
    }

    @Override
    public Map<Player, Vec3> processEntities(ExplosionVNT explosion, Level level, double x, double y, double z, float size) {

        Map<Player, Vec3> affectedPlayers = new HashMap<>();

        size *= 2.0F;

        if (range != null) {
            size = range.mutateRange(explosion, size);
        }

        double minX = x - size - 1.0D;
        double maxX = x + size + 1.0D;
        double minY = y - size - 1.0D;
        double maxY = y + size + 1.0D;
        double minZ = z - size - 1.0D;
        double maxZ = z + size + 1.0D;

        AABB bounds = new AABB(minX, minY, minZ, maxX, maxY, maxZ);

        List<Entity> entities = level.getEntities(explosion.exploder, bounds);

        net.neoforged.neoforge.event.EventHooks.onExplosionDetonate(level, explosion.compat, entities, size);

        Vec3[] nodes = new Vec3[7];
        for (int i = 0; i < 7; i++) {
            Direction dir = Direction.from3DDataValue(i);
            nodes[i] = new Vec3(
                    x + dir.getStepX() * nodeDist,
                    y + dir.getStepY() * nodeDist,
                    z + dir.getStepZ() * nodeDist
            );
        }

        Map<Entity, Float> damageMap = new HashMap<>();

        for (Entity entity : entities) {
            AABB bb = entity.getBoundingBox();

            double xDist = (bb.minX <= x && bb.maxX >= x) ? 0 : Math.min(Math.abs(bb.minX - x), Math.abs(bb.maxX - x));
            double yDist = (bb.minY <= y && bb.maxY >= y) ? 0 : Math.min(Math.abs(bb.minY - y), Math.abs(bb.maxY - y));
            double zDist = (bb.minZ <= z && bb.maxZ >= z) ? 0 : Math.min(Math.abs(bb.minZ - z), Math.abs(bb.maxZ - z));

            double dist = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
            double distanceScaled = dist / size;

            if (distanceScaled <= 1.0D) {
                double deltaX = entity.getX() - x;
                double deltaY = entity.getEyeY() - y;
                double deltaZ = entity.getZ() - z;
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

                if (distance != 0.0D) {
                    deltaX /= distance;
                    deltaY /= distance;
                    deltaZ /= distance;

                    double density = 0.0D;
                    for (Vec3 vec : nodes) {
                        double d = Explosion.getSeenPercent(vec, entity);
                        if (d > density) {
                            density = d;
                        }
                    }

                    double knockback = (1.0D - distanceScaled) * density;

                    float dmg = calculateDamage(distanceScaled, density, knockback, size);
                    damageMap.merge(entity, dmg, Math::max);

                    Holder<Enchantment> blast = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.BLAST_PROTECTION);

                    int blastProtectionLevel = 0;
                    if (entity instanceof LivingEntity living) {
                        for (ItemStack stack : living.getArmorSlots()) {
                            blastProtectionLevel += EnchantmentHelper.getTagEnchantmentLevel(blast, stack);
                        }
                    }

//                    if (!(entity instanceof EntityBulletBaseMK4)) {
//                        Vec3 motion = entity.getDeltaMovement().add(
//                                deltaX * blastProtectionLevel * knockbackMult,
//                                deltaY * blastProtectionLevel * knockbackMult,
//                                deltaZ * blastProtectionLevel * knockbackMult
//                        );
//                        entity.setDeltaMovement(motion);
//                    }

                    if (entity instanceof ServerPlayer player) {
                        affectedPlayers.put(player, new Vec3(
                                deltaX * knockback * knockbackMult,
                                deltaY * knockback * knockbackMult,
                                deltaZ * knockback * knockbackMult
                        ));
                    }
                }
            }
        }

        for (Map.Entry<Entity, Float> entry : damageMap.entrySet()) {
            Entity entity = entry.getKey();
            attackEntity(entity, explosion, entry.getValue());

            if (damage != null) {
                AABB bb = entity.getBoundingBox();
                double xDist = (bb.minX <= x && bb.maxX >= x) ? 0 : Math.min(Math.abs(bb.minX - x), Math.abs(bb.maxX - x));
                double yDist = (bb.minY <= y && bb.maxY >= y) ? 0 : Math.min(Math.abs(bb.minY - y), Math.abs(bb.maxY - y));
                double zDist = (bb.minZ <= z && bb.maxZ >= z) ? 0 : Math.min(Math.abs(bb.minZ - z), Math.abs(bb.maxZ - z));
                double dist = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
                double distanceScaled = dist / size;
                damage.handleAttack(explosion, entity, distanceScaled);
            }
        }

        return affectedPlayers;
    }


    public void attackEntity(Entity entity, ExplosionVNT source, float amount) {
        entity.hurt(setExplosionSource((ServerLevel) entity.level(), source.compat), amount);
    }

    public float calculateDamage(double distanceScaled, double density, double knockback, float size) {
        return (float) ((int) ((knockback * knockback + knockback) / 2.0D * 8.0D * size + 1.0D));
    }

    public float calculateDamage(double knockback, float size) {
        return (float) ((int) ((knockback * knockback + knockback) / 2.0D * 8.0D * size + 1.0D));
    }

    public static DamageSource setExplosionSource(ServerLevel level, Explosion explosion) {
        DamageSources sources = level.damageSources();
        Entity causing = explosion.getIndirectSourceEntity();
        Entity direct  = explosion.getDirectSourceEntity();
        return sources.explosion(causing, direct);
    }


    public EntityProcessorCross withRangeMod(float mod) {
        range = new IEntityRangeMutator() {
            @Override
            public float mutateRange(ExplosionVNT explosion, float range) {
                return range * mod;
            }
        };
        return this;
    }

    public EntityProcessorCross withDamageMod(ICustomDamageHandler damage) {
        this.damage = damage;
        return this;
    }
}
