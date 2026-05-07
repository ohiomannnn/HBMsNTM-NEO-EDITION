package com.hbm.blockentity.bomb;

import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blockentity.NukeBaseBlockEntity;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.menus.NukePrototypeMenu;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.BreedingRodItem.BreedingRodType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class NukePrototypeBlockEntity extends NukeBaseBlockEntity {

    public NukePrototypeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.NUKE_PROTOTYPE.get(), pos, state, 14);
    }

    @Override public Component getDefaultName() { return Component.translatable("container.nukePrototype"); }

    @Override public int getMaxStackSize() { return 1; }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        Item item = stack.getItem();
        return item == NtmItems.CELL_SAS3.get() ||
                item == NtmItems.ROD_QUAD.get();
    }

    @Override
    public boolean isReady() {
        return slots.get(0).getItem() == NtmItems.CELL_SAS3.get() &&
                slots.get(1).getItem() == NtmItems.CELL_SAS3.get() &&
                MetaHelper.compareStacks(slots.get(2), MetaHelper.newStack(NtmItems.ROD_QUAD, BreedingRodType.URANIUM)) &&
                MetaHelper.compareStacks(slots.get(3), MetaHelper.newStack(NtmItems.ROD_QUAD, BreedingRodType.URANIUM)) &&
                MetaHelper.compareStacks(slots.get(4), MetaHelper.newStack(NtmItems.ROD_QUAD, BreedingRodType.LEAD)) &&
                MetaHelper.compareStacks(slots.get(5), MetaHelper.newStack(NtmItems.ROD_QUAD, BreedingRodType.LEAD)) &&
                MetaHelper.compareStacks(slots.get(6), MetaHelper.newStack(NtmItems.ROD_QUAD, BreedingRodType.NP237)) &&
                MetaHelper.compareStacks(slots.get(7), MetaHelper.newStack(NtmItems.ROD_QUAD, BreedingRodType.NP237)) &&
                MetaHelper.compareStacks(slots.get(8), MetaHelper.newStack(NtmItems.ROD_QUAD, BreedingRodType.LEAD)) &&
                MetaHelper.compareStacks(slots.get(9), MetaHelper.newStack(NtmItems.ROD_QUAD, BreedingRodType.LEAD)) &&
                MetaHelper.compareStacks(slots.get(10), MetaHelper.newStack(NtmItems.ROD_QUAD, BreedingRodType.URANIUM)) &&
                MetaHelper.compareStacks(slots.get(11), MetaHelper.newStack(NtmItems.ROD_QUAD, BreedingRodType.URANIUM)) &&
                slots.get(12).getItem() == NtmItems.CELL_SAS3.get() &&
                slots.get(13).getItem() == NtmItems.CELL_SAS3.get();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new NukePrototypeMenu(id, inventory, this);
    }
}
