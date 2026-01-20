package com.hbm.blocks;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.List;

public interface ILookOverlay {
    @OnlyIn(Dist.CLIENT)
    void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos);

    @OnlyIn(Dist.CLIENT)
    static void printGeneric(RenderGuiEvent.Pre event, String title, int titleCol, int bgCol, List<String> text) {
        Minecraft mc = Minecraft.getInstance();

        Options options = mc.options;
        if (!options.getCameraType().isFirstPerson()) return;
        if (options.hideGui) return;
        if (mc.gameMode.getPlayerMode() == GameType.SPECTATOR) return;

        Window window = mc.getWindow();
        int pX = window.getGuiScaledWidth() / 2 + 8;
        int pZ = window.getGuiScaledHeight() / 2;

        Font font = mc.font;

        event.getGuiGraphics().drawString(font, title, pX + 1, pZ - 9, bgCol);
        event.getGuiGraphics().drawString(font, title, pX, pZ - 10, titleCol);

        for (String line : text) {

            int color = 0xFFFFFF;
            if (line.startsWith("&[")) {
                int end = line.lastIndexOf("&]");
                color = Integer.parseInt(line.substring(2, end));
                line = line.substring(end + 2);
            }

            event.getGuiGraphics().drawString(font, line, pX, pZ, color, true);

            pZ += 10;
        }
    }
}
