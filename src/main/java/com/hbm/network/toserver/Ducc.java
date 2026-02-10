package com.hbm.network.toserver;

import com.hbm.HBMsNTM;
import com.hbm.entity.ModEntityTypes;
import com.hbm.entity.mob.Duck;
import com.hbm.extprop.HbmPlayerAttachments;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.lib.ModAttachments;
import com.hbm.lib.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record Ducc() implements CustomPacketPayload {
    public static final Type<Ducc> TYPE = new Type<>(HBMsNTM.withDefaultNamespaceNT("ducc"));

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
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.DUCK, SoundSource.NEUTRAL, 1.0F, 1.0F);

                props.ducked = true;
                player.setData(ModAttachments.PLAYER_ATTACHMENT, props);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
