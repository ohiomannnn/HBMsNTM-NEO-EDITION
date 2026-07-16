package com.hbm.inventory.screens;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.machine.NTMAnvilBlock;
import com.hbm.inventory.MetaHelper;
import com.hbm.main.NuclearTechMod;
import com.hbm.util.i18n.I18nUtil;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AnvilScreen extends Screen {

    public static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/gui/processing/gui_anvil.png");

    private static final int IMAGE_WIDTH = 176;
    private static final int IMAGE_HEIGHT = 222;
    private static final int PAGE_SIZE = 10;

    private final NTMAnvilBlock.Variant variant;
    private final int tier;

    private final List<VariantEntry> originList = new ArrayList<>();
    private final List<VariantEntry> recipes = new ArrayList<>();

    private EditBox search;
    private int leftPos;
    private int topPos;
    private int index;
    private int size;
    private int selection = -1;
    private int lastSize = 1;

    public AnvilScreen(NTMAnvilBlock.Variant variant, int tier) {
        super(Component.translatable("container.anvil", tier));
        this.variant = variant;
        this.tier = tier;

        for(NTMAnvilBlock.Variant entry : NTMAnvilBlock.Variant.values()) {
            this.originList.add(new VariantEntry(entry));
        }

        this.regenerateRecipes();
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - IMAGE_WIDTH) / 2;
        this.topPos = (this.height - IMAGE_HEIGHT) / 2;

        this.search = new EditBox(this.font, this.leftPos + 10, this.topPos + 111, 84, 12, Component.empty());
        this.search.setTextColor(-1);
        this.search.setTextColorUneditable(-1);
        this.search.setBordered(false);
        this.search.setMaxLength(25);
        this.addWidget(this.search);

        this.selectVariant(this.variant);
    }

    private void regenerateRecipes() {
        this.recipes.clear();

        for(VariantEntry entry : this.originList) {
            if(entry.isTierValid(this.tier)) {
                this.recipes.add(entry);
            }
        }

        this.resetPaging();
    }

    private void search(String search) {
        String needle = search.toLowerCase(Locale.US);

        this.recipes.clear();

        if(needle.isEmpty()) {
            this.recipes.addAll(this.originList.stream().filter(entry -> entry.isTierValid(this.tier)).toList());
        } else {
            for(VariantEntry entry : this.originList) {
                if(!entry.isTierValid(this.tier)) {
                    continue;
                }

                for(String haystack : entry.searchTerms()) {
                    if(haystack.contains(needle)) {
                        this.recipes.add(entry);
                        break;
                    }
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

    private void selectVariant(NTMAnvilBlock.Variant target) {
        for(int i = 0; i < this.recipes.size(); i++) {
            if(this.recipes.get(i).variant == target) {
                this.selection = i;
                this.index = i / PAGE_SIZE;
                return;
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(graphics);

        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        int slide = Mth.clamp(this.lastSize - 42, 0, 1000);
        int mul = 1;
        while(slide >= 51 * mul) {
            graphics.blit(TEXTURE, this.leftPos + 125 + 51 * mul, this.topPos + 17, 125, 17, 54, 108);
            mul++;
        }
        graphics.blit(TEXTURE, this.leftPos + 125 + slide, this.topPos + 17, 125, 17, 54, 108);

        if(this.search != null && this.search.isFocused()) {
            graphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 108, 168, 222, 88, 16);
        }

        graphics.drawString(this.font, this.title, this.leftPos + 61 - this.font.width(this.title) / 2, this.topPos + 8, 0x404040, false);
        graphics.drawString(this.font, Component.translatable("container.inventory"), this.leftPos + 8, this.topPos + IMAGE_HEIGHT - 96 + 2, 0x404040, false);

        if(this.leftPos + 7 <= mouseX && this.leftPos + 7 + 9 > mouseX && this.topPos + 71 < mouseY && this.topPos + 71 + 36 >= mouseY) {
            graphics.blit(TEXTURE, this.leftPos + 7, this.topPos + 71, 176, 186, 9, 36);
        }
        if(this.leftPos + 106 <= mouseX && this.leftPos + 106 + 9 > mouseX && this.topPos + 71 < mouseY && this.topPos + 71 + 36 >= mouseY) {
            graphics.blit(TEXTURE, this.leftPos + 106, this.topPos + 71, 185, 186, 9, 36);
        }
        if(this.leftPos + 52 <= mouseX && this.leftPos + 52 + 18 > mouseX && this.topPos + 53 < mouseY && this.topPos + 53 + 18 >= mouseY) {
            graphics.blit(TEXTURE, this.leftPos + 52, this.topPos + 53, 176, 150, 18, 18);
        }
        if(this.leftPos + 97 <= mouseX && this.leftPos + 97 + 18 > mouseX && this.topPos + 107 < mouseY && this.topPos + 107 + 18 >= mouseY) {
            graphics.blit(TEXTURE, this.leftPos + 97, this.topPos + 107, 176, 168, 18, 18);
        }

        for(int i = this.index * 2; i < this.index * 2 + PAGE_SIZE; i++) {
            if(i >= this.recipes.size()) {
                break;
            }

            int ind = i - this.index * 2;
            VariantEntry entry = this.recipes.get(i);

            graphics.renderItem(entry.display, this.leftPos + 17 + 18 * (ind / 2), this.topPos + 72 + 18 * (ind % 2));

            graphics.blit(TEXTURE, this.leftPos + 16 + 18 * (ind / 2), this.topPos + 71 + 18 * (ind % 2), 18 + 18 * entry.overlay.ordinal(), 222, 18, 18);

            if(this.selection == i) {
                graphics.blit(TEXTURE, this.leftPos + 16 + 18 * (ind / 2), this.topPos + 71 + 18 * (ind % 2), 0, 222, 18, 18);
            }
        }

        if(this.selection >= 0 && this.selection < this.recipes.size()) {
            VariantEntry entry = this.recipes.get(this.selection);
            List<String> lines = entry.tooltipLines();
            int longest = 0;
            for(String line : lines) {
                longest = Math.max(longest, this.font.width(line));
            }

            graphics.pose().pushPose();
            graphics.pose().scale(0.5F, 0.5F, 0.5F);
            int offset = 0;
            for(String line : lines) {
                graphics.drawString(this.font, line, 260, 50 + offset, 0xFFFFFF, false);
                offset += 9;
            }
            graphics.pose().popPose();

            this.lastSize = (int)(longest * 0.5F);
        } else {
            this.lastSize = 0;
        }

        this.search.render(graphics, mouseX, mouseY, partialTick);

        for(int i = this.index * 2; i < this.index * 2 + PAGE_SIZE; i++) {
            if(i >= this.recipes.size()) {
                break;
            }

            int ind = i - this.index * 2;
            int ix = 16 + 18 * (ind / 2);
            int iy = 71 + 18 * (ind % 2);
            if(this.leftPos + ix <= mouseX && this.leftPos + ix + 18 > mouseX && this.topPos + iy < mouseY && this.topPos + iy + 18 >= mouseY) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(this.recipes.get(i).display.getHoverName());
                graphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if(this.leftPos <= mouseX && this.leftPos + IMAGE_WIDTH > mouseX && this.topPos < mouseY && this.topPos + IMAGE_HEIGHT >= mouseY) {
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

        if(this.leftPos + 7 <= mouseX && this.leftPos + 7 + 9 > mouseX && this.topPos + 71 < mouseY && this.topPos + 71 + 36 >= mouseY) {
            this.click();
            if(this.index > 0) {
                this.index--;
            }
            return true;
        }

        if(this.leftPos + 106 <= mouseX && this.leftPos + 106 + 9 > mouseX && this.topPos + 71 < mouseY && this.topPos + 71 + 36 >= mouseY) {
            this.click();
            if(this.index < this.size) {
                this.index++;
            }
            return true;
        }

        if(this.leftPos + 52 <= mouseX && this.leftPos + 52 + 18 > mouseX && this.topPos + 53 < mouseY && this.topPos + 53 + 18 >= mouseY) {
            if(this.selection >= 0) {
                this.click();
            }
            return true;
        }

        if(this.leftPos + 97 <= mouseX && this.leftPos + 97 + 18 > mouseX && this.topPos + 107 < mouseY && this.topPos + 107 + 18 >= mouseY) {
            this.click();
            this.search.setValue("");
            this.search(this.search.getValue());
            return true;
        }

        for(int i = this.index * 2; i < this.index * 2 + PAGE_SIZE; i++) {
            if(i >= this.recipes.size()) {
                break;
            }

            int ind = i - this.index * 2;
            int ix = 16 + 18 * (ind / 2);
            int iy = 71 + 18 * (ind % 2);
            if(this.leftPos + ix <= mouseX && this.leftPos + ix + 18 > mouseX && this.topPos + iy < mouseY && this.topPos + iy + 18 >= mouseY) {
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
        if(keyCode == GLFW.GLFW_KEY_ESCAPE || this.minecraft.options.keyInventory.isActiveAndMatches(key)) {
            this.onClose();
            return true;
        }

        if(this.search.keyPressed(keyCode, scanCode, modifiers)) {
            this.search(this.search.getValue());
            return true;
        }

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

    private void click() {
        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    private static final class VariantEntry {
        private final NTMAnvilBlock.Variant variant;
        private final ItemStack display;
        private final OverlayType overlay;

        private VariantEntry(NTMAnvilBlock.Variant variant) {
            this.variant = variant;
            this.display = MetaHelper.newStack(NtmBlocks.ANVIL.asItem(), variant.ordinal());
            this.overlay = switch(variant.tier) {
                case NTMAnvilBlock.TIER_IRON -> OverlayType.SMITHING;
                case NTMAnvilBlock.TIER_STEEL -> OverlayType.CONSTRUCTION;
                case NTMAnvilBlock.TIER_OIL, NTMAnvilBlock.TIER_NUCLEAR -> OverlayType.CONSTRUCTION;
                case NTMAnvilBlock.TIER_RBMK, NTMAnvilBlock.TIER_FUSION, NTMAnvilBlock.TIER_PARTICLE, NTMAnvilBlock.TIER_GERALD -> OverlayType.RECYCLING;
                default -> OverlayType.NONE;
            };
        }

        private boolean isTierValid(int tier) {
            return tier >= this.variant.tier;
        }

        private List<String> searchTerms() {
            List<String> terms = new ArrayList<>();
            terms.add(this.variant.name().toLowerCase(Locale.US));
            terms.add(("block.hbmsntm.anvil." + this.variant.name().toLowerCase(Locale.US)).toLowerCase(Locale.US));
            terms.add(Component.translatable("block.hbmsntm.anvil." + this.variant.name().toLowerCase(Locale.US)).getString().toLowerCase(Locale.US));
            terms.add(Integer.toString(this.variant.tier));
            return terms;
        }

        private List<String> tooltipLines() {
            List<String> lines = new ArrayList<>();
            lines.add(I18nUtil.resolveKey("info.template_in_p"));
            lines.add("> " + Component.translatable("block.hbmsntm.anvil." + this.variant.name().toLowerCase(Locale.US)).getString());
            lines.add("> " + this.variant.modelName);
            lines.add("");
            lines.add(I18nUtil.resolveKey("info.template_out_p"));
            lines.add("> " + I18nUtil.resolveKey("block.hbmsntm.anvil.tier", this.variant.tier));
            return lines;
        }
    }

    private enum OverlayType {
        NONE,
        CONSTRUCTION,
        RECYCLING,
        SMITHING
    }
}
