package com.hbm.blockentity;

import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.IProxyController;
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

        if (cachedPosition != null) {
            BlockEntity be = Compat.getBlockEntityStandard(level, cachedPosition);
            if (be != null && !(be instanceof ProxyBaseBlockEntity)) return be;
            cachedPosition = null;
            this.setChanged();
        }

        if (this.getBlockState().getBlock() instanceof DummyableBlock dummyable) {

            BlockPos pos = dummyable.findCore(level, this.getBlockPos());

            if (pos != null) {

                BlockEntity be = Compat.getBlockEntityStandard(level, pos);
                if (be != null && !(be instanceof ProxyBaseBlockEntity)) return be;
            }
        }

        if (this.getBlockState().getBlock() instanceof IProxyController controller) {
            BlockEntity be = controller.getCore(level, this.getBlockPos());

            if (be != null && !(be instanceof ProxyBaseBlockEntity)) return be;
        }

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
