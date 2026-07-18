package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineSolderingStationBlockEntity;
import com.hbm.inventory.menus.MachineSolderingStationMenu;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MachineSolderingStationScreen extends InfoScreen<MachineSolderingStationMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_soldering_station.png");

    private final MachineSolderingStationBlockEntity be;

    public MachineSolderingStationScreen(MachineSolderingStationMenu menu, Inventory inventory, Component title) {
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

        List<Component> lines = new ArrayList<>();
        lines.add(Component.literal("Recipe Collision Prevention: ").append(this.be.collisionPrevention ? Component.literal("ON").withStyle(ChatFormatting.GREEN) : Component.literal("OFF").withStyle(ChatFormatting.RED)));
        lines.add(Component.literal("Prevents no-fluid recipes from being processed"));
        lines.add(Component.literal("when fluid is present."));
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 5, this.topPos + 66, 10, 10, mouseX, mouseY, lines);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if(this.isHovered(x, y, 5, 66, 10, 10)) {
            this.click();
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("collision", true);
            PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
            return true;
        }

        return super.mouseClicked(x, y, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 8, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int p = (int) (this.be.power * 52 / Math.max(this.be.maxPower, 1));
        guiGraphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 70 - p, 176, 52 - p, 16, p);

        int i = this.be.progress * 33 / Math.max(this.be.processTime, 1);
        guiGraphics.blit(TEXTURE, this.leftPos + 72, this.topPos + 28, 192, 0, i, 14);

        if(this.be.power >= this.be.consumption) {
            guiGraphics.blit(TEXTURE, this.leftPos + 156, this.topPos + 4, 176, 52, 9, 12);
        }

        this.drawInfoPanel(guiGraphics, this.leftPos + 78, this.topPos + 67, 8);
        this.be.tank.renderTank(this.leftPos + 35, this.topPos + 79, 0, 34, 16, 1);
    }

    private List<Component> getUpgradeInfo() {
        List<Component> lines = new ArrayList<>();
        HashMap<UpgradeType, Integer> levels = new HashMap<>();

        for(int slot = 9; slot <= 10; slot++) {
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
