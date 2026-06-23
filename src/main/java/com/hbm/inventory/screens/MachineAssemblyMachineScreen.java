package com.hbm.inventory.screens;

import com.hbm.blockentity.machine.MachineAssemblyMachineBlockEntity;
import com.hbm.inventory.menus.MachineAssemblyMachineMenu;
import com.hbm.inventory.recipes.AssemblyMachineRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.BlueprintsItem;
import com.hbm.main.NuclearTechMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineAssemblyMachineScreen extends InfoScreen<MachineAssemblyMachineMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/assembler.png");

    private final MachineAssemblyMachineBlockEntity be;

    public MachineAssemblyMachineScreen(MachineAssemblyMachineMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.be = menu.be;

        this.imageWidth = 176;
        this.imageHeight = 256;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        be.inputTank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 8, this.topPos + 99, 52, 16);
        be.outputTank.renderTankTooltip(guiGraphics, mouseX, mouseY, this.leftPos + 80, this.topPos + 99, 52, 16);

        this.drawElectricityInfo(guiGraphics, mouseX, mouseY, this.leftPos + 152, this.topPos + 18, 16, 61, be.power, be.maxPower);

        if(this.leftPos + 7 <= mouseX && this.leftPos + 7 + 18 > mouseX && this.topPos + 125 < mouseY && this.topPos + 125 + 18 >= mouseY) {
            if(this.be.assemblerModule.recipe != null && AssemblyMachineRecipes.INSTANCE.recipeNameMap.containsKey(this.be.assemblerModule.recipe)) {
                GenericRecipe recipe = AssemblyMachineRecipes.INSTANCE.recipeNameMap.get(this.be.assemblerModule.recipe);
                guiGraphics.renderComponentTooltip(this.font, recipe.print(), mouseX, mouseY);
                //GUIElements.drawHoveringTextRecipe(recipe.print(), mouseX, mouseY, this.fontRendererObj, itemRender, this.width, this.height);
            } else {
                guiGraphics.renderTooltip(this.font, Component.translatable("container.recipe.set_recipe").withStyle(ChatFormatting.YELLOW), mouseX, mouseY);
            }
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if(this.checkClick(mouseX, mouseY, 7, 125, 18, 18)) RecipeSelectorScreen.openSelector(AssemblyMachineRecipes.INSTANCE, be, be.assemblerModule.recipe, 0, BlueprintsItem.grabPool(be.slots.get(1)), this);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 70 - font.width(this.title) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        int p = (int) (be.power * 61 / be.maxPower);
        guiGraphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 79 - p, 176, 61 - p, 16, p);

        if(be.assemblerModule.progress > 0) {
            int j = (int) Math.ceil(70 * be.assemblerModule.progress);
            guiGraphics.blit(TEXTURE, this.leftPos + 62, this.topPos + 126, 176, 61, j, 16);
        }

        GenericRecipe recipe = AssemblyMachineRecipes.INSTANCE.recipeNameMap.get(be.assemblerModule.recipe);

        /// LEFT LED
        if(be.didProcess) {
            guiGraphics.blit(TEXTURE, this.leftPos + 51, this.topPos + 121, 195, 0, 3, 6);
        } else if(recipe != null) {
            guiGraphics.blit(TEXTURE, this.leftPos + 51, this.topPos + 121, 192, 0, 3, 6);
        }

        /// RIGHT LED
        if(be.didProcess) {
            guiGraphics.blit(TEXTURE, this.leftPos + 56, this.topPos + 121, 195, 0, 3, 6);
        } else if(recipe != null && be.power >= recipe.power) {
            guiGraphics.blit(TEXTURE, this.leftPos + 56, this.topPos + 121, 192, 0, 3, 6);
        }

        guiGraphics.renderItem(recipe != null ? recipe.getIcon() : new ItemStack(NtmItems.TEMPLATE_FOLDER.get()), this.leftPos + 8, this.topPos + 126);

        if(recipe != null && recipe.inputItem != null) {

            RenderSystem.setShaderColor(1F, 1F, 1F, 0.5F);
            for(int i = 0; i < recipe.inputItem.length; i++) {
                Slot slot = this.menu.slots.get(be.assemblerModule.inputSlots[i]);
                if(!slot.hasItem()) guiGraphics.renderItem(recipe.inputItem[i].extractForCyclingDisplay(20), this.leftPos + slot.x, this.topPos + slot.y);
            }
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        }

        be.inputTank.renderTank(this.leftPos + 8, this.topPos + 115, 1F, 52, 16, 1);
        be.outputTank.renderTank(this.leftPos + 80, this.topPos + 115, 1F, 52, 16, 1);
    }
}
