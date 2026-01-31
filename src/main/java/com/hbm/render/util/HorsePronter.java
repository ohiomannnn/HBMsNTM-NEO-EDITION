package com.hbm.render.util;

import com.hbm.HBMsNTM;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.IModelCustom;
import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class HorsePronter {
    public static final IModelCustom horse = new HFRWavefrontObject(HBMsNTM.withDefaultNamespaceNT("models/obj/mobs/horse.obj")).asVBO();

    public static final ResourceLocation tex_demohorse = HBMsNTM.withDefaultNamespaceNT("textures/models/horse/horse_demo.png");

    private static Vec3NT[] pose = new Vec3NT[] {
            new Vec3NT(0, 0, 0), //head
            new Vec3NT(0, 0, 0), //left front leg
            new Vec3NT(0, 0, 0), //right front leg
            new Vec3NT(0, 0, 0), //left back leg
            new Vec3NT(0, 0, 0), //right back leg
            new Vec3NT(0, 0, 0), //tail
            new Vec3NT(0, 0, 0), //body
            new Vec3NT(0, 0, 0) //body offset
    };

    private static Vec3NT[] offsets = new Vec3NT[] {
            new Vec3NT(0, 1.125, 0.375), //head
            new Vec3NT(0.125, 0.75, 0.3125), //left front leg
            new Vec3NT(-0.125, 0.75, 0.3125), //right front leg
            new Vec3NT(0.125, 0.75, -0.25), //left back leg
            new Vec3NT(-0.125, 0.75, -0.25), //right back leg
            new Vec3NT(0, 1.125, -0.4375), //tail
            new Vec3NT(0, 0, 0), //body
            new Vec3NT(0, 0, 0) //body offset
    };

    public static final int id_head = 0;
    public static final int id_lfl = 1;
    public static final int id_rfl = 2;
    public static final int id_lbl = 3;
    public static final int id_rbl = 4;
    public static final int id_tail = 5;
    public static final int id_body = 6;
    public static final int id_position = 7;

    private static boolean wings = false;
    private static boolean horn = false;
    private static boolean maleSnoot = false;

    public static void reset() {

        wings = false;
        horn = false;

        for (Vec3NT angles : pose) {
            angles.xCoord = 0;
            angles.yCoord = 0;
            angles.zCoord = 0;
        }
    }

    public static void enableHorn() { horn = true; }
    public static void enableWings() { wings = true; }
    public static void setMaleSnoot() { maleSnoot = true; }

    public static void setAlicorn() {
        enableHorn();
        enableWings();
    }

    public static void poseStandardSit() {
        double r = 60;
        pose(HorsePronter.id_body, 0, -r, 0);
        pose(HorsePronter.id_tail, 0, 45, 90);
        pose(HorsePronter.id_lbl, 0, -90 + r, 35);
        pose(HorsePronter.id_rbl, 0, -90 + r, -35);
        pose(HorsePronter.id_lfl, 0, r - 10, 5);
        pose(HorsePronter.id_rfl, 0, r - 10, -5);
        pose(HorsePronter.id_head, 0, r, 0);
    }

    public static void pose(int id, double yaw, double pitch, double roll) {
        pose[id].xCoord = yaw;
        pose[id].yCoord = pitch;
        pose[id].zCoord = roll;
    }

    public static void pront(MultiBufferSource buffer, PoseStack poseStack, int packedLight, int packedOverlay, ResourceLocation tex) {
        poseStack.pushPose();
        doTransforms(poseStack, id_body);

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(tex));
        horse.renderPart("Body", poseStack, consumer, packedLight, packedOverlay);

        if (horn) {
            renderWithTransform(buffer, poseStack, packedLight, packedOverlay, tex, id_head, "Head", "Mane", maleSnoot ? "NoseMale" : "NoseFemale", "HornPointy");
        } else {
            renderWithTransform(buffer, poseStack, packedLight, packedOverlay, tex, id_head, "Head", "Mane", maleSnoot ? "NoseMale" : "NoseFemale");
        }

        renderWithTransform(buffer, poseStack, packedLight, packedOverlay, tex, id_lfl, "LeftFrontLeg");
        renderWithTransform(buffer, poseStack, packedLight, packedOverlay, tex, id_rfl, "RightFrontLeg");
        renderWithTransform(buffer, poseStack, packedLight, packedOverlay, tex, id_lbl, "LeftBackLeg");
        renderWithTransform(buffer, poseStack, packedLight, packedOverlay, tex, id_rbl, "RightBackLeg");
        renderWithTransform(buffer, poseStack, packedLight, packedOverlay, tex, id_tail,"Tail");

        if (wings) {
            horse.renderPart("LeftWing", poseStack, consumer, packedLight, packedOverlay);
            horse.renderPart("RightWing", poseStack, consumer, packedLight, packedOverlay);
        }

        poseStack.popPose();
    }

    private static void doTransforms(PoseStack poseStack, int id) {
        Vec3NT rotation = pose[id];
        Vec3NT offset = offsets[id];
        poseStack.translate(offset.xCoord, offset.yCoord, offset.zCoord);
        poseStack.mulPose(Axis.YP.rotationDegrees((float) rotation.xCoord));
        poseStack.mulPose(Axis.XP.rotationDegrees((float) rotation.yCoord));
        poseStack.mulPose(Axis.ZP.rotationDegrees((float) rotation.zCoord));
        poseStack.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
    }

    private static void renderWithTransform(MultiBufferSource buffer, PoseStack poseStack, int packedLight, int packedOverlay, ResourceLocation tex, int id, String... parts) {
        poseStack.pushPose();
        doTransforms(poseStack, id);
        for (String part : parts) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(tex));
            horse.renderPart(part, poseStack, consumer, packedLight, packedOverlay);
        }
        poseStack.popPose();
    }
}
