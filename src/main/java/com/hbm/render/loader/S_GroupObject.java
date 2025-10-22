package com.hbm.render.loader;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import java.util.ArrayList;

public class S_GroupObject {
    public String name;
    public ArrayList<S_Face> faces = new ArrayList<>();
    public RenderType renderType;

    public S_GroupObject() {
        this("");
    }

    public S_GroupObject(String name) {
        this(name, RenderType.solid());
    }

    public S_GroupObject(String name, RenderType renderType) {
        this.name = name;
        this.renderType = renderType;
    }

    public void render() {
        if (this.faces.size() > 0) {
            ByteBufferBuilder builder = new ByteBufferBuilder(256);
            MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(builder);
            VertexConsumer consumer = bufferSource.getBuffer(this.renderType);
            render(consumer);
            BufferUploader.drawWithShader( (MeshData) consumer);
        }
    }

    public void render(VertexConsumer consumer) {
        if (this.faces.size() > 0) {
            for (S_Face face : this.faces) {
                face.addFaceForRender(consumer);
            }
        }
    }
}
