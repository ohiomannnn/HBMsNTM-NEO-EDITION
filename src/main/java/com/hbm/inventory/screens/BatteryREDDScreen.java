package com.hbm.inventory.screens;

import com.hbm.HBMsNTM;
import com.hbm.blockentity.machine.storage.BatteryREDDBlockEntity;
import com.hbm.inventory.menus.BatteryREDDMenu;
import com.hbm.network.toserver.CompoundTagControl;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BatteryREDDScreen extends InfoScreen<BatteryREDDMenu> {

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/gui/storage/gui_battery_redd.png");

    public BatteryREDDBlockEntity be;

    public BatteryREDDScreen(BatteryREDDMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 181;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        String lang = switch (be.priority) {
            case LOW -> "low";
            case HIGH -> "high";
            default -> "normal";
        };

        List<Component> priority = new ArrayList<>();
        priority.add(Component.translatable("battery.priority." + lang).withStyle(ChatFormatting.GRAY));
        priority.add(Component.translatable("battery.priority.recommended").withStyle(ChatFormatting.GRAY));
        String[] desc = I18nUtil.resolveKeyArray("battery.priority." + lang + ".desc");
        for (String s : desc) priority.add(Component.literal(s).withStyle(ChatFormatting.GRAY));

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, leftPos + 152, topPos + 35, 16, 16, mouseX, mouseY, priority);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {

        CompoundTag tag = new CompoundTag();

        if (this.checkClick((int) x, (int) y, 133, 16, 18, 18)) { this.click(); tag.putBoolean("low", true); }
        if (this.checkClick((int) x, (int) y, 133, 52, 18, 18)) { this.click(); tag.putBoolean("high", true); }
        if (this.checkClick((int) x, (int) y, 152, 35, 16, 16)) { this.click(); tag.putBoolean("priority", true); }

        if (!tag.isEmpty()) PacketDistributor.sendToServer(new CompoundTagControl(tag, be.getBlockPos()));

        return super.mouseClicked(x, y, button);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int partialTicks) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        guiGraphics.blit(TEXTURE, this.leftPos + 133, this.topPos + 16, 176, 52 + be.redLow * 18, 18, 18);
        guiGraphics.blit(TEXTURE, this.leftPos + 133, this.topPos + 52, 176, 52 + be.redHigh * 18, 18, 18);
        guiGraphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 35, 194, 52 + be.priority.ordinal() * 16 - 16, 16, 16);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.imageHeight - 96 + 2, 4210752, false);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.5F, 0.5F, 1.0F);

        String label = String.format(Locale.US, "%,d", be.power) + " HE";
        guiGraphics.drawString(this.font, label, 242 - this.font.width(label), 45, 0x00ff00);

        String deltaText = String.format(Locale.US, "%,d", be.delta) + " HE/s";

        int comp = be.delta.compareTo(BigInteger.ZERO);
        if (comp > 0) deltaText = ChatFormatting.GREEN + "+" + deltaText;
        else if (comp < 0) deltaText = ChatFormatting.RED + deltaText;
        else deltaText = ChatFormatting.YELLOW + "+" + deltaText;

        guiGraphics.drawString(this.font, deltaText, 242 - this.font.width(deltaText), 65, 0x00ff00);

        guiGraphics.pose().popPose();
    }
}
