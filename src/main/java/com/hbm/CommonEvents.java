package com.hbm;

import com.hbm.entity.ModEntities;
import com.hbm.entity.mob.EntityDuck;
import com.hbm.handler.EntityEffectHandler;
import com.hbm.hazard.HazardRegistry;
import com.hbm.hazard.HazardSystem;
import com.hbm.inventory.ModMenus;
import com.hbm.inventory.gui.GUICrateIron;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.Random;

@EventBusSubscriber(modid = HBMsNTM.MODID)
public class CommonEvents {
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DUCK.get(), EntityDuck.createAttributes().build());
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        HBMsNTM.LOGGER.debug("pol = {}", polaroidID);
        HazardRegistry.registerItems();
    }

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();

        if (entity.level().isClientSide) return;

        if (entity instanceof Player player) {
            HazardSystem.updatePlayerInventory(player);
        }
        if (entity instanceof ItemEntity itemEntity) {
            HazardSystem.updateDroppedItem(itemEntity);
        }
        if (entity instanceof LivingEntity livingEntity) {
            HazardSystem.updateLivingInventory(livingEntity);
            EntityEffectHandler.tick(livingEntity);
        }
    }
    public static int polaroidID = 1;
    public static int generalOverride = 0;

    public static void RerollPal() {
        // Reroll Polaroid
        if(generalOverride > 0 && generalOverride < 19) {
            polaroidID = generalOverride;
        } else {
            do polaroidID = new Random().nextInt(18) + 1;
            while (polaroidID == 4 || polaroidID == 9);
        }
    }
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.IRON_CRATE.get(), GUICrateIron::new);
    }
}