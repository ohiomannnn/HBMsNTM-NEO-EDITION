package com.hbm.blockentity.machine.storage;

import com.hbm.blockentity.NtmBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class CrateTemplateBlockEntity extends CrateBaseBlockEntity {

    public CrateTemplateBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.CRATE_TEMPLATE.get(), pos, state, 27, "gui_crate_template", 9, 3, 8, 18, 8, 86, 176, 168, 8, 4210752, 4210752);
    }

    @Override public Component getName() { return Component.translatable("container.crateTemplate"); }
}
