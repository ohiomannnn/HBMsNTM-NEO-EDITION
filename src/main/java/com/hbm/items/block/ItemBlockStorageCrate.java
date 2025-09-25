package com.hbm.items.block;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.machine.storage.CrateBaseBlockEntity;
import com.hbm.blockentity.machine.storage.CrateIronBlockEntity;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.ModMenus;
import com.hbm.inventory.container.*;
import com.hbm.items.ItemInventory;
import com.hbm.items.tools.KeyItem;
import com.hbm.util.TagsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemBlockStorageCrate extends ItemBlockBase {

    public ItemBlockStorageCrate(Block block, Properties props) {
        super(block, props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
//        if(!ServerConfig.CRATE_OPEN_HELD.get()) return InteractionResultHolder.pass(stack);
//
//        Block block = Block.byItem(stack.getItem());
//        if(block == ModBlocks.mass_storage) return InteractionResultHolder.pass(stack);

        if(!level.isClientSide && stack.getCount() == 1) {
            BlockPos pos = player.blockPosition();
            CompoundTag tag = TagsUtil.getTag(stack);
            if(tag.contains("lock")) {
                for(ItemStack item : player.getInventory().items) {
                    if(item.isEmpty()) continue;
                    if(!(item.getItem() instanceof KeyItem)) continue;
                    CompoundTag keyTag = TagsUtil.getTag(stack);
                    if(keyTag.getInt("pins") == tag.getInt("lock")) {
                        CrateBaseBlockEntity.spawnSpiders(player, level, stack);
                        openGui(player, level, pos);
                        break;
                    }
                }
                return InteractionResultHolder.success(stack);
            }
            CrateBaseBlockEntity.spawnSpiders(player, level, stack);
            openGui(player, level, pos);
        }

        return InteractionResultHolder.success(stack);
    }

    private void openGui(Player player, Level level, BlockPos pos) {
        if (!level.isClientSide && player instanceof ServerPlayer sp) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CrateBaseBlockEntity crate && crate.canAccess(player)) {
                sp.openMenu(crate, pos);
            }
        }
    }


    private AbstractContainerMenu createMenu(int id, Inventory playerInv, ItemStack stack) {
        Block block = Block.byItem(stack.getItem());
        if(block == ModBlocks.IRON_CRATE.get()) return new ContainerCrateIron(id, playerInv, new InventoryCrate(playerInv.player, stack));
//        if(block == ModBlocks.crate_steel) return new ContainerCrateSteel(id, playerInv, new InventoryCrate(playerInv.player, stack));
//        if(block == ModBlocks.crate_desh) return new ContainerCrateDesh(id, playerInv, new InventoryCrate(playerInv.player, stack));
//        if(block == ModBlocks.crate_tungsten) return new ContainerCrateTungsten(id, playerInv, new InventoryCrate(playerInv.player, stack));
//        if(block == ModBlocks.crate_template) return new ContainerCrateTemplate(id, playerInv, new InventoryCrate(playerInv.player, stack));
//        if(block == ModBlocks.safe) return new ContainerSafe(id, playerInv, new InventoryCrate(playerInv.player, stack));
        throw new NullPointerException();
    }

    public static class InventoryCrate extends ItemInventory {
        public InventoryCrate(Player player, ItemStack crate) {
            super(player, crate, 27);
            this.player = player;
            this.target = crate;
//            this.slots = new ItemStack[this.getContainerSize()];
//
//            CompoundTag tag = TagsUtil.getOrCreateTag(target);
//            for(int i = 0; i < slots.length; i++) {
//                CompoundTag slotTag = tag.getCompound("slot" + i);
//                if(!slotTag.isEmpty()) slots[i] = ItemStack.of(slotTag);
//            }
        }

        public static CrateBaseBlockEntity findCrateType(Item crate) {
            Block block = Block.byItem(crate);
            if(block == ModBlocks.IRON_CRATE.get()) return new CrateIronBlockEntity(BlockPos.ZERO, ModBlocks.IRON_CRATE.get().defaultBlockState());
//            if(block == ModBlocks.crate_steel) return new TileEntityCrateSteel();
//            if(block == ModBlocks.crate_desh) return new TileEntityCrateDesh();
//            if(block == ModBlocks.crate_tungsten) return new TileEntityCrateTungsten();
//            if(block == ModBlocks.crate_template) return new TileEntityCrateTemplate();
//            if(block == ModBlocks.safe) return new TileEntitySafe();
            throw new NullPointerException();
        }

        @Override
        public int getContainerSize() {
            return findCrateType(target.getItem()).getContainerSize();
        }

        @Override
        public void setChanged() {
            CompoundTag nbt = TagsUtil.getOrCreateTag(target);
            int invSize = this.getContainerSize();
            for(int i = 0; i < invSize; i++) {
                ItemStack stack = this.getItem(i);
                if(stack.isEmpty()) {
                    nbt.remove("slot" + i);
                } else {
                    CompoundTag slot = new CompoundTag();
                    stack.save((HolderLookup.Provider) slot);
                    nbt.put("slot" + i, slot);
                }
            }
            if(nbt.isEmpty()) TagsUtil.setTag(target, null);
        }

        @Override
        public void stopOpen(Player player) {
            super.stopOpen(player);
            TagsUtil.setTag(target, checkNBT(TagsUtil.getTag(target)));
            player.containerMenu.broadcastChanges();
        }
        public InventoryCrate() {
            super(null, ItemStack.EMPTY, 27);
        }
    }
}
