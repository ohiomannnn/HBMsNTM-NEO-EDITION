package com.hbm.inventory.screen;

import com.hbm.HBMsNTM;
import com.hbm.inventory.menus.MachineSatLinkerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class MachineSatLinkerScreen extends InfoScreen<MachineSatLinkerMenu> {

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/gui/gui_linker.png");

    public MachineSatLinkerScreen(MachineSatLinkerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        List<Component> text = List.of(
                Component.literal("The first slot will copy the satellite/chip's"),
                Component.literal("frequency and paste it to the second slot.")
        );
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos - 16, this.topPos + 36, 16, 16, this.leftPos - 8, this.topPos + 36 + 16, text);

        List<Component> text1 = List.of(
                Component.literal("The third slot will randomize the"),
                Component.literal("satellite/chip's frequency.")
        );
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos - 16, this.topPos + 36 + 16, 16, 16, this.leftPos - 8, this.topPos + 36 + 16, text1);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int partialTicks) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        this.drawInfoPanel(guiGraphics, this.leftPos - 16, this.topPos + 36, 2);
        this.drawInfoPanel(guiGraphics, this.leftPos - 16, this.topPos + 36 + 16, 3);
    }
}
