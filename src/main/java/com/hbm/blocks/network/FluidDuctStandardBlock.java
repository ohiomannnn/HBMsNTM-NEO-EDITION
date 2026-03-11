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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class FluidDuctStandardBlock extends FluidDuctConnectingBlock implements ILookOverlay {

    public FluidDuctStandardBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (level.getBlockEntity(pos) instanceof PipeBaseBlockEntity pipe) {
            FluidType type = pipe.getFluidType();
            state = state.setValue(WEST,  canConnectTo(level, pos, Direction.WEST, type))
                    .setValue(EAST,     canConnectTo(level, pos, Direction.EAST, type))
                    .setValue(DOWN,     canConnectTo(level, pos, Direction.DOWN, type))
                    .setValue(UP,       canConnectTo(level, pos, Direction.UP, type))
                    .setValue(NORTH,    canConnectTo(level, pos, Direction.NORTH, type))
                    .setValue(SOUTH,    canConnectTo(level, pos, Direction.SOUTH, type));
        }
        return state;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {

        boolean nX = state.getValue(WEST);
        boolean pX = state.getValue(EAST);
        boolean nY = state.getValue(DOWN);
        boolean pY = state.getValue(UP);
        boolean nZ = state.getValue(NORTH);
        boolean pZ = state.getValue(SOUTH);

        int mask = 0 + (pX ? 32 : 0) + (nX ? 16 : 0) + (pY ? 8 : 0) + (nY ? 4 : 0) + (pZ ? 2 : 0) + (nZ ? 1 : 0);

        if (mask == 0) {
            return Shapes.or(
                    Shapes.box(0.6875D, 0.3125D, 0.3125D, 1.0D, 0.6875D, 0.6875D),
                    Shapes.box(0.0D, 0.3125D, 0.3125D, 0.3125D, 0.6875D, 0.6875D),
                    Shapes.box(0.3125D, 0.6875D, 0.3125D, 0.6875D, 1.0D, 0.6875D),
                    Shapes.box(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.3125D, 0.6875D),
                    Shapes.box(0.3125D, 0.3125D, 0.6875D, 0.6875D, 0.6875D, 1.0D),
                    Shapes.box(0.3125D, 0.3125D, 0.0D, 0.6875D, 0.6875D, 0.3125D)
            );
        } else if (mask == 0b100000 || mask == 0b010000 || mask == 0b110000) {
            return Shapes.box(0.0D, 0.3125D, 0.3125D, 1.0D, 0.6875D, 0.6875D);
        } else if (mask == 0b001000 || mask == 0b000100 || mask == 0b001100) {
            return Shapes.box(0.3125D, 0.0D, 0.3125D, 0.6875D, 1.0D, 0.6875D);
        } else if (mask == 0b000010 || mask == 0b000001 || mask == 0b000011) {
            return Shapes.box(0.3125D, 0.3125D, 0.0D, 0.6875D, 0.6875D, 1.0D);
        } else {

            VoxelShape shape = Shapes.box(0.3125D, 0.3125D, 0.3125D, 0.6875D, 0.6875D, 0.6875D);

            if (pX) shape = Shapes.or(shape, Shapes.box(0.6875D, 0.3125D, 0.3125D, 1.0D, 0.6875D, 0.6875D));
            if (nX) shape = Shapes.or(shape, Shapes.box(0.0D, 0.3125D, 0.3125D, 0.3125D, 0.6875D, 0.6875D));
            if (pY) shape = Shapes.or(shape, Shapes.box(0.3125D, 0.6875D, 0.3125D, 0.6875D, 1.0D, 0.6875D));
            if (nY) shape = Shapes.or(shape, Shapes.box(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.3125D, 0.6875D));
            if (pZ) shape = Shapes.or(shape, Shapes.box(0.3125D, 0.3125D, 0.6875D, 0.6875D, 0.6875D, 1.0D));
            if (nZ) shape = Shapes.or(shape, Shapes.box(0.3125D, 0.3125D, 0.0D, 0.6875D, 0.6875D, 0.3125D));

            return shape;
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {

        boolean nX = state.getValue(WEST);
        boolean pX = state.getValue(EAST);
        boolean nY = state.getValue(DOWN);
        boolean pY = state.getValue(UP);
        boolean nZ = state.getValue(NORTH);
        boolean pZ = state.getValue(SOUTH);

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
