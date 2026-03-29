package com.hbm.render.item;

import com.hbm.items.ModItems;
import com.hbm.main.ResourceManager;
import com.hbm.render.loader.IModelCustomOld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.function.Consumer;

public class ItemRenderMissileGeneric extends BlockEntityWithoutLevelRenderer {

    public static HashMap<Item, Consumer<RenderStarter>> renderers = new HashMap<>();

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

        Consumer<RenderStarter> renderer = renderers.get(stack.getItem());
        if (renderer == null) return;

        poseStack.pushPose();

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

        poseStack.translate(0.5F, 0F, 0.5F);
        switch (displayContext) {
            case FIRST_PERSON_RIGHT_HAND -> {
                poseStack.translate(0.3F, 0.41F, 0.2F);
                poseStack.scale(0.35F, 0.35F, 0.35F);
                poseStack.mulPose(Axis.XP.rotationDegrees(-25F));
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));

                float s = 0.3F;
                poseStack.translate(0.5F, 0.25F, 0F);
                poseStack.scale(s, s, s);
            }
            case FIRST_PERSON_LEFT_HAND -> {
                poseStack.translate(-0.3F, 0.41F, 0.2F);
                poseStack.scale(0.35F, 0.35F, 0.35F);
                poseStack.mulPose(Axis.XP.rotationDegrees(-25F));
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));

                float s = 0.3F;
                poseStack.translate(0.5F, 0.25F, 0F);
                poseStack.scale(s, s, s);
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
            }
            case THIRD_PERSON_RIGHT_HAND, HEAD -> {
                poseStack.translate(0F, 0.55F, -0.18F);
                poseStack.scale(0.35F, 0.35F, 0.35F);

                float s = 0.15F;
                poseStack.translate(0F, -0.5F, 0.5F);
                poseStack.scale(s, s, s);
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));
                poseStack.mulPose(Axis.YN.rotationDegrees(15F));
                poseStack.mulPose(Axis.XN.rotationDegrees(15F));
            }
            case THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(0F, 0.55F, -0.18F);
                poseStack.scale(0.35F, 0.35F, 0.35F);

                float s = 0.15F;
                poseStack.translate(0F, -0.5F, 0.5F);
                poseStack.scale(s, s, s);
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
                poseStack.mulPose(Axis.YN.rotationDegrees(15F));
                poseStack.mulPose(Axis.XN.rotationDegrees(15F));

            }
            case GROUND -> {
                poseStack.translate(0F, 0.3F, 0F);
                poseStack.scale(0.35F, 0.35F, 0.35F);
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));

                float s = 0.15F;
                poseStack.scale(s, s, s);
            }
            case FIXED -> {
                poseStack.translate(0F, 0.3F, 0F);
                poseStack.scale(0.25F, 0.25F, 0.25F);
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));
            }
            case GUI -> {
                poseStack.scale(0.0450F, 0.0450F, 0.0450F);
                poseStack.translate(0F, 11.6F, -11.6F);

                poseStack.scale(guiScale, guiScale, guiScale);
                poseStack.mulPose(Axis.ZP.rotationDegrees(45F));
                poseStack.mulPose(Axis.YP.rotationDegrees(System.currentTimeMillis() / 15 % 360));
                poseStack.translate(0F, -16F + guiOffset, 0F);
            }
            case NONE -> {}
        }

        renderer.accept(new RenderStarter(buffer, poseStack, packedLight, packedOverlay));

        poseStack.popPose();
    }

    public static Consumer<RenderStarter> generateStandard(ResourceLocation texture, IModelCustomOld model) { return generateWithScale(texture, model, 1F); }
    public static Consumer<RenderStarter> generateLarge(ResourceLocation texture, IModelCustomOld model) { return generateWithScale(texture, model, 1.5F); }

    public static Consumer<RenderStarter> generateWithScale(ResourceLocation texture, IModelCustomOld model, float scale) {
        return x -> {
            x.poseStack().scale(scale, scale, scale);
            VertexConsumer consumer = x.bufferSource().getBuffer(RenderType.entityCutout(texture));
            model.renderAll(x.poseStack(), consumer, x.packedLight(), x.packedOverlay());
        };
    }

    public static void init() {

        renderers.put(ModItems.MISSILE_TAINT.get(), generateStandard(ResourceManager.MISSILE_MICRO_TAINT_TEX, ResourceManager.missileMicro));
        renderers.put(ModItems.MISSILE_MICRO.get(), generateStandard(ResourceManager.MISSILE_MICRO_TEX, ResourceManager.missileMicro));
        renderers.put(ModItems.MISSILE_BHOLE.get(), generateStandard(ResourceManager.MISSILE_MICRO_BHOLE_TEX, ResourceManager.missileMicro));
        renderers.put(ModItems.MISSILE_SCHRABIDIUM.get(), generateStandard(ResourceManager.MISSILE_MICRO_SCHRABIDIUM_TEX, ResourceManager.missileMicro));
        renderers.put(ModItems.MISSILE_EMP.get(), generateStandard(ResourceManager.MISSILE_MICRO_EMP_TEX, ResourceManager.missileMicro));

        renderers.put(ModItems.MISSILE_GENERIC.get(), generateStandard(ResourceManager.MISSILE_V2_HE_TEX, ResourceManager.missileV2));
        renderers.put(ModItems.MISSILE_DECOY.get(), generateStandard(ResourceManager.MISSILE_V2_DECOY_TEX, ResourceManager.missileV2));
        renderers.put(ModItems.MISSILE_INCENDIARY.get(), generateStandard(ResourceManager.MISSILE_V2_IN_TEX, ResourceManager.missileV2));
        renderers.put(ModItems.MISSILE_CLUSTER.get(), generateStandard(ResourceManager.MISSILE_V2_CL_TEX, ResourceManager.missileV2));
        renderers.put(ModItems.MISSILE_BUSTER.get(), generateStandard(ResourceManager.MISSILE_V2_BU_TEX, ResourceManager.missileV2));

        renderers.put(ModItems.MISSILE_STEALTH.get(), x -> {
            VertexConsumer consumer = x.bufferSource().getBuffer(RenderType.entityCutout(ResourceManager.MISSILE_STEALTH_TEX));
            ResourceManager.missileStealth.renderAll(x.poseStack(), consumer, x.packedLight(), x.packedOverlay());
        });

        renderers.put(ModItems.MISSILE_STRONG.get(), generateLarge(ResourceManager.MISSILE_STRONG_HE_TEX, ResourceManager.missileStrong));
        renderers.put(ModItems.MISSILE_INCENDIARY_STRONG.get(), generateLarge(ResourceManager.MISSILE_STRONG_IN_TEX, ResourceManager.missileStrong));
        renderers.put(ModItems.MISSILE_CLUSTER_STRONG.get(), generateLarge(ResourceManager.MISSILE_STRONG_CL_TEX, ResourceManager.missileStrong));
        renderers.put(ModItems.MISSILE_BUSTER_STRONG.get(), generateLarge(ResourceManager.MISSILE_STRONG_BU_TEX, ResourceManager.missileStrong));
        renderers.put(ModItems.MISSILE_EMP_STRONG.get(), generateLarge(ResourceManager.MISSILE_STRONG_EMP_TEX, ResourceManager.missileStrong));

        renderers.put(ModItems.MISSILE_BURST.get(), generateStandard(ResourceManager.MISSILE_HUGE_HE_TEX, ResourceManager.missileHuge));
        renderers.put(ModItems.MISSILE_INFERNO.get(), generateStandard(ResourceManager.MISSILE_HUGE_IN_TEX, ResourceManager.missileHuge));
        renderers.put(ModItems.MISSILE_RAIN.get(), generateStandard(ResourceManager.MISSILE_HUGE_CL_TEX, ResourceManager.missileHuge));
        renderers.put(ModItems.MISSILE_DRILL.get(), generateStandard(ResourceManager.MISSILE_HUGE_BU_TEX, ResourceManager.missileHuge));

        renderers.put(ModItems.MISSILE_NUCLEAR.get(), generateStandard(ResourceManager.MISSILE_NUCLEAR_TEX, ResourceManager.missileNuclear));
        renderers.put(ModItems.MISSILE_NUCLEAR_CLUSTER.get(), generateStandard(ResourceManager.MISSILE_THERMO_TEX, ResourceManager.missileNuclear));
        renderers.put(ModItems.MISSILE_VOLCANO.get(), generateStandard(ResourceManager.MISSILE_VOLCANO_TEX, ResourceManager.missileNuclear));
        renderers.put(ModItems.MISSILE_DOOMSDAY.get(), generateStandard(ResourceManager.MISSILE_DOOMSDAY_TEX, ResourceManager.missileNuclear));
        renderers.put(ModItems.MISSILE_DOOMSDAY_RUSTED.get(), generateStandard(ResourceManager.MISSILE_DOOMSDAY_RUSTED_TEX, ResourceManager.missileNuclear));

        renderers.put(ModItems.MISSILE_SHUTTLE.get(), generateStandard(ResourceManager.MISSILE_SHUTTLE_TEX, ResourceManager.missileShuttle));
    }
}
