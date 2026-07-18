package com.hbm.inventory.recipes;

import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.NtmItems;
import com.hbm.util.Tuple;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class RefineryRecipes {

    public static final int oil_frac_heavy = 50;
    public static final int oil_frac_naph = 25;
    public static final int oil_frac_light = 15;
    public static final int oil_frac_petro = 10;

    public static final int crack_frac_naph = 40;
    public static final int crack_frac_light = 30;
    public static final int crack_frac_aroma = 15;
    public static final int crack_frac_unsat = 15;

    public static final int oilds_frac_heavy = 30;
    public static final int oilds_frac_naph = 35;
    public static final int oilds_frac_light = 20;
    public static final int oilds_frac_unsat = 15;

    public static final int crackds_frac_naph = 35;
    public static final int crackds_frac_light = 35;
    public static final int crackds_frac_aroma = 15;
    public static final int crackds_frac_unsat = 15;

    public static final LinkedHashMap<FluidType, Tuple.Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack>> refinery = new LinkedHashMap<>();

    public static void registerDefaults() {
        if(!refinery.isEmpty()) return;

        register(
                Fluids.OIL_CRUDE_HOT,
                new FluidStack(Fluids.OIL_HEAVY, oil_frac_heavy),
                new FluidStack(Fluids.NAPHTHA, oil_frac_naph),
                new FluidStack(Fluids.OIL_LIGHT, oil_frac_light),
                new FluidStack(Fluids.PETROLEUM_GAS, oil_frac_petro),
                new ItemStack(NtmItems.SULFUR.get())
        );

        register(
                Fluids.OIL_CRUDE_CRACKED_HOT,
                new FluidStack(Fluids.NAPHTHA_CRACKED, crack_frac_naph),
                new FluidStack(Fluids.OIL_LIGHT_CRACKED, crack_frac_light),
                new FluidStack(Fluids.AROMATICS, crack_frac_aroma),
                new FluidStack(Fluids.UNSATURATEDS, crack_frac_unsat),
                new ItemStack(NtmItems.OIL_TAR_CRACK.get())
        );

        register(
                Fluids.OIL_CRUDE_DESULFURIZED_HOT,
                new FluidStack(Fluids.OIL_HEAVY, oilds_frac_heavy),
                new FluidStack(Fluids.NAPHTHA_DESULFURIZED, oilds_frac_naph),
                new FluidStack(Fluids.OIL_LIGHT_DESULFURIZED, oilds_frac_light),
                new FluidStack(Fluids.UNSATURATEDS, oilds_frac_unsat),
                new ItemStack(NtmItems.OIL_TAR_PARAFFIN.get())
        );

        register(
                Fluids.OIL_CRUDE_DESULFURIZED_CRACKED_HOT,
                new FluidStack(Fluids.NAPHTHA_DESULFURIZED, crackds_frac_naph),
                new FluidStack(Fluids.OIL_LIGHT_DESULFURIZED, crackds_frac_light),
                new FluidStack(Fluids.AROMATICS, crackds_frac_aroma),
                new FluidStack(Fluids.UNSATURATEDS, crackds_frac_unsat),
                new ItemStack(NtmItems.OIL_TAR_PARAFFIN.get())
        );
    }

    private static void register(FluidType input, FluidStack output1, FluidStack output2, FluidStack output3, FluidStack output4, ItemStack byproduct) {
        refinery.put(input, new Tuple.Quintet<>(output1, output2, output3, output4, byproduct));
    }

    public static Tuple.Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack> getRefinery(FluidType oil) {
        registerDefaults();
        return refinery.get(oil);
    }

    public static Map<FluidType, Tuple.Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack>> getRecipes() {
        registerDefaults();
        return refinery;
    }
}
