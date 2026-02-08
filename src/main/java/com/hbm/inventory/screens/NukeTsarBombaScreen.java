package com.hbm.inventory.screens;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.bomb.NukeTsarBombaBlockEntity;
import com.hbm.inventory.menus.NukeTsarBombaMenu;
import com.hbm.items.ModItems;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class NukeTsarBombaScreen extends InfoScreen<NukeTsarBombaMenu> {

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/gui/weapon/tsar_bomba_schematic.png");

    private final NukeTsarBombaBlockEntity be;

    public NukeTsarBombaScreen(NukeTsarBombaMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.be = menu.be;

        this.imageWidth = 256;
        this.imageHeight = 233;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        List<Component> text = new ArrayList<>();
        for (String s : I18nUtil.resolveKeyArray("gui.nukeTsarBomba.desc")) text.add(Component.literal(s));

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos - 16, this.topPos + 16, 16, 16, this.leftPos - 8, this.topPos + 16 + 16, text);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        if (be.isFilled()) {
            guiGraphics.blit(NukeIvyMikeScreen.TEXTURE, this.leftPos + 18, this.topPos + 50, 176, 0, 16, 16);
        }
        if (be.isReady()) {
            guiGraphics.blit(NukeIvyMikeScreen.TEXTURE, this.leftPos + 18, this.topPos + 50, 176, 18, 16, 16);
        }

        for (int index = 0; index < 4; index++) {
            if (be.slots.get(index).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get()) {
                switch (index) {
                    case 0 -> guiGraphics.blit(NukeIvyMikeScreen.TEXTURE, this.leftPos + 24 + 16, this.topPos + 20 + 16, 209, 1, 23, 23);
                    case 1 -> guiGraphics.blit(NukeIvyMikeScreen.TEXTURE, this.leftPos + 47 + 16, this.topPos + 20 + 16, 232, 1, 23, 23);
                    case 2 -> guiGraphics.blit(NukeIvyMikeScreen.TEXTURE, this.leftPos + 24 + 16, this.topPos + 43 + 16, 209, 24, 23, 23);
                    case 3 -> guiGraphics.blit(NukeIvyMikeScreen.TEXTURE, this.leftPos + 47 + 16, this.topPos + 43 + 16, 232, 24, 23, 23);
                }
            }
        }

        if (be.slots.get(5).getItem() == ModItems.TSAR_BOMBA_CORE.get()) guiGraphics.blit(NukeIvyMikeScreen.TEXTURE, this.leftPos + 75 + 16, this.topPos + 25 + 16, 176, 220, 80, 36);

        this.drawInfoPanel(guiGraphics, this.leftPos - 16, this.topPos + 16, 2);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 48, this.imageHeight - 96 + 2, 4210752, false);
    }
}
