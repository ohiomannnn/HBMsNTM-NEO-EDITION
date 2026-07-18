package com.hbm.items;

import com.hbm.inventory.MetaHelper;
import com.hbm.items.component.NtmDataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CastPlateItem extends Item implements IMetaItem {

    private final boolean welded;

    public CastPlateItem(Properties properties, boolean welded) {
        super(properties.component(NtmDataComponents.META.get(), 0));
        this.welded = welded;
    }

    @Override
    public void getSubItems(Item item, List<ItemStack> stacks) {
        for(Type type : Type.values()) {
            if(type.isAllowed(this.welded)) {
                stacks.add(MetaHelper.newStack(item, 1, type.ordinal()));
            }
        }
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return super.getDescriptionId(stack) + "." + Type.byMeta(MetaHelper.getMeta(stack)).key;
    }

    public int getColor(ItemStack stack) {
        return Type.byMeta(MetaHelper.getMeta(stack)).color;
    }

    public enum Type {
        IRON("iron", "Iron", true, true, 0x999999),
        GOLD("gold", "Gold", true, false, 0xF7D13A),
        SCHRABIDIUM("schrabidium", "Schrabidium", true, false, 0x059B9B),
        SCHRABIDATE("schrabidate", "Ferric Schrabidate", true, false, 0x524D8E),
        TITANIUM("titanium", "Titanium", true, true, 0x96928D),
        COPPER("copper", "Copper", true, true, 0xE75B25),
        TUNGSTEN("tungsten", "Tungsten", true, true, 0x2C2C2C),
        ALUMINIUM("aluminium", "Aluminium", true, true, 0x9BA4AB),
        LEAD("lead", "Lead", true, true, 0x474753),
        ZIRCONIUM("zirconium", "Zirconium", true, true, 0x948D6F),
        OSMIRIDIUM("osmiridium", "Osmiridium", true, true, 0x9BAFD0),
        STEEL("steel", "Steel", true, true, 0x494949),
        DURA_STEEL("dura_steel", "High-Speed Steel", true, true, 0x223E36),
        DESH("desh", "Desh", true, false, 0xC91717),
        STARMETAL("starmetal", "Starmetal", true, false, 0x5F5F7D),
        FERROURANIUM("ferrouranium", "Ferrouranium", true, false, 0x3E3E5B),
        TCALLOY("tcalloy", "Technetium Steel", true, true, 0x747D7D),
        CDALLOY("cdalloy", "C-D Alloy", true, true, 0xAD882A),
        BISMUTH_BRONZE("bismuth_bronze", "Bismuth Bronze", true, false, 0x8A6F59),
        ARSENIC_BRONZE("arsenic_bronze", "Arsenic Bronze", true, false, 0x745C45),
        COMBINE_STEEL("combine_steel", "CMB Steel", true, true, 0x191954),
        WEAPON_STEEL("weapon_steel", "Weapon Steel", true, false, 0x505050),
        SATURNITE("saturnite", "Saturnite", true, false, 0x217380);

        public final String key;
        public final String displayName;
        public final boolean castAllowed;
        public final boolean weldedAllowed;
        public final int color;

        Type(String key, String displayName, boolean castAllowed, boolean weldedAllowed, int color) {
            this.key = key;
            this.displayName = displayName;
            this.castAllowed = castAllowed;
            this.weldedAllowed = weldedAllowed;
            this.color = color;
        }

        public boolean isAllowed(boolean welded) {
            return welded ? this.weldedAllowed : this.castAllowed;
        }

        public static Type byMeta(int meta) {
            Type[] values = values();
            if(meta < 0 || meta >= values.length) return IRON;
            return values[meta];
        }
    }
}
