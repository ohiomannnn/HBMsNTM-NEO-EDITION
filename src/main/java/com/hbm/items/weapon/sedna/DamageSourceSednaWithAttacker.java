package com.hbm.items.weapon.sedna;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class DamageSourceSednaWithAttacker extends DamageSourceSednaNoAttacker {

    public Entity projectile;
    public Entity shooter;

    public DamageSourceSednaWithAttacker(String name, Entity projectile, Entity shooter) {
        super(shooter.level(), name);
        this.projectile = projectile;
        this.shooter = shooter;
    }

    @Override public Entity getDirectEntity() { return this.projectile; }
    @Override public Entity getEntity() { return this.shooter; }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity livingEntity) {
        Component diedName = livingEntity.getDisplayName();
        Component shooterName = shooter != null ? shooter.getDisplayName() : Component.literal("Unknown").setStyle(Style.EMPTY.withObfuscated(true));
        return Component.translatable("death.sedna." + this.type() + ".attacker", diedName, shooterName);
    }
}
