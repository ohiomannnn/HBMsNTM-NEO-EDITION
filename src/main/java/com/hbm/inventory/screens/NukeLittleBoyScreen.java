package com.hbm.inventory.screens;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.bomb.NukeBoyBlockEntity;
import com.hbm.inventory.menus.NukeLittleBoyMenu;
import com.hbm.items.ModItems;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class NukeLittleBoyScreen extends InfoScreen<NukeLittleBoyMenu> {

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/gui/weapon/lil_boy_schematic.png");

    private final NukeBoyBlockEntity be;

    public NukeLittleBoyScreen(NukeLittleBoyMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        List<Component> text = new ArrayList<>();
        for (String s : I18nUtil.resolveKeyArray("gui.nukeLittleBoy.desc")) text.add(Component.literal(s));

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos - 16, this.topPos + 16, 16, 16, this.leftPos - 8, this.topPos + 16 + 16, text);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int partialTicks) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        if (be.isReady()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 142, this.topPos + 90, 176, 0, 16, 16);
        }

        if (be.slots.get(0).getItem() == ModItems.LITTLE_BOY_SHIELDING.get()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 27, this.topPos + 87, 176, 16, 21, 22);
        }
        if (be.slots.get(1).getItem() == ModItems.LITTLE_BOY_TARGET.get()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 27, this.topPos + 89, 176, 38, 21, 18);
        }
        if (be.slots.get(2).getItem() == ModItems.LITTLE_BOY_BULLET.get()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 74, this.topPos + 94, 176, 57, 19, 8);
        }
        if (be.slots.get(3).getItem() == ModItems.LITTLE_BOY_PROPELLANT.get()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 92, this.topPos + 95, 176, 66, 12, 6);
        }
        if (be.slots.get(4).getItem() == ModItems.LITTLE_BOY_IGNITER.get()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 107, this.topPos + 91, 176, 75, 16, 14);
        }

        this.drawInfoPanel(guiGraphics, this.leftPos - 16, this.topPos + 16, 2);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }
}
