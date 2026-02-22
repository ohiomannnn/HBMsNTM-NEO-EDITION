package com.hbm.render.entity.projectile;

import com.hbm.entity.projectile.Rubble;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;

@OnlyIn(Dist.CLIENT)
public class RenderRubble extends EntityRenderer<Rubble> {

    protected TextureAtlasSprite sprite;

    private final ModelRubble<Rubble> model;

    public RenderRubble(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelRubble<>(context.bakeLayer(ModelRubble.LAYER));
    }

    @Override
    public void render(Rubble rubble, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(1.0F, 1.0F, 1.0F);

        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        float rot = ((rubble.tickCount + partialTicks) % 360) * 10;
        poseStack.mulPose(Axis.XP.rotationDegrees(rot));
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rot));

        Minecraft mc = Minecraft.getInstance();

        BlockState state = rubble.getBlock().defaultBlockState();
        int color = 0xFFFFFFFF;

        this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(state));

        if (IClientBlockExtensions.of(state).areBreakingParticlesTinted(state, mc.level, rubble.blockPosition())) {
            color = Minecraft.getInstance().getBlockColors().getColor(state, mc.level, rubble.blockPosition(), 0);
        }

        BakedModel stateModel = mc.getBlockRenderer().getBlockModel(state);
        TextureAtlasSprite sprite = stateModel.getParticleIcon();

        ResourceLocation texturePath = sprite.contents().name();
        ResourceLocation tex = texturePath.withPrefix("textures/").withSuffix(".png");

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(tex));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);

        poseStack.popPose();
    }

    protected void setSprite(TextureAtlasSprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public ResourceLocation getTextureLocation(Rubble rubble) {
        return null;
    }
}
