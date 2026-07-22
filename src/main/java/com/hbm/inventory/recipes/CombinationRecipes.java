package com.hbm.inventory.recipes;

import com.hbm.inventory.FluidStack;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.RecipesCommon.TagStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.FluidIconItem;
import com.hbm.util.Tuple;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CombinationRecipes {

    private static final LinkedHashMap<AStack, Tuple.Pair<ItemStack, FluidStack>> RECIPES = new LinkedHashMap<>();
    private static boolean defaultsRegistered = false;

    public static void registerDefaults() {
        if(defaultsRegistered) return;
        defaultsRegistered = true;

        add(new ComparableStack(Items.COAL), new ItemStack(NtmItems.COKE_COAL.get()), fluid(Fluids.COALCREOSOTE, 100));
        add(new ComparableStack(NtmItems.POWDER_COAL.get()), new ItemStack(NtmItems.COKE_COAL.get()), fluid(Fluids.COALCREOSOTE, 100));
        add(new ComparableStack(NtmItems.BRIQUETTE_COAL.get()), new ItemStack(NtmItems.COKE_COAL.get()), fluid(Fluids.COALCREOSOTE, 150));

        add(new ComparableStack(NtmItems.LIGNITE.get()), new ItemStack(NtmItems.COKE_LIGNITE.get()), fluid(Fluids.COALCREOSOTE, 50));
        add(new ComparableStack(NtmItems.POWDER_LIGNITE.get()), new ItemStack(NtmItems.COKE_LIGNITE.get()), fluid(Fluids.COALCREOSOTE, 50));
        add(new ComparableStack(NtmItems.BRIQUETTE_LIGNITE.get()), new ItemStack(NtmItems.COKE_LIGNITE.get()), fluid(Fluids.COALCREOSOTE, 100));

        add(new ComparableStack(NtmItems.POWDER_CHLOROCALCITE.get()), new ItemStack(NtmItems.POWDER_CALCIUM.get()), fluid(Fluids.CHLORINE, 250));
        add(new ComparableStack(NtmItems.POWDER_MOLYSITE.get()), new ItemStack(Items.IRON_INGOT), fluid(Fluids.CHLORINE, 250));
        add(new ComparableStack(NtmItems.CINNABAR.get()), new ItemStack(NtmItems.SULFUR.get()), fluid(Fluids.MERCURY, 100));
        add(new ComparableStack(Items.GLOWSTONE_DUST), new ItemStack(NtmItems.SULFUR.get()), fluid(Fluids.CHLORINE, 100));
        add(new ComparableStack(NtmItems.GEM_SODALITE.get()), new ItemStack(NtmItems.POWDER_SODIUM.get()), fluid(Fluids.CHLORINE, 100));
        add(new ComparableStack(NtmItems.CHUNK_CRYOLITE.get()), new ItemStack(NtmItems.POWDER_ALUMINIUM.get()), fluid(Fluids.LYE, 150));
        add(new ComparableStack(NtmItems.POWDER_SODIUM.get()), ItemStack.EMPTY, fluid(Fluids.SODIUM, 100));
        add(new ComparableStack(NtmItems.POWDER_LIMESTONE.get()), new ItemStack(NtmItems.POWDER_CALCIUM.get()), fluid(Fluids.CARBONDIOXIDE, 50));

        add(new TagStack(ItemTags.LOGS), new ItemStack(Items.CHARCOAL), fluid(Fluids.WOODOIL, 250));
        add(new TagStack(ItemTags.SAPLINGS), new ItemStack(NtmItems.POWDER_ASH_WOOD.get()), fluid(Fluids.WOODOIL, 50));
        add(new ComparableStack(NtmItems.BRIQUETTE_WOOD.get()), new ItemStack(Items.CHARCOAL), fluid(Fluids.WOODOIL, 500));

        add(new ComparableStack(NtmItems.OIL_TAR_CRUDE.get()), new ItemStack(NtmItems.COKE_PETROLEUM.get()), null);
        add(new ComparableStack(NtmItems.OIL_TAR_CRACK.get()), new ItemStack(NtmItems.COKE_PETROLEUM.get()), null);
        add(new ComparableStack(NtmItems.OIL_TAR_COAL.get()), new ItemStack(NtmItems.COKE_COAL.get()), null);
        add(new ComparableStack(NtmItems.OIL_TAR_WOOD.get()), new ItemStack(NtmItems.COKE_COAL.get()), null);
        add(new ComparableStack(NtmItems.OIL_TAR_WAX.get()), new ItemStack(NtmItems.COKE_PETROLEUM.get()), null);

        add(new ComparableStack(Items.SUGAR_CANE), new ItemStack(Items.SUGAR, 2), fluid(Fluids.ETHANOL, 50));
        add(new ComparableStack(Blocks.CLAY), new ItemStack(Blocks.BRICKS), null);
    }

    public static boolean isItemValid(ItemStack stack) {
        return getOutput(stack) != null;
    }

    public static Tuple.Pair<ItemStack, FluidStack> getOutput(ItemStack stack) {
        registerDefaults();
        if(stack.isEmpty()) return null;

        for(Map.Entry<AStack, Tuple.Pair<ItemStack, FluidStack>> entry : RECIPES.entrySet()) {
            if(entry.getKey().matchesRecipe(stack, true)) {
                Tuple.Pair<ItemStack, FluidStack> pair = entry.getValue();
                return new Tuple.Pair<>(pair.getKey().copy(), copyFluid(pair.getValue()));
            }
        }

        return null;
    }

    public static List<JeiRecipe> getJeiRecipes() {
        registerDefaults();
        List<JeiRecipe> recipes = new ArrayList<>();
        for(Map.Entry<AStack, Tuple.Pair<ItemStack, FluidStack>> entry : RECIPES.entrySet()) {
            recipes.add(new JeiRecipe(entry.getKey(), entry.getValue().getKey(), entry.getValue().getValue()));
        }
        return recipes;
    }

    private static void add(AStack input, ItemStack output, FluidStack fluid) {
        RECIPES.put(input, new Tuple.Pair<>(output == null ? ItemStack.EMPTY : output.copy(), copyFluid(fluid)));
    }

    private static FluidStack fluid(com.hbm.inventory.fluid.FluidType type, int amount) {
        return new FluidStack(type, amount);
    }

    private static FluidStack copyFluid(FluidStack stack) {
        if(stack == null) return null;
        return new FluidStack(stack.type, stack.fill, stack.pressure);
    }

    public record JeiRecipe(AStack input, ItemStack outputItem, FluidStack outputFluid) {
        public List<ItemStack> inputStacks() {
            return input.extractForJEI();
        }

        public ItemStack fluidIcon() {
            return outputFluid == null ? ItemStack.EMPTY : FluidIconItem.make(outputFluid);
        }
    }
}
