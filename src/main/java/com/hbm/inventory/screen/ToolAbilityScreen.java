package com.hbm.inventory.screen;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.handler.ability.*;
import com.hbm.items.tools.ToolAbilityItem;
import com.hbm.lib.ModSounds;
import com.hbm.network.toserver.CompoundTagItemControl;
import com.hbm.util.TagsUtil;
import com.hbm.util.Tuple.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class ToolAbilityScreen extends Screen {

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/gui/tool/gui_tool_ability.png");

    protected int guiLeft;
    protected int guiTop;
    protected int xSize;
    protected int ySize;
    protected int insetWidth;

    public static class AbilityInfo {
        public final IBaseAbility ability;
        public int textureU, textureV;

        public AbilityInfo(IBaseAbility ability, int textureU, int textureV) {
            this.ability = ability;
            this.textureU = textureU;
            this.textureV = textureV;
        }
    }

    public static final List<AbilityInfo> abilitiesArea = new ArrayList<>();
    public static final List<AbilityInfo> abilitiesHarvest = new ArrayList<>();

    static {
        abilitiesArea.add(new AbilityInfo(IToolAreaAbility.NONE, 0, 91));
        abilitiesArea.add(new AbilityInfo(IToolAreaAbility.RECURSION, 32, 91));
        abilitiesArea.add(new AbilityInfo(IToolAreaAbility.HAMMER, 64, 91));
        abilitiesArea.add(new AbilityInfo(IToolAreaAbility.HAMMER_FLAT, 96, 91));
        abilitiesArea.add(new AbilityInfo(IToolAreaAbility.EXPLOSION, 128, 91));

        abilitiesHarvest.add(new AbilityInfo(IToolHarvestAbility.NONE, 0, 107));
        abilitiesHarvest.add(new AbilityInfo(IToolHarvestAbility.SILK, 32, 107));
        abilitiesHarvest.add(new AbilityInfo(IToolHarvestAbility.LUCK, 64, 107));
        abilitiesHarvest.add(new AbilityInfo(IToolHarvestAbility.SMELTER, 96, 107));
        abilitiesHarvest.add(new AbilityInfo(IToolHarvestAbility.SHREDDER, 128, 107));
        abilitiesHarvest.add(new AbilityInfo(IToolHarvestAbility.CENTRIFUGE, 160, 107));
        abilitiesHarvest.add(new AbilityInfo(IToolHarvestAbility.CRYSTALLIZER, 192, 107));
        abilitiesHarvest.add(new AbilityInfo(IToolHarvestAbility.MERCURY, 224, 107));
    }

    protected ItemStack toolStack;
    protected AvailableAbilities availableAbilities;
    protected ToolAbilityItem.Configuration config;

    protected int hoverIdxHarvest = -1;
    protected int hoverIdxArea = -1;
    protected int hoverIdxExtraBtn = -1;

    public ToolAbilityScreen(AvailableAbilities availableAbilities) {
        super(Component.empty());
        this.availableAbilities = availableAbilities;

        this.xSize = 186; // Note: increased dynamically
        this.ySize = 76;

        this.insetWidth = 20 * Math.max(abilitiesArea.size() - 4, abilitiesHarvest.size() - 8);
        this.xSize += insetWidth;
    }

    @Override
    protected void init() {
        this.toolStack = Minecraft.getInstance().player.getMainHandItem();

        if (toolStack.isEmpty()) {
            onClose();
        }

        this.config = ((ToolAbilityItem) toolStack.getItem()).getConfiguration(toolStack);

        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partial);

        // Draw window background
        blitStretched(guiGraphics, guiLeft, guiTop, 0, 0, xSize, xSize - insetWidth, ySize, 74, 87);

        // Draw the switches
        ToolPreset activePreset = config.getActivePreset();
        hoverIdxArea = blitSwitches(guiGraphics, abilitiesArea, activePreset.areaAbility, activePreset.areaAbilityLevel, guiLeft + 15, guiTop + 25, mouseX, mouseY);
        hoverIdxHarvest = blitSwitches(guiGraphics, abilitiesHarvest, activePreset.harvestAbility, activePreset.harvestAbilityLevel, guiLeft + 15, guiTop + 45, mouseX, mouseY);

        // Draw preset indicator
        drawNumber(guiGraphics, config.currentPreset + 1, guiLeft + insetWidth + 115, guiTop + 25);
        drawNumber(guiGraphics, config.presets.size(), guiLeft + insetWidth + 149, guiTop + 25);

        // Draw extra buttons hover highlights
        int extraX = guiLeft + xSize - 86;

        hoverIdxExtraBtn = -1;
        for (int i = 0; i < 7; i++) {
            if (isInAABB(mouseX, mouseY, extraX + i * 11, guiTop + 11, 9, 9)) {
                hoverIdxExtraBtn = i;
                guiGraphics.blit(TEXTURE, extraX + i * 11, guiTop + 11, 193 + i * 9, 0, 9, 9);
            }
        }

        String tooltipValue  = "";

        if (hoverIdxArea != -1) {
            int level = 0;
            if (abilitiesArea.get(hoverIdxArea).ability == activePreset.areaAbility) {
                level = activePreset.areaAbilityLevel;
            }
            tooltipValue = abilitiesArea.get(hoverIdxArea).ability.getFullName(level);
        } else if (hoverIdxHarvest != -1) {
            int level = 0;
            if (abilitiesHarvest.get(hoverIdxHarvest).ability == activePreset.harvestAbility) {
                level = activePreset.harvestAbilityLevel;
            }
            tooltipValue = abilitiesHarvest.get(hoverIdxHarvest).ability.getFullName(level);
        } else if (hoverIdxExtraBtn != -1) {
            tooltipValue = switch (hoverIdxExtraBtn) {
                case 0 -> "Reset all presets";
                case 1 -> "Delete current preset";
                case 2 -> "Add new preset";
                case 3 -> "Select first preset";
                case 4 -> "Next preset";
                case 5 -> "Previous preset";
                case 6 -> "Close window";
                default -> tooltipValue;
            };
        }
        if (!tooltipValue.isEmpty()) {
            int tooltipWidth = Math.max(6, font.width(tooltipValue));
            int tooltipX = guiLeft + xSize / 2 - tooltipWidth / 2;
            int tooltipY = guiTop + ySize + 1 + 4;

            blitStretched(guiGraphics, tooltipX - 5, tooltipY - 4, 0, 76, tooltipWidth + 10, 186, 15, 3, 3);
            guiGraphics.drawString(font, tooltipValue, tooltipX, tooltipY, 0xffffffff);
        }
    }

    private void blitStretched(GuiGraphics guiGraphics, int x, int y, int u, int v, int realWidth, int width, int height, int keepLeft, int keepRight) {
        int midWidth = width - keepLeft - keepRight;
        int realMidWidth = realWidth - keepLeft - keepRight;
        guiGraphics.blit(TEXTURE, x, y, u, v, keepLeft, height);
        for (int i = 0; i < realMidWidth; i += midWidth) {
            guiGraphics.blit(TEXTURE, x + keepLeft + i, y, u + keepLeft, v, Math.min(midWidth, realMidWidth - i), height);
        }
        guiGraphics.blit(TEXTURE, x + keepLeft + realMidWidth, y, u + keepLeft + midWidth, v, keepRight, height);
    }

    private int blitSwitches(GuiGraphics guiGraphics, List<AbilityInfo> abilities, IBaseAbility selectedAbility, int selectedLevel, int x, int y, int mouseX, int mouseY) {
        int hoverIdx = -1;

        for (int i = 0; i < abilities.size(); i++) {
            AbilityInfo abilityInfo = abilities.get(i);
            boolean available = abilityAvailable(abilityInfo.ability);
            boolean selected = abilityInfo.ability == selectedAbility;

            guiGraphics.blit(TEXTURE, x + 20 * i, y, abilityInfo.textureU + (available ? 16 : 0), abilityInfo.textureV, 16, 16);

            // Draw level LEDs
            if (abilityInfo.ability.levels() > 1) {
                int level = 0;

                if (selected) {
                    level = selectedLevel + 1;
                }

                // Note: only visual effect for the LEDs
                // int maxLevel = Math.min(abilityInfo.ability.levels(), 5);
                int maxLevel = 5;

                if (level > 10 || level < 0) {
                    // All-red LEDs for invalid levels
                    level = -1;
                }

                guiGraphics.blit(TEXTURE, x + 20 * i + 17, y + 1, 188 + level * 2, maxLevel * 14, 2, 14);
            }

            boolean isHovered = isInAABB(mouseX, mouseY, x + 20 * i, y, 16, 16);
            if (isHovered) {
                hoverIdx = i;
            }

            if (selected) {
                guiGraphics.blit(TEXTURE, x + 20 * i - 1, y - 1, 220, 9, 18, 18);
            } else if (available && isHovered) {
                guiGraphics.blit(TEXTURE, x + 20 * i - 1, y - 1, 238, 9, 18, 18);
            }
        }

        return hoverIdx;
    }

    private void drawNumber(GuiGraphics g, int n, int x, int y) {
        n += 100; // Against accidental negatives
        drawDigit(g, (n / 10) % 10, x, y);
        drawDigit(g, n % 10, x + 12, y);
    }

    private void drawDigit(GuiGraphics g, int d, int x, int y) {
        g.blit(TEXTURE, x, y, d * 10, 123, 10, 15);
    }

    private boolean isInAABB(int mouseX, int mouseY, int x, int y, int width, int height) {
        return x <= mouseX && x + width > mouseX && y <= mouseY && y + height > mouseY;
    }

    private boolean abilityAvailable(IBaseAbility ability) {
        if (!availableAbilities.supportsAbility(ability)) {
            return false;
        }

        ToolPreset activePreset = config.getActivePreset();
        if (ability instanceof IToolHarvestAbility && ability != IToolHarvestAbility.NONE && !activePreset.areaAbility.allowsHarvest(activePreset.areaAbilityLevel)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {

        if (scrollY < 0) doPrevPreset(true);
        if (scrollY > 0) doNextPreset(true);

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        ToolPreset activePreset = config.getActivePreset();
        int mouseX = (int) x;
        int mouseY = (int) y;

        // Process switches
        Pair<IBaseAbility, Integer> clickResult;

        clickResult = handleSwitchesClicked(abilitiesArea, activePreset.areaAbility, activePreset.areaAbilityLevel, hoverIdxArea, mouseX, mouseY);
        activePreset.areaAbility = (IToolAreaAbility) clickResult.key;
        activePreset.areaAbilityLevel = clickResult.value;

        clickResult = handleSwitchesClicked(abilitiesHarvest, activePreset.harvestAbility, activePreset.harvestAbilityLevel, hoverIdxHarvest, mouseX, mouseY);
        activePreset.harvestAbility = (IToolHarvestAbility) clickResult.key;
        activePreset.harvestAbilityLevel = clickResult.value;

        if (!activePreset.areaAbility.allowsHarvest(activePreset.areaAbilityLevel)) {
            activePreset.harvestAbility = IToolHarvestAbility.NONE;
            activePreset.harvestAbilityLevel = 0;
        }

        // Process extra buttons
        if (hoverIdxExtraBtn != -1) {
            switch (hoverIdxExtraBtn) {
                case 0 -> doResetPresets();
                case 1 -> doDelPreset();
                case 2 -> doAddPreset();
                case 3 -> doZeroPreset();
                case 4 -> doNextPreset(false);
                case 5 -> doPrevPreset(false);
                case 6 -> onClose();
            }

            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.5F));
        }

        // Allow quick-closing
        if (!isInAABB(mouseX, mouseY, guiLeft, guiTop, xSize, ySize)) {
            onClose();
        }

        return super.mouseClicked(x, y, button);
    }

    private Pair<IBaseAbility, Integer> handleSwitchesClicked(List<AbilityInfo> abilities, IBaseAbility selectedAbility, int selectedLevel, int hoverIdx, int mouseX, int mouseY) {
        if (hoverIdx != -1) {
            IBaseAbility hoveredAbility = abilities.get(hoverIdx).ability;
            boolean available = abilityAvailable(hoveredAbility);

            if (available) {
                int availableLevels = availableAbilities.maxLevel(hoveredAbility) + 1;

                if (hoveredAbility != selectedAbility || availableLevels > 1) {
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.TECH_BOOP.get(), 2.0F));
                }

                if (hoveredAbility == selectedAbility) {
                    selectedLevel = (selectedLevel + 1) % availableLevels;
                } else {
                    selectedLevel = 0;
                }

                selectedAbility = hoveredAbility;
            }
        }

        return new Pair<>(selectedAbility, selectedLevel);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected void doResetPresets() {
        config.reset(availableAbilities);
    }

    protected void doDelPreset() {
        if (config.presets.size() <= 1) {
            return;
        }
        config.presets.remove(config.currentPreset);
        config.currentPreset = Math.min(config.currentPreset, config.presets.size() - 1);
    }

    protected void doAddPreset() {
        if (config.presets.size() >= 99) {
            return;
        }

        config.presets.add(config.currentPreset + 1, new ToolPreset());
        config.currentPreset += 1;
    }

    protected void doZeroPreset() {
        config.currentPreset = 0;
    }

    protected void doNextPreset(boolean bound) {
        if (bound) {
            if (config.currentPreset < config.presets.size() - 1) {
                config.currentPreset += 1;
            }
        } else {
            config.currentPreset = (config.currentPreset + 1) % config.presets.size();
        }
    }

    protected void doPrevPreset(boolean bound) {
        if (bound) {
            if (config.currentPreset > 0) {
                config.currentPreset -= 1;
            }
        } else {
            config.currentPreset = (config.currentPreset + config.presets.size() - 1) % config.presets.size();
        }
    }

    @Override
    public void onClose() {
        super.onClose();

        LocalPlayer player = this.minecraft.player;
        ((ToolAbilityItem) this.toolStack.getItem()).setConfiguration(toolStack, config);
        PacketDistributor.sendToServer(new CompoundTagItemControl(TagsUtil.getTag(this.toolStack)));

        HBMsNTMClient.displayTooltip(config.getActivePreset().getMessage(), 1000, HBMsNTMClient.ID_TOOLABILITY);

        this.minecraft.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.25F, config.getActivePreset().isNone() ? 0.75F : 1.25F);
    }
}
