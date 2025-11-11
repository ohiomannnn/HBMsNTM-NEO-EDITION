package com.hbm.render.entity.projectile;

import com.hbm.entity.projectile.EntityRubble;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

public class RenderRubble extends EntityRenderer<EntityRubble> {

    private final ModelRubble<EntityRubble> model;

    public RenderRubble(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelRubble<>(context.bakeLayer(ModelRubble.LAYER_LOCATION));
    }

    @Override
    public void render(EntityRubble entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.translate(0.0D, 0.0D, 0.0D);

        poseStack.scale(1.0F, 1.0F, 1.0F);

        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        float rot = ((entity.tickCount + partialTicks) % 360) * 10;
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rot));

        BlockState state = entity.getBlockState();
        TextureAtlasSprite atlas = Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModelShaper()
                .getTexture(state, entity.level(), entity.blockPosition());

        VertexConsumer vc = buffer.getBuffer(RenderType.entityCutout(atlas.atlasLocation()));
        model.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRubble entityShrapnel) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
