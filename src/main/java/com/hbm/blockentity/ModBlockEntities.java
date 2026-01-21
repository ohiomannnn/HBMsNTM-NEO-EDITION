package com.hbm.blockentity;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.bomb.*;
import com.hbm.blockentity.machine.MachinePressBlockEntity;
import com.hbm.blockentity.machine.MachineSatLinkerBlockEntity;
import com.hbm.blockentity.machine.DecontaminatorBlockEntity;
import com.hbm.blockentity.machine.GeigerBlockEntity;
import com.hbm.blockentity.machine.storage.*;
import com.hbm.blockentity.network.CableBlockEntityBaseNT;
import com.hbm.blocks.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("DataFlowIssue") // kill yourself
public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, HBMsNTM.MODID);

    // Machines
    public static final Supplier<BlockEntityType<MachineSatLinkerBlockEntity>> MACHINE_SATLINKER = REGISTER.register(
            "machine_satlinker",
            () -> BlockEntityType.Builder.of(
                            MachineSatLinkerBlockEntity::new,
                            ModBlocks.MACHINE_SATLINKER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<CrateIronBlockEntity>> CRATE_IRON = REGISTER.register(
            "crate_iron",
            () -> BlockEntityType.Builder.of(
                            CrateIronBlockEntity::new,
                            ModBlocks.MACHINE_SATLINKER.get())
                    .build(null));
    public static final Supplier<BlockEntityType<CrateSteelBlockEntity>> CRATE_STEEL = REGISTER.register(
            "crate_steel",
            () -> BlockEntityType.Builder.of(
                            CrateSteelBlockEntity::new,
                            ModBlocks.MACHINE_SATLINKER.get())
                    .build(null));
    public static final Supplier<BlockEntityType<CrateDeshBlockEntity>> CRATE_DESH = REGISTER.register(
            "crate_desh",
            () -> BlockEntityType.Builder.of(
                            CrateDeshBlockEntity::new,
                            ModBlocks.MACHINE_SATLINKER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachinePressBlockEntity>> PRESS = REGISTER.register(
            "press",
            () -> BlockEntityType.Builder.of(
                            MachinePressBlockEntity::new,
                            ModBlocks.MACHINE_PRESS.get())
                    .build(null));

    public static final Supplier<BlockEntityType<MachineFluidTankBlockEntity>> FLUID_TANK = REGISTER.register(
            "fluid_tank",
            () -> BlockEntityType.Builder.of(
                            MachineFluidTankBlockEntity::new,
                            ModBlocks.MACHINE_FLUID_TANK.get())
                    .build(null));

    public static final Supplier<BlockEntityType<BatterySocketBlockEntity>> BATTERY_SOCKET = REGISTER.register(
            "battery_socket",
            () -> BlockEntityType.Builder.of(
                            BatterySocketBlockEntity::new,
                            ModBlocks.MACHINE_BATTERY_SOCKET.get())
                    .build(null));

    public static final Supplier<BlockEntityType<BatteryREDDBlockEntity>> BATTERY_REDD = REGISTER.register(
            "battery_redd",
            () -> BlockEntityType.Builder.of(
                            BatteryREDDBlockEntity::new,
                            ModBlocks.MACHINE_BATTERY_REDD.get())
                    .build(null));

    public static final Supplier<BlockEntityType<ProxyComboBlockEntity>> PROXY_COMBO = REGISTER.register(
            "proxy_combo",
            () -> BlockEntityType.Builder.of(
                            ProxyComboBlockEntity::new,
                            ModBlocks.MACHINE_PRESS.get(),

                            ModBlocks.MACHINE_FLUID_TANK.get(),

                            ModBlocks.MACHINE_BATTERY_SOCKET.get(),
                            ModBlocks.MACHINE_BATTERY_REDD.get()
                    ).build(null));

    public static final Supplier<BlockEntityType<CableBlockEntityBaseNT>> NETWORK_CABLE = REGISTER.register(
            "network_cable",
            () -> BlockEntityType.Builder.of(
                            CableBlockEntityBaseNT::new,
                            ModBlocks.CABLE.get())
                    .build(null));

    public static final Supplier<BlockEntityType<DecontaminatorBlockEntity>> DECONTAMINATOR = REGISTER.register(
            "decontaminator",
            () -> BlockEntityType.Builder.of(
                            DecontaminatorBlockEntity::new,
                            ModBlocks.DECONTAMINATOR.get())
                    .build(null));

    public static final Supplier<BlockEntityType<DetCordBlockEntity>> DET_CORD = REGISTER.register(
            "det_cord",
            () -> BlockEntityType.Builder.of(
                            DetCordBlockEntity::new,
                            ModBlocks.DET_CORD.get())
                    .build(null));

    public static final Supplier<BlockEntityType<NukeBoyBlockEntity>> NUKE_LITTLE_BOY = REGISTER.register(
            "nuke_little_boy",
            () -> BlockEntityType.Builder.of(
                    NukeBoyBlockEntity::new,
                    ModBlocks.NUKE_LITTLE_BOY.get()
            ).build(null));

    public static final Supplier<BlockEntityType<NukeFatManBlockEntity>> NUKE_FAT_MAN = REGISTER.register(
            "nuke_fat_man",
            () -> BlockEntityType.Builder.of(
                            NukeFatManBlockEntity::new,
                            ModBlocks.NUKE_FAT_MAN.get()
                    ).build(null));

    public static final Supplier<BlockEntityType<GeigerBlockEntity>> GEIGER_COUNTER = REGISTER.register(
            "geiger_counter",
            () -> BlockEntityType.Builder.of(
                    (pos, state) -> new GeigerBlockEntity(ModBlockEntities.GEIGER_COUNTER.get(), pos, state),
                            ModBlocks.GEIGER.get())
                    .build(null));

    public static final Supplier<BlockEntityType<LandMineBlockEntity>> LANDMINE = REGISTER.register(
            "landmine",
            () -> BlockEntityType.Builder.of(
                    LandMineBlockEntity::new,
                            ModBlocks.MINE_AP.get(),
                            ModBlocks.MINE_HE.get(),
                            ModBlocks.MINE_SHRAP.get(),
                            ModBlocks.MINE_FAT.get(),
                            ModBlocks.MINE_NAVAL.get())
                    .build(null));

    public static final Supplier<BlockEntityType<CrashedBombBlockEntity>> CRASHED_BOMB_BALEFIRE = REGISTER.register(
            "crashed_bomb_balefire",
            () -> BlockEntityType.Builder.of(
                            CrashedBombBlockEntity::balefire,
                            ModBlocks.CRASHED_BOMB_BALEFIRE.get())
                    .build(null));

    public static final Supplier<BlockEntityType<CrashedBombBlockEntity>> CRASHED_BOMB_CONVENTIONAL = REGISTER.register(
            "crashed_bomb_conventional",
            () -> BlockEntityType.Builder.of(
                            CrashedBombBlockEntity::conventional,
                            ModBlocks.CRASHED_BOMB_CONVENTIONAL.get())
                    .build(null));

    public static final Supplier<BlockEntityType<CrashedBombBlockEntity>> CRASHED_BOMB_NUKE = REGISTER.register(
            "crashed_bomb_nuke",
            () -> BlockEntityType.Builder.of(
                            CrashedBombBlockEntity::nuke,
                            ModBlocks.CRASHED_BOMB_NUKE.get())
                    .build(null));

    public static final Supplier<BlockEntityType<CrashedBombBlockEntity>> CRASHED_BOMB_SALTED = REGISTER.register(
            "crashed_bomb_salted",
            () -> BlockEntityType.Builder.of(
                            CrashedBombBlockEntity::salted,
                            ModBlocks.CRASHED_BOMB_SALTED.get())
                    .build(null));

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}