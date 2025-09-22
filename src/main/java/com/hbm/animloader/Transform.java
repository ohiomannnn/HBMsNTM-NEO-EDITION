package com.hbm.animloader;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.hbm.util.BobMathUtil;
import com.mojang.blaze3d.vertex.PoseStack;

public class Transform {

    Vector3f scale;
    Vector3f translation;
    Quaternionf rotation;

    boolean hidden = false;

    public Transform(Matrix4f matrix) {
        Matrix4f mat = new Matrix4f().set(matrix);
        scale = getScaleFromMatrix(mat);
        rotation = new Quaternionf().setFromUnnormalized(mat);
        translation = new Vector3f(mat.m30(), mat.m31(), mat.m32());
    }

    private Vector3f getScaleFromMatrix(Matrix4f mat) {
        Vector3f col0 = new Vector3f(mat.m00(), mat.m01(), mat.m02());
        Vector3f col1 = new Vector3f(mat.m10(), mat.m11(), mat.m12());
        Vector3f col2 = new Vector3f(mat.m20(), mat.m21(), mat.m22());

        float scaleX = col0.length();
        float scaleY = col1.length();
        float scaleZ = col2.length();

        col0.div(scaleX);
        col1.div(scaleY);
        col2.div(scaleZ);

        mat.m00(col0.x).m01(col0.y).m02(col0.z);
        mat.m10(col1.x).m11(col1.y).m12(col1.z);
        mat.m20(col2.x).m21(col2.y).m22(col2.z);

        return new Vector3f(scaleX, scaleY, scaleZ);
    }

    public void interpolateAndApply(Transform other, float inter, PoseStack poseStack) {
        Vector3f trans = BobMathUtil.interpVec(this.translation, other.translation, inter);
        Vector3f scale = BobMathUtil.interpVec(this.scale, other.scale, inter);
        Quaternionf rot = slerp(this.rotation, other.rotation, inter);

        poseStack.translate(trans.x, trans.y, trans.z);
        poseStack.mulPose(rot);
        poseStack.scale(scale.x, scale.y, scale.z);
    }

    protected Quaternionf slerp(Quaternionf q1, Quaternionf q2, float t) {
        return new Quaternionf(q1).slerp(q2, t);
    }
}
