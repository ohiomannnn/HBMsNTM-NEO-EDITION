package com.hbm.blockentity;

import com.hbm.util.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ProxyBaseBlockEntity extends LoadedBaseBlockEntity {

    public ProxyBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public BlockPos cachedPosition;

    public boolean canUpdate() {
        return false;
    }

    @Nullable
    public BlockEntity getBE() {

        if (level == null) return null;

        if(cachedPosition != null) {
            BlockEntity be = Compat.getBlockEntityStandard(level, cachedPosition);
            if (be != null && !(be instanceof ProxyBaseBlockEntity)) return be;
            cachedPosition = null;
            this.setChanged();
        }

//        if (this.getBlockType() instanceof BlockDummyable) {
//
//            BlockDummyable dummy = (BlockDummyable) this.getBlockType();
//
//            int[] pos = dummy.findCore(worldObj, xCoord, yCoord, zCoord);
//
//            if (pos != null) {
//
//                TileEntity te = Compat.getBlockEntityStandard(worldObj, pos[0], pos[1], pos[2]);
//                if (te != null && !(te instanceof TileEntityProxyBase)) return te;
//            }
//        }
//
//        if (this.getBlockType() instanceof IProxyController) {
//            IProxyController controller = (IProxyController) this.getBlockType();
//            TileEntity tile = controller.getCore(worldObj, xCoord, yCoord, zCoord);
//
//            if (tile != null && !(tile instanceof TileEntityProxyBase)) return tile;
//        }

        return null;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);


        if (tag.getBoolean("hasPos")) cachedPosition = new BlockPos(tag.getInt("pX"), tag.getInt("pY"), tag.getInt("pZ"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (this.cachedPosition != null) {
            tag.putBoolean("hasPos", true);
            tag.putInt("pX", this.cachedPosition.getX());
            tag.putInt("pY", this.cachedPosition.getY());
            tag.putInt("pZ", this.cachedPosition.getZ());
        }
    }
}
