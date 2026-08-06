package com.hbm.inventory.screens;

import com.hbm.items.tools.SatInterfaceItem;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagItemControl;
import com.hbm.util.TagsUtil;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.glfw.GLFW;

public class SatCoordScreen extends Screen {

    protected static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/satellites/gui_sat_coord.png");
    protected int imageWidth = 176;
    protected int imageHeight = 126;
    protected int leftPos;
    protected int topPos;

    private EditBox xField;
    private EditBox yField;
    private EditBox zField;

    protected ItemStack[] device = new ItemStack[2];

    public SatCoordScreen(Player player) {
        super(Component.translatable("container.sat_coord"));
        
        this.device[0] = player.getMainHandItem();
        this.device[1] = player.getOffhandItem();
    }

    @Override public boolean isPauseScreen() { return false; }

    @Override
    protected void init() {

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.xField = new EditBox(this.font, this.leftPos + 66, this.topPos + 21, 48, 12, Component.empty());
        this.xField.setTextColor(-1);
        this.xField.setTextColorUneditable(-1);
        this.xField.setBordered(false);
        this.xField.setMaxLength(7);
        this.yField = new EditBox(this.font, this.leftPos + 66, this.topPos + 56, 48, 12, Component.empty());
        this.yField.setTextColor(-1);
        this.yField.setTextColorUneditable(-1);
        this.yField.setBordered(false);
        this.yField.setMaxLength(7);
        this.zField = new EditBox(this.font, this.leftPos + 66, this.topPos + 92, 48, 12, Component.empty());
        this.zField.setTextColor(-1);
        this.zField.setTextColorUneditable(-1);
        this.zField.setBordered(false);
        this.zField.setMaxLength(7);

        this.addRenderableWidget(this.xField);
        this.addRenderableWidget(this.yField);
        this.addRenderableWidget(this.zField);
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if(!TagsUtil.getCustomData(device[0]).getBoolean(SatInterfaceItem.KEY_NBT_CONNECTED) &&
                !TagsUtil.getCustomData(device[1]).getBoolean(SatInterfaceItem.KEY_NBT_CONNECTED)) return super.mouseClicked(mouseX, mouseY, button);

        if(ScreenUtils.isHovered(this.leftPos, this.topPos, mouseX, mouseY, 133, 52, 18, 18)) {

            if(NumberUtils.isNumber(xField.getValue()) && NumberUtils.isNumber(zField.getValue())) {

                ScreenUtils.click(this.minecraft.getSoundManager());
                CompoundTag tag = new CompoundTag();
                tag.putInt("x", (int) Double.parseDouble(xField.getValue()));
                tag.putInt("z", (int) Double.parseDouble(zField.getValue()));
                if(NumberUtils.isNumber(yField.getValue())) tag.putInt("y", (int) Double.parseDouble(yField.getValue()));
                PacketDistributor.sendToServer(new CompoundTagItemControl(tag));

                this.onClose();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(guiGraphics);
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        for(Renderable renderable : this.renderables) renderable.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(xField.isFocused()) guiGraphics.blit(TEXTURE, this.leftPos + 61, this.topPos + 16, 0, 126, 54, 18);
        if(yField.isFocused()) guiGraphics.blit(TEXTURE, this.leftPos + 61, this.topPos + 52, 0, 126, 54, 18);
        if(zField.isFocused()) guiGraphics.blit(TEXTURE, this.leftPos + 61, this.topPos + 88, 0, 126, 54, 18);
        
        if(TagsUtil.getCustomData(device[0]).getBoolean(SatInterfaceItem.KEY_NBT_CONNECTED) ||
                TagsUtil.getCustomData(device[1]).getBoolean(SatInterfaceItem.KEY_NBT_CONNECTED)) {

            guiGraphics.blit(TEXTURE, this.leftPos + 120, this.topPos + 17, 194, 0, 7, 7);
            guiGraphics.blit(TEXTURE, this.leftPos + 120, this.topPos + 25, 194, 0, 7, 7);
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if(this.xField.charTyped(codePoint, modifiers)) return true;
        if(this.yField.charTyped(codePoint, modifiers)) return true;
        if(this.zField.charTyped(codePoint, modifiers)) return true;
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if(this.xField.keyPressed(keyCode, scanCode, modifiers)) return true;
        if(this.yField.keyPressed(keyCode, scanCode, modifiers)) return true;
        if(this.zField.keyPressed(keyCode, scanCode, modifiers)) return true;

        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);

        if(keyCode == GLFW.GLFW_KEY_ESCAPE || this.minecraft.options.keyInventory.isActiveAndMatches(key)) {
            this.onClose();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
