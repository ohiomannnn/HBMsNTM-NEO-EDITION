package com.hbm.inventory.screens;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.machine.storage.MachineFluidTankBlockEntity;
import com.hbm.inventory.menus.MachineFluidTankMenu;
import com.hbm.network.toserver.CompoundTagControl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class MachineFluidTankScreen extends InfoScreen<MachineFluidTankMenu> {

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/gui/storage/gui_tank.png");

    public MachineFluidTankBlockEntity be;

    public MachineFluidTankScreen(MachineFluidTankMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {

        CompoundTag tag = new CompoundTag();

        if (this.checkClick((int) x, (int) y, 151, 35, 18, 18)) { this.click(); tag.putBoolean("mode", true); }

        if (!tag.isEmpty()) PacketDistributor.sendToServer(new CompoundTagControl(tag, be.getBlockPos()));

        return super.mouseClicked(x, y, button);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int partialTicks) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        guiGraphics.blit(TEXTURE, this.leftPos + 151, this.topPos + 34, 176, be.mode * 18, 18, 18);

        be.tank.renderTank(this.leftPos + 71, this.topPos + 69, 0, 34, 52);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.imageHeight - 96 + 2, 4210752, false);
    }
}
