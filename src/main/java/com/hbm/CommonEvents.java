package com.hbm;

import com.hbm.config.FalloutConfigJSON;
import com.hbm.config.MainConfig;
import com.hbm.entity.ModEntities;
import com.hbm.entity.mob.CreeperNuclear;
import com.hbm.entity.mob.Duck;
import com.hbm.handler.EntityEffectHandler;
import com.hbm.handler.HTTPHandler;
import com.hbm.handler.HazmatRegistry;
import com.hbm.hazard.HazardRegistry;
import com.hbm.hazard.HazardSystem;
import com.hbm.items.special.PolaroidItem;
import com.hbm.lib.ModCommands;
import com.hbm.util.DamageResistanceHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.io.File;
import java.util.Random;

@EventBusSubscriber(modid = HBMsNTM.MODID)
public class CommonEvents {

    public static File configDir;
    public static File configHbmDir;

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {

        configDir = FMLPaths.CONFIGDIR.get().toFile();
        configHbmDir = new File(configDir, "hbmConfig");

        if (!configHbmDir.exists()) configHbmDir.mkdirs();

        HBMsNTM.LOGGER.info("Let us celebrate the fact that the logger finally works again!");

        HTTPHandler.loadStats();
        FalloutConfigJSON.initialize();
        DamageResistanceHandler.init();
        HazardRegistry.registerItems();
        HazmatRegistry.registerHazmats();

        HBMsNTM.LOGGER.info("Polaroid id = {}", PolaroidItem.polaroidID);
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (MainConfig.COMMON.ENABLE_MOTD.get()) {
            Player player = event.getEntity();

            player.sendSystemMessage(Component.translatable("message.hbmsntm.loaded", HBMsNTM.VERSION));

            if (HTTPHandler.newVersion) {
                player.sendSystemMessage(
                        Component.translatable("message.hbmsntm.new_version", HTTPHandler.versionNumber)
                                .withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW))
                                .append(Component.translatable("message.hbmsntm.click_here")
                                        .withStyle(Style.EMPTY
                                                .withColor(ChatFormatting.RED)
                                                .withUnderlined(true)
                                                .withClickEvent(new ClickEvent(
                                                        ClickEvent.Action.OPEN_URL,
                                                        "https://github.com/ohiomannnn/HBMsNTM-NEO-EDITION/releases"
                                                ))
                                ))
                                .append(Component.translatable("message.hbmsntm.to_download").withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)))
                );
            }
        }
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DUCK.get(), Duck.createAttributes().build());
        event.put(ModEntities.CREEPER_NUCLEAR.get(), CreeperNuclear.createAttributes().build());
    }

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player player) {
            HazardSystem.updatePlayerInventory(player);
        }
        if (entity instanceof ItemEntity itemEntity) {
            HazardSystem.updateDroppedItem(itemEntity);
        }
        if (entity instanceof LivingEntity livingEntity) {
            HazardSystem.updateLivingInventory(livingEntity);
            EntityEffectHandler.tick(livingEntity);
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ModCommands.registerCommandLivingProperties(event.getDispatcher());
    }
}