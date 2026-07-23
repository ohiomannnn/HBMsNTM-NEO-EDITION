package com.hbm.util.mixins;

import com.hbm.render.item.weapon.sedna.ItemRenderWeaponBase;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Shadow
    private ItemStack mainHandItem;

    @Redirect(method = "renderHandsWithItems", at = @At(value = "INVOKE", target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;"))
    private Quaternionf renderHandsWithItems(Axis axis, float degrees) {
        if(IClientItemExtensions.of(mainHandItem).getCustomRenderer() instanceof ItemRenderWeaponBase) return new Quaternionf();
        return axis.rotationDegrees(degrees);
    }
}
