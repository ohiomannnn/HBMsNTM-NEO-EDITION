package com.hbm.inventory.gui;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.handler.abilities.*;
import com.hbm.items.tools.ToolAbilityItem;
import com.hbm.network.toserver.CompoundTagItemControl;
import com.hbm.util.TagsUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class ToolAbilityScreen extends Screen {

    public static ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID,"textures/gui/tool/gui_tool_ability.png");
    private static final Minecraft mc = Minecraft.getInstance();

    protected int guiLeft;
    protected int guiTop;
    protected int xSize;
    protected int ySize;
    protected int insetWidth;

    public static class AbilityInfo {
        public IBaseAbility ability;
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
        this.toolStack = mc.player.getMainHandItem();

        if (this.toolStack.isEmpty()) doClose();

        this.config = ((ToolAbilityItem) this.toolStack.getItem()).getConfiguration(this.toolStack);

        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        drawStretchedRect(guiGraphics, guiLeft, guiTop, 0, 0, xSize, xSize - insetWidth, ySize, 74, 87);

        ToolPreset activePreset = config.getActivePreset();
        hoverIdxArea = drawSwitches(guiGraphics, abilitiesArea, activePreset.areaAbility, activePreset.areaAbilityLevel, guiLeft + 15, guiTop + 25, mouseX, mouseY);
        hoverIdxHarvest = drawSwitches(guiGraphics, abilitiesHarvest, activePreset.harvestAbility, activePreset.harvestAbilityLevel, guiLeft + 15, guiTop + 45, mouseX, mouseY);
    }

    protected void drawStretchedRect(GuiGraphics guiGraphics, int x, int y, int u, int v, int realWidth, int width, int height, int keepLeft, int keepRight) {
        int midWidth = width - keepLeft - keepRight;
        int realMidWidth = realWidth - keepLeft - keepRight;
        guiGraphics.blit(texture, x, y, u, v, keepLeft, height);
        for(int i = 0; i < realMidWidth; i += midWidth) {
            guiGraphics.blit(texture, x + keepLeft + i, y, u + keepLeft, v, Math.min(midWidth, realMidWidth - i), height);
        }
        guiGraphics.blit(texture, + keepLeft + realMidWidth, y, u + keepLeft + midWidth, v, keepRight, height);
    }

    protected int drawSwitches(GuiGraphics guiGraphics, List<AbilityInfo> abilities, IBaseAbility selectedAbility, int selectedLevel, int x, int y, int mouseX, int mouseY) {
        int hoverIdx = -1;

        for(int i = 0; i < abilities.size(); ++i) {
            AbilityInfo abilityInfo = abilities.get(i);
            boolean available = abilityAvailable(abilityInfo.ability);
            boolean selected = abilityInfo.ability == selectedAbility;

            // Draw switch
            guiGraphics.blit(texture, x + 20 * i, y, abilityInfo.textureU + (available ? 16 : 0), abilityInfo.textureV, 16, 16);

            // Draw level LEDs
            if(abilityInfo.ability.levels() > 1) {
                int level = 0;

                if(selected) {
                    level = selectedLevel + 1;
                }

                // Note: only visual effect for the LEDs
                // int maxLevel = Math.min(abilityInfo.ability.levels(), 5);
                int maxLevel = 5;

                if(level > 10 || level < 0) {
                    // All-red LEDs for invalid levels
                    level = -1;
                }

                guiGraphics.blit(texture,  x + 20 * i + 17, y + 1, 188 + level * 2, maxLevel * 14, 2, 14);
            }

            boolean isHovered = isInAABB(mouseX, mouseY, x + 20 * i, y, 16, 16);

            if(isHovered) {
                hoverIdx = i;
            }

            if(selected) {
                // Draw selection highlight
                guiGraphics.blit(texture, x + 20 * i - 1, y - 1, 220, 9, 18, 18);
            } else if(available && isHovered) {
                // Draw hover highlight
                guiGraphics.blit(texture, x + 20 * i - 1, y - 1, 238, 9, 18, 18);
            }
        }

        return hoverIdx;
    }

    private boolean isInAABB(int mouseX, int mouseY, int x, int y, int width, int height) {
        return x <= mouseX && x + width > mouseX && y <= mouseY && y + height > mouseY;
    }

    private boolean abilityAvailable(IBaseAbility ability) {
        if (!availableAbilities.supportsAbility(ability)) {
            return false;
        }

        ToolPreset activePreset = config.getActivePreset();
        return !(ability instanceof IToolHarvestAbility) || ability == IToolHarvestAbility.NONE || activePreset.areaAbility.allowsHarvest(activePreset.areaAbilityLevel);
    }

    protected void doClose() {
        // A bit messy, but I suppose it works
        ((ToolAbilityItem) this.toolStack.getItem()).setConfiguration(toolStack, config);
        PacketDistributor.sendToServer(new CompoundTagItemControl(TagsUtil.getTag(this.toolStack)));

        mc.screen = null;

        HBMsNTMClient.displayTooltip(config.getActivePreset().getMessage(), 1000, HBMsNTMClient.ID_TOOLABILITY);

        mc.level.playSound(null, mc.player.getX(), mc.player.getY(), mc.player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.25F, config.getActivePreset().isNone() ? 0.75F : 1.25F);
    }
}
