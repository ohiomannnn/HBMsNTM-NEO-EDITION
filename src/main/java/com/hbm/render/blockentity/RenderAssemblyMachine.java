package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.MachineAssemblyMachineBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.recipes.AssemblyMachineRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.main.NuclearTechMod;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import com.hbm.util.BobMathUtil;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderAssemblyMachine extends BlockEntityRendererNT<MachineAssemblyMachineBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<MachineAssemblyMachineBlockEntity> create(Context context) { return new RenderAssemblyMachine(); }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void render(MachineAssemblyMachineBlockEntity be, MultiBufferSource buffer, float partialTicks) {
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(90F));

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        switch(facing) {
            case NORTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(0F));
            case WEST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            case SOUTH -> RenderContext.mulPose(Axis.YP.rotationDegrees(180F));
            case EAST ->  RenderContext.mulPose(Axis.YP.rotationDegrees(270F));
        }

        bindTexture(ResourceManager.ASSEMBLY_MACHINE_TEX);
        ResourceManager.assembly_machine.renderPart("Base");
        if(be.frame) ResourceManager.assembly_machine.renderPart("Frame");

        RenderContext.pushPose();

        float spin = BobMathUtil.interp(be.prevRing, be.ring, partialTicks);
        float[] arm1 = be.arms[0].getPositions(partialTicks);
        float[] arm2 = be.arms[1].getPositions(partialTicks);

        RenderContext.mulPose(Axis.YP.rotationDegrees(spin));
        ResourceManager.assembly_machine.renderPart("Ring");

        RenderContext.pushPose(); {
            RenderContext.translate(0F, 1.625F, 0.9375F);
            RenderContext.mulPose(Axis.XP.rotationDegrees(arm1[0]));
            RenderContext.translate(0F, -1.625F, -0.9375F);
            ResourceManager.assembly_machine.renderPart("ArmLower1");

            RenderContext.translate(0F, 2.375F, 0.9375F);
            RenderContext.mulPose(Axis.XP.rotationDegrees(arm1[1]));
            RenderContext.translate(0F, -2.375F, -0.9375F);
            ResourceManager.assembly_machine.renderPart("ArmUpper1");

            RenderContext.translate(0F, 2.375F, 0.4375F);
            RenderContext.mulPose(Axis.XP.rotationDegrees(arm1[2]));
            RenderContext.translate(0F, -2.375F, -0.4375F);
            ResourceManager.assembly_machine.renderPart("Head1");
            RenderContext.translate(0F, arm1[3], 0F);
            ResourceManager.assembly_machine.renderPart("Spike1");

        } RenderContext.popPose();

        RenderContext.pushPose(); {
            RenderContext.translate(0F, 1.625F, -0.9375F);
            RenderContext.mulPose(Axis.XP.rotationDegrees(-arm2[0]));
            RenderContext.translate(0F, -1.625F, 0.9375F);
            ResourceManager.assembly_machine.renderPart("ArmLower2");

            RenderContext.translate(0F, 2.375F, -0.9375F);
            RenderContext.mulPose(Axis.XP.rotationDegrees(-arm2[1]));
            RenderContext.translate(0F, -2.375F, 0.9375F);
            ResourceManager.assembly_machine.renderPart("ArmUpper2");

            RenderContext.translate(0F, 2.375F, -0.4375F);
            RenderContext.mulPose(Axis.XP.rotationDegrees(-arm2[2]));
            RenderContext.translate(0F, -2.375F, 0.4375F);
            ResourceManager.assembly_machine.renderPart("Head2");
            RenderContext.translate(0F, arm2[3], 0F);
            ResourceManager.assembly_machine.renderPart("Spike2");

        } RenderContext.popPose();

        RenderContext.popPose();

        GenericRecipe recipe = AssemblyMachineRecipes.INSTANCE.recipeNameMap.get(be.assemblerModule.recipe);
        if(recipe != null && NuclearTechMod.proxy.me().distanceToSqr(be.getBlockPos().getBottomCenter().add(0, 1, 0)) < 35 * 35) {

            RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
            RenderContext.translate(0F, 1.0625F, 0F);

            ItemStack stack = recipe.getIcon();

            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

            BakedModel model = renderer.getModel(stack, null, null, 0);

            if(model.isGui3d()) {
                RenderContext.translate(0F, 0.1F, 0F);
            } else {
                RenderContext.mulPose(Axis.XP.rotationDegrees(-90F));
            }
            RenderContext.scale(0.75F, 0.75F, 0.75F);

            renderer.render(stack, ItemDisplayContext.FIXED, false, RenderContext.poseStack(), buffer, RenderContext.light(), RenderContext.overlay(), model);
        }
    }

    @Override
    public int getPacketLight(int packedLight, MachineAssemblyMachineBlockEntity be) {
        if(be.getLevel() != null && be.getBlockState().getBlock() instanceof DummyableBlock dummy) {
            return LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(dummy.getDimensions()[0]));
        }
        return packedLight;
    }

    private AABB bb = null;

    @Override
    public AABB getRenderBoundingBox(MachineAssemblyMachineBlockEntity be) {

        if(bb == null) {
            int x = be.getBlockPos().getX();
            int y = be.getBlockPos().getY();
            int z = be.getBlockPos().getZ();

            bb = new AABB(x - 1, y, z - 1, x + 2, y + 3, z + 2);
        }

        return bb;
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_ASSEMBLY_MACHINE.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -2.75F, 0F);
                RenderContext.scale(4.5F, 4.5F, 4.5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.scale(0.75F, 0.75F, 0.75F);
                bindTexture(ResourceManager.ASSEMBLY_MACHINE_TEX);
                ResourceManager.assembly_machine.renderAll();
            }
        };
    }
}
