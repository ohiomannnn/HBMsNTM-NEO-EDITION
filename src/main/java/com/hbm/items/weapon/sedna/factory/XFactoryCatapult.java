package com.hbm.items.weapon.sedna.factory;

import com.hbm.entity.projectile.BulletBaseMK4;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.explosion.vanillant.standard.EntityProcessorCrossSmooth;
import com.hbm.explosion.vanillant.standard.ExplosionEffectWeapon;
import com.hbm.items.weapon.sedna.BulletConfig;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;

public class XFactoryCatapult {

    public static BulletConfig cluster_submunition;

    public static BiConsumer<BulletBaseMK4, HitResult> LAMBDA_SUBMUNITION = (bullet, hr) -> {
        Vec3 position = hr.getLocation();
        ExplosionVNT vnt = new ExplosionVNT(bullet.level, position.x, position.y, position.z, 7.5F, bullet.getOwner());
        vnt.setBlockAllocator(new BlockAllocatorStandard());
        vnt.setBlockProcessor(new BlockProcessorStandard());
        vnt.setEntityProcessor(new EntityProcessorCrossSmooth(1, bullet.damage));
        vnt.setSFX(new ExplosionEffectWeapon(10, 2.5F, 1F));
        vnt.explode();
        bullet.discard();
    };

    public static void init() {

        cluster_submunition = new BulletConfig().setLife(1_200).setGrav(0.025F).setOnImpact(LAMBDA_SUBMUNITION);

    }
}
