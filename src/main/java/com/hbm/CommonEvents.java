package com.hbm;

import com.hbm.config.FalloutConfigJSON;
import com.hbm.config.MainConfig;
import com.hbm.entity.ModEntities;
import com.hbm.entity.mob.CreeperNuclear;
import com.hbm.entity.mob.Duck;
import com.hbm.handler.EntityEffectHandler;
import com.hbm.handler.HTTPHandler;
import com.hbm.hazard.HazardRegistry;
import com.hbm.hazard.HazardSystem;
import com.hbm.inventory.ModMenus;
import com.hbm.inventory.gui.GUICrateIron;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
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
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (MainConfig.COMMON.ENABLE_MOTD.get()) {
            Player player = event.getEntity();

            player.sendSystemMessage(Component.literal("Loaded world with Hbm's Nuclear Tech Mod " + HBMsNTM.VERSION + " for Minecraft 1.21.1!"));

            if (HTTPHandler.newVersion) {
                player.sendSystemMessage(
                        Component.literal("New version " + HTTPHandler.versionNumber + " is available! Click ")
                                .withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW))
                                .append(Component.literal("[here]")
                                        .withStyle(Style.EMPTY
                                                        .withColor(ChatFormatting.RED)
                                                        .withUnderlined(true)
                                                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/ohiomannnn/HBMsNTM-NEO-EDITION/releases"))
                                        )
                                ).append(Component.literal(" to download!").withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)))
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

    public static int polaroidID = 1;
    public static int generalOverride = 0;

    public static void RerollPal() {
        // Reroll Polaroid
        if (generalOverride > 0 && generalOverride < 19) {
            polaroidID = generalOverride;
        } else {
            do polaroidID = new Random().nextInt(18) + 1;
            while (polaroidID == 4 || polaroidID == 9);
        }
    }
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.IRON_CRATE.get(), GUICrateIron::new);
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ModCommands.registerCommandLivingProperties(event.getDispatcher());
    }
}