package com.hbm.handler.jei;

import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.menus.MachineAssemblyMachineMenu;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssemblyMachineTransferInfo implements IRecipeTransferInfo<MachineAssemblyMachineMenu, GenericRecipe> {

    @Override
    public Class<? extends MachineAssemblyMachineMenu> getContainerClass() {
        return MachineAssemblyMachineMenu.class;
    }

    @Override
    public Optional<MenuType<MachineAssemblyMachineMenu>> getMenuType() {
        return Optional.of(NtmMenuTypes.ASSEMBLY_MACHINE.get());
    }

    @Override
    public RecipeType<GenericRecipe> getRecipeType() {
        return AssemblyMachineRecipeHandler.RECIPE_TYPE;
    }

    @Override
    public boolean canHandle(MachineAssemblyMachineMenu container, GenericRecipe recipe) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(MachineAssemblyMachineMenu container, GenericRecipe recipe) {
        List<Slot> slots = new ArrayList<>(12);
        for(int i = 4; i < 16; i++) {
            slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(MachineAssemblyMachineMenu container, GenericRecipe recipe) {
        return new ArrayList<>(container.slots.subList(17, container.slots.size()));
    }
}
