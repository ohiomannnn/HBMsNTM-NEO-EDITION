package com.hbm;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.ClientConfig;
import com.hbm.config.ServerConfig;
import com.hbm.creativetabs.ModCreativeTabs;
import com.hbm.entity.ModEntities;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.items.ModItems;
import com.hbm.lib.ModAttachments;
import com.hbm.lib.ModCommands;
import com.hbm.lib.ModEffect;
import com.hbm.lib.ModSounds;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(HBMsNTM.MODID)
public class HBMsNTM {
    public static final String MODID = "hbmsntm";
    public static final Logger LOGGER = LoggerFactory.getLogger("hbmsntm");
    public static final ChunkRadiationManager radiationManager = new ChunkRadiationManager();

    public HBMsNTM (IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModAttachments.register(modEventBus);
        ModEffect.register(modEventBus);

        NeoForge.EVENT_BUS.register(radiationManager);
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::onConfigLoad);

        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        ModCommands.registerCommandNTMEntityFields(event.getDispatcher());
    }
    private void onConfigLoad(final ModConfigEvent.Loading event) {
        if (event.getConfig().getModId().equals("hbmsntm")) {
            ChunkRadiationManager.initProxy();
        }
    }
}


