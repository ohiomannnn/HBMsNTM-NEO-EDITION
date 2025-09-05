package com.hbm;

import com.hbm.block.ModBlocks;
import com.hbm.config.ServerConfig;
import com.hbm.creativetabs.ModCreativeTabs;
import com.hbm.entity.ModEntities;
import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.item.ModItems;
import com.hbm.lib.ModAttachments;
import com.hbm.lib.ModCommands;
import com.hbm.lib.ModDamageSource;
import com.hbm.lib.ModSounds;
import com.hbm.potions.ModPotions;
import com.hbm.util.ContaminationUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.hbm.handler.radiation.ChunkRadiationManager.initProxy;
import static com.hbm.lib.ModDamageSource.BANG;
import static com.hbm.lib.ModDamageSource.DIGAMMA;

@Mod(HBMsNTM.MODID)
public class HBMsNTM {
    public static final String MODID = "hbmsntm";
    public static final Logger LOGGER = LoggerFactory.getLogger("hbmsntm");

    public static final ChunkRadiationManager radiationManager = new ChunkRadiationManager();

    public HBMsNTM(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModAttachments.register(modEventBus);
        ModPotions.register(modEventBus);

        NeoForge.EVENT_BUS.register(radiationManager);
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::ConfigLoad);

        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        ModCommands.registerCommandNTMEntityFields(event.getDispatcher());
    }
    public void ConfigLoad(ModConfigEvent.Loading event) {
        initProxy();
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        for (ServerLevel world : server.getAllLevels()) {
            for (ServerPlayer player : world.players()) {
                double rads = Math.floor(ChunkRadiationManager.proxy.getRadiation(
                        world,
                        player.blockPosition().getX(),
                        player.blockPosition().getY(),
                        player.blockPosition().getZ()
                ) * 10) / 10D;

                ContaminationUtil.contaminate(
                        player,
                        ContaminationUtil.HazardType.RADIATION,
                        ContaminationUtil.ContaminationType.CREATIVE,
                        (float) rads
                );
                if (rads >= 50) {
                    // наносим урон
                    DamageSource src = new DamageSource(
                            player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DIGAMMA)
                    );
                    player.hurt(src, Float.MAX_VALUE);
                }
            }
        }
    }
}

