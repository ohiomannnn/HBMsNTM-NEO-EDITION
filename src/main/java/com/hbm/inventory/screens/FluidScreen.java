package com.hbm.inventory.screens;

import com.hbm.HBMsNTM;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ModItems;
import com.hbm.items.machine.FluidIDMultiItem;
import com.hbm.network.toserver.CompoundTagItemControl;
import com.hbm.util.InventoryUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FluidScreen extends Screen {

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/gui/machine/gui_fluid.png");
    protected int xSize = 176;
    protected int ySize = 54;
    protected int guiLeft;
    protected int guiTop;
    private EditBox search;

    private final Player player;
    private FluidType primary = Fluids.NONE;
    private FluidType secondary = Fluids.NONE;
    private FluidType[] searchArray = new FluidType[9];

    public FluidScreen(Player player) {
        super(Component.empty());
        this.player = player;
    }

    @Override
    public void tick() {
        boolean close = InventoryUtil.getItemSteamFromBothHands(player).noneMatch(stack -> stack.is(ModItems.FLUID_IDENTIFIER_MULTI.get()));
        if (close) this.onClose();
    }

    @Override
    protected void init() {
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        search = new EditBox(this.font,  guiLeft + 46, guiTop + 11, 210, 13, Component.empty());
        search.setTextColor(-1);
        search.setBordered(false);
        search.setFocused(true);

        List<ItemStack> stacks = InventoryUtil.getItemSteamFromBothHands(player).filter(stack -> stack.getItem() instanceof FluidIDMultiItem).toList();
        for (ItemStack stack : stacks) {
            this.primary = FluidIDMultiItem.getType(stack, true);
            this.secondary = FluidIDMultiItem.getType(stack, false);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        this.renderSearchBox(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(TEXTURE, guiLeft, guiTop, 0, 0, xSize, ySize);

        if (this.search.isFocused()) guiGraphics.blit(TEXTURE, guiLeft + 43, guiTop + 7, 166, 54, 90, 18);

        for (int k = 0; k < this.searchArray.length; k++) {
            FluidType type = this.searchArray[k];

            if (type == null) return;

            Color color = new Color(type.getColor());
            guiGraphics.setColor(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.0F);
            guiGraphics.blit(TEXTURE, guiLeft + 12 + k * 18, guiTop + 31, 12 + k * 18, 56, 8, 14);
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

            if (type == this.primary && type == this.secondary) {
                guiGraphics.blit(TEXTURE, guiLeft + 7 + k * 18, guiTop + 29, 176, 36, 18, 18);
            } else if (type == this.primary) {
                guiGraphics.blit(TEXTURE, guiLeft + 7 + k * 18, guiTop + 29, 176, 0, 18, 18);
            } else if (type == this.secondary) {
                guiGraphics.blit(TEXTURE, guiLeft + 7 + k * 18, guiTop + 29, 176, 18, 18, 18);
            }
        }
    }

    public void renderSearchBox(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.search.render(guiGraphics, mouseX, mouseY, partialTick);

        for (int k = 0; k < this.searchArray.length; k++) {

            if (this.searchArray[k] == null) return;

            if (guiLeft + 7 + k * 18 <= mouseX && guiLeft + 7 + k * 18 + 18 > mouseX && guiTop + 29 < mouseY && guiTop + 29 + 18 >= mouseY) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(this.searchArray[k].getName());
                this.searchArray[k].addInfo(tooltip);
                guiGraphics.renderComponentTooltip(font, tooltip, mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int k = 0; k < this.searchArray.length; k++) {

            if (this.searchArray[k] == null) return super.mouseClicked(mouseX, mouseY, button);;

            if (guiLeft + 7 + k * 18 <= mouseX && guiLeft + 7 + k * 18 + 18 > mouseX && guiTop + 29 < mouseY && guiTop + 29 + 18 >= mouseY) {
                if (button == 0) {
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    this.primary = this.searchArray[k];
                    CompoundTag tag = new CompoundTag();
                    tag.putInt("Primary", this.primary.getID());
                    PacketDistributor.sendToServer(new CompoundTagItemControl(tag));
                } else if (button == 1) {
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    this.secondary = this.searchArray[k];
                    CompoundTag tag = new CompoundTag();
                    tag.putInt("Secondary", this.secondary.getID());
                    PacketDistributor.sendToServer(new CompoundTagItemControl(tag));
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.search.keyPressed(keyCode, scanCode, modifiers)) {
            updateSearch();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (this.search.charTyped(codePoint, modifiers)) {
            updateSearch();
        }
        return super.charTyped(codePoint, modifiers);
    }

    private void updateSearch() {
        this.searchArray = new FluidType[9];

        int next = 0;
        String subs = this.search.getValue().toLowerCase(Locale.US);

        for (FluidType type : Fluids.getInNiceOrder()) {
            String name = type.getUnlocalizedName().toLowerCase(Locale.US);

            if (name.contains(subs) && !type.hasNoID()) {
                this.searchArray[next] = type;
                next++;

                if (next >= 9) return;
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
