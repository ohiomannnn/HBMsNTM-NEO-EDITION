package com.hbm.interfaces;

import com.hbm.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ICopiable {

    CompoundTag getSettings(Level level, BlockPos pos);

    void pasteSettings(CompoundTag tag, int index, Level level, Player player, BlockPos pos);

    default String getSettingsSourceID(Either<BlockEntity, Block> self) {
        Block block = self.isLeft() ? self.left().getBlockState().getBlock() : self.right();
        return block.getDescriptionId();
    }

    default String getSettingsSourceDisplay(Either<BlockEntity, Block> self) {
        Block block = self.isLeft() ? self.left().getBlockState().getBlock() : self.right();
        return block.getDescriptionId();
    }

    default String[] infoForDisplay(Level level, BlockPos pos){
        return null;
    }
}
