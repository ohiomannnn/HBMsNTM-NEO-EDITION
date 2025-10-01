package com.hbm.wiaj;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.Direction;

public class WorldInAJar implements BlockAndTintGetter {

    public final int sizeX;
    public final int sizeY;
    public final int sizeZ;

    public int lightlevel = 15;

    private final BlockState[][][] blocks;
    private final BlockEntity[][][] tiles;

    public WorldInAJar(int x, int y, int z) {
        this.sizeX = x;
        this.sizeY = y;
        this.sizeZ = z;

        this.blocks = new BlockState[x][y][z];
        this.tiles = new BlockEntity[x][y][z];
    }

    public void nuke() {
        for (int i = 0; i < sizeX; i++)
            for (int j = 0; j < sizeY; j++)
                for (int k = 0; k < sizeZ; k++) {
                    blocks[i][j][k] = null;
                    tiles[i][j][k] = null;
                }
    }

    public BlockState getBlock(int x, int y, int z) {
        if (x < 0 || x >= sizeX || y < 0 || y >= sizeY || z < 0 || z >= sizeZ)
            return Blocks.AIR.defaultBlockState();
        return blocks[x][y][z] != null ? blocks[x][y][z] : Blocks.AIR.defaultBlockState();
    }

    public void setBlock(int x, int y, int z, BlockState state) {
        if (x < 0 || x >= sizeX || y < 0 || y >= sizeY || z < 0 || z >= sizeZ)
            return;
        this.blocks[x][y][z] = state;
    }

    public void setTileEntity(int x, int y, int z, BlockEntity tile) {
        if (x < 0 || x >= sizeX || y < 0 || y >= sizeY || z < 0 || z >= sizeZ)
            return;
        this.tiles[x][y][z] = tile;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return getBlock(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        if (x < 0 || x >= sizeX || y < 0 || y >= sizeY || z < 0 || z >= sizeZ)
            return null;
        return tiles[x][y][z];
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public int getBrightness(LightLayer type, BlockPos pos) {
        return lightlevel;
    }

    @Override
    public int getRawBrightness(BlockPos pos, int ambientDarkening) {
        return lightlevel;
    }

    @Override
    public int getHeight() {
        return sizeY;
    }

    @Override
    public int getMinBuildHeight() {
        return 0;
    }

    @Override
    public float getShade(Direction direction, boolean b) {
        return 1.0F;
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return null;
    }

    @Override
    public int getBlockTint(BlockPos pos, ColorResolver resolver) {
        // green grass
        Biome plains = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS);
        return resolver.getColor(plains, pos.getX(), pos.getZ());
    }
}
