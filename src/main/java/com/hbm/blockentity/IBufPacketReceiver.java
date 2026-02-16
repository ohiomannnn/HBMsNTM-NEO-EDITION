package com.hbm.blockentity;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;

public interface IBufPacketReceiver {
    void serialize(ByteBuf buf);
    void deserialize(ByteBuf buf);
}
