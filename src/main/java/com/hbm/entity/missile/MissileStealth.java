package com.hbm.entity.missile;

import com.hbm.items.NtmItems;
import com.hbm.particle.helper.ExplosionCreator;
import com.hbm.util.RayTraceResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class MissileStealth extends MissileBaseNT {

    public MissileStealth(EntityType<? extends MissileBaseNT> entityType, Level level) { super(entityType, level); }

    @Override
    public List<ItemStack> getDebris() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(NtmItems.NOTHING.get(), 4));
        return list;
    }

    @Override public ItemStack getMissileItemForInfo() { return new ItemStack(NtmItems.MISSILE_STEALTH.get()); }
    @Override public boolean canBeSeenBy(Object radar) { return false; }

    @Override public void onMissileImpact(RayTraceResult result) { this.explodeStandard(20F, 24, false); ExplosionCreator.composeEffectStandard(this.level, this.position.x, this.position.y, this.position.z); }
    @Override public ItemStack getDebrisRareDrop() { return new ItemStack(NtmItems.NOTHING.get(), 1); }
}
