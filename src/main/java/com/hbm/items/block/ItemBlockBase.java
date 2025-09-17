package com.hbm.items.block;

import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blocks.IBlockMulti;
import com.hbm.blocks.IPersistentInfoProvider;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.util.TagsUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemBlockBase extends BlockItem {

    public ItemBlockBase(Block block, Properties props) {
        super(block, props);
    }

    @Override
    public Component getName(ItemStack stack) {
        Block block = getBlock();
        if (block instanceof IBlockMulti multi) {
            String override = multi.getOverrideDisplayName(stack);
            if (override != null) {
                return Component.translatable(override);
            }
            return Component.translatable(multi.getUnlocalizedName(stack) + ".name");
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        Block block = getBlock();

        if (block instanceof ITooltipProvider tipProvider) {
            tipProvider.addInformation(stack, context, tooltipComponents, tooltipFlag);
        }

        if (block instanceof IPersistentInfoProvider infoProvider && TagsUtil.hasTag(stack)) {
            CompoundTag data = TagsUtil.getTagElement(stack, IPersistentNBT.NBT_PERSISTENT_KEY);
            if (data != null) {
                infoProvider.addInformation(stack, context, tooltipComponents, tooltipFlag);
            }
        }
    }
}
