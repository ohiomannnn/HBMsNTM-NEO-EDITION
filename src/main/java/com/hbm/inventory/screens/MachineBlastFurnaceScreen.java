package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineBlastFurnaceBlockEntity;
import com.hbm.inventory.menus.MachineBlastFurnaceMenu;
import com.hbm.inventory.screens.element.ScreenElements;
import com.hbm.main.NuclearTechMod;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MachineBlastFurnaceScreen extends InfoScreen<MachineBlastFurnaceMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_blast_furnace.png");

    private final MachineBlastFurnaceBlockEntity be;

    public MachineBlastFurnaceScreen(MachineBlastFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.be.tanks[0].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 25, this.topPos + 71, 18, 18);
        this.be.tanks[1].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 25, this.topPos + 17, 18, 18);
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 79, this.topPos + 62, 18, 18, mouseX, mouseY,
                Component.literal("Speed: " + (int) (this.be.speed * 100F) + "%").withStyle(ChatFormatting.YELLOW));
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

        int fuel = this.be.getFuelScaled(26);
        int progress = this.be.getProgressScaled(88 - fuel);
        if(progress > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 62, this.topPos + 106 - progress - fuel, 176, 102 - progress, 56, progress, 256, 256);
        }
        if(fuel > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 62, this.topPos + 106 - fuel, 176, 128 - fuel, 56, fuel, 256, 256);
        }
        if(this.be.isProgressing) {
            guiGraphics.blit(TEXTURE, this.leftPos + 81, this.topPos + 64, 176, 0, 14, 14, 256, 256);
        }

        float airblast = this.be.tanks[0].getFill() / (float) this.be.tanks[0].getMaxFill();
        float flue = this.be.tanks[1].getFill() / (float) this.be.tanks[1].getMaxFill();
        ScreenElements.drawSmoothGauge(this.leftPos + 34, this.topPos + 80, airblast, 5, 2, 1, 0xFF800000);
        ScreenElements.drawSmoothGauge(this.leftPos + 34, this.topPos + 26, flue, 5, 2, 1, 0xFF800000);
    }
}
