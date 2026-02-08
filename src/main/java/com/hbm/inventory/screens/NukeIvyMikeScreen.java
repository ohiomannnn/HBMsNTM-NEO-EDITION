package com.hbm.inventory.screens;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.bomb.NukeIvyMikeBlockEntity;
import com.hbm.inventory.menus.NukeIvyMikeMenu;
import com.hbm.items.ModItems;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class NukeIvyMikeScreen extends InfoScreen<NukeIvyMikeMenu> {

    public static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/gui/weapon/ivy_mike_schematic.png");

    private final NukeIvyMikeBlockEntity be;

    public NukeIvyMikeScreen(NukeIvyMikeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 217;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        List<Component> text = new ArrayList<>();
        for (String s : I18nUtil.resolveKeyArray("gui.nukeIvyMike.desc")) text.add(Component.literal(s));

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos - 16, this.topPos + 16, 16, 16, this.leftPos - 8, this.topPos + 16 + 16, text);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        if (be.isFilled()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 5, this.topPos + 35, 177, 1, 16, 16);
        }
        if (be.isReady()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 5, this.topPos + 35, 177, 19, 16, 16);
        }

        if (be.slots.get(5).getItem() == ModItems.IVY_MIKE_CORE.get()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 75, this.topPos + 25, 176, 49, 80, 36);
        }
        if (be.slots.get(6).getItem() == ModItems.IVY_MIKE_DEUT.get()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 30, 180, 88, 58, 26);
        }
        if (be.slots.get(7).getItem() == ModItems.IVY_MIKE_COOLING_UNIT.get()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 140, this.topPos + 30, 240, 88, 12, 26);
        }

        for (int index = 0; index < 4; index++) {
            if (be.slots.get(index).getItem() == ModItems.EARLY_EXPLOSIVE_LENSES.get()) {
                switch (index) {
                    case 0 -> guiGraphics.blit(TEXTURE, this.leftPos + 24, this.topPos + 20, 209, 1, 23, 23);
                    case 1 -> guiGraphics.blit(TEXTURE, this.leftPos + 47, this.topPos + 20, 232, 1, 23, 23);
                    case 2 -> guiGraphics.blit(TEXTURE, this.leftPos + 24, this.topPos + 43, 209, 24, 23, 23);
                    case 3 -> guiGraphics.blit(TEXTURE, this.leftPos + 47, this.topPos + 43, 232, 24, 23, 23);
                }
            }
        }

        this.drawInfoPanel(guiGraphics, this.leftPos - 16, this.topPos + 16, 2);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - font.width(this.title) / 2, 4, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }
}
