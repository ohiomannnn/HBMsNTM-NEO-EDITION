package com.hbm.inventory.screens;

import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;

public class ScreenUtils {

    public static boolean isHovered(int leftPos, int topPos, double mouseX, double mouseY, int left, int top, int sizeX, int sizeY) {
        return leftPos + left <= mouseX && leftPos + left + sizeX > mouseX && topPos + top < mouseY && topPos + top + sizeY >= mouseY;
    }

    public static void click(SoundManager man) {
        man.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1F));
    }
}
