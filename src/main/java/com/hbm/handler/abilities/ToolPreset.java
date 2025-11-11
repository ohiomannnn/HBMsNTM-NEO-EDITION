package com.hbm.handler.abilities;

import com.hbm.util.ChatBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

public class ToolPreset {
    public IToolAreaAbility areaAbility = IToolAreaAbility.NONE;
    public int areaAbilityLevel = 0;
    public IToolHarvestAbility harvestAbility = IToolHarvestAbility.NONE;
    public int harvestAbilityLevel = 0;

    public ToolPreset() { }

    public ToolPreset(IToolAreaAbility areaAbility, IToolHarvestAbility harvestAbility) {
        this.areaAbility = areaAbility;
        this.harvestAbility = harvestAbility;
    }

    public ToolPreset(IToolAreaAbility areaAbility, int areaAbilityLevel, IToolHarvestAbility harvestAbility, int harvestAbilityLevel) {
        this.areaAbility = areaAbility;
        this.areaAbilityLevel = areaAbilityLevel;
        this.harvestAbility = harvestAbility;
        this.harvestAbilityLevel = harvestAbilityLevel;
    }


    public Component getMessage() {
        if (isNone()) {
            return ChatBuilder.start("[Tool ability deactivated]").color(ChatFormatting.GOLD).flush();
        }

        boolean hasArea = areaAbility != IToolAreaAbility.NONE;
        boolean hasHarvest = harvestAbility != IToolHarvestAbility.NONE;

        ChatBuilder builder = ChatBuilder.start("[Enabled ");

        if (hasArea) {
            builder.nextTranslation(areaAbility.getName());
            builder.next(areaAbility.getExtension(areaAbilityLevel));
        }

        if(hasArea && hasHarvest) {
            builder.next(" + ");
        }

        if(hasHarvest) {
            builder.nextTranslation(harvestAbility.getName());
            builder.next(harvestAbility.getExtension(harvestAbilityLevel));
        }

        return builder.colorAll(ChatFormatting.YELLOW).flush();
    }


    public boolean isNone() {
        return areaAbility == IToolAreaAbility.NONE && harvestAbility == IToolHarvestAbility.NONE;
    }

    public void writeToNBT(CompoundTag nbt) {
        nbt.putString("area", areaAbility.getName());
        nbt.putInt("areaLevel", areaAbilityLevel);
        nbt.putString("harvest", harvestAbility.getName());
        nbt.putInt("harvestLevel", harvestAbilityLevel);
    }

    public void readFromNBT(CompoundTag nbt) {
        areaAbility = IToolAreaAbility.getByName(nbt.getString("area"));
        areaAbilityLevel = nbt.getInt("areaLevel");
        harvestAbility = IToolHarvestAbility.getByName(nbt.getString("harvest"));
        harvestAbilityLevel = nbt.getInt("harvestLevel");

        areaAbilityLevel = Math.min(areaAbilityLevel, areaAbility.levels() - 1);
        harvestAbilityLevel = Math.min(harvestAbilityLevel, harvestAbility.levels() - 1);
    }

    public void restrictTo(AvailableAbilities availableAbilities) {
        int maxAreaLevel = availableAbilities.maxLevel(areaAbility);

        if(maxAreaLevel == -1) {
            areaAbility = IToolAreaAbility.NONE;
            areaAbilityLevel = 0;
        } else if(areaAbilityLevel > maxAreaLevel) {
            areaAbilityLevel = maxAreaLevel;
        } else if(areaAbilityLevel < 0) {
            areaAbilityLevel = 0;
        }

        if (!areaAbility.allowsHarvest(areaAbilityLevel)) {
            harvestAbility = IToolHarvestAbility.NONE;
            harvestAbilityLevel = 0;
        }

        int maxHarvestLevel = availableAbilities.maxLevel(harvestAbility);

        if (maxHarvestLevel == -1) {
            harvestAbility = IToolHarvestAbility.NONE;
            harvestAbilityLevel = 0;
        } else if (harvestAbilityLevel > maxHarvestLevel) {
            harvestAbilityLevel = maxHarvestLevel;
        } else if (harvestAbilityLevel < 0) {
            harvestAbilityLevel = 0;
        }
    }
}
