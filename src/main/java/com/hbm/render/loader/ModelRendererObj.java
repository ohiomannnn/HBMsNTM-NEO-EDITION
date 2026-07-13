package com.hbm.render.loader;


import com.hbm.render.util.RenderContext;
import net.minecraft.client.model.geom.ModelPart;
import org.joml.Quaternionf;

public class ModelRendererObj {

    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;
    public float xScale = 1.0F;
    public float yScale = 1.0F;
    public float zScale = 1.0F;

    public boolean doRender = true;

    private final String[] parts;
    private final IModelCustom model;

    public ModelRendererObj(IModelCustom model, String... parts) {
        this.model = model;
        this.parts = parts;
    }

    public void setPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ModelRendererObj setRotation(float xRot, float yRot, float zRot) {
        this.xRot = xRot;
        this.yRot = yRot;
        this.zRot = zRot;

        return this;
    }

    public void copyTo(ModelRendererObj obj) {
        obj.x = x;
        obj.y = y;
        obj.z = z;
        obj.xRot = xRot;
        obj.yRot = yRot;
        obj.zRot = zRot;
        obj.xScale = xScale;
        obj.yScale = yScale;
        obj.zScale = zScale;
    }

    public ModelRendererObj copyRotationFrom(ModelPart model) {
        this.x = model.x;
        this.y = model.y;
        this.z = model.z;
        this.xRot = model.xRot;
        this.yRot = model.yRot;
        this.zRot = model.zRot;
        this.xScale = model.xScale;
        this.yScale = model.yScale;
        this.zScale = model.zScale;

        return this;
    }

    public void render(float scale) {

        if(parts == null) return;

        RenderContext.pushPose();

        this.translateAndRotate(scale);

        if(doRender) {
            if(parts.length > 0) {
                for(String part : parts) model.renderPart(part);
            } else {
                model.renderAll();
            }
        }

        RenderContext.popPose();
    }

    public void translateAndRotate(float scale) {
        RenderContext.translate(this.x / 16.0F, this.y / 16.0F, this.z / 16.0F);
        if(this.xRot != 0.0F || this.yRot != 0.0F || this.zRot != 0.0F) {
            RenderContext.mulPose(new Quaternionf().rotationZYX(this.zRot, this.yRot, this.xRot));
        }

        if(this.xScale != 1.0F || this.yScale != 1.0F || this.zScale != 1.0F) {
            RenderContext.scale(this.xScale, this.yScale, this.zScale);
        }

        RenderContext.scale(scale, scale, scale);
    }
}
