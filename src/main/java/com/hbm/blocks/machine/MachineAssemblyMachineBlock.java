package com.hbm.blocks.machine;

import com.hbm.blocks.DummyableBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MachineAssemblyMachineBlock extends DummyableBlock {

    public MachineAssemblyMachineBlock(Properties properties) {
        super(properties);
    }

    @Override public int[] getDimensions() { return new int[] {2, 0, 1, 1, 1, 1}; }
    @Override public int getOffset() { return 1; }

    public static final MapCodec<MachineAssemblyMachine> CODEC = simpleCodec(MachineAssemblyMachine::new);
    @Override public MapCodec<MachineAssemblyMachine> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        //            DummyBlockType type = pressed.getValue(TYPE);
        //            case CORE -> new assBLOCKENT(pos, pressed);
        //            case EXTRA -> new ProxyComboBlockEntity(pos, pressed)
        //            case DUMMY -> null;
        return null;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        x -= dir.getStepX();
        z -= dir.getStepZ();

        for(int i = -1; i <= 1; i++) for(int j = -1; j <= 1; j++) {
            if(i != 0 || j != 0) this.makeExtra(level, new BlockPos(x + i, y, z + j));
        }
    }
}