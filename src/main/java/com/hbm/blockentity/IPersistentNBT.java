package com.hbm.blockentity;

import com.hbm.util.CompatExternal;
import com.hbm.util.TagsUtilDegradation;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
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
        writeNBT(tag);
        if (!tag.isEmpty()) {
            TagsUtilDegradation.putTag(stack, tag);
        }
        list.add(stack);
        return list;
    }

    static List<ItemStack> getDropsFromLootParams(BlockState state, LootParams.Builder builder) {
        ServerLevel level = builder.getLevel();
        Vec3 origin = builder.getOptionalParameter(LootContextParams.ORIGIN);

        assert origin != null; // i think
        BlockPos pos = BlockPos.containing(origin.x, origin.y, origin.z);

        return getDrops(level, pos, state.getBlock());
    }

    static List<ItemStack> getDrops(Level level, BlockPos pos, Block b) {

        BlockEntity be = CompatExternal.getCoreFromPos(level, pos);

        if (be instanceof IPersistentNBT persistentNBT) {
            return persistentNBT.getDrops(b);
        }

        return new ArrayList<>();
    }

    static void restoreData(Level level, BlockPos pos, ItemStack stack) {
        try {
            if (!TagsUtilDegradation.containsAnyTag(stack)) return;
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof IPersistentNBT persistentNBT)) return;
            persistentNBT.readNBT(TagsUtilDegradation.getTag(stack));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
