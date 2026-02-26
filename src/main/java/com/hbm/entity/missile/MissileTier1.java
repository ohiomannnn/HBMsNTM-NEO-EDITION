package com.hbm.entity.missile;

import api.hbm.entity.IRadarDetectableNT;
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

public abstract class MissileTier1 extends MissileBaseNT {

    public MissileTier1(EntityType<? extends MissileTier1> entityType, Level level) { super(entityType, level); }

    @Override
    public List<ItemStack> getDebris() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(ModItems.NOTHING.get(), 4));
        list.add(new ItemStack(ModItems.NOTHING.get(), 1));
        return list;
    }

    @Override
    protected float getContrailScale() {
        return 0.5F;
    }

    public static class MissileGeneric extends MissileTier1 {
        public MissileGeneric(EntityType<? extends MissileGeneric> entityType, Level level) { super(entityType, level); }
        @Override public void onMissileImpact(RayTraceResult mop) { ExplosionCreator.composeEffectSmall(level, this.position.x, this.position.y, this.position.z); this.explodeStandard(15F, 24, false); }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_GENERIC.get()); }
    }

    public static class MissileDecoy extends MissileTier1 {
        public MissileDecoy(EntityType<? extends MissileDecoy> entityType, Level level) { super(entityType, level); }
        @Override public void onMissileImpact(RayTraceResult mop) { ExplosionVNT.newExplosion(level, null, this.position.x, this.position.y, this.position.z, 4F, false, false); }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public String getUnlocalizedName() { return "radar.target.tier4"; }
        @Override public int getBlipLevel() { return IRadarDetectableNT.TIER4; }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_DECOY.get()); }
    }

    public static class MissileIncendiary extends MissileTier1 {
        public MissileIncendiary(EntityType<? extends MissileIncendiary> entityType, Level level) { super(entityType, level); }
        @Override public void onMissileImpact(RayTraceResult mop) { ExplosionCreator.composeEffectSmall(level, this.position.x, this.position.y, this.position.z); this.explodeStandard(15F, 24, true); }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_INCENDIARY.get()); }
    }

    public static class MissileCluster extends MissileTier1 {
        public MissileCluster(EntityType<? extends MissileCluster> entityType, Level level) { super(entityType, level); this.isCluster = true; }
        @Override public void onMissileImpact(RayTraceResult mop) {
            ExplosionVNT.createExplosion(level, this, this.position.x, this.position.y, this.position.z, 5F, true);
            ExplosionChaos.cluster(level, this.position.x, this.position.y, this.position.z, 25);
        }
        @Override public void cluster() { this.onMissileImpact(null); }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_CLUSTER.get()); }
    }

    public static class MissileBunkerBuster extends MissileTier1 {
        public MissileBunkerBuster(EntityType<? extends MissileBunkerBuster> entityType, Level level) { super(entityType, level); }
        @Override public void onMissileImpact(RayTraceResult mop) {
            for (int i = 0; i < 15; i++) ExplosionVNT.createExplosion(level, this, this.position.x, this.position.y - i, this.position.z, 5F, true);
            if (level instanceof ServerLevel serverLevel) {
                ExplosionLarge.spawnParticles(serverLevel, this.position.x, this.position.y, this.position.z, 5);
                ExplosionLarge.spawnShrapnels(serverLevel, this.position.x, this.position.y, this.position.z, 5);
                ExplosionLarge.spawnRubble(serverLevel, this.position.x, this.position.y, this.position.z, 5);
            }
        }
        @Override public void cluster() { this.onMissileImpact(null); }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.MISSILE_BUSTER.get()); }
    }
}
