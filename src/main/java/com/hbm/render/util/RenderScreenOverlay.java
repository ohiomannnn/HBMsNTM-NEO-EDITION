package com.hbm.render.util;

import com.hbm.HBMsNTM;
import com.hbm.util.Clock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;

public class RenderScreenOverlay {
    public static final ResourceLocation MISC_TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/misc/overlay_misc.png");

    private static long lastSurvey;
    private static float prevResult;
    private static float lastResult;

    public static void renderRadCounter(GuiGraphics guiGraphics, float in) {
        Minecraft mc = Minecraft.getInstance();

        if (!mc.options.getCameraType().isFirstPerson()) return;
        if (mc.options.hideGui) return;
        if (mc.gameMode.getPlayerMode() == GameType.SPECTATOR) return;

        float radiation;

        radiation = lastResult - prevResult;

        if (Clock.get_ms() >= lastSurvey + 1000) {
            lastSurvey = Clock.get_ms();
            prevResult = lastResult;
            lastResult = in;
        }

        int length = 74;
        int maxRad = 1000;

        int bar = getScaled(in, maxRad, 74);

        int posX = 16;
        int posY = guiGraphics.guiHeight() - 20;

        guiGraphics.blit(MISC_TEXTURE, posX, posY, 0, 0, 94, 18);
        guiGraphics.blit(MISC_TEXTURE, posX + 1, posY + 1, 1, 19, bar, 16);

        if (radiation >= 25) {
            guiGraphics.blit(MISC_TEXTURE, posX + length + 2, posY - 18, 36, 36, 18, 18);
        } else if (radiation >= 10) {
            guiGraphics.blit(MISC_TEXTURE, posX + length + 2, posY - 18, 18, 36, 18, 18);
        } else if (radiation >= 2.5) {
            guiGraphics.blit(MISC_TEXTURE, posX + length + 2, posY - 18, 0, 36, 18, 18);
        }

        Font font = Minecraft.getInstance().font;

        if (radiation > 1000) {
            guiGraphics.drawString(font, ">1000 RAD/s", posX, posY - 8, 0xFF0000);
        } else if (radiation >= 1) {
            guiGraphics.drawString(font, Math.round(radiation) + " RAD/s", posX, posY - 8, 0xFF0000);
        } else if (radiation > 0) {
            guiGraphics.drawString(font, "<1 RAD/s", posX, posY - 8, 0xFF0000);
        }
    }

    private static int getScaled(double cur, double max, double scale) {
        return (int) Math.min(cur / max * scale, scale);
    }
}
