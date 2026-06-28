package com.hbm.blockentity;

import com.hbm.util.CompatExternal;
import com.hbm.util.TagsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public interface IPersistentNBT {

    String NBT_PERSISTENT_KEY = "persistent";

    void writeNBT(CompoundTag savedTag);
    void readNBT(CompoundTag savedTag);

    default List<ItemStack> getDrops(Block block) {
        List<ItemStack> list = new ArrayList<>();
        ItemStack stack = new ItemStack(block);
        CompoundTag tag = new CompoundTag();
        this.writeNBT(tag);
        if(!tag.isEmpty()) TagsUtil.putCustomData(stack, tag);
        list.add(stack);
        return list;
    }

    static List<ItemStack> getDropsFromLootParams(BlockState state, LootParams.Builder builder) {

        LootParams params = builder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);

        Vec3 origin = params.getParameter(LootContextParams.ORIGIN);

        ServerLevel level = params.getLevel();

        return getDrops(level, BlockPos.containing(origin.x, origin.y, origin.z), state.getBlock());
    }

    static List<ItemStack> getDrops(Level level, BlockPos pos, Block b) {

        BlockEntity be = CompatExternal.getCoreFromPos(level, pos);

        if(be instanceof IPersistentNBT persistentNBT) return persistentNBT.getDrops(b);

        return new ArrayList<>();
    }

    static void restoreData(Level level, BlockPos pos, ItemStack stack) {
        try {
            if(!TagsUtil.hasCustomData(stack)) return;
            BlockEntity be = level.getBlockEntity(pos);
            if(!(be instanceof IPersistentNBT persistentNBT)) return;
            persistentNBT.readNBT(TagsUtil.getCustomData(stack));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
