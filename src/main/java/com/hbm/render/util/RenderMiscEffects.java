package com.hbm.render.util;

import com.hbm.main.NuclearTechMod;
import com.hbm.render.loader.IModelCustom;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RenderMiscEffects {

    public static final ResourceLocation GLINT = NuclearTechMod.withDefaultNamespace("textures/misc/glint.png");
    public static final ResourceLocation GLINT_BF = NuclearTechMod.withDefaultNamespace("textures/misc/glint_bf.png");

    public static void renderClassicGlint(float partialTicks, IModelCustom model, String part, float colorMod, float r, float g, float b, float speed, float scale) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        float color = colorMod;
        RenderStateManager.setColor(color, color, color, 1.0F);

        float offset = player.tickCount + partialTicks;

        FullBright.enable();

        for (int k = 0; k < 2; ++k) {

            float glintColor = 0.76F;
            RenderStateManager.setColor(r * glintColor, g * glintColor, b * glintColor, 1.0F);

            float movement = offset * (0.001F + (float) k * 0.003F) * speed;

            Matrix4f texMat = new Matrix4f()
                    .scale(scale, scale, scale)
                    .rotate(30.0F - (float) k * 60.0F, 0.0F, 0.0F, 1.0F)
                    .translate(0.0F, movement, 0.0F);
            RenderSystem.setTextureMatrix(texMat);

            if ("all".equals(part)) {
                model.renderAll();
            } else {
                model.renderPart(part);
            }

            RenderSystem.resetTextureMatrix();
        }

        FullBright.disable();
    }

    public static void renderClassicGlint(float interpol, IModelCustom model, String part, float r, float g, float b, float speed, float scale) {
        renderClassicGlint(interpol, model, part, 0.5F, r, g, b, speed, scale);
    }

    public static void renderClassicGlint(float interpol, IModelCustom model, String part) {
        renderClassicGlint(interpol, model, part, 0.5F, 0.25F, 0.8F, 20.0F, 1F / 3F);
    }
}
