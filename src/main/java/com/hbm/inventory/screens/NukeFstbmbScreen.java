package com.hbm.inventory.screens;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.bomb.NukeBalefireBlockEntity;
import com.hbm.interfaces.Placeholder;
import com.hbm.inventory.menus.NukeFstbmbMenu;
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

@Placeholder(reason = "...")
public class NukeFstbmbScreen extends InfoScreen<NukeFstbmbMenu> {

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/gui/weapon/fstbmb_schematic.png");

    private EditBox timer;
    private final NukeBalefireBlockEntity be;

    public NukeFstbmbScreen(NukeFstbmbMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    @Override
    protected void init() {
        super.init();

        this.timer = new EditBox(this.font, this.leftPos + 94, this.topPos + 40, 29, 12, Component.empty());
        this.timer.setTextColor(0xFF0000);
        this.timer.setTextColorUneditable(0x800000);
        this.timer.setBordered(false);
        this.timer.setMaxLength(3);
        this.timer.setValue(String.valueOf(be.timer / 20));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        CompoundTag tag = new CompoundTag();
        if (this.checkClick((int) mouseY, (int) mouseY, 142 , 35, 18, 18)) { tag.putInt("Meta", 0); }
        if (!tag.isEmpty()) PacketDistributor.sendToServer(new CompoundTagControl(tag, be.getBlockPos()));

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.timer.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (this.timer.charTyped(codePoint, modifiers)) {
            if (NumberUtils.isNumber(timer.getValue())) {
                int j = Mth.clamp((int) Double.parseDouble(timer.getValue()), 1, 999);
                CompoundTag tag = new CompoundTag();
                tag.putInt("Meta", 1);
                tag.putInt("Value", j);
                PacketDistributor.sendToServer(new CompoundTagControl(tag, be.getBlockPos()));
            }
        }
        return super.charTyped(codePoint, modifiers);
    }


    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }
}
