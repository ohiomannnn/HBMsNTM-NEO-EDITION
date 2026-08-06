package com.hbm.inventory.screens;

import com.hbm.blockentity.network.RadioTorchBaseBlockEntity;
import com.hbm.blockentity.network.RadioTorchSenderBlockEntity;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class RadioTorchScreen extends Screen {

    public static final int MAX_CHAN_LENGTH = 15;
    protected static final ResourceLocation TEXTURE_SENDER = NuclearTechMod.withDefaultNamespace("textures/gui/machine/gui_rtty_sender.png");
    protected static final ResourceLocation TEXTURE_RECEIVER = NuclearTechMod.withDefaultNamespace("textures/gui/machine/gui_rtty_receiver.png");
    protected ResourceLocation texture;
    protected final RadioTorchBaseBlockEntity radio;
    protected int imageWidth = 256;
    protected int imageHeight = 204;
    protected int leftPos;
    protected int topPos;

    protected EditBox frequency;
    protected EditBox[] remap;

    public RadioTorchScreen(RadioTorchBaseBlockEntity radio) {
        super(radio instanceof RadioTorchSenderBlockEntity ? Component.translatable("container.rtty_sender") : Component.translatable("container.rtty_receiver"));
        this.radio = radio;

        if(radio instanceof RadioTorchSenderBlockEntity) {
            this.texture = TEXTURE_SENDER;
        } else {
            this.texture = TEXTURE_RECEIVER;
        }
    }

    @Override public boolean isPauseScreen() { return false; }

    @Override
    protected void init() {

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        int oX = 4;
        int oY = 4;
        int in = radio instanceof RadioTorchSenderBlockEntity ? 18 : 0;

        this.frequency = new EditBox(this.font, this.leftPos + 66, this.topPos + 21, 48, 12, Component.empty());
        this.frequency.setTextColor(0x00ff00);
        this.frequency.setTextColorUneditable(0x00ff00);
        this.frequency.setBordered(false);
        this.frequency.setMaxLength(MAX_CHAN_LENGTH);
        this.frequency.setValue(radio.channel == null ? "" : radio.channel);
        this.addRenderableWidget(this.frequency);

        this.remap = new EditBox[16];

        for(int i = 0; i < 16; i++) {
            this.remap[i] = new EditBox(this.font, this.leftPos + 7 + (130 * (i / 8)) + oX + in, this.topPos + 53 + (18 * (i % 8)) + oY, 90 - oX * 2, 14, Component.empty());
            this.remap[i].setTextColor(0x00ff00);
            this.remap[i].setTextColorUneditable(0x00ff00);
            this.remap[i].setBordered(false);
            this.remap[i].setMaxLength(32);
            this.remap[i].setValue(radio.mapping[i] == null ? "" : radio.mapping[i]);
            this.addRenderableWidget(this.remap[i]);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(guiGraphics);
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        this.renderLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        if(radio.customMap) {
            guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
            guiGraphics.blit(texture, this.leftPos + 137, this.topPos + 17, 0, 204, 18, 18);
            if(radio.polling) guiGraphics.blit(texture, this.leftPos + 173, this.topPos + 17, 0, 222, 18, 18);
            for(int j = 0; j < 16; j++) {
                this.remap[j].render(guiGraphics, mouseX, mouseY, partialTick);
            }
        } else {
            guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, 35);
            guiGraphics.blit(texture, this.leftPos, this.topPos + 35, 0, 197, this.imageWidth, 7);
            if(radio.polling) guiGraphics.blit(texture, this.leftPos + 173, this.topPos + 17, 0, 222, 18, 18);
        }

        this.frequency.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.leftPos + this.imageWidth / 2 - this.font.width(this.title) / 2, this.topPos + 6, 4210752, false);

        if(ScreenUtils.isHovered(this.leftPos, this.topPos, mouseX, mouseY, 137, 17, 18, 18)) {
            guiGraphics.renderTooltip(this.font, Component.translatable(radio.customMap ? "container.rtty.custom" : "container.rtty.pass"), mouseX, mouseY);
        }
        if(ScreenUtils.isHovered(this.leftPos, this.topPos, mouseX, mouseY, 173, 17, 18, 18)) {
            guiGraphics.renderTooltip(this.font, Component.translatable(radio.polling ? "container.rtty.polling" : "container.rtty.change"), mouseX, mouseY);
        }
        if(ScreenUtils.isHovered(this.leftPos, this.topPos, mouseX, mouseY, 209, 17, 18, 18)) {
            guiGraphics.renderTooltip(this.font, Component.translatable("container.rtty.save"), mouseX, mouseY);
        }
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        this.frequency.mouseClicked(mouseX, mouseY, button);
        if(radio.customMap) for(int j = 0; j < 16; j++) this.remap[j].mouseClicked(mouseX, mouseY, button);

        if(ScreenUtils.isHovered(this.leftPos, this.topPos, mouseX, mouseY, 137, 17, 18, 18)) {
            ScreenUtils.click(this.minecraft.getSoundManager());
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("m", !radio.customMap);
            PacketDistributor.sendToServer(new CompoundTagControl(tag, radio.getBlockPos()));
        }

        if(ScreenUtils.isHovered(this.leftPos, this.topPos, mouseX, mouseY, 173, 17, 18, 18)) {
            ScreenUtils.click(this.minecraft.getSoundManager());
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("p", !radio.polling);
            PacketDistributor.sendToServer(new CompoundTagControl(tag, radio.getBlockPos()));
        }

        if(ScreenUtils.isHovered(this.leftPos, this.topPos, mouseX, mouseY, 209, 17, 18, 18)) {
            ScreenUtils.click(this.minecraft.getSoundManager());
            CompoundTag tag = new CompoundTag();
            tag.putString("c", this.frequency.getValue());
            for(int j = 0; j < 16; j++) tag.putString("m" + j, this.remap[j].getValue());
            PacketDistributor.sendToServer(new CompoundTagControl(tag, radio.getBlockPos()));
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if(this.frequency.charTyped(codePoint, modifiers)) return true;
        if(radio.customMap) for(int j = 0; j < 16; j++) if(this.remap[j].charTyped(codePoint, modifiers)) return true;
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if(this.frequency.keyPressed(keyCode, scanCode, modifiers)) return true;
        if(radio.customMap) for(int j = 0; j < 16; j++) if(this.remap[j].keyPressed(keyCode, scanCode, modifiers)) return true;

        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);

        if(keyCode == GLFW.GLFW_KEY_ESCAPE || this.minecraft.options.keyInventory.isActiveAndMatches(key)) {
            this.onClose();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
