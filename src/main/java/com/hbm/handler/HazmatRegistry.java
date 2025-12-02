package com.hbm.handler;

import com.hbm.lib.ModEffect;
import com.hbm.util.ShadyUtil;
import com.hbm.util.TagsUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;

public class HazmatRegistry {
    public static void initDefault() {

        //assuming coefficient of 10
        //real coefficient turned out to be 5
        //oops

        double helmet = 0.2D;
        double chest = 0.4D;
        double legs = 0.3D;
        double boots = 0.1D;

        double gold = 0.0225D; // 5%

        HazmatRegistry.registerHazmat(Items.GOLDEN_HELMET, gold * helmet);
        HazmatRegistry.registerHazmat(Items.GOLDEN_CHESTPLATE, gold * chest);
        HazmatRegistry.registerHazmat(Items.GOLDEN_LEGGINGS, gold * legs);
        HazmatRegistry.registerHazmat(Items.GOLDEN_BOOTS, gold * boots);
    }

    private static HashMap<Item, Double> entries = new HashMap<>();

    public static void registerHazmat(Item item, double resistance) {
        entries.put(item, resistance);
    }

    public static double getResistance(ItemStack stack) {

        if (stack.isEmpty()) return 0;

        double cladding = getCladding(stack);

        Double f = entries.get(stack.getItem());

        if (f != null)
            return f + cladding;

        return cladding;
    }

    public static double getCladding(ItemStack stack) {

        if (TagsUtil.getTag(stack).getFloat("hfr_cladding") > 0)
            return TagsUtil.getTag(stack).getFloat("hfr_cladding");

        return 0;
    }

    public static float getResistance(Player player) {

        float res = 0.0F;

        if (player.getUUID().toString().equals(ShadyUtil.Pu_238)) res += 0.4F;

        for (int i = 0; i < 4; i++) {
            res += (float) getResistance(player.getInventory().armor.get(i));
        }

        if (player.hasEffect(ModEffect.RADX))
            res += 0.2F;

        return res;
    }
}
