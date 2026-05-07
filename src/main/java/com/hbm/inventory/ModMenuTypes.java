package com.hbm.inventory;

import com.hbm.main.NuclearTechMod;
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
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, NuclearTechMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<MachineSatLinkerMenu>> SAT_LINKER = reg("sat_linker", MachineSatLinkerMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<MachineFluidTankMenu>> FLUID_TANK = reg("fluid_tank", MachineFluidTankMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<BatterySocketMenu>> BATTERY_SOCKET = reg("battery_socket", BatterySocketMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<BatteryREDDMenu>> BATTERY_REDD = reg("battery_redd", BatteryREDDMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<NukeGadgetMenu>> NUKE_GADGET = reg("nuke_gadget", NukeGadgetMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<NukeLittleBoyMenu>> NUKE_LITTLE_BOY = reg("nuke_little_boy", NukeLittleBoyMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<NukeFatManMenu>> NUKE_FAT_MAN = reg("nuke_fat_man", NukeFatManMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<NukeIvyMikeMenu>> NUKE_IVY_MIKE = reg("nuke_ivy_mike", NukeIvyMikeMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<NukeTsarBombaMenu>> NUKE_TSAR_BOMBA = reg("nuke_tsar_bomba", NukeTsarBombaMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<NukePrototypeMenu>> NUKE_PROTOTYPE = reg("nuke_prototype", NukePrototypeMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<NukeFleijaMenu>> NUKE_FLEIJA = reg("nuke_fleija", NukeFleijaMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<NukeN2Menu>> NUKE_N2 = reg("nuke_n2", NukeN2Menu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<NukeFstbmbMenu>> NUKE_FSTBMB = reg("nuke_fstbmb", NukeFstbmbMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<LaunchPadLargeMenu>> LAUNCH_PAD_LARGE = reg("launch_pad_large", LaunchPadLargeMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> reg(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
