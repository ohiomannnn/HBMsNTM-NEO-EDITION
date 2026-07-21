package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineShredderBlockEntity;
import com.hbm.inventory.menus.MachineShredderMenu;
import com.hbm.main.NuclearTechMod;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class MachineShredderScreen extends InfoScreen<MachineShredderMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/gui_shredder.png");

    private final MachineShredderBlockEntity be;

    public MachineShredderScreen(MachineShredderMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 176;
        this.imageHeight = 233;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 8, this.topPos + 18, 16, 88, this.be.getPower(), this.be.getMaxPower());

        boolean flag = this.be.getGearLeft() == 0 || this.be.getGearLeft() == 3 || this.be.getGearRight() == 0 || this.be.getGearRight() == 3;
        if(flag) {
            this.drawCustomInfoStat(
                    guiGraphics,
                    mouseX,
                    mouseY,
                    this.leftPos - 16,
                    this.topPos + 36,
                    16,
                    16,
                    mouseX,
                    mouseY,
                    List.of(Component.translatable("container.machine_shredder.blades_missing").withStyle(ChatFormatting.YELLOW))
            );
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int p = this.be.getPowerScaled(88);
        if(p > 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 106 - p, 176, 160 - p, 16, p);
        }

        int progress = this.be.getProgressScaled(34);
        guiGraphics.blit(TEXTURE, this.leftPos + 63, this.topPos + 89, 176, 54, progress + 1, 18);

        int left = this.be.getGearLeft();
        int right = this.be.getGearRight();

        if(left != 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 43, this.topPos + 71, 176, (left - 1) * 18, 18, 18);
        }

        if(right != 0) {
            guiGraphics.blit(TEXTURE, this.leftPos + 79, this.topPos + 71, 194, (right - 1) * 18, 18, 18);
        }

        if(left == 0 || left == 3 || right == 0 || right == 3) {
            this.drawInfoPanel(guiGraphics, this.leftPos - 16, this.topPos + 36, 6);
        }
    }
}
