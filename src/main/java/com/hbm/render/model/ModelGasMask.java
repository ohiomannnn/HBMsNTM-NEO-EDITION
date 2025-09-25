package com.hbm.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

public class ModelGasMask<T extends LivingEntity> extends HumanoidModel<T> {

    private final ModelPart mask;

    public ModelGasMask(ModelPart root) {
        super(root, RenderType::entityCutoutNoCull);
        this.mask = root.getChild("mask");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition root = mesh.getRoot();

        PartDefinition mask = root.addOrReplaceChild("mask",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8, 8, 3) // main part
                        .texOffs(22, 0).addBox(-3.0F, -5.0F, -4.5F, 2, 2, 1) // filter left
                        .texOffs(22, 0).addBox(1.0F, -5.0F, -4.5F, 2, 2, 1)  // filter right
                        .texOffs(0, 11).addBox(-1.0F, -3.0F, -4.0F, 2, 2, 2)
                        .texOffs(0, 15).addBox(-0.5F, -1.0F, -4.0F, 3, 4, 3)
                        .texOffs(0, 22).addBox(-4.0F, -5.0F, -1.0F, 8, 1, 5),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        this.mask.copyFrom(this.head);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);

        poseStack.pushPose();
        poseStack.scale(1.15F, 1.15F, 1.15F);
        this.mask.render(poseStack, buffer, packedLight, packedOverlay, color);
        poseStack.popPose();
    }

}
