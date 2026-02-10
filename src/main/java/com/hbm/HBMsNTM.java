package com.hbm;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blocks.ModBlocks;
import com.hbm.config.MainConfig;
import com.hbm.entity.ModEntityTypes;
import com.hbm.inventory.ModCreativeTabs;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.items.ModItems;
import com.hbm.items.datacomps.ModDataComponents;
import com.hbm.lib.ModAttachments;
import com.hbm.lib.ModEffect;
import com.hbm.lib.ModSounds;
import com.hbm.particle.ModParticles;
import com.hbm.world.biome.ModBiomes;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Mod(HBMsNTM.MODID)
public class HBMsNTM {
    public static final String MODID = "hbmsntm";
    public static ResourceLocation withDefaultNamespaceNT(String path) { return ResourceLocation.fromNamespaceAndPath(MODID, path); }
    // HBM's -Beta- ALPHA Naming Convention:
    // V T (X)
    // V -> next release version
    // T -> build type
    // X -> days since 10/10/2010
    public static final String VERSION = "0.0.10 ALPHA (5594)";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static File configDir;
    public static File configHbmDir;

    public HBMsNTM(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModEntityTypes.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModAttachments.register(modEventBus);
        ModEffect.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModBiomes.register(modEventBus);
        ModParticles.register(modEventBus);

        MainConfig.register(modContainer);
    }
}