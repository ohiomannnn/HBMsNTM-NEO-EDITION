package com.hbm.blocks.network;

import com.hbm.blockentity.network.PipeBaseBlockEntity;
import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.lib.Library;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class FluidDuctStandardBlock extends FluidDuctBaseBlock implements ILookOverlay {

    public FluidDuctStandardBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    // this is sucks
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {

        if (level.getBlockEntity(pos) instanceof PipeBaseBlockEntity pipe) {
            FluidType type = pipe.getFluidType();

            boolean nX = canConnectTo(level, pos, Library.NEG_X, type);
            boolean pX = canConnectTo(level, pos, Library.POS_X, type);
            boolean nY = canConnectTo(level, pos, Library.NEG_Y, type);
            boolean pY = canConnectTo(level, pos, Library.POS_Y, type);
            boolean nZ = canConnectTo(level, pos, Library.NEG_Z, type);
            boolean pZ = canConnectTo(level, pos, Library.POS_Z, type);

            int mask = 0 + (pX ? 32 : 0) + (nX ? 16 : 0) + (pY ? 8 : 0) + (nY ? 4 : 0) + (pZ ? 2 : 0) + (nZ ? 1 : 0);

            if (mask == 0) {
                return Shapes.box(0F, 0F, 0F, 1F, 1F, 1F);
            } else if (mask == 0b100000 || mask == 0b010000 || mask == 0b110000) {
                return Shapes.box(0F, 0.3125F, 0.3125F, 1F, 0.6875F, 0.6875F);
            } else if (mask == 0b001000 || mask == 0b000100 || mask == 0b001100) {
                return Shapes.box(0.3125F, 0F, 0.3125F, 0.6875F, 1F, 0.6875F);
            } else if (mask == 0b000010 || mask == 0b000001 || mask == 0b000011) {
                return Shapes.box(0.3125F, 0.3125F, 0F, 0.6875F, 0.6875F, 1F);
            } else {

                return Shapes.box(
                        nX ? 0F : 0.3125F,
                        nY ? 0F : 0.3125F,
                        nZ ? 0F : 0.3125F,
                        pX ? 1F : 0.6875F,
                        pY ? 1F : 0.6875F,
                        pZ ? 1F : 0.6875F
                );
            }
        }

        return Shapes.block();
    }

    public boolean canConnectTo(BlockGetter level, BlockPos pos, Direction dir, FluidType type) {
        return Library.canConnectFluid(level, pos.relative(dir), dir, type);
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);

        if (!(be instanceof PipeBaseBlockEntity pipe)) return;

        List<Component> text = new ArrayList<>();
        FluidType type = pipe.getFluidType();
        text.add(Component.translatable(type.getUnlocalizedName()).withColor(type.getColor()));
        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
    }
}
