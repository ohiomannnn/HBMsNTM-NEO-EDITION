package com.hbm.network.toclient;

import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.main.NuclearTechMod;
import com.hbm.render.anim.AnimationEnums.GunAnimation;
import com.hbm.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record HbmAnimation(short animType, int rec, int gun) implements CustomPacketPayload {

    public static final Type<HbmAnimation> TYPE = new Type<>(NuclearTechMod.withDefaultNamespace("hbm_animation"));

    public static final StreamCodec<RegistryFriendlyByteBuf, HbmAnimation> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public HbmAnimation decode(RegistryFriendlyByteBuf buf) {
            return new HbmAnimation(
                    buf.readShort(),
                    buf.readInt(),
                    buf.readInt()
            );
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, HbmAnimation packet) {
            buf.writeShort(packet.animType);
            buf.writeInt(packet.rec);
            buf.writeInt(packet.gun);
        }
    };

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(HbmAnimation packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if(player == null) return;

            int slot = player.inventory.selected;

            for(ItemStack stack : InventoryUtil.getItemsFromBothHands(player)) {
                if(stack.getItem() instanceof GunBaseNTItem) handleSedna(player, stack, slot, GunAnimation.values()[packet.animType], packet.rec, packet.gun);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleSedna(Player player, ItemStack stack, int slot, GunAnimation type, int receiverIndex, int gunIndex) {

    }

    @Override public Type<HbmAnimation> type() { return TYPE; }
}
