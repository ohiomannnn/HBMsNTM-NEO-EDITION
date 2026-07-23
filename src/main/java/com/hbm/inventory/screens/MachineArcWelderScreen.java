package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineArcWelderBlockEntity;
import com.hbm.inventory.menus.MachineArcWelderMenu;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MachineArcWelderScreen extends InfoScreen<MachineArcWelderMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_arc_welder.png");

    private final MachineArcWelderBlockEntity be;

    public MachineArcWelderScreen(MachineArcWelderMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 204;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.be.tank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 35, this.topPos + 63, 34, 16);
        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 152, this.topPos + 18, 16, 52, this.be.getPower(), this.be.getMaxPower());
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 78, this.topPos + 67, 8, 8, mouseX, mouseY, this.getUpgradeInfo());
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int titleX = this.imageWidth / 2 - this.font.width(this.title) / 2 - 18;
        guiGraphics.drawString(this.font, this.title, titleX, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int p = (int) (this.be.power * 52 / Math.max(this.be.maxPower, 1));
        guiGraphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 70 - p, 176, 52 - p, 16, p);

        int i = this.be.progress * 33 / Math.max(this.be.processTime, 1);
        guiGraphics.blit(TEXTURE, this.leftPos + 72, this.topPos + 37, 192, 0, i, 14);

        if(this.be.power >= this.be.consumption) {
            guiGraphics.blit(TEXTURE, this.leftPos + 156, this.topPos + 4, 176, 52, 9, 12);
        }

        this.drawInfoPanel(guiGraphics, this.leftPos + 78, this.topPos + 67, 8);
        this.be.tank.renderTank(this.leftPos + 35, this.topPos + 79, 0, 34, 16, 1);
    }

    private List<Component> getUpgradeInfo() {
        List<Component> lines = new ArrayList<>();
        HashMap<MachineUpgradeItem.UpgradeType, Integer> levels = new HashMap<>();

        for(int slot = 6; slot <= 7; slot++) {
            ItemStack stack = this.be.getItem(slot);
            if(stack.getItem() instanceof MachineUpgradeItem item && this.be.getValidUpgrades().containsKey(item.type)) {
                levels.merge(item.type, item.tier, Integer::sum);
            }
        }

        if(levels.isEmpty()) {
            lines.add(Component.literal("No upgrades installed"));
            return lines;
        }

        for(MachineUpgradeItem.UpgradeType type : MachineUpgradeItem.UpgradeType.values()) {
            Integer level = levels.get(type);
            if(level == null || level <= 0) continue;
            List<String> raw = new ArrayList<>();
            this.be.provideInfo(type, level, raw, false);
            for(String line : raw) lines.add(Component.literal(line));
        }

        return lines;
    }
}
