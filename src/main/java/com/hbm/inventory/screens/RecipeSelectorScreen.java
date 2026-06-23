package com.hbm.inventory.screens;

import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toserver.CompoundTagControl;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class RecipeSelectorScreen extends Screen {

    public static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_recipe_selector.png");

    //basic GUI setup
    protected int xSize = 176;
    protected int ySize = 132;
    protected int leftPos;
    protected int topPos;
    // search crap
    protected GenericRecipes recipeSet;
    protected List<GenericRecipe> recipes = new ArrayList<>();
    protected EditBox search;
    protected int pageIndex;
    protected int size;
    protected String selection;
    public static final String NULL_SELECTION = "null";
    // callback
    protected int index;
    protected IControlReceiver be;
    protected Screen previousScreen;
    protected String installedPool;

    public static void openSelector(GenericRecipes recipeSet, IControlReceiver be, String selection, int index, String installedPool, Screen previousScreen) {
        Minecraft.getInstance().setScreen(new RecipeSelectorScreen(recipeSet, be, selection, index, installedPool, previousScreen));
    }

    public RecipeSelectorScreen(GenericRecipes recipeSet, IControlReceiver be, String selection, int index, String installedPool, Screen previousScreen) {
        super(Component.empty());
        this.recipeSet = recipeSet;
        this.selection = selection;
        this.be = be;
        this.index = index;
        this.installedPool = installedPool;
        this.previousScreen = previousScreen;
        if(this.selection == null) this.selection = NULL_SELECTION;

        regenerateRecipes();
    }

    @Override
    protected void init() {

        this.leftPos = (this.width - this.xSize) / 2;
        this.topPos = (this.height - this.ySize) / 2;

        this.search = new EditBox(this.font, this.leftPos + 28, this.topPos + 111, 102, 12, Component.empty());
        this.search.setTextColor(-1);
        this.search.setTextColorUneditable(-1);
        this.search.setBordered(false);
        this.search.setMaxLength(32);
        this.addWidget(this.search);
    }

    private void regenerateRecipes() {

        this.recipes.clear();

        for(Object o : recipeSet.recipeOrderedList) {
            GenericRecipe recipe = (GenericRecipe) o;
            if(!recipe.isPooled() || (this.installedPool != null && recipe.isPartOfPool(installedPool))) this.recipes.add(recipe);
        }

        resetPaging();
    }

    private void search(String search) {
        this.recipes.clear();

        if(search.isEmpty()) {
            regenerateRecipes();
        } else {
            for(Object o : recipeSet.recipeOrderedList) {
                GenericRecipe recipe = (GenericRecipe) o;
                if(recipe.matchesSearch(search)) {
                    if(!recipe.isPooled() || (this.installedPool != null && recipe.isPartOfPool(installedPool))) this.recipes.add(recipe);
                }
            }

            resetPaging();
        }
    }

    private void resetPaging() {
        this.pageIndex = 0;
        this.size = Math.max(0, (int)Math.ceil((this.recipes.size() - 40) / 8D));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderTransparentBackground(guiGraphics);

        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);

        for(Renderable renderable : this.renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, partialTicks);
        }

        if(this.leftPos + 7 <= mouseX && this.leftPos + 7 + 144 > mouseX && this.topPos + 17 < mouseY && this.topPos + 17 + 90 >= mouseY) {
            for(int i = pageIndex * 8; i < pageIndex * 8 + 40; i++) {
                if(i >= this.recipes.size()) break;

                int ind = i - pageIndex * 8;
                int ix = 7 + 18 * (ind % 8);
                int iy = 17 + 18 * (ind / 8);

                if(this.leftPos + ix <= mouseX && this.leftPos + ix + 18 > mouseX && this.topPos + iy < mouseY && this.topPos + iy + 18 >= mouseY) {
                    GenericRecipe recipe = recipes.get(i);
                    guiGraphics.renderComponentTooltip(this.font, recipe.print(), mouseX, mouseY);
                }
            }
        }


        if(this.leftPos + 151 <= mouseX && this.leftPos + 151 + 18 > mouseX && this.topPos + 71 < mouseY && this.topPos + 71 + 18 >= mouseY) {
            if(this.selection != null && this.recipeSet.recipeNameMap.containsKey(selection)) {
                GenericRecipe recipe = (GenericRecipe) this.recipeSet.recipeNameMap.get(selection);
                guiGraphics.renderComponentTooltip(this.font, recipe.print(), mouseX, mouseY);
            }
        }

        if(this.leftPos + 152 <= mouseX && this.leftPos + 152 + 16 > mouseX && this.topPos + 90 < mouseY && this.topPos + 90 + 16 >= mouseY) {
            guiGraphics.renderTooltip(this.font, Component.translatable("container.recipe_selector.close").withStyle(ChatFormatting.YELLOW), mouseX, mouseY);
        }

        if(this.leftPos + 134 <= mouseX && this.leftPos + 134 + 16 > mouseX && this.topPos + 108 < mouseY && this.topPos + 108 + 16 >= mouseY) {
            guiGraphics.renderTooltip(this.font, Component.translatable("container.recipe_selector.close_search").withStyle(ChatFormatting.YELLOW), mouseX, mouseY);
        }

        if(this.leftPos + 8 <= mouseX && this.leftPos + 8 + 16 > mouseX && this.topPos + 108 < mouseY && this.topPos + 108 + 16 >= mouseY) {
            guiGraphics.renderTooltip(this.font, Component.translatable("container.recipe_selector.toggle_focus").withStyle(ChatFormatting.ITALIC), mouseX, mouseY);
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, xSize, ySize);

        if(this.search.isFocused()) {
            graphics.blit(TEXTURE, this.leftPos + 26, this.topPos + 108, 0, 132, 106, 16);
        }

        if(this.leftPos + 152 <= mouseX && this.leftPos + 152 + 16 > mouseX && this.topPos + 18 < mouseY && this.topPos + 18 + 16 >= mouseY) {
            graphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 18, 176, 0, 16, 16);
        }

        if(this.leftPos + 152 <= mouseX && this.leftPos + 152 + 16 > mouseX && this.topPos + 36 < mouseY && this.topPos + 36 + 16 >= mouseY) {
            graphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 36, 176, 16, 16, 16);
        }

        if(this.leftPos + 152 <= mouseX && this.leftPos + 152 + 16 > mouseX && this.topPos + 90 < mouseY && this.topPos + 90 + 16 >= mouseY) {
            graphics.blit(TEXTURE, this.leftPos + 152, this.topPos + 90, 176, 32, 16, 16);
        }

        if(this.leftPos + 134 <= mouseX && this.leftPos + 134 + 16 > mouseX && this.topPos + 108 < mouseY && this.topPos + 108 + 16 >= mouseY) {
            graphics.blit(TEXTURE, this.leftPos + 134, this.topPos + 108, 176, 48, 16, 16);
        }

        if(this.leftPos + 8 <= mouseX && this.leftPos + 8 + 16 > mouseX && this.topPos + 108 < mouseY && this.topPos + 108 + 16 >= mouseY) {
            graphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 108, 176, 64, 16, 16);
        }

        for(int i = pageIndex * 8; i < pageIndex * 8 + 40; i++) {
            if(i >= recipes.size()) break;
            int ind = i - pageIndex * 8;
            GenericRecipe recipe = recipes.get(i);
            if(recipe.getInternalName().equals(this.selection)) graphics.blit(TEXTURE, this.leftPos + 7 + 18 * (ind % 8), this.topPos + 17 + 18 * (ind / 8), 192, 0, 18, 18);
        }

        for(int i = pageIndex * 8; i < pageIndex * 8 + 40; i++) {
            if(i >= recipes.size()) break;

            int ind = i - pageIndex * 8;
            GenericRecipe recipe = recipes.get(i);

            graphics.renderItem(recipe.getIcon(), this.leftPos + (8 + 18 * (ind % 8)), this.topPos + (18 + 18 * (ind / 8)));
        }

        if(this.selection != null && this.recipeSet.recipeNameMap.containsKey(selection)) {
            GenericRecipe recipe = (GenericRecipe) this.recipeSet.recipeNameMap.get(selection);
            graphics.renderItem(recipe.getIcon(), this.leftPos + 152, this.topPos + 72);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {

        if(scrollY < 0 && this.pageIndex > 0) this.pageIndex--;
        if(scrollY > 0 && this.pageIndex < this.size) this.pageIndex++;

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        this.search.mouseClicked(mouseX, mouseY, button);

        if(this.leftPos + 152 <= mouseX && this.leftPos + 152 + 16 > mouseX && this.topPos + 18 < mouseY && this.topPos + 18 + 16 >= mouseY) {
            click();
            if(this.pageIndex > 0) this.pageIndex--;
            return true;
        }

        if(this.leftPos + 152 <= mouseX && this.leftPos + 152 + 16 > mouseX && this.topPos + 36 < mouseY && this.topPos + 36 + 16 >= mouseY) {
            click();
            if(this.pageIndex < this.size) this.pageIndex++;
            return true;
        }

        if(this.leftPos + 134 <= mouseX && this.leftPos + 134 + 16 > mouseX && this.topPos + 108 < mouseY && this.topPos + 108 + 16 >= mouseY) {
            this.search.setValue("");
            this.search("");
            this.search.setFocused(true);
            return true;
        }

        for(int i = pageIndex * 8; i < pageIndex * 8 + 40; i++) {
            if(i >= this.recipes.size()) break;

            int ind = i - pageIndex * 8;
            int ix = 7 + 18 * (ind % 8);
            int iy = 17 + 18 * (ind / 8);

            if(this.leftPos + ix <= mouseX && this.leftPos + ix + 18 > mouseX && this.topPos + iy < mouseY && this.topPos + iy + 18 >= mouseY) {

                String newSelection = recipes.get(i).getInternalName();

                if(!newSelection.equals(selection))
                    this.selection = newSelection;
                else
                    this.selection = NULL_SELECTION;

                click();
                return true;
            }
        }

        if(this.leftPos + 151 <= mouseX && this.leftPos + 151 + 18 > mouseX && this.topPos + 71 < mouseY && this.topPos + 71 + 18 >= mouseY) {
            if(!NULL_SELECTION.equals(this.selection)) {
                this.selection = NULL_SELECTION;
                click();
                return true;
            }
        }

        if(this.leftPos + 152 <= mouseX && this.leftPos + 152 + 16 > mouseX && this.topPos + 90 < mouseY && this.topPos + 90 + 16 >= mouseY) {
            this.onClose();
            Minecraft.getInstance().setScreen(previousScreen);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onClose() {
        super.onClose();

        CompoundTag tag = new CompoundTag();
        tag.putInt("index", this.index);
        tag.putString("selection", this.selection);
        BlockEntity be = (BlockEntity) this.be;
        PacketDistributor.sendToServer(new CompoundTagControl(tag, be.getBlockPos()));
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if(keyCode == GLFW.GLFW_KEY_ENTER) {
            this.search.setFocused(!this.search.isFocused());
            return true;
        }

        if(this.search.keyPressed(keyCode, scanCode, modifiers)) {
            this.search(this.search.getValue());
            return true;
        }

        if(keyCode == GLFW.GLFW_KEY_UP) pageIndex--;
        if(keyCode == GLFW.GLFW_KEY_DOWN) pageIndex++;
        if(keyCode == GLFW.GLFW_KEY_PAGE_UP) pageIndex -= 5;
        if(keyCode == GLFW.GLFW_KEY_PAGE_DOWN) pageIndex += 5;
        if(keyCode == GLFW.GLFW_KEY_HOME) pageIndex = 0;
        if(keyCode == GLFW.GLFW_KEY_END) pageIndex = size;

        pageIndex = Mth.clamp(pageIndex, 0, size);

        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);

        if(keyCode == GLFW.GLFW_KEY_ESCAPE || this.minecraft.options.keyInventory.isActiveAndMatches(key)) {
            this.onClose();
            Minecraft.getInstance().setScreen(previousScreen);
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override public boolean isPauseScreen() { return false; }

    public void click() {
        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
