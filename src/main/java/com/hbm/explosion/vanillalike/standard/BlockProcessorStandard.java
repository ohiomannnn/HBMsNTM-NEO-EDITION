package com.hbm.explosion.vanillalike.standard;

import com.hbm.explosion.vanillalike.ExplosionVNT;
import com.hbm.explosion.vanillalike.interfaces.IBlockProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public class BlockProcessorStandard implements IBlockProcessor {
    private boolean doDrops = true;

    @Override
    public void processBlocks(ExplosionVNT explosion, Level level, double x, double y, double z, HashSet<BlockPos> blocks) {
        blocks.forEach(blockPos -> {
            BlockState blockState = level.getBlockState(blockPos);
            if (!blockState.isAir()) {
                if (doDrops) {
                    BlockEntity blockEntity = blockState.hasBlockEntity() ? level.getBlockEntity(blockPos) : null;
                    LootParams.Builder lootBuilder = new LootParams.Builder(level.getServer().overworld())
                            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockPos))
                            .withParameter(LootContextParams.TOOL, null)
                            .withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
                            .withOptionalParameter(LootContextParams.THIS_ENTITY, explosion.exploder);

                    blockState.getDrops(lootBuilder).forEach(stack -> Block.popResource(level, blockPos, stack));
                }
                level.setBlock(blockPos, blockState.getFluidState().createLegacyBlock(), 3);
            }
        });
    }
    public BlockProcessorStandard setNoDrop() {
        this.doDrops = false;
        return this;
    }
}
