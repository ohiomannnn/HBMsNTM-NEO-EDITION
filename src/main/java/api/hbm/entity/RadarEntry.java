package api.hbm.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RadarEntry {

    /** Name use for radar display */
    public Component name;
    /** The type of dot to show on the radar as well as the redstone level in tier mode */
    public int blipLevel;
    public BlockPos pos;
    public ResourceKey<Level> dim;
    public int entityId;
    /** Whether this radar entry should be counted for the redstone output */
    public boolean redstone;

    public RadarEntry() { } //blank ctor for packets

    public RadarEntry(Component name, int lvl, BlockPos pos, ResourceKey<Level> dim, int entityId, boolean redstone) {
        this.name = name;
        this.blipLevel = lvl;
        this.pos = pos;
        this.dim = dim;
        this.entityId = entityId;
        this.redstone = redstone;
    }

    public RadarEntry(IRadarDetectableNT detectable, Entity entity, boolean redstone) {
        this(detectable.getName(), detectable.getBlipLevel(), entity.blockPosition(), entity.level.dimension(), entity.getId(), redstone);
    }

    public RadarEntry(Player player) {
        this(player.getDisplayName(), IRadarDetectableNT.PLAYER, player.blockPosition(), player.level.dimension(), player.getId(), true);
    }

    public void decode(RegistryFriendlyByteBuf buf) {
        this.name = Component.Serializer.fromJson(buf.readUtf(), buf.registryAccess());
        this.blipLevel = buf.readShort();
        this.pos = buf.readBlockPos();
        this.dim = buf.readResourceKey(Registries.DIMENSION);
        this.entityId = buf.readInt();
    }

    public void encode(RegistryFriendlyByteBuf buf) {
        buf.writeUtf(Component.Serializer.toJson(this.name, buf.registryAccess()));
        buf.writeShort(this.blipLevel);
        buf.writeBlockPos(this.pos);
        buf.writeResourceKey(this.dim);
        buf.writeInt(this.entityId);
    }
}
