package com.hbm.main;

import com.hbm.config.NtmConfig;
import com.hbm.handler.pollution.PollutionHandler.PollutionType;
import com.hbm.network.PermaSyncHandler;
import com.hbm.render.item.weapon.sedna.ItemRenderWeaponBase;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.event.ViewportEvent.RenderFog;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

@EventBusSubscriber(value = Dist.CLIENT, modid = NuclearTechMod.MODID)
public class NtmEventHandlerRenderer {

    @SubscribeEvent
    public static void thickenFov(ViewportEvent.ComputeFov event) {

        Player player = Minecraft.getInstance().player;
        if(player == null) return;

        ItemStack stack = player.getMainHandItem();
        if(IClientItemExtensions.of(stack).getCustomRenderer() instanceof ItemRenderWeaponBase gunRender) {
            event.setFOV(gunRender.getViewFOV(stack, event.getFOV(), event.getPartialTick()));
        }
    }

    private static float renderSoot = 0;

    @SubscribeEvent
    public static void worldTick(ClientTickEvent.Pre event) {

        if(NtmConfig.COMMON.ENABLE_SOOT_FOG.get()) {

            float step = 0.05F;
            float soot = PermaSyncHandler.pollution[PollutionType.SOOT.ordinal()];

            if(Math.abs(renderSoot - soot) < step) {
                renderSoot = soot;
            } else if(renderSoot < soot) {
                renderSoot += step;
            } else if(renderSoot > soot) {
                renderSoot -= step;
            }
        }
    }

    @SubscribeEvent
    public static void thickenFog(RenderFog event) {
        float soot = (float) (renderSoot - NtmConfig.COMMON.SOOT_FOG_THRESHOLD.get());
        if(soot > 0 && NtmConfig.COMMON.ENABLE_SOOT_FOG.get()) {

            float farPlaneDistance = (float) (Minecraft.getInstance().options.renderDistance().get() * 16);
            float fogDist = farPlaneDistance / (1 + soot * 5F / (float) NtmConfig.COMMON.SOOT_FOG_DIVISOR.getAsDouble());
            event.setNearPlaneDistance(0);
            event.setFarPlaneDistance(fogDist);
        }
    }

}
