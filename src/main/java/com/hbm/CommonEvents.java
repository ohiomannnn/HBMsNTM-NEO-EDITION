package com.hbm;

import com.hbm.entity.ModEntities;
import com.hbm.entity.mob.EntityDuck;
import com.hbm.extprop.HbmLivingProps;

import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.hazard.HazardRegistry;
import com.hbm.hazard.HazardSystem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import static com.hbm.extprop.HbmLivingProps.getData;
import static com.hbm.items.ModItems.polaroidID;

@EventBusSubscriber(modid = HBMsNTM.MODID)
public class CommonEvents {
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DUCK.get(), EntityDuck.createAttributes().build());
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        HBMsNTM.LOGGER.info("pol = {}", polaroidID);
        var player = event.getEntity();
        var props = HbmLivingProps.getData(player);

        getData(player);
        HBMsNTM.LOGGER.info("[DEBUG] {}", props.serializeNBT());
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
        }
    }
    @SubscribeEvent
    public static void ConfigLoad(ModConfigEvent.Loading event) {
        ChunkRadiationManager.initProxy();
        HazardRegistry.registerItems();
    }
}