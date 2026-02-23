package com.hbm.entity.missile;

import com.hbm.config.MainConfig;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.items.ModItems;
import com.hbm.particle.helper.ExplosionCreator;
import com.hbm.particle.helper.NukeTorexCreator;
import com.hbm.util.RayTraceResult;
import com.hbm.util.Vec3NT;
import com.hbm.world.WorldUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public abstract class MissileTier4 extends MissileBaseNT {
    public MissileTier4(EntityType<? extends MissileTier4> entityType, Level level) { super(entityType, level); }

    @Override
    public List<ItemStack> getDebris() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(ModItems.NOTHING.get(), 4));
        return list;
    }

    @Override
    protected void spawnContrail() {
        Direction dir = this.getEntityData().get(MissileBaseNT.ROT);

        Vec3NT thrust = new Vec3NT(0, 0, 1);
        switch (dir) {
            case WEST -> thrust.rotateAroundYDeg((float) -Math.PI / 2F);
            case SOUTH -> thrust.rotateAroundYDeg((float) -Math.PI);
            case EAST -> thrust.rotateAroundYDeg((float) -Math.PI / 2F * 3F);
        }
        thrust.rotateAroundYDeg((this.yRot + 90) * (float) Math.PI / 180F);
        thrust.rotateAroundXDeg(this.xRot * (float) Math.PI / 180F);
        thrust.rotateAroundYDeg(-(this.yRot + 90) * (float) Math.PI / 180F);

        this.spawnContraolWithOffset(thrust.xCoord, thrust.yCoord, thrust.zCoord);
        this.spawnContraolWithOffset(0, 0, 0);
        this.spawnContraolWithOffset(-thrust.xCoord, -thrust.zCoord, -thrust.zCoord);
    }

    public static class MissileDoomsday extends MissileTier4 {
        public MissileDoomsday(EntityType<? extends MissileDoomsday> entityType, Level level) { super(entityType, level); }
        @Override public void onMissileImpact(RayTraceResult mop) {
            WorldUtil.loadAndSpawnEntityInWorld(NukeExplosionMK5.statFacNoSpawn(this.level, MainConfig.COMMON.MISSLE_RADIUS.get() * 2, this.position.x, this.position.y, this.position.z).setMoreFallout(100));
            NukeTorexCreator.statFacStandard(this.level, this.position.x, this.position.y, this.position.z, MainConfig.COMMON.MISSLE_RADIUS.get() * 2);
        }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(ModItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(ModItems.NOTHING.get()); }
    }
}
