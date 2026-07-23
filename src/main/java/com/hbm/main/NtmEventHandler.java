package com.hbm.main;

import com.hbm.blockentity.machine.MachineRadarBlockEntity;
import com.hbm.config.NtmConfig;
import com.hbm.entity.missile.MissileAntiBallistic;
import com.hbm.extprop.HbmPlayerAttachments;
import com.hbm.handler.HTTPHandler;
import com.hbm.network.toclient.InformPlayer;
import com.hbm.uninos.UniNodespace;
import com.hbm.world.MeteorStrikeSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = NuclearTechMod.MODID)
public class NtmEventHandler {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        Level level = player.level;

        if(!level.isClientSide) {

            if(NtmConfig.COMMON.ENABLE_MOTD.get()) {
                player.sendSystemMessage(Component.translatable("message.hbmsntm.loaded", Component.translatable("message.hbmsntm.neo").withStyle(ChatFormatting.LIGHT_PURPLE), NuclearTechMod.VERSION));

                if(HTTPHandler.newVersion) {
                    player.sendSystemMessage(
                            Component.translatable(
                                    "message.hbmsntm.new_version",
                                    HTTPHandler.versionNumber,
                                    Component.translatable("message.hbmsntm.click_here")
                                            .withStyle(
                                                    Style.EMPTY
                                                            .withColor(ChatFormatting.RED)
                                                            .withUnderlined(true)
                                                            .withClickEvent(
                                                                    new ClickEvent(
                                                                            ClickEvent.Action.OPEN_URL,
                                                                            "https://github.com/ohiomannnn/HBMsNTM-NEO-EDITION/releases"
                                                                    )
                                                            )
                                            )
                            ).withStyle(ChatFormatting.YELLOW)
                    );
                }
            }

            if(NtmConfig.COMMON.ENABLE_DUCKS.get() && !HbmPlayerAttachments.getData(player).ducked) {
                if(player instanceof ServerPlayer serverPlayer) PacketDistributor.sendToPlayer(serverPlayer, new InformPlayer(Component.translatable("info.duck"), NuclearTechModClient.ID_DUCK, 30_000));
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {

        // Networks! All of them!
        UniNodespace.updateNodespace(event.getServer());
        // Radar entry handling
        MachineRadarBlockEntity.updateSystem(event.getServer());
        // Meteor strike handling
        MeteorStrikeSystem.update(event.getServer());
    }

}
