package com.hbm.network.toserver;

import com.hbm.inventory.menus.AnvilMenu;
import com.hbm.inventory.recipes.anvil.AnvilRecipes;
import com.hbm.main.NuclearTechMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AnvilConstructionPacket(int recipeIndex, int mode) implements CustomPacketPayload {

    public static final Type<AnvilConstructionPacket> TYPE = new Type<>(NuclearTechMod.withDefaultNamespace("anvil_construction"));

    public static final StreamCodec<FriendlyByteBuf, AnvilConstructionPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public AnvilConstructionPacket decode(FriendlyByteBuf buf) {
            return new AnvilConstructionPacket(buf.readInt(), buf.readInt());
        }

        @Override
        public void encode(FriendlyByteBuf buf, AnvilConstructionPacket packet) {
            buf.writeInt(packet.recipeIndex);
            buf.writeInt(packet.mode);
        }
    };

    public static void handleServer(AnvilConstructionPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if(!(context.player().containerMenu instanceof AnvilMenu menu)) {
                return;
            }

            AnvilRecipes.executeConstruction(context.player(), menu.getTier(), packet.recipeIndex, packet.mode);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
