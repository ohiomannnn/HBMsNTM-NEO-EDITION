package com.hbm.blockentity.machine;

import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.ModBlockEntityTypes;
import com.hbm.util.BufferUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MachinePressBlockEntity extends MachineBaseBlockEntity {

    public int speed = 0; // speed ticks up once (or four times if preheated) when operating
    public static final int maxSpeed = 400; // max speed ticks for acceleration
    public static final int progressAtMax = 25; // max progress speed when hot
    public int burnTime = 0; // burn ticks of the loaded fuel, 200 ticks equal one operation

    public int press; // extension of the press, operation is completed if maxPress is reached
    public double renderPress; // client-side version of the press var, a double for smoother rendering
    public double lastPress; // for interp
    private int syncPress; // for interp
    private int turnProgress; // for interp 3: revenge of the sith
    public final static int maxPress = 200; // max tick count per operation assuming speed is 1
    boolean isRetracting = false; // direction the press is currently going
    private int delay; // delay between direction changes to look a bit more appealing

    public ItemStack syncStack;

    public MachinePressBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.PRESS.get(), pos, state, 4);
    }

    @Override
    public Component getDefaultName() { return Component.translatable("container.press"); }

    @Override
    public void updateEntity() {
        //sure
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.speed);
        buf.writeInt(this.burnTime);
        buf.writeInt(this.press);
        //BufferUtil.writeItemStack(buf, this.slots.get(2), registryAccess);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        this.speed = buf.readInt();
        this.burnTime = buf.readInt();
        this.syncPress = buf.readInt();
        //this.syncStack = BufferUtil.readItemStack(buf, registryAccess);

        this.turnProgress = 2;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }
}
