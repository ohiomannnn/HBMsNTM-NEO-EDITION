package com.hbm.render.item.weapon.sedna;

import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.main.NuclearTechMod;
import com.hbm.main.ResourceManager;
import com.hbm.particle.SpentCasing;
import com.hbm.render.anim.HbmAnimations;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class ItemRenderSPAS12 extends ItemRenderWeaponBase {

    @Override protected float getTurnMagnitude(ItemStack stack) { return GunBaseNTItem.getIsAiming(stack) ? 2.5F : -0.5F; }

    @Override
    public void setupFirstPerson(ItemStack stack) {
        RenderContext.translate(0F, 0F, 0.875F);

        float offset = 0.8F;
        standardAimingTransform(stack,
                -1.25F * offset, -1.75F * offset, -0.5F * offset,
                0, 0, 0);
    }

    @Override
    public void setupThirdPerson(ItemStack stack) {
        super.setupThirdPerson(stack);
        float scale = 1.75F;
        RenderContext.scale(scale, scale, scale);
        RenderContext.translate(0F, -0.75F, 0F);
    }

    @Override
    public void setupInv(ItemStack stack) {
        super.setupInv(stack);
        float scale = 2F;
        RenderContext.scale(scale, scale, scale);
        RenderContext.mulPose(Axis.XP.rotationDegrees(25F));
        RenderContext.mulPose(Axis.YP.rotationDegrees(45F));
        RenderContext.translate(4.25F, -0.5F, 0F);
    }

    @Override
    public void renderFirstPerson(ItemStack stack, MultiBufferSource buffer) {

        GunBaseNTItem gun = (GunBaseNTItem) stack.getItem();
        RenderSystem.setShaderTexture(0, ResourceManager.SPAS_12_TEX);
        float scale = 0.5F;
        RenderContext.scale(scale, scale, scale);
        RenderContext.mulPose(Axis.YP.rotationDegrees(180F));

        float[] equip = HbmAnimations.getRelevantTransformation("EQUIP");

        RenderContext.mulPose(Axis.XP.rotationDegrees(equip[0]));

        HbmAnimations.applyRelevantTransformation("MainBody");
        ResourceManager.spas_12.renderPart("MainBody");

        RenderContext.pushPose();
        HbmAnimations.applyRelevantTransformation("PumpGrip");
        ResourceManager.spas_12.renderPart("PumpGrip");
        RenderContext.popPose();

        RenderSystem.setShaderTexture(0, ResourceManager.CASINGS_TEX);

        HbmAnimations.applyRelevantTransformation("Shell");
        SpentCasing casing = gun.getConfig(stack, 0).getReceivers(stack)[0].getMagazine(stack).getCasing(stack, NuclearTechMod.proxy.me().inventory);
        int color0 = SpentCasing.COLOR_CASE_BRASS;
        int color1 = SpentCasing.COLOR_CASE_BRASS;

        if(casing != null) {
            int[] colors = casing.getColors();
            color0 = colors[0];
            color1 = colors[colors.length > 1 ? 1 : 0];
        }

        Color shellColor = new Color(color1);
        RenderContext.setColor(shellColor.getRed() / 255F, shellColor.getGreen() / 255F, shellColor.getBlue() / 255F, 1F);
        ResourceManager.spas_12.renderPart("Shell");

        Color shellForeColor = new Color(color0);
        RenderContext.setColor(shellForeColor.getRed() / 255F, shellForeColor.getGreen() / 255F, shellForeColor.getBlue() / 255F, 1F);
        ResourceManager.spas_12.renderPart("ShellFore");

        RenderContext.setColor(1F, 1F, 1F, 1F);

        float smokeScale = 0.25F;

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.5F, -11F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(-90F));
        RenderContext.scale(smokeScale, smokeScale, smokeScale);
        renderSmokeNodes(buffer, gun.getConfig(stack, 0).smokeNodes, 0.75F);
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(0F, 1.5F, -11F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(-90F));
        RenderContext.mulPose(Axis.XP.rotationDegrees(90F * gun.shotRand));
        renderMuzzleFlash(buffer, gun.lastShot[0], 75, 7.5F);
        RenderContext.popPose();
    }

    @Override
    public void renderStatic(ItemStack stack, MultiBufferSource buffer, ItemDisplayContext displayContext) {

        RenderContext.mulPose(Axis.YP.rotationDegrees(180F));

        RenderSystem.setShaderTexture(0, ResourceManager.SPAS_12_TEX);
        ResourceManager.spas_12.renderPart("MainBody");
        ResourceManager.spas_12.renderPart("PumpGrip");

        if(living != null && (displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
            long shot;
            float shotRand = 0F;
            if(living == Minecraft.getInstance().player) {
                GunBaseNTItem gun = (GunBaseNTItem) stack.getItem();
                shot = gun.lastShot[0];
                shotRand = gun.shotRand;
            } else {
                shot = ItemRenderWeaponBase.flashMap.getOrDefault(living, (long) -1);
                if(shot < 0) return;
            }

            RenderContext.pushPose();
            RenderContext.translate(0F, 1.5F, -11F);
            RenderContext.mulPose(Axis.YP.rotationDegrees(-90F));
            RenderContext.mulPose(Axis.XP.rotationDegrees(90F * shotRand));
            renderMuzzleFlash(buffer, shot, 75, 7.5F);
            RenderContext.popPose();
        }
    }
}
