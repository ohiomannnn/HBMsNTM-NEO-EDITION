package com.hbm.blocks;

import com.hbm.inventory.MetaHelper;
import com.hbm.util.EnumUtil;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public abstract class EnumMultiBlock extends MultiBlock {

    public Class<? extends Enum<?>> theEnum;
    public final boolean multiName;
    public final boolean multiTexture;

    public EnumMultiBlock(Properties properties, Class<? extends Enum<?>> theEnum, boolean multiName, boolean multiTexture) {
        super(properties);
        this.theEnum = theEnum;
        this.multiName = multiName;
        this.multiTexture = multiTexture;
    }

    @Override
    public String getItemDescriptionId(ItemStack stack) {
        if(multiName) {
            Enum<?> num = EnumUtil.grabEnumSafely(theEnum, MetaHelper.getMeta(stack));
            return super.getDescriptionId() + "." + num.name().toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    @Override
    public int getSubCount() {
        return this.theEnum.getEnumConstants().length;
    }
}
