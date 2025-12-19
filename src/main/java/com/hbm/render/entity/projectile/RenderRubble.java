package com.hbm.render.entity.projectile;

import com.hbm.entity.projectile.Rubble;
import com.hbm.render.util.AtlasSpriteVertexConsumer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class RenderRubble extends EntityRenderer<Rubble> {

    private final ModelRubble<Rubble> model;

    public RenderRubble(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelRubble<>(context.bakeLayer(ModelRubble.LAYER_LOCATION));
    }

    @Override
    public void render(Rubble entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.scale(1.0F, 1.0F, 1.0F);

        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        float rot = ((entity.tickCount + partialTicks) % 360) * 10;
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rot));

        Block block = entity.getBlock();

        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
        ResourceLocation tex = blockId.withPrefix("textures/block/").withSuffix(".png");

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(tex));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Rubble entity) {
        return null;
    }
}
