package com.hbm.blockentity;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;

/**
 * RegistryAccess for saving items
 */
public interface IBufPacketReceiver {
    void serialize(ByteBuf buf, RegistryAccess registryAccess);
    void deserialize(ByteBuf buf, RegistryAccess registryAccess);
}
