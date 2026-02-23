package com.hbm.explosion;

import com.hbm.entity.ModEntityTypes;
import com.hbm.entity.projectile.Rocket;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExplosionChaos {

    public static void cluster(Level level, double x, double y, double z, int count) {

        double d1;
        double d2;
        double d3;
        Rocket fragment;

        for (int i = 0; i < count; i++) {
            d1 = level.random.nextDouble();
            d2 = level.random.nextDouble();
            d3 = level.random.nextDouble();

            if (level.random.nextInt(2) == 0) {
                d1 *= -1;
            }

            if (level.random.nextInt(2) == 0) {
                d3 *= -1;
            }

            fragment = new Rocket(ModEntityTypes.ROCKET.get(), level);
            fragment.setPos(x + 0.5, y + 0.5, z + 0.5);
            fragment.deltaMovement = new Vec3(d1, d2, d3);
            fragment.gravity = 0.0125D;

            level.addFreshEntity(fragment);
        }
    }
}
