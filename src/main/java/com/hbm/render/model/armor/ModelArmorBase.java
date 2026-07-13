package com.hbm.render.model.armor;

import com.hbm.render.loader.ModelRendererObj;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public abstract class ModelArmorBase extends Model {

    public final HumanoidModel<? extends LivingEntity> model;
    public @Nullable LivingEntity living;
    public EquipmentSlot slot;

    public ModelRendererObj head;
    public ModelRendererObj body;
    public ModelRendererObj leftArm;
    public ModelRendererObj rightArm;
    public ModelRendererObj leftLeg;
    public ModelRendererObj rightLeg;
    public ModelRendererObj leftFoot;
    public ModelRendererObj rightFoot;

    public ModelArmorBase(HumanoidModel<? extends LivingEntity> model, EquipmentSlot slot) {
        super(RenderType::entityCutoutNoCull);

        this.model = model;
        this.slot = slot;

        // Generate null defaults to prevent major breakage from using incomplete models
        this.head = new ModelRendererObj(null).copyRotationFrom(model.head);
        this.body = new ModelRendererObj(null).copyRotationFrom(model.body);
        this.rightArm = new ModelRendererObj(null).copyRotationFrom(model.rightArm);
        this.leftArm = new ModelRendererObj(null).copyRotationFrom(model.leftArm);
        this.rightLeg = new ModelRendererObj(null).copyRotationFrom(model.rightLeg);
        this.leftLeg = new ModelRendererObj(null).copyRotationFrom(model.leftLeg);
        this.rightFoot = new ModelRendererObj(null).copyRotationFrom(model.rightLeg);
        this.leftFoot = new ModelRendererObj(null).copyRotationFrom(model.leftLeg);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color) {
        RenderContext.setup(poseStack, packedLight, packedOverlay);

        if(model.crouching) {
            this.rightFoot.z = 4.0F;
            this.leftFoot.z = 4.0F;
            this.rightFoot.y = 9.0F;
            this.leftFoot.y = 9.0F;
        } else {
            this.rightFoot.z = 0.1F;
            this.leftFoot.z = 0.1F;
            this.rightFoot.y = 12.0F;
            this.leftFoot.y = 12.0F;
        }

        this.rightFoot.xRot = this.rightLeg.xRot;
        this.rightFoot.yRot = this.rightLeg.yRot;
        this.rightFoot.zRot = this.rightLeg.zRot;
        this.leftFoot.xRot = this.leftLeg.xRot;
        this.leftFoot.yRot = this.leftLeg.yRot;
        this.leftFoot.zRot = this.leftLeg.zRot;

        if(model.young) {
            RenderContext.pushPose();
            RenderContext.scale(0.75F, 0.75F, 0.75F);

            RenderContext.translate(0F, 1F, 0F);
            this.render(true);
            RenderContext.popPose();
            RenderContext.pushPose();
            RenderContext.scale(0.5F, 0.5F, 0.5F);
            RenderContext.translate(0F, 1.5F, 0F);
            this.render(false);
            RenderContext.popPose();
        } else {
            this.render(true);
            this.render(false);
        }

        RenderContext.end();
    }

    public void getPropertiesFrom(HumanoidModel<? extends LivingEntity> model) {
        this.head.copyRotationFrom(model.head);
        this.body.copyRotationFrom(model.body);
        this.rightArm.copyRotationFrom(model.rightArm);
        this.leftArm.copyRotationFrom(model.leftArm);
        this.rightLeg.copyRotationFrom(model.rightLeg);
        this.leftLeg.copyRotationFrom(model.leftLeg);
        this.head.doRender = model.head.visible;
        this.body.doRender = model.body.visible;
        this.rightArm.doRender = model.rightArm.visible;
        this.leftArm.doRender = model.leftArm.visible;
        this.rightLeg.doRender = model.rightLeg.visible;
        this.leftLeg.doRender = model.leftLeg.visible;
        this.rightFoot.doRender = model.rightLeg.visible;
        this.leftFoot.doRender = model.leftLeg.visible;
    }

    public abstract void render(boolean head);

    public static void bindTexture(ResourceLocation texture) { RenderSystem.setShaderTexture(0, texture); }
}
