package com.hbm.inventory.gui;

import com.hbm.handler.HTTPHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Random;

public class LoadingScreenRendererNT {

    private final Minecraft mc;
    private String currentlyDisplayedText = "";

    public String tipOfTheDay = "Tip of the day: " + chooseTip();

    public LoadingScreenRendererNT(Minecraft mc) {
        this.mc = mc;
    }

    private String chooseTip() {
        if (HTTPHandler.tipOfTheDay.isEmpty()) return "Explore! There's tons of free stuff to find.";
        return HTTPHandler.tipOfTheDay.get(new Random().nextInt(HTTPHandler.tipOfTheDay.size()));
    }

    public void resetProgressAndMessage(String message) {
        this.setMessage(message);

        if (!HTTPHandler.tipOfTheDay.isEmpty()) {
            this.tipOfTheDay = "Tip of the day: " +
                    HTTPHandler.tipOfTheDay.get(new Random().nextInt(HTTPHandler.tipOfTheDay.size()));
        } else {
            this.tipOfTheDay = "Explore! There's tons of free stuff to find.";
        }
    }

    private void setMessage(String message) {
        this.currentlyDisplayedText = message;
    }

    public void render(GuiGraphics gg, int progress) {
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        if (progress >= 0) {
            int barWidth = 100;
            int barHeight = 2;
            int x = width / 2 - barWidth / 2;
            int y = height / 2 + 16;

            int clamped = Math.min(progress, barWidth);
            gg.fill(x, y, x + barWidth, y + barHeight, 0xFF808080);
            gg.fill(x, y, x + clamped, y + barHeight, 0xFF00FF00);
        }

        gg.drawCenteredString(mc.font, this.currentlyDisplayedText, width / 2, height / 2 - 20, 0xFFFFFF);
        String message = "";
        gg.drawCenteredString(mc.font, message, width / 2, height / 2 - 4, 0xFFFFFF);

        String[] frags = this.tipOfTheDay.split("\\$");
        int baseY = height / 2 - 60 - mc.font.lineHeight;
        for (int i = 0; i < frags.length; i++) {
            String frag = frags[i];
            gg.drawCenteredString(mc.font, ChatFormatting.YELLOW + frag,
                    width / 2, baseY + i * mc.font.lineHeight, 0xFFFFFF);
        }
    }
}
