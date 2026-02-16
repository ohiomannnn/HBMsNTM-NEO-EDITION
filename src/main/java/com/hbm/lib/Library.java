package com.hbm.lib;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyConnectorBlock;
import api.hbm.energymk2.IEnergyConnectorMK2;
import com.hbm.interfaces.Spaghetti;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.entity.PartEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

@Spaghetti("this whole class")
public class Library {
    public static final Direction POS_X = Direction.EAST;
    public static final Direction NEG_X = Direction.WEST;
    public static final Direction POS_Y = Direction.UP;
    public static final Direction NEG_Y = Direction.DOWN;
    public static final Direction POS_Z = Direction.SOUTH;
    public static final Direction NEG_Z = Direction.NORTH;

    /**
     * Is putting this into this trash can a good idea? No. Do I have a better idea? Not currently.
     * @param dir cable's connecting side
     */
    public static boolean canConnect(BlockGetter level, BlockPos pos, Direction dir) {
        if (pos.getY() > level.getMaxBuildHeight() || pos.getY() < level.getMinBuildHeight()) return false;

        Block b = level.getBlockState(pos).getBlock();

        if (b instanceof IEnergyConnectorBlock con) {

            if (con.canConnect(level, pos, dir.getOpposite() /* machine's connecting side */)) {
                return true;
            }
        }

        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof IEnergyConnectorMK2 con) {

            if (con.canConnect(dir.getOpposite() /* machine's connecting side */)) {
                return true;
            }
        }

