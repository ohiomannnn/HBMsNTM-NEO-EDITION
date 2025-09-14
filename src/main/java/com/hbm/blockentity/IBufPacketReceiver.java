package com.hbm.blockentity;

import io.netty.buffer.ByteBuf;

public interface IBufPacketReceiver {
    void serialize(ByteBuf buf);
    void deserialize(ByteBuf buf);
}
