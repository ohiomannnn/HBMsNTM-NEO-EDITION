package com.hbm.handler.jei;

import com.hbm.handler.jei.subtypes.BatterySubtypeInterpreter;
import com.hbm.handler.jei.subtypes.MetaSubtypeInterpreter;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.FluidIconItem;
import com.hbm.main.NuclearTechMod;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IExtraIngredientRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
@SuppressWarnings("unused")
public class ModPlugin implements IModPlugin {

    @Override public ResourceLocation getPluginUid() { return NuclearTechMod.withDefaultNamespace("jei_plugin"); }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration regs) {

        List<Item> ignoreMeta = List.of(

                NtmItems.BATTERY_SC.get(),

                NtmItems.FLUID_TANK_FULL.get(),
                NtmItems.FLUID_TANK_LEAD_FULL.get(),
                NtmItems.FLUID_BARREL_FULL.get(),
                NtmItems.FLUID_PACK_FULL.get(),

                NtmItems.FLUID_ICON.get(),
                NtmItems.FLUID_IDENTIFIER_MULTI.get()
        );

        for (Item item : ignoreMeta) {
            regs.registerSubtypeInterpreter(item, MetaSubtypeInterpreter.INSTANCE);
        }
//        for (Item item : ignoreCD) {
//            regs.registerSubtypeInterpreter(item, CustomDataSubtypeInterpreter.INSTANCE);
//        }

        regs.registerSubtypeInterpreter(NtmItems.BATTERY_PACK.get(), BatterySubtypeInterpreter.INSTANCE);
    }

    @Override
    public void registerExtraIngredients(IExtraIngredientRegistration regs) {
        List<ItemStack> extra = new ArrayList<>();

        FluidType[] types = Fluids.getInNiceOrder();
        for (int i = 1; i < types.length; ++i) {
            FluidType type = types[i];

            extra.add(FluidIconItem.make(type, 1000));
        }

        regs.addExtraItemStacks(extra);
    }
}
