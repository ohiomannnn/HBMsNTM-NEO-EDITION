package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.storage.BarrelBlockEntity;
import com.hbm.inventory.menus.BarrelMenu;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class BarrelScreen extends InfoScreen<BarrelMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/storage/gui_tank.png");

    private final BarrelBlockEntity be;

    public BarrelScreen(BarrelMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        this.be.tank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 71, this.topPos + 69 - 52, 34, 52);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.isHovered((int) mouseX, (int) mouseY, 151, 34, 18, 18)) {
            this.click();
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("Mode", true);
            PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.blit(TEXTURE, this.leftPos + 151, this.topPos + 34, 176, this.be.mode * 18, 18, 18);
        this.be.tank.renderTank(this.leftPos + 71, this.topPos + 69, 0, 34, 52);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.imageHeight - 96 + 2, 4210752, false);
    }
}
