package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.SoyuzLauncherBlockEntity;
import com.hbm.inventory.menus.SoyuzLauncherMenu;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class SoyuzLauncherScreen extends InfoScreen<SoyuzLauncherMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/gui_soyuz.png");

    private final SoyuzLauncherBlockEntity be;

    public SoyuzLauncherScreen(SoyuzLauncherMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;

        this.imageWidth = 194;
        this.imageHeight = 244;
    }

    @Override
    protected void init() {
        super.init();

        this.titleLabelX = this.imageWidth / 2 - this.font.width(this.title) / 2;
        this.titleLabelY = 4;
        this.inventoryLabelX = 17;
        this.inventoryLabelY = this.imageHeight - 96 + 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        be.tanks[0].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 152, this.topPos + 44, 16, 52);
        be.tanks[1].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 170, this.topPos + 44, 16, 52);
        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 134, this.topPos + 44, 16, 52, be.power, be.maxPower);

        List<Component> descText = new ArrayList<>(); for(String s : I18nUtil.resolveKeyArray("container.soyuz_launcher.desc")) descText.add(Component.literal(s));
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos - 16, this.topPos + 53, 16, 16, this.leftPos - 8, this.topPos + 53 + 16, descText);

        List<Component> cargoText = new ArrayList<>(); for(String s : I18nUtil.resolveKeyArray("container.soyuz_launcher.cargo")) cargoText.add(Component.literal(s));
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 79, this.topPos + 52, 18, 18, mouseX, mouseY, cargoText);
        List<Component> satelliteText = new ArrayList<>(); for(String s : I18nUtil.resolveKeyArray("container.soyuz_launcher.satellite")) satelliteText.add(Component.literal(s));
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 97, this.topPos + 52, 18, 18, mouseX, mouseY, satelliteText);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        CompoundTag tag = new CompoundTag();

        if(this.isHovered(mouseX, mouseY, 97, 52, 18, 18)) { this.click(); tag.putByte("mode", (byte) 0); }
        if(this.isHovered(mouseX, mouseY, 79, 52, 18, 18)) { this.click(); tag.putByte("mode", (byte) 1); }
        if(this.isHovered(mouseX, mouseY, 88, 97, 18, 18)) { this.click(); tag.putBoolean("start", true); }

        if(!tag.isEmpty()) PacketDistributor.sendToServer(new CompoundTagControl(tag, be.getBlockPos()));

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        int i = (int) be.getPowerScaled(52);
        guiGraphics.blit(TEXTURE, this.leftPos + 134, this.topPos + 96 - i, 194, 52 - i, 16, i);

        guiGraphics.blit(TEXTURE, this.leftPos + 97, this.topPos + 79, 210 + (be.hasRocket() ? 18 : 0), 8, 18, 18);
        int j = be.designator();

        if(j > 0) guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 79, 210 + (j - 1) * 18, 8, 18, 18);

        int k = be.mode;
        guiGraphics.blit(TEXTURE, this.leftPos + 97 - k * 18, this.topPos + 52, 228 - k * 18, 26, 18, 18);

        int l = be.orbital();

        if(l > 0) guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 25, 210 + (l - 1) * 18, 8, 18, 18);

        int m = be.satellite();

        if(m > 0) guiGraphics.blit(TEXTURE, this.leftPos + 97, this.topPos + 25, 210 + (m - 1) * 18, 8, 18, 18);

        if(be.starting) guiGraphics.blit(TEXTURE, this.leftPos + 88, this.topPos + 97, 210, 44, 18, 18);

        if(be.hasFuel()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 157, this.topPos + 31, 210, 0, 6, 8);
        } else {
            guiGraphics.blit(TEXTURE, this.leftPos + 157, this.topPos + 31, 216, 0, 6, 8);
        }

        if(be.hasOxy()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 175, this.topPos + 31, 210, 0, 6, 8);
        } else {
            guiGraphics.blit(TEXTURE, this.leftPos + 175, this.topPos + 31, 216, 0, 6, 8);
        }

        if(be.hasPower()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 139, this.topPos + 31, 210, 0, 6, 8);
        } else {
            guiGraphics.blit(TEXTURE, this.leftPos + 139, this.topPos + 31, 216, 0, 6, 8);
        }

        be.tanks[0].renderTank(this.leftPos + 152, this.topPos + 96, 1F, 16, 52);
        be.tanks[1].renderTank(this.leftPos + 170, this.topPos + 96, 1F, 16, 52);

        this.drawInfoPanel(guiGraphics, this.leftPos - 16, this.topPos + 53, 2);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);

        String secs = "" + be.countdown / 20;
        String cents = "" + (be.countdown % 20) * 5;

        if(secs.length() == 1) secs = "0" + secs;
        if(cents.length() == 1) cents += "0";

        float scale = 1F;
        guiGraphics.pose().scale(scale, scale, 1F);
        guiGraphics.drawString(this.font, secs + ":" + cents, (int)(85F / scale), (int)(121F / scale), 0xff0000);
        guiGraphics.pose().scale(1 / scale, 1 / scale, 1F);
    }
}
