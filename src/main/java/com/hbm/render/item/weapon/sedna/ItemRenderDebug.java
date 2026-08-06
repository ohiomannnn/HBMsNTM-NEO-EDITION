package com.hbm.render.item.weapon.sedna;

import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.main.ResourceManager;
import com.hbm.render.anim.HbmAnimations;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ItemRenderDebug extends ItemRenderWeaponBase {

    @Override protected float getTurnMagnitude(ItemStack stack) { return GunBaseNTItem.getIsAiming(stack) ? 2.5F : -0.25F; }

    @Override
    public void setupFirstPerson(ItemStack stack) {
        super.setupFirstPerson(stack);

        float offset = 0.8F;
        standardAimingTransform(stack,
                -1.0F * offset, -0.75F * offset, 1F * offset,
                0F, -3.875F / 8F, 0F);
    }

    @Override
    public void setupThirdPerson(ItemStack stack) {
        super.setupThirdPerson(stack);
        RenderContext.scale(0.75F, 0.75F, 0.75F);
        RenderContext.translate(0F, 1F, 3F);
    }

    @Override
    public void setupInv(ItemStack stack) {
        super.setupInv(stack);
        float scale = 1.25F;
        RenderContext.scale(scale, scale, scale);
        RenderContext.mulPose(Axis.XP.rotationDegrees(25F));
        RenderContext.mulPose(Axis.YP.rotationDegrees(45F));
    }

    @Override
    public void renderFirstPerson(ItemStack stack, MultiBufferSource buffer) {

        GunBaseNTItem gun = (GunBaseNTItem) stack.getItem();

        float scale = 0.125F;
        RenderContext.scale(scale, scale, scale);
        RenderContext.mulPose(Axis.YP.rotationDegrees(90F));

        float[] equipSpin = HbmAnimations.getRelevantTransformation("ROTATE");
        float[] recoil = HbmAnimations.getRelevantTransformation("RECOIL");
        float[] reloadLift = HbmAnimations.getRelevantTransformation("RELOAD_LIFT");
        float[] reloadJolt = HbmAnimations.getRelevantTransformation("RELOAD_JOLT");
        float[] reloadTilt = HbmAnimations.getRelevantTransformation("RELAOD_TILT");
        float[] cylinderFlip = HbmAnimations.getRelevantTransformation("RELOAD_CYLINDER");
        float[] reloadBullets = HbmAnimations.getRelevantTransformation("RELOAD_BULLETS");

        RenderContext.mulPose(Axis.ZP.rotationDegrees(equipSpin[0]));

        standardAimingTransform(stack, 0, 0, recoil[2], -recoil[2], 0, 0);
        RenderContext.mulPose(Axis.ZP.rotationDegrees(recoil[2] * 10));

        RenderContext.pushPose();
        RenderContext.translate(-9F, 2.5F, 0F);
        RenderContext.mulPose(Axis.ZP.rotationDegrees(recoil[2] * -10));
        renderSmokeNodes(buffer, gun.getConfig(stack, 0).smokeNodes, 0.5F);
        RenderContext.popPose();

        RenderContext.mulPose(Axis.ZP.rotationDegrees(reloadLift[0]));
        RenderContext.translate(reloadJolt[0], 0F, 0F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(reloadTilt[0]));

        RenderSystem.setShaderTexture(0, ResourceManager.DEBUG_GUN_TEX);
        ResourceManager.lilmac.renderPart("Gun");

        RenderContext.pushPose();
        RenderContext.mulPose(Axis.XP.rotationDegrees(cylinderFlip[0]));
        ResourceManager.lilmac.renderPart("Pivot");
        RenderContext.translate(0F, 1.75F, 0F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(HbmAnimations.getRelevantTransformation("DRUM")[2] * -60));
        RenderContext.translate(0F, -1.75F, 0F);
        ResourceManager.lilmac.renderPart("Cylinder");
        RenderContext.translate(reloadBullets[0], reloadBullets[1], reloadBullets[2]);
        if(HbmAnimations.getRelevantTransformation("RELOAD_BULLETS_CON")[0] != 1) ResourceManager.lilmac.renderPart("Bullets");
        ResourceManager.lilmac.renderPart("Casings");
        RenderContext.popPose();

        RenderContext.pushPose(); /// HAMMER ///
        RenderContext.translate(4F, 1.25F, 0F);
        RenderContext.mulPose(Axis.ZP.rotationDegrees(-30 + 30 * HbmAnimations.getRelevantTransformation("HAMMER")[2]));
        RenderContext.translate(-4F, -1.25F, 0F);
        ResourceManager.lilmac.renderPart("Hammer");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(0.125F, 2.5F, 0F);
        renderGapFlash(buffer, gun.lastShot[0]);
        RenderContext.popPose();
    }

    @Override
    public void renderStatic(ItemStack stack, MultiBufferSource buffer, ItemDisplayContext displayContext) {

        RenderContext.mulPose(Axis.YP.rotationDegrees(90F));

        RenderSystem.setShaderTexture(0, ResourceManager.DEBUG_GUN_TEX);
        ResourceManager.lilmac.renderPart("Gun");
        ResourceManager.lilmac.renderPart("Cylinder");
        ResourceManager.lilmac.renderPart("Bullets");
        ResourceManager.lilmac.renderPart("Casings");
        ResourceManager.lilmac.renderPart("Pivot");
        ResourceManager.lilmac.renderPart("Hammer");

        if(living != null && (displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
            long shot;
            if(living == Minecraft.getInstance().player) {
                GunBaseNTItem gun = (GunBaseNTItem) stack.getItem();
                shot = gun.lastShot[0];
            } else {
                shot = ItemRenderWeaponBase.flashMap.getOrDefault(living, (long) -1);
                if(shot < 0) return;
            }

            RenderContext.pushPose();
            RenderContext.translate(0.125F, 2.5F, 0F);
            renderGapFlash(buffer, shot);
            RenderContext.popPose();
        }
    }
}
