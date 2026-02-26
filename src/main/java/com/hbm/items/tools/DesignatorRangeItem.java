package com.hbm.items.tools;

import com.hbm.items.IDesignatorItem;
import com.hbm.lib.Library;
import com.hbm.lib.ModSounds;
import com.hbm.util.RayTraceResult;
import com.hbm.util.TagsUtilDegradation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class DesignatorRangeItem extends Item implements IDesignatorItem {

    public DesignatorRangeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        RayTraceResult ray = Library.rayTrace(player, 300, 1);

        BlockPos pos = ray.getBlockPos();
        if (!level.isClientSide) {
            CompoundTag tag = TagsUtilDegradation.getTag(stack);
            tag.putInt("x", pos.getX());
            tag.putInt("z", pos.getZ());
            TagsUtilDegradation.putTag(stack, tag);

            player.displayClientMessage(Component.translatable(this.getDescriptionId() + ".posSet", pos.getX(), pos.getZ()), false);
            level.playSound(null, player.position.x, player.position.y, player.position.z, ModSounds.TECH_BLEEP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
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
