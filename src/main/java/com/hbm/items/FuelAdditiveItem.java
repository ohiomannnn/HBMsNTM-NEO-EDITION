package com.hbm.items;

import com.hbm.interfaces.IOrderedEnum;

public class FuelAdditiveItem extends EnumMultiItem {

    public enum Type implements IOrderedEnum {
        ANTIKNOCK,
        DEICER;

        @Override
        public Enum<?>[] getOrder() {
            return values();
        }
    }

    public FuelAdditiveItem(Properties properties) {
        super(properties, Type.class, true, true);
    }
}
