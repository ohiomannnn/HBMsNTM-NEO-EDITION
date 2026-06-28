package com.hbm.items.tools;

import com.hbm.items.IDesignatorItem;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.SoundUtils;
import com.hbm.util.TagsUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class DesignatorItem extends Item implements IDesignatorItem {

    public DesignatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if(player == null) return InteractionResult.PASS;
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();

        if(!level.isClientSide) {
            CompoundTag tag = TagsUtil.getCustomData(stack);
            tag.putInt("x", pos.getX());
            tag.putInt("z", pos.getZ());
            TagsUtil.putCustomData(stack, tag);

            player.displayClientMessage(Component.translatable("item.hbmsntm.obj_designator.pos_set"), false);
            SoundUtils.playAtEntity(player, NtmSoundEvents.TECH_BLEEP.get(), SoundSource.PLAYERS);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        if(TagsUtil.hasCustomData(stack)) {
            CompoundTag tag = TagsUtil.getCustomData(stack);
            components.add(Component.translatable("item.hbmsntm.obj_designator.pos_target").withStyle(ChatFormatting.GRAY));
            components.add(Component.literal("X: " + tag.getInt("x")).withStyle(ChatFormatting.GRAY));
            components.add(Component.literal("Z: " + tag.getInt("z")).withStyle(ChatFormatting.GRAY));
        } else {
            components.add(Component.translatable("item.hbmsntm.obj_designator.pos_select").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public boolean isReady(Level level, ItemStack stack, BlockPos pos) {
        return TagsUtil.hasCustomData(stack);
    }

    @Override
    public Vec3 getCoords(Level level, ItemStack stack, BlockPos pos) {
        CompoundTag tag = TagsUtil.getCustomData(stack);
        return new Vec3(tag.getInt("x"), 0, tag.getInt("z"));
    }
}
