package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.oil.MachineOilDerrickBlockEntity;
import com.hbm.inventory.menus.MachineOilDerrickMenu;
import com.hbm.main.NuclearTechMod;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MachineOilDerrickScreen extends InfoScreen<MachineOilDerrickMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/machine/gui_oil_derrick.png");

    private final MachineOilDerrickBlockEntity be;

    public MachineOilDerrickScreen(MachineOilDerrickMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.be.tanks[0].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 62, this.topPos + 17, 16, 52);
        this.be.tanks[1].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 107, this.topPos + 17, 16, 52);

        if(this.be.tanks.length >= 3) {
            this.be.tanks[2].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 40, this.topPos + 37, 6, 32);
        }

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 156, this.topPos + 3, 8, 8, mouseX, mouseY, this.getUpgradeInfo());
        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 8, this.topPos + 17, 16, 34, this.be.power, this.be.getMaxPower());

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 151 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int p = (int) (this.be.power * 34 / Math.max(this.be.getMaxPower(), 1L));
        guiGraphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 51 - p, 176, 34 - p, 16, p);

        if(this.be.indicator != 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 35, this.topPos + 17, 176 + (this.be.indicator - 1) * 16, 52, 16, 16);
        }

        if(this.be.tanks.length < 3) {
            guiGraphics.blit(TEXTURE, this.leftPos + 34, this.topPos + 36, 192, 0, 18, 34);
        }

        this.be.tanks[0].renderTank(this.leftPos + 62, this.topPos + 69, 0, 16, 52);
        this.be.tanks[1].renderTank(this.leftPos + 107, this.topPos + 69, 0, 16, 52);

        if(this.be.tanks.length > 2) {
            this.be.tanks[2].renderTank(this.leftPos + 40, this.topPos + 69, 0, 6, 32);
        }

        this.drawInfoPanel(guiGraphics, this.leftPos + 156, this.topPos + 3, 8);
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

            List<String> raw = new ArrayList<>();
            this.be.provideInfo(type, level, raw, false);

            for(String line : raw) {
                lines.add(Component.literal(line));
            }
        }

        return lines;
    }
}
