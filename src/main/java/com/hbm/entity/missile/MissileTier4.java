package com.hbm.entity.missile;

import api.hbm.entity.IRadarDetectableNT;
import com.hbm.config.MainConfig;
import com.hbm.entity.logic.NukeExplosionMK5;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.items.NtmItems;
import com.hbm.particle.helper.NukeTorexCreator;
import com.hbm.util.RayTraceResult;
import com.hbm.util.Vec3NT;
import com.hbm.world.WorldUtil;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class MissileTier4 extends MissileBaseNT {
    public MissileTier4(EntityType<? extends MissileTier4> entityType, Level level) { super(entityType, level); }

    @Override
    public List<ItemStack> getDebris() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(NtmItems.NOTHING.get(), 16));
        list.add(new ItemStack(NtmItems.NOTHING.get(), 20));
        list.add(new ItemStack(NtmItems.NOTHING.get(), 12));
        list.add(new ItemStack(NtmItems.NOTHING.get(), 1));
        return list;
    }

    @Override
    public String getUnlocalizedName() {
        return "radar.target.tier4";
    }

    @Override
    public int getBlipLevel() {
        return IRadarDetectableNT.TIER4;
    }

    @Override
    protected void spawnContrail() {
        Direction dir = this.getEntityData().get(MissileBaseNT.ROT);

        Vec3NT thrust = new Vec3NT(0, 0, 1);
        switch (dir) {
            case WEST -> thrust.rotateAroundYRad((float) -Math.PI / 2F);
            case SOUTH -> thrust.rotateAroundYRad((float) -Math.PI);
            case EAST -> thrust.rotateAroundYRad((float) -Math.PI / 2F * 3F);
        }
        thrust.rotateAroundYRad((this.yRot + 90) * (float) Math.PI / 180F);
        thrust.rotateAroundXRad(this.xRot * (float) Math.PI / 180F);
        thrust.rotateAroundYRad(-(this.yRot + 90) * (float) Math.PI / 180F);

        this.spawnContraolWithOffset(thrust.xCoord, thrust.yCoord, thrust.zCoord);
        this.spawnContraolWithOffset(0, 0, 0);
        this.spawnContraolWithOffset(-thrust.xCoord, -thrust.zCoord, -thrust.zCoord);
    }

    public static class MissileNuclear extends MissileTier4 {
        public MissileNuclear(EntityType<? extends MissileNuclear> entityType, Level level) { super(entityType, level); }
        @Override public void onMissileImpact(RayTraceResult mop) {
            WorldUtil.loadAndSpawnEntityInWorld(NukeExplosionMK5.statFacNoSpawn(this.level, MainConfig.COMMON.MISSLE_RADIUS.get(), this.position.x, this.position.y, this.position.z));
            NukeTorexCreator.statFacStandard(this.level, this.position.x, this.position.y, this.position.z, MainConfig.COMMON.MISSLE_RADIUS.get());
        }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(NtmItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(NtmItems.MISSILE_NUCLEAR.get()); }
    }

    public static class MissileMirv extends MissileTier4 {
        public MissileMirv(EntityType<? extends MissileMirv> entityType, Level level) { super(entityType, level); }
        @Override public void onMissileImpact(RayTraceResult mop) {
            WorldUtil.loadAndSpawnEntityInWorld(NukeExplosionMK5.statFacNoSpawn(this.level, MainConfig.COMMON.MISSLE_RADIUS.get() * 2, this.position.x, this.position.y, this.position.z));
            NukeTorexCreator.statFacStandard(this.level, this.position.x, this.position.y, this.position.z, MainConfig.COMMON.MISSLE_RADIUS.get() * 2);
        }
        @Override public List<ItemStack> getDebris() {
            List<ItemStack> list = new ArrayList<>();
            list.add(new ItemStack(NtmItems.NOTHING.get(), 16));
            list.add(new ItemStack(NtmItems.NOTHING.get(), 20));
            list.add(new ItemStack(NtmItems.NOTHING.get(), 12));
            list.add(new ItemStack(NtmItems.NOTHING.get(), 1));
            return list;
        }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(NtmItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(NtmItems.MISSILE_NUCLEAR_CLUSTER.get()); }
    }

    public static class MissileVolcano extends MissileTier4 {
        public MissileVolcano(EntityType<? extends MissileVolcano> entityType, Level level) { super(entityType, level); }
        @Override public void onMissileImpact(RayTraceResult mop) {
            if (level instanceof ServerLevel serverLevel) {
                ExplosionLarge.explode(serverLevel, this.position.x, this.position.y, this.position.z, 10, true, true, true);
                for (Player player : serverLevel.players()) {
                    player.sendSystemMessage(Component.literal("whoops, volcano missile is placeholder, sorry!"));
                }
            }
        }
        @Override public ItemStack getDebrisRareDrop() { return new ItemStack(NtmItems.NOTHING.get()); }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(NtmItems.MISSILE_VOLCANO.get()); }
    }

    public static class MissileDoomsday extends MissileTier4 {
        public MissileDoomsday(EntityType<? extends MissileDoomsday> entityType, Level level) { super(entityType, level); }
        @Override public void onMissileImpact(RayTraceResult mop) {
            WorldUtil.loadAndSpawnEntityInWorld(NukeExplosionMK5.statFacNoSpawn(this.level, MainConfig.COMMON.MISSLE_RADIUS.get() * 2, this.position.x, this.position.y, this.position.z).setMoreFallout(100));
            NukeTorexCreator.statFacStandard(this.level, this.position.x, this.position.y, this.position.z, MainConfig.COMMON.MISSLE_RADIUS.get() * 2);
        }
        @Override public List<ItemStack> getDebris() { return null; }
        @Override public ItemStack getDebrisRareDrop() { return null; }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(NtmItems.MISSILE_DOOMSDAY.get()); }
    }

    public static class MissileDoomsdayRusted extends MissileDoomsday {
        public MissileDoomsdayRusted(EntityType<? extends MissileDoomsdayRusted> entityType, Level level) { super(entityType, level); }
        @Override public void onMissileImpact(RayTraceResult mop) {
            WorldUtil.loadAndSpawnEntityInWorld(NukeExplosionMK5.statFacNoSpawn(this.level, MainConfig.COMMON.MISSLE_RADIUS.get(), this.position.x, this.position.y, this.position.z).setMoreFallout(100));
            NukeTorexCreator.statFacStandard(this.level, this.position.x, this.position.y, this.position.z, MainConfig.COMMON.MISSLE_RADIUS.get());
        }
        @Override public ItemStack getMissileItemForInfo() { return new ItemStack(NtmItems.MISSILE_DOOMSDAY_RUSTED.get()); }
    }
}
