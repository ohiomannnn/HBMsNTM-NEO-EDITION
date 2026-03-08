package com.hbm.blockentity.network;

import api.hbm.fluidmk2.FluidNode;
import api.hbm.fluidmk2.IFluidPipeMK2;
import com.hbm.blockentity.IFluidCopiable;
import com.hbm.blockentity.LoadedBaseBlockEntity;
import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.blockentity.Tickable;
import com.hbm.blocks.network.IBlockFluidDuct;
import com.hbm.extprop.HbmPlayerAttachments;
import com.hbm.handler.HbmKeybinds.EnumKeybind;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.lib.ModAttachments;
import com.hbm.uninos.UniNodespace;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PipeBaseBlockEntity extends LoadedBaseBlockEntity implements IFluidPipeMK2, IFluidCopiable, Tickable {

    protected FluidNode node;
    protected FluidType type = Fluids.NONE;

    public PipeBaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.FLUID_DUCT.get(), pos, state);
    }

    @Override
    public void updateEntity() {
        if (level == null) return;

        if (!level.isClientSide) {
            if (this.node == null || this.node.expired) {

                if (this.shouldCreateNode()) {
                    this.node = (FluidNode) UniNodespace.getNode(this.level, this.worldPosition, type.getNetworkProvider());

                    if (this.node == null || this.node.expired) {
                        this.node = this.createNode(type);
                        UniNodespace.createNode(this.level, this.node);
                    }
                }
            }
        }

        this.networkPackNT(150);
    }

    public boolean shouldCreateNode() {
        return true;
    }

    public FluidType getFluidType() {
        return this.type;
    }

    public void setFluidType(FluidType type) {
        if (level == null) return;

        FluidType prev = this.type;
        this.type = type;
        this.setChanged();

        UniNodespace.destroyNode(this.level, this.worldPosition, prev.getNetworkProvider());

        if (this.node != null) {
            this.node = null;
        }
    }

    @Override
    public boolean canConnect(FluidType type, Direction dir) {
        return dir != null && type == this.type;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if (this.level != null && !this.level.isClientSide) {
            if (this.node != null) {
                UniNodespace.destroyNode(level, worldPosition, type.getNetworkProvider());
            }
        }
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(type.getID());
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        this.type = Fluids.fromID(buf.readInt());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.type = Fluids.fromID(tag.getInt("Type"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Type", this.type.getID());
    }

    @Override
    public int[] getFluidIDToCopy() {
        return new int[]{ type.getID() };
    }

    @Override
    public FluidTank getTankToPaste() {
        return null;
    }

    @Override
    public void pasteSettings(CompoundTag tag, int index, Level level, Player player, BlockPos pos) {
        int[] ids = tag.getIntArray("FluidID");

        if (ids.length > 0) {
            int id;
            if (index < ids.length) {
                id = ids[index];
            } else {
                id = 0;
            }

            FluidType fluid = Fluids.fromID(id);

            HbmPlayerAttachments data = player.getData(ModAttachments.PLAYER_ATTACHMENT.get());

            if (data.getKeyPressed(EnumKeybind.TOOL_CTRL)) {
                if (level.getBlockState(this.worldPosition).getBlock() instanceof IBlockFluidDuct ibfd) {
                    ibfd.changeTypeRecursively(level, pos, fluid, 64);
                }
            } else {
                this.setFluidType(fluid);
            }
        }
    }
}
