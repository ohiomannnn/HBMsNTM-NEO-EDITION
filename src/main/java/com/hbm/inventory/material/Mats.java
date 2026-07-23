package com.hbm.inventory.material;

/* with every new rewrite, optimization and improvement, the code becomes more gregian */

import com.hbm.inventory.RecipesCommon.ComparableStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Defines materials that wrap around DictFrames to more accurately describe that material.
 * Direct uses are the crucible and possibly item auto-gen, depending on what traits are set.
 * @author hbm
 */
// todo everything???
public class Mats {

    public static final List<NTMMaterial> orderedList = new ArrayList<>();
    public static final HashMap<String, MaterialShapes> prefixByName = new HashMap<>();
    public static final HashMap<Integer, NTMMaterial> matById = new HashMap<>();
    public static final HashMap<String, NTMMaterial> matByTag = new HashMap<>();
    public static final HashMap<ComparableStack, List<MaterialStack>> materialEntries = new HashMap<>();
    public static final HashMap<String, List<MaterialStack>> materialOreEntries = new HashMap<>();


    public static class MaterialStack {
        //final fields to prevent accidental changing
        public final NTMMaterial material;
        public int amount;

        public MaterialStack(NTMMaterial material, int amount) {
            this.material = material;
            this.amount = amount;
        }

        public MaterialStack copy() {
            return new MaterialStack(material, amount);
        }
    }

}
