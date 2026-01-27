package com.hbm.inventory;

import com.hbm.HBMsNTM;
import com.hbm.inventory.menus.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, HBMsNTM.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<MachineSatLinkerMenu>> SAT_LINKER = reg("sat_linker", MachineSatLinkerMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<MachineFluidTankMenu>> FLUID_TANK = reg("fluid_tank", MachineFluidTankMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<BatterySocketMenu>> BATTERY_SOCKET = reg("battery_socket", BatterySocketMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<BatteryREDDMenu>> BATTERY_REDD = reg("battery_redd", BatteryREDDMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<NukeGadgetMenu>> NUKE_GADGET = reg("nuke_gadget", NukeGadgetMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<NukeLittleBoyMenu>> NUKE_LITTLE_BOY = reg("nuke_little_boy", NukeLittleBoyMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<NukeFatManMenu>> NUKE_FAT_MAN = reg("nuke_fat_man", NukeFatManMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> reg(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
