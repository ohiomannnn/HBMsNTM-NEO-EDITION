package com.hbm.main;

import com.hbm.blockentity.bomb.LaunchPadBaseBlockEntity;
import com.hbm.blockentity.machine.MachineRadarBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.commands.ChunkRadCommand;
import com.hbm.commands.LivingPropsCommand;
import com.hbm.commands.SatellitesCommand;
import com.hbm.config.FalloutConfigJSON;
import com.hbm.config.NtmConfig;
import com.hbm.entity.NtmEntityTypes;
import com.hbm.entity.missile.MissileAntiBallistic;
import com.hbm.entity.mob.CreeperNuclear;
import com.hbm.entity.mob.Duck;
import com.hbm.extprop.HbmPlayerAttachments;
import com.hbm.handler.EntityEffectHandler;
import com.hbm.handler.HTTPHandler;
import com.hbm.handler.HazmatRegistry;
import com.hbm.hazard.HazardRegistry;
import com.hbm.hazard.HazardSystem;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.inventory.screens.*;
import com.hbm.items.IEquipReceiver;
import com.hbm.items.weapon.sedna.factory.GunFactory;
import com.hbm.network.toclient.InformPlayer;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent.BreakEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = NuclearTechMod.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {

        // to make sure that foreign registered fluids are accounted for,
        // even when the reload listener is registered too late due to load order
        // IMPORTANT: fluids have to load before recipes. weird shit happens if not.
        Fluids.reloadFluids();
        FluidContainerRegistry.register();

        //the good stuff
        SerializableRecipe.registerAllHandlers();
        SerializableRecipe.initialize();

        HTTPHandler.loadStats();
        FalloutConfigJSON.initialize();
        DamageResistanceHandler.init();
        HazardRegistry.registerItems();
        HazmatRegistry.registerHazmats();
        ArmorUtil.register();
        Satellite.register();
        LaunchPadBaseBlockEntity.registerLaunchables();

        MachineRadarBlockEntity.registerEntityClasses();
        MachineRadarBlockEntity.registerConverters();

        event.enqueueWork(() -> {
            GunFactory.initCfg();
        });
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(NtmEntityTypes.DUCK.get(), Duck.createAttributes().build());
        event.put(NtmEntityTypes.CREEPER_NUCLEAR.get(), CreeperNuclear.createAttributes().build());
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
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if(event.getSlot() != EquipmentSlot.MAINHAND) return;

        if(!(event.getEntity() instanceof ServerPlayer player)) return;

        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();

        if(to.isEmpty()) return;
        if(!from.isEmpty() && from.getItem() == to.getItem()) return;

        if(to.getItem() instanceof IEquipReceiver receiver) {
            receiver.onEquip(player, to);
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
                        level.setBlock(offsetPos, NtmBlocks.GAS_COAL.get().defaultBlockState(), 3);
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
        event.register(NtmMenuTypes.MACHINE_SOLDERING_STATION.get(), MachineSolderingStationScreen::new);
        event.register(NtmMenuTypes.MACHINE_OIL_DERRICK.get(), MachineOilDerrickScreen::new);
        event.register(NtmMenuTypes.MACHINE_REFINERY.get(), MachineRefineryScreen::new);
        event.register(NtmMenuTypes.SAT_LINKER.get(), MachineSatLinkerScreen::new);
        event.register(NtmMenuTypes.ANVIL.get(), AnvilMenuScreen::new);

        event.register(NtmMenuTypes.FLUID_TANK.get(), MachineFluidTankScreen::new);

        event.register(NtmMenuTypes.ASSEMBLY_MACHINE.get(), MachineAssemblyMachineScreen::new);
        event.register(NtmMenuTypes.PRESS.get(), MachinePressScreen::new);

        event.register(NtmMenuTypes.BATTERY_SOCKET.get(), BatterySocketScreen::new);
        event.register(NtmMenuTypes.BATTERY_REDD.get(), BatteryREDDScreen::new);

        event.register(NtmMenuTypes.NUKE_GADGET.get(), NukeGadgetScreen::new);
        event.register(NtmMenuTypes.NUKE_LITTLE_BOY.get(), NukeLittleBoyScreen::new);
        event.register(NtmMenuTypes.NUKE_FAT_MAN.get(), NukeFatManScreen::new);
        event.register(NtmMenuTypes.NUKE_IVY_MIKE.get(), NukeIvyMikeScreen::new);
        event.register(NtmMenuTypes.NUKE_TSAR_BOMBA.get(), NukeTsarBombaScreen::new);
        event.register(NtmMenuTypes.NUKE_PROTOTYPE.get(), NukePrototypeScreen::new);
        event.register(NtmMenuTypes.NUKE_FLEIJA.get(), NukeFleijaScreen::new);
        event.register(NtmMenuTypes.NUKE_SOLINIUM.get(), NukeSoliniumScreen::new);
        event.register(NtmMenuTypes.NUKE_N2.get(), NukeN2Screen::new);
        event.register(NtmMenuTypes.NUKE_FSTBMB.get(), NukeFstbmbScreen::new);

        event.register(NtmMenuTypes.LAUNCH_PAD_LARGE.get(), LaunchPadLargeScreen::new);
        event.register(NtmMenuTypes.SOYUZ_LAUNCHER.get(), SoyuzLauncherScreen::new);
    }
}
