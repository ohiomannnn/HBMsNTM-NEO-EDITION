package com.hbm.blockentity;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;

public interface IBufPacketReceiver {

    void serialize(RegistryFriendlyByteBuf buf);
    void deserialize(RegistryFriendlyByteBuf buf);
}
