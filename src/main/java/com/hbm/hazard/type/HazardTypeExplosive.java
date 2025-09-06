package com.hbm.hazard.type;

import java.util.List;

import com.hbm.config.ServerConfig;
import com.hbm.hazard.modifier.HazardModifier;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class HazardTypeExplosive extends HazardTypeBase {

    @Override
    public void onUpdate(LivingEntity target, float level, ItemStack stack) {

        if (ServerConfig.DISABLE_EXPLOSIVE.getAsBoolean())
            return;

        BlockPos pos = target.blockPosition();
        BlockState state = target.level().getBlockState(pos);

        if (!target.level().isClientSide
                && (target.isOnFire() || target.isInLava() || state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE))
                && stack.getCount() > 0) {

            stack.setCount(0);
            target.level().explode(null, target.getX(), target.getY() + 2, target.getZ(), level, Level.ExplosionInteraction.NONE);
        }
    }

    @Override
    public void updateEntity(ItemEntity item, float level) {

        if (ServerConfig.DISABLE_EXPLOSIVE.getAsBoolean())
            return;

        if (item.isOnFire() || item.isInLava()) {
            item.discard();
            item.level().explode(null, item.getX(), item.getY(), item.getZ(), level, Level.ExplosionInteraction.NONE);
        }
    }

    @Override
    public void addHazardInformation(Player player, List list, float level, ItemStack stack, List<HazardModifier> modifiers) {
        @SuppressWarnings("unchecked") // no
        List<Component> components = (List<Component>) list;

        components.add(Component.literal("[" + Component.translatable("trait.explosive").getString() + "]")
                .withStyle(ChatFormatting.RED));
    }
}