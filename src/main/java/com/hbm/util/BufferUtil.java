package com.hbm.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BufferUtil {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public static void writeNBT(ByteBuf buf, CompoundTag compound) {
        if (compound != null && !compound.isEmpty()) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                NbtIo.writeCompressed(compound, baos);
                byte[] nbtData = baos.toByteArray();
                buf.writeInt(nbtData.length);
                buf.writeBytes(nbtData);
            } catch (IOException e) {
                e.printStackTrace();
                buf.writeInt(-1);
            }
        } else {
            buf.writeInt(-1);
        }
    }

    public static CompoundTag readNBT(ByteBuf buf) {
        int nbtLength = buf.readInt();

        if (nbtLength <= 0) {
            return new CompoundTag();
        }

        byte[] tags = new byte[nbtLength];
        buf.readBytes(tags);

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(tags);
            return NbtIo.readCompressed(bais, NbtAccounter.create(2097152L));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CompoundTag();
    }

    public static void writeItemStack(ByteBuf buf, ItemStack stack, RegistryAccess registryAccess) {
        if (stack == null || stack.isEmpty()) {
            buf.writeBoolean(false);
            return;
        }

        buf.writeBoolean(true);

        Tag tag = stack.save(registryAccess);
        CompoundTag compound = tag instanceof CompoundTag ? (CompoundTag) tag : new CompoundTag();

        writeNBT(buf, compound);
    }

    public static ItemStack readItemStack(ByteBuf buf, RegistryAccess registryAccess) {
        boolean exists = buf.readBoolean();
        if (!exists) {
            return ItemStack.EMPTY;
        }

        CompoundTag compound = readNBT(buf);
        return ItemStack.parse(registryAccess, compound).orElse(ItemStack.EMPTY);
    }
}
