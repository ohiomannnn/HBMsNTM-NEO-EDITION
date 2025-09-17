package com.hbm.inventory.gui;

import com.hbm.HBMsNTM;
import com.hbm.inventory.container.ContainerCrateIron;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GUICrateIron extends AbstractContainerScreen<ContainerCrateIron> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/gui/storage/gui_crate_iron.png");

    public GUICrateIron(ContainerCrateIron menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 176;
        this.imageHeight = 186;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        String name = this.title.getString();
        guiGraphics.drawString(this.font, name,
                this.imageWidth / 2 - this.font.width(name) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, I18n.get("container.inventory"),
                8, this.imageHeight - 96 + 2, 4210752, false);
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
