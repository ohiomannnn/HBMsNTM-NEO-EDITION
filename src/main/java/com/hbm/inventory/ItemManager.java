package com.hbm.inventory;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;
import java.util.Map;

import static net.minecraft.world.item.Items.*;
import static com.hbm.items.ModItems.*;

public class ItemManager {

    public static ItemFrame IRON;

    public static void register() {
        IRON = new ItemFrame().setNugget(IRON_NUGGET).setIngot(IRON_INGOT).setBlock(IRON_BLOCK);
    }

    public enum Type {
        ANY,
        NUGGET,
        INGOT,
        BLOCK,
        ORE,
        TINY,
        BOLT,
        DUST_TINY,
        DUST,
        GEM,
        CRYSTAL,
        PLATE,
        CAST_PLATE,
        WELDED_PLATE,
        WIRE,
        DENSE_WIRE,
        SHELL,
        PIPE,
        FRAGMENT,
        LIGHT_BARREL,
        HEAVY_BARREL,
        LIGHT_RECEIVER,
        HEAVY_RECEIVER,
        MECHANISM,
        STOCK,
        GRIP
    }

    public static class ItemFrame {

        private final Map<Type, ItemStack> items = new EnumMap<>(Type.class);

        public ItemFrame() {
            for (Type type : Type.values()) {
                items.put(type, NOTHING.toStack());
            }
        }

        public ItemStack get(Type type) {
            return items.getOrDefault(type, NOTHING.toStack()).copy();
        }

        public ItemFrame set(Type type, Item item) {
            items.put(type, new ItemStack(item));
            return this;
        }

        public ItemFrame set(Type type, ItemStack stack) {
            items.put(type, stack);
            return this;
        }

        public boolean has(Type type) {
            ItemStack stack = items.get(type);
            return stack != null && !stack.isEmpty() && stack.getItem() != NOTHING.get();
        }

        public ItemFrame setNugget(Item item)        { return set(Type.NUGGET, item); }
        public ItemFrame setIngot(Item item)         { return set(Type.INGOT, item); }
        public ItemFrame setBlock(Item item)         { return set(Type.BLOCK, item); }
        public ItemFrame setOre(Item item)           { return set(Type.ORE, item); }
        public ItemFrame setTiny(Item item)          { return set(Type.TINY, item); }
        public ItemFrame setBolt(Item item)          { return set(Type.BOLT, item); }
        public ItemFrame setDustTiny(Item item)      { return set(Type.DUST_TINY, item); }
        public ItemFrame setDust(Item item)          { return set(Type.DUST, item); }
        public ItemFrame setGem(Item item)           { return set(Type.GEM, item); }
        public ItemFrame setCrystal(Item item)       { return set(Type.CRYSTAL, item); }
        public ItemFrame setPlate(Item item)         { return set(Type.PLATE, item); }
        public ItemFrame setCastPlate(Item item)     { return set(Type.CAST_PLATE, item); }
        public ItemFrame setWeldedPlate(Item item)   { return set(Type.WELDED_PLATE, item); }
        public ItemFrame setWire(Item item)          { return set(Type.WIRE, item); }
        public ItemFrame setDenseWire(Item item)     { return set(Type.DENSE_WIRE, item); }
        public ItemFrame setShell(Item item)         { return set(Type.SHELL, item); }
        public ItemFrame setPipe(Item item)          { return set(Type.PIPE, item); }
        public ItemFrame setFragment(Item item)      { return set(Type.FRAGMENT, item); }
        public ItemFrame setLightBarrel(Item item)   { return set(Type.LIGHT_BARREL, item); }
        public ItemFrame setHeavyBarrel(Item item)   { return set(Type.HEAVY_BARREL, item); }
        public ItemFrame setLightReceiver(Item item) { return set(Type.LIGHT_RECEIVER, item); }
        public ItemFrame setHeavyReceiver(Item item) { return set(Type.HEAVY_RECEIVER, item); }
        public ItemFrame setMechanism(Item item)     { return set(Type.MECHANISM, item); }
        public ItemFrame setStock(Item item)         { return set(Type.STOCK, item); }
        public ItemFrame setGrip(Item item)          { return set(Type.GRIP, item); }

        public ItemStack nugget()           { return get(Type.NUGGET); }
        public ItemStack ingot()            { return get(Type.INGOT); }
        public ItemStack block()            { return get(Type.BLOCK); }
        public ItemStack ore()              { return get(Type.ORE); }
        public ItemStack tiny()             { return get(Type.TINY); }
        public ItemStack bolt()             { return get(Type.BOLT); }
        public ItemStack dustTiny()         { return get(Type.DUST_TINY); }
        public ItemStack dust()             { return get(Type.DUST); }
        public ItemStack gem()              { return get(Type.GEM); }
        public ItemStack crystal()          { return get(Type.CRYSTAL); }
        public ItemStack plate()            { return get(Type.PLATE); }
        public ItemStack castPlate()        { return get(Type.CAST_PLATE); }
        public ItemStack weldedPlate()      { return get(Type.WELDED_PLATE); }
        public ItemStack wire()             { return get(Type.WIRE); }
        public ItemStack denseWire()        { return get(Type.DENSE_WIRE); }
        public ItemStack shell()            { return get(Type.SHELL); }
        public ItemStack pipe()             { return get(Type.PIPE); }
        public ItemStack fragment()         { return get(Type.FRAGMENT); }
        public ItemStack lightBarrel()      { return get(Type.LIGHT_BARREL); }
        public ItemStack heavyBarrel()      { return get(Type.HEAVY_BARREL); }
        public ItemStack lightReceiver()    { return get(Type.LIGHT_RECEIVER); }
        public ItemStack heavyReceiver()    { return get(Type.HEAVY_RECEIVER); }
        public ItemStack mechanism()        { return get(Type.MECHANISM); }
        public ItemStack stock()            { return get(Type.STOCK); }
        public ItemStack grip()             { return get(Type.GRIP); }
    }
}
