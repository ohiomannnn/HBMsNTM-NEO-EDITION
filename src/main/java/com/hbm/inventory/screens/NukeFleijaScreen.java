package com.hbm.inventory.screens;

import com.hbm.main.NuclearTechMod;
import com.hbm.blockentity.bomb.NukeFleijaBlockEntity;
import com.hbm.inventory.menus.NukeFleijaMenu;
import com.hbm.items.ModItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class NukeFleijaScreen extends InfoScreen<NukeFleijaMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/weapon/fleija_schematic.png");

    private final NukeFleijaBlockEntity be;

    public NukeFleijaScreen(NukeFleijaMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        if (be.slots.get(0).is(ModItems.FLEIJA_IGNITER.get())) guiGraphics.blit(TEXTURE, this.leftPos + 7, this.topPos + 88, 176, 0, 30, 20);
        if (be.slots.get(1).is(ModItems.FLEIJA_IGNITER.get())) guiGraphics.blit(TEXTURE, this.leftPos + 139, this.topPos + 88, 206, 0, 30, 20);
        if (be.slots.get(2).is(ModItems.FLEIJA_PROPELLANT.get())) guiGraphics.blit(TEXTURE, this.leftPos + 57, this.topPos + 77, 176, 62, 18, 14);
        if (be.slots.get(3).is(ModItems.FLEIJA_PROPELLANT.get())) guiGraphics.blit(TEXTURE, this.leftPos + 57, this.topPos + 91, 176, 76, 18, 14);
        if (be.slots.get(4).is(ModItems.FLEIJA_PROPELLANT.get())) guiGraphics.blit(TEXTURE, this.leftPos + 57, this.topPos + 105, 176, 90, 18, 14);
        if (be.slots.get(5).is(ModItems.FLEIJA_CORE.get())) guiGraphics.blit(TEXTURE, this.leftPos + 85, this.topPos + 77, 176, 20, 18, 15);
        if (be.slots.get(6).is(ModItems.FLEIJA_CORE.get())) guiGraphics.blit(TEXTURE, this.leftPos + 103, this.topPos + 77, 194, 20, 18, 15);
        if (be.slots.get(7).is(ModItems.FLEIJA_CORE.get())) guiGraphics.blit(TEXTURE, this.leftPos + 85, this.topPos + 92, 176, 35, 18, 12);
        if (be.slots.get(8).is(ModItems.FLEIJA_CORE.get())) guiGraphics.blit(TEXTURE, this.leftPos + 103, this.topPos + 92, 194, 35, 18, 12);
        if (be.slots.get(9).is(ModItems.FLEIJA_CORE.get())) guiGraphics.blit(TEXTURE, this.leftPos + 85, this.topPos + 104, 176, 47, 18, 15);
        if (be.slots.get(10).is(ModItems.FLEIJA_CORE.get())) guiGraphics.blit(TEXTURE, this.leftPos + 103, this.topPos + 104, 194, 47, 18, 15);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }
}
