package com.hbm;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.ServerConfig;
import com.hbm.creativetabs.ModCreativeTabs;
import com.hbm.entity.ModEntities;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.inventory.ModMenus;
import com.hbm.items.ModItems;
import com.hbm.lib.ModAttachments;
import com.hbm.lib.ModCommands;
import com.hbm.lib.ModEffect;
import com.hbm.lib.ModSounds;
import com.hbm.blockentity.ModBlockEntities;
import com.hbm.world.biome.ModBiomes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
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
        ModBlockEntities.register(modEventBus);
        ModMenus.register(modEventBus);
        ModBiomes.register(modEventBus);

        NeoForge.EVENT_BUS.register(radiationManager);
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::onConfigLoad);

        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        ModCommands.registerCommandLivingProperties(event.getDispatcher());
    }
    private void onConfigLoad(final ModConfigEvent.Loading event) {
        ChunkRadiationManager.initProxy();
    }
}


