package com.hbm.blockentity;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.machine.storage.CrateIronBlockEntity;
import com.hbm.blocks.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, HBMsNTM.MODID);

    public static final Supplier<BlockEntityType<CrateIronBlockEntity>> IRON_CRATE =
            BLOCK_ENTITIES.register("iron_crate",
                    () -> BlockEntityType.Builder.of(CrateIronBlockEntity::new,
                            ModBlocks.IRON_CRATE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
