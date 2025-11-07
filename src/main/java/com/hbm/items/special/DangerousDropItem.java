package com.hbm.items.special;

import com.hbm.HBMsNTM;
import com.hbm.config.MainConfig;
import com.hbm.explosion.vanillant.ExplosionVNT;
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
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity itemEntity) {
        Level level = itemEntity.level();

        if (level.isClientSide) return false;
        int lifespan = stack.getEntityLifespan(level);
        if (itemEntity.getAge() >= lifespan - 1) return false;

        String throwerName = "";
        if (TagsUtil.hasTag(stack)) throwerName = TagsUtil.getString(stack, "lastUser", "Somebody");

        if (itemEntity.getAge() > 5) {
            if (stack.is(ModItems.DETONATOR_DEADMAN.get())) {
                if (TagsUtil.hasTag(stack)) {
                    int x = TagsUtil.getInt(stack, "x", 0);
                    int y = TagsUtil.getInt(stack, "y", 0);
                    int z = TagsUtil.getInt(stack, "z", 0);

                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = level.getBlockState(pos).getBlock();
                    if (block instanceof IBomb bomb) {
                        bomb.explode(level, pos);

                        if (MainConfig.COMMON.ENABLE_EXTENDED_LOGGING.get()) {
                            HBMsNTM.LOGGER.info("[DEAD MAN'S DETONATOR] {} detonated {} at {} / {} / {}!", throwerName, block.getName().getString(), x, y, z);
                        }
                    }
                }
                level.explode(null, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 0.0F, Level.ExplosionInteraction.NONE);
                itemEntity.discard();
                return true;
            }
            if (stack.is(ModItems.DETONATOR_DE.get()) && MainConfig.COMMON.DROP_DEAD_MANS_EXPLOSIVE.get()) {
                level.explode(null, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 15.0F, Level.ExplosionInteraction.TNT);
                itemEntity.discard();
                return true;
            }
        }

        if (itemEntity.onGround()) {
            if (stack.is(ModItems.CELL_ANTIMATTER.get()) && MainConfig.COMMON.DROP_CELL.get()) {
                new ExplosionVNT(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 5F).makeAmat().explode();
            }
        }

        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (this == ModItems.CELL_ANTIMATTER.get()) {
            tooltipComponents.add(Component.literal("Warning: Exposure to matter will"));
            tooltipComponents.add(Component.literal("lead to violent annihilation!"));
        }
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
            
            // funny part
            TagsUtil.setString(stack, "lastUser", player.getName().getString());

            if (!context.getLevel().isClientSide) {
                player.sendSystemMessage(Component.literal("Position set!"));
            }

            context.getLevel().playSound(null, player.blockPosition(), ModSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 2.0F, 1.0F);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
