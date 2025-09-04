package com.hbm.item.grenade;

import com.hbm.HBMsNTM;
import com.hbm.entity.ModEntities;
import com.hbm.entity.grenade.EntityGrenadeGeneric;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class ItemGrenade extends Item {

    public ItemGrenade() {
        super(new Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            EntityGrenadeGeneric grenade = new EntityGrenadeGeneric(ModEntities.GRENADE.get(), level, player);
            grenade.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            level.addFreshEntity(grenade);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
