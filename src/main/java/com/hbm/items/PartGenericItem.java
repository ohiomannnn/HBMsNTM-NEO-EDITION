package com.hbm.items;

import com.hbm.interfaces.IOrderedEnum;

public class PartGenericItem extends EnumMultiItem {

    public enum Type implements IOrderedEnum {
        PISTON_PNEUMATIC,
        PISTON_HYDRAULIC,
        PISTON_ELECTRIC,
        LDE,
        HDE,
        GLASS_POLARIZED;

        @Override
        public Enum<?>[] getOrder() {
            return values();
        }
    }

    public PartGenericItem(Properties properties) {
        super(properties, Type.class, true, true);
    }
}
