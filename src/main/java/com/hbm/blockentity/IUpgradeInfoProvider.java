package com.hbm.blockentity;

import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.util.ChatBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.List;

public interface IUpgradeInfoProvider {

    /** If any of the automated display stuff should be applied for this upgrade. A level of 0 is used by the GUI's indicator, as opposed to the item tooltips */
    boolean canProvideInfo(UpgradeType type, int lvl, TooltipFlag flag);
    void provideInfo(UpgradeType type, int lvl, List<Component> components, TooltipFlag flag);
    HashMap<UpgradeType, Integer> getValidUpgrades();

    static Component getStandardLabel(Block block) {
        return ChatBuilder.start(">>> ").nextTranslation(block.getDescriptionId()).next(" <<<").colorAll(ChatFormatting.YELLOW).flush();
    }

    String KEY_ACID = "upgrade.acid";
    String KEY_BURN = "upgrade.burn";
    String KEY_CONSUMPTION = "upgrade.consumption";
    String KEY_COOLANT_CONSUMPTION = "upgrade.coolant_consumption";
    String KEY_DELAY = "upgrade.delay";
    String KEY_SPEED = "upgrade.speed";
    String KEY_EFFICIENCY = "upgrade.efficiency";
    String KEY_PRODUCTIVITY = "upgrade.productivity";
    String KEY_FORTUNE = "upgrade.fortune";
    String KEY_RANGE = "upgrade.range";
    String KEY_YES = "upgrade.yes";
}
