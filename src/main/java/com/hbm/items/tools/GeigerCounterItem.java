package com.hbm.items.tools;

import com.hbm.HBMsNTM;
import com.hbm.extprop.LivingProperties;
import com.hbm.lib.ModSounds;
import com.hbm.util.ContaminationUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeigerCounterItem extends Item {
    public GeigerCounterItem(Properties properties) {
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

                if (x < 1) list.add(0);
                if (x < 5) list.add(0);
                if (x < 10) list.add(1);
                if (x > 5 && x < 15) list.add(2);
                if (x > 10 && x < 20) list.add(3);
                if (x > 15 && x < 25) list.add(4);
                if (x > 20 && x < 30) list.add(5);
                if (x > 25) list.add(6);

                int r = list.get(rand.nextInt(list.size()));

                if (r > 0) {
                    switch (r) {
                        case 1 -> playSnd(level, entity, ModSounds.GEIGER1.get());
                        case 2 -> playSnd(level, entity, ModSounds.GEIGER2.get());
                        case 3 -> playSnd(level, entity, ModSounds.GEIGER3.get());
                        case 4 -> playSnd(level, entity, ModSounds.GEIGER4.get());
                        case 5 -> playSnd(level, entity, ModSounds.GEIGER5.get());
                        case 6 -> playSnd(level, entity, ModSounds.GEIGER6.get());
                    }
                }
            } else if (rand.nextInt(50) == 0) {
                playSoundRand(level, entity, 1 + rand.nextInt(1));
            }
        }
    }

    // enough coding for today...
    protected static void playSnd(Level level, Entity entity, SoundEvent soundEvent) {
        level.playSound(null, entity.blockPosition(), soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
    protected static void playSoundRand(Level level, Entity entity, int i) {
        switch (i) {
            case 1 -> level.playSound(null, entity.blockPosition(), ModSounds.GEIGER1.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            case 2 -> level.playSound(null, entity.blockPosition(), ModSounds.GEIGER2.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (!player.isCrouching()) {
                ContaminationUtil.printGeigerData(player);
                level.playSound(null, player.blockPosition(), ModSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            } else {
                // TODO: make another item for this
                ContaminationUtil.printDiagnosticData(player);
                level.playSound(null, player.blockPosition(), ModSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            }
        }

        return InteractionResultHolder.pass(stack);
    }
}