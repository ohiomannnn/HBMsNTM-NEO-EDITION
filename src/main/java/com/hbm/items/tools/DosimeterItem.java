package com.hbm.items.tools;

import com.hbm.extprop.LivingProperties;
import com.hbm.lib.ModSounds;
import com.hbm.util.ContaminationUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
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
import java.util.Random;

import static com.hbm.items.tools.GeigerCounterItem.*;

public class DosimeterItem extends Item {
    public DosimeterItem(Properties properties) {
        super(properties);
    }

    private final Random rand = new Random();

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide || !(entity instanceof LivingEntity living))
            return;

        float x = LivingProperties.getRadBuf(living);

        if (level.getGameTime() % 5 == 0) {
            if (x > 1E-5) {
                List<Integer> list = new ArrayList<>();

                if (x < 0.5) list.add(0);
                if (x < 1) list.add(1);
                if (x >= 0.5 && x < 2) list.add(2);
                if (x >= 2) list.add(3);

                int r = list.get(rand.nextInt(list.size()));

                if (r > 0) {
                    switch (r) {
                        case 1 -> playSnd(level, entity, ModSounds.GEIGER1.get());
                        case 2 -> playSnd(level, entity, ModSounds.GEIGER2.get());
                        case 3 -> playSnd(level, entity, ModSounds.GEIGER3.get());
                    }
                }
            } else if (rand.nextInt(100) == 0) {
                playSoundRand(level, entity, 1 + rand.nextInt(1));
            }
        }
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            ContaminationUtil.printDosimeterData(player);
            level.playSound(null, player.blockPosition(), ModSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        }

        return InteractionResultHolder.pass(stack);
    }
}
