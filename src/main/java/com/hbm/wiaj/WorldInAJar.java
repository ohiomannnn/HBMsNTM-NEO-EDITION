package com.hbm.wiaj;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class WorldInAJar implements BlockAndTintGetter {

    public final int sizeX;
    public final int sizeY;
    public final int sizeZ;

    private BlockState[][][] blocks;
    private BlockEntity[][][] be;

    public WorldInAJar(int x, int y, int z) {
        this.sizeX = x;
        this.sizeY = y;
        this.sizeZ = z;

        this.blocks = new BlockState[x][y][z];
        this.be = new BlockEntity[x][y][z];
    }

    public void nuke() {
        this.blocks = new BlockState[sizeX][sizeY][sizeZ];
        this.be = new BlockEntity[sizeX][sizeY][sizeZ];
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

    public void setBlockEntity(int x, int y, int z, BlockEntity blockEntity) {
        if (x < 0 || x >= sizeX || y < 0 || y >= sizeY || z < 0 || z >= sizeZ) return;
        this.be[x][y][z] = blockEntity;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return getBlock(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        if (x < 0 || x >= sizeX || y < 0 || y >= sizeY || z < 0 || z >= sizeZ) return null;
        return be[x][y][z];
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return Fluids.EMPTY.defaultFluidState();
    }

    public boolean isAirBlock(int x, int y, int z) {
        return getBlock(x, y, z).isAir();
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
    public float getShade(Direction direction, boolean shade) {
        assert Minecraft.getInstance().level != null;
        return Minecraft.getInstance().level.getShade(direction, shade);
    }

    @Override
    public LevelLightEngine getLightEngine() {
        assert Minecraft.getInstance().level != null;
        return Minecraft.getInstance().level.getLightEngine();
    }

    @Override
    public int getBlockTint(BlockPos pos, ColorResolver resolver) {
        assert Minecraft.getInstance().level != null;
        // green grass
        Biome plains = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS);
        return resolver.getColor(plains, pos.getX(), pos.getZ());
    }
}
