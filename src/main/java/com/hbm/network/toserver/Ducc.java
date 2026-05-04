package com.hbm.network.toserver;

import com.hbm.main.NuclearTechMod;
import com.hbm.entity.ModEntityTypes;
import com.hbm.entity.mob.Duck;
import com.hbm.extprop.HbmPlayerAttachments;
import com.hbm.lib.ModAttachments;
import com.hbm.registry.NtmSoundEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record Ducc() implements CustomPacketPayload {
    public static final Type<Ducc> TYPE = new Type<>(NuclearTechMod.withDefaultNamespace("ducc"));

    public static final StreamCodec<FriendlyByteBuf, Ducc> STREAM_CODEC = new StreamCodec<>() {
        @Override public Ducc decode(FriendlyByteBuf buf) { return new Ducc(); }
        @Override public void encode(FriendlyByteBuf buf, Ducc packet) { }
    };

    public static void handleServer(Ducc ignored, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            HbmPlayerAttachments props = HbmPlayerAttachments.getData(player);
            if (!props.ducked) {
                Duck ducc = new Duck(ModEntityTypes.DUCK.get(), player.level());

                ducc.setPos(player.getX(), player.getEyeY(), player.getZ());
                ducc.setDeltaMovement(player.getLookAngle());
                ducc.fallDistance = 0.0F;

                player.level().addFreshEntity(ducc);
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), NtmSoundEvents.DUCC, SoundSource.NEUTRAL, 1.0F, 1.0F);

                props.ducked = true;
                player.setData(ModAttachments.PLAYER_ATTACHMENT, props);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
