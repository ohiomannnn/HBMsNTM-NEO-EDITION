package com.hbm.inventory.screens;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.HBMsNTM;
import com.hbm.inventory.menus.BatterySocketMenu;
import com.hbm.network.toserver.CompoundTagControl;
import com.hbm.util.BobMathUtil;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class BatterySocketScreen extends InfoScreen<BatterySocketMenu> {

    private static final ResourceLocation TEXTURE = HBMsNTM.withDefaultNamespaceNT("textures/gui/storage/gui_battery_socket.png");

    public BatterySocketScreen(BatterySocketMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.imageWidth = 176;
        this.imageHeight = 181;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (this.menu.socket.slots.getFirst().getItem() instanceof IBatteryItem batteryItem) {
            String deltaText = BobMathUtil.getShortNumber(Math.abs(menu.socket.delta)) + "HE/s";

            if (menu.socket.delta > 0) deltaText = ChatFormatting.GREEN + "+" + deltaText;
            else if(menu.socket.delta < 0) deltaText = ChatFormatting.RED + "-" + deltaText;
            else deltaText = ChatFormatting.YELLOW + "+" + deltaText;

            List<Component> info = List.of(
                    Component.literal(BobMathUtil.getShortNumber(batteryItem.getCharge(this.menu.socket.slots.getFirst())) + "/"
                            + BobMathUtil.getShortNumber(batteryItem.getMaxCharge(this.menu.socket.slots.getFirst())) + "HE"),
                    Component.literal(deltaText)
            );

            this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, leftPos + 62, topPos + 69 - 52, 34, 52, mouseX, mouseY, info);
        }

        String lang = switch (this.menu.socket.priority) {
            case LOW -> "low";
            case HIGH -> "high";
            default -> "normal";
        };

        List<Component> priority = new ArrayList<>();
        priority.add(Component.translatable("battery.priority." + lang).withStyle(ChatFormatting.GRAY));
        priority.add(Component.translatable("battery.priority.recommended").withStyle(ChatFormatting.GRAY));
        String[] desc = I18nUtil.resolveKeyArray("battery.priority." + lang + ".desc");
        for (String s : desc) priority.add(Component.literal(s).withStyle(ChatFormatting.GRAY));

        this.drawCustomInfoStat(guiGraphics, mouseX, mouseY, leftPos + 125, topPos + 35, 16, 16, mouseX, mouseY, priority);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {

        CompoundTag tag = new CompoundTag();

        if (this.checkClick((int) x, (int) y, 106, 16, 18, 18)) { this.click(); tag.putBoolean("low", true); }
        if (this.checkClick((int) x, (int) y, 106, 52, 18, 18)) { this.click(); tag.putBoolean("high", true); }
        if (this.checkClick((int) x, (int) y, 125, 35, 16, 16)) { this.click(); tag.putBoolean("priority", true); }

        if (!tag.isEmpty()) PacketDistributor.sendToServer(new CompoundTagControl(tag, this.menu.socket.getBlockPos()));

        return super.mouseClicked(x, y, button);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int partialTicks) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        if (this.menu.socket.slots.getFirst().getItem() instanceof IBatteryItem batteryItem) {
            long power = batteryItem.getCharge(this.menu.socket.slots.getFirst());
            long maxPower = batteryItem.getMaxCharge(this.menu.socket.slots.getFirst());
            if (power > Long.MAX_VALUE / 100) {
                power /= 100;
                maxPower /= 100;
            }
            if (maxPower <= 1) maxPower = 1;
            int p = (int) (power * 52 / maxPower); // won't work then flying too close to the sun (the limits of the LONG data type)
            guiGraphics.blit(TEXTURE, this.leftPos + 62, this.topPos + 69 - p, 176, 52 - p, 34, p);
        }

        guiGraphics.blit(TEXTURE, this.leftPos + 106, this.topPos + 16, 176, 52 + menu.socket.redLow * 18, 18, 18);
        guiGraphics.blit(TEXTURE, this.leftPos + 106, this.topPos + 52, 176, 52 + menu.socket.redHigh * 18, 18, 18);
        guiGraphics.blit(TEXTURE, this.leftPos + 125, this.topPos + 35, 194, 52 + menu.socket.priority.ordinal() * 16 - 16, 16, 16);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.imageHeight - 96 + 2, 4210752, false);
    }
}
