package com.hbm.inventory.screens;

import com.hbm.blockentity.bomb.NukeSoliniumBlockEntity;
import com.hbm.inventory.menus.NukeSoliniumMenu;
import com.hbm.items.NtmItems;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class NukeSoliniumScreen extends InfoScreen<NukeSoliniumMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/weapon/solinium_schematic.png");

    private final NukeSoliniumBlockEntity be;

    public NukeSoliniumScreen(NukeSoliniumMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 222;
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

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        if(be.slots.get(0).is(NtmItems.SOLINIUM_IGNITER.get())) guiGraphics.blit(TEXTURE, this.leftPos + 24, this.topPos + 84, 0, 222, 22, 14);
        if(be.slots.get(1).is(NtmItems.SOLINIUM_PROPELLANT.get())) guiGraphics.blit(TEXTURE, this.leftPos + 46, this.topPos + 84, 22, 222, 18, 14);
        if(be.slots.get(2).is(NtmItems.SOLINIUM_PROPELLANT.get())) guiGraphics.blit(TEXTURE, this.leftPos + 76, this.topPos + 84, 52, 222, 18, 14);
        if(be.slots.get(3).is(NtmItems.SOLINIUM_IGNITER.get())) guiGraphics.blit(TEXTURE, this.leftPos + 94, this.topPos + 84, 70, 222, 22, 14);
        if(be.slots.get(4).is(NtmItems.SOLINIUM_CORE.get())) guiGraphics.blit(TEXTURE, this.leftPos + 64, this.topPos + 84, 40, 222, 12, 28);
        if(be.slots.get(5).is(NtmItems.SOLINIUM_IGNITER.get())) guiGraphics.blit(TEXTURE, this.leftPos + 24, this.topPos + 98, 0, 236, 22, 14);
        if(be.slots.get(6).is(NtmItems.SOLINIUM_PROPELLANT.get())) guiGraphics.blit(TEXTURE, this.leftPos + 46, this.topPos + 98, 22, 236, 18, 14);
        if(be.slots.get(7).is(NtmItems.SOLINIUM_PROPELLANT.get())) guiGraphics.blit(TEXTURE, this.leftPos + 76, this.topPos + 98, 52, 236, 18, 14);
        if(be.slots.get(8).is(NtmItems.SOLINIUM_IGNITER.get())) guiGraphics.blit(TEXTURE, this.leftPos + 94, this.topPos + 98, 70, 236, 22, 14);

        if(be.isReady()) guiGraphics.blit(TEXTURE, this.leftPos + 134, this.topPos + 90, 176, 0, 16, 16);
    }
}
