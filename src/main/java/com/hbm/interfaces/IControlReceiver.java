package com.hbm.interfaces;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

/**
 * For receiving (sort of) complex control data via NBT from clients
 * @author hbm
 */
public interface IControlReceiver {

    boolean hasPermission(Player player);

    void receiveControl(CompoundTag tag);
    /* this was the easiest way of doing this without needing to change all 7 quadrillion implementors */
    default void receiveControl(Player player, CompoundTag tag) { }
}
