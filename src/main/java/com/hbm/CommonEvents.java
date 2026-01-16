package com.hbm;

import com.hbm.blocks.ModBlocks;
import com.hbm.commands.ChunkRadCommand;
import com.hbm.commands.LivingPropsCommand;
import com.hbm.commands.SatellitesCommand;
import com.hbm.config.FalloutConfigJSON;
import com.hbm.config.MainConfig;
import com.hbm.entity.ModEntityTypes;
import com.hbm.entity.mob.CreeperNuclear;
import com.hbm.entity.mob.Duck;
import com.hbm.handler.EntityEffectHandler;
import com.hbm.handler.HTTPHandler;
import com.hbm.handler.HazmatRegistry;
import com.hbm.hazard.HazardRegistry;
import com.hbm.hazard.HazardSystem;
import com.hbm.inventory.ModMenuTypes;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.menus.BatteryREDDMenu;
import com.hbm.inventory.screens.BatteryREDDScreen;
import com.hbm.inventory.screens.BatterySocketScreen;
import com.hbm.inventory.screens.MachineSatLinkerScreen;
import com.hbm.inventory.screens.NukeFatManScreen;
import com.hbm.items.ModItems;
import com.hbm.saveddata.satellite.Satellite;
import com.hbm.uninos.UniNodespace;
import com.hbm.util.ArmorUtil;
import com.hbm.util.DamageResistanceHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent.BreakEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.io.File;

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
        Fluids.init();
        HazardRegistry.registerItems();
        HazmatRegistry.registerHazmats();
        ArmorUtil.register();
        Satellite.register();
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {

        // Networks! All of them!
        UniNodespace.updateNodespace(event.getServer());
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
                                .append(Component.translatable("message.hbmsntm.click_here").withStyle(Style.EMPTY.withColor(ChatFormatting.RED).withUnderlined(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/ohiomannnn/HBMsNTM-NEO-EDITION/releases"))))
                                .append(Component.translatable("message.hbmsntm.to_download").withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)))
                );
            }
        }
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.DUCK.get(), Duck.createAttributes().build());
        event.put(ModEntityTypes.CREEPER_NUCLEAR.get(), CreeperNuclear.createAttributes().build());
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
    public static void onBlockBreak(BreakEvent event) {
        BlockPos pos = event.getPos();
        Level level = (Level) event.getLevel();

        if (!level.isClientSide) {
            if (event.getState() == Blocks.COAL_ORE.defaultBlockState() || event.getState() == Blocks.DEEPSLATE_COAL_ORE.defaultBlockState() || event.getState() == Blocks.COAL_BLOCK.defaultBlockState()) {
                for (Direction dir : Direction.values()) {
                    BlockPos offsetPos = pos.relative(dir);

                    if (level.random.nextInt(2) == 0 && level.getBlockState(offsetPos).isAir()) {
                        level.setBlock(offsetPos, ModBlocks.GAS_COAL.get().defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        LivingPropsCommand.register(event.getDispatcher());
        SatellitesCommand.register(event.getDispatcher());
        ChunkRadCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.SAT_LINKER.get(), MachineSatLinkerScreen::new);

        event.register(ModMenuTypes.BATTERY_SOCKET.get(), BatterySocketScreen::new);
        event.register(ModMenuTypes.BATTERY_REDD.get(), BatteryREDDScreen::new);

        event.register(ModMenuTypes.NUKE_FATMAN.get(), NukeFatManScreen::new);
    }

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.ALLOY_SWORD);
            event.accept(ModItems.ALLOY_HELMET);
            event.accept(ModItems.ALLOY_CHESTPLATE);
            event.accept(ModItems.ALLOY_LEGGINGS);
            event.accept(ModItems.ALLOY_BOOTS);
        }
    }
}