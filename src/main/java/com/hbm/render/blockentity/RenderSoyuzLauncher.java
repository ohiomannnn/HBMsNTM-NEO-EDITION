package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.SoyuzLauncherBlockEntity;
import com.hbm.main.ResourceManager;
import com.hbm.render.util.RenderContext;
import com.hbm.render.util.SoyuzPronter;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

public class RenderSoyuzLauncher extends BlockEntityRendererNT<SoyuzLauncherBlockEntity> {

    @Override public BlockEntityRenderer<SoyuzLauncherBlockEntity> create(Context context) { return new RenderSoyuzLauncher(); }

    @Override
    public void render(SoyuzLauncherBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        RenderContext.setup(poseStack, packedLight, packedOverlay);
        RenderContext.translate(0.5F, -4F, 0.5F);

        float open = 45F;
        int timer = 20;

        float rot = open;

        if(be.rocketType >=0) rot = 0;
        if(be.starting && be.countdown < timer) rot = (timer - be.countdown + partialTicks) * open / timer;

        renderSoyuzLauncher(rot);

        if(be.rocketType >= 0) {
            RenderContext.translate(0.0F, 5.0F, 0.0F);
            SoyuzPronter.prontSoyuz(be.rocketType);
        }

        RenderContext.end();
    }

    public void renderSoyuzLauncher(float rot) {

        RenderContext.pushPose();
        RenderSystem.disableCull();

        bindTexture(ResourceManager.SOYUZ_LAUNCHER_LEGS_TEX);
        ResourceManager.soyuz_launcher_legs.renderAll();

        bindTexture(ResourceManager.SOYUZ_LAUNCHER_TABLE_TEX);
        ResourceManager.soyuz_launcher_table.renderAll();

        bindTexture(ResourceManager.SOYUZ_LAUNCHER_TOWER_BASE_TEX);
        ResourceManager.soyuz_launcher_tower_base.renderAll();

        RenderContext.pushPose(); {
            bindTexture(ResourceManager.SOYUZ_LAUNCHER_TOWER_TEX);
            RenderContext.translate(0F, 5.5F, 5.5F);
            RenderContext.mulPose(Axis.XP.rotationDegrees(rot));
            RenderContext.translate(0F, -5.5F, -5.5F);
            ResourceManager.soyuz_launcher_tower.renderAll();
        } RenderContext.popPose();

        bindTexture(ResourceManager.SOYUZ_LAUNCHER_SUPPORT_BASE_TEX);
        ResourceManager.soyuz_launcher_support_base.renderAll();

        RenderContext.pushPose(); {
            bindTexture(ResourceManager.SOYUZ_LAUNCHER_SUPPORT_TEX);
            RenderContext.translate(0F, 5.5F, -6.5F);
            RenderContext.mulPose(Axis.XN.rotationDegrees(rot));
            RenderContext.translate(0F, -5.5F, 6.5F);
            ResourceManager.soyuz_launcher_support.renderAll();
        } RenderContext.popPose();

        RenderSystem.enableCull();
        RenderContext.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(SoyuzLauncherBlockEntity be) {
        return true;
    }
}
