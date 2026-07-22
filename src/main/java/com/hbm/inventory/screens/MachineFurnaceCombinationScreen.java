package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineFurnaceCombinationBlockEntity;
import com.hbm.inventory.menus.MachineFurnaceCombinationMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MachineFurnaceCombinationScreen extends InfoScreen<MachineFurnaceCombinationMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_furnace_combination.png");

    private final MachineFurnaceCombinationBlockEntity be;

    public MachineFurnaceCombinationScreen(MachineFurnaceCombinationMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 186;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.be.tank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 118, this.topPos + 18, 16, 52);
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 44, this.topPos + 36, 39, 7, mouseX, mouseY,
                Component.literal(String.format("%,d / %,d TU", this.be.progress, MachineFurnaceCombinationBlockEntity.PROCESS_TIME)).withStyle(ChatFormatting.YELLOW));
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 44, this.topPos + 45, 39, 7, mouseX, mouseY,
                Component.literal(String.format("%,d / %,d TU", this.be.heat, MachineFurnaceCombinationBlockEntity.MAX_HEAT)).withStyle(ChatFormatting.YELLOW));
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        int progress = this.be.getProgressScaled(38);
        if(progress > 0) guiGraphics.blit(TEXTURE, this.leftPos + 45, this.topPos + 37, 176, 0, progress, 5, 256, 256);
        int heat = this.be.getHeatScaled(37);
        if(heat > 0) guiGraphics.blit(TEXTURE, this.leftPos + 45, this.topPos + 46, 176, 5, heat, 5, 256, 256);
        this.be.tank.renderTank(this.leftPos + 118, this.topPos + 70, 0.0F, 16, 52);
    }
}
