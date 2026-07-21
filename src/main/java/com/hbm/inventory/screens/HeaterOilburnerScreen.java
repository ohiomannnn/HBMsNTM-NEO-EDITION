package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.heater.HeaterOilburnerBlockEntity;
import com.hbm.inventory.menus.HeaterOilburnerMenu;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import com.hbm.inventory.fluid.trait.FT_Flammable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Locale;

public class HeaterOilburnerScreen extends InfoScreen<HeaterOilburnerMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/machine/gui_oilburner.png");

    private final HeaterOilburnerBlockEntity be;

    public HeaterOilburnerScreen(HeaterOilburnerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 203;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.be.tank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 44, this.topPos + 17, 16, 52);

        this.drawCustomInfoStat(
                guiGraphics,
                mouseX,
                mouseY,
                this.leftPos + 116,
                this.topPos + 17,
                16,
                52,
                mouseX,
                mouseY,
                Component.literal(String.format("%,d", Math.min(this.be.heatEnergy, HeaterOilburnerBlockEntity.maxHeatEnergy)) + " / " + String.format("%,d", HeaterOilburnerBlockEntity.maxHeatEnergy) + " TU")
        );

        if(this.be.tank.getTankType().hasTrait(FT_Flammable.class)) {
            FT_Flammable trait = this.be.tank.getTankType().getTrait(FT_Flammable.class);
            int heat = (int) (trait.getHeatEnergy() / 1000D) * this.be.setting;
            this.drawCustomInfoStat(
                    guiGraphics,
                    mouseX,
                    mouseY,
                    this.leftPos + 79,
                    this.topPos + 34,
                    18,
                    18,
                    mouseX,
                    mouseY,
                    Component.literal(this.be.setting + " mB/t"),
                    Component.literal(String.format(Locale.US, "%,d", heat) + " TU/t")
            );
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int heat = this.be.heatEnergy * 52 / Math.max(HeaterOilburnerBlockEntity.maxHeatEnergy, 1);
        guiGraphics.blit(TEXTURE, this.leftPos + 116, this.topPos + 69 - heat, 194, 52 - heat, 16, heat);

        if(this.be.isOn) {
            guiGraphics.blit(TEXTURE, this.leftPos + 70, this.topPos + 54, 210, 0, 35, 14);

            if(this.be.tank.getFill() > 0 && this.be.tank.getTankType().hasTrait(FT_Flammable.class)) {
                guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 34, 176, 0, 18, 18);
            }
        }

        this.be.tank.renderTank(this.leftPos + 44, this.topPos + 69, 0, 16, 52);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.isHovered(mouseX, mouseY, 80, 54, 16, 14)) {
            this.click();
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("toggle", true);
            PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
