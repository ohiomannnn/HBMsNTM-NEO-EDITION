package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineWoodBurnerBlockEntity;
import com.hbm.inventory.menus.MachineWoodBurnerMenu;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class MachineWoodBurnerScreen extends InfoScreen<MachineWoodBurnerMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/generators/gui_wood_burner_alt.png");

    private final MachineWoodBurnerBlockEntity be;

    public MachineWoodBurnerScreen(MachineWoodBurnerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 186;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 143, this.topPos + 18, 16, 34, this.be.power, MachineWoodBurnerBlockEntity.MAX_POWER);

        if(this.menu.getCarried().isEmpty()) {
            if(this.isHovering(26, 18, 16, 16, mouseX, mouseY) && !this.menu.slots.get(0).hasItem()) {
                List<Component> lines = MachineWoodBurnerBlockEntity.BURN_MODULE.getDesc().stream().<Component>map(Component::literal).toList();
                if(!lines.isEmpty()) guiGraphics.renderComponentTooltip(this.font, lines, mouseX, mouseY);
            }
        }

        if(this.be.liquidBurn) {
            this.be.tank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 80, this.topPos + 18, 16, 52);
        } else {
            this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 16, this.topPos + 17, 8, 54, mouseX, mouseY,
                    Component.literal(this.be.burnTime / 20 + "s"));
        }

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 53, this.topPos + 17, 16, 15, mouseX, mouseY,
                Component.literal(this.be.isOn ? "ON" : "OFF").withStyle(this.be.isOn ? ChatFormatting.GREEN : ChatFormatting.RED));
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 70 - this.font.width(this.title) / 2, 6, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        if(this.be.liquidBurn) {
            guiGraphics.blit(TEXTURE, this.leftPos + 16, this.topPos + 17, 176, 52, 60, 54, 256, 256);
            guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 17, 176, 106, 36, 54, 256, 256);
        }
        if(this.be.isOn) {
            guiGraphics.blit(TEXTURE, this.leftPos + 53, this.topPos + 17, 196, 0, 16, 15, 256, 256);
        }

        int power = this.be.getPowerScaled(34);
        guiGraphics.blit(TEXTURE, this.leftPos + 143, this.topPos + 52 - power, 176, 52 - power, 16, power, 256, 256);

        if(!this.be.liquidBurn) {
            int burn = this.be.getBurnScaled(52);
            if(burn > 0) {
                guiGraphics.blit(TEXTURE, this.leftPos + 17, this.topPos + 70 - burn, 192, 52 - burn, 4, burn, 256, 256);
            }
        } else {
            this.be.tank.renderTank(this.leftPos + 80, this.topPos + 70, 0, 16, 52);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        CompoundTag tag = new CompoundTag();
        if(button == 0 && this.isHovered(mouseX, mouseY, 53, 17, 16, 15)) {
            this.click();
            tag.putBoolean("toggle", true);
        } else if(button == 0 && this.isHovered(mouseX, mouseY, 46, 37, 30, 14)) {
            this.click();
            tag.putBoolean("switch", true);
        }

        if(!tag.isEmpty()) {
            PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
