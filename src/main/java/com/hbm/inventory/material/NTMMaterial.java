package com.hbm.inventory.material;

import com.hbm.inventory.MetaHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Encapsulates most materials that are currently listed as DictFrames, even vanilla ones.
 *
 * @author hbm
 */
// todo everything???
public class NTMMaterial {

    public int id;
    public String[] names;
    public Set<MaterialShapes> autogen = new HashSet<>();
    public Set<MatTraits> traits = new HashSet<>();
    public SmeltingBehavior smeltable = SmeltingBehavior.NOT_SMELTABLE;
    public int solidColorLight = 0xFF4A00;
    public int solidColorDark = 0x802000;
    public int moltenColor = 0xFF4A00;

    public NTMMaterial smeltsInto;
    public int convIn;
    public int convOut;

//    public NTMMaterial(int id, DictFrame dict) {
//
//        this.names = dict.mats;
//        this.id = id;
//
//        this.smeltsInto = this;
//        this.convIn = 1;
//        this.convOut = 1;
//
//        for(String name : dict.mats) {
//            Mats.matByName.put(name, this);
//        }
//
//        Mats.orderedList.add(this);
//        Mats.matById.put(id, this);
//    }

    public String getDescriptionId() {
        return "hbmmat." + this.names[0].toLowerCase(Locale.US);
    }

    public MutableComponent getName() {
        return Component.translatable(this.getDescriptionId());
    }

    public NTMMaterial setConversion(NTMMaterial mat, int in, int out) {
        this.smeltsInto = mat;
        this.convIn = in;
        this.convOut = out;
        return this;
    }

    /** Shapes for autogen */
    public NTMMaterial setAutogen(MaterialShapes... shapes) {
        this.autogen.addAll(Arrays.asList(shapes));
        return this;
    }

    /** Traits for recipe detection */
    public NTMMaterial setTraits(MatTraits... traits) {
        this.traits.addAll(Arrays.asList(traits));
        return this;
    }

    public NTMMaterial m() { this.traits.add(MatTraits.METAL); return this; }
    public NTMMaterial n() { this.traits.add(MatTraits.NONMETAL); return this; }

    /** Defines smelting behavior */
    public NTMMaterial smeltable(SmeltingBehavior behavior) {
        this.smeltable = behavior;
        return this;
    }

    public NTMMaterial setSolidColor(int colorLight, int colorDark) {
        this.solidColorLight = colorLight;
        this.solidColorDark = colorDark;
        return this;
    }

    public NTMMaterial setMoltenColor(int color) {
        this.moltenColor = color;
        return this;
    }

    public ItemStack make(Item item, int amount) {
        return MetaHelper.newStack(item, amount, this.id);
    }

    public ItemStack make(Item item) {
        return make(item, 1);
    }

    public enum SmeltingBehavior {
        NOT_SMELTABLE,	//anything that can't be smelted or otherwise doesn't belong in a smelter, like diamond. may also include things that are smeltable but turn into a different type
        VAPORIZES,		//can't be smelted because the material would skadoodle
        BREAKS,			//can't be smelted because the material doesn't survive the temperatures
        SMELTABLE,		//mostly metal
        ADDITIVE		//stuff like coal which isn't smeltable but can be put in a crucible anyway
    }

    public enum MatTraits {
        METAL,		//metal(like), smeltable by arc furnaces
        NONMETAL;	//non-metal(like), for gems, non-alloy compounds and similar
    }
}
