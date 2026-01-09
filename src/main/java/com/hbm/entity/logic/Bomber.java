package com.hbm.entity.logic;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.config.MainConfig;
import com.hbm.entity.ModEntityTypes;
import com.hbm.entity.projectile.BombletZeta;
import com.hbm.interfaces.NotableComments;
import com.hbm.lib.ModSounds;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.Vec3NT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@NotableComments
public class Bomber extends PlaneBase {

    /* This was probably the dumbest fucking way that I could have handled this. Not gonna change it now, be glad I made a superclass at all. */
    // thanks mr bobert
    int bombStart = 75;
    int bombStop = 125;
    int bombRate = 3;
    int type = 0;
    public int getBomberType() { return this.type; } // sure

    private static final EntityDataAccessor<Byte> STYLE = SynchedEntityData.defineId(Bomber.class, EntityDataSerializers.BYTE);
    public byte getBomberStyle() { return this.entityData.get(STYLE); }
    public void setBomberStyle(byte type) { this.entityData.set(STYLE, type); }


    protected AudioWrapper audio;

    public Bomber(EntityType<?> type, Level level) {
        super(type, level);
    }

    /** This sucks balls. Too bad! */
    @Override
    public void tick() {
        super.tick();

        HBMsNTM.LOGGER.info("tick air");

        if (level().isClientSide) {
            if (this.getHealth() > 0) {
                if (audio == null || !audio.isPlaying()) {
                    int bomberType = getBomberStyle();
                    audio = HBMsNTMClient.getLoopedSound(bomberType <= 4 ? ModSounds.BOMBER_SMALL_LOOP.get() : ModSounds.BOMBER_LOOP.get(), SoundSource.AMBIENT, (float) this.getX(), (float) this.getY(), (float) this.getZ(), 2F, 250F, 1F, 20);
                    audio.startSound();
                }
                audio.keepAlive();
                audio.updatePosition((float) this.getX(), (float) this.getY(), (float) this.getZ());
            } else {
                if (audio != null && audio.isPlaying()) {
                    audio.stopSound();
                    audio = null;
                }
            }
        }

        if (!level().isClientSide && this.getHealth() > 0 && this.tickCount > bombStart && this.tickCount < bombStop && this.tickCount % bombRate == 0) {
            level().playSound(null, this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5, ModSounds.BOMB_WHISTLE.get(), SoundSource.AMBIENT, 10.0F, 0.9F + random.nextFloat() * 0.2F);
            BombletZeta zeta = new BombletZeta(ModEntityTypes.BOMBLET_ZETA.get(), level());
            zeta.rotation();
            zeta.type = type;
            zeta.setPos(this.getX() + random.nextDouble() - 0.5, this.getY() - random.nextDouble(), this.getZ() + random.nextDouble() - 0.5);
            if (type == 0) {
                zeta.setDeltaMovement(this.getDeltaMovement().x + random.nextGaussian() * 0.15, 0, this.getDeltaMovement().z + random.nextGaussian() * 0.15);
            } else {
                zeta.setDeltaMovement(this.getDeltaMovement().x, 0, this.getDeltaMovement().z);
            }
            level().addFreshEntity(zeta);
        }
    }

    public void fac(Level level, double x, double y, double z) {

        Vec3NT vector = new Vec3NT(level.random.nextDouble() - 0.5, 0, level.random.nextDouble() - 0.5);
        vector = vector.normalizeSelf();
        vector.xCoord *= MainConfig.COMMON.ENABLE_BOMBER_SHORT_MODE.get() ? 1 : 2;
        vector.zCoord *= MainConfig.COMMON.ENABLE_BOMBER_SHORT_MODE.get() ? 1 : 2;

        this.moveTo(x - vector.xCoord * 100, y + 50, z - vector.zCoord * 100, 0.0F, 0.0F);

        this.setDeltaMovement(vector.xCoord, 0, vector.zCoord);

        this.rotation();

        int i = 1;

        int rand = level.random.nextInt(7);

        i = switch (rand) {
            case 0, 1 -> 1;
            case 2, 3 -> 2;
            case 4 -> 5;
            case 5 -> 6;
            case 6 -> 7;
            default -> i;
        };

        if (level.random.nextInt(100) == 0) {
            rand = level.random.nextInt(4);
            i = switch (rand) {
                case 0 -> 0;
                case 1 -> 3;
                case 2 -> 4;
                case 3 -> 8;
                default -> i;
            };
        }

        this.setBomberStyle((byte) i);
    }

    public static Bomber statFacCarpet(Level level, double x, double y, double z) {
        Bomber bomber = new Bomber(ModEntityTypes.BOMBER.get(), level);
        bomber.timer = 200;
        bomber.bombStart = 50;
        bomber.bombStop = 100;
        bomber.bombRate = 2;
        bomber.fac(level, x, y, z);
        bomber.type = 0;
        return bomber;
    }

    public static Bomber statFacNapalm(Level level, double x, double y, double z) {
        Bomber bomber = new Bomber(ModEntityTypes.BOMBER.get(), level);
        bomber.timer = 200;
        bomber.bombStart = 50;
        bomber.bombStop = 100;
        bomber.bombRate = 5;
        bomber.fac(level, x, y, z);
        bomber.type = 1;
        return bomber;
    }

    public static Bomber statFacABomb(Level level, double x, double y, double z) {
        Bomber bomber = new Bomber(ModEntityTypes.BOMBER.get(), level);
        bomber.timer = 200;
        bomber.bombStart = 60;
        bomber.bombStop = 70;
        bomber.bombRate = 65;
        bomber.fac(level, x, y, z);
        int i = 1;

        int rand = level.random.nextInt(3);

        i = switch (rand) {
            case 0 -> 5;
            case 1 -> 6;
            case 2 -> 7;
            default -> i;
        };
        if (level.random.nextInt(100) == 0) i = 8;

        bomber.setBomberStyle((byte) i);
        bomber.type = 4;
        return bomber;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(STYLE, (byte) 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        bombStart = tag.getInt("BombStart");
        bombStop = tag.getInt("BombStop");
        bombRate = tag.getInt("BombRate");
        type = tag.getInt("Type");
        this.setBomberStyle(tag.getByte("Style"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("BombStart", bombStart);
        tag.putInt("BombStop", bombStop);
        tag.putInt("BombRate", bombRate);
        tag.putInt("Type", type);
        tag.putByte("Style", this.getBomberStyle());
    }
}
