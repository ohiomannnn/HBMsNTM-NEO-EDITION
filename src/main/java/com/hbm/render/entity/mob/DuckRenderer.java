package com.hbm.render.entity.mob;

import com.hbm.HBMsNTM;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DuckRenderer extends ChickenRenderer {

    private static final ResourceLocation CHICKEN_LOCATION = HBMsNTM.withDefaultNamespaceNT("textures/entity/duck.png");

    public DuckRenderer(EntityRendererProvider.Context context) { super(context); }

    @Override
    public ResourceLocation getTextureLocation(Chicken chicken) {
        return CHICKEN_LOCATION;
    }
}