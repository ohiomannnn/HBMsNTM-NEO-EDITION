package com.hbm.explosion;

import com.hbm.blocks.ModBlocks;
import com.hbm.lib.ModDamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;
import java.util.Random;

public class ExplosionNukeGeneric {

    private final static Random random = new Random();

    public static void empBlast(Level level, int x, int y, int z, int bombStartStrength) {
        int r = bombStartStrength;
        int r2 = r * r;
        int r22 = r2 / 2;
        for (int xx = -r; xx < r; xx++) {
            int X = xx + x;
            int XX = xx * xx;
            for (int yy = -r; yy < r; yy++) {
                int Y = yy + y;
                int YY = XX + yy * yy;
                for (int zz = -r; zz < r; zz++) {
                    int Z = zz + z;
                    int ZZ = YY + zz * zz;
                    if (ZZ < r22) {
                        emp(level, X, Y, Z);
                    }
                }
            }
        }
    }

    public static void dealDamage(Level world, double x, double y, double z, double radius) {
        dealDamage(world, x, y, z, radius, 250F);
    }

    private static void dealDamage(Level level, double x, double y, double z, double radius, float damage) {

        BlockPos center = new BlockPos((int) x, (int) y, (int) z);
        AABB area = new AABB(center).inflate(radius);
        List<Entity> entities = level.getEntities(null, area);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                DamageSource src = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageSource.NUCLEAR_BLAST));
                living.hurt(src, damage);
                Vec3 dir = living.position().subtract(Vec3.atCenterOf(center)).normalize();
                living.setDeltaMovement(living.getDeltaMovement().add(dir.scale(1.5)));
            }
            //we do not need millions of items just laying around
            if (entity instanceof ItemEntity itemEntity) {
                itemEntity.discard();
            }
        }
    }

    private static boolean isExplosionExempt(Entity entity) {
        return entity instanceof Ocelot || entity instanceof Player player && !player.isCreative();
    }
    public static int destruction(Level level, int x, int y, int z) {
        int rand;
        if (!level.isClientSide) {
            BlockPos blockPos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(blockPos);
            Block b = state.getBlock();
            if (state.getExplosionResistance(level, blockPos, null) >= 200f) {	//500 is the resistance of liquids
                int protection = (int)(state.getExplosionResistance(level, blockPos, null) / 300f);
                if (b == ModBlocks.BRICK_CONCRETE.get()) {
                    rand = random.nextInt(8);
                    if (rand == 0) {
                        level.setBlock(blockPos, Blocks.GRAVEL.defaultBlockState(), 3);
                        return 0;
                    }
                } else if (b == ModBlocks.BRICK_LIGHT.get()) {
                    rand = random.nextInt(3);
                    if (rand == 0) {
                        level.setBlock(blockPos, ModBlocks.WASTE_PLANKS.get().defaultBlockState(), 3);
                        return 0;
                    } else if (rand == 1){
                        level.setBlock(blockPos, ModBlocks.BLOCK_SCRAP.get().defaultBlockState(), 3);
                        return 0;
                    }
                } else if (b == ModBlocks.BRICK_OBSIDIAN.get()) {
                    rand = random.nextInt(20);
                    if (rand == 0) {
                        level.setBlock(blockPos, Blocks.OBSIDIAN.defaultBlockState(), 3);
                    }
                } else if (b == Blocks.OBSIDIAN) {
                    level.setBlock(blockPos, ModBlocks.GRAVEL_OBSIDIAN.get().defaultBlockState(), 3);
                    return 0;
                } else if (random.nextInt(protection+3)==0){
                    level.setBlock(blockPos, ModBlocks.BLOCK_SCRAP.get().defaultBlockState(), 3);
                }
                return protection;
            } else { //otherwise, kill the block!
                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
        return 0;
    }

    public static int vaporDest(Level level, int x, int y, int z) {
        if (!level.isClientSide) {
            BlockPos blockPos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(blockPos);
            Block b = state.getBlock();
            if (b.getExplosionResistance(state, level, blockPos, null) < 0.5f // most light things
                    || b == Blocks.COBWEB //b == ModBlocks.red_cable not for now
                    || b instanceof LiquidBlock) {
                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                return 0;
            } else if (b.getExplosionResistance(state, level, blockPos, null)<=3.0f && !state.isSolidRender(level, blockPos)) {
                if (b != Blocks.CHEST && b != Blocks.FARMLAND){
                    //destroy all medium resistance blocks that aren't chests or farmland
                    level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                    return 0;
                }
            }
            BlockPos posAbove = new BlockPos(x, y + 1, z);
            if (b.isFlammable(state, level, blockPos, Direction.UP)
                    && level.getBlockState(posAbove).isAir()) {
                level.setBlock(posAbove, Blocks.FIRE.defaultBlockState(),3);
            }
            return (int)(b.getExplosionResistance(state, level, blockPos, null) / 300f);
        }
        return 0;
    }

    public static void waste(Level level, int x, int y, int z, int radius) {
        int r = radius;
        int r2 = r * r;
        int r22 = r2 / 2;
        for (int xx = -r; xx < r; xx++) {
            int X = xx + x;
            int XX = xx * xx;
            for (int yy = -r; yy < r; yy++) {
                int Y = yy + y;
                int YY = XX + yy * yy;
                for (int zz = -r; zz < r; zz++) {
                    int Z = zz + z;
                    int ZZ = YY + zz * zz;
                    if (ZZ < r22 + level.random.nextInt(r22 / 5)) {
                        BlockPos blockPos = new BlockPos(X, Y, Z);
                        if (level.getBlockState(blockPos).isAir())
                            wasteDest(level, X, Y, Z);
                    }
                }
            }
        }
    }

    public static void wasteDest(Level level, int x, int y, int z) {
        if (!level.isClientSide) {
            int rand;
            BlockPos blockPos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(blockPos);
            Block b = state.getBlock();

            if (state.is(BlockTags.DOORS)) {
                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
            }

            else if (b == Blocks.GRASS_BLOCK) {
                level.setBlock(blockPos, ModBlocks.WASTE_EARTH.get().defaultBlockState(), 3);
            }

            else if (b == Blocks.MYCELIUM) {
                level.setBlock(blockPos, ModBlocks.WASTE_MYCELIUM.get().defaultBlockState(), 3);
            }

            else if (state.is(BlockTags.SAND)) {
                rand = random.nextInt(20);
                if (rand == 1 && state.is(Blocks.SAND)) {
                    level.setBlock(blockPos, ModBlocks.WASTE_TRINITITE.get().defaultBlockState(), 3);
                }
                if (rand == 1 && state.is(Blocks.RED_SAND)) {
                    level.setBlock(blockPos, ModBlocks.WASTE_TRINITITE_RED.get().defaultBlockState(), 3);
                }
            }

            else if (b == Blocks.CLAY) {
                level.setBlock(blockPos, Blocks.TERRACOTTA.defaultBlockState(), 3);
            }

            else if (b == Blocks.MOSSY_COBBLESTONE) {
                level.setBlock(blockPos, Blocks.COAL_ORE.defaultBlockState(), 3);
            }

            else if (b == Blocks.COAL_ORE) {
                rand = random.nextInt(10);
                if (rand == 1 || rand == 2 || rand == 3) {
                    level.setBlock(blockPos, Blocks.DIAMOND_ORE.defaultBlockState(), 3);
                }
                if (rand == 9) {
                    level.setBlock(blockPos, Blocks.EMERALD_ORE.defaultBlockState(), 3);
                }
            }

            else if (state.is(BlockTags.LOGS)) {
                level.setBlock(blockPos, ModBlocks.WASTE_LOG.get().defaultBlockState(), 3);
            }

//            else if (b == Blocks.BROWN_MUSHROOM_BLOCK) {
//                if (state.equals(BROWN_MUSHROOM_META10)) {
//                    level.setBlock(blockPos, ModBlocks.WASTE_LOG.get().defaultBlockState(), 3);
//                } else {
//                    level.setBlock(blockPos, Blocks.AIR.defaultBlockState() ,3);
//                }
//            }
//
//            else if (b == Blocks.RED_MUSHROOM_BLOCK) {
//                if (state.equals(RED_MUSHROOM_META10)) {
//                    level.setBlock(blockPos, ModBlocks.WASTE_LOG.get().defaultBlockState(), 3);
//                } else {
//                    level.setBlock(blockPos, Blocks.AIR.defaultBlockState() ,3);
//                }
//            }

            if (state.is(BlockTags.PLANKS)
                    && state.isSolidRender(level, blockPos)
                    && !state.is(ModBlocks.WASTE_LOG.get())) {

                level.setBlock(blockPos, ModBlocks.WASTE_PLANKS.get().defaultBlockState(), 3);
            }

//            else if (b == ModBlocks.ORE_URANIUM.get()) {
//                rand = random.nextInt(ServerConfigt.SCHRABIDIUM_FROM_URANIUM_CHANCE.getAsInt());
//                if (rand == 1) {
//                    level.setBlock(blockPos, ModBlocks.ORE_SCHRABIDIUM.get().defaultBlockState(), 3);
//                } else {
//                    level.setBlock(blockPos, ModBlocks.ORE_URANIUM_SCORCHED.get().defaultBlockState(), 3);
//                }
//            }
//
//            else if (b == ModBlocks.ORE_URANIUM.get()) {
//                rand = random.nextInt(ServerConfigt.SCHRABIDIUM_FROM_URANIUM_CHANCE.getAsInt());
//                if (rand == 1) {
//                    level.setBlock(blockPos, ModBlocks.ORE_NETHER_SCHRABIDIUM.get().defaultBlockState(), 3);
//                } else {
//                    level.setBlock(blockPos, ModBlocks.ORE_NETHER_URANIUM_SCORCHED.get().defaultBlockState(), 3);
//                }
//            }
//
//            else if (b == ModBlocks.ORE_GNEISS_URANIUM.get()) {
//                rand = random.nextInt(ServerConfigt.SCHRABIDIUM_FROM_URANIUM_CHANCE.getAsInt());
//                if (rand == 1) {
//                    level.setBlock(blockPos, ModBlocks.ORE_GNEISS_SCHRABIDIUM.get().defaultBlockState(), 3);
//                } else {
//                    level.setBlock(blockPos, ModBlocks.ORE_GNEISS_SCHRABIDIUM.get().defaultBlockState(), 3);
//                }
//            }
        }
    }

    public static void emp(Level level, int x, int y, int z) {
        if (!level.isClientSide) {
            BlockPos blockPos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(blockPos);
            BlockEntity be = level.getBlockEntity(blockPos);

            IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, blockPos, state, be, null);
            if (storage != null) {
                storage.extractEnergy(storage.getEnergyStored(), false);
            }

            for (Direction dir : Direction.values()) {
                IEnergyStorage sidedStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, blockPos, state, be, dir);
                if (sidedStorage != null) {
                    sidedStorage.extractEnergy(sidedStorage.getEnergyStored(), false);
                }
            }
        }
    }

    public static void solinium(Level level, int x, int y, int z) {
        if (!level.isClientSide) {
            BlockPos blockPos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(blockPos);
            Block b = state.getBlock();

            if(b == Blocks.GRASS_BLOCK || b == Blocks.MYCELIUM || b == ModBlocks.WASTE_EARTH.get() || b == ModBlocks.WASTE_MYCELIUM.get()) {
                level.setBlock(blockPos, Blocks.DIRT.defaultBlockState(), 3);
                return;
            }
            // oof
            if ( state.is(Blocks.CACTUS)
                    || state.is(BlockTags.CORAL_BLOCKS) || state.is(BlockTags.CORALS) || state.is(BlockTags.WALL_CORALS)
                    || state.is(BlockTags.LEAVES)
                    || state.is(BlockTags.SMALL_FLOWERS) || state.is(BlockTags.TALL_FLOWERS) || state.is(BlockTags.SAPLINGS) || state.is(BlockTags.CROPS) // растения
                    || state.is(Blocks.SPONGE) || state.is(Blocks.WET_SPONGE)
                    || state.is(Blocks.VINE) || state.is(Blocks.GLOW_LICHEN)
                    || state.is(Blocks.MELON) || state.is(Blocks.PUMPKIN) || state.is(Blocks.CARVED_PUMPKIN) || state.is(Blocks.JACK_O_LANTERN)
                    || state.is(BlockTags.LOGS) || state.is(BlockTags.PLANKS) ) {

                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }
}