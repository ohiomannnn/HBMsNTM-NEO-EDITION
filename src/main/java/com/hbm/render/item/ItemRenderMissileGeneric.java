package com.hbm.render.item;

import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.items.ModItems;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.newloader.IModelCustom;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

public class ItemRenderMissileGeneric extends BlockEntityWithoutLevelRenderer {

    public static HashMap<ComparableStack, RocketModelData> renderers = new HashMap<>();

    protected RenderMissileType type;

    public enum RenderMissileType {
        TYPE_TIER0,
        TYPE_TIER1,
        TYPE_TIER2,
        TYPE_TIER3,
        TYPE_STEALTH,
        TYPE_ABM,
        TYPE_NUCLEAR,
        TYPE_ROBIN
    }

    public ItemRenderMissileGeneric(RenderMissileType type) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.type = type;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        RocketModelData renderer = renderers.get(new ComparableStack(stack));
        if (renderer == null) return;

        RenderContext.setup(poseStack, packedLight, packedOverlay);

        float guiScale = 1;
        float guiOffset = 0;

        switch (this.type) {
            case TYPE_TIER0 -> { guiScale = 5F; guiOffset = 13.5F; }
            case TYPE_TIER1 -> { guiScale = 3.75F; guiOffset = 13F; }
            case TYPE_TIER2 -> {  guiScale = 2.75F; guiOffset = 12F; }
            case TYPE_TIER3 -> {  guiScale = 1.85F; guiOffset = 10F; }
            case TYPE_STEALTH -> {  guiScale = 2.4F; guiOffset = 11F; }
            case TYPE_ABM -> {  guiScale = 2.25F; guiOffset = 7F; }
            case TYPE_NUCLEAR -> {  guiScale = 1.8F; guiOffset = 9F; }
            case TYPE_ROBIN -> {  guiScale = 1.6F; guiOffset = 11F; }
        }

        RenderContext.translate(0.5F, 0F, 0.5F);
        switch (displayContext) {
            case FIRST_PERSON_RIGHT_HAND -> {
                RenderContext.translate(0.3F, 0.41F, 0.2F);
                RenderContext.scale(0.35F, 0.35F, 0.35F);
                RenderContext.mulPose(Axis.XP.rotationDegrees(-25F));
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));

