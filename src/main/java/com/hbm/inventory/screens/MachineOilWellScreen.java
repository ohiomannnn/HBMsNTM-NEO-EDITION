package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.oil.MachineOilWellBlockEntity;
import com.hbm.inventory.menus.MachineOilWellMenu;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.main.NuclearTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MachineOilWellScreen extends InfoScreen<MachineOilWellMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/machine/gui_well.png");

    private final MachineOilWellBlockEntity be;

    public MachineOilWellScreen(MachineOilWellMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;

        this.imageWidth = 184;
        this.imageHeight = 190;
    }

    @Override
    protected void init() {
        super.init();

        this.titleLabelX = 126 - this.font.width(this.title) / 2;
        this.titleLabelY = 10;
        this.inventoryLabelX = 12;
        this.inventoryLabelY = this.imageHeight - 96 + 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.be.tanks[0].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 76, this.topPos + 74 - 52, 16, 52);
        this.be.tanks[1].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 112, this.topPos + 74 - 52, 16, 52);

        if(this.be.tanks.length >= 3) {
            this.be.tanks[2].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 54, this.topPos + 45, 6, 32);
        }

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 160, this.topPos + 21, 8, 8, mouseX, mouseY, this.getUpgradeInfo());

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 8, this.topPos + 22, 16, 34, this.be.power, this.be.getMaxPower());

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int i = (int) (this.be.power * 34 / this.be.getMaxPower());
        guiGraphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 56 - i, 184, 34 - i, 16, i);

        int k = this.be.indicator;

        if(k != 0) guiGraphics.blit(TEXTURE, this.leftPos + 50, this.topPos + 19, 184 + (k - 1) * 14, 34, 14, 14);

        if(be.tanks.length < 3) {
            guiGraphics.blit(TEXTURE, this.leftPos + 48, this.topPos + 44, 200, 0, 18, 34);
        }

        be.tanks[0].renderTank(this.leftPos + 76, this.topPos + 74, 1F, 16, 52);
        be.tanks[1].renderTank(this.leftPos + 112, this.topPos + 74, 1F, 16, 52);

        if(be.tanks.length > 2) {
            be.tanks[2].renderTank(this.leftPos + 54, this.topPos + 77, 1F, 6, 32);
        }

        this.drawInfoPanel(guiGraphics, this.leftPos + 160, this.topPos + 21, 8);
    }

    private List<Component> getUpgradeInfo() {
        List<Component> lines = new ArrayList<>();
        HashMap<UpgradeType, Integer> levels = new HashMap<>();

        for(int slot = 5; slot <= 7; slot++) {
            ItemStack stack = this.be.getItem(slot);
            if(stack.getItem() instanceof MachineUpgradeItem item && this.be.getValidUpgrades().containsKey(item.type)) {
                levels.merge(item.type, item.tier, Integer::sum);
            }
        }

        if(levels.isEmpty()) {
            lines.add(Component.literal("No upgrades installed"));
            return lines;
        }

        for(UpgradeType type : UpgradeType.values()) {
            Integer level = levels.get(type);
            if(level == null || level <= 0) continue;

            this.be.provideInfo(type, level, lines, false);
        }

        return lines;
    }
}
