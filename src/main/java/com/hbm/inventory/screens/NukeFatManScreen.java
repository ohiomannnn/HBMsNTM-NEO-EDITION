package com.hbm.inventory.screens;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.bomb.NukeFatManBlockEntity;
import com.hbm.blockentity.bomb.NukeGadgetBlockEntity;
import com.hbm.inventory.menus.NukeFatManMenu;
import com.hbm.items.ModItems;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class NukeFatManScreen extends InfoScreen<NukeFatManMenu> {

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/gui/weapon/fat_man_schematic.png");

    private final NukeFatManBlockEntity be;

    public NukeFatManScreen(NukeFatManMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        List<Component> text = new ArrayList<>();
        for (String s : I18nUtil.resolveKeyArray("gui.nukeFatMan.desc")) text.add(Component.literal(s));

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos - 16, this.topPos + 16, 16, 16, this.leftPos - 8, this.topPos + 16 + 16, text);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        if (be.isReady()) guiGraphics.blit(TEXTURE, this.leftPos + 134, this.topPos + 35, 176, 48, 16, 16);

        for (int index = 0; index < 4; index++) {
            if (be.slots.get(index).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get()) {
                switch (index) {
                    case 0 -> guiGraphics.blit(TEXTURE, this.leftPos + 82, this.topPos + 19, 176, 0, 24, 24);
                    case 1 -> guiGraphics.blit(TEXTURE, this.leftPos + 106, this.topPos + 19, 200, 0, 24, 24);
                    case 2 -> guiGraphics.blit(TEXTURE, this.leftPos + 82, this.topPos + 43, 176, 24, 24, 24);
                    case 3 -> guiGraphics.blit(TEXTURE, this.leftPos + 106, this.topPos + 43, 200, 24, 24, 24);
                }
            }
        }

        this.drawInfoPanel(guiGraphics, this.leftPos - 16, this.topPos + 16, 2);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }
}
