package com.hbm.util.mixins;

import com.hbm.render.item.weapon.sedna.ItemRenderWeaponBase;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @ModifyExpressionValue(method = "renderItemInHand(Lnet/minecraft/client/Camera;FLorg/joml/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;"))
    private Object renderItemInHand(Object original, Camera camera, float partialTick, Matrix4f projectionMatrix) {
        boolean vanillaVal = (Boolean) original;

        Player player = Minecraft.getInstance().player;
        if(player == null) return vanillaVal;

        return vanillaVal && !(IClientItemExtensions.of(player.getMainHandItem()).getCustomRenderer() instanceof ItemRenderWeaponBase);
    }
}
