package com.hbm.entity.missile;

import api.hbm.entity.IRadarDetectableNT;
import com.hbm.entity.ModEntityTypes;
import com.hbm.entity.logic.EMP;
import com.hbm.explosion.ExplosionChaos;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.items.ModItems;
import com.hbm.particle.helper.ExplosionCreator;
import com.hbm.util.RayTraceResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class MissileTier2 extends MissileBaseNT {

    public MissileTier2(EntityType<? extends MissileTier2> entityType, Level level) { super(entityType, level); }

    @Override
    public List<ItemStack> getDebris() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(ModItems.NOTHING.get(), 10));
        list.add(new ItemStack(ModItems.NOTHING.get(), 6));
        list.add(new ItemStack(ModItems.NOTHING.get(), 1));
        return list;
    }

    @Override
    public String getUnlocalizedName() {
        return "radar.target.tier2";
    }

    @Override
    public int getBlipLevel() {
        return IRadarDetectableNT.TIER2;
    }

    public static class MissileStrong extends MissileTier2 {
        public MissileStrong(EntityType<? extends MissileStrong> entityType, Level level) { super(entityType, level); }
        @Override public void onMissileImpact(RayTraceResult mop) { ExplosionCreator.composeEffectStandard(level, this.position.x, this.position.y, this.position.z); this.explodeStandard(30F, 32, false); }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_STRONG.get()); }
    }

    public static class MissileIncendiaryStrong extends MissileTier2 {
        public MissileIncendiaryStrong(EntityType<? extends MissileIncendiaryStrong> entityType, Level level) { super(entityType, level); }
        @Override public void onMissileImpact(RayTraceResult mop) {
            ExplosionCreator.composeEffectStandard(level, this.position.x, this.position.y, this.position.z);
            this.explodeStandard(30F, 32, true);
            ExplosionChaos.flameDeath(level, (int) (this.position.x + 0.5), (int) (this.position.y + 0.5), (int) (this.position.z + 0.5), 25);
        }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_INCENDIARY_STRONG.get()); }
    }

    public static class MissileClusterStrong extends MissileTier2 {
        public MissileClusterStrong(EntityType<? extends MissileClusterStrong> entityType, Level level) { super(entityType, level); this.isCluster = true; }
        @Override public void onMissileImpact(RayTraceResult mop) {
            ExplosionVNT.createExplosion(level, this, this.position.x, this.position.y, this.position.z, 15F, true);
            ExplosionChaos.cluster(level, this.position.x, this.position.y, this.position.z, 50);
        }
        @Override public void cluster() { this.onMissileImpact(null); }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_CLUSTER_STRONG.get()); }
    }

    public static class MissileBusterStrong extends MissileTier2 {
        public MissileBusterStrong(EntityType<? extends MissileBusterStrong> entityType, Level level) { super(entityType, level); }
        @Override public void onMissileImpact(RayTraceResult mop) {
            for (int i = 0; i < 20; i++) ExplosionVNT.createExplosion(level, this, this.position.x, this.position.y - i, this.position.z, 7.5F, true);
            if (level instanceof ServerLevel serverLevel) {
                ExplosionLarge.spawnParticles(serverLevel, this.position.x, this.position.y, this.position.z, 8);
                ExplosionLarge.spawnShrapnels(serverLevel, this.position.x, this.position.y, this.position.z, 8);
                ExplosionLarge.spawnRubble(serverLevel, this.position.x, this.position.y, this.position.z, 8);
            }
        }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_BUSTER_STRONG.get()); }
    }

    public static class MissileEMPStrong extends MissileTier2 {
        public MissileEMPStrong(EntityType<? extends MissileEMPStrong> entityType, Level level) { super(entityType, level); }
        @Override public void onMissileImpact(RayTraceResult mop) {
            EMP emp = new EMP(ModEntityTypes.EMP.get(), this.level);
            emp.setPos(this.position);
            level.addFreshEntity(emp);
        }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_EMP_STRONG.get()); }
    }
}
