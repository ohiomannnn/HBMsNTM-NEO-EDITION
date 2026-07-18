package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.oil.MachineRefineryBlockEntity;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.menus.MachineRefineryMenu;
import com.hbm.inventory.recipes.RefineryRecipes;
import com.hbm.main.NuclearTechMod;
import com.hbm.util.Tuple;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class MachineRefineryScreen extends InfoScreen<MachineRefineryMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/gui_refinery.png");

    private final MachineRefineryBlockEntity be;

    public MachineRefineryScreen(MachineRefineryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = menu.be;
        this.imageWidth = 210;
        this.imageHeight = 231;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.be.tanks[0].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 30, this.topPos + 27, 21, 104);
        this.be.tanks[1].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 86, this.topPos + 42, 16, 52);
        this.be.tanks[2].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 106, this.topPos + 42, 16, 52);
        this.be.tanks[3].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 126, this.topPos + 42, 16, 52);
        this.be.tanks[4].renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 146, this.topPos + 42, 16, 52);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 186, this.topPos + 18, 16, 52, this.be.power, MachineRefineryBlockEntity.MAX_POWER);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 4, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 350, 256);

        int p = (int) (this.be.power * 50 / Math.max(MachineRefineryBlockEntity.MAX_POWER, 1L));
        guiGraphics.blit(TEXTURE, this.leftPos + 186, this.topPos + 69 - p, 210, 52 - p, 16, p, 350, 256);

        this.be.tanks[0].renderTank(this.leftPos + 33, this.topPos + 130, 0, 16, 101, 1);

        Tuple.Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack> recipe = RefineryRecipes.getRefinery(this.be.tanks[0].getTankType());

        if(recipe == null) {
            guiGraphics.blit(TEXTURE, this.leftPos + 52, this.topPos + 63, 247, 1, 33, 48, 350, 256);
            guiGraphics.blit(TEXTURE, this.leftPos + 52, this.topPos + 32, 247, 50, 66, 52, 350, 256);
            guiGraphics.blit(TEXTURE, this.leftPos + 52, this.topPos + 24, 247, 145, 86, 35, 350, 256);
            guiGraphics.blit(TEXTURE, this.leftPos + 36, this.topPos + 16, 211, 119, 122, 25, 350, 256);
        } else {
            int[] colors = new int[] {
                    recipe.getV().type.getColor(),
                    recipe.getW().type.getColor(),
                    recipe.getX().type.getColor(),
                    recipe.getY().type.getColor()
            };

            guiGraphics.setColor((colors[0] >> 16 & 255) / 255F, (colors[0] >> 8 & 255) / 255F, (colors[0] & 255) / 255F, 1F);
            guiGraphics.blit(TEXTURE, this.leftPos + 52, this.topPos + 63, 247, 1, 33, 48, 350, 256);

            guiGraphics.setColor((colors[1] >> 16 & 255) / 255F, (colors[1] >> 8 & 255) / 255F, (colors[1] & 255) / 255F, 1F);
            guiGraphics.blit(TEXTURE, this.leftPos + 52, this.topPos + 32, 247, 50, 66, 52, 350, 256);

            guiGraphics.setColor((colors[2] >> 16 & 255) / 255F, (colors[2] >> 8 & 255) / 255F, (colors[2] & 255) / 255F, 1F);
            guiGraphics.blit(TEXTURE, this.leftPos + 52, this.topPos + 24, 247, 145, 86, 35, 350, 256);

            guiGraphics.setColor((colors[3] >> 16 & 255) / 255F, (colors[3] >> 8 & 255) / 255F, (colors[3] & 255) / 255F, 1F);
            guiGraphics.blit(TEXTURE, this.leftPos + 36, this.topPos + 16, 211, 119, 122, 25, 350, 256);

            guiGraphics.setColor(1F, 1F, 1F, 1F);
        }

        this.be.tanks[1].renderTank(this.leftPos + 86, this.topPos + 95, 0, 16, 52);
        this.be.tanks[2].renderTank(this.leftPos + 106, this.topPos + 95, 0, 16, 52);
        this.be.tanks[3].renderTank(this.leftPos + 126, this.topPos + 95, 0, 16, 52);
        this.be.tanks[4].renderTank(this.leftPos + 146, this.topPos + 95, 0, 16, 52);
    }
}
