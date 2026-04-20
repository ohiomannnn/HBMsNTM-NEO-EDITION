package com.hbm.render.model;

import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import org.joml.Vector3f;

public class BakedModelTransforms {

    private static final ItemTransforms PIPE_ITEM = buildPipeItem();

    public static ItemTransforms pipeItem() {
        return PIPE_ITEM;
    }

    private static ItemTransforms buildPipeItem() {
        ItemTransform gui = new ItemTransform(
                new Vector3f(30F, -45F, 0F),
                new Vector3f(0F, 0.05F, 0F),
                new Vector3f(0.8F, 0.8F, 0.8F)
        );

        ItemTransform thirdPerson = new ItemTransform(
                new Vector3f(75, 45, 0),
                new Vector3f(0, 0.25f, 0),
                new Vector3f(0.5f, 0.5f, 0.5f)
        );

        ItemTransform firstPerson = new ItemTransform(
                new Vector3f(0, 45, 0),
                new Vector3f(0, 0.25f, 0),
                new Vector3f(0.5f, 0.5f, 0.5f)
        );

        ItemTransform ground = new ItemTransform(
                new Vector3f(0, 0, 0),
                new Vector3f(0, 2f / 16f, 0),
                new Vector3f(0.5f, 0.5f, 0.5f)
        );

        ItemTransform head = new ItemTransform(
                new Vector3f(0, 0, 0),
                new Vector3f(0, 13f / 16f, 7f / 16f),
                new Vector3f(1, 1, 1)
        );

        ItemTransform fixed = new ItemTransform(
                new Vector3f(0, 180, 0),
                new Vector3f(0, 0, 0),
                new Vector3f(0.75f, 0.75f, 0.75f)
        );

        return new ItemTransforms(thirdPerson, thirdPerson, firstPerson, firstPerson, head, gui, ground, fixed);
    }
}
