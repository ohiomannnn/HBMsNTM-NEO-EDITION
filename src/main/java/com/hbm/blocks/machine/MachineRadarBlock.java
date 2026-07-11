package com.hbm.blocks.machine;

import com.hbm.blockentity.IScreenProvider;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.machine.MachineRadarBlockEntity;
import com.hbm.inventory.screens.MachineRadarScreen;
import com.hbm.main.NuclearTechMod;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class MachineRadarBlock extends BaseEntityBlock implements IScreenProvider {

    public MachineRadarBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<MachineRadarBlock> CODEC = simpleCodec(MachineRadarBlock::new);
    @Override public MapCodec<MachineRadarBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MachineRadarBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

        if(!player.isShiftKeyDown()) {
            NuclearTechMod.proxy.openScreen(player, pos);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Object provideScreen(Player player, BlockPos pos) {
        return new MachineRadarScreen((MachineRadarBlockEntity) player.level.getBlockEntity(pos));
    }

    @Override protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) { return 1F; }

    @Override protected RenderShape getRenderShape(BlockState state) { return RenderShape.ENTITYBLOCK_ANIMATED; }
}
