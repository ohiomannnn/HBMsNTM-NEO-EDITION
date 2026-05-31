package com.hbm.util.mixins;

import com.hbm.util.DamageResistanceHandler;
import com.hbm.util.EntityDamageUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public float lastHurt;

    @Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/CommonHooks;onEntityIncomingDamage(Lnet/minecraft/world/entity/LivingEntity;Lnet/neoforged/neoforge/common/damagesource/DamageContainer;)Z"))
    private boolean hurt(LivingEntity entity, DamageContainer container, DamageSource source, float amount) {
        boolean originalResult = CommonHooks.onEntityIncomingDamage(entity, container);

        if(EntityDamageUtil.changeVanilla) {
            return originalResult && EntityDamageUtil.allowSpecialCancel;
        } else {
            return originalResult;
        }
    }

    @Inject(method = "hurt", at = @At(value = "HEAD"))
    private void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(EntityDamageUtil.changeVanilla && EntityDamageUtil.ignoreIFrame) this.lastHurt = 0F;
    }

    @Inject(method = "knockback", at = @At(value = "HEAD"), cancellable = true)
    private void knockback(double strength, double x, double z, CallbackInfo ci) {
        if(EntityDamageUtil.changeVanilla) {
            if(EntityDamageUtil.knockbackMultiplier > 0) {
                LivingEntity targetEntity = (LivingEntity) (Object) this;
                EntityDamageUtil.knockBack(targetEntity, strength, x, z, EntityDamageUtil.knockbackMultiplier);
            }
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/damagesource/DamageContainer;getNewDamage()F", ordinal = 3))
    private float actuallyHurt(float original) {
        if(EntityDamageUtil.changeVanilla) {
            return original * (1F - DamageResistanceHandler.currentPDR);
        } else {
            return original;
        }
    }
}
