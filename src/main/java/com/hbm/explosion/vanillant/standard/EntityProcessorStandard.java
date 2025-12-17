package com.hbm.explosion.vanillant.standard;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.interfaces.ICustomDamageHandler;
import com.hbm.explosion.vanillant.interfaces.IEntityProcessor;
import com.hbm.explosion.vanillant.interfaces.IEntityRangeMutator;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
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

public class EntityProcessorStandard implements IEntityProcessor {

    protected IEntityRangeMutator range;
    protected ICustomDamageHandler damage;
    protected boolean allowSelfDamage = false;

    @Override
    public HashMap<Player, Vec3> process(ExplosionVNT explosion, Level level, double x, double y, double z, float size) {

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

        List<Entity> entities = level.getEntities(allowSelfDamage ? null : explosion.exploder, new AABB(minX, minY, minZ, maxX, maxY, maxZ));
        EventHooks.onExplosionDetonate(level, explosion.compat, entities, size);
        Vec3 vec3 = new Vec3(x, y, z);

        for (Entity entity : entities) {

            double distanceScaled = entity.distanceToSqr(x, y, z) / size;

            if (distanceScaled <= 1.0D) {

                double deltaX = entity.getX() - x;
                double deltaY = entity.getEyeY() - y;
                double deltaZ = entity.getZ() - z;
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

                if (distance != 0.0D) {

                    deltaX /= distance;
                    deltaY /= distance;
                    deltaZ /= distance;

                    double density = Explosion.getSeenPercent(vec3, entity);
                    double knockback;

                    if (entity instanceof LivingEntity livingEntity) {
                        knockback = density * (1D - livingEntity.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE));
                    } else {
                        knockback = density;
                    }

                    entity.hurt(setExplosionSource(level, explosion.compat), calculateDamage(distanceScaled, density, knockback, size));

                    deltaX *= knockback;
                    deltaY *= knockback;
                    deltaZ *= knockback;

                    Vec3 velocity = new Vec3(deltaX, deltaY, deltaZ);

                    if (entity instanceof Player player) {
                        if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
                            affectedPlayers.put(player, velocity);
                        }
                    }

                    if (damage != null) {
                        damage.handleAttack(explosion, entity, distanceScaled);
                    }
                }
            }
        }

        return affectedPlayers;
    }

    public float calculateDamage(double distanceScaled, double density, double knockback, float size) {
        return (float) ((int) ((knockback * knockback + knockback) / 2.0D * 8.0D * size + 1.0D));
    }

    public static DamageSource setExplosionSource(Level level, Explosion explosion) {
        DamageSources sources = level.damageSources();
        Entity causing = explosion.getIndirectSourceEntity();
        Entity direct  = explosion.getDirectSourceEntity();
        return sources.explosion(causing, direct);
    }

    public EntityProcessorStandard withRangeMod(float mod) {
        range = new IEntityRangeMutator() {
            @Override
            public float mutateRange(ExplosionVNT explosion, float range) {
                return range * mod;
            }
        };
        return this;
    }

    public EntityProcessorStandard withDamageMod(ICustomDamageHandler damage) {
        this.damage = damage;
        return this;
    }

    public EntityProcessorStandard allowSelfDamage() {
        this.allowSelfDamage = true;
        return this;
    }
}
