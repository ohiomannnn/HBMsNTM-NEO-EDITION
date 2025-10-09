package com.hbm.explosion;

import com.hbm.HBMsNTM;
import com.hbm.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;

public class ExplosionBalefire {

    public int posX;
    public int posY;
    public int posZ;
    public int lastposX = 0;
    public int lastposZ = 0;
    public int radius;
    public int radius2;
    public Level level;
    private int n = 1;
    private int nlimit;
    private int shell;
    private int leg;
    private int element;

    public void saveToNbt(CompoundTag tag, String name) {
        tag.putInt(name + "posX", posX);
        tag.putInt(name + "posY", posY);
        tag.putInt(name + "posZ", posZ);
        tag.putInt(name + "lastposX", lastposX);
        tag.putInt(name + "lastposZ", lastposZ);
        tag.putInt(name + "radius", radius);
        tag.putInt(name + "radius2", radius2);
        tag.putInt(name + "n", n);
        tag.putInt(name + "nlimit", nlimit);
        tag.putInt(name + "shell", shell);
        tag.putInt(name + "leg", leg);
        tag.putInt(name + "element", element);
    }

    public void readFromNbt(CompoundTag tag, String name) {
        posX = tag.getInt(name + "posX");
        posY = tag.getInt(name + "posY");
        posZ = tag.getInt(name + "posZ");
        lastposX = tag.getInt(name + "lastposX");
        lastposZ = tag.getInt(name + "lastposZ");
        radius = tag.getInt(name + "radius");
        radius2 = tag.getInt(name + "radius2");
        n = Math.max(tag.getInt(name + "n"), 1); //prevents invalid read operation
        nlimit = tag.getInt(name + "nlimit");
        shell = tag.getInt(name + "shell");
        leg = tag.getInt(name + "leg");
        element = tag.getInt(name + "element");
    }

    public ExplosionBalefire(int x, int y, int z, Level level, int rad) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;

        this.level = level;

        this.radius = rad;
        this.radius2 = this.radius * this.radius;

        this.nlimit = this.radius2 * 4;
    }

    public boolean update() {

        if(n == 0) return true;

        breakColumn(this.lastposX, this.lastposZ);
        this.shell = (int) Math.floor((Math.sqrt(n) + 1) / 2);
        int shell2 = this.shell * 2;

        if(shell2 == 0) return true;

        this.leg = (int) Math.floor((this.n - (shell2 - 1) * (shell2 - 1)) / shell2);
        this.element = (this.n - (shell2 - 1) * (shell2 - 1)) - shell2 * this.leg - this.shell + 1;
        this.lastposX = this.leg == 0 ? this.shell : this.leg == 1 ? -this.element : this.leg == 2 ? -this.shell : this.element;
        this.lastposZ = this.leg == 0 ? this.element : this.leg == 1 ? this.shell : this.leg == 2 ? -this.element : -this.shell;
        this.n++;
        return this.n > this.nlimit;
    }

    private void breakColumn(int x, int z) {
        int dist = (int) (radius - Math.sqrt(x * x + z * z));

        if (dist > 0) {
            int pX = posX + x;
            int pZ = posZ + z;

            int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, pX, pZ);
            int maxdepth = (int) (10 + radius * 0.25);
            int depth = (int) ((maxdepth * dist / radius) + (Math.sin(dist * 0.15 + 2) * 2));

            depth = Math.max(y - depth, -64);

            //TODO: make clusters
            while (y > depth) {
                BlockPos pos = new BlockPos(pX, y, pZ);

//                if (level.getBlockState(pos == ModBlocks.block_schrabidium_cluster)) {
//
//                    if (level.random.nextInt(10) == 0) {
//                        level.setBlock(new BlockPos(pX, y + 1, pZ), ModBlocks.BALEFIRE.get().defaultBlockState(), 3);
//                        level.setBlock(pX, y, pZ, ModBlocks.block_euphemium_cluster, level.getBlockMetadata(pX, y, pZ), 3);
//                    }
//                    return;
//                }

                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

                y--;
            }

            if(level.random.nextInt(10) == 0) {
                level.setBlock(new BlockPos(pX, depth + 1, pZ), ModBlocks.BALEFIRE.get().defaultBlockState(), 3);

//                if(worldObj.getBlock(pX, y, pZ) == ModBlocks.block_schrabidium_cluster)
//                    worldObj.setBlock(pX, y, pZ, ModBlocks.block_euphemium_cluster, worldObj.getBlockMetadata(pX, y, pZ), 3);
            }

            for (int i = depth; i > depth - 5; i--) {
                if (level.getBlockState(new BlockPos(pX, i, pZ)).is(Blocks.STONE))
                    level.setBlock(new BlockPos(pX, i, pZ), ModBlocks.SELLAFIELD_SLAKED.get().defaultBlockState(), 3);
            }
        }
    }
}