                float s = 0.3F;
                RenderContext.translate(0.5F, 0.25F, 0F);
                RenderContext.scale(s, s, s);
            }
            case FIRST_PERSON_LEFT_HAND -> {
                RenderContext.translate(-0.3F, 0.41F, 0.2F);
                RenderContext.scale(0.35F, 0.35F, 0.35F);
                RenderContext.mulPose(Axis.XP.rotationDegrees(-25F));
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));

                float s = 0.3F;
                RenderContext.translate(0.5F, 0.25F, 0F);
                RenderContext.scale(s, s, s);
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            }
            case THIRD_PERSON_RIGHT_HAND, HEAD -> {
                RenderContext.translate(0F, 0.55F, -0.18F);
                RenderContext.scale(0.35F, 0.35F, 0.35F);

                float s = 0.15F;
                RenderContext.translate(0F, -0.5F, 0.5F);
                RenderContext.scale(s, s, s);
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            }
            case THIRD_PERSON_LEFT_HAND -> {
                RenderContext.translate(0F, 0.55F, -0.18F);
                RenderContext.scale(0.35F, 0.35F, 0.35F);

                float s = 0.15F;
                RenderContext.translate(0F, -0.5F, 0.5F);
                RenderContext.scale(s, s, s);
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            }
            case GROUND -> {
                RenderContext.translate(0F, 0.3F, 0F);
                RenderContext.scale(0.35F, 0.35F, 0.35F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));

                float s = 0.15F;
                RenderContext.scale(s, s, s);
            }
            case FIXED -> {
                RenderContext.translate(0F, 0.3F, 0F);
                RenderContext.scale(0.25F, 0.25F, 0.25F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            }
            case GUI -> {
                RenderContext.scale(0.0450F, 0.0450F, 0.0450F);
                RenderContext.translate(0F, 11.6F, -11.6F);

                RenderContext.scale(guiScale, guiScale, guiScale);
                RenderContext.mulPose(Axis.ZP.rotationDegrees(45F));
                RenderContext.mulPose(Axis.YP.rotationDegrees(System.currentTimeMillis() / 15 % 360));
                RenderContext.translate(0F, -16F + guiOffset, 0F);
            }
            case NONE -> {}
        }

        renderer.render(false);

        RenderContext.end();
    }

    public static RocketModelData generateStandard(ResourceLocation texture, IModelCustom model) { return generateWithScale(texture, model, 1F); }
    public static RocketModelData generateLarge(ResourceLocation texture, IModelCustom model) { return generateWithScale(texture, model, 1.5F); }

    public static RocketModelData generateWithScale(ResourceLocation texture, IModelCustom model, float scale) {
        return new RocketModelData(texture, model, scale);
    }

    public static void init() {

        renderers.put(new ComparableStack(ModItems.MISSILE_TAINT.get()), generateStandard(ResourceManager.MISSILE_MICRO_TAINT_TEX, ResourceManager.missileMicro));
        renderers.put(new ComparableStack(ModItems.MISSILE_MICRO.get()), generateStandard(ResourceManager.MISSILE_MICRO_TEX, ResourceManager.missileMicro));
        renderers.put(new ComparableStack(ModItems.MISSILE_BHOLE.get()), generateStandard(ResourceManager.MISSILE_MICRO_BHOLE_TEX, ResourceManager.missileMicro));
        renderers.put(new ComparableStack(ModItems.MISSILE_SCHRABIDIUM.get()), generateStandard(ResourceManager.MISSILE_MICRO_SCHRABIDIUM_TEX, ResourceManager.missileMicro));
        renderers.put(new ComparableStack(ModItems.MISSILE_EMP.get()), generateStandard(ResourceManager.MISSILE_MICRO_EMP_TEX, ResourceManager.missileMicro));

        renderers.put(new ComparableStack(ModItems.MISSILE_GENERIC.get()), generateStandard(ResourceManager.MISSILE_V2_HE_TEX, ResourceManager.missileV2));
        renderers.put(new ComparableStack(ModItems.MISSILE_DECOY.get()), generateStandard(ResourceManager.MISSILE_V2_DECOY_TEX, ResourceManager.missileV2));
        renderers.put(new ComparableStack(ModItems.MISSILE_INCENDIARY.get()), generateStandard(ResourceManager.MISSILE_V2_IN_TEX, ResourceManager.missileV2));
        renderers.put(new ComparableStack(ModItems.MISSILE_CLUSTER.get()), generateStandard(ResourceManager.MISSILE_V2_CL_TEX, ResourceManager.missileV2));
        renderers.put(new ComparableStack(ModItems.MISSILE_BUSTER.get()), generateStandard(ResourceManager.MISSILE_V2_BU_TEX, ResourceManager.missileV2));

        renderers.put(new ComparableStack(ModItems.MISSILE_STEALTH.get()), new RocketModelData(null, null, 0F) {
            @Override
            public void render(boolean cullFace) {
                RenderContext.setRenderType(cullFace ? NtmRenderTypes.FVBO.apply(ResourceManager.MISSILE_STEALTH_TEX) : NtmRenderTypes.FVBO_NC.apply(ResourceManager.MISSILE_STEALTH_TEX));
                ResourceManager.missileStealth.renderAll();
            }
        });

        renderers.put(new ComparableStack(ModItems.MISSILE_STRONG.get()), generateLarge(ResourceManager.MISSILE_STRONG_HE_TEX, ResourceManager.missileStrong));
        renderers.put(new ComparableStack(ModItems.MISSILE_INCENDIARY_STRONG.get()), generateLarge(ResourceManager.MISSILE_STRONG_IN_TEX, ResourceManager.missileStrong));
        renderers.put(new ComparableStack(ModItems.MISSILE_CLUSTER_STRONG.get()), generateLarge(ResourceManager.MISSILE_STRONG_CL_TEX, ResourceManager.missileStrong));
        renderers.put(new ComparableStack(ModItems.MISSILE_BUSTER_STRONG.get()), generateLarge(ResourceManager.MISSILE_STRONG_BU_TEX, ResourceManager.missileStrong));
        renderers.put(new ComparableStack(ModItems.MISSILE_EMP_STRONG.get()), generateLarge(ResourceManager.MISSILE_STRONG_EMP_TEX, ResourceManager.missileStrong));

        renderers.put(new ComparableStack(ModItems.MISSILE_BURST.get()), generateStandard(ResourceManager.MISSILE_HUGE_HE_TEX, ResourceManager.missileHuge));
        renderers.put(new ComparableStack(ModItems.MISSILE_INFERNO.get()), generateStandard(ResourceManager.MISSILE_HUGE_IN_TEX, ResourceManager.missileHuge));
        renderers.put(new ComparableStack(ModItems.MISSILE_RAIN.get()), generateStandard(ResourceManager.MISSILE_HUGE_CL_TEX, ResourceManager.missileHuge));
        renderers.put(new ComparableStack(ModItems.MISSILE_DRILL.get()), generateStandard(ResourceManager.MISSILE_HUGE_BU_TEX, ResourceManager.missileHuge));

        renderers.put(new ComparableStack(ModItems.MISSILE_NUCLEAR.get()), generateStandard(ResourceManager.MISSILE_NUCLEAR_TEX, ResourceManager.missileNuclear));
        renderers.put(new ComparableStack(ModItems.MISSILE_NUCLEAR_CLUSTER.get()), generateStandard(ResourceManager.MISSILE_THERMO_TEX, ResourceManager.missileNuclear));
        renderers.put(new ComparableStack(ModItems.MISSILE_VOLCANO.get()), generateStandard(ResourceManager.MISSILE_VOLCANO_TEX, ResourceManager.missileNuclear));
        renderers.put(new ComparableStack(ModItems.MISSILE_DOOMSDAY.get()), generateStandard(ResourceManager.MISSILE_DOOMSDAY_TEX, ResourceManager.missileNuclear));
        renderers.put(new ComparableStack(ModItems.MISSILE_DOOMSDAY_RUSTED.get()), generateStandard(ResourceManager.MISSILE_DOOMSDAY_RUSTED_TEX, ResourceManager.missileNuclear));

        renderers.put(new ComparableStack(ModItems.MISSILE_SHUTTLE.get()), generateStandard(ResourceManager.MISSILE_SHUTTLE_TEX, ResourceManager.missileShuttle));
    }

    public static class RocketModelData {
        public final ResourceLocation texture;
        public final IModelCustom model;
        public final float scale;

        public RocketModelData(ResourceLocation texture, IModelCustom model, float scale) {
            this.texture = texture;
            this.model = model;
            this.scale = scale;
        }

        public void render(boolean cullFace) {
            RenderContext.scale(scale, scale, scale);
            RenderContext.setRenderType(cullFace ? NtmRenderTypes.FVBO.apply(texture) : NtmRenderTypes.FVBO_NC.apply(texture));
            model.renderAll();
        }
    }
}
