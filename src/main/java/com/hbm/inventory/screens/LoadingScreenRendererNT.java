package com.hbm.inventory.screens;

import com.hbm.handler.HTTPHandler;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;

public class LoadingScreenRendererNT {

    private static final RandomSource random = RandomSource.create();

    public LoadingScreenRendererNT() { }

    private static String chooseTip() {
        if (HTTPHandler.tipOfTheDay.isEmpty()) return "Explore! There's tons of free stuff to find.";
        return HTTPHandler.tipOfTheDay.get(random.nextInt(HTTPHandler.tipOfTheDay.size()));
    }

    private static String tipOfTheDay = "Tip of the day: " + chooseTip();
    public static void resetMessage() { tipOfTheDay = "Tip of the day: " + chooseTip(); }

    public static void render(GuiGraphics guiGraphics) {
        Font font = Minecraft.getInstance().font;
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();

        String[] frags = I18nUtil.resolveKeyArray(tipOfTheDay);
        int y = (height / 2) / 3;
        for (int i = 0; i < frags.length; i++) {
            String frag = frags[i];
            guiGraphics.drawCenteredString(font, Component.literal(frag).withStyle(ChatFormatting.YELLOW), width / 2, y + i * font.lineHeight, 0xFFFFFF);
        }
    }
}