package com.hbm.entity.logic;

import java.util.List;

import com.hbm.interfaces.IExplosionRay;
import net.minecraft.nbt.CompoundTag;

import com.hbm.explosion.ExplosionNukeGeneric;
import com.hbm.explosion.ExplosionNukeRayBatched;

import com.hbm.util.ContaminationUtil;
import com.hbm.util.ContaminationUtil.ContaminationType;
import com.hbm.util.ContaminationUtil.HazardType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


public class EntityNukeExplosion extends EntityExplosionChunkloading {

    public int strength;
    public int speed;
    public int length;
    private long explosionStart;
    public boolean fallout = true;
    private int falloutAdd = 0;

    private IExplosionRay explosion;

    public EntityNukeExplosion(Level level) {
        super(level);
    }

    public EntityNukeExplosion(Level level, int strength, int speed, int length) {
        super(level);
        this.strength = strength;
        this.speed = speed;
        this.length = length;
    }

    @Override
    public void onUpdate() {

        if(strength == 0) {
            this.clearChunkLoader();
            this.setDead();
            return;
        }

        if(!level().isClientSide) loadChunk((int) Math.floor(this.getX() / 16D), (int) Math.floor(this.getZ() / 16D));

//        for(Object player : this.level().playerEntities) {
//            ((Player)player).triggerAchievement(MainRegistry.achManhattan);
//        }

        if(fallout && explosion != null && this.tickCount < 10 && strength >= 75) {
            radiate(2_500_000F / (this.tickCount * 5 + 1), this.length * 2);
        }

        ExplosionNukeGeneric.dealDamage(this.level(), this.getX(), this.getY(), this.getZ(), this.length * 2);

        if(explosion == null) {
            explosionStart = System.currentTimeMillis();
            //if(BombConfig.explosionAlgorithm == 1 || BombConfig.explosionAlgorithm == 2) {
            //	explosion = new ExplosionNukeRayParallelized(worldObj, posX, posY, posZ, strength, speed, length);
            //} else {
            explosion = new ExplosionNukeRayBatched(level(), (int) this.getX(), (int) this.getY(), (int) this.getZ(), strength, speed, length);
            //}
        }

        if(!explosion.isComplete()) {
            explosion.cacheChunksTick(BombConfig.mk5);
            explosion.destructionTick(BombConfig.mk5);
        } else {
            if(GeneralConfig.enableExtendedLogging && explosionStart != 0)
                MainRegistry.logger.log(Level.INFO, "[NUKE] Explosion complete. Time elapsed: {}ms", (System.currentTimeMillis() - explosionStart));
            if(fallout) {
                EntityFalloutRain fallout = new EntityFalloutRain(this.worldObj);
                fallout.posX = this.posX;
                fallout.posY = this.posY;
                fallout.posZ = this.posZ;
                fallout.setScale((int)(this.length * 2.5 + falloutAdd) * BombConfig.falloutRange / 100);
                this.worldObj.spawnEntityInWorld(fallout);
            }
            this.clearChunkLoader();
            this.setDead();
        }
    }

    private void radiate(float rads, double range) {

        List<LivingEntity> entities = level().getEntitiesWithinAABB(LivingEntity.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX, posY, posZ).expand(range, range, range));

        for(EntityLivingBase e : entities) {

            Vec3 vec = Vec3.createVectorHelper(e.posX - posX, (e.posY + e.getEyeHeight()) - posY, e.posZ - posZ);
            double len = vec.lengthVector();
            vec = vec.normalize();

            float res = 0;

            for(int i = 1; i < len; i++) {

                int ix = (int)Math.floor(posX + vec.xCoord * i);
                int iy = (int)Math.floor(posY + vec.yCoord * i);
                int iz = (int)Math.floor(posZ + vec.zCoord * i);

                res += worldObj.getBlock(ix, iy, iz).getExplosionResistance(null);
            }

            if(res < 1)
                res = 1;

            float eRads = rads;
            eRads /= (float)res;
            eRads /= (float)(len * len);

            ContaminationUtil.contaminate(e, HazardType.RADIATION, ContaminationType.RAD_BYPASS, eRads);
        }
    }

    @Override
    public void setDead() {
        if(explosion != null)
            explosion.cancel();
    }

    @Override
    protected void readEntityFromNBT(CompoundTag nbt) {
        this.tickCount = nbt.getInt("ticksExisted");
    }

    @Override
    protected void writeEntityToNBT(CompoundTag nbt) {
        nbt.putInt("ticksExisted", this.tickCount);
    }

    public static EntityNukeExplosion statFac(Level level, int r, double x, double y, double z) {

//        if(GeneralConfig.enableExtendedLogging && !world.isRemote)
//            MainRegistry.logger.log(Level.INFO, "[NUKE] Initialized explosion at {} / {} / {} with strength {}!", x, y, z, r);

        if(r == 0)
            r = 25;

        r *= 2;

        EntityNukeExplosion mk5 = new EntityNukeExplosion(level);
        mk5.strength = (int)(r);
        mk5.speed = (int)Math.ceil(100000 / mk5.strength);
        mk5.setPosition(x, y, z);
        mk5.length = mk5.strength / 2;
        mk5.loadChunk((int) Math.floor(x / 16D), (int) Math.floor(z / 16D));
        return mk5;
    }

    public static EntityNukeExplosion statFacNoRad(Level world, int r, double x, double y, double z) {

        EntityNukeExplosion mk5 = statFac(world, r, x, y ,z);
        mk5.fallout = false;
        return mk5;
    }

    public EntityNukeExplosion moreFallout(int fallout) {
        falloutAdd = fallout;
        return this;
    }
}