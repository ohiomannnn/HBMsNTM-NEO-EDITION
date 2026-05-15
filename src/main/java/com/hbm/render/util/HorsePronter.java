package com.hbm.render.util;

import com.hbm.main.NuclearTechMod;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.IModelCustom;
import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

public class HorsePronter {
    public static final IModelCustom horse = new HFRWavefrontObject("models/obj/mobs/horse.obj").asVBO();

    public static final ResourceLocation DEMOHORSE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/horse/horse_demo.png");

    private static Vector3f[] pose = new Vector3f[] {
            new Vector3f(0F, 0F, 0F), //head
            new Vector3f(0F, 0F, 0F), //left front leg
            new Vector3f(0F, 0F, 0F), //right front leg
            new Vector3f(0F, 0F, 0F), //left back leg
            new Vector3f(0F, 0F, 0F), //right back leg
            new Vector3f(0F, 0F, 0F), //tail
            new Vector3f(0F, 0F, 0F), //body
            new Vector3f(0F, 0F, 0F) //body offset
    };

    private static Vector3f[] offsets = new Vector3f[] {
            new Vector3f(0F, 1.125F, 0.375F), //head
            new Vector3f(0.125F, 0.75F, 0.3125F), //left front leg
            new Vector3f(-0.125F, 0.75F, 0.3125F), //right front leg
            new Vector3f(0.125F, 0.75F, -0.25F), //left back leg
            new Vector3f(-0.125F, 0.75F, -0.25F), //right back leg
            new Vector3f(0F, 1.125F, -0.4375F), //tail
            new Vector3f(0F, 0F, 0F), //body
            new Vector3f(0F, 0F, 0F) //body offset
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

        for(Vector3f angles : pose) {
            angles.x = 0;
            angles.y = 0;
            angles.z = 0;
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
        float r = 60F;
        pose(HorsePronter.id_body, 0, -r, 0);
        pose(HorsePronter.id_tail, 0, 45, 90);
        pose(HorsePronter.id_lbl, 0, -90 + r, 35);
        pose(HorsePronter.id_rbl, 0, -90 + r, -35);
        pose(HorsePronter.id_lfl, 0, r - 10, 5);
        pose(HorsePronter.id_rfl, 0, r - 10, -5);
        pose(HorsePronter.id_head, 0, r, 0);
    }

    public static void pose(int id, float yaw, float pitch, float roll) {
        pose[id].x = yaw;
        pose[id].y = pitch;
        pose[id].z = roll;
    }

    public static void pront() {
        RenderContext.pushPose();
        RenderSystem.disableCull();
        doTransforms(id_body);

        horse.renderPart("Body");

        if(horn) {
            renderWithTransform(id_head, "Head", "Mane", maleSnoot ? "NoseMale" : "NoseFemale", "HornPointy");
        } else {
            renderWithTransform(id_head, "Head", "Mane", maleSnoot ? "NoseMale" : "NoseFemale");
        }

        renderWithTransform(id_lfl, "LeftFrontLeg");
        renderWithTransform(id_rfl, "RightFrontLeg");
        renderWithTransform(id_lbl, "LeftBackLeg");
        renderWithTransform(id_rbl, "RightBackLeg");
        renderWithTransform(id_tail,"Tail");

        if(wings) {
            horse.renderPart("LeftWing");
            horse.renderPart("RightWing");
        }

        RenderSystem.enableBlend();
        RenderContext.popPose();
    }

    private static void doTransforms(int id) {
        Vector3f rotation = pose[id];
        Vector3f offset = offsets[id];
        RenderContext.translate(offset.x, offset.y, offset.z);
        RenderContext.mulPose(Axis.YP.rotationDegrees(rotation.x));
        RenderContext.mulPose(Axis.XP.rotationDegrees(rotation.y));
        RenderContext.mulPose(Axis.ZP.rotationDegrees(rotation.z));
        RenderContext.translate(-offset.x, -offset.y, -offset.z);
    }

    private static void renderWithTransform(int id, String... parts) {
        RenderContext.pushPose();
        doTransforms(id);
        for(String part : parts) horse.renderPart(part);
        RenderContext.popPose();
    }
}
