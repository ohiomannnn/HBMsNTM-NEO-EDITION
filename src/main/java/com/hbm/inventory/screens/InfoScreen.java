package com.hbm.inventory.screens;

import com.hbm.HBMsNTM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.Arrays;
import java.util.List;

public abstract class InfoScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    private static final ResourceLocation GUI_UTIL = HBMsNTM.withDefaultNamespaceNT("textures/gui/gui_utility.png");

    public InfoScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    public void drawCustomInfoStat(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y, int width, int height, int tPosX, int tPosY, Component... text) {
        drawCustomInfoStat(guiGraphics, mouseX, mouseY, x, y, width, height, tPosX, tPosY, Arrays.asList(text));
    }

    public void drawCustomInfoStat(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y, int width, int height, int tPosX, int tPosY, List<Component> text) {
        Font font = Minecraft.getInstance().font;

        if (x <= mouseX && x + width > mouseX && y < mouseY && y + height >= mouseY) {
            guiGraphics.renderComponentTooltip(font, text, tPosX, tPosY);
        }
    }

    public void drawInfoPanel(GuiGraphics guiGraphics, int x, int y, int type) {
        switch (type) {
            case 0 -> guiGraphics.blit(GUI_UTIL, x, y, 0, 0, 8, 8); //Small blue I
            case 1 -> guiGraphics.blit(GUI_UTIL, x, y,  0, 8, 8, 8); //Small green I
            case 2 -> guiGraphics.blit(GUI_UTIL, x, y, 8, 0, 16, 16); //Large blue I
            case 3 -> guiGraphics.blit(GUI_UTIL, x, y, 24, 0, 16, 16); //Large green I
            case 4 -> guiGraphics.blit(GUI_UTIL, x, y, 0, 16, 8, 8); //Small red !
            case 5 -> guiGraphics.blit(GUI_UTIL, x, y, 0, 24, 8, 8); //Small yellow !
            case 6 -> guiGraphics.blit(GUI_UTIL, x, y, 8, 16, 16, 16); //Large red !
            case 7 -> guiGraphics.blit(GUI_UTIL, x, y, 24, 16, 16, 16); //Large yellow !
            case 8 -> guiGraphics.blit(GUI_UTIL, x, y, 0, 32, 8, 8); //Small blue *
            case 9 -> guiGraphics.blit(GUI_UTIL, x, y, 0, 40, 8, 8); //Small grey *
            case 10 -> guiGraphics.blit(GUI_UTIL, x, y, 8, 32, 16, 16); //Large blue *
            case 11 -> guiGraphics.blit(GUI_UTIL, x, y, 24, 32, 16, 16); //Large grey *
        }
    }
}
