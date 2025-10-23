package com.hbm.handler.gui;

import com.hbm.HBMsNTM;
import com.hbm.items.tools.GeigerCounterItem;
import com.hbm.network.toserver.GetRadPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

public class GeigerGUI {

    private static final int UPDATE_INTERVAL_TICKS = 10;
    private static long prevTick = -1;
    private static float prevRad = 0f;
    private static float smoothedRadiation = 0f;
    public static float rad;

    private static int getScaled(double cur, double max, double scale) {
        return (int) Math.min(cur / max * scale, scale);
    }

    public static final ResourceLocation MISC_TEXTURE = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/misc/overlay_misc.png");

    public static void RegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.parse("geiger_gui"), (guiGraphics, partialTick) -> {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            
            renderRadCounter(guiGraphics, mc.font, player);
        });
    }
    public static void renderRadCounter(GuiGraphics guiGraphics, Font font, Player player) {
        if (!checkForGeiger((LocalPlayer) player)) return;
        if (Minecraft.getInstance().level == null) return;
        if (Minecraft.getInstance().options.hideGui) return;

        guiGraphics.pose().pushPose();
        getRad(player.getUUID());

        long currentTick = Minecraft.getInstance().level.getGameTime();

        if (prevTick < 0) {
            prevTick = currentTick;
            prevRad = rad;
        }

        long ticksPassed = currentTick - prevTick;
        if (ticksPassed >= UPDATE_INTERVAL_TICKS) {
            float secondsPassed = ticksPassed / 20f;
            smoothedRadiation = (rad - prevRad) / secondsPassed;

            prevTick = currentTick;
            prevRad = rad;
        }

        int length = 74;
        int maxRad = 1000;
        int bar = getScaled(rad, maxRad, length);

        int posX = 16;
        int posY = guiGraphics.guiHeight() - 20;

        guiGraphics.blit(MISC_TEXTURE, posX, posY, 0, 0, 94, 18);
        guiGraphics.blit(MISC_TEXTURE, posX + 1, posY + 1, 1, 19, bar, 16);

        if (smoothedRadiation >= 25) {
            guiGraphics.blit(MISC_TEXTURE, posX + length + 2, posY - 18, 36, 36, 18, 18);
        } else if (smoothedRadiation >= 10) {
            guiGraphics.blit(MISC_TEXTURE, posX + length + 2, posY - 18, 18, 36, 18, 18);
        } else if (smoothedRadiation >= 2.5) {
            guiGraphics.blit(MISC_TEXTURE, posX + length + 2, posY - 18, 0, 36, 18, 18);
        }

        String radText = "";
        if (smoothedRadiation > 1000) {
            radText = ">1000 RAD/s";
        } else if (smoothedRadiation >= 0.1f) {
            radText = String.format("%.1f RAD/s", smoothedRadiation);;
        } else if (smoothedRadiation > 0) {
            radText = "<0.1 RAD/s";
        }
        if (!radText.isEmpty()) {
            guiGraphics.drawString(font, radText, posX, posY - 8, 0xFF0000, false);
        }

        guiGraphics.pose().popPose();
    }
    private static boolean checkForGeiger(LocalPlayer player) {
        //offhand is not an inventory
        if (player.getOffhandItem().getItem() instanceof GeigerCounterItem) {
            return true;
        }
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof GeigerCounterItem) {
                return true;
            }
        }
        return false;
    }

    public static void getRadFromServer(float rad) {
        GeigerGUI.rad = rad;
    }
    private static void getRad(UUID target) {
        PacketDistributor.sendToServer(new GetRadPacket(target));
    }
}
