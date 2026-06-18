package com.hbm.items.block;

import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blocks.IMultiBlock;
import com.hbm.blocks.IPersistentInfoProvider;
import com.hbm.items.IMetaItem;
import com.hbm.util.TagsUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;

import java.util.List;

public class BlockItemBase extends BlockItem implements IMetaItem {

    public BlockItemBase(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void getSubItems(Item item, List<ItemStack> stacks) {
        if(this.getBlock() instanceof IMultiBlock imb) imb.getSubItems(item, stacks);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        if(this.getBlock() instanceof IMultiBlock imb) {
            String string = imb.getItemDescriptionId(stack);
            if(string != null) return string;
        }
        return super.getDescriptionId();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, context, components, flag);

        if(this.getBlock() instanceof IPersistentInfoProvider ipip) {
            CompoundTag tag = TagsUtil.getCData(stack);
            if(tag.contains(IPersistentNBT.NBT_PERSISTENT_KEY)) ipip.appendHoverText(stack, tag.getCompound(IPersistentNBT.NBT_PERSISTENT_KEY), components, context, flag);
        }
    }
}
