package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.storage.CrateBaseBlockEntity;
import com.hbm.inventory.menus.CrateMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CrateScreen extends InfoScreen<CrateMenu> {

    private final CrateBaseBlockEntity crate;
    private final ResourceLocation texture;

    public CrateScreen(CrateMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.crate = menu.be;
        this.imageWidth = crate.getGuiWidth();
        this.imageHeight = crate.getGuiHeight();
        this.inventoryLabelX = crate.getInventoryLabelX();
        this.texture = NuclearTechMod.withDefaultNamespace("textures/gui/storage/" + crate.getTexture() + ".png");
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(this.texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, this.crate.getTitleColor(), false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.imageHeight - 96 + 2, this.crate.getInventoryLabelColor(), false);
    }
}
