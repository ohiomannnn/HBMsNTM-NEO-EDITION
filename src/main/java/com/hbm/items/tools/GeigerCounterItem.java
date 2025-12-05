package com.hbm.items.tools;

import com.hbm.extprop.LivingProperties;
import com.hbm.lib.ModSounds;
import com.hbm.util.ContaminationUtil;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class GeigerCounterItem extends Item {

    public GeigerCounterItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide) {
            if (entity instanceof LivingEntity user) {
                RandomSource rand = level.random;
                float rads = LivingProperties.getRadBuf(user);
                if (level.getGameTime() % 5 == 0) {
                    if (rads > 1E-5) {
                        List<Integer> list = new ArrayList<>();

                        if (rads < 1) list.add(0);
                        if (rads < 5) list.add(0);
                        if (rads < 10) list.add(1);
                        if (rads > 5 && rads < 15) list.add(2);
                        if (rads > 10 && rads < 20) list.add(3);
                        if (rads > 15 && rads < 25) list.add(4);
                        if (rads > 20 && rads < 30) list.add(5);
                        if (rads > 25) list.add(6);

                        int r = list.get(rand.nextInt(list.size()));

                        if (r > 0) {
                            switch (r) {
                                case 1 -> level.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.GEIGER1, SoundSource.AMBIENT, 1.0F, 1.0F);
                                case 2 -> level.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.GEIGER2, SoundSource.AMBIENT, 1.0F, 1.0F);
                                case 3 -> level.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.GEIGER3, SoundSource.AMBIENT, 1.0F, 1.0F);
                                case 4 -> level.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.GEIGER4, SoundSource.AMBIENT, 1.0F, 1.0F);
                                case 5 -> level.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.GEIGER5, SoundSource.AMBIENT, 1.0F, 1.0F);
                                case 6 -> level.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.GEIGER6, SoundSource.AMBIENT, 1.0F, 1.0F);
                            }
                        }
                    } else if (rand.nextInt(50) == 0) {
                        int i = 1 + rand.nextInt(1);
                        switch (i) {
                            case 1 -> level.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.GEIGER1.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                            case 2 -> level.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.GEIGER2.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            ContaminationUtil.printGeigerData(player);
        }

        return InteractionResultHolder.pass(stack);
    }
}