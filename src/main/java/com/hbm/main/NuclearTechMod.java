package com.hbm.main;

import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.config.NtmConfig;
import com.hbm.entity.NtmEntityTypes;
import com.hbm.fluids.NtmFluidTypes;
import com.hbm.fluids.NtmFluids;
import com.hbm.inventory.NtmCreativeTabs;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.NtmItems;
import com.hbm.items.component.NtmDataComponents;
import com.hbm.lib.ModAttachments;
import com.hbm.lib.ModEffect;
import com.hbm.particle.ModParticles;
import com.hbm.registry.NtmSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
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

    public static ServerProxy proxy;

    public static File configDir;
    public static File configHbmDir;

    public NuclearTechMod(IEventBus eventBus, ModContainer container) {
        proxy = FMLLoader.getDist().isClient() ? new ClientProxy() : new ServerProxy();

        configDir = FMLPaths.CONFIGDIR.get().toFile();
        configHbmDir = new File(NuclearTechMod.configDir, "hbmConfig");

        if(!NuclearTechMod.configHbmDir.exists()) NuclearTechMod.configHbmDir.mkdirs();

        NtmConfig.register(container);

        Fluids.init();
        NtmItems.register(eventBus);
        NtmBlocks.register(eventBus);
        NtmFluidTypes.register(eventBus);
        NtmFluids.register(eventBus);
        NtmDataComponents.register(eventBus);
        NtmEntityTypes.register(eventBus);
        NtmSoundEvents.register(eventBus);
        NtmCreativeTabs.register(eventBus);
        ModAttachments.register(eventBus);
        ModEffect.register(eventBus);
        NtmBlockEntityTypes.register(eventBus);
        NtmMenuTypes.register(eventBus);
        ModParticles.register(eventBus);
    }
}