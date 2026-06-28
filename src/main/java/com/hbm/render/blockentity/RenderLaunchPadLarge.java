package com.hbm.render.blockentity;

import com.hbm.blockentity.bomb.LaunchPadLargeBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.items.weapon.MissileItem.MissileFormFactor;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.item.ItemRenderMissileGeneric;
import com.hbm.render.item.ItemRenderMissileGeneric.RocketModelData;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderLaunchPadLarge extends BlockEntityRendererNT<LaunchPadLargeBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<LaunchPadLargeBlockEntity> create(Context context) { return new RenderLaunchPadLarge(); }

    @Override
    public void render(LaunchPadLargeBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        RenderContext.setup(poseStack, packedLight, packedOverlay);
        RenderContext.translate(0.5F, 0F, 0.5F);

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case WEST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
            case EAST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
        }

        bindTexture(ResourceManager.MISSILE_ERECTOR_TEX);
        ResourceManager.missile_erector.renderPart("Pad");

        if(be.formFactor >= 0 && be.formFactor < MissileFormFactor.values().length) {

            MissileFormFactor formFactor = MissileFormFactor.values()[be.formFactor];
            String[] parts = null;
            float[] offset = null;

            switch(formFactor) {
                case ABM, OTHER -> {
                    parts = new String[] { "ABM_Pad", "ABM_Erector", "ABM_Pivot", "ABM_Rope" };
                    offset = new float[] { 1.5F, 1.25F };
                    bindTexture(ResourceManager.MISSILE_ERECTOR_ABM_TEX);
                }
                case MICRO -> {
                    parts = new String[] { "Micro_Pad", "Micro_Erector", "Micro_Pivot", "Micro_Rope" };
                    offset = new float[] { 1.5F, 1.25F };
                    bindTexture(ResourceManager.MISSILE_ERECTOR_MICRO_TEX);
                }
                case V2 -> {
                    parts = new String[] { "V2_Pad", "V2_Erector", "V2_Pivot", "V2_Rope" };
                    offset = new float[] { 1.75F, 1.25F };
                    bindTexture(ResourceManager.MISSILE_ERECTOR_V2_TEX);
                }
                case STRONG -> {
                    parts = new String[] { "Strong_Pad", "Strong_Erector", "Strong_Pivot", "Strong_Rope" };
                    offset = new float[] { 3F, 1.5F };
                    bindTexture(ResourceManager.MISSILE_ERECTOR_STRONG_TEX);
                }
                case HUGE -> {
                    parts = new String[] { "Huge_Pad", "Huge_Erector", "Huge_Pivot", "Huge_Rope" };
                    offset = new float[] { 3F, 1.5F };
                    bindTexture(ResourceManager.MISSILE_ERECTOR_HUGE_TEX);
                }
                case ATLAS -> {
                    parts = new String[]{ "Atlas_Pad", "Atlas_Erector", "Atlas_Pivot", "Atlas_Rope" };
                    offset = new float[]{ 4F, 1.5F };
                    bindTexture(ResourceManager.MISSILE_ERECTOR_ATLAS_TEX);
                }
            }

            float erectorAngle = Mth.lerp(partialTicks, be.prevErector, be.erector);
            float erectorLift = Mth.lerp(partialTicks, be.prevLift, be.lift);

            RenderContext.pushPose();
            ResourceManager.missile_erector.renderPart(parts[0]);
            if(be.toRender.isEmpty() && be.erected) ResourceManager.missile_erector.renderPart(parts[3]);
            RenderContext.translate(0, offset[1], -offset[0]);
            RenderContext.mulPose(Axis.XP.rotationDegrees(-erectorAngle));
            RenderContext.translate(0, -offset[1], offset[0]);
            ResourceManager.missile_erector.renderPart(parts[2]);
            RenderContext.translate(0, erectorLift, 0);
            ResourceManager.missile_erector.renderPart(parts[1]);

            if(be.erected) {
                RenderContext.popPose();
                RenderContext.pushPose();
            }

            if(!be.toRender.isEmpty() && be.erected || be.readyToLoad) {
                poseStack.translate(0F, 2F, 0F);
                RocketModelData render = ItemRenderMissileGeneric.renderers.get(new ComparableStack(be.toRender).makeSingular());
                if(render != null) render.render();
            }

            RenderContext.popPose();
        }

        RenderContext.end();
    }

    private AABB bb = null;

    @Override
    public AABB getRenderBoundingBox(LaunchPadLargeBlockEntity be) {

        if(bb == null) {
            int x = be.getBlockPos().getX();
            int y = be.getBlockPos().getY();
            int z = be.getBlockPos().getZ();

            bb = new AABB(
                    x - 10,
                    y - 0,
                    z - 10,
                    x + 11,
                    y + 15,
                    z + 11
            );
        }

        return bb;
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.LAUNCH_PAD_LARGE.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -3.75F, 0F);
                RenderContext.scale(1.625F, 1.625F, 1.625F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(0.5F, 0.5F, 0.5F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));

                bindTexture(ResourceManager.MISSILE_ERECTOR_TEX);
                ResourceManager.missile_erector.renderPart("Pad");
                bindTexture(ResourceManager.MISSILE_ERECTOR_ATLAS_TEX);
                ResourceManager.missile_erector.renderPart("Atlas_Pad");
                ResourceManager.missile_erector.renderPart("Atlas_Erector");
                ResourceManager.missile_erector.renderPart("Atlas_Pivot");
            }
        };
    }
}
