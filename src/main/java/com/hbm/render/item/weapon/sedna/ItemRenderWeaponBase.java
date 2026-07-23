package com.hbm.render.item.weapon.sedna;

import com.hbm.items.weapon.sedna.GunBaseNTItem;
import com.hbm.main.NuclearTechMod;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

public abstract class ItemRenderWeaponBase extends BlockEntityWithoutLevelRenderer {

    public static final ResourceLocation FLASH_PLUME_TEX = NuclearTechMod.withDefaultNamespace("textures/models/weapons/lilmac_plume.png");
    public static final ResourceLocation LASER_FLASH_TEX = NuclearTechMod.withDefaultNamespace("textures/models/weapons/laser_flash.png");

    protected static float partialTick;
    public static final HashMap<LivingEntity, Long> flashMap = new HashMap<>();

    public ItemRenderWeaponBase() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if(displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) return;

        RenderContext.setup(poseStack, packedLight, packedOverlay);

        if(displayContext != ItemDisplayContext.GUI) RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
        switch(displayContext) {
            case FIRST_PERSON_RIGHT_HAND -> {
                RenderContext.translate(-1.75F, -0.1F, -0.2F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(3F));
                this.setupFirstPerson(stack);
                this.renderFirstPerson(stack);
            }
            case THIRD_PERSON_RIGHT_HAND -> {
                this.setupThirdPerson(stack);
                this.renderStatic(stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
            }
            case GROUND -> {
                this.setupEntity(stack);
                this.renderStatic(stack, ItemDisplayContext.GROUND);
            }
            case GUI -> {
                this.setupInv(stack);
                this.renderStatic(stack, ItemDisplayContext.GUI);
            }
            default -> { }
        }

        RenderContext.end();
    }

    public void setup(ItemStack stack, PoseStack poseStack, float partialTick) {
        ItemRenderWeaponBase.partialTick = partialTick;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if(player == null) return;

        poseStack.translate(-1F, 0F, -1F);

        float swayMagnitude = this.getSwayMagnitude(stack);
        float swayPeriod = this.getSwayPeriod(stack);
        float turnMagnitude = this.getTurnMagnitude(stack);

        //floppyness
        float xBob = Mth.lerp(partialTick, player.xBobO, player.xBob);
        float yBob = Mth.lerp(partialTick, player.yBobO, player.yBob);
        poseStack.mulPose(Axis.XP.rotationDegrees((player.xRot - xBob) * 0.02F * turnMagnitude));
        poseStack.mulPose(Axis.YP.rotationDegrees((player.yRot - yBob) * 0.02F * turnMagnitude));

        //viewbob
        if(mc.getCameraEntity() instanceof Player) {
            float distanceDelta = player.walkDist - player.walkDistO;
            float distanceInterp = -(player.walkDist + distanceDelta * partialTick);
            float bob = Mth.lerp(partialTick, player.oBob, player.bob);
            poseStack.translate(Mth.sin(distanceInterp * (float) Math.PI * swayPeriod) * bob * 0.5F * swayMagnitude, -Math.abs(Mth.cos(distanceInterp * (float) Math.PI * swayPeriod) * bob) * swayMagnitude, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(distanceInterp * (float) Math.PI * swayPeriod) * bob * 3.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(Math.abs(Mth.cos(distanceInterp * (float) Math.PI * swayPeriod - 0.2F) * bob) * 5.0F));
        }
    }

//        RenderSystem.backupProjectionMatrix();
//        RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);
//        float fov = this.getFOVModifier(partialTick, false);
//        Matrix4f projectionMatrix = mc.gameRenderer.getProjectionMatrix(fov);
//        RenderSystem.setProjectionMatrix(projectionMatrix, VertexSorting.DISTANCE_TO_ORIGIN);
//        RenderSystem.restoreProjectionMatrix();

    private float getFOVModifier(float partialTick, boolean useFOVSetting) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if(player == null) return 70.0F;

        float fov = getBaseFOV(player.getMainHandItem());
        if(useFOVSetting) fov = (float) mc.options.fov().get();

        if(player.getHealth() <= 0.0F) {
            float f2 = (float) player.deathTime + partialTick;
            fov /= (1.0F - 500.0F / (f2 + 500.0F)) * 2.0F + 1.0F;
        }

        if(player.isEyeInFluid(FluidTags.WATER)) {
            fov *= 60.0F / 70.0F;
        }

        return fov;
    }

    public boolean isAkimbo(LivingEntity entity) { return false; }
    public boolean isLeftHanded() { return false; }
    protected float getBaseFOV(ItemStack stack) { return 70F; }
    public float getViewFOV(ItemStack stack, float fov) { return fov; }
    protected float getSwayMagnitude(ItemStack stack) { return GunBaseNTItem.getIsAiming(stack) ? 0.1F : 0.5F; }
    protected float getSwayPeriod(ItemStack stack) { return 0.75F; }
    protected float getTurnMagnitude(ItemStack stack) { return 2.75F; }

    public abstract void renderFirstPerson(ItemStack stack);
    public void renderStatic(ItemStack stack, ItemDisplayContext displayContext) { }

    public void setupFirstPerson(ItemStack stack) {
        RenderContext.translate(0F, 0F, 1F);
    }

    public void setupThirdPerson(ItemStack stack) {
        RenderContext.translate(0F, 0.5F, 0.1F);
        float scale = 0.05F;
        RenderContext.scale(scale, scale, scale);
    }

    public void setupInv(ItemStack stack) {
        float scale = 0.07F;
        RenderContext.scale(scale, scale, scale);
        RenderContext.translate(-7F, 7F, 0F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(120F));
        RenderContext.mulPose(Axis.ZP.rotationDegrees(-10F));
        RenderContext.mulPose(Axis.XP.rotationDegrees(-20F));
    }

    public void setupEntity(ItemStack stack) {
        RenderContext.translate(0F, 0.5F, 0F);
        float scale = 0.05F;
        RenderContext.scale(scale, scale, scale);
    }

    public static void standardAimingTransform(ItemStack stack, float sX, float sY, float sZ, float aX, float aY, float aZ) {
        float aimingProgress = Mth.lerp(partialTick, GunBaseNTItem.prevAimingProgress, GunBaseNTItem.aimingProgress);
        float x = sX + (aX - sX) * aimingProgress;
        float y = sY + (aY - sY) * aimingProgress;
        float z = sZ + (aZ - sZ) * aimingProgress;
        RenderContext.translate(x, y, z);
    }
}
