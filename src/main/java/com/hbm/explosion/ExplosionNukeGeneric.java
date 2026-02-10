package com.hbm.explosion;

import com.hbm.blocks.ModBlocks;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.inventory.ModTags;
import com.hbm.lib.Library;
import com.hbm.lib.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ExplosionNukeGeneric {

    public static void incrementRad(Level level, double posX, double posY, double posZ, float mult) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (Math.abs(i) + Math.abs(j) < 4) {
                    ChunkRadiationManager.proxy.incrementRad(level, new BlockPos((int) Math.floor(posX + i * 16), (int) Math.floor(posY), (int) Math.floor(posZ + j * 16)), 50F / (Math.abs(i) + Math.abs(j) + 1) * mult);
                }
            }
        }
    }

    public static void dealDamage(Level level, double x, double y, double z, double radius) {
        dealDamage(level, x, y, z, radius, 250F);
    }

    private static void dealDamage(Level level, double x, double y, double z, double radius, float maxDamage) {
        List<Entity> entities = level.getEntities(null, new AABB(x, y, z, x, y, z).inflate(radius));

        for (Entity entity : entities) {
            double distSq = entity.distanceToSqr(x, y, z);
            if (distSq <= radius * radius) {

                double entX = entity.getX();
                double entY = entity.getY() + entity.getEyeHeight();
                double entZ = entity.getZ();

                if (!isExplosionExempt(entity) && !Library.isObstructed(level, x, y, z, entX, entY, entZ)) {
                    double dist = Math.sqrt(distSq);
                    double damage = maxDamage * (radius - dist) / radius;
                    entity.hurt(level.damageSources().source(ModDamageTypes.NUCLEAR_BLAST), (float) damage);
                    entity.setRemainingFireTicks(100);

                    double knockX = entX - x;
                    double knockY = (entity.getY() + entity.getEyeHeight()) - y;
                    double knockZ = entZ - z;

                    Vec3 knock = new Vec3(knockX, knockY, knockZ).normalize().scale(0.2D);
                    entity.setDeltaMovement(entity.getDeltaMovement().add(knock));
                }
            }
        }
    }

    private static boolean isExplosionExempt(Entity entity) {
        if (entity instanceof Ocelot) return true;

        if (
                entity instanceof Player && ((Player) entity).isCreative()
        ) return true;

        return false;
    }

    public static void solinium(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            BlockState state = level.getBlockState(pos);
            Block b = state.getBlock();

            if (b == Blocks.GRASS_BLOCK || b == Blocks.MYCELIUM || b == ModBlocks.WASTE_EARTH.get() || b == ModBlocks.WASTE_MYCELIUM.get()) {
                level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);
                return;
            }

            if (state.is(ModTags.Blocks.PLANTS) || state.is(BlockTags.LEAVES) || state.is(BlockTags.PLANKS)) {
                level.removeBlock(pos, false);
            }
        }
    }
}