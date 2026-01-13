package com.hbm.inventory;

import com.hbm.HBMsNTM;
import com.hbm.inventory.menus.BatteryREDDMenu;
import com.hbm.inventory.menus.BatterySocketMenu;
import com.hbm.inventory.menus.MachineSatLinkerMenu;
import com.hbm.inventory.menus.NukeFatManMenu;
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

    public static final DeferredHolder<MenuType<?>, MenuType<MachineSatLinkerMenu>> SAT_LINKER = register("sat_linker", MachineSatLinkerMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<BatterySocketMenu>> BATTERY_SOCKET = register("battery_socket", BatterySocketMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<BatteryREDDMenu>> BATTERY_REDD = register("battery_redd", BatteryREDDMenu::new);

    // NUKES GOES HERE
    public static final DeferredHolder<MenuType<?>, MenuType<NukeFatManMenu>> NUKE_FATMAN = register("nuke_fatman", NukeFatManMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
