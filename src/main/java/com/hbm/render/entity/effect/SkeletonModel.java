package com.hbm.render.entity.effect;

import com.hbm.HBMsNTM;
import com.hbm.particle.helper.SkeletonCreator.EnumSkeletonType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class SkeletonModel {

    public static ModelLayerLocation SKELETON_PART_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "particle_skeleton_part"), "main");

    private final ModelPart root;

    public SkeletonModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition partDefinition = mesh.getRoot();

        partDefinition.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0,0)
                        .addBox(-4,-8,-4, 8,8,8),
                PartPose.ZERO
        );
        partDefinition.addOrReplaceChild("limb",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F),
                PartPose.ZERO
        );
        partDefinition.addOrReplaceChild("torso",
                CubeListBuilder.create()
                        .texOffs(16, 16)
                        .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F),
                PartPose.ZERO
        );
        partDefinition.addOrReplaceChild("head_villager",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F),
                PartPose.ZERO
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    public void render(PoseStack pose, VertexConsumer buffer, int brightness, int overlay, int color, EnumSkeletonType type) {
        switch (type) {
            case SKULL -> root.getChild("head").render(pose, buffer, brightness, overlay, color);
            case LIMB -> root.getChild("limb").render(pose, buffer, brightness, overlay, color);
            case TORSO -> root.getChild("torso").render(pose, buffer, brightness, overlay, color);
            case SKULL_VILLAGER -> root.getChild("head_villager").render(pose, buffer, brightness, overlay, color);
        }
    }
}
