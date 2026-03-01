package com.hbm.explosion;

import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ExplosionHurtUtil {
    /**
     * Adds radiation to entities in an AoE
     */
    public static void doRadiation(Level level, double x, double y, double z, float outer, float inner, double radius) {

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));

        for (LivingEntity entity : entities) {
            Vec3 vec = new Vec3(x - entity.position.x, y - entity.position.y, z - entity.position.z);

            double dist = vec.length();

            if (dist > radius) continue;

            double interpolation = 1.0 - (dist / radius);
            float rad = (float) (outer + (inner - outer) * interpolation);

            ContaminationUtil.contaminate(entity, HazardType.RADIATION, ContaminationType.CREATIVE, rad);
        }
    }
}
