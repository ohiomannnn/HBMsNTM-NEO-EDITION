package com.hbm.util.mixins;

import com.hbm.items.ICustomRarityItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Final @Shadow private Item item;

    @Inject(method = "getRarity", at = @At(value = "HEAD"), cancellable = true)
    public void rarity(CallbackInfoReturnable<Rarity> cir) {
        if(this.item instanceof ICustomRarityItem icri) cir.setReturnValue(icri.getRarity((ItemStack) (Object) this));
    }
}
