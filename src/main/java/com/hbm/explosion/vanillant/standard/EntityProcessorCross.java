package com.hbm.explosion.vanillant.standard;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.interfaces.ICustomDamageHandler;
import com.hbm.explosion.vanillant.interfaces.IEntityProcessor;
import com.hbm.explosion.vanillant.interfaces.IEntityRangeMutator;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/** The amount of good decisions in NTM is few and far between, but the VNT explosion surely is one of them. */
public class EntityProcessorCross implements IEntityProcessor {

    protected double nodeDist;
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
    public HashMap<Player, Vec3> processEntities(ExplosionVNT explosion, Level level, double x, double y, double z, float size) {

        HashMap<Player, Vec3> affectedPlayers = new HashMap<>();

        size *= 2.0F;

        if (range != null) {
            size = range.mutateRange(explosion, size);
        }

        double minX = x - (double) size - 1.0D;
        double maxX = x + (double) size + 1.0D;
        double minY = y - (double) size - 1.0D;
        double maxY = y + (double) size + 1.0D;
        double minZ = z - (double) size - 1.0D;
        double maxZ = z + (double) size + 1.0D;

        List<Entity> list = level.getEntities(allowSelfDamage ? null : explosion.exploder, new AABB(minX, minY, minZ, maxX, maxY, maxZ));
        EventHooks.onExplosionDetonate(level, explosion.compat, list, size);

        Vec3[] nodes = new Vec3[7];

        for (int i = 0; i < 7; i++) {
            Direction dir = Direction.from3DDataValue(i);
            nodes[i] = new Vec3(x + dir.getStepX() * nodeDist, y + dir.getStepY() * nodeDist, z + dir.getStepZ() * nodeDist);
        }

        HashMap<Entity, Float> damageMap = new HashMap<>();

        for (Entity entity : list) {

            AABB entityBoundingBox = entity.getBoundingBox();
            double xDist = (entityBoundingBox.minX <= x && entityBoundingBox.maxX >= x) ? 0 : Math.min(Math.abs(entityBoundingBox.minX - x), Math.abs(entityBoundingBox.maxX - x));
            double yDist = (entityBoundingBox.minY <= y && entityBoundingBox.maxY >= y) ? 0 : Math.min(Math.abs(entityBoundingBox.minY - y), Math.abs(entityBoundingBox.maxY - y));
            double zDist = (entityBoundingBox.minZ <= z && entityBoundingBox.maxZ >= z) ? 0 : Math.min(Math.abs(entityBoundingBox.minZ - z), Math.abs(entityBoundingBox.maxZ - z));
            double dist = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
            double distanceScaled = dist / size;


            if (distanceScaled <= 1.0D) {
                double deltaX = entity.getX() - x;
                double deltaY = entity.getY() + entity.getEyeHeight() - y;
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

                    double knockback = density * (1.0D - distanceScaled);

                    float dmg = calculateDamage(distanceScaled, density, knockback, size);
                    if (!damageMap.containsKey(entity) || damageMap.get(entity) < dmg) damageMap.put(entity, dmg);

                    if (entity instanceof LivingEntity livingEntity) {
                        knockback = density * (1D - livingEntity.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE));
                    } else {
                        knockback = density;
                    }

                    deltaX *= knockback * knockbackMult;
                    deltaY *= knockback * knockbackMult;
                    deltaZ *= knockback * knockbackMult;

                    Vec3 velocity = new Vec3(deltaX, deltaY, deltaZ);

                    if (entity instanceof Player player) {
                        if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
                            affectedPlayers.put(player, velocity);
                        }
                    }
                }
            }
        }

        for (Entry<Entity, Float> entry : damageMap.entrySet()) {

            Entity entity = entry.getKey();
            attackEntity(entity, explosion, entry.getValue());

            if (damage != null) {
                AABB entityBoundingBox = entity.getBoundingBox();
                double xDist = (entityBoundingBox.minX <= x && entityBoundingBox.maxX >= x) ? 0 : Math.min(Math.abs(entityBoundingBox.minX - x), Math.abs(entityBoundingBox.maxX - x));
                double yDist = (entityBoundingBox.minY <= y && entityBoundingBox.maxY >= y) ? 0 : Math.min(Math.abs(entityBoundingBox.minY - y), Math.abs(entityBoundingBox.maxY - y));
                double zDist = (entityBoundingBox.minZ <= z && entityBoundingBox.maxZ >= z) ? 0 : Math.min(Math.abs(entityBoundingBox.minZ - z), Math.abs(entityBoundingBox.maxZ - z));
                double dist = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
                double distanceScaled = dist / size;
                damage.handleAttack(explosion, entity, distanceScaled);
            }
        }

        return affectedPlayers;
    }


    public void attackEntity(Entity entity, ExplosionVNT source, float amount) {
        entity.hurt(setExplosionSource(entity.level(), source.compat), amount);
    }

    public float calculateDamage(double distanceScaled, double density, double knockback, float size) {
        return (float) ((int) ((knockback * knockback + knockback) / 2.0D * 8.0D * size + 1.0D));
    }

    public static DamageSource setExplosionSource(Level level, Explosion explosion) {
        DamageSources sources = level.damageSources();
        Entity causing = explosion.getIndirectSourceEntity();
        Entity direct = explosion.getDirectSourceEntity();
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
