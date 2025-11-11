package com.hbm.render.entity.projectile;

import com.hbm.HBMsNTM;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelRubble<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "rubble"), "main");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart shape7;
    private final ModelPart shape8;
    private final ModelPart shape9;
    private final ModelPart shape10;

    public ModelRubble(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.shape1 = root.getChild("shape1");
        this.shape2 = root.getChild("shape2");
        this.shape3 = root.getChild("shape3");
        this.shape4 = root.getChild("shape4");
        this.shape5 = root.getChild("shape5");
        this.shape6 = root.getChild("shape6");
        this.shape7 = root.getChild("shape7");
        this.shape8 = root.getChild("shape8");
        this.shape9 = root.getChild("shape9");
        this.shape10 = root.getChild("shape10");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-7F, 1F, 2F, 14, 6, 6),
                PartPose.ZERO);

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-7F, -6F, -5F, 6, 13, 5),
                PartPose.ZERO);

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(1F, 1F, -5F, 6, 6, 6),
                PartPose.ZERO);

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-7F, -7F, 2F, 14, 7, 4),
                PartPose.rotation(0F, 0.4363323F, 0F));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0F, -6F, -5F, 6, 6, 11),
                PartPose.ZERO);

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4F, -4F, -4F, 8, 8, 8),
                PartPose.ZERO);

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-7F, -5F, 1F, 6, 5, 7),
                PartPose.ZERO);

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-6F, -1F, 3F, 12, 6, 4),
                PartPose.rotation(0F, 0F, -0.3490659F));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-6F, 2F, -3F, 12, 6, 6),
                PartPose.rotation(0F, -0.2094395F, 0F));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-5F, -3F, -6F, 6, 10, 4),
                PartPose.rotation(0F, 0F, -0.3490659F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(T t, float v, float v1, float v2, float v3, float v4) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        shape1.render(poseStack, buffer, packedLight, packedOverlay, color);
        shape2.render(poseStack, buffer, packedLight, packedOverlay, color);
        shape3.render(poseStack, buffer, packedLight, packedOverlay, color);
        shape4.render(poseStack, buffer, packedLight, packedOverlay, color);
        shape5.render(poseStack, buffer, packedLight, packedOverlay, color);
        shape6.render(poseStack, buffer, packedLight, packedOverlay, color);
        shape7.render(poseStack, buffer, packedLight, packedOverlay, color);
        shape8.render(poseStack, buffer, packedLight, packedOverlay, color);
        shape9.render(poseStack, buffer, packedLight, packedOverlay, color);
        shape10.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
