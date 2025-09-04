package com.hbm;

import com.hbm.entity.ModEntities;
import com.hbm.entity.mob.EntityDuck;
import com.hbm.extprop.HbmLivingProps;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static com.hbm.extprop.HbmLivingProps.getData;

@EventBusSubscriber(modid = HBMsNTM.MODID)
public class CommonEvents {
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DUCK.get(), EntityDuck.createAttributes().build());
    }
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getEntity();
        var props = HbmLivingProps.getData(player);

        getData(player);
        HBMsNTM.LOGGER.info("[DEBUG] {}", props.serializeNBT());
    }
}