package com.hbm.render.item.weapon.sedna;

import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.main.ResourceManager;
import com.hbm.render.anim.HbmAnimations;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;


// todo make aiming
public class ItemRenderDebug extends ItemRenderWeaponBase {

    @Override
    public void renderFirstPerson(ItemStack stack) {

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

        RenderContext.mulPose(Axis.ZP.rotationDegrees(recoil[2] * 10));

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
    }

    @Override
    public void renderStatic(ItemStack stack, ItemDisplayContext displayContext) {

        RenderContext.mulPose(Axis.YP.rotationDegrees(90F));

        RenderSystem.setShaderTexture(0, ResourceManager.DEBUG_GUN_TEX);
        ResourceManager.lilmac.renderPart("Gun");
        ResourceManager.lilmac.renderPart("Cylinder");
        ResourceManager.lilmac.renderPart("Bullets");
        ResourceManager.lilmac.renderPart("Casings");
        ResourceManager.lilmac.renderPart("Pivot");
        ResourceManager.lilmac.renderPart("Hammer");
    }
}
