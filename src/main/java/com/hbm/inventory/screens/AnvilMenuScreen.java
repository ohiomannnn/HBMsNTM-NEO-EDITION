package com.hbm.inventory.screens;

import com.hbm.inventory.menus.AnvilMenu;
import com.hbm.inventory.recipes.anvil.AnvilRecipes;
import com.hbm.inventory.recipes.anvil.AnvilRecipes.AnvilConstructionRecipe;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.AnvilConstructionPacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AnvilMenuScreen extends InfoScreen<AnvilMenu> {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_anvil.png");
    private static final int PAGE_SIZE = 10;

    private final List<AnvilConstructionRecipe> originList = new ArrayList<>();
    private final List<AnvilConstructionRecipe> recipes = new ArrayList<>();

    private EditBox search;
    private int index;
    private int size;
    private int selection = -1;
    private int lastSize = 1;

    public AnvilMenuScreen(AnvilMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 222;

        for(AnvilConstructionRecipe recipe : AnvilRecipes.getConstruction()) {
            if(recipe.isTierValid(menu.getTier())) {
                this.originList.add(recipe);
            }
        }

        this.regenerateRecipes();
    }

    @Override
    protected void init() {
        super.init();

        this.search = new EditBox(this.font, this.leftPos + 10, this.topPos + 111, 84, 12, Component.empty());
        this.search.setTextColor(-1);
        this.search.setTextColorUneditable(-1);
        this.search.setBordered(false);
        this.search.setMaxLength(25);
        this.addRenderableWidget(this.search);
    }

    private void regenerateRecipes() {
        this.recipes.clear();
        this.recipes.addAll(this.originList);
        this.resetPaging();
    }

    private void search(String search) {
        String needle = search.toLowerCase(Locale.US);
        this.recipes.clear();

        if(needle.isEmpty()) {
            this.recipes.addAll(this.originList);
        } else {
            for(AnvilConstructionRecipe recipe : this.originList) {
                if(recipe.matchesSearch(needle)) {
                    this.recipes.add(recipe);
                }
            }
        }

        this.resetPaging();
    }

    private void resetPaging() {
        this.index = 0;
        this.selection = -1;
        this.size = Math.max(0, (int)Math.ceil((this.recipes.size() - PAGE_SIZE) / 2D));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        if(this.selection >= 0 && this.selection < this.recipes.size()) {
            AnvilConstructionRecipe recipe = this.recipes.get(this.selection);
            List<Component> lines = this.recipeToList(recipe);
            int longest = 0;
            for(Component line : lines) {
                longest = Math.max(longest, this.font.width(line));
            }

            float scale = 0.5F;
            int textX = Mth.floor((this.leftPos + 130) / scale);
            int textY = Mth.floor((this.topPos + 25) / scale);
            int offset = 0;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, scale);
            for(Component line : lines) {
                guiGraphics.drawString(this.font, line, textX, textY + offset, 0xFFFFFF, false);
                offset += 10;
            }
            guiGraphics.pose().popPose();

            this.lastSize = (int)(longest * scale);
        } else {
            this.lastSize = 0;
        }

        for(int i = this.index * 2; i < this.index * 2 + PAGE_SIZE; i++) {
            if(i >= this.recipes.size()) {
                break;
            }

            int ind = i - this.index * 2;
            int ix = 16 + 18 * (ind / 2);
            int iy = 71 + 18 * (ind % 2);
            if(this.isHovered(mouseX, mouseY, ix, iy, 18, 18)) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(this.recipes.get(i).getDisplay().getHoverName());
                guiGraphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
            }
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int slide = Mth.clamp(this.lastSize - 42, 0, 1000);
        int mul = 1;
        while(slide >= 51 * mul) {
            guiGraphics.blit(TEXTURE, this.leftPos + 125 + 51 * mul, this.topPos + 17, 125, 17, 54, 108);
            mul++;
        }
        guiGraphics.blit(TEXTURE, this.leftPos + 125 + slide, this.topPos + 17, 125, 17, 54, 108);

        if(this.search != null && this.search.isFocused()) {
            guiGraphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 108, 168, 222, 88, 16);
        }

        if(this.isHovered(mouseX, mouseY, 7, 71, 9, 36)) {
            guiGraphics.blit(TEXTURE, this.leftPos + 7, this.topPos + 71, 176, 186, 9, 36);
        }
        if(this.isHovered(mouseX, mouseY, 106, 71, 9, 36)) {
            guiGraphics.blit(TEXTURE, this.leftPos + 106, this.topPos + 71, 185, 186, 9, 36);
        }
        if(this.isHovered(mouseX, mouseY, 52, 53, 18, 18)) {
            guiGraphics.blit(TEXTURE, this.leftPos + 52, this.topPos + 53, 176, 150, 18, 18);
        }
        if(this.isHovered(mouseX, mouseY, 97, 107, 18, 18)) {
            guiGraphics.blit(TEXTURE, this.leftPos + 97, this.topPos + 107, 176, 168, 18, 18);
        }

        for(int i = this.index * 2; i < this.index * 2 + PAGE_SIZE; i++) {
            if(i >= this.recipes.size()) {
                break;
            }

            int ind = i - this.index * 2;
            AnvilConstructionRecipe recipe = this.recipes.get(i);
            guiGraphics.renderItem(recipe.getDisplay(), this.leftPos + 17 + 18 * (ind / 2), this.topPos + 72 + 18 * (ind % 2));
            guiGraphics.blit(TEXTURE, this.leftPos + 16 + 18 * (ind / 2), this.topPos + 71 + 18 * (ind % 2), 18 + 18 * recipe.getOverlay().ordinal(), 222, 18, 18);

            if(this.selection == i) {
                guiGraphics.blit(TEXTURE, this.leftPos + 16 + 18 * (ind / 2), this.topPos + 71 + 18 * (ind % 2), 0, 222, 18, 18);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 61 - this.font.width(this.title) / 2, 8, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if(this.isHovered(mouseX, mouseY, 0, 0, this.imageWidth, this.imageHeight)) {
            if(scrollY > 0 && this.index > 0) {
                this.index--;
            }
            if(scrollY < 0 && this.index < this.size) {
                this.index++;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.search.mouseClicked(mouseX, mouseY, button);

        if(this.isHovered(mouseX, mouseY, 7, 71, 9, 36)) {
            this.click();
            if(this.index > 0) {
                this.index--;
            }
            return true;
        }

        if(this.isHovered(mouseX, mouseY, 106, 71, 9, 36)) {
            this.click();
            if(this.index < this.size) {
                this.index++;
            }
            return true;
        }

        if(this.isHovered(mouseX, mouseY, 52, 53, 18, 18)) {
            if(this.selection >= 0 && this.selection < this.recipes.size()) {
                this.click();
                int recipeIndex = AnvilRecipes.getConstruction().indexOf(this.recipes.get(this.selection));
                PacketDistributor.sendToServer(new AnvilConstructionPacket(recipeIndex, Screen.hasShiftDown() ? 1 : 0));
            }
            return true;
        }

        if(this.isHovered(mouseX, mouseY, 97, 107, 18, 18)) {
            this.click();
            this.search.setValue("");
            this.search(this.search.getValue());
            this.search.setFocused(true);
            return true;
        }

        for(int i = this.index * 2; i < this.index * 2 + PAGE_SIZE; i++) {
            if(i >= this.recipes.size()) {
                break;
            }

            int ind = i - this.index * 2;
            int ix = 16 + 18 * (ind / 2);
            int iy = 71 + 18 * (ind % 2);
            if(this.isHovered(mouseX, mouseY, ix, iy, 18, 18)) {
                if(this.selection != i) {
                    this.selection = i;
                } else {
                    this.selection = -1;
                }

                this.click();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
        if(keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.onClose();
            return true;
        }

        if(this.search.keyPressed(keyCode, scanCode, modifiers)) {
            this.search(this.search.getValue());
            return true;
        }

        if(this.search.isFocused()) {
            if(keyCode == GLFW.GLFW_KEY_ENTER) {
                this.search.setFocused(false);
            }
            return true;
        }

        if(this.minecraft.options.keyInventory.isActiveAndMatches(key)) {
            this.onClose();
            return true;
        }

        if(keyCode == GLFW.GLFW_KEY_UP) {
            this.index--;
        }
        if(keyCode == GLFW.GLFW_KEY_DOWN) {
            this.index++;
        }
        if(keyCode == GLFW.GLFW_KEY_PAGE_UP) {
            this.index -= 5;
        }
        if(keyCode == GLFW.GLFW_KEY_PAGE_DOWN) {
            this.index += 5;
        }
        if(keyCode == GLFW.GLFW_KEY_HOME) {
            this.index = 0;
        }
        if(keyCode == GLFW.GLFW_KEY_END) {
            this.index = this.size;
        }

        this.index = Mth.clamp(this.index, 0, this.size);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if(this.search.charTyped(codePoint, modifiers)) {
            this.search(this.search.getValue());
            return true;
        }

        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private List<Component> recipeToList(AnvilConstructionRecipe recipe) {
        List<Component> list = new ArrayList<>();
        list.add(Component.translatable("container.recipe.input").withStyle(ChatFormatting.YELLOW));

        for(var stack : recipe.input) {
            for(ItemStack input : stack.extractForJEI()) {
                list.add(Component.literal("> " + input.getCount() + "x " + input.getHoverName().getString()));
            }
        }

        list.add(Component.empty());
        list.add(Component.translatable("container.recipe.output").withStyle(ChatFormatting.YELLOW));

        for(var stack : recipe.output) {
            String line = "> " + stack.stack.getCount() + "x " + stack.stack.getHoverName().getString();
            if(stack.chance != 1F) {
                line += " (" + (stack.chance * 100F) + "%)";
            }
            list.add(Component.literal(line));
        }

        return list;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void click() {
        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
