package com.hbm.blocks.generic;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SpeedyBlock extends Block {

    private final double speed;

    public SpeedyBlock(double speed, Properties properties) {
        super(properties);

        this.speed = speed;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {

        if(entity instanceof Player player) {
            Vec3 delta = player.getDeltaMovement();

            if(delta.horizontalDistanceSqr() > 0.0) {
                player.setDeltaMovement(delta.scale(speed));
                player.hasImpulse = true;
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("block.hbmsntm.obj_speedy.desc", (Mth.floor((speed - 1) * 100))).withStyle(ChatFormatting.BLUE));
    }
}
