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

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "rubble"), "main");

    private final ModelPart root;

    public ModelRubble(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 14.0F, 6.0F, 6.0F),
                PartPose.offset(-7.0F, 1.0F, 2.0F));

        partdefinition.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 13.0F, 5.0F),
                PartPose.offset(-7.0F, -6.0F, -5.0F));

        partdefinition.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(1.0F, 1.0F, -5.0F));

        partdefinition.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 14.0F, 7.0F, 4.0F),
                PartPose.offsetAndRotation(-7.0F, -7.0F, 2.0F, 0.0F, 0.4363323F, 0.0F));

        partdefinition.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 6.0F, 11.0F),
                PartPose.offset(0.0F, -6.0F, -5.0F));

        partdefinition.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(-4.0F, -4.0F, -4.0F));

        partdefinition.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 5.0F, 7.0F),
                PartPose.offset(-7.0F, -5.0F, 1.0F));

        partdefinition.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 12.0F, 6.0F, 4.0F),
                PartPose.offsetAndRotation(-6.0F, -1.0F, 3.0F, 0.0F, 0.0F, -0.3490659F));

        partdefinition.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 12.0F, 6.0F, 6.0F),
                PartPose.offsetAndRotation(-6.0F, 2.0F, -3.0F, 0.0F, -0.2094395F, 0.0F));

        partdefinition.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0F, 0.0F, 0.0F, 6.0F, 10.0F, 4.0F),
                PartPose.offsetAndRotation(-5.0F, -3.0F, -6.0F, 0.0F, 0.0F, -0.3490659F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T t, float v, float v1, float v2, float v3, float v4) { }
}
