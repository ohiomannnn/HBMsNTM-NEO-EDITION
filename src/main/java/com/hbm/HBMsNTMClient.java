package com.hbm;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.bomb.BalefireBlock;
import com.hbm.entity.ModEntities;
import com.hbm.entity.mob.rendrer.EntityDuckRenderer;
import com.hbm.handler.gui.GeigerGUI;
import com.hbm.hazard.HazardSystem;
import com.hbm.items.ModItems;
import com.hbm.packets.PacketsClient;
import com.hbm.particle.*;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.awt.*;
import java.util.List;

@Mod(value = HBMsNTM.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class HBMsNTMClient {
    public HBMsNTMClient(IEventBus modBus, ModContainer modContainer) {
        //config screen
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        //particles
        ModParticles.register(modBus);
        //gui
        modBus.addListener(GeigerGUI::RegisterGuiLayers);

        modBus.addListener(this::onClientSetup);
//        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);

        modBus.addListener(PacketsClient::registerPackets);
    }
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.DUCK.get(), EntityDuckRenderer::new);
    }
    @SubscribeEvent
    public static void drawTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> list = event.getToolTip();

        HazardSystem.addFullTooltip(stack, list);
    }
    private void onClientSetup(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.SOME_PART.get(), ParticleSomePart.Provider::new);
        event.registerSpriteSet(ModParticles.MUKE_CLOUD.get(), ParticleMukeCloud.Provider::new);
        event.registerSpriteSet(ModParticles.EXPLOSION_SMALL.get(), ParticleExplosionSmall.Provider::new);
        event.registerSpriteSet(ModParticles.MUKE_WAVE.get(), ParticleMukeWave.Provider::new);
        event.registerSpriteSet(ModParticles.COOLING_TOWER.get(), ParticleCoolingTower.Provider::new);
        ItemProperties.register(ModItems.POLAROID.get(),
                ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "polaroid_id"),
                (stack, level, entity, seed) -> ModItems.polaroidID);
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            if (state.hasProperty(BalefireBlock.AGE)) {
                int age = state.getValue(BalefireBlock.AGE);
                return Color.HSBtoRGB(0F, 0F, 1F - age / 30F);
            }
            return 0xFFFFFF;
        }, ModBlocks.BALEFIRE.get());
    }
}
