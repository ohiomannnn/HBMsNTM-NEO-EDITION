package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachinePressBlockEntity;
import com.hbm.inventory.menus.MachinePressMenu;
import com.hbm.inventory.screens.element.ScreenElements;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MachinePressScreen extends InfoScreen<MachinePressMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_press.png");

    private final MachinePressBlockEntity be;

    public MachinePressScreen(MachinePressMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 214;
    }

    @Override
    protected void init() {
        super.init();

        this.titleLabelX = this.imageWidth / 2 - this.font.width(this.title) / 2;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 96 + 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 25, this.topPos + 16, 18, 18, mouseX, mouseY, Component.literal((be.speed * 100 / MachinePressBlockEntity.maxSpeed) + "%"));
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 25, this.topPos + 34, 18, 18, mouseX, mouseY, Component.translatable("container.press.left", (be.burnTime / 200)));

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        if(be.burnTime >= 20) {
            guiGraphics.blit(TEXTURE, this.leftPos + 26, this.topPos + 36, 0, 214, 14, 14);
        }

        int k = (int) (be.renderPress * 16 / be.maxPress);
        guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 35, 15, 214, 18, k);

        float i = (float) be.speed / (float) be.maxSpeed;
        ScreenElements.drawSmoothGauge(this.leftPos + 34, this.topPos + 25, i, 5, 2, 1, 0xFF7f0000);
    }
}
