package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.heater.HeaterOvenBlockEntity;
import com.hbm.blockentity.machine.heater.HeaterFireboxBlockEntity;
import com.hbm.inventory.menus.HeaterOvenMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class HeaterOvenScreen extends InfoScreen<HeaterOvenMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/machine/gui_heating_oven.png");

    private final HeaterFireboxBlockEntity be;

    public HeaterOvenScreen(HeaterOvenMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 168;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.renderTooltip(guiGraphics, mouseX, mouseY);

        Slot hoveredSlot = this.hoveredSlot;
        if(hoveredSlot != null && hoveredSlot.index < 2 && this.menu.getCarried().isEmpty() && !hoveredSlot.hasItem()) {
            List<Component> components = HeaterFireboxBlockEntity.burnModule.getDesc();
            if(!components.isEmpty()) guiGraphics.renderComponentTooltip(this.font, components, mouseX, mouseY);
        }

        this.drawCustomInfoStat(
                guiGraphics,
                mouseX,
                mouseY,
                this.leftPos + 81,
                this.topPos + 28,
                69,
                5,
                mouseX,
                mouseY,
                Component.literal(String.format("%,d", this.be.heatEnergy) + " / " + String.format("%,d", HeaterOvenBlockEntity.maxHeatEnergy) + " TU")
        );
        this.drawCustomInfoStat(
                guiGraphics,
                mouseX,
                mouseY,
                this.leftPos + 81,
                this.topPos + 37,
                70,
                5,
                mouseX,
                mouseY,
                Component.literal(String.format("%,d", this.be.burnHeat) + " TU/t"),
                Component.literal(String.format("%,d", this.be.burnTime / 20) + " s")
        );
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int heat = this.be.heatEnergy * 69 / Math.max(HeaterOvenBlockEntity.maxHeatEnergy, 1);
        guiGraphics.blit(TEXTURE, this.leftPos + 81, this.topPos + 28, 176, 0, heat, 5);

        int burn = this.be.burnTime * 70 / Math.max(this.be.maxBurnTime, 1);
        guiGraphics.blit(TEXTURE, this.leftPos + 81, this.topPos + 37, 176, 5, burn, 5);

        if(this.be.wasOn) {
            guiGraphics.blit(TEXTURE, this.leftPos + 25, this.topPos + 26, 176, 10, 18, 18);
        }
    }
}
