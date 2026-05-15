package com.hbm.render.blockentity;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.generic.PlushieBlock;
import com.hbm.blocks.generic.PlushieBlock.PlushieBlockEntity;
import com.hbm.blocks.generic.PlushieBlock.PlushieType;
import com.hbm.items.NtmItems;
import com.hbm.main.NuclearTechMod;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.IModelCustom;
import com.hbm.render.util.HorsePronter;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class RenderPlushie extends BlockEntityRendererNT<PlushieBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<PlushieBlockEntity> create(Context context) {

        yomiModel = new HFRWavefrontObject("models/obj/trinkets/yomi.obj").asVBO();
        hundunModel = new HFRWavefrontObject("models/obj/trinkets/hundun.obj").asVBO();
        dergModel = new HFRWavefrontObject("models/obj/trinkets/derg.obj").asVBO();

        return new RenderPlushie();
    }

    public static IModelCustom yomiModel;
    public static IModelCustom hundunModel;
    public static IModelCustom dergModel;

    public static final ResourceLocation YOMI_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/yomi.png");
    public static final ResourceLocation NUMBERNINE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/horse/numbernine.png");
    public static final ResourceLocation HUNDUN_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/hundun.png");
    public static final ResourceLocation DERG_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/derg.png");

    @Override
    public void render(PlushieBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        RenderContext.setup(poseStack, packedLight, packedOverlay);
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YN.rotationDegrees((float) (22.5D * be.getBlockState().getValue(PlushieBlock.DIRECTION) + 90)));

        if (be.squishTimer > 0) {
            double squish = be.squishTimer - partialTicks;
            RenderContext.scale(1F,  (float) (1F + (-(Math.sin(squish)) * squish) * 0.025F), 1F);
        }

        switch (be.type) {
            case YOMI -> poseStack.scale(0.5F, 0.5F, 0.5F);
            case NUMBERNINE -> poseStack.scale(0.75F, 0.75F, 0.75F);
            case HUNDUN -> poseStack.scale(1F, 1F, 1F);
        }

        renderPlushie(be.type, buffer, be.squishTimer > 0);

        RenderContext.end();
    }

    public static void renderPlushie(PlushieType type, MultiBufferSource buffer, boolean squish) {

        switch (type) {
            case YOMI -> {
                RenderSystem.setShaderTexture(0, YOMI_TEX);
                yomiModel.renderAll();
            }
            case NUMBERNINE -> {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.mulPose(Axis.XN.rotationDegrees(15));
                RenderContext.translate(0F, -0.25F, 0.75F);
                HorsePronter.reset();
                float r = 45F;
                HorsePronter.pose(HorsePronter.id_body, 0, -r, 0);
                HorsePronter.pose(HorsePronter.id_tail, 0, 60, 90);
                HorsePronter.pose(HorsePronter.id_lbl, 0, -75 + r, 35);
                HorsePronter.pose(HorsePronter.id_rbl, 0, -75 + r, -35);
                HorsePronter.pose(HorsePronter.id_lfl, 0, r - 25, 5);
                HorsePronter.pose(HorsePronter.id_rfl, 0, r - 25, -5);
                HorsePronter.pose(HorsePronter.id_head, 0, r + 15, 0);
                RenderSystem.setShaderTexture(0, NUMBERNINE_TEX);
                HorsePronter.pront();
                RenderContext.mulPose(Axis.XP.rotationDegrees(15F));

                RenderContext.pushPose();
                RenderContext.translate(0F, 1F, -0.6875F);
                RenderSystem.disableCull();
                float s = 1.125F;
                RenderContext.scale(0.0625F * s, 0.0625F * s, 0.0625F * s);
                RenderContext.mulPose(Axis.XP.rotationDegrees(180F));
                RenderSystem.setShaderTexture(0, ResourceManager.NO9);
                ResourceManager.armor_no9.renderPart("Helmet");
                RenderSystem.setShaderTexture(0, ResourceManager.NO9_INSIGNIA);
                ResourceManager.armor_no9.renderPart("Insignia");
                RenderContext.popPose();
                RenderSystem.enableCull();

                ItemStack stack = new ItemStack(NtmItems.CIGARETTE.get());
                float scale = 0.25F;
                RenderContext.translate(-0.06F, 1.13F, -0.28F);
                RenderContext.scale(scale, scale, scale);
                RenderContext.mulPose(Axis.YN.rotationDegrees(90F));
                RenderContext.mulPose(Axis.ZN.rotationDegrees(60F));
                Minecraft mc = Minecraft.getInstance();
                ItemRenderer itemRenderer = mc.getItemRenderer();
                BakedModel model = itemRenderer.getModel(stack, null, null, 0);
                itemRenderer.render(stack, ItemDisplayContext.FIXED, false, RenderContext.poseStack(), buffer, RenderContext.light(), RenderContext.overlay(), model);
            }
            case HUNDUN -> {
                RenderSystem.setShaderTexture(0, HUNDUN_TEX);
                hundunModel.renderPart("goober_posed");
            }
            case DERG -> {
                RenderSystem.setShaderTexture(0, DERG_TEX);
                dergModel.renderPart("Derg");
                dergModel.renderPart(squish ? "Blep" : "ColonThree");
            }
        }
    }

    @Override
    public Item[] getItemsForRenderer() {
        return new Item[] {
                NtmBlocks.PLUSHIE_YOMI.asItem(),
                NtmBlocks.PLUSHIE_NUMBERNINE.asItem(),
                NtmBlocks.PLUSHIE_HUNDUN.asItem(),
                NtmBlocks.PLUSHIE_DERG.asItem()
        };
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -6F, 0F);
                RenderContext.scale(6F, 6F, 6F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, 0.25F, 0F);

                PlushieType type = getType(stack);
                if(type == null) return;

                switch (type) {
                    case YOMI -> RenderContext.scale(1.25F, 1.25F, 1.25F);
                    case NUMBERNINE -> {
                        RenderContext.translate(0F, 0.25F, 0.25F);
                        RenderContext.scale(1.25F, 1.25F, 1.25F);
                    }
                    case HUNDUN -> {
                        RenderContext.translate(0.5F, 0.5F, 0);
                        RenderContext.scale(1.25F, 1.25F, 1.25F);
                    }
                    case DERG -> RenderContext.scale(1.5F, 1.5F, 1.5F);
                }

                renderPlushie(type, buffer, false);
            }
        };
    }

    @Nullable
    private static PlushieType getType(ItemStack stack) {
        if (stack.is(NtmBlocks.PLUSHIE_YOMI.asItem())) return PlushieType.YOMI;
        if (stack.is(NtmBlocks.PLUSHIE_NUMBERNINE.asItem())) return PlushieType.NUMBERNINE;
        if (stack.is(NtmBlocks.PLUSHIE_HUNDUN.asItem())) return PlushieType.HUNDUN;
        if (stack.is(NtmBlocks.PLUSHIE_DERG.asItem())) return PlushieType.DERG;
        return null;
    }
}