        return false;
    }

    //not great either but certainly better
    public static long chargeItemsFromTE(NonNullList<ItemStack> slots, int index, long power, long maxPower) {

        if (power < 0) return 0;
        if (power > maxPower) return maxPower;

        if (slots.get(index).getItem() instanceof IBatteryItem batteryItem) {

            long batMax = batteryItem.getMaxCharge(slots.get(index));
            long batCharge = batteryItem.getCharge(slots.get(index));
            long batRate = batteryItem.getChargeRate(slots.get(index));
            long toCharge = Math.min(Math.min(power, batRate), batMax - batCharge);

            power -= toCharge;

            batteryItem.chargeBattery(slots.get(index), toCharge);
        }

        return power;
    }

    public static long chargeTEFromItems(NonNullList<ItemStack> slots, int index, long power, long maxPower) {

        if (slots.get(index).getItem() instanceof IBatteryItem batteryItem) {

            long batCharge = batteryItem.getCharge(slots.get(index));
            long batRate = batteryItem.getDischargeRate(slots.get(index));
            long toDischarge = Math.min(Math.min((maxPower - power), batRate), batCharge);

            batteryItem.dischargeBattery(slots.get(index), toDischarge);
            power += toDischarge;
        }

        return power;
    }

    public static Vec3 getPosition(float interpolation, Player player) {
        if (interpolation == 1.0F) {
            return player.getEyePosition();
        } else {
            return player.getEyePosition(interpolation);
        }
    }

    public static HitResult rayTrace(Player player, double length, float interpolation) {
        Vec3 vec3 = getPosition(interpolation, player);
        Vec3 vec31 = player.getViewVector(interpolation);
        Vec3 vec32 = vec3.add(vec31.x * length, vec31.y * length, vec31.z * length);
        return rayTraceBlocks(player.level(), vec3, vec32, false, false, true);
    }

    @Nullable
    public static BlockHitResult rayTraceBlocks(Level world, Vec3 startVec, Vec3 endVec, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        return rayTraceBlocksInternal(world, startVec, endVec, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock, 200);
    }

    @Nullable
    public static BlockHitResult rayTraceBlocks(Level world, Vec3 startVec, Vec3 endVec, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock, int maxSteps) {
        return rayTraceBlocksInternal(world, startVec, endVec, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock, maxSteps);
    }

    private static LevelChunk getChunkForBlockTrace(Level level, int cx, int cz) {
        ChunkSource source = level.getChunkSource();
        if (source instanceof ServerChunkCache scc) {
            return scc.getChunkNow(cx, cz);
        } else if (source instanceof ClientChunkCache ccc) {
            return ccc.getChunk(cx, cz, ChunkStatus.FULL, false);
        }
        return null;
    }

    // copied from ce edition
    // works WAY better than mojangs shitty code
    @Nullable
    private static BlockHitResult rayTraceBlocksInternal(Level level, Vec3 startVec, Vec3 endVec, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock, int maxSteps) {

        if (Double.isNaN(startVec.x) || Double.isNaN(startVec.y) || Double.isNaN(startVec.z) || Double.isNaN(endVec.x) || Double.isNaN(endVec.y) || Double.isNaN(endVec.z))
            return null;

        int endX = Mth.floor(endVec.x);
        int endY = Mth.floor(endVec.y);
        int endZ = Mth.floor(endVec.z);

        int x = Mth.floor(startVec.x);
        int y = Mth.floor(startVec.y);
        int z = Mth.floor(startVec.z);

        double startX = startVec.x;
        double startY = startVec.y;
        double startZ = startVec.z;

        double dx = endVec.x - startX;
        double dy = endVec.y - startY;
        double dz = endVec.z - startZ;

        int stepX = Integer.compare(endX, x);
        int stepY = Integer.compare(endY, y);
        int stepZ = Integer.compare(endZ, z);

        byte faceX = (byte) (5 - ((stepX + 1) >>> 1));
        byte faceY = (byte) (1 - ((stepY + 1) >>> 1));
        byte faceZ = (byte) (3 - ((stepZ + 1) >>> 1));

        double tDeltaX = (stepX == 0) ? Double.POSITIVE_INFINITY : (1.0D / Math.abs(dx));
        double tDeltaY = (stepY == 0) ? Double.POSITIVE_INFINITY : (1.0D / Math.abs(dy));
        double tDeltaZ = (stepZ == 0) ? Double.POSITIVE_INFINITY : (1.0D / Math.abs(dz));

        double tMaxX = (stepX == 0) ? Double.POSITIVE_INFINITY : ((stepX > 0 ? (x + 1.0D - startX) : (startX - x)) * tDeltaX);
        double tMaxY = (stepY == 0) ? Double.POSITIVE_INFINITY : ((stepY > 0 ? (y + 1.0D - startY) : (startY - y)) * tDeltaY);
        double tMaxZ = (stepZ == 0) ? Double.POSITIVE_INFINITY : ((stepZ > 0 ? (z + 1.0D - startZ) : (startZ - z)) * tDeltaZ);

        int minBuildY = level.getMinBuildHeight();
        int maxBuildY = level.getMaxBuildHeight();
        int minSection = level.getMinSection();

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);

        int cachedChunkX = Integer.MIN_VALUE;
        int cachedChunkZ = Integer.MIN_VALUE;
        LevelChunkSection[] cachedSections = null;

        int cachedSecY = Integer.MIN_VALUE;
        LevelChunkSection cachedSection = null;
        boolean cachedSectionEmpty = true;

        BlockState startState;
        if (y < minBuildY || y >= maxBuildY) {
            startState = Blocks.AIR.defaultBlockState();
        } else {
            int cx = x >> 4;
            int cz = z >> 4;
            cachedChunkX = cx;
            cachedChunkZ = cz;

            LevelChunk chunk = getChunkForBlockTrace(level, cx, cz);
            cachedSections = (chunk != null) ? chunk.getSections() : null;

            int secY = y >> 4;
            cachedSecY = secY;
            int secIndex = secY - minSection;

            if (cachedSections == null || secIndex < 0 || secIndex >= cachedSections.length) {
                startState = Blocks.AIR.defaultBlockState();
            } else {
                cachedSection = cachedSections[secIndex];
                cachedSectionEmpty = (cachedSection == null) || cachedSection.hasOnlyAir();
                startState = cachedSectionEmpty ? Blocks.AIR.defaultBlockState() : cachedSection.getBlockState(x & 15, y & 15, z & 15);
            }
        }

        if (stopOnLiquid && !startState.getFluidState().isEmpty()) {
            VoxelShape fluidShape = startState.getFluidState().getShape(level, pos);
            BlockHitResult hit = fluidShape.clip(startVec, endVec, pos.immutable());
            if (hit != null) {
                return hit;
            }
        }

        if (!startState.isAir()) {
            VoxelShape shape = startState.getShape(level, pos);
            if (!ignoreBlockWithoutBoundingBox || !shape.isEmpty()) {
                BlockHitResult hit = shape.clip(startVec, endVec, pos.immutable());
                if (hit != null) {
                    return hit;
                }
            }
        }

        boolean hasLastMiss = false;
        int lastMissBx = 0, lastMissBy = 0, lastMissBz = 0;
        byte lastMissSide = 0;
        double lastMissHx = 0.0D, lastMissHy = 0.0D, lastMissHz = 0.0D;

        double curX, curY, curZ;
        int stepsLeft = Math.max(0, maxSteps);

        while (stepsLeft-- > 0) {
            if (x == endX && y == endY && z == endZ) {
                break;
            }

            byte sideHit;
            double tNext;

            if (tMaxX < tMaxY) {
                if (tMaxX < tMaxZ) {
                    sideHit = faceX;
                    tNext = tMaxX;
                    tMaxX += tDeltaX;
                    x += stepX;
                    curX = (stepX > 0) ? (double) x : (double) (x + 1);
                    curY = startY + dy * tNext;
                    curZ = startZ + dz * tNext;
                } else {
                    sideHit = faceZ;
                    tNext = tMaxZ;
                    tMaxZ += tDeltaZ;
                    z += stepZ;
                    curX = startX + dx * tNext;
                    curY = startY + dy * tNext;
                    curZ = (stepZ > 0) ? (double) z : (double) (z + 1);
                }
            } else {
                if (tMaxY < tMaxZ) {
                    sideHit = faceY;
                    tNext = tMaxY;
                    tMaxY += tDeltaY;
                    y += stepY;
                    curX = startX + dx * tNext;
                    curY = (stepY > 0) ? (double) y : (double) (y + 1);
                    curZ = startZ + dz * tNext;
                } else {
                    sideHit = faceZ;
                    tNext = tMaxZ;
                    tMaxZ += tDeltaZ;
                    z += stepZ;
                    curX = startX + dx * tNext;
                    curY = startY + dy * tNext;
                    curZ = (stepZ > 0) ? (double) z : (double) (z + 1);
                }
            }

            pos.set(x, y, z);

            BlockState state;
            if (y < minBuildY || y >= maxBuildY) {
                state = Blocks.AIR.defaultBlockState();
            } else {
                int cx = x >> 4;
                int cz = z >> 4;

                if (cx != cachedChunkX || cz != cachedChunkZ) {
                    cachedChunkX = cx;
                    cachedChunkZ = cz;
                    LevelChunk chunk = getChunkForBlockTrace(level, cx, cz);
                    cachedSections = (chunk != null) ? chunk.getSections() : null;
                    cachedSecY = Integer.MIN_VALUE;
                    cachedSection = null;
                    cachedSectionEmpty = true;
                }

                int secY = y >> 4;
                if (secY != cachedSecY) {
                    cachedSecY = secY;
                    int secIndex = secY - minSection;
                    if (cachedSections == null || secIndex < 0 || secIndex >= cachedSections.length) {
                        cachedSection = null;
                        cachedSectionEmpty = true;
                    } else {
                        cachedSection = cachedSections[secIndex];
                        cachedSectionEmpty = (cachedSection == null) || cachedSection.hasOnlyAir();
                    }
                }

                state = cachedSectionEmpty ? Blocks.AIR.defaultBlockState() : cachedSection.getBlockState(x & 15, y & 15, z & 15);
            }

            if (state.isAir()) {
                if (returnLastUncollidableBlock) {
                    hasLastMiss = true;
                    lastMissSide = sideHit;
                    lastMissBx = x; lastMissBy = y; lastMissBz = z;
                    lastMissHx = curX; lastMissHy = curY; lastMissHz = curZ;
                }
                continue;
            }

            if (stopOnLiquid && !state.getFluidState().isEmpty()) {
                VoxelShape fluidShape = state.getFluidState().getShape(level, pos);
                BlockHitResult hit = fluidShape.clip(startVec, endVec, pos.immutable());
                if (hit != null) {
                    return hit;
                }
            }

            VoxelShape shape = state.getShape(level, pos);

            if (!ignoreBlockWithoutBoundingBox || state.is(Blocks.NETHER_PORTAL) || !shape.isEmpty()) {
                BlockHitResult hit = shape.clip(startVec, endVec, pos.immutable());

                if (hit != null) {
                    return hit;
                }
            }
            if (returnLastUncollidableBlock) {
                hasLastMiss = true;
                lastMissSide = sideHit;
                lastMissBx = x;
                lastMissBy = y;
                lastMissBz = z;
                lastMissHx = curX;
                lastMissHy = curY;
                lastMissHz = curZ;
            }
        }

        if (!returnLastUncollidableBlock || !hasLastMiss) return null;
        return BlockHitResult.miss(new Vec3(lastMissHx, lastMissHy, lastMissHz), Direction.values()[lastMissSide], new BlockPos(lastMissBx, lastMissBy, lastMissBz));
    }

    public static boolean isObstructed(Level level, double x, double y, double z, double a, double b, double c) {
        return level.clip(new ClipContext(new Vec3(x, y, z), new Vec3(a, b, c), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, CollisionContext.empty())).getType() != HitResult.Type.MISS;
    }
}
