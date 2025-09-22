package com.hbm.items.special;

import com.hbm.HBMsNTM;
import com.hbm.config.ServerConfig;
import com.hbm.interfaces.IBomb;
import com.hbm.items.ModItems;
import com.hbm.lib.ModSounds;
import com.hbm.util.TagsUtil;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class DangerousDropItem extends Item {
    public DangerousDropItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        Level level = entity.level();

        if (level.isClientSide) return false;
        int lifespan = stack.getEntityLifespan(level);
        if (entity.getAge() >= lifespan - 1) return false;

        if (entity.getAge() < 5) return false;

        if (stack.is(ModItems.DETONATOR_DEADMAN.get())) {
            if (TagsUtil.hasTag(stack)) {
                int x = TagsUtil.getInt(stack, "x", 0);
                int y = TagsUtil.getInt(stack, "y", 0);
                int z = TagsUtil.getInt(stack, "z", 0);

                Block block = level.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block instanceof IBomb) {
                    ((IBomb) block).explode(level, x, y, z);

                    if (ServerConfig.ENABLE_EXTENDED_LOGGING.getAsBoolean())
                        HBMsNTM.LOGGER.info("[DET] Tried to detonate block at " + x + " / " + y + " / " + z + " by dead man's switch!");
                }
            }
            level.explode(null, entity.getX(), entity.getY(), entity.getZ(), 0.0F, Level.ExplosionInteraction.NONE);
            entity.discard();
            return true;
        }
        if (stack.is(ModItems.DETONATOR_DE.get())) {
            level.explode(null, entity.getX(), entity.getY(), entity.getZ(), 15.0F, Level.ExplosionInteraction.MOB);

            if (ServerConfig.ENABLE_EXTENDED_LOGGING.getAsBoolean())
                HBMsNTM.LOGGER.info("[DET] Detonated dead man's explosive at " + ((int) entity.getX()) + " / " + ((int) entity.getY()) + " / " + ((int) entity.getZ()) + "!");
            return true;
        }

        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (this == ModItems.DETONATOR_DEADMAN.get()) {
            tooltipComponents.add(Component.literal("Shift right-click to set position,"));
            tooltipComponents.add(Component.literal("drop to detonate!"));
            if (!TagsUtil.hasTag(stack)) {
                tooltipComponents.add(Component.literal("No position set!"));
            } else {
                tooltipComponents.add(Component.literal("Set position to "
                                + TagsUtil.getInt(stack, "x", 0) + ", "
                                + TagsUtil.getInt(stack, "y", 0) + ", "
                                + TagsUtil.getInt(stack, "z", 0)
                ));
            }
        }
        if (this == ModItems.DETONATOR_DE.get()) {
            tooltipComponents.add(Component.literal("Explodes when dropped!"));
        }

        tooltipComponents.add(Component.literal("[" + I18nUtil.resolveKey("trait.drop") + "]").withStyle(ChatFormatting.RED));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (this != ModItems.DETONATOR_DEADMAN.get()) {
            return super.onItemUseFirst(stack, context);
        }
        Player player = context.getPlayer();

        if (player.isCrouching()) {
            TagsUtil.setInt(stack, "x", context.getClickedPos().getX());
            TagsUtil.setInt(stack, "y", context.getClickedPos().getY());
            TagsUtil.setInt(stack, "z", context.getClickedPos().getZ());

            if (!context.getLevel().isClientSide) {
                player.sendSystemMessage(Component.literal("Position set!"));
            }

            context.getLevel().playSound(null, player.blockPosition(), ModSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 2.0F, 1.0F);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
