package com.hbm.entity.missile;

import com.hbm.items.ModItems;
import com.hbm.particle.helper.ExplosionCreator;
import com.hbm.util.RayTraceResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class MissileTier1 extends MissileBaseNT {
    public MissileTier1(EntityType<? extends MissileTier1> entityType, Level level) { super(entityType, level); }
    public MissileTier1(EntityType<? extends MissileTier1> entityType, Level level, double x, double y, double z, int a, int b) { super(entityType, level, x, y, z, a, b); }

    @Override
    public List<ItemStack> getDebris() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(ModItems.NOTHING.get(), 4));
        return list;
    }

    @Override
    protected float getContrailScale() {
        return 0.5F;
    }

    public static class MissileGeneric extends MissileTier1 {
        public MissileGeneric(EntityType<? extends MissileGeneric> entityType, Level level) { super(entityType, level); }
        public MissileGeneric(EntityType<? extends MissileGeneric> entityType, Level level, double x, double y, double z, int a, int b) { super(entityType, level, x, y, z, a, b); }
        @Override public void onMissileImpact(RayTraceResult mop) { this.explodeStandard(15F, 24, false); ExplosionCreator.composeEffectSmall(level(), getX(), getY(), getZ()); }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.NOTHING.get()); }
    }
}
