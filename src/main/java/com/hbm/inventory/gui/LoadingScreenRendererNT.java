package com.hbm.inventory.gui;

import com.hbm.handler.HTTPHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Random;

public class LoadingScreenRendererNT {

    private final Minecraft mc;

    public String tipOfTheDay = "Tip of the day: " + chooseTip();

    public LoadingScreenRendererNT(Minecraft mc) { this.mc = mc; }

    private String chooseTip() {
        if (HTTPHandler.tipOfTheDay.isEmpty()) return "Explore! There's tons of free stuff to find.";
        return HTTPHandler.tipOfTheDay.get(new Random().nextInt(HTTPHandler.tipOfTheDay.size()));
    }

    public void resetMessage() {
        if (!HTTPHandler.tipOfTheDay.isEmpty()) {
            this.tipOfTheDay = "Tip of the day: " + HTTPHandler.tipOfTheDay.get(new Random().nextInt(HTTPHandler.tipOfTheDay.size()));
        } else {
            this.tipOfTheDay = "Explore! There's tons of free stuff to find.";
        }
    }

    public void render(GuiGraphics guiGraphics) {
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        String[] frags = this.tipOfTheDay.split("\\$");
        int baseY = height / 2 - 60 - mc.font.lineHeight;
        for (int i = 0; i < frags.length; i++) {
            String frag = frags[i];
            guiGraphics.drawCenteredString(mc.font, ChatFormatting.YELLOW + frag, width / 2, baseY + i * mc.font.lineHeight, 0xFFFFFF);
        }
    }
}
