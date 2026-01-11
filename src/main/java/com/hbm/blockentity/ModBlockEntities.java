package com.hbm.blockentity;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.bomb.CrashedBombBlockEntity;
import com.hbm.blockentity.bomb.DetCordBlockEntity;
import com.hbm.blockentity.bomb.LandMineBlockEntity;
import com.hbm.blockentity.bomb.NukeFatManBlockEntity;
import com.hbm.blockentity.machine.MachineSatLinkerBlockEntity;
import com.hbm.blockentity.machine.DecontaminatorBlockEntity;
import com.hbm.blockentity.machine.GeigerBlockEntity;
import com.hbm.blockentity.machine.storage.BatterySocketBlockEntity;
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

    public static final Supplier<BlockEntityType<BatterySocketBlockEntity>> BATTERY_SOCKET = REGISTER.register(
            "batter_socket",
            () -> BlockEntityType.Builder.of(
                            (pos, state) -> new BatterySocketBlockEntity(ModBlockEntities.BATTERY_SOCKET.get(), pos, state),
                            ModBlocks.MACHINE_BATTER_SOCKET.get())
                    .build(null));

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


    public static final Supplier<BlockEntityType<NukeFatManBlockEntity>> NUKE_FATMAN = REGISTER.register(
            "nuke_fatman",
            () -> BlockEntityType.Builder.of(
                            NukeFatManBlockEntity::new,
                            ModBlocks.NUKE_FATMAN.get())
                    .build(null));

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