package com.hbm.blockentity.bomb;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blocks.bomb.LandmineBlock;
import com.hbm.lib.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

// BlockEntity JUST for renderer
public class DetCordBlockEntity extends BlockEntity {
    public DetCordBlockEntity(BlockPos pos, BlockState blockState) { super(ModBlockEntities.DET_CORD.get(), pos, blockState); }
}
