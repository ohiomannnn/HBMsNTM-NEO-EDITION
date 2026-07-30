package com.hbm.items.machine;

import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blocks.ITooltipProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class MachineUpgradeItem extends Item {

    public UpgradeType type;
    public int tier;

    public MachineUpgradeItem(Properties properties, UpgradeType type, int tier) {
        super(properties.stacksTo(1));
        this.type = type;
        this.tier = tier;
    }

    public MachineUpgradeItem(Properties properties, UpgradeType type) {
        this(properties, type, 0);
    }

    public MachineUpgradeItem(Properties properties) {
        this(properties, UpgradeType.SPECIAL, 0);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {

        Screen open = Minecraft.getInstance().screen;
        if(open instanceof AbstractContainerScreen<?> guiContainer) {
            AbstractContainerMenu menu = guiContainer.getMenu();
            if(!menu.slots.isEmpty()) {
                Slot first = menu.getSlot(0);
                Container inv = first.container;
                if(inv instanceof IUpgradeInfoProvider provider) {
                    if(provider.canProvideInfo(this.type, this.tier, flag)) {
                        provider.provideInfo(this.type, this.tier, components, flag);
                        return;
                    }
                }
            }
        }

        String[] lines = ITooltipProvider.getDescriptionOrNull(stack);
        if(lines != null) {
            for(String line : lines) {
                components.add(Component.translatable(line).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    public enum UpgradeType {
        SPEED,
        EFFECT,
        POWER,
        FORTUNE,
        AFTERBURN,
        OVERDRIVE,
        SPECIAL,
        LM_DESROYER,
        LM_SCREM,
        LM_SMELTER(true),
        LM_SHREDDER(true),
        LM_CENTRIFUGE(true),
        LM_CRYSTALLIZER(true),
        GS_SPEED;

        public boolean mutex = false;

        UpgradeType() { }

        UpgradeType(boolean mutex) {
            this.mutex = mutex;
        }
    }
}
