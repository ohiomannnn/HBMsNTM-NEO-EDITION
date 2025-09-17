package com.hbm.blockentity.machine.storage;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.inventory.container.ContainerCrateIron;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrateIronBlockEntity extends CrateBaseBlockEntity implements MenuProvider {
    public CrateIronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IRON_CRATE.get(), pos, state, 27);
    }

    @Override
    public void clearContent() {

    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.crateIron");
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ContainerCrateIron(i, inventory, this);
    }
}