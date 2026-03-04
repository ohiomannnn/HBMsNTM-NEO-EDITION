package com.hbm.handler.jei;

import com.hbm.HBMsNTM;
import com.hbm.handler.jei.subtypes.CustomDataSubtypeInterpreter;
import com.hbm.handler.jei.subtypes.FluidTypeSubtypeInterpreter;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ModItems;
import com.hbm.items.datacomps.ModDataComponents;
import com.hbm.items.machine.FluidIconItem;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IExtraIngredientRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JeiPlugin
@SuppressWarnings("unused")
public class ModPlugin implements IModPlugin {

    @Override public ResourceLocation getPluginUid() { return HBMsNTM.withDefaultNamespaceNT("jei_plugin"); }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration regs) {

        List<Item> ignoreCD = List.of(
                // battery packs
                ModItems.BATTERY_PACK_REDSTONE.get(),
                ModItems.BATTERY_PACK_LEAD.get(),
                ModItems.BATTERY_PACK_LITHIUM.get(),
                ModItems.BATTERY_PACK_SODIUM.get(),
                ModItems.BATTERY_PACK_SCHRABIDIUM.get(),
                ModItems.BATTERY_PACK_QUANTUM.get(),
                // capacitors
                ModItems.CAPACITOR_COPPER.get(),
                ModItems.CAPACITOR_GOLD.get(),
                ModItems.CAPACITOR_NIOBIUM.get(),
                ModItems.CAPACITOR_TANTALUM.get(),
                ModItems.CAPACITOR_BISMUTH.get(),
                ModItems.CAPACITOR_SPARK.get()
        );

        List<Item> ignoreFT = List.of(
                ModItems.FLUID_TANK_FULL.get(),
                ModItems.FLUID_TANK_LEAD_FULL.get(),
                ModItems.FLUID_BARREL_FULL.get(),
                ModItems.FLUID_PACK_FULL.get(),

                ModItems.FLUID_ICON.get(),
                ModItems.FLUID_IDENTIFIER_MULTI.get()
        );

        for (Item item : ignoreCD) {
            regs.registerSubtypeInterpreter(item, CustomDataSubtypeInterpreter.INSTANCE);
        }
        for (Item item : ignoreFT) {
            regs.registerSubtypeInterpreter(item, FluidTypeSubtypeInterpreter.INSTANCE);
        }
    }

    @Override
    public void registerExtraIngredients(IExtraIngredientRegistration regs) {
        HashSet<ItemStack> extra = new HashSet<>();

        FluidType[] types = Fluids.getInNiceOrder();
        for (int i = 1; i < types.length; ++i) {
            FluidType type = types[i];

            extra.add(FluidIconItem.make(type, 1000));
        }

        regs.addExtraItemStacks(extra);
    }
}
