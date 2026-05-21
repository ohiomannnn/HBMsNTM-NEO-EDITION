package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineAssemblyMachineBlockEntity;
import com.hbm.inventory.menus.MachineAssemblyMachineMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MachineAssemblyMachineScreen extends InfoScreen<MachineAssemblyMachineMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/assembler.png");

    private final MachineAssemblyMachineBlockEntity be;

    public MachineAssemblyMachineScreen(MachineAssemblyMachineMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 256;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        be.inputTank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 8, this.topPos + 99, 52, 16);
        be.outputTank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 80, this.topPos + 99, 52, 16);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 152, this.topPos + 18, 16, 61, be.power, be.maxPower);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        int p = (int) (be.power * 61 / be.maxPower);
        guiGraphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 79 - p, 176, 61 - p, 16, p);

        if(be.assemblerModule.progress > 0) {
            int j = (int) Math.ceil(70 * be.assemblerModule.progress);
            guiGraphics.blit(TEXTURE, this.leftPos + 62, this.topPos + 126, 176, 61, j, 16);
        }


        be.inputTank.renderTank(this.leftPos + 8, this.topPos + 115, 52, 16, 1);
        be.outputTank.renderTank(this.leftPos + 80, this.topPos + 115, 52, 16, 1);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 70 - font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }
}
