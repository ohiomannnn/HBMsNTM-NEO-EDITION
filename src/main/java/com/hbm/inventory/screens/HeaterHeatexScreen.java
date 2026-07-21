package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.heater.HeaterHeatexBlockEntity;
import com.hbm.inventory.menus.HeaterHeatexMenu;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.math.NumberUtils;

public class HeaterHeatexScreen extends InfoScreen<HeaterHeatexMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/machine/gui_heatex.png");

    private final HeaterHeatexBlockEntity be;
    private EditBox fieldCycles;
    private EditBox fieldDelay;

    public HeaterHeatexScreen(HeaterHeatexMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 204;
    }

    @Override
    protected void init() {
        super.init();

        this.fieldCycles = new EditBox(this.font, this.leftPos + 73, this.topPos + 31, 30, 10, Component.empty());
        this.fieldCycles.setTextColor(0x00ff00);
        this.fieldCycles.setTextColorUneditable(0x00ff00);
        this.fieldCycles.setBordered(false);
        this.fieldCycles.setMaxLength(5);
        this.fieldCycles.setValue(String.valueOf(this.be.amountToCool));
        this.addRenderableWidget(this.fieldCycles);

        this.fieldDelay = new EditBox(this.font, this.leftPos + 73, this.topPos + 49, 30, 10, Component.empty());
        this.fieldDelay.setTextColor(0x00ff00);
        this.fieldDelay.setTextColorUneditable(0x00ff00);
        this.fieldDelay.setBordered(false);
        this.fieldDelay.setMaxLength(5);
        this.fieldDelay.setValue(String.valueOf(this.be.tickDelay));
        this.addRenderableWidget(this.fieldDelay);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.be.tanks[0].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 44, this.topPos + 36, 16, 52);
        this.be.tanks[1].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 116, this.topPos + 36, 16, 52);

        if(this.isHovered(70, 26, 36, 18, mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font, Component.translatable("gui.heatex.amount"), mouseX, mouseY);
        }
        if(this.isHovered(70, 44, 36, 18, mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font, Component.translatable("gui.heatex.cycle"), mouseX, mouseY);
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

        this.be.tanks[0].renderTank(this.leftPos + 44, this.topPos + 88, 0, 16, 52);
        this.be.tanks[1].renderTank(this.leftPos + 116, this.topPos + 88, 0, 16, 52);

        this.fieldCycles.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.fieldDelay.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = false;
        handled |= this.fieldCycles.mouseClicked(mouseX, mouseY, button);
        handled |= this.fieldDelay.mouseClicked(mouseX, mouseY, button);
        if(handled) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(this.fieldCycles.keyPressed(keyCode, scanCode, modifiers)) return true;
        if(this.fieldDelay.keyPressed(keyCode, scanCode, modifiers)) return true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if(this.fieldCycles.charTyped(codePoint, modifiers)) {
            if(NumberUtils.isNumber(this.fieldCycles.getValue())) {
                int value = Math.max(NumberUtils.toInt(this.fieldCycles.getValue()), 1);
                CompoundTag tag = new CompoundTag();
                tag.putInt("toCool", value);
                PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
            }
            return true;
        }

        if(this.fieldDelay.charTyped(codePoint, modifiers)) {
            if(NumberUtils.isNumber(this.fieldDelay.getValue())) {
                int value = Math.max(NumberUtils.toInt(this.fieldDelay.getValue()), 1);
                CompoundTag tag = new CompoundTag();
                tag.putInt("delay", value);
                PacketDistributor.sendToServer(new CompoundTagControl(tag, this.be.getBlockPos()));
            }
            return true;
        }

        return super.charTyped(codePoint, modifiers);
    }
}
