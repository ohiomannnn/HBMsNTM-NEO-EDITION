package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.entity.IRadarDetectableNT;
import api.hbm.entity.IRadarDetectableNT.RadarScanParams;
import api.hbm.entity.RadarEntry;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.extprop.HbmLivingAttachments;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.lib.Library;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.SoundUtils;
import com.hbm.util.Tuple.Triplet;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Now with SmЯt™ lag-free entity detection! (patent pending)
 * @author hbm
 */
public class MachineRadarBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IControlReceiver {

    public boolean scanMissiles = true;
    public boolean scanShells = true;
    public boolean scanPlayers = true;
    public boolean smartMode = true;
    public boolean redMode = true;
    public boolean showMap = false;

    public boolean jammed = false;

    public float prevRotation;
    public float rotation;

    public long power = 0;

    protected int pingTimer = 0;
    protected int lastPower;
    protected final static int MAX_TIMER = 80;

    public static int maxPower = 100_000;
    public static int consumption = 500;
    public static int radarRange = 1_000;
    public static int radarBuffer = 30;
    public static int radarAltitude = 55;
    public static int chunkLoadCap = 10;
    public static boolean generateChunks = false;

    public byte[] map = new byte[40_000];
    public boolean clearFlag = false;

    public List<RadarEntry> entries = new ArrayList<>();

