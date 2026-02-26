package com.hbm.explosion;

import com.hbm.entity.ModEntityTypes;
import com.hbm.entity.projectile.Rocket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class ExplosionChaos {

    /**
     * Sets all flammable blocks on fire
     */
    public static void flameDeath(Level level, int x, int y, int z, int bound) {

        int r = bound;
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
                        if (level.getBlockState(new BlockPos(X, Y, Z)).isFlammable(level, new BlockPos(XX, YY, ZZ), Direction.UP) && level.getBlockState(new BlockPos(X, Y + 1, Z)).isAir()) {
                            level.setBlock(new BlockPos(X, Y + 1, Z), Blocks.FIRE.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }

    }

    /**
     * Sets all blocks on fire
     */
    public static void burn(Level level, int x, int y, int z, int bound) {

        int r = bound;
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
                        BlockPos pos = new BlockPos(X, Y, Z);
                        if ((level.getBlockState(pos.above()).isAir()
                                || level.getBlockState(pos.above()).is(Blocks.SNOW))
                                && !level.getBlockState(pos).isAir()) {
                            level.setBlock(pos.above(), Blocks.FIRE.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }

    }

    public static void cluster(Level level, double x, double y, double z, int count) {

        double d1;
        double d2;
        double d3;
        Rocket fragment;

        for (int i = 0; i < count; i++) {
            d1 = level.random.nextDouble();
            d2 = level.random.nextDouble();
            d3 = level.random.nextDouble();

            if (level.random.nextInt(2) == 0) {
                d1 *= -1;
            }

            if (level.random.nextInt(2) == 0) {
                d3 *= -1;
            }

            fragment = new Rocket(ModEntityTypes.ROCKET.get(), level);
            fragment.setPos(x + 0.5, y + 0.5, z + 0.5);
            fragment.deltaMovement = new Vec3(d1, d2, d3);
            fragment.gravity = 0.0125D;

            level.addFreshEntity(fragment);
        }
    }
}
