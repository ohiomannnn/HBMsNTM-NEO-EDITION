package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineCentrifugeBlockEntity;
import com.hbm.inventory.menus.MachineCentrifugeMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MachineCentrifugeScreen extends InfoScreen<MachineCentrifugeMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/gui_centrifuge.png");

    private final MachineCentrifugeBlockEntity be;

    public MachineCentrifugeScreen(MachineCentrifugeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 186;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 9, this.topPos + 12, 16, 35, this.be.getPower(), this.be.getMaxPower());
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(this.be.hasPower()) {
            int power = this.be.getPowerRemainingScaled(35);
            guiGraphics.blit(TEXTURE, this.leftPos + 9, this.topPos + 48 - power, 176, 35 - power, 16, power);
        }

        if(this.be.isProcessing()) {
            int progress = this.be.getCentrifugeProgressScaled(145);
            for(int i = 0; i < 4; i++) {
                int piece = Math.min(progress, 36);
                if(piece > 0) {
                    guiGraphics.blit(TEXTURE, this.leftPos + 65 + i * 20, this.topPos + 50 - piece, 176, 71 - piece, 12, piece);
                }
                progress -= piece;
            }
        }
    }
}
