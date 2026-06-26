package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.SoyuzLauncherBlockEntity;
import com.hbm.inventory.menus.SoyuzLauncherMenu;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class SoyuzLauncherScreen extends InfoScreen<SoyuzLauncherMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/gui_soyuz.png");

    private final SoyuzLauncherBlockEntity be;

    public SoyuzLauncherScreen(SoyuzLauncherMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    @Override
    protected void init() {
        super.init();

        this.titleLabelX = this.imageWidth / 2 - this.font.width(this.title) / 2;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 96 + 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        be.tanks[0].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 8, this.topPos + 36, 16, 52);
        be.tanks[1].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 26, this.topPos + 36, 16, 52);
        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 49, this.topPos + 72, 6, 34, be.power, be.maxPower);

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 43, this.topPos + 17, 18, 18, mouseX, mouseY, Component.translatable("container.soyuz_launcher.soyuz_here"));
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 43, this.topPos + 35, 18, 18, mouseX, mouseY, Component.translatable("container.soyuz_launcher.designator_here"));
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 133, this.topPos + 17, 18, 18, mouseX, mouseY, Component.translatable("container.soyuz_launcher.payload_here"));
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 133, this.topPos + 35, 18, 18, mouseX, mouseY, Component.translatable("container.soyuz_launcher.module_here"));
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 88, this.topPos + 17, 18, 18, mouseX, mouseY, Component.translatable("container.soyuz_launcher.sat_mode"));
        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, this.leftPos + 88, this.topPos + 35, 18, 18, mouseX, mouseY, Component.translatable("container.soyuz_launcher.cargo_mode"));

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        CompoundTag tag = new CompoundTag();

        if(this.isHovered(mouseX, mouseY, 88, 17, 18, 18)) { this.click(); tag.putByte("mode", (byte) 0); }
        if(this.isHovered(mouseX, mouseY, 88, 35, 18, 18)) { this.click(); tag.putByte("mode", (byte) 1); }
        if(this.isHovered(mouseX, mouseY, 151, 17, 18, 18)) { this.click(); tag.putBoolean("start", true); }

        if(!tag.isEmpty()) PacketDistributor.sendToServer(new CompoundTagControl(tag, be.getBlockPos()));

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        int i = (int)be.getPowerScaled(34);
        guiGraphics.blit(TEXTURE, this.leftPos + 49, this.topPos + 106 - i, 194, 52 - i, 6, i);

        guiGraphics.blit(TEXTURE, this.leftPos + 61, this.topPos + 17, 176 + (be.hasRocket() ? 18 : 0), 0, 18, 18);
        int j = be.designator();

        if(j > 0) guiGraphics.blit(TEXTURE, this.leftPos + 61, this.topPos + 35, 176 + (j - 1) * 18, 0, 18, 18);

        int k = be.mode;
        guiGraphics.blit(TEXTURE, this.leftPos + 88, this.topPos + 17 + k * 18, 176, 18 + k * 18, 18, 18);

        int l = be.orbital();

        if(l > 0) guiGraphics.blit(TEXTURE, this.leftPos + 115, this.topPos + 35, 176 + (l - 1) * 18, 0, 18, 18);

        int m = be.satellite();

        if(m > 0) guiGraphics.blit(TEXTURE, this.leftPos + 115, this.topPos + 17, 176 + (m - 1) * 18, 0, 18, 18);

        if(be.starting) guiGraphics.blit(TEXTURE, this.leftPos + 151, this.topPos + 17, 176, 54, 18, 18);

        if(be.hasFuel()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 13, this.topPos + 23, 212, 0, 6, 8);
        } else {
            guiGraphics.blit(TEXTURE, this.leftPos + 13, this.topPos + 23, 218, 0, 6, 8);
        }

        if(be.hasOxy()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 31, this.topPos + 23, 212, 0, 6, 8);
        } else {
            guiGraphics.blit(TEXTURE, this.leftPos + 31, this.topPos + 23, 218, 0, 6, 8);
        }

        if(be.hasPower()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 49, this.topPos + 59, 212, 0, 6, 8);
        } else {
            guiGraphics.blit(TEXTURE, this.leftPos + 49, this.topPos + 59, 218, 0, 6, 8);
        }

        be.tanks[0].renderTank(this.leftPos + 8, this.topPos + 88, 1F, 16, 52);
        be.tanks[1].renderTank(this.leftPos + 26, this.topPos + 88, 1F, 16, 52);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);

        String secs = "" + be.countdown / 20;
        String cents = "" + (be.countdown % 20) * 5;

        if(secs.length() == 1) secs = "0" + secs;
        if(cents.length() == 1) cents += "0";

        float scale = 0.5F;
        guiGraphics.pose().scale(scale, scale, 1F);
        guiGraphics.drawString(this.font, secs + ":" + cents, (int)(153.5F / scale), (int)(37.5F / scale), 0xff0000);
        guiGraphics.pose().scale(1 / scale, 1 / scale, 1F);
    }
}
