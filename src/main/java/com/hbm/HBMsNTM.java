package com.hbm;

import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blocks.ModBlocks;
import com.hbm.config.ModConfigs;
import com.hbm.creativetabs.ModCreativeTabs;
import com.hbm.entity.ModEntities;
import com.hbm.inventory.ModMenus;
import com.hbm.items.ModItems;
import com.hbm.lib.ModAttachments;
import com.hbm.lib.ModEffect;
import com.hbm.lib.ModSounds;
import com.hbm.packets.PacketDispatcher;
import com.hbm.world.biome.ModBiomes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(HBMsNTM.MODID)
public class HBMsNTM {
    public static final String MODID = "hbmsntm";
    //HBM's Beta Naming Convention:
    //V T (X)
    //V -> next release version
    //T -> build type
    //X -> days since 10/10/2010
    public static final String VERSION = "0.0.4 ALPHA (5489)";
    public static final Logger LOGGER = LoggerFactory.getLogger("Hbm's NTM");

    public HBMsNTM(IEventBus modEventBus, ModContainer modContainer) {
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
        modEventBus.addListener(PacketDispatcher::registerPackets);

        ModConfigs.register(modContainer);
    }
}


