package com.hbm.render.item.weapon.sedna;

import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.main.ResourceManager;
import com.hbm.render.anim.HbmAnimations;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ItemRenderMaresleg extends ItemRenderWeaponBase {

    public final ResourceLocation texture;

    public ItemRenderMaresleg(ResourceLocation texture) {
        this.texture = texture;
    }

    @Override protected float getTurnMagnitude(ItemStack stack) { return GunBaseNTItem.getIsAiming(stack) ? 2.5F : -0.5F; }

    @Override
    public double getViewFOV(ItemStack stack, double fov, double partialTick) {
        double aimingProgress = Mth.lerp(partialTick, GunBaseNTItem.prevAimingProgress, GunBaseNTItem.aimingProgress);
        return fov * (1 - aimingProgress * 0.33);
    }

    @Override
    public void setupFirstPerson(ItemStack stack) {
        RenderContext.translate(0F, 0F, 0.875F);

        float offset = 0.8F;
        standardAimingTransform(stack,
                -1.25F * offset, -1F * offset, 2F * offset,
                0, -3.875F / 8F, 1);
    }

    @Override
    public void setupThirdPerson(ItemStack stack) {
        super.setupThirdPerson(stack);
        float scale = 1.75F;
        RenderContext.scale(scale, scale, scale);
        RenderContext.translate(0F, 0.25F, 3F);
    }

    @Override
    public void setupInv(ItemStack stack) {
        super.setupInv(stack);

        if(getShort(stack)) {
            float scale = 2.5F;
            RenderContext.scale(scale, scale, scale);
            RenderContext.mulPose(Axis.XP.rotationDegrees(25F));
            RenderContext.mulPose(Axis.YP.rotationDegrees(45F));
            RenderContext.translate(-1F, 0F, 0F);
        } else {
            float scale = 1.4375F;
            RenderContext.scale(scale, scale, scale);
            RenderContext.mulPose(Axis.XP.rotationDegrees(25F));
            RenderContext.mulPose(Axis.YP.rotationDegrees(45F));
            RenderContext.translate(-0.5F, 0.5F, 0F);
        }
    }

    @Override
    public void renderFirstPerson(ItemStack stack, MultiBufferSource buffer) {

        GunBaseNTItem gun = (GunBaseNTItem) stack.getItem();
        RenderSystem.setShaderTexture(0, texture);
        float scale = 0.375F;
        RenderContext.scale(scale, scale, scale);

        boolean shortened = getShort(stack);

        float[] equip = HbmAnimations.getRelevantTransformation("EQUIP");
        float[] recoil = HbmAnimations.getRelevantTransformation("RECOIL");
        float[] lever = HbmAnimations.getRelevantTransformation("LEVER");
        float[] turn = HbmAnimations.getRelevantTransformation("TURN");
        float[] flip = HbmAnimations.getRelevantTransformation("FLIP");
        float[] lift = HbmAnimations.getRelevantTransformation("LIFT");
        float[] shell = HbmAnimations.getRelevantTransformation("SHELL");
        float[] flag = HbmAnimations.getRelevantTransformation("FLAG");

        RenderContext.translate(recoil[0] * 2, recoil[1], recoil[2]);
        RenderContext.mulPose(Axis.XP.rotationDegrees(recoil[2] * 5));
        RenderContext.mulPose(Axis.ZP.rotationDegrees(turn[2]));

        RenderContext.translate(0F, 0F, -4F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(lift[0]));
        RenderContext.translate(0F, 0F, 4F);

        RenderContext.translate(0F, 0F, -4F);
        RenderContext.mulPose(Axis.XN.rotationDegrees(equip[0]));
        RenderContext.translate(0F, 0F, 4F);

        RenderContext.translate(0F, 0F, -2F);
        RenderContext.mulPose(Axis.XN.rotationDegrees(flip[0]));
        RenderContext.translate(0F, 0F, 2F);

        RenderContext.pushPose();
        RenderContext.translate(0F, 1F, shortened ? 3.75F : 8F);
        RenderContext.mulPose(Axis.ZN.rotationDegrees(turn[2]));
        RenderContext.mulPose(Axis.XP.rotationDegrees(flip[0]));
        RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
        renderSmokeNodes(buffer, gun.getConfig(stack, 0).smokeNodes, 0.25F);
        RenderContext.popPose();

        ResourceManager.maresleg.renderPart("Gun");
        if(!shortened) {
            ResourceManager.maresleg.renderPart("Stock");
            ResourceManager.maresleg.renderPart("Barrel");
        }

        RenderContext.pushPose();
        RenderContext.translate(0, 0.125F, -2.875F);
        RenderContext.mulPose(Axis.XP.rotationDegrees(lever[0]));
        RenderContext.translate(0, -0.125F, 2.875F);
        ResourceManager.maresleg.renderPart("Lever");
        RenderContext.popPose();

        RenderContext.pushPose();
        RenderContext.translate(shell[0], shell[1] - 0.75F, shell[2]);
        ResourceManager.maresleg.renderPart("Shell");
        RenderContext.popPose();

        if(flag[0] != 0) {
            RenderContext.pushPose();
            RenderContext.translate(0F, -0.5F, 0F);
            ResourceManager.maresleg.renderPart("Shell");
            RenderContext.popPose();
        }

        RenderContext.pushPose();
        RenderContext.translate(0F, 1F, shortened ? 3.75F : 8F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
        RenderContext.mulPose(Axis.XP.rotationDegrees(90 * gun.shotRand));
        renderMuzzleFlash(buffer, gun.lastShot[0], 75, 5);
        RenderContext.popPose();
    }

    @Override
    public void renderStatic(ItemStack stack, MultiBufferSource buffer, ItemDisplayContext displayContext) {

        RenderSystem.setShaderTexture(0, texture);
        ResourceManager.maresleg.renderPart("Gun");
        ResourceManager.maresleg.renderPart("Lever");
        boolean shortened = getShort(stack);
        if(!shortened) {
            ResourceManager.maresleg.renderPart("Stock");
            ResourceManager.maresleg.renderPart("Barrel");
        }

        if(living != null && (displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
            long shot;
            float shotRand = 0;
            if(living == Minecraft.getInstance().player) {
                GunBaseNTItem gun = (GunBaseNTItem) stack.getItem();
                shot = gun.lastShot[0];
                shotRand = gun.shotRand;
            } else {
                shot = ItemRenderWeaponBase.flashMap.getOrDefault(living, (long) -1);
                if(shot < 0) return;
            }

            RenderContext.pushPose();
            RenderContext.translate(0F, 1F, shortened ? 3.75F : 8F);
            RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            RenderContext.mulPose(Axis.XP.rotationDegrees(90 * shotRand));
            renderMuzzleFlash(buffer, shot, 75, 5);
            RenderContext.popPose();
        }
    }

    public boolean getShort(ItemStack stack) {
        //return stack.getItem() == ModItems.gun_maresleg_broken || XWeaponModManager.hasUpgrade(stack, 0, XWeaponModManager.ID_SAWED_OFF);
        return false;
    }
}
