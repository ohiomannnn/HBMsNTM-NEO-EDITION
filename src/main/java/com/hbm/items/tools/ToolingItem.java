package com.hbm.items.tools;

import api.hbm.block.IToolable;
import api.hbm.block.IToolable.ToolType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ToolingItem extends Item {

    protected ToolType type;

    public ToolingItem(ToolType type, Properties properties) {
        super(properties.stacksTo(1));

        this.type = type;

        type.register(new ItemStack(this));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos usedPos = context.getClickedPos();
        Direction usedDirection = context.getClickedFace();

        if (player == null) return InteractionResult.PASS;

        if (level.getBlockState(usedPos).getBlock() instanceof IToolable toolableBlock) {
            toolableBlock.onScrew(level, player, usedPos, usedDirection, this.type);

            if (this.getMaxDamage(new ItemStack(this)) > 0) {
                context.getItemInHand().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
