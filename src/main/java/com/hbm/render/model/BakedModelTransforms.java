package com.hbm.render.model;

import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import org.joml.Vector3f;

public class BakedModelTransforms {

    public static final ItemTransforms BLOCK_ITEM = buildBlockItem();
    public static final ItemTransforms PIPE_ITEM = buildPipeItem();

    private static ItemTransforms buildPipeItem() {
        ItemTransform gui = new ItemTransform(
                new Vector3f(30F, 225F, 0F),
                new Vector3f(-0.57F, 0.3F, 0F),
                new Vector3f(0.8F, 0.8F, 0.8F)
        );
        ItemTransform ground = new ItemTransform(
                new Vector3f(0F, 0F, 0F),
                new Vector3f(0F, 0F, 0F),
                new Vector3f(0.25F, 0.25F, 0.25F)
        );
        ItemTransform fixed = new ItemTransform(
                new Vector3f(0F, 0F, 0F),
                new Vector3f(0F, 0F, 0F),
                new Vector3f(0.5F, 0.5F, 0.5F)
        );
        ItemTransform thirdPerson = new ItemTransform(
                new Vector3f(75F, 135F, 0F),
                new Vector3f(0F, 0.1F, 0F),
                new Vector3f(0.375F, 0.375F, 0.375F)
        );
        ItemTransform firstPerson = new ItemTransform(
                new Vector3f(0F, 45F, 0F),
                new Vector3f(0F, 0F, 0F),
                new Vector3f(0.4F, 0.4F, 0.4F)
        );
        return new ItemTransforms(thirdPerson, thirdPerson, firstPerson, firstPerson, fixed, gui, ground, fixed);
    }

    private static ItemTransforms buildBlockItem() {
        ItemTransform gui = new ItemTransform(
                new Vector3f(30F, 225F, 0F),
                new Vector3f(0F, 0F, 0F),
                new Vector3f(0.625F, 0.625F, 0.625F)
        );
        ItemTransform ground = new ItemTransform(
                new Vector3f(0F, 0F, 0F),
                new Vector3f(0F, 3F, 0F),
                new Vector3f(0.25F, 0.25F, 0.25F)
        );
        ItemTransform fixed = new ItemTransform(
                new Vector3f(0F, 0F, 0F),
                new Vector3f(0F, 0F, 0F),
                new Vector3f(0.5F, 0.5F, 0.5F)
        );
        ItemTransform thirdPerson = new ItemTransform(
                new Vector3f(75F, 135F, 0F),
                new Vector3f(0F, 0.1F, 0F),
                new Vector3f(0.375F, 0.375F, 0.375F)
        );
        ItemTransform firstPerson = new ItemTransform(
                new Vector3f(0F, 45F, 0F),
                new Vector3f(0F, 0F, 0F),
                new Vector3f(0.4F, 0.4F, 0.4F)
        );
        return new ItemTransforms(thirdPerson, thirdPerson, firstPerson, firstPerson, fixed, gui, ground, fixed);
    }
}
