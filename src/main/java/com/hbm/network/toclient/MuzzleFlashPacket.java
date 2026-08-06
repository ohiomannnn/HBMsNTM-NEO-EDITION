package com.hbm.network.toclient;

import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.main.NuclearTechMod;
import com.hbm.render.item.weapon.sedna.ItemRenderWeaponBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MuzzleFlashPacket(int livingId) implements CustomPacketPayload {

    public static final Type<MuzzleFlashPacket> TYPE = new Type<>(NuclearTechMod.withDefaultNamespace("muzzle_flash"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MuzzleFlashPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public MuzzleFlashPacket decode(RegistryFriendlyByteBuf buf) {
            return new MuzzleFlashPacket(buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, MuzzleFlashPacket packet) {
            buf.writeInt(packet.livingId);
        }
    };

    public static void handleCommon(MuzzleFlashPacket packet, IPayloadContext context) { handleClient(packet, context); }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(MuzzleFlashPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            Player player = Minecraft.getInstance().player;
            if(level == null || player == null) return;
            Entity entity = level.getEntity(packet.livingId);
            if(!(entity instanceof LivingEntity living) || entity == player) return; //packets are sent to the player who fired
            ItemStack stack = living.getMainHandItem();
            if(stack.isEmpty()) return;

            if(stack.getItem() instanceof GunBaseNTItem) {
                ItemRenderWeaponBase.flashMap.put(living, System.currentTimeMillis());
            }

        });
    }

    @Override public Type<MuzzleFlashPacket> type() { return TYPE; }
}