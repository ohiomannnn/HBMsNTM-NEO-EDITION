package com.hbm.inventory.screens;

import com.hbm.blockentity.bomb.NukeN2BlockEntity;
import com.hbm.inventory.menus.NukeN2Menu;
import com.hbm.items.NtmItems;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class NukeN2Screen extends InfoScreen<NukeN2Menu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/weapon/n2_schematic.png");

    private final NukeN2BlockEntity be;

    public NukeN2Screen(NukeN2Menu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

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

        int count = 0;
        for(int i = 0; i < 12; i++) {
            if(this.be.slots.get(i).getItem() == NtmItems.N2_CHARGE.get()) count++;
        }
        if(count > 0) guiGraphics.blit(TEXTURE, this.leftPos + 35, this.topPos + 120 - 6 * count, 176, 0, 34, 6 * count);
    }
}
