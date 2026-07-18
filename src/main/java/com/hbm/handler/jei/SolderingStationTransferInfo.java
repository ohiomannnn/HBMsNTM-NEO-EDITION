package com.hbm.handler.jei;

import com.hbm.inventory.menus.MachineSolderingStationMenu;
import com.hbm.inventory.recipes.SolderingRecipes.SolderingRecipe;
import com.hbm.inventory.NtmMenuTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SolderingStationTransferInfo implements IRecipeTransferInfo<MachineSolderingStationMenu, SolderingRecipe> {

    @Override
    public Class<? extends MachineSolderingStationMenu> getContainerClass() {
        return MachineSolderingStationMenu.class;
    }

    @Override
    public Optional<MenuType<MachineSolderingStationMenu>> getMenuType() {
        return Optional.of(NtmMenuTypes.MACHINE_SOLDERING_STATION.get());
    }

    @Override
    public RecipeType<SolderingRecipe> getRecipeType() {
        return SolderingStationRecipeHandler.RECIPE_TYPE;
    }

    @Override
    public boolean canHandle(MachineSolderingStationMenu container, SolderingRecipe recipe) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(MachineSolderingStationMenu container, SolderingRecipe recipe) {
        List<Slot> slots = new ArrayList<>(6);
        for(int i = 0; i < 6; i++) {
            slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(MachineSolderingStationMenu container, SolderingRecipe recipe) {
        return new ArrayList<>(container.slots.subList(11, container.slots.size()));
    }
}
