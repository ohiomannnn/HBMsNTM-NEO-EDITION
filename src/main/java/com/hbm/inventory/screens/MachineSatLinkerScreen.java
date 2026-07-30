package com.hbm.inventory.screens;

import com.hbm.inventory.menus.MachineSatLinkerMenu;
import com.hbm.main.NuclearTechMod;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class MachineSatLinkerScreen extends InfoScreen<MachineSatLinkerMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/gui_sat_linker.png");

    public MachineSatLinkerScreen(MachineSatLinkerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 186;
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

        List<Component> chipText = new ArrayList<>(); for(String s : I18nUtil.resolveKeyArray("container.sat_linker.chip")) chipText.add(Component.literal(s));
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 12, this.topPos + 28, 16, 16, this.leftPos + 20, this.topPos + 28 + 16, chipText);

        List<Component> randomText = new ArrayList<>(); for(String s : I18nUtil.resolveKeyArray("container.sat_linker.random")) randomText.add(Component.literal(s));
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 12, this.topPos + 28 + 16, 16, 16, this.leftPos + 20, this.topPos + 28 + 32, randomText);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int partialTicks) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        this.drawInfoPanel(guiGraphics, this.leftPos + 12, this.topPos + 28, 2);
        this.drawInfoPanel(guiGraphics, this.leftPos + 12, this.topPos + 28 + 16, 3);
    }
}
