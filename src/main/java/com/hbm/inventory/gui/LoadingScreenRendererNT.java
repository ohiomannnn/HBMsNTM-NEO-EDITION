package com.hbm.inventory.gui;

import com.hbm.handler.HTTPHandler;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.Random;

public class LoadingScreenRendererNT {

    private final Minecraft mc;
    private final Random random = new Random();

    public LoadingScreenRendererNT(Minecraft mc) { this.mc = mc; }

    private String chooseTip() {
        if (HTTPHandler.tipOfTheDay.isEmpty()) return "Explore! There's tons of free stuff to find.";
        return HTTPHandler.tipOfTheDay.get(random.nextInt(HTTPHandler.tipOfTheDay.size()));
    }

    private String tipOfTheDay = "Tip of the day: " + chooseTip();
    public void resetMessage() { this.tipOfTheDay = "Tip of the day: " + chooseTip(); }

    public void render(GuiGraphics guiGraphics) {
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        String[] frags = I18nUtil.resolveKeyArray(tipOfTheDay);
        int y = (height / 2) / 3;
        for (int i = 0; i < frags.length; i++) {
            String frag = frags[i];
            guiGraphics.drawCenteredString(mc.font, Component.literal(frag).withStyle(ChatFormatting.YELLOW), width / 2, y + i * mc.font.lineHeight, 0xFFFFFF);
        }
    }
}