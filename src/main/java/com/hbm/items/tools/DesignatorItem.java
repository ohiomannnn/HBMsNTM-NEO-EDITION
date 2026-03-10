package com.hbm.items.tools;

import com.hbm.items.IDesignatorItem;
import com.hbm.lib.ModSounds;
import com.hbm.util.TagsUtilDegradation;
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
        if (player == null) return InteractionResult.PASS;
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();

        if (!level.isClientSide) {
            CompoundTag tag = TagsUtilDegradation.getTag(stack);
            tag.putInt("x", pos.getX());
            tag.putInt("z", pos.getZ());
            TagsUtilDegradation.putTag(stack, tag);

            player.displayClientMessage(Component.translatable(this.getDescriptionId() + ".posSet", pos.getX(), pos.getZ()), false);
            level.playSound(null, player.position.x, player.position.y, player.position.z, ModSounds.TECH_BLEEP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        if (TagsUtilDegradation.containsAnyTag(stack)) {
            CompoundTag tag = TagsUtilDegradation.getTag(stack);
            components.add(Component.translatable("item.hbmsntm.designator.desc.targetPos").withStyle(ChatFormatting.GRAY));
            components.add(Component.literal("X: " + tag.getInt("x")).withStyle(ChatFormatting.GRAY));
            components.add(Component.literal("Z: " + tag.getInt("z")).withStyle(ChatFormatting.GRAY));
        } else {
            components.add(Component.translatable("item.hbmsntm.designator.desc.selectTarget").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public boolean isReady(Level level, ItemStack stack, BlockPos pos) {
        return TagsUtilDegradation.containsAnyTag(stack);
    }

    @Override
    public Vec3 getCoords(Level level, ItemStack stack, BlockPos pos) {
        CompoundTag tag = TagsUtilDegradation.getTag(stack);
        return new Vec3(tag.getInt("x"), 0, tag.getInt("z"));
    }
}