    public MachineRadarBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_RADAR.get(), pos, state, 10);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.radar"); }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(this.map == null || this.map.length != 40_000) this.map = new byte[40_000];

        if(!this.level.isClientSide) {

            this.power = Library.chargeTEFromItems(slots, 9, power, maxPower);

            if(level.getGameTime() % 20 == 0) {
                for(DirPos pos : this.getConPos()) {
                    this.trySubscribe(this.level, pos);
                }
            }

            this.power = Library.chargeTEFromItems(slots, 0, power, maxPower);
            this.jammed = false;
            this.allocateTargets();

            if(this.lastPower != getRedPower()) {
                this.setChanged();
                for(DirPos pos : this.getConPos()) this.level.updateNeighbourForOutputSignal(pos.makeCompat(), this.getBlockState().getBlock());
            }
            this.lastPower = getRedPower();

            if(!this.muffled) {

                pingTimer++;

                if(power > 0 && pingTimer >= MAX_TIMER) {
                    SoundUtils.playAtVec3(this.level, Vec3.atCenterOf(this.getBlockPos()), NtmSoundEvents.SONAR_PING.get(), SoundSource.BLOCKS);
                    pingTimer = 0;
                }
            }

            if(this.showMap) {
                int chunkLoads = 0;
                for(int i = 0; i < 100; i++) {
                    int index = (int) (level.getGameTime() % 400) * 100 + i;
                    int iX = (index % 200) * this.getRange() * 2 / 200;
                    int iZ = index / 200 * this.getRange() * 2 / 200;

                    BlockPos pos = this.getBlockPos();

                    int x = pos.getX() - this.getRange() + iX;
                    int z = pos.getZ() - this.getRange() + iZ;

                    if(level.hasChunk(x >> 4, z >> 4)) {
                        this.map[index] = (byte) Mth.clamp(level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z), 50, 128);
                    } else {
                        if(this.map[index] == 0 && chunkLoads < chunkLoadCap) {
//                            if(this.generateChunks) {
                            level.getChunk(x >> 4, z >> 4);
                            this.map[index] = (byte) Mth.clamp(level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z), 50, 128);
                            chunkLoads++;
//                            } else {
//                                WorldUtil.provideChunk((WorldServer) worldObj, x >> 4, z >> 4);
//                                this.map[index] = (byte) MathHelper.clamp_int(worldObj.getHeightValue(x, z), 50, 128);
//                                if(worldObj.getChunkProvider().chunkExists(x >> 4, z >> 4)) chunkLoads++;
//                            }
                        }
                    }
                }
            }

            this.networkPackNT(50);
            if(this.clearFlag) {
                this.map = new byte[40_000];
                this.clearFlag = false;
            }
        } else {
            prevRotation = rotation;
            if(power > 0) rotation += 5F;

            if(rotation >= 360) {
                rotation -= 360F;
                prevRotation -= 360F;
            }
        }
    }

    public DirPos[] getConPos() {
        BlockPos pos = this.getBlockPos();
        return new DirPos[] {
                new DirPos(pos.offset(+1, 0, 0), Library.POS_X),
                new DirPos(pos.offset(-1, 0, 0), Library.NEG_X),
                new DirPos(pos.offset(0, 0, +1), Library.POS_Z),
                new DirPos(pos.offset(0, 0, -1), Library.NEG_Z),
        };
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        if(this.level == null) return;

        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeBoolean(this.scanMissiles);
        buf.writeBoolean(this.scanShells);
        buf.writeBoolean(this.scanPlayers);
        buf.writeBoolean(this.smartMode);
        buf.writeBoolean(this.redMode);
        buf.writeBoolean(this.showMap);
        buf.writeBoolean(this.jammed);
        buf.writeInt(entries.size());
        for(RadarEntry entry : entries) entry.encode(buf);
        if(this.clearFlag) {
            buf.writeBoolean(true);
        } else {
            buf.writeBoolean(false);
            if(this.showMap) {
                buf.writeBoolean(true);
                short index = (short) (this.level.getGameTime() % 400);
                buf.writeShort(index);
                for(int i = index * 100; i < (index + 1) * 100; i++) {
                    buf.writeByte(this.map[i]);
                }
            } else {
                buf.writeBoolean(false);
            }
        }
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        if(this.level == null) return;

        super.deserialize(buf);
        this.power = buf.readLong();
        this.scanMissiles = buf.readBoolean();
        this.scanShells = buf.readBoolean();
        this.scanPlayers = buf.readBoolean();
        this.smartMode = buf.readBoolean();
        this.redMode = buf.readBoolean();
        this.showMap = buf.readBoolean();
        this.jammed = buf.readBoolean();
        int count = buf.readInt();
        this.entries.clear();
        for(int i = 0; i < count; i++) {
            RadarEntry entry = new RadarEntry();
            entry.decode(buf);
            this.entries.add(entry);
        }
        if(buf.readBoolean()) { // clear flag
            this.map = new byte[40_000];
        } else {
            if(buf.readBoolean()) { // map enabled
                int index = buf.readShort();
                for(int i = index * 100; i < (index + 1) * 100; i++) {
                    this.map[i] = buf.readByte();
                }
            }
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.power = tag.getLong("power");
        this.scanMissiles = tag.getBoolean("scanMissiles");
        this.scanShells = tag.getBoolean("scanShells");
        this.scanPlayers = tag.getBoolean("scanPlayers");
        this.smartMode = tag.getBoolean("smartMode");
        this.redMode = tag.getBoolean("redMode");
        this.showMap = tag.getBoolean("showMap");
        if(tag.contains("map")) this.map = tag.getByteArray("map");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putLong("power", power);
        tag.putBoolean("scanMissiles", scanMissiles);
        tag.putBoolean("scanShells", scanShells);
        tag.putBoolean("scanPlayers", scanPlayers);
        tag.putBoolean("smartMode", smartMode);
        tag.putBoolean("redMode", redMode);
        tag.putBoolean("showMap", showMap);
        tag.putByteArray("map", map);
    }

    public int getRange() {
        return radarRange;
    }

    protected void allocateTargets() {
        if(this.level == null) return;
        this.entries.clear();

        if(this.getBlockPos().getY() < radarAltitude) return;
        if(this.power < consumption) {
            this.power = 0;
            return;
        }
        this.power -= consumption;

        int scan = this.getRange();

        RadarScanParams params = new RadarScanParams(this.scanMissiles, this.scanShells, this.scanPlayers, this.smartMode);

        for(Entity e : matchingEntities) {

            Vec3 position = e.position();
            BlockPos bePos = this.getBlockPos();

            if(e.level.dimension() == this.level.dimension() && Math.abs(position.x - (bePos.getX() + 0.5)) <= scan && Math.abs(position.z - (bePos.getZ() + 0.5)) <= scan && position.y - bePos.getY() > radarBuffer) {

                if(e instanceof LivingEntity living && HbmLivingAttachments.getDigamma(living) > 0.001) {
                    this.jammed = true;
                    entries.clear();
                    return;
                }

                for(Function<Triplet<Entity, Object, RadarScanParams>, RadarEntry> converter : converters) {

                    RadarEntry entry = converter.apply(new Triplet<>(e, this, params));
                    if(entry != null) {
                        this.entries.add(entry);
                        break;
                    }
                }
            }
        }
    }

    public int getRedPower() {

        if(!entries.isEmpty()) {

            /// PROXIMITY ///
            if(redMode) {

                double maxRange = this.getRange() * Math.sqrt(2D);
                int power = 0;

                for(RadarEntry e : entries) {
                    if(!e.redstone) continue;
                    BlockPos entryPos = e.pos;
                    BlockPos bePos = this.getBlockPos();
                    double dist = Math.sqrt(Math.pow(entryPos.getX() - bePos.getX(), 2) + Math.pow(entryPos.getZ() - bePos.getZ(), 2));
                    int p = 15 - (int) Math.floor(dist / maxRange * 15);

                    if(p > power) power = p;
                }

                return power;

                /// TIER ///
            } else {

                int power = 0;

                for(RadarEntry e : entries) {
                    if(!e.redstone) continue;
                    if(e.blipLevel + 1 > power) {
                        power = e.blipLevel + 1;
                    }
                }

                return power;
            }
        }

        return 0;
    }

    @Override public void setPower(long power) { this.power = power; }
    @Override public long getPower() { return this.power; }
    @Override public long getMaxPower() { return maxPower; }

    @Override public boolean hasPermission(Player player) { return this.stillValid(player); }

    @Override public void receiveControl(CompoundTag tag) { }

    @Override
    public void receiveControl(Player player, CompoundTag tag) {

        if(tag.contains("missiles")) this.scanMissiles = !this.scanMissiles;
        if(tag.contains("shells")) this.scanShells = !this.scanShells;
        if(tag.contains("players")) this.scanPlayers = !this.scanPlayers;
        if(tag.contains("smart")) this.smartMode = !this.smartMode;
        if(tag.contains("red")) this.redMode = !this.redMode;
        if(tag.contains("map")) this.showMap = !this.showMap;
        if(tag.contains("clear")) this.clearFlag = true;

    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return null;
    }

    /** List of lambdas that are supplied a Pair with the entity and radar in question to generate a RadarEntry
     The converters coming first have the highest priority */
    public static List<Function<Triplet<Entity, Object, RadarScanParams>, RadarEntry>> converters = new ArrayList();
    public static final List<Class<?>> classes = new ArrayList<>();
    public static final List<Entity> matchingEntities = new ArrayList<>();

    /**
     * Iterates over every entity in the world and add them to the matchingEntities list if the class is in the detectable list
     * From this compiled list, radars can easily grab the required entities since we can assume that the total amount of detectable entities is comparatively low
     */
    public static void updateSystem(MinecraftServer server) {
        matchingEntities.clear();

        for(ServerLevel serverLevel : server.getAllLevels()) {
            for(Entity entity : serverLevel.getEntities().getAll()) {
                for(Class<?> clazz : classes) {
                    if(clazz.isAssignableFrom(entity.getClass())) {
                        matchingEntities.add(entity);
                        break;
                    }
                }
            }
        }
    }

    /** Registers a class that if an entity inherits that class, it is picked up by the system */
    public static void registerEntityClasses() {
        classes.add(IRadarDetectableNT.class);
        classes.add(Player.class);
    }

    /** Registers converters. Converters are used to go over the list of detected entities and turn them into a RadarEntry using the entity instance and the radar's instance. */
    public static void registerConverters() {
        // IRadarDetectableNT
        converters.add(x -> {
            Entity e = x.getX();
            if(e instanceof IRadarDetectableNT detectable) {
                if(detectable.canBeSeenBy(x.getY()) && detectable.paramsApplicable(x.getZ())) return new RadarEntry(detectable, e, detectable.suppliesRedstone(x.getZ()));
            }
            return null;
        });
        // Players
        converters.add(x -> {
            if(x.getX() instanceof Player && x.getZ().scanPlayers) return new RadarEntry((Player) x.getX());
            return null;
        });
    }
}
