package com.hbm.items.special;

import com.hbm.entity.ModEntities;
import com.hbm.entity.mob.EntityDuck;
import com.hbm.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class EntitySpawnerItem extends Item {

    public EntitySpawnerItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        BlockPos pos = ctx.getClickedPos().relative(ctx.getClickedFace());

        if (!level.isClientSide) {
            Entity entity = spawnCreature(level, ctx.getItemInHand(), pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

            if (entity != null) {
                if (entity instanceof LivingEntity living) {

                    Component name = ctx.getItemInHand().getHoverName();
                    living.setCustomName(name);
                }

                if (player != null && !player.getAbilities().instabuild) {
                    ctx.getItemInHand().shrink(1);
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            BlockHitResult hit = getPlayerPOVHitResult(level, player, net.minecraft.world.level.ClipContext.Fluid.ANY);

            if (hit.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hit.getBlockPos();

                if (level.getFluidState(pos).isEmpty()) {
                    return InteractionResultHolder.pass(stack);
                }

                Entity entity = spawnCreature(level, stack, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

                if (entity != null) {
                    if (entity instanceof LivingEntity living) {

                        Component name = stack.getHoverName();
                        living.setCustomName(name);
                    }

                    if (player != null && !player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private Entity spawnCreature(Level level, ItemStack stack, double x, double y, double z) {
        Entity entity = null;

//        if (stack.is(ModItems.SPAWN_CHOPPER.get())) {
//            entity = new EntityHunterChopper(level);
//        }
//        if (stack.is(ModItems.SPAWN_WORM.get())) {
//            entity = new EntityBOTPrimeHead(level);
//        }
//        if (stack.is(ModItems.SPAWN_UFO.get())) {
//            entity = new EntityUFO(level);
//            ((EntityUFO) entity).scanCooldown = 100;
//            y += 35;
//        }
        if (stack.is(ModItems.DUCK_SPAWN_EGG.get())) {
            EntityDuck duck = ModEntities.DUCK.get().create(level);
            if (duck != null) {
                duck.moveTo(x, y, z, level.random.nextFloat() * 360F, 0.0F);
                level.addFreshEntity(duck);
            }
        }

        if (entity != null) {
            if (entity instanceof LivingEntity living) {
                entity.moveTo(x, y, z, Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
                living.setYHeadRot(living.getYRot());
                living.setYBodyRot(living.getYRot());
                level.addFreshEntity(entity);
            }
        }

        return entity;
    }

//    @Override
//    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
//        if (stack.is(ModItems.SPAWN_WORM.get())) {
//            tooltip.add(Component.literal("Without a player in survival mode"));
//            tooltip.add(Component.literal("to target, he struggles around a lot."));
//            tooltip.add(Component.empty());
//            tooltip.add(Component.literal("He's doing his best so please show him"));
//            tooltip.add(Component.literal("some consideration."));
//        }
//    }
}
