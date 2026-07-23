package com.hbm.network.toclient;

import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.items.weapon.sedna.GunBaseNTItem.LambdaContext;
import com.hbm.items.weapon.sedna.GunConfig;
import com.hbm.items.weapon.sedna.Receiver;
import com.hbm.main.NuclearTechMod;
import com.hbm.render.anim.AnimationEnums.GunAnimation;
import com.hbm.render.anim.BusAnimation;
import com.hbm.render.anim.HbmAnimations;
import com.hbm.render.anim.HbmAnimations.Animation;
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

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

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

    public static void handleCommon(HbmAnimation packet, IPayloadContext context) { handleClient(packet, context); }

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
        GunBaseNTItem gun = (GunBaseNTItem) stack.getItem();
        GunConfig config = gun.getConfig(stack, gunIndex);

        if(type == GunAnimation.CYCLE) {
            if(gunIndex < gun.lastShot.length) gun.lastShot[gunIndex] = System.currentTimeMillis();
            gun.shotRand = player.level.random.nextDouble();

            Receiver[] receivers = config.getReceivers(stack);
            if(receiverIndex >= 0 && receiverIndex < receivers.length) {
                Receiver rec = receivers[receiverIndex];
                BiConsumer<ItemStack, LambdaContext> onRecoil= rec.getRecoil(stack);
                if(onRecoil != null) onRecoil.accept(stack, new LambdaContext(config, player, player.inventory, receiverIndex));
            }
        }

        BiFunction<ItemStack, GunAnimation, BusAnimation> anims = config.getAnims(stack);
        BusAnimation animation = anims.apply(stack, type);

        if(animation == null && (type == GunAnimation.ALT_CYCLE || type == GunAnimation.CYCLE_EMPTY)) {
            animation = anims.apply(stack, GunAnimation.CYCLE);
        }

        if(animation != null) {
            boolean isReloadAnimation = type == GunAnimation.RELOAD || type == GunAnimation.RELOAD_CYCLE;
            //if(isReloadAnimation && ArmorTrenchmaster.isTrenchMaster(player)) animation.setTimeMult(0.5D);
            HbmAnimations.hotbar[slot][gunIndex] = new Animation(stack.getItem().getDescriptionId(), System.currentTimeMillis(), animation, isReloadAnimation && config.getReloadAnimSequential(stack));
        }
    }

    @Override public Type<HbmAnimation> type() { return TYPE; }
}
