package com.hbm.main;

import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.config.MainConfig;
import com.hbm.entity.ModEntityTypes;
import com.hbm.inventory.ModCreativeTabs;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.NtmItems;
import com.hbm.items.component.NtmDataComponents;
import com.hbm.lib.ModAttachments;
import com.hbm.lib.ModEffect;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.particle.ModParticles;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Mod(NuclearTechMod.MODID)
public class NuclearTechMod {
    public static final String MODID = "hbmsntm";
    public static ResourceLocation withDefaultNamespace(String path) { return ResourceLocation.fromNamespaceAndPath(MODID, path); }
    public static final String VERSION = "128A";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static File configDir;
    public static File configHbmDir;

    public NuclearTechMod(IEventBus modEventBus, ModContainer modContainer) {

        configDir = FMLPaths.CONFIGDIR.get().toFile();
        configHbmDir = new File(NuclearTechMod.configDir, "hbmConfig");

        if (!NuclearTechMod.configHbmDir.exists()) {
            NuclearTechMod.configHbmDir.mkdirs();
        }

        Fluids.init();
        NtmItems.register(modEventBus);
        NtmBlocks.register(modEventBus);
        NtmDataComponents.register(modEventBus);
        ModEntityTypes.register(modEventBus);
        NtmSoundEvents.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModAttachments.register(modEventBus);
        ModEffect.register(modEventBus);
        ModBlockEntityTypes.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModParticles.register(modEventBus);

        MainConfig.register(modContainer);
    }
}