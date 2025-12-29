package com.hbm.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public interface INBTBlockTransformable {

    /**
     * Defines this block as something that has a rotation or some other blockstate
     * which needs transformations applied when building from an .nbt structure file
     */

    // Takes the block current state and translates it into a rotated state
    BlockState transformState(BlockState state, Rotation rotation);

    // Takes the block and turns it into a different block entirely, to turn off lights, shit like that
    default Block transformBlock(Block block) {
        return block;
    }


    /**
     * A fair few blocks have generalized rotations so, since we have all this space, put em here
     */

    static Rotation fromCoordBaseMode(int coordBaseMode) {
        return switch (coordBaseMode) {
            case 1 -> Rotation.CLOCKWISE_90;
            case 2 -> Rotation.CLOCKWISE_180;
            case 3 -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };
    }

    static int toCoordBaseMode(Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_90 -> 1;
            case CLOCKWISE_180 -> 2;
            case COUNTERCLOCKWISE_90 -> 3;
            default -> 0;
        };
    }

    static BlockState rotateState(BlockState state, Rotation rotation) {
        if (rotation == Rotation.NONE) {
            return state;
        }
        return state.rotate(rotation);
    }

    static Direction rotateDirection(Direction direction, Rotation rotation) {
        if (direction.getAxis() == Direction.Axis.Y) {
            return direction; // Don't rotate UP/DOWN
        }
        return rotation.rotate(direction);
    }

    static BlockState transformFacingState(BlockState state, Rotation rotation) {
        if (rotation == Rotation.NONE) {
            return state;
        }

        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            Direction rotated = rotation.rotate(facing);
            return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotated);
        }

        if (state.hasProperty(BlockStateProperties.FACING)) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            if (facing.getAxis() != Direction.Axis.Y) {
                Direction rotated = rotation.rotate(facing);
                return state.setValue(BlockStateProperties.FACING, rotated);
            }
        }

        return state;
    }

    static BlockState transformAxisState(BlockState state, Rotation rotation) {
        if (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180) {
            return state;
        }

        if (state.hasProperty(BlockStateProperties.AXIS)) {
            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
            if (axis == Direction.Axis.X) {
                return state.setValue(BlockStateProperties.AXIS, Direction.Axis.Z);
            } else if (axis == Direction.Axis.Z) {
                return state.setValue(BlockStateProperties.AXIS, Direction.Axis.X);
            }
        }

        return state;
    }

    static BlockState transformStairsState(BlockState state, Rotation rotation) {
        return state.rotate(rotation);
    }

    static BlockState transformTrapdoorState(BlockState state, Rotation rotation) {
        return state.rotate(rotation);
    }

    static BlockState transformDoorState(BlockState state, Rotation rotation) {
        return state.rotate(rotation);
    }

    static BlockState transformTorchState(BlockState state, Rotation rotation) {
        if (rotation == Rotation.NONE) {
            return state;
        }

        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            Direction rotated = rotation.rotate(facing);
            return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotated);
        }

        return state;
    }

    static BlockState transformLeverState(BlockState state, Rotation rotation) {
        return state.rotate(rotation);
    }

    static BlockState transformVineState(BlockState state, Rotation rotation) {
        if (rotation == Rotation.NONE) {
            return state;
        }

        boolean north = state.getValue(BlockStateProperties.NORTH);
        boolean south = state.getValue(BlockStateProperties.SOUTH);
        boolean east = state.getValue(BlockStateProperties.EAST);
        boolean west = state.getValue(BlockStateProperties.WEST);

        return switch (rotation) {
            case CLOCKWISE_90 -> state
                    .setValue(BlockStateProperties.NORTH, west)
                    .setValue(BlockStateProperties.EAST, north)
                    .setValue(BlockStateProperties.SOUTH, east)
                    .setValue(BlockStateProperties.WEST, south);
            case CLOCKWISE_180 -> state
                    .setValue(BlockStateProperties.NORTH, south)
                    .setValue(BlockStateProperties.SOUTH, north)
                    .setValue(BlockStateProperties.EAST, west)
                    .setValue(BlockStateProperties.WEST, east);
            case COUNTERCLOCKWISE_90 -> state
                    .setValue(BlockStateProperties.NORTH, east)
                    .setValue(BlockStateProperties.EAST, south)
                    .setValue(BlockStateProperties.SOUTH, west)
                    .setValue(BlockStateProperties.WEST, north);
            default -> state;
        };
    }

    static BlockState transformDecoModelState(BlockState state, Rotation rotation, EnumProperty<Direction> facingProperty) {
        if (rotation == Rotation.NONE) {
            return state;
        }

        if (state.hasProperty(facingProperty)) {
            Direction facing = state.getValue(facingProperty);
            Direction rotated = rotation.rotate(facing);
            return state.setValue(facingProperty, rotated);
        }

        return state;
    }
}