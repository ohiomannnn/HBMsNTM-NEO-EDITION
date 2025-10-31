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

public class ModelShrapnel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "shrapnel"), "main");

    private final ModelPart bullet;

    public ModelShrapnel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.bullet = root.getChild("bullet");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("bullet",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offset(1.0F, -0.5F, -0.5F));

        return LayerDefinition.create(mesh, 16, 8);
    }

    @Override
    public void setupAnim(T t, float v, float v1, float v2, float v3, float v4) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        bullet.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
