package com.hbm.render.mob;

import com.hbm.HBMsNTM;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;

public class EntityDuckRenderer extends ChickenRenderer {

    public EntityDuckRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Chicken entity) {
        return ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/entity/duck.png");
    }
}